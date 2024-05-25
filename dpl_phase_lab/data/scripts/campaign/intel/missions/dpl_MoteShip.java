package data.scripts.campaign.intel.missions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.CampaignEventListener.FleetDespawnReason;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.FleetEventListener;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin.DerelictShipData;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin.DerelictType;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.missions.hub.HubMissionWithBarEvent;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner.ShipRecoverySpecialCreator;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial.PerShipData;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial.ShipCondition;
import com.fs.starfarer.api.ui.SectorMapAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static com.fs.starfarer.api.impl.campaign.ids.FleetTypes.PATROL_LARGE;

public class dpl_MoteShip extends HubMissionWithBarEvent implements FleetEventListener {

    // time we have to complete the mission
    public static float MISSION_DAYS = 120f;

    // mission stages
    public static enum Stage {
        SCAN_LIGHT,
        GO_TO_RSV,
        WAIT_FOR_SHIP,
        KILL_FLEET,
        RETURN_TO_RSV,
        COMPLETED,
    }

    // important objects, systems and people
    protected CampaignFleetAPI target;
    protected SectorEntityToken clarinet;
    protected PersonAPI merc;
    protected StarSystemAPI system;
    protected StarSystemAPI system2;

    // run when the bar event starts / when we ask a contact about the mission
    protected boolean create(MarketAPI createdAt, boolean barEvent) {

        PersonAPI person = getPerson();
        if (person == null) return false;
        
        MarketAPI market = person.getMarket();
        if (market == null) return false;
        if (!market.getFactionId().equals("dpl_phase_lab")) return false;

        // setting the mission ref allows us to use the Call rulecommand in their dialogues, so that we can make this script do things
        if (!setPersonMissionRef(person, "$dpl_mtship_ref")) {
            return false;
        }

        // set up the disgraced executive
        merc = Global.getSector().getFaction(Factions.MERCENARY).createRandomPerson();
        merc.setRankId(Ranks.SPACE_CAPTAIN);
        merc.setPostId(Ranks.POST_FLEET_COMMANDER);
        merc.getMemoryWithoutUpdate().set("$dpl_mtship_merc", true);

        // pick the system of the merc.
        requireSystemInterestingAndNotUnsafeOrCore();
        preferSystemInInnerSector();
        preferSystemUnexplored();
        preferSystemInDirectionOfOtherMissions();

        system = person.getMarket().getStarSystem();
        if (system == null) return false;

        system2 = pickSystem(true);
        if (system2 == null) return false;

        beginStageTrigger(Stage.KILL_FLEET);
        triggerRunScriptAfterDelay(0, new Script() {
			@Override
			public void run() {
				spawn_merc_fleet();
			}
		});
        endTrigger(); 

        // set a global reference we can use, useful for once-off missions.
        if (!setGlobalReference("$dpl_mtship_ref")) return false;

        // set our starting, success and failure stages
        setStartingStage(Stage.SCAN_LIGHT);
        setSuccessStage(Stage.COMPLETED);

        // set stage transitions when certain global flags are set, and when certain flags are set on the questgiver
        setStageOnGlobalFlag(Stage.GO_TO_RSV, "$dpl_mtship_scanned");
        makeImportant(market, "$dpl_mtship", Stage.GO_TO_RSV);
		makeImportant(person, "$dpl_mtship", Stage.GO_TO_RSV);
		setStageOnGlobalFlag(Stage.WAIT_FOR_SHIP, "$dpl_mtshipAskedElizaShip");
        makeImportant(market, "$dpl_mtship", Stage.WAIT_FOR_SHIP);
		makeImportant(person, "$dpl_mtship", Stage.WAIT_FOR_SHIP);
		setStageOnGlobalFlag(Stage.KILL_FLEET, "$dpl_mtship_AskedPrototype");
		setStageOnMemoryFlag(Stage.RETURN_TO_RSV, person, "$dpl_mtship_killed");
        makeImportant(market, "$dpl_mtship", Stage.RETURN_TO_RSV);
		makeImportant(person, "$dpl_mtship", Stage.RETURN_TO_RSV);
		setStageOnGlobalFlag(Stage.COMPLETED, "$dpl_mtship_completed");
        // set time limit and credit reward
        setCreditReward(200000);

        return true;
    }

    // during the initial dialogue and in any dialogue where we use "Call $dpl_mtship_ref updateData", these values will be put in memory
    // here, used so we can, say, type $dpl_mtship_execName and automatically insert the disgraced executive's name
    protected void updateInteractionDataImpl() {
        set("$dpl_mtship_barEvent", isBarEvent());
        set("$dpl_mtship_manOrWoman", getPerson().getManOrWoman());
        set("$dpl_mtship_heOrShe", getPerson().getHeOrShe());
        set("$dpl_mtship_reward", Misc.getWithDGS(getCreditsReward()));

        set("$dpl_mtship_personName", getPerson().getNameString());
        set("$dpl_mtship_mercName", merc.getNameString());
        set("$dpl_mtship_systemName", system.getNameWithLowercaseTypeShort());
        set("$dpl_mtship_system2Name", system2.getNameWithLowercaseTypeShort());
        set("$dpl_mtship_dist", getDistanceLY(system2));
    }
    
    public void spawn_merc_fleet(){
    	// set up the target fleet. I've done this using the old style, because the trigger-system doesn't support event listeners by default,
        // and we need to know when this fleet dies or despawns
        FleetParamsV3 params = new FleetParamsV3(
                null,
                null,
                Factions.TRITACHYON,
                null,
                PATROL_LARGE,
                160f, // combatPts
                20f, // freighterPts
                20f, // tankerPts
                0f, // transportPts
                0f, // linerPts
                0f, // utilityPts
                1.25f // qualityMod
        );
        target = FleetFactoryV3.createFleet(params);
        target.getFleetData().addFleetMember("dpl_panpipes_defence");

        target.setName(merc.getNameString() + "'s Salvage Fleet");
        target.setNoFactionInName(true);

        target.setCommander(merc);
        target.getFlagship().setCaptain(merc);

        Misc.makeHostile(target);
        Misc.makeNoRepImpact(target, "$dpl_mtship");
        Misc.makeImportant(target, "$dpl_mtship");

        target.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_MAKE_AGGRESSIVE, "$dpl_mtship");
        target.getMemoryWithoutUpdate().set(MemFlags.FLEET_IGNORED_BY_OTHER_FLEETS, "$dpl_mtship");
        target.getMemoryWithoutUpdate().set(MemFlags.FLEET_IGNORES_OTHER_FLEETS, "$dpl_mtship");

        target.getMemoryWithoutUpdate().set("$dpl_mtship_mercfleet", true);
        target.getAI().addAssignment(FleetAssignment.PATROL_SYSTEM, system2.getCenter(), 200f, null);
        target.addEventListener(this);
        system2.addEntity(target);
    }

    // used to detect when the executive's fleet is destroyed and complete the mission
    public void reportBattleOccurred(CampaignFleetAPI fleet, CampaignFleetAPI primaryWinner, BattleAPI battle) {
        if (isDone() || result != null) return;

        // also credit the player if they're in the same location as the fleet and nearby
        float distToPlayer = Misc.getDistance(fleet, Global.getSector().getPlayerFleet());
        boolean playerInvolved = battle.isPlayerInvolved() || (fleet.isInCurrentLocation() && distToPlayer < 2000f);

        if (battle.isInvolved(fleet) && !playerInvolved) {
            if (fleet.getFlagship() == null || fleet.getFlagship().getCaptain() != merc) {
                fleet.setCommander(fleet.getFaction().createRandomPerson());
                getPerson().getMemoryWithoutUpdate().set("$dpl_mtship_killed", true);
                return;
            }
        }

        if (!playerInvolved || !battle.isInvolved(fleet) || battle.onPlayerSide(fleet)) {
            return;
        }

        // didn't destroy the original flagship
        if (fleet.getFlagship() != null && fleet.getFlagship().getCaptain() == merc) return;

        getPerson().getMemoryWithoutUpdate().set("$dpl_mtship_killed", true);

    }

    // description when selected in intel screen
    @Override
    public void addDescriptionForNonEndStage(TooltipMakerAPI info, float width, float height) {
        float opad = 10f;
        Color h = Misc.getHighlightColor();
        if (currentStage == Stage.SCAN_LIGHT) {
            info.addPara("Find an abyssal light and scan it with Eliza's device.", opad);
        } else if (currentStage == Stage.GO_TO_RSV) {
            info.addPara("Report to Research Site V with the data in " +
                    system.getNameWithLowercaseTypeShort() + ".", opad);
        } else if (currentStage == Stage.WAIT_FOR_SHIP) {
            info.addPara("Wait for Eliza to complete the prototype, she is in " +
                    system.getNameWithLowercaseTypeShort() + ".", opad);
        } else if (currentStage == Stage.KILL_FLEET) {
            info.addPara("Find and take the hijacked ship in the " +
                    system2.getNameWithLowercaseTypeShort() + ".", opad);
        } else if (currentStage == Stage.RETURN_TO_RSV) {
            info.addPara("Report to Research Site V in " +
                    system.getNameWithLowercaseTypeShort() + ".", opad);
        }
        if (isDevMode()) {
            info.addPara("DEVMODE: MERC IS LOCATED IN THE " +
                    system2.getNameWithLowercaseTypeShort() + ".", opad);
        }
    }

    // short description in popups and the intel entry
    @Override
    public boolean addNextStepText(TooltipMakerAPI info, Color tc, float pad) {
        Color h = Misc.getHighlightColor();
        if (currentStage == Stage.SCAN_LIGHT) {
            info.addPara("Look for an abyssal light.", tc, pad);
            return true;
        } else if (currentStage == Stage.GO_TO_RSV) {
            info.addPara("Go to Research Site V.", tc, pad);
            return true;
        } else if (currentStage == Stage.WAIT_FOR_SHIP) {
            info.addPara("Wait for the ship at Research Site V", tc, pad);
            return true;
        } else if (currentStage == Stage.KILL_FLEET) {
            info.addPara("Find the hijacked ship in " +
                    system2.getNameWithLowercaseTypeShort(), tc, pad);
            return true;
        } else if (currentStage == Stage.RETURN_TO_RSV) {
            info.addPara("Go to Research Site V.", tc, pad);
            return true;
        }
        return false;
    }

    // where on the map the intel screen tells us to go
    @Override
    public SectorEntityToken getMapLocation(SectorMapAPI map) {
        if (currentStage == Stage.GO_TO_RSV) {
            return getMapLocationFor(system.getCenter());
        } else if (currentStage == Stage.WAIT_FOR_SHIP) {
            return getMapLocationFor(system.getCenter());
        } else if (currentStage == Stage.KILL_FLEET) {
            return getMapLocationFor(system2.getCenter());
        } else if (currentStage == Stage.RETURN_TO_RSV) {
            return getMapLocationFor(system.getCenter());
        }
        return null;
    }

    // mission name
    @Override
    public String getBaseName() {
        return "Mote Anomaly Project";
    }

	@Override
	public void reportFleetDespawnedToListener(CampaignFleetAPI fleet, FleetDespawnReason reason, Object param) {
		// TODO Auto-generated method stub
		
	}
}
