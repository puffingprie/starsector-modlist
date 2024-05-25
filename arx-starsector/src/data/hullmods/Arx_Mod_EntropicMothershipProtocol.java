package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class Arx_Mod_EntropicMothershipProtocol extends BaseHullMod {
    public static float WING_RANGE_BONUS = 100f;
    public static float WING_REPLACEMENT_BONUS = 25f;
    public static float WING_HULL_FLAT_BONUS = 200f;
    public static float WING_ARMOR_FLAT_BONUS = 50f;
    public static float WING_HULL_BONUS = 100f;
    public static float WING_ARMOR_BONUS = 100f;
    public static float WING_FLUX_BONUS = 500f;
    public static float WING_SPEED_BONUS = 50f;
    public static float WING_DAMAGE_BONUS = 50f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getFighterWingRange().modifyPercent(id, WING_RANGE_BONUS);
        stats.getDynamic().getMod(Stats.REPLACEMENT_RATE_DECREASE_MULT).modifyMult(id,
                1f - (WING_REPLACEMENT_BONUS * 0.01f));
        stats.getDynamic().getMod(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyMult(id, WING_REPLACEMENT_BONUS * 0.01f);
    }

    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        MutableShipStatsAPI stats = fighter.getMutableStats();
        stats.getHullBonus().modifyFlat(id, WING_HULL_FLAT_BONUS);
        stats.getHullBonus().modifyPercent(id, WING_HULL_BONUS);
        stats.getArmorBonus().modifyFlat(id, WING_ARMOR_FLAT_BONUS);
        stats.getArmorBonus().modifyPercent(id, WING_ARMOR_BONUS);
        stats.getFluxCapacity().modifyPercent(id, WING_FLUX_BONUS);
        stats.getMaxSpeed().modifyPercent(id, WING_SPEED_BONUS);
        stats.getDamageToMissiles().modifyPercent(id, WING_DAMAGE_BONUS);
        stats.getDamageToFrigates().modifyPercent(id, WING_DAMAGE_BONUS);
        stats.getDamageToFighters().modifyPercent(id, WING_DAMAGE_BONUS);
        stats.getDamageToDestroyers().modifyPercent(id, WING_DAMAGE_BONUS);
        stats.getDamageToCruisers().modifyPercent(id, WING_DAMAGE_BONUS);
        stats.getDamageToCapital().modifyPercent(id, WING_DAMAGE_BONUS);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width,
            boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        tooltip.addPara("Increases wing hull by %s, then %s.", oPad, good, Math.round(WING_HULL_FLAT_BONUS) + "",
                Math.round(WING_HULL_BONUS) + "%");
        tooltip.addPara("Increases wing armor by %s, then %s.", pad, good, Math.round(WING_ARMOR_FLAT_BONUS) + "",
                Math.round(WING_ARMOR_BONUS) + "%");
        tooltip.addPara("Increases wing flux capacity by %s.", pad, good, Math.round(WING_FLUX_BONUS) + "%");
        tooltip.addPara("Increases wing max speed by %s.", pad, good, Math.round(WING_SPEED_BONUS) + "%");
        tooltip.addPara("Increases wing damage by %s.", pad, good, Math.round(WING_DAMAGE_BONUS) + "%");
    }
}
