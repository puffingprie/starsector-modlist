package data.campaign.econ.industries;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.DebugFlags;
import static com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry.getDeficitText;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;

import static com.fs.starfarer.api.impl.campaign.econ.impl.GroundDefenses.DEFENSE_BONUS_BATTERIES;
import static com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase.DEFENSE_BONUS_MILITARY;
import static com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase.DEFENSE_BONUS_PATROL;
import static com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase.createPatrol;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.impl.campaign.fleets.PatrolAssignmentAIV4;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.util.Misc;
import data.campaign.econ.GMDA_industries;

import java.util.Random;

// TODO: Contains stuff to be checked after a Starsector update is released
public class GMDA_NSHQ extends MilitaryBase
{
    // Copy of code from MilitaryBase.java; validate values on Starsector update
    protected static int[] getNumPatrolsForBase(int size, boolean patrol,
            boolean militaryBase, boolean command)
    {
        int light = 1, medium = 1, heavy = 2;

        if (patrol)
        {
            light = 2;
            medium = 0;
            heavy = 0;
        }
        else
        {
            if (size <= 3)
            {
                light = 2;
                medium = 0;
                heavy = 0;
            }
            else if (size == 4)
            {
                light = 2;
                medium = 0;
                heavy = 0;
            }
            else if (size == 5)
            {
                light = 2;
                medium = 1;
                heavy = 0;
            }
            else if (size == 6)
            {
                light = 3;
                medium = 1;
                heavy = 0;
            }
            else if (size == 7)
            {
                light = 3;
                medium = 2;
                heavy = 0;
            }
            else if (size == 8)
            {
                light = 3;
                medium = 3;
                heavy = 0;
            }
            else if (size >= 9)
            {
                light = 4;
                medium = 3;
                heavy = 0;
            }
        }

        if (militaryBase || command)
        {
            // Light++;
            medium = Math.max(medium + 1, size / 2 - 1);
            heavy = Math.max(heavy, medium - 1);
        }

        if (command)
        {
            medium++;
            heavy++;
        }

        return new int[]
        {
            light, medium, heavy
        };
    }

    @Override
    public void apply()
    {
        updateSupplyAndDemandModifiers();

        updateIncomeAndUpkeep();

        applyAICoreModifiers();

        if (this instanceof MarketImmigrationModifier)
        {
            market.addTransientImmigrationModifier((MarketImmigrationModifier) this);
        }

        int size = market.getSize();

        int extraDemand = 1;

        // Number of patrols
        int light = 1, medium = 1, heavy = 2;
            if (size <= 3)
            {
                light = 2;
                medium = 0;
                heavy = 0;
            }
            else if (size == 4)
            {
                light = 2;
                medium = 0;
                heavy = 0;
            }
            else if (size == 5)
            {
                light = 2;
                medium = 1;
                heavy = 0;
            }
            else if (size == 6)
            {
                light = 3;
                medium = 1;
                heavy = 0;
            }
            else if (size == 7)
            {
                light = 3;
                medium = 2;
                heavy = 0;
            }
            else if (size == 8)
            {
                light = 3;
                medium = 3;
                heavy = 0;
            }
            else if (size >= 9)
            {
                light = 4;
                medium = 3;
                heavy = 0;
            }

        medium = Math.max(medium + 1, size / 2 - 1);
        heavy = Math.max(heavy, medium - 1);

        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).modifyFlat(getModId(), light);
        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_MEDIUM_MOD).modifyFlat(getModId(), medium);
        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_HEAVY_MOD).modifyFlat(getModId(), heavy);

        demand(Commodities.SUPPLIES, size - 1 + extraDemand);
        demand(Commodities.FUEL, size - 1 + extraDemand);
        demand(Commodities.SHIPS, size - 1 + extraDemand);
        demand(Commodities.CREW, size - 1 + extraDemand);
        demand(Commodities.MARINES, size - 2 + extraDemand);
        demand(Commodities.HAND_WEAPONS, size - 2 + extraDemand);

        modifyStabilityWithBaseMod();

        float mult = getDeficitMult(Commodities.SUPPLIES);
        String extra = "";
        if (mult != 1)
        {
            String com = getMaxDeficit(Commodities.SUPPLIES).one;
            extra = " (" + getDeficitText(com).toLowerCase() + ")";
        }

        // Ground defense mod
        float bonus = DEFENSE_BONUS_BATTERIES;

        if (bonus > 0)
        {
            market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD)
                    .modifyMult(getModId(), 1f + bonus * mult, getNameForModifier() + extra);
        }
        else
        {
            market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodify(getModId());
        }

        MemoryAPI memory = market.getMemoryWithoutUpdate();
        Misc.setFlagWithReason(memory, MemFlags.MARKET_PATROL, getModId(), true, -1);
        Misc.setFlagWithReason(memory, MemFlags.MARKET_MILITARY, getModId(), true, -1);

        if (!isFunctional())
        {
            supply.clear();
            unapply();
        }
    }


    // Same as vanilla except fixes http://fractalsoftworks.com/forum/index.php?topic=8558.msg258499#msg258499
    @Override
    public CampaignFleetAPI spawnFleet(RouteManager.RouteData route)
    {
        PatrolFleetData custom = (PatrolFleetData) route.getCustom();
        FleetFactory.PatrolType type = custom.type;

        Random random = route.getRandom();

        CampaignFleetAPI fleet = createPatrol(type, "gmda", route, market, null, random);

        if (fleet == null || fleet.isEmpty())
        {
            return null;
        }

        fleet.addEventListener(this);

        market.getContainingLocation().addEntity(fleet);
        fleet.setFacing((float) Math.random() * 360f);
        // This will get overridden by the patrol assignment AI, depending on route-time elapsed etc
        fleet.setLocation(market.getPrimaryEntity().getLocation().x, market.getPrimaryEntity().getLocation().y);

        fleet.addScript(new PatrolAssignmentAIV4(fleet, route));

        //market.getContainingLocation().addEntity(fleet);
        //fleet.setLocation(market.getPrimaryEntity().getLocation().x, market.getPrimaryEntity().getLocation().y);
        if (custom.spawnFP <= 0)
        {
            custom.spawnFP = fleet.getFleetPoints();
        }

        return fleet;
    }

    @Override
    public boolean isAvailableToBuild() {
        boolean hasGMDA = false;
        FactionAPI player = Global.getSector().getFaction(Factions.PLAYER);
        RepLevel level = player.getRelationshipLevel(Global.getSector().getFaction("gmda"));

        if (!level.isAtWorst(RepLevel.INHOSPITABLE)) {
            hasGMDA = true;
        }

        if (market.getPlanetEntity() != null && (market.hasIndustry(GMDA_industries.CONDOT))) {
            hasGMDA = true;
        }

        if (hasGMDA) {
            return false;
        } else if (!hasGMDA) {
            return true;
        }

        return false;
    }

    @Override
    public String getUnavailableReason() {
        if (!super.isAvailableToBuild()) return super.getUnavailableReason();
        return "Either another Mercenary outpost is already present, or you are hostile to the GMDA.";
    }

    @Override
    public boolean showWhenUnavailable()
    {
        return Global.getSector().getPlayerFaction().knowsIndustry(getId());
    }

    @Override
    public String getCurrentImage()
    {
        return getSpec().getImageName();
    }
}