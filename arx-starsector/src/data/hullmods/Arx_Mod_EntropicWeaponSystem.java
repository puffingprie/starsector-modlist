package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;

public class Arx_Mod_EntropicWeaponSystem extends BaseHullMod {
    public static float AMMO_BONUS = 50f;
    public static float PROJECTILE_SPEED_BONUS = 50f;
    public static float ROF_BONUS = 50f;
    public static float DAMAGE_BONUS = 100f;
    public static float FLUX_COST_BONUS = -25f;
    public static float RANGE_BONUS = 100f;
    public static float MISSILE_RANGE_BONUS = 50f;
    public static float WEAPON_TURN_RATE_BONUS = 50f;
    public static float MISSILE_HP_BONUS = 50f;
    public static float SIGHT_BONUS = 100f;
    public static float WEAPON_DURABILITY_BONUS = 100f;
    public static float WEAPON_REPAIR_TIME_BONUS = 50f;
    public static float WEAPON_OP_COST_BONUS = 50f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticAmmoBonus().modifyPercent(id, AMMO_BONUS);
        stats.getEnergyAmmoBonus().modifyPercent(id, AMMO_BONUS);
        stats.getMissileAmmoBonus().modifyPercent(id, AMMO_BONUS);

        stats.getBallisticAmmoRegenMult().modifyPercent(id, AMMO_BONUS);
        stats.getEnergyAmmoRegenMult().modifyPercent(id, AMMO_BONUS);
        stats.getMissileAmmoRegenMult().modifyPercent(id, AMMO_BONUS);

        stats.getBallisticProjectileSpeedMult().modifyPercent(id, PROJECTILE_SPEED_BONUS);
        stats.getEnergyProjectileSpeedMult().modifyPercent(id, PROJECTILE_SPEED_BONUS);
        stats.getProjectileSpeedMult().modifyPercent(id, PROJECTILE_SPEED_BONUS);

        stats.getBallisticRoFMult().modifyPercent(id, ROF_BONUS);
        stats.getEnergyRoFMult().modifyPercent(id, ROF_BONUS);

        stats.getBallisticWeaponDamageMult().modifyPercent(id, DAMAGE_BONUS);
        stats.getBeamWeaponDamageMult().modifyPercent(id, DAMAGE_BONUS);
        stats.getEnergyWeaponDamageMult().modifyPercent(id, DAMAGE_BONUS);
        stats.getMissileWeaponDamageMult().modifyPercent(id, DAMAGE_BONUS);

        stats.getBallisticWeaponFluxCostMod().modifyPercent(id, FLUX_COST_BONUS);
        stats.getBeamWeaponFluxCostMult().modifyPercent(id, FLUX_COST_BONUS);
        stats.getEnergyWeaponFluxCostMod().modifyPercent(id, FLUX_COST_BONUS);
        stats.getMissileWeaponFluxCostMod().modifyPercent(id, FLUX_COST_BONUS);

        stats.getBallisticWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
        stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
        stats.getMissileWeaponRangeBonus().modifyPercent(id, MISSILE_RANGE_BONUS);

        stats.getWeaponTurnRateBonus().modifyPercent(id, WEAPON_TURN_RATE_BONUS);

        stats.getMissileHealthBonus().modifyPercent(id, MISSILE_HP_BONUS);

        stats.getSightRadiusMod().modifyPercent(id, SIGHT_BONUS);

        stats.getDynamic().getMod(Stats.SMALL_BALLISTIC_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.MEDIUM_BALLISTIC_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.LARGE_BALLISTIC_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.ALL_FIGHTER_COST_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.SMALL_ENERGY_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.MEDIUM_ENERGY_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.LARGE_ENERGY_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.SMALL_MISSILE_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.MEDIUM_MISSILE_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.LARGE_MISSILE_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.SMALL_PD_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.MEDIUM_PD_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.LARGE_PD_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.SMALL_BEAM_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.MEDIUM_BEAM_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
        stats.getDynamic().getMod(Stats.LARGE_BEAM_MOD).modifyPercent(id, -WEAPON_OP_COST_BONUS);
    }

    @Override
    public boolean affectsOPCosts() {
        return true;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width,
            boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        tooltip.addPara("Increases ammo capacity and ammo regen by %s.", oPad, good, Math.round(AMMO_BONUS) + "%");
        tooltip.addPara("Increases projectile speed by %s.", pad, good, Math.round(PROJECTILE_SPEED_BONUS) + "%");
        tooltip.addPara("Increases weapons ROF by %s.", pad, good, Math.round(ROF_BONUS) + "%");
        tooltip.addPara("Increases weapon and missile damage by %s.", pad, good, Math.round(DAMAGE_BONUS) + "%");
        tooltip.addPara("Decreases weapons flux cost by %s.", pad, good, Math.round(FLUX_COST_BONUS) + "%");
        tooltip.addPara("Increases weapons and missile range by %s.", pad, good, Math.round(RANGE_BONUS) + "%");
        tooltip.addPara("Increases turret turn rate by %s.", pad, good, Math.round(WEAPON_TURN_RATE_BONUS) + "%");
        tooltip.addPara("Increases missile health by %s.", pad, good, Math.round(MISSILE_HP_BONUS) + "%");
        tooltip.addPara("Increases sight range by %s.", pad, good, Math.round(SIGHT_BONUS) + "%");
        tooltip.addPara("Decreases all weapons OP costs by %s.", pad, good, Math.round(WEAPON_OP_COST_BONUS) + "%");
    }
}
