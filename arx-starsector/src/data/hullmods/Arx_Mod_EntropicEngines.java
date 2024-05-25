package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class Arx_Mod_EntropicEngines extends BaseHullMod {
    public static float ZERO_FLUX_SPEED_BONUS = 50f;
    public static float SPEED_BONUS = 50f;
    public static float MANEUVERABILITY_BONUS = 25f;
    public static float ACCELERATION_BONUS = 50f;
    public static float DECELERATION_BONUS = 50f;
    public static float BURN_BONUS = 4f;
    public static float ENGINE_DURABILITY_BONUS = 100f;
    public static float ENGINE_REPAIR_TIME_BONUS = 50f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getZeroFluxSpeedBoost().modifyPercent(id, ZERO_FLUX_SPEED_BONUS);
        stats.getMaxSpeed().modifyPercent(id, SPEED_BONUS);
        stats.getMaxTurnRate().modifyPercent(id, MANEUVERABILITY_BONUS);
        stats.getAcceleration().modifyPercent(id, ACCELERATION_BONUS);
        stats.getDeceleration().modifyPercent(id, DECELERATION_BONUS);
        stats.getMaxBurnLevel().modifyFlat(id, BURN_BONUS);
        stats.getEngineHealthBonus().modifyPercent(id, ENGINE_DURABILITY_BONUS);
        stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f - ENGINE_REPAIR_TIME_BONUS * 0.01f);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width,
            boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        tooltip.addPara("Increase zero-flux speed boost by %s.", oPad, good, Math.round(ZERO_FLUX_SPEED_BONUS) + "%");
        tooltip.addPara("Increase max speed by %s.", pad, good, Math.round(SPEED_BONUS) + "%");
        tooltip.addPara("Increase maneuverability by %s.", pad, good, Math.round(MANEUVERABILITY_BONUS) + "%");
        tooltip.addPara("Increase acceleration by %s, and deceleration by %s.", pad, good,
                Math.round(ACCELERATION_BONUS) + "%",
                Math.round(DECELERATION_BONUS) + "%");
        tooltip.addPara("Increase burn by %s.", pad, good, Math.round(BURN_BONUS) + "");
        tooltip.addPara("Increase engine durability by %s and decrease engine repair time by %s.", pad, good,
                Math.round(ENGINE_DURABILITY_BONUS) + "%", Math.round(ENGINE_REPAIR_TIME_BONUS) + "%");
    }
}
