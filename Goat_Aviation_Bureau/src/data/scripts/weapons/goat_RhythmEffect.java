package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lwjgl.util.vector.Vector2f;

public class goat_RhythmEffect implements BeamEffectPlugin {

	private final IntervalUtil fireInterval = new IntervalUtil(0.2f, 0.3f);
	private boolean wasZero = true;

	@Override
	public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
		CombatEntityAPI target = beam.getDamageTarget();
		if (target instanceof ShipAPI && beam.getBrightness() >= 1f) {
			float dur = beam.getDamage().getDpsDuration();
			// needed because when the ship is in fast-time, dpsDuration will not be reset every frame as it should be
			if (!wasZero) dur = 0;
			wasZero = beam.getDamage().getDpsDuration() <= 0;
			fireInterval.advance(dur);
			if (fireInterval.intervalElapsed()) {
				ShipAPI ship = (ShipAPI)target;
				boolean hitShield = target.getShield() != null && target.getShield().isWithinArc(beam.getTo());
				float pierceChance = ((ShipAPI)target).getHardFluxLevel() - 0.1f;
				pierceChance *= ship.getMutableStats().getDynamic().getValue(Stats.SHIELD_PIERCED_MULT);

				boolean piercedShield = hitShield && (float)Math.random() < pierceChance;
				//piercedShield = true;

				if (!hitShield || piercedShield) {
					Vector2f point = beam.getRayEndPrevFrame();
					float emp = beam.getDamage().getFluxComponent() * 2.5f;
					float dam = beam.getDamage().getDamage() * 1.1f;
					engine.spawnEmpArcPierceShields(beam.getSource(), point, beam.getDamageTarget(), beam.getDamageTarget(), DamageType.ENERGY, dam, // damage
							emp, // emp
							100000f, // max range
							"null", beam.getWidth() + 5f, beam.getFringeColor(), beam.getCoreColor());
				}
			}
		}
		//			Global.getSoundPlayer().playLoop("system_emp_emitter_loop",
		//											 beam.getDamageTarget(), 1.5f, beam.getBrightness() * 0.5f,
		//											 beam.getTo(), new Vector2f());
	}
}
