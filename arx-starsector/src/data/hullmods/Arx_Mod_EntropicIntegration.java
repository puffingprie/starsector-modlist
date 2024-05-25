package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class Arx_Mod_EntropicIntegration extends BaseHullMod {
    public static float HULL_BONUS = 100f;
    public static float ARMOR_BONUS = 100f;
    public static float SHIELD_ARC_BONUS = 120f;
    public static float SHIELD_UNFOLD_BONUS = 50f;
    public static float SHIELD_UPKEEP_BONUS = 50f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getHullBonus().modifyPercent(id, HULL_BONUS);
        stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
        stats.getShieldArcBonus().modifyFlat(id, SHIELD_ARC_BONUS);
        stats.getShieldUnfoldRateMult().modifyPercent(id, SHIELD_UNFOLD_BONUS);
        stats.getShieldUpkeepMult().modifyPercent(id, -SHIELD_UPKEEP_BONUS);
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        ShieldAPI shield = ship.getShield();
        if (shield != null)
            shield.setType(ShieldType.OMNI);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width,
            boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        tooltip.addPara("Increases hull by %s.", oPad, good, Math.round(HULL_BONUS) + "%");
        tooltip.addPara("Increases armor by %s.", pad, good, Math.round(ARMOR_BONUS) + "%");
        tooltip.addPara("Sets shield type to %s.", pad, good, "Omni");
        tooltip.addPara("Increases shield arc by %s.", pad, good, Math.round(SHIELD_ARC_BONUS) + " degrees");
        tooltip.addPara("Increases shield unfold rate by %s.", pad, good, Math.round(SHIELD_UNFOLD_BONUS) + "%");
        tooltip.addPara("Increases shield efficiency by %s.", pad, good, Math.round(SHIELD_UPKEEP_BONUS) + "%");
    }
}
