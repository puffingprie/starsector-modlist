package data.scripts.weapons;

import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.List;

public class goat_InvasionEveryFrameEffect implements EveryFrameWeaponEffectPlugin {

	public static final String ID = "goat_InvasionEveryFrameEffect";

	public static final float TARGET_RANGE = 10f;
	public static final float RIFT_RANGE = 5f;

	private int fire = 0;
	protected IntervalUtil interval = new IntervalUtil(0.6f, 1.2f);

	public goat_InvasionEveryFrameEffect() {
		interval.setElapsed((float)Math.random() * interval.getIntervalDuration());
	}

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

		if (weapon.getShip() == null) return;

		if (weapon.getChargeLevel() >= 0.09f && weapon.getChargeLevel() <= 0.11f && weapon.getCooldownRemaining() <= 0f) {

			//engine.spawnEmpArcVisual(firePoint,weapon.getShip(),MathUtils.getRandomPointInCircle(firePoint,110f),weapon.getShip(),12f,new Color(210, 99, 52, 245),new Color(183, 59, 35, 221);

			Vector2f fire = weapon.getFirePoint(0);
		}

		if (weapon.getChargeLevel() >= 0.5f || weapon.getCooldownRemaining() >= 0.01f) {
			Vector2f firePoint = weapon.getFirePoint(0);
			for (int i = 0; i < 1; i = i + 1) {
				engine.addHitParticle(firePoint, MathUtils.getRandomPointInCircle(new Vector2f(), MathUtils.getRandomNumberInRange(5f, 58f)), MathUtils.getRandomNumberInRange(20f, 43f), 4.0f, 0.03f, 0.05f, new Color(184, 227, 229, 225));
			}

		}

		if (weapon.isFiring()) {
			if (weapon.getCooldownRemaining() <= 0f) {

				Vector2f firePoint = weapon.getFirePoint(0);

				for (int i = 0; i < 1; i = i + 1) {
					engine.addNebulaParticle(firePoint, MathUtils.getRandomPointInCircle(new Vector2f(), MathUtils.getRandomNumberInRange(25f, 65f)), MathUtils.getRandomNumberInRange(20f, 30f), 0.5f, 1.2f, 0.6f, 0.5f, new Color(155, 143, 143, 90)

					);
				}

			} else if (!weapon.isFiring() && weapon.getCooldownRemaining() <= 0f && fire != 0) {
				fire = 0;
			}
		} else if (!weapon.isFiring() && weapon.getCooldownRemaining() <= 0f && fire != 0) {
			fire = 0;
		}

		List<BeamAPI> beams = weapon.getBeams();
		if (beams.isEmpty()) return;
		BeamAPI beam = beams.get(0);
		if (beam.getBrightness() < 1f) return;

		interval.advance(amount * 80f);
		if (interval.intervalElapsed()) {
			if (beam.getLengthPrevFrame() < 10) return;

		}

	}
}