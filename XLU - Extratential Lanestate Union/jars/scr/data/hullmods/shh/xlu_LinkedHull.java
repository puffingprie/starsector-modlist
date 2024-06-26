package data.hullmods.shh;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import data.scripts.everyframe.xlu_BlockedHullmodDisplayScript;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class xlu_LinkedHull extends BaseHullMod {

    private static void advanceChild(ShipAPI child, ShipAPI parent) {
        ShipEngineControllerAPI ec = parent.getEngineController();
        if (ec != null) {
            if (parent.isAlive()) {
                if (ec.isAccelerating()) {
                    child.giveCommand(ShipCommand.ACCELERATE, null, 0);
                }
                if (ec.isAcceleratingBackwards()) {
                    child.giveCommand(ShipCommand.ACCELERATE_BACKWARDS, null, 0);
                }
                if (ec.isDecelerating()) {
                    child.giveCommand(ShipCommand.DECELERATE, null, 0);
                }
                if (ec.isStrafingLeft()) {
                    child.giveCommand(ShipCommand.STRAFE_LEFT, null, 0);
                }
                if (ec.isStrafingRight()) {
                    child.giveCommand(ShipCommand.STRAFE_RIGHT, null, 0);
                }
                if (ec.isTurningLeft()) {
                    child.giveCommand(ShipCommand.TURN_LEFT, null, 0);
                }
                if (ec.isTurningRight()) {
                    child.giveCommand(ShipCommand.TURN_RIGHT, null, 0);
                }
            }

            ShipEngineControllerAPI cec = child.getEngineController();
            if (cec != null) {
                if ((ec.isFlamingOut() || ec.isFlamedOut()) && !cec.isFlamingOut() && !cec.isFlamedOut()) {
                    child.getEngineController().forceFlameout(true);
                }
            }
        }

        /* Mirror parent's fighter commands */
        if (child.hasLaunchBays()) {
            if (child.isPullBackFighters() ^ parent.isPullBackFighters()) {
                child.giveCommand(ShipCommand.PULL_BACK_FIGHTERS, null, 0);
            }
            if (child.getAIFlags() != null) {
                if (((Global.getCombatEngine().getPlayerShip() == parent) || (parent.getAIFlags() == null))
                        && (parent.getShipTarget() != null)) {
                    child.getAIFlags().setFlag(AIFlags.CARRIER_FIGHTER_TARGET, 1f, parent.getShipTarget());
                } else if ((parent.getAIFlags() != null)
                        && parent.getAIFlags().hasFlag(AIFlags.CARRIER_FIGHTER_TARGET)
                        && (parent.getAIFlags().getCustom(AIFlags.CARRIER_FIGHTER_TARGET) != null)) {
                    child.getAIFlags().setFlag(AIFlags.CARRIER_FIGHTER_TARGET, 1f, parent.getAIFlags().getCustom(AIFlags.CARRIER_FIGHTER_TARGET));
                }
            }
        }
//        if (parent.isHulk() && parent.getHullSpec().getBaseHullId().contentEquals("swp_wall")) {
//            child.setParentStation(null);
//            child.setStationSlot(null);
//        }
    }

    private static void advanceParent(ShipAPI parent, List<ShipAPI> children) {
        ShipEngineControllerAPI ec = parent.getEngineController();
        if (ec != null) {
            float originalMass;
            int originalEngines;
            switch (parent.getHullSpec().getBaseHullId()) {
                default:
                case "xlu_rhodochrosite":
                    originalMass = 9000f;
                    originalEngines = 20;
                    break;
                case "xlu_azurim":
                    originalMass = 2400f;
                    originalEngines = 12;
                    break;
            }
            float thrustPerEngine = originalMass / originalEngines;

            /* Don't count parent's engines for this stuff - game already affects stats */
            float workingEngines = ec.getShipEngines().size();
            for (ShipAPI child : children) {
                if ((child.getParentStation() == parent) && (child.getStationSlot() != null) && child.isAlive()) {
                    ShipEngineControllerAPI cec = child.getEngineController();
                    if (cec != null) {
                        float contribution = 0f;
                        for (ShipEngineAPI ce : cec.getShipEngines()) {
                            if (ce.isActive() && !ce.isDisabled() && !ce.isPermanentlyDisabled() && !ce.isSystemActivated()) {
                                contribution += ce.getContribution();
                            }
                        }
                        workingEngines += cec.getShipEngines().size() * contribution;
                    }
                }
            }

            float thrust = workingEngines * thrustPerEngine;
            float enginePerformance = thrust / Math.max(1f, parent.getMassWithModules());
            parent.getMutableStats().getAcceleration().modifyMult("xlu_LinkedHull", enginePerformance);
            parent.getMutableStats().getDeceleration().modifyMult("xlu_LinkedHull", enginePerformance);
            parent.getMutableStats().getTurnAcceleration().modifyMult("xlu_LinkedHull", enginePerformance);
            parent.getMutableStats().getMaxTurnRate().modifyMult("xlu_LinkedHull", enginePerformance);
            parent.getMutableStats().getMaxSpeed().modifyMult("xlu_LinkedHull", enginePerformance);
            parent.getMutableStats().getZeroFluxSpeedBoost().modifyMult("xlu_LinkedHull", enginePerformance);
        }
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        ShipAPI parent = ship.getParentStation();
        if (parent != null) {
            advanceChild(ship, parent);
        }

        List<ShipAPI> children = ship.getChildModulesCopy();
        if (children != null && !children.isEmpty()) {
            advanceParent(ship, children);
        }
    }

    private static final Set<String> BLOCKED_OMNI = new HashSet<>();
    private static final Set<String> BLOCKED_OTHER = new HashSet<>();
    private static final Set<String> BLOCKED_OTHER_PLAYER_ONLY = new HashSet<>();

    static {
        //BLOCKED_FRONT.add("frontemitter");
        //BLOCKED_FRONT.add("frontshield");

        /* Not designed for omni arcs */
        BLOCKED_OMNI.add("adaptiveshields");

        /* Modules don't move on their own */
        BLOCKED_OTHER.add("auxiliarythrusters");
        //BLOCKED_OTHER.add("unstable_injector");

        /* Module's can't provide ECM/Nav */
        BLOCKED_OTHER.add("ecm");
        BLOCKED_OTHER.add("nav_relay");

        /* Logistics mods partially or completely don't apply on modules */
        BLOCKED_OTHER.add("operations_center");
        BLOCKED_OTHER.add("recovery_shuttles");
        BLOCKED_OTHER.add("additional_berthing");
        BLOCKED_OTHER.add("augmentedengines");
        BLOCKED_OTHER.add("auxiliary_fuel_tanks");
        BLOCKED_OTHER.add("efficiency_overhaul");
        BLOCKED_OTHER.add("expanded_cargo_holds");
        BLOCKED_OTHER.add("hiressensors");
        //BLOCKED_OTHER.add("insulatedengine"); // Niche use
        //BLOCKED_OTHER.add("solar_shielding"); // Niche use
        BLOCKED_OTHER.add("surveying_equipment");

        /* Crew penalty doesn't reflect in campaign */
        BLOCKED_OTHER_PLAYER_ONLY.add("converted_hangar");
        BLOCKED_OTHER_PLAYER_ONLY.add("expanded_deck_crew");
        BLOCKED_OTHER_PLAYER_ONLY.add("TSC_converted_hangar");
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        switch (ship.getHullSpec().getBaseHullId()) {
            case "xlu_rhodochrosite_e_l":
                for (String tmp : BLOCKED_OMNI) {
                    if (ship.getVariant().getHullMods().contains(tmp)) {
                        ship.getVariant().removeMod(tmp);
                        xlu_BlockedHullmodDisplayScript.showBlocked(ship);
                    }
                }
                break;
            case "xlu_rhodochrosite_e_r":
                for (String tmp : BLOCKED_OMNI) {
                    if (ship.getVariant().getHullMods().contains(tmp)) {
                        ship.getVariant().removeMod(tmp);
                        xlu_BlockedHullmodDisplayScript.showBlocked(ship);
                    }
                }
                break;
            case "xlu_rhodochrosite_rear":
                for (String tmp : BLOCKED_OMNI) {
                    if (ship.getVariant().getHullMods().contains(tmp)) {
                        ship.getVariant().removeMod(tmp);
                        xlu_BlockedHullmodDisplayScript.showBlocked(ship);
                    }
                }
                break;
            default:
                break;
        }
        switch (ship.getHullSpec().getBaseHullId()) {
            case "xlu_rhodochrosite_e_l":
                for (String tmp : BLOCKED_OTHER) {
                    if (ship.getVariant().getHullMods().contains(tmp)) {
                        ship.getVariant().removeMod(tmp);
                        xlu_BlockedHullmodDisplayScript.showBlocked(ship);
                    }
                }
                break;
            case "xlu_rhodochrosite_e_r":
                for (String tmp : BLOCKED_OTHER) {
                    if (ship.getVariant().getHullMods().contains(tmp)) {
                        ship.getVariant().removeMod(tmp);
                        xlu_BlockedHullmodDisplayScript.showBlocked(ship);
                    }
                }
                break;
            case "xlu_rhodochrosite_rear":
                for (String tmp : BLOCKED_OTHER) {
                    if (ship.getVariant().getHullMods().contains(tmp)) {
                        ship.getVariant().removeMod(tmp);
                        xlu_BlockedHullmodDisplayScript.showBlocked(ship);
                    }
                }
                break;
            default:
                break;
        }
        switch (ship.getHullSpec().getBaseHullId()) {
            case "xlu_rhodochrosite_e_l":
                for (String tmp : BLOCKED_OTHER_PLAYER_ONLY) {
                    if (ship.getVariant().getHullMods().contains(tmp)) {
                        ship.getVariant().removeMod(tmp);
                        xlu_BlockedHullmodDisplayScript.showBlocked(ship);
                    }
                }
                break;
            case "xlu_rhodochrosite_e_r":
                for (String tmp : BLOCKED_OTHER_PLAYER_ONLY) {
                    if (ship.getVariant().getHullMods().contains(tmp)) {
                        ship.getVariant().removeMod(tmp);
                        xlu_BlockedHullmodDisplayScript.showBlocked(ship);
                    }
                }
                break;
            case "xlu_rhodochrosite_rear":
                for (String tmp : BLOCKED_OTHER_PLAYER_ONLY) {
                    if (ship.getVariant().getHullMods().contains(tmp)) {
                        ship.getVariant().removeMod(tmp);
                        xlu_BlockedHullmodDisplayScript.showBlocked(ship);
                    }
                }
                break;
            default:
                break;
        }
    }
}
