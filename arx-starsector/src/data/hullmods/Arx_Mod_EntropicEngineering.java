package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class Arx_Mod_EntropicEngineering extends BaseHullMod {
    public static float FLUX_CAPACITY_BONUS = 100f;
    public static float FLUX_DISSIPATION_BONUS = 100f;
    public static float HARD_FLUX_DISSIPATION_FRACTION_BONUS = 10f;
    public static float VENT_RATE_BONUS = 100f;
    public static float PEAK_CR_TIME_BONUS = 25f;
    public static float MAX_CR_BONUS = 15f;
    public static float SENSOR_BONUS = 25f;
    public static float SENSOR_PROFILE_BONUS = 25f;
    public static float CORONA_EFFECT_BONUS = 90f;

    public static final String ARX_AUTO_REPAIR = "arxAutoRepair";

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getFluxCapacity().modifyPercent(id, FLUX_CAPACITY_BONUS);
        stats.getFluxDissipation().modifyPercent(id, FLUX_DISSIPATION_BONUS);
        stats.getHardFluxDissipationFraction().modifyPercent(id, HARD_FLUX_DISSIPATION_FRACTION_BONUS);
        stats.getVentRateMult().modifyPercent(id, VENT_RATE_BONUS);
        stats.getPeakCRDuration().modifyPercent(id, PEAK_CR_TIME_BONUS);
        stats.getMaxCombatReadiness().modifyFlat(id, MAX_CR_BONUS);
        stats.getSensorStrength().modifyPercent(id, SENSOR_BONUS);
        stats.getSensorProfile().modifyPercent(id, -SENSOR_PROFILE_BONUS);
        stats.getDynamic().getMod(Stats.CORONA_EFFECT_MULT).modifyMult(id, 1f - CORONA_EFFECT_BONUS * 0.01f);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;
        MutableShipStatsAPI stats = ship.getMutableStats();
        float hullRegen = 0.6f;
        float hullHp = ship.getHitpoints();
        float hullMaxHp = ship.getMaxHitpoints();
        float hullMaxCap = hullRegen * (float) Math.min(Math.max(0, hullMaxHp - 12500f) / 50000f, 0.75);
        float hullPer = (hullHp / hullMaxHp) * 100f;
        float hullPerSegment = (100f - hullPer) / 20f;
        if (hullHp == hullMaxHp) {
            stats.getMaxCombatHullRepairFraction().modifyFlat(ARX_AUTO_REPAIR, 0f);
            stats.getHullCombatRepairRatePercentPerSecond().modifyFlat(ARX_AUTO_REPAIR,
                    0f);
        } else {
            if (ship.getFluxTracker().isVenting() == true) {
                stats.getMaxCombatHullRepairFraction().modifyFlat(ARX_AUTO_REPAIR, 0f);
                stats.getHullCombatRepairRatePercentPerSecond().modifyFlat(ARX_AUTO_REPAIR,
                        0f);
            } else {
                stats.getMaxCombatHullRepairFraction().modifyFlat(ARX_AUTO_REPAIR, 1f);
                stats.getHullCombatRepairRatePercentPerSecond().modifyFlat(ARX_AUTO_REPAIR,
                        (hullRegen - hullMaxCap) * hullPerSegment);
            }
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width,
            boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        tooltip.addPara("Increases flux capacity by %s.", oPad, good, Math.round(FLUX_CAPACITY_BONUS) + "%");
        tooltip.addPara("Increases flux dissipation by %s.", pad, good, Math.round(FLUX_DISSIPATION_BONUS) + "%");
        tooltip.addPara("Increases dissipation fraction by %s.", pad, good,
                Math.round(HARD_FLUX_DISSIPATION_FRACTION_BONUS) + "%");
        tooltip.addPara("Increases vent effectiveness by %s.", pad, good, Math.round(VENT_RATE_BONUS) + "%");
        tooltip.addPara("Increases peak CR duration by %s.", pad, good, Math.round(PEAK_CR_TIME_BONUS) + "%");
        tooltip.addPara("Increases max CR by %s.", pad, good, Math.round(MAX_CR_BONUS) + "");
        tooltip.addPara("Increases sensor strength by %s.", pad, good, Math.round(SENSOR_BONUS) + "%");
        tooltip.addPara("Decreases sensor profile by %s.", pad, good, Math.round(SENSOR_PROFILE_BONUS) + "%");
        tooltip.addPara("Decreases corona effects by %s.", pad, good, Math.round(CORONA_EFFECT_BONUS) + "%");
        tooltip.addPara("Repairs hull in combat", pad);
    }
}
