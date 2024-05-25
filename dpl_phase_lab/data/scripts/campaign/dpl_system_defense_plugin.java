//By VladimirVV. Spawns lore-friendly fleets, and gives some lore-friendly buffs to the system defense.
package data.scripts.campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BattleAPI;
import com.fs.starfarer.api.campaign.CampaignEventListener.FleetDespawnReason;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.comm.CommMessageAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.FleetEventListener;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;

import static com.fs.starfarer.api.impl.campaign.ids.FleetTypes.PATROL_LARGE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.util.vector.Vector2f;

public class dpl_system_defense_plugin implements EveryFrameScript, FleetEventListener {

    //The ID for the faction that gets their relations adjusted (sorry, couldn't remember your faction ID off the top of my head)
    private static final String MAIN_FACTION = "dpl_phase_lab";
    private static int MAX_FLEET = 3;
    private static int MAX_FLEET_RSV = 4;
    private static int MAX_FLEET_ENEMY = 6;
    private MarketAPI lab_factory = null;
    private StarSystemAPI muspelheim = null;
    protected Set<CampaignFleetAPI> sysdefFleets = new HashSet<>();
    protected Set<CampaignFleetAPI> rsvFleets = new HashSet<>();
    protected Set<CampaignFleetAPI> enemyFleets = new HashSet<>();
    private int CURRENT_FLEET = 0;
    private int CURRENT_FLEET_RSV = 0;
    private boolean FLEET_SPAWNED = false;

    @Override
    public void advance( float amount ) {
        //Necessary Sector check
        SectorAPI sector = Global.getSector();
        if (sector == null) {
            return;
        }
        
        lab_factory = Global.getSector().getEconomy().getMarket("dpl_factory");
        if (lab_factory == null) {
        	return;
        }
        
        muspelheim = FindLargestMarket().getStarSystem();
        if (muspelheim == null) {
        	return;
        }
        
        Global.getSector().getMemoryWithoutUpdate().set("$dpl_enemy_current", enemyFleets.size());
        
        List<CampaignFleetAPI> allFleets = muspelheim.getFleets();
		for (CampaignFleetAPI fleets : allFleets) {
			if (fleets.getFaction() != null) {
				if (fleets.getFaction().isHostileTo(MAIN_FACTION) && !(fleets.isPlayerFleet())) {
					if (!(fleets.hasTag("dpl_marked_as_enemy"))) {
						fleets.addEventListener(this);
						fleets.addTag("dpl_marked_as_enemy");
						enemyFleets.add(fleets);
					}
				}
			}
		}

        //This situation should never happen. But if it happens, we don't want this code to break the game.
        if (sector.getFaction(MAIN_FACTION) == null) {
            return;
        }

        CURRENT_FLEET = sysdefFleets.size();
        CURRENT_FLEET_RSV = rsvFleets.size();
        FLEET_SPAWNED = Global.getSector().getMemoryWithoutUpdate().getBoolean("$dpl_sysdef_spawned");
        if (CURRENT_FLEET <= MAX_FLEET && !FLEET_SPAWNED) {
        	boolean success = spawnFleet();
        	if (success) {
        		Global.getSector().getMemoryWithoutUpdate().set("$dpl_sysdef_current", CURRENT_FLEET);
            	Global.getSector().getMemoryWithoutUpdate().set("$dpl_sysdef_spawned", true, 1);
        	}
        }
        
        if (CURRENT_FLEET_RSV <= MAX_FLEET_RSV && !FLEET_SPAWNED) {
        	boolean success_rsv = spawnFleetRSV();
        	if (success_rsv) {
        		Global.getSector().getMemoryWithoutUpdate().set("$dpl_rsv_current", CURRENT_FLEET_RSV);
            	Global.getSector().getMemoryWithoutUpdate().set("$dpl_sysdef_spawned", true, 1);
        	}
        }
        
    }
    
    protected MarketAPI FindLargestMarket() {
	    MarketAPI largestMarket = null;
		int size = 0;
		List<MarketAPI> allMarkets = Global.getSector().getEconomy().getMarketsCopy();
		for (MarketAPI market : allMarkets) {
			if (market.getFaction().equals(Global.getSector().getFaction("dpl_phase_lab"))) {
				if (market.getSize() >= size) {
					largestMarket = market;
					size = market.getSize();
				}
			}
		}
		return largestMarket;
    }
    
    protected Boolean spawnFleet() {
		
		MarketAPI source = FindLargestMarket();
		if (source == null) return false;
		SectorEntityToken planet = source.getPrimaryEntity();
		String name = source.getName();
		
		if (lab_factory == null) return false;
		SectorEntityToken lf_planet = lab_factory.getPrimaryEntity();
		String lf_name = lab_factory.getName();
		
		float combat = 240f;
		float tanker = 30f;
		float freighter = 30f;
		
		FleetParamsV3 params = new FleetParamsV3(
				null, 
				lab_factory.getLocation(),
				MAIN_FACTION, // quality will always be reduced by non-market-faction penalty, which is what we want 
				null,
				PATROL_LARGE,
				combat, // combatPts
				freighter, // freighterPts 
				tanker, // tankerPts
				0f, // transportPts
				0f, // linerPts
				0f, // utilityPts
				2f // qualityMod
				);
		
		CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);
		if (fleet == null || fleet.isEmpty()) return false;
		
		fleet.setName("Phase Lab Security Armada");
		fleet.setNoFactionInName(true);
		
		fleet.getFleetData().sort();
    	List<FleetMemberAPI> members = fleet.getFleetData().getMembersListCopy();
		for (FleetMemberAPI curr : members) {
			curr.getRepairTracker().setCR(curr.getRepairTracker().getMaxCR());
		}
		
		fleet.getMemoryWithoutUpdate().set(MemFlags.FLEET_MILITARY_RESPONSE, true);
		
		planet.getContainingLocation().addEntity(fleet);
		fleet.setLocation(planet.getLocation().x, planet.getLocation().y);
		
		fleet.addAssignment(FleetAssignment.ORBIT_PASSIVE, planet, 2f + (float) Math.random() * 2f,
								"preparing for patrol duty");
		
		fleet.addAssignment(FleetAssignment.GO_TO_LOCATION, lf_planet, 1000f,
					"going to " + lf_name);
		fleet.addAssignment(FleetAssignment.ORBIT_AGGRESSIVE, lf_planet, 90f,
				"ptrolling around " + lf_name);
		fleet.addAssignment(FleetAssignment.GO_TO_LOCATION, planet, 1000f,
				"going to " + name);
		fleet.addAssignment(FleetAssignment.ORBIT_PASSIVE, planet, 2f + 2f * (float) Math.random(),
					"standing down");
		fleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, planet, 1000);
		
		fleet.addEventListener(this);
		
		sysdefFleets.add(fleet);
		
		return true;
	}
    
protected Boolean spawnFleetRSV() {
		
		MarketAPI source = FindLargestMarket();
		if (source == null) return false;
		SectorEntityToken planet = source.getPrimaryEntity();
		String name = source.getName();
		
		float combat = 240f;
		float tanker = 30f;
		float freighter = 30f;
		
		FleetParamsV3 params = new FleetParamsV3(
				null, 
				lab_factory.getLocation(),
				MAIN_FACTION, // quality will always be reduced by non-market-faction penalty, which is what we want 
				null,
				PATROL_LARGE,
				combat, // combatPts
				freighter, // freighterPts 
				tanker, // tankerPts
				0f, // transportPts
				0f, // linerPts
				0f, // utilityPts
				2f // qualityMod
				);
		
		CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);
		if (fleet == null || fleet.isEmpty()) return false;
		
		fleet.setName("Phase Lab Security Armada");
		fleet.setNoFactionInName(true);
		
		fleet.getFleetData().sort();
    	List<FleetMemberAPI> members = fleet.getFleetData().getMembersListCopy();
		for (FleetMemberAPI curr : members) {
			curr.getRepairTracker().setCR(curr.getRepairTracker().getMaxCR());
		}
		
		fleet.getMemoryWithoutUpdate().set(MemFlags.FLEET_MILITARY_RESPONSE, true);
		
		planet.getContainingLocation().addEntity(fleet);
		fleet.setLocation(planet.getLocation().x, planet.getLocation().y);
			
		fleet.addAssignment(FleetAssignment.ORBIT_PASSIVE, planet, 2f + (float) Math.random() * 2f,
								"preparing for patrol duty");
		fleet.addAssignment(FleetAssignment.ORBIT_AGGRESSIVE, planet, 90f,
				"ptrolling around " + name);
		fleet.addAssignment(FleetAssignment.ORBIT_PASSIVE, planet, 2f + 2f * (float) Math.random(),
					"standing down");
		fleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, planet, 1000);
		
		fleet.addEventListener(this);
		
		rsvFleets.add(fleet);
		
		return true;
	}

    @Override
	public void reportFleetDespawnedToListener(CampaignFleetAPI fleet, FleetDespawnReason reason, Object param) {
    	if (isDone()) return;
        if (sysdefFleets.contains(fleet)) {
        	sysdefFleets.remove(fleet);
        	CURRENT_FLEET = sysdefFleets.size();
        	Global.getSector().getMemoryWithoutUpdate().set("$dpl_sysdef_current", CURRENT_FLEET);
		} else if (rsvFleets.contains(fleet)) {
        	rsvFleets.remove(fleet);
        	CURRENT_FLEET_RSV = rsvFleets.size();
        	Global.getSector().getMemoryWithoutUpdate().set("$dpl_rsv_current", CURRENT_FLEET_RSV);
		} else if (enemyFleets.contains(fleet)) {
			enemyFleets.remove(fleet);
		}
	}

	@Override
	public void reportBattleOccurred(CampaignFleetAPI fleet, CampaignFleetAPI primaryWinner, BattleAPI battle) {
		// TODO Auto-generated method stub
		int numEnemyFleets = enemyFleets.size();
		if (enemyFleets.contains(fleet)) {
			if (!(battle.isPlayerInvolved())) {
				if ((numEnemyFleets > MAX_FLEET_ENEMY) || (fleet.getFleetPoints() > 330)) {
					List<FleetMemberAPI> allMembers = fleet.getFleetData().getMembersListCopy();
					for (FleetMemberAPI ship : allMembers) {
						fleet.removeFleetMemberWithDestructionFlash(ship);
					}
				}
			}
		}
	}

    //We are never DONE.
    @Override
    public boolean isDone() {
        return false;
    }

    //No need to run while paused
    @Override
    public boolean runWhilePaused() {
        return false;
    }

}
