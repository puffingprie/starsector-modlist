package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;

public class goat_HYBRID_imp_EveryFrameEffect implements EveryFrameWeaponEffectPlugin {

	public static final float MAX_RANGE_INCREASE = 300f;
	public static final String WEAPON_ID = "all_goat_implement";

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

		ShipAPI ship = weapon.getShip();
		if (ship == null) return;
		if (!ship.isAlive()) return;

		if (engine.isPaused()) return;

		goat_HYBRID_Range1 m = goat_HYBRID_Range1.getInstance(ship);

	}

	public static class goat_HYBRID_Range1 implements WeaponBaseRangeModifier {

		public static final String DATA_KEY = goat_HYBRID_Range1.class.getName() + WEAPON_ID;

		public static goat_HYBRID_Range1 getInstance(ShipAPI ship) {
			if (!ship.getCustomData().containsKey(DATA_KEY)) {
				goat_HYBRID_Range1 m = new goat_HYBRID_Range1();
				ship.setCustomData(DATA_KEY, m);
				ship.addListener(m);
			}

			return (goat_HYBRID_Range1)ship.getCustomData().get(DATA_KEY);
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
			if (weapon.getSlot().getWeaponType() == WeaponAPI.WeaponType.HYBRID && weapon.getId().contentEquals(WEAPON_ID)) {
				return MAX_RANGE_INCREASE;
			}
			return 0f;
		}
	}
}
