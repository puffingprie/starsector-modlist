package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class Arx_Mod_Ragnorak_Core extends BaseHullMod {
    // NON-MISSILE BONUSES
    public static float AMMO_BONUS = 200f;
    public static float PROJECTILE_SPEED_BONUS = 100f;
    public static float ROF_BONUS = 100f;
    public static float DAMAGE_BONUS = 400f;
    public static float FLUX_COST_BONUS = -75f;
    public static float RANGE_BONUS = 400f;
    public static float WEAPON_TURN_RATE_BONUS = 100f;
    public static float MISSILE_HP_BONUS = 100f;
    // HULL & ARMOR
    public static float HULL_ARMOR_BONUS = 300f;
    public static final String ARX_AUTO_REPAIR = "arxAutoRepair";
    public static float REPAIR_PER_SEC = 0.01f;
    // SHIELD
    public static float SHIELD_ARC_BONUS = 360f;
    public static float SHIELD_UNFOLD_BONUS = 100f;
    public static float SHIELD_EFFICIENCY_BONUS = 100f;
    public static float SHIELD_FLUX_BONUS = 100f;
    // SPEED & MANEUVERABILITY
    public static float SPEED_BONUS = 1f;
    public static float MANEUVERABILITY_BONUS = 0.5f;
    // FLUX
    public static float FLUX_CAPACITY_BONUS = 50f;
    public static float FLUX_DISSIPATION_BONUS = 25f;
    // OTHER SHIP STATS
    public static float SENSOR_BONUS = 1f;
    public static float SENSOR_PROFILE_BONUS = 1f;
    public static float ECM_BONUS = 1f;
    public static float PEAK_OPERATING_TIME_BONUS = 2f;
    public static float CARGO_CAPACITY_BONUS = 4f;
    public static float CREW_CAPACITY_BONUS = 4f;
    public static float FUEL_CAPACITY_BONUS = 4f;
    public static float FUEL_PER_LIGHT_YEAR_BONUS = 4f;
    public static float BURN_LEVEL_BONUS = 10f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticAmmoBonus().modifyPercent(id, AMMO_BONUS);
        stats.getEnergyAmmoBonus().modifyPercent(id, AMMO_BONUS);
        stats.getMissileAmmoBonus().modifyPercent(id, AMMO_BONUS);
        stats.getBallisticAmmoRegenMult().modifyMult(id, 1f + AMMO_BONUS * 0.01f);
        stats.getEnergyAmmoRegenMult().modifyMult(id, 1f + AMMO_BONUS * 0.01f);
        stats.getMissileAmmoRegenMult().modifyMult(id, 1f + AMMO_BONUS * 0.01f);
        stats.getBallisticProjectileSpeedMult().modifyMult(id, 1f + PROJECTILE_SPEED_BONUS * 0.01f);
        stats.getEnergyProjectileSpeedMult().modifyMult(id, 1f + PROJECTILE_SPEED_BONUS * 0.01f);
        stats.getMissileMaxSpeedBonus().modifyMult(id, 1f + PROJECTILE_SPEED_BONUS * 0.01f);
        stats.getProjectileSpeedMult().modifyMult(id, 1f + PROJECTILE_SPEED_BONUS * 0.005f);
        stats.getBallisticRoFMult().modifyMult(id, 1f + ROF_BONUS * 0.01f);
        stats.getEnergyRoFMult().modifyMult(id, 1f + ROF_BONUS * 0.01f);
        stats.getMissileRoFMult().modifyMult(id, 1f + ROF_BONUS * 0.01f);
        stats.getBallisticWeaponDamageMult().modifyMult(id, 1f + DAMAGE_BONUS * 0.01f);
        stats.getBeamWeaponDamageMult().modifyMult(id, 1f + DAMAGE_BONUS * 0.01f);
        stats.getEnergyWeaponDamageMult().modifyMult(id, 1f + DAMAGE_BONUS * 0.01f);
        stats.getMissileWeaponDamageMult().modifyMult(id, 1f + DAMAGE_BONUS * 0.01f);
        stats.getBallisticWeaponFluxCostMod().modifyMult(id, 1f + FLUX_COST_BONUS * 0.01f);
        stats.getBeamWeaponFluxCostMult().modifyMult(id, 1f + FLUX_COST_BONUS * 0.01f);
        stats.getEnergyWeaponFluxCostMod().modifyMult(id, 1f + FLUX_COST_BONUS * 0.01f);
        stats.getMissileWeaponFluxCostMod().modifyMult(id, 1f + FLUX_COST_BONUS * 0.01f);
        stats.getBallisticWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
        stats.getBeamWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
        stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
        stats.getMissileWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
        stats.getWeaponTurnRateBonus().modifyPercent(id, WEAPON_TURN_RATE_BONUS);
        stats.getBeamWeaponTurnRateBonus().modifyPercent(id, WEAPON_TURN_RATE_BONUS);
        stats.getMissileHealthBonus().modifyPercent(id, MISSILE_HP_BONUS);

        stats.getHullBonus().modifyPercent(id, HULL_ARMOR_BONUS);
        stats.getArmorBonus().modifyPercent(id, HULL_ARMOR_BONUS);

        stats.getShieldArcBonus().modifyFlat(id, SHIELD_ARC_BONUS);
        stats.getShieldUnfoldRateMult().modifyMult(id, 1f + SHIELD_UNFOLD_BONUS * 0.01f);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive())
            return;
        MutableShipStatsAPI stats = ship.getMutableStats();
        float hullHp = ship.getHitpoints();
        float hullMaxHp = ship.getMaxHitpoints();
        if (hullHp == hullMaxHp) {
            stats.getMaxCombatHullRepairFraction().modifyFlat(ARX_AUTO_REPAIR, 0f);
            stats.getHullCombatRepairRatePercentPerSecond().modifyFlat(ARX_AUTO_REPAIR, 0f);
        } else {
            if (ship.getFluxTracker().isVenting() == true) {
                stats.getMaxCombatHullRepairFraction().modifyFlat(ARX_AUTO_REPAIR, 0f);
                stats.getHullCombatRepairRatePercentPerSecond().modifyFlat(ARX_AUTO_REPAIR, 0f);
            } else {
                stats.getMaxCombatHullRepairFraction().modifyFlat(ARX_AUTO_REPAIR, 1f);
                stats.getHullCombatRepairRatePercentPerSecond().modifyFlat(ARX_AUTO_REPAIR, hullMaxHp * REPAIR_PER_SEC);
            }
        }
    }

    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0)
            return "" + (int) AMMO_BONUS + "%";
        if (index == 1)
            return "" + (int) PROJECTILE_SPEED_BONUS + "%";
        if (index == 2)
            return "" + (int) ROF_BONUS + "%";
        if (index == 3)
            return "" + (int) DAMAGE_BONUS + "%";
        if (index == 4)
            return "" + (int) FLUX_COST_BONUS + "%";
        if (index == 5)
            return "" + (int) RANGE_BONUS + "%";
        if (index == 6)
            return "" + (int) WEAPON_TURN_RATE_BONUS + "%";
        if (index == 7)
            return "" + (int) MISSILE_HP_BONUS + "%";
        if (index == 8)
            return "" + (int) HULL_ARMOR_BONUS + "%";
        if (index == 9)
            return "" + (int) (REPAIR_PER_SEC * 100) + "%";
        if (index == 10)
            return "" + (int) SHIELD_ARC_BONUS + "°";
        if (index == 11)
            return "" + (int) SHIELD_UNFOLD_BONUS + "%";
        return null;
    }

    /**
     * Increases the ammo capacity and ammo regen of weapons by %s.
     * Increases the speed of projectiles and missiles by %s.
     * Increases the rate of fire of weapons by %s.
     * Increases the damage of weapons by %s.
     * Reduces the flux cost of weapons by %s.
     * Increases the range of weapons by %s.
     * Increases the turn rate of weapons by %s.
     * Increases the health of missiles by %s.
     * Increases the hull and armor of the ship by %s.
     * Automatically repairs the ship by %s of its maximum hull per second when not
     * venting flux.
     * Increases the shield arc of the ship by %s.
     */
}
