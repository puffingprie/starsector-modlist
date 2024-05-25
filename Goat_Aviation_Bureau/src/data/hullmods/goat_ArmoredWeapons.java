package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;

public class goat_ArmoredWeapons extends BaseHullMod {

    public static float HEALTH_BONUS = 400f;
    public static float ARMOR_BONUS = 30f;

    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
        stats.getWeaponHealthBonus().modifyPercent(id, HEALTH_BONUS);

    }
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return "" + (int) HEALTH_BONUS + "%";
        return null;
    }
}
