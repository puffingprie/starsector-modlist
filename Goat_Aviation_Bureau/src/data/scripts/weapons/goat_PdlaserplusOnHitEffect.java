package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class goat_PdlaserplusOnHitEffect implements BeamEffectPlugin {

	private boolean empEnabled = true;

	@Override
	public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
		CombatEntityAPI target = beam.getDamageTarget();

		if (target instanceof ShipAPI && ((ShipAPI)target).isFighter()) {
			Vector2f point = beam.getRayEndPrevFrame();
			float emp = beam.getDamage().getDamage() * 4.1f;
			float dam = beam.getDamage().getDamage() * 2.5f;
			if (empEnabled) {
				engine.addNebulaParticle(point, MathUtils.getRandomPointInCircle(new Vector2f(), MathUtils.getRandomNumberInRange(55f, 65f)), MathUtils.getRandomNumberInRange(40f, 60f), 2.2f, 1.2f, 0.6f, 1.0f, new Color(169, 23, 23, 125));
				engine.addSwirlyNebulaParticle(point, MathUtils.getRandomPointInCircle(new Vector2f(), MathUtils.getRandomNumberInRange(55f, 65f)), MathUtils.getRandomNumberInRange(40f, 60f), 0.8f, 1.2f, 0.6f, 1.0f, new Color(213, 88, 75, 225), true);

				engine.spawnEmpArcPierceShields(beam.getSource(), point, beam.getDamageTarget(), beam.getDamageTarget(), DamageType.ENERGY, dam, // damage
						emp, // emp
						100000f, // max range
						"null", beam.getWidth() + 5f, new Color(54, 23, 23, 165), new Color(59, 32, 32, 165));

				empEnabled = false;
			}
		}

		if (target instanceof MissileAPI) {
			Vector2f point = beam.getRayEndPrevFrame();
			float dam1 = beam.getDamage().getDamage() * 35.0f;

			if (empEnabled) {
				engine.addNebulaParticle(point, MathUtils.getRandomPointInCircle(new Vector2f(), MathUtils.getRandomNumberInRange(55f, 65f)), MathUtils.getRandomNumberInRange(40f, 60f), 2.2f, 1.2f, 0.6f, 1.4f, new Color(162, 32, 13, 125));
				engine.addSwirlyNebulaParticle(point, MathUtils.getRandomPointInCircle(new Vector2f(), MathUtils.getRandomNumberInRange(55f, 65f)), MathUtils.getRandomNumberInRange(40f, 60f), 0.8f, 1.2f, 0.6f, 1.4f, new Color(224, 76, 8, 225), true);

				engine.applyDamage(beam.getDamageTarget(), point, dam1, // damage
						DamageType.ENERGY, 0, // emp
						false, false, null, false);

				empEnabled = false;
			}
		}
	}
}
