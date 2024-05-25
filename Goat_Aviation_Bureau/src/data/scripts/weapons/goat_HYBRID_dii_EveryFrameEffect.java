package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;

public class goat_HYBRID_dii_EveryFrameEffect implements EveryFrameWeaponEffectPlugin {

	public static final float MAX_RANGE_INCREASE = 200f;
	public static final String WEAPON_ID = "all_goat_diiscipline";

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

		ShipAPI ship = weapon.getShip();
		if (ship == null) return;
		if (!ship.isAlive()) return;

		if (engine.isPaused()) return;

		goat_HYBRID_Range m = goat_HYBRID_Range.getInstance(ship);

	}

	public static class goat_HYBRID_Range implements WeaponBaseRangeModifier {

		public static final String DATA_KEY = goat_HYBRID_Range.class.getName() + WEAPON_ID;

		public static goat_HYBRID_Range getInstance(ShipAPI ship) {
			if (!ship.getCustomData().containsKey(DATA_KEY)) {
				goat_HYBRID_Range m = new goat_HYBRID_Range();
				ship.setCustomData(DATA_KEY, m);
				ship.addListener(m);
			}

			return (goat_HYBRID_Range)ship.getCustomData().get(DATA_KEY);
		}

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
			if (weapon.getSlot().getWeaponType() == WeaponType.HYBRID && weapon.getId().contentEquals(WEAPON_ID)) {
				return MAX_RANGE_INCREASE;
			}
			return 0f;
		}
	}
}