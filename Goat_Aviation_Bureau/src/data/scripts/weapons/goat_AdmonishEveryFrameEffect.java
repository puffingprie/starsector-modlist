package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class goat_AdmonishEveryFrameEffect implements EveryFrameWeaponEffectPlugin {

	public static final float MAX_RANGE_INCREASE = 200f;
	public static final String WEAPON_ID = "all_goat_admonish";

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

		if (weapon.getChargeLevel() >= 1f) {
			Vector2f firePoint = weapon.getFirePoint(0);
			for (int i = 0; i < 6; i++) {
				engine.addSwirlyNebulaParticle(firePoint, MathUtils.getRandomPointInCircle(new Vector2f(), MathUtils.getRandomNumberInRange(15f, 158f)), MathUtils.getRandomNumberInRange(0f, 43f), 1.6f, 0.2f, 0.1f, 1.0f, new Color(194, 72, 20, 225), true);
			}
		}

		ShipAPI ship = weapon.getShip();
		if (ship == null) return;
		if (!ship.isAlive()) return;

		if (engine.isPaused()) return;

		goat_HYBRID_Range2 m = goat_HYBRID_Range2.getInstance(ship);

	}

	public static class goat_HYBRID_Range2 implements WeaponBaseRangeModifier {

		public static final String DATA_KEY = goat_HYBRID_Range2.class.getName() + WEAPON_ID;

		public static goat_HYBRID_Range2 getInstance(ShipAPI ship) {
			if (!ship.getCustomData().containsKey(DATA_KEY)) {
				goat_HYBRID_Range2 m = new goat_HYBRID_Range2();
				ship.setCustomData(DATA_KEY, m);
				ship.addListener(m);
			}

			return (goat_HYBRID_Range2)ship.getCustomData().get(DATA_KEY);
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
