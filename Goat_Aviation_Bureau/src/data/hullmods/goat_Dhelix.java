package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;

public class goat_Dhelix extends BaseHullMod {

	private static final float PROFILE_MULT = 0.7f;

	public static final float RANGE_INCREASE_MAX = 1200f;
	public static final float RANGE_INCREASE_VALUE = 300f;

	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getSensorProfile().modifyMult(id, PROFILE_MULT);

	}

	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ship.addListener(L);
	}

	public static final DhelixRangeModifier L = new DhelixRangeModifier();

	public static class DhelixRangeModifier implements WeaponBaseRangeModifier {

		@Override
		public float getWeaponBaseRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
			return 0f;
		}

		@Override
		public float getWeaponBaseRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
			return 1f;
		}

		@Override
		public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {

			float bonus = 0f;
			if (weapon.getSize() == WeaponAPI.WeaponSize.LARGE) {
				bonus = RANGE_INCREASE_VALUE;
			}

			float base = weapon.getSpec().getMaxRange();
			if (base + bonus > RANGE_INCREASE_MAX) {
				bonus = RANGE_INCREASE_MAX - base;
			}

			if (bonus < 0f) bonus = 0f;
			return bonus;
		}
	}

	@Override
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)((1f - PROFILE_MULT) * 100f) + "%";
		if (index == 1) return "" + (int)(RANGE_INCREASE_MAX);
		if (index == 2) return "" + (int)(RANGE_INCREASE_VALUE);
		if (index == 3) return "" + (int)(RANGE_INCREASE_MAX);
		return null;
	}
}
