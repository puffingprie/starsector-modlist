package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;

import java.util.HashMap;
import java.util.Map;

public class goat_GnawDecoEffect implements EveryFrameWeaponEffectPlugin {

	public static final String ID = "goat_GnawDecoEffect";

	private float aniLevel = 0f;
	private boolean init = false;

	public static final float MAX_RANGE_INCREASE = 300f;
	public static final String WEAPON_ID = "all_goat_gnaw";

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
		if (!engine.getCustomData().containsKey(ID)) {
			engine.getCustomData().put(ID, new LocalData());
		} else if (!((LocalData)engine.getCustomData().get(ID)).fireData.containsKey(weapon)) {
			((LocalData)engine.getCustomData().get(ID)).fireData.put(weapon, new FireData());
		}

		float rate = 15f * weapon.getShip().getMutableStats().getBallisticRoFMult().getModifiedValue();

		if (!weapon.isFiring() || weapon.getCooldownRemaining() <= 0f) {
			init = false;
		} else if (weapon.isFiring() && weapon.getChargeLevel() < 1f && weapon.getCooldownRemaining() > 0f && !init) {
			if (weapon.getAnimation().getFrame() == 0) {
				aniLevel += rate * amount;
				if (aniLevel >= 1f) {
					aniLevel -= 1f;
					init = true;
					weapon.getAnimation().setFrame(1);
				}
			} else {
				init = true;
			}
		} else if (weapon.getAnimation().getFrame() != 0) {
			aniLevel += rate * amount;
			if (aniLevel >= 1f) {
				aniLevel -= 1f;
				if (weapon.getAnimation().getFrame() < weapon.getAnimation().getNumFrames() - 1) {
					weapon.getAnimation().setFrame(weapon.getAnimation().getFrame() + 1);
				} else {
					weapon.getAnimation().setFrame(0);
				}
			}
		}

		ShipAPI ship = weapon.getShip();
		if (ship == null) return;
		if (!ship.isAlive()) return;

		if (engine.isPaused()) return;

		goat_HYBRID_Range3 m = goat_HYBRID_Range3.getInstance(ship);

	}

	protected static final class LocalData {

		protected final Map<WeaponAPI, FireData> fireData = new HashMap<>();
	}

	protected static final class FireData {

		protected boolean more;

		protected FireData() {
			more = false;
		}
	}

	public static class goat_HYBRID_Range3 implements WeaponBaseRangeModifier {

		public static final String DATA_KEY = goat_HYBRID_Range3.class.getName() + WEAPON_ID;

		public static goat_HYBRID_Range3 getInstance(ShipAPI ship) {
			if (!ship.getCustomData().containsKey(DATA_KEY)) {
				goat_HYBRID_Range3 m = new goat_HYBRID_Range3();
				ship.setCustomData(DATA_KEY, m);
				ship.addListener(m);
			}

			return (goat_HYBRID_Range3)ship.getCustomData().get(DATA_KEY);
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