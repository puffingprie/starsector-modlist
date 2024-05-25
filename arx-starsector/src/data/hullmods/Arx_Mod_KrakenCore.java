package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class Arx_Mod_KrakenCore extends BaseHullMod {
        public static float FLUX_CAPACITY_BONUS = 100f;
        public static float FLUX_DISSIPATION_BONUS = 100f;
        public static float HARD_FLUX_DISSIPATION_FRACTION_BONUS = 10f;
        public static float VENT_RATE_BONUS = 100f;
        public static float PEAK_CR_TIME_BONUS = 25f;
        public static float MAX_CR_BONUS = 15f;
        public static float SENSOR_BONUS = 25f;
        public static float SENSOR_PROFILE_BONUS = 25f;
        public static float ZERO_FLUX_SPEED_BONUS = 50f;
        public static float SPEED_BONUS = 50f;
        public static float MANEUVERABILITY_BONUS = 25f;
        public static float ACCELERATION_BONUS = 50f;
        public static float DECELERATION_BONUS = 50f;
        public static float BURN_BONUS = 4f;
        public static float ENGINE_DURABILITY_BONUS = 100f;
        public static float ENGINE_REPAIR_TIME_BONUS = 50f;
        public static float HULL_BONUS = 100f;
        public static float ARMOR_BONUS = 100f;
        public static float SHIELD_ARC_BONUS = 120f;
        public static float SHIELD_UNFOLD_BONUS = 50f;
        public static float SHIELD_UPKEEP_BONUS = 50f;
        public static float CREW_CAPACITY_BONUS = 100f;
        public static float MIN_CREW_BONUS = 50f;
        public static float CREW_LOSS_BONUS = 50f;
        public static float CARGO_CAPACITY_BONUS = 400f;
        public static float SUPPLY_USE_BONUS = 50f;
        public static float FUEL_CAPACITY_BONUS = 400f;
        public static float FUEL_USE_BONUS = 50f;
        public static float WING_RANGE_BONUS = 100f;
        public static float WING_REPLACEMENT_BONUS = 25f;
        public static float WING_HULL_FLAT_BONUS = 200f;
        public static float WING_ARMOR_FLAT_BONUS = 50f;
        public static float WING_HULL_BONUS = 100f;
        public static float WING_ARMOR_BONUS = 100f;
        public static float WING_FLUX_BONUS = 500f;
        public static float WING_SPEED_BONUS = 50f;
        public static float WING_DAMAGE_BONUS = 50f;
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
                stats.getZeroFluxSpeedBoost().modifyPercent(id, ZERO_FLUX_SPEED_BONUS);
                stats.getMaxSpeed().modifyPercent(id, SPEED_BONUS);
                stats.getMaxTurnRate().modifyPercent(id, MANEUVERABILITY_BONUS);
                stats.getAcceleration().modifyPercent(id, ACCELERATION_BONUS);
                stats.getDeceleration().modifyPercent(id, DECELERATION_BONUS);
                stats.getMaxBurnLevel().modifyFlat(id, BURN_BONUS);
                stats.getEngineHealthBonus().modifyPercent(id, ENGINE_DURABILITY_BONUS);
                stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f - ENGINE_REPAIR_TIME_BONUS * 0.01f);
                stats.getHullBonus().modifyPercent(id, HULL_BONUS);
                stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
                stats.getShieldArcBonus().modifyFlat(id, SHIELD_ARC_BONUS);
                stats.getShieldUnfoldRateMult().modifyPercent(id, SHIELD_UNFOLD_BONUS);
                stats.getShieldUpkeepMult().modifyPercent(id, -SHIELD_UPKEEP_BONUS);
                stats.getMaxCrewMod().modifyPercent(id, CREW_CAPACITY_BONUS);
                stats.getMinCrewMod().modifyMult(id, 1f - MIN_CREW_BONUS * 0.01f);
                stats.getCrewLossMult().modifyMult(id, 1f - CREW_LOSS_BONUS * 0.01f);
                stats.getCargoMod().modifyPercent(id, CARGO_CAPACITY_BONUS);
                stats.getSuppliesPerMonth().modifyMult(id, 1f - SUPPLY_USE_BONUS * 0.01f);
                stats.getFuelMod().modifyPercent(id, FUEL_CAPACITY_BONUS);
                stats.getFuelUseMod().modifyMult(id, 1f - FUEL_USE_BONUS * 0.01f);
                stats.getFighterWingRange().modifyPercent(id, WING_RANGE_BONUS);
                stats.getDynamic().getMod(Stats.REPLACEMENT_RATE_DECREASE_MULT).modifyMult(id,
                                1f - (WING_REPLACEMENT_BONUS * 0.01f));
                stats.getDynamic().getMod(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyMult(id,
                                WING_REPLACEMENT_BONUS * 0.01f);
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
        public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
                ShieldAPI shield = ship.getShield();
                if (shield != null)
                        shield.setType(ShieldType.OMNI);
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
                tooltip.addPara("Increases flux capacity by %s.", oPad, good, Math.round(FLUX_CAPACITY_BONUS) + "%");
                tooltip.addPara("Increases flux dissipation by %s.", pad, good,
                                Math.round(FLUX_DISSIPATION_BONUS) + "%");
                tooltip.addPara("Increases dissipation fraction by %s.", pad, good,
                                Math.round(HARD_FLUX_DISSIPATION_FRACTION_BONUS) + "%");
                tooltip.addPara("Increases vent effectiveness by %s.", pad, good, Math.round(VENT_RATE_BONUS) + "%");
                tooltip.addPara("Increases peak CR duration by %s.", pad, good, Math.round(PEAK_CR_TIME_BONUS) + "%");
                tooltip.addPara("Increases max CR by %s.", pad, good, Math.round(MAX_CR_BONUS) + "");
                tooltip.addPara("Increases sensor strength by %s.", pad, good, Math.round(SENSOR_BONUS) + "%");
                tooltip.addPara("Decreases sensor profile by %s.", pad, good, Math.round(SENSOR_PROFILE_BONUS) + "%");
                tooltip.addPara("Increase zero-flux speed boost by %s.", oPad, good,
                                Math.round(ZERO_FLUX_SPEED_BONUS) + "%");
                tooltip.addPara("Increase max speed by %s.", pad, good, Math.round(SPEED_BONUS) + "%");
                tooltip.addPara("Increase maneuverability by %s.", pad, good, Math.round(MANEUVERABILITY_BONUS) + "%");
                tooltip.addPara("Increase acceleration by %s, and deceleration by %s.", pad, good,
                                Math.round(ACCELERATION_BONUS) + "%",
                                Math.round(DECELERATION_BONUS) + "%");
                tooltip.addPara("Increase burn by %s.", pad, good, Math.round(BURN_BONUS) + "");
                tooltip.addPara("Increase engine durability by %s and decrease engine repair time by %s.", pad, good,
                                Math.round(ENGINE_DURABILITY_BONUS) + "%", Math.round(ENGINE_REPAIR_TIME_BONUS) + "%");
                tooltip.addPara("Increases hull by %s.", oPad, good, Math.round(HULL_BONUS) + "%");
                tooltip.addPara("Increases armor by %s.", pad, good, Math.round(ARMOR_BONUS) + "%");
                tooltip.addPara("Sets shield type to %s.", pad, good, "Omni");
                tooltip.addPara("Increases shield arc by %s.", pad, good, Math.round(SHIELD_ARC_BONUS) + " degrees");
                tooltip.addPara("Increases shield unfold rate by %s.", pad, good,
                                Math.round(SHIELD_UNFOLD_BONUS) + "%");
                tooltip.addPara("Increases shield efficiency by %s.", pad, good, Math.round(SHIELD_UPKEEP_BONUS) + "%");
                tooltip.addPara(
                                "Increases crew capacity by %s, decreases crew requirements by %s, and decreases crew loss by %s.",
                                oPad, good, Math.round(CREW_CAPACITY_BONUS) + "%", Math.round(MIN_CREW_BONUS) + "%",
                                Math.round(CREW_LOSS_BONUS) + "%");
                tooltip.addPara("Increases cargo capacity by %s and decreases supply use by %s", pad, good,
                                Math.round(CARGO_CAPACITY_BONUS) + "%", Math.round(SUPPLY_USE_BONUS) + "%");
                tooltip.addPara("Increases fuel capacity by %s and decreases fuel use by %s", pad, good,
                                Math.round(FUEL_CAPACITY_BONUS) + "%", Math.round(FUEL_USE_BONUS) + "%");
                tooltip.addPara("Increases wing hull by %s, then %s.", oPad, good,
                                Math.round(WING_HULL_FLAT_BONUS) + "",
                                Math.round(WING_HULL_BONUS) + "%");
                tooltip.addPara("Increases wing armor by %s, then %s.", pad, good,
                                Math.round(WING_ARMOR_FLAT_BONUS) + "",
                                Math.round(WING_ARMOR_BONUS) + "%");
                tooltip.addPara("Increases wing flux capacity by %s.", pad, good, Math.round(WING_FLUX_BONUS) + "%");
                tooltip.addPara("Increases wing max speed by %s.", pad, good, Math.round(WING_SPEED_BONUS) + "%");
                tooltip.addPara("Increases wing damage by %s.", pad, good, Math.round(WING_DAMAGE_BONUS) + "%");
                tooltip.addPara("Increases ammo capacity and ammo regen by %s.", oPad, good,
                                Math.round(AMMO_BONUS) + "%");
                tooltip.addPara("Increases projectile speed by %s.", pad, good,
                                Math.round(PROJECTILE_SPEED_BONUS) + "%");
                tooltip.addPara("Increases weapons ROF by %s.", pad, good, Math.round(ROF_BONUS) + "%");
                tooltip.addPara("Increases weapon and missile damage by %s.", pad, good,
                                Math.round(DAMAGE_BONUS) + "%");
                tooltip.addPara("Decreases weapons flux cost by %s.", pad, good, Math.round(FLUX_COST_BONUS) + "%");
                tooltip.addPara("Increases weapons and missile range by %s.", pad, good, Math.round(RANGE_BONUS) + "%");
                tooltip.addPara("Increases turret turn rate by %s.", pad, good,
                                Math.round(WEAPON_TURN_RATE_BONUS) + "%");
                tooltip.addPara("Increases missile health by %s.", pad, good, Math.round(MISSILE_HP_BONUS) + "%");
                tooltip.addPara("Increases sight range by %s.", pad, good, Math.round(SIGHT_BONUS) + "%");
                tooltip.addPara("Decreases all weapons OP costs by %s.", pad, good,
                                Math.round(WEAPON_OP_COST_BONUS) + "%");
        }
}
