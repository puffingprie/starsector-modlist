package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class Arx_Mod_EntropicLogisticsAI extends BaseHullMod {
        public static float CREW_CAPACITY_BONUS = 100f;
        public static float MIN_CREW_BONUS = 50f;
        public static float CREW_LOSS_BONUS = 50f;
        public static float CARGO_CAPACITY_BONUS = 400f;
        public static float SUPPLY_USE_BONUS = 50f;
        public static float FUEL_CAPACITY_BONUS = 400f;
        public static float FUEL_USE_BONUS = 50f;

        @Override
        public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
                stats.getMaxCrewMod().modifyPercent(id, CREW_CAPACITY_BONUS);
                stats.getMinCrewMod().modifyMult(id, 1f - MIN_CREW_BONUS * 0.01f);
                stats.getCrewLossMult().modifyMult(id, 1f - CREW_LOSS_BONUS * 0.01f);
                stats.getCargoMod().modifyPercent(id, CARGO_CAPACITY_BONUS);
                stats.getSuppliesPerMonth().modifyMult(id, 1f - SUPPLY_USE_BONUS * 0.01f);
                stats.getFuelMod().modifyPercent(id, FUEL_CAPACITY_BONUS);
                stats.getFuelUseMod().modifyMult(id, 1f - FUEL_USE_BONUS * 0.01f);
        }

        @Override
        public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width,
                        boolean isForModSpec) {
                float pad = 3f;
                float oPad = 10f;
                Color good = Misc.getPositiveHighlightColor();
                tooltip.addPara(
                                "Increases crew capacity by %s, decreases crew requirements by %s, and decreases crew loss by %s.",
                                oPad, good, Math.round(CREW_CAPACITY_BONUS) + "%", Math.round(MIN_CREW_BONUS) + "%",
                                Math.round(CREW_LOSS_BONUS) + "%");
                tooltip.addPara("Increases cargo capacity by %s and decreases supply use by %s", pad, good,
                                Math.round(CARGO_CAPACITY_BONUS) + "%", Math.round(SUPPLY_USE_BONUS) + "%");
                tooltip.addPara("Increases fuel capacity by %s and decreases fuel use by %s", pad, good,
                                Math.round(FUEL_CAPACITY_BONUS) + "%", Math.round(FUEL_USE_BONUS) + "%");
        }
}
