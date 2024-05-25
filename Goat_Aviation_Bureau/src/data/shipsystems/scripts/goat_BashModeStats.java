package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class goat_BashModeStats extends BaseShipSystemScript {

	public static final int MIN_FRAME = 0;
	public static final int MAX_FRAME = 9 - 1;
	public static final int FIRE_WAVE = 3;

	public static final Vector2f ZERO = new Vector2f();

	private String sideString;
	private final Map<WeaponAPI, Float> angles = new HashMap<>();

	private float timer = 0f;

	@Override
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		CombatEngineAPI engine = Global.getCombatEngine();
		if (engine.isPaused()) return;

		ShipAPI ship = (ShipAPI)stats.getEntity();
		if (ship == null) return;
		if (!ship.isAlive()) return;
		if (!engine.isEntityInPlay(ship)) return;

		if (sideString == null) {
			sideString = getSideString(ship);
		}

		float amount = engine.getElapsedInLastFrame();
		float activeDuration = ship.getSystem().getChargeActiveDur() - 0.05f;

		boolean shouldFire = false;
		if (effectLevel >= 1f) {
			float requiredTimer = activeDuration / FIRE_WAVE;
			timer += amount;
			if (timer >= requiredTimer) {
				timer -= requiredTimer;

				shouldFire = true;
			}
		}

		for (WeaponAPI weapon : ship.getUsableWeapons()) {

			if (!weapon.getId().contentEquals("goat_pd")) continue;
			if (!weapon.getSlot().getId().endsWith(sideString)) continue;

			if (!angles.containsKey(weapon)) {
				angles.put(weapon, weapon.getCurrAngle());
			} else {
				weapon.setCurrAngle(angles.get(weapon));
			}

			weapon.setRemainingCooldownTo(weapon.getCooldown() * effectLevel);
			weapon.getAnimation().setFrame((int)(MIN_FRAME + effectLevel * MAX_FRAME));

			if (shouldFire) {
				simFire(weapon);
			}
		}
	}

	private void simFire(WeaponAPI weapon) {
		float angle = weapon.getCurrAngle() + MathUtils.getRandomNumberInRange(0f, 5f) - MathUtils.getRandomNumberInRange(0f, 5f);
		Global.getCombatEngine().spawnProjectile(weapon.getShip(), weapon, "goat_pd_bashmode", weapon.getLocation(), angle, null);
		Global.getSoundPlayer().playSound("all_goat_bombard_fire1", 1.6f, 1.7f, weapon.getLocation(), ZERO);

		// add effect here
	}

	public static String getSideString(ShipAPI ship) {
		float sr = MathUtils.getShortestRotation(ship.getFacing(), VectorUtils.getAngle(ship.getLocation(), ship.getMouseTarget()));
		if (sr > 0f) return "L";
		return "R";
	}

	@Override
	public void unapply(MutableShipStatsAPI stats, String id) {
		sideString = null;
		angles.clear();
		timer = 0f;
	}

	@Override
	public StatusData getStatusData(int index, State state, float effectLevel) {
		return null;
	}
}