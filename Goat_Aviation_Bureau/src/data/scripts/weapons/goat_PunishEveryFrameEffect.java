package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicFakeBeam;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

public class goat_PunishEveryFrameEffect implements EveryFrameWeaponEffectPlugin {

	public static final String ID = "goat_PunishEveryFrameEffect";

	public static final float TARGET_RANGE = 10f;
	public static final float RIFT_RANGE = 5f;

	private int fire = 0;
	protected IntervalUtil interval = new IntervalUtil(0.6f, 1.2f);

	public goat_PunishEveryFrameEffect() {
		interval.setElapsed((float)Math.random() * interval.getIntervalDuration());
	}

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

		if (weapon.getShip() == null) return;

		if (weapon.getChargeLevel() >= 0.09f && weapon.getChargeLevel() <= 0.11f && weapon.getCooldownRemaining() <= 0f) {

			Vector2f firePoint = weapon.getFirePoint(0);
			//engine.spawnEmpArcVisual(firePoint,weapon.getShip(),MathUtils.getRandomPointInCircle(firePoint,110f),weapon.getShip(),12f,new Color(210, 99, 52, 245),new Color(183, 59, 35, 221);

			for (int i = 0; i < 6; i = i + 1) {
				engine.addNebulaParticle(firePoint, MathUtils.getRandomPointInCircle(new Vector2f(), MathUtils.getRandomNumberInRange(55f, 65f)), MathUtils.getRandomNumberInRange(60f, 80f), 0.6f, 2.2f, 0.6f, 0.3f, new Color(169, 45, 23, 255)

				);
			}

			Global.getSoundPlayer().playSound("all_goat_punish_charge", 0.6f, 0.8f, weapon.getLocation(), new Vector2f());
			Vector2f fire = weapon.getFirePoint(0);
		}

		if (weapon.getChargeLevel() >= 1.0f) {
			Vector2f firePoint = weapon.getFirePoint(0);
			for (int i = 0; i < 1; i = i + 1) {
				engine.addNegativeSwirlyNebulaParticle(firePoint, MathUtils.getRandomPointInCircle(new Vector2f(), MathUtils.getRandomNumberInRange(15f, 158f)), MathUtils.getRandomNumberInRange(0f, 43f), 1.6f, 0.2f, 0.1f, 1.0f, new Color(143, 233, 236, 225)

				);
			}

		}

		if (weapon.isFiring()) {
			if (weapon.getCooldownRemaining() <= 0f) {

				Vector2f firePoint = weapon.getFirePoint(0);

				for (int i = 0; i < 1; i = i + 1) {
					engine.addNebulaParticle(firePoint, MathUtils.getRandomPointInCircle(new Vector2f(), MathUtils.getRandomNumberInRange(55f, 65f)), MathUtils.getRandomNumberInRange(60f, 80f), 0.6f, 1.2f, 0.6f, 0.4f, new Color(169, 23, 23, 155)

					);
					engine.addHitParticle(firePoint, MathUtils.getRandomPointInCircle(new Vector2f(), MathUtils.getRandomNumberInRange(155f, 165f)), MathUtils.getRandomNumberInRange(60f, 80f), 0.2f, 0.7f, new Color(169, 57, 23, 23)

					);
				}

				for (int i = 0; i < 4; i++) {
					if (fire <= i && weapon.getChargeLevel() >= 0.2f * (i + 1f)) {
						fire += 1;
						MagicFakeBeam.spawnAdvancedFakeBeam(engine, weapon.getFirePoint(0), weapon.getRange(), weapon.getCurrAngle(), 5f, 5f, -5f, "goat_beam_weave_core", "goat_beam_weave_fringe", 32f, 128f, 0f, 0f, 0.1f, 0.4f, 5f, new Color(162, 22, 22, 255), new Color(221, 75, 30, 255), 50f, weapon.getDamage().getType(), 0f, weapon.getShip());
						break;
					}
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

			Vector2f loc;
			CombatEntityAPI target = findTarget(beam, beam.getWeapon(), engine);
			if (target == null) {
				loc = pickNoTargetDest(beam, beam.getWeapon(), engine);
			} else {
				loc = target.getLocation();
			}

			Vector2f from = Misc.closestPointOnSegmentToPoint(beam.getFrom(), beam.getRayEndPrevFrame(), loc);
			Vector2f to = Misc.getUnitVectorAtDegreeAngle(Misc.getAngleInDegrees(from, loc));
			//to.scale(Math.max(RIFT_RANGE * 0.5f, Math.min(Misc.getDistance(from, loc), RIFT_RANGE)));
			to.scale(Math.min(Misc.getDistance(from, loc), RIFT_RANGE));
			Vector2f.add(from, to, to);

			spawnMine(beam.getSource(), to);
			//			float thickness = beam.getWidth();
			//			EmpArcEntityAPI arc = engine.spawnEmpArcVisual(from, null, to, null, thickness, beam.getFringeColor(), Color.white);
			//			arc.setCoreWidthOverride(Math.max(20f, thickness * 0.67f));
			//Global.getSoundPlayer().playSound("tachyon_lance_emp_impact", 1f, 1f, arc.getLocation(), arc.getVelocity());
		}

	}

	public void spawnMine(ShipAPI source, Vector2f mineLoc) {
		CombatEngineAPI engine = Global.getCombatEngine();

		//Vector2f currLoc = mineLoc;
		MissileAPI mine = (MissileAPI)engine.spawnProjectile(source, null, "all_goat_punish_minelayer", mineLoc, (float)Math.random() * 360f, null);
		if (source != null) {
			Global.getCombatEngine().applyDamageModifiersToSpawnedProjectileWithNullWeapon(source, WeaponType.MISSILE, false, mine.getDamage());
		}

		float fadeInTime = 0.1f;
		mine.getVelocity().scale(0);
		mine.fadeOutThenIn(fadeInTime);

		float liveTime = 0f;
		//liveTime = 0.01f;
		mine.setFlightTime(mine.getMaxFlightTime() - liveTime);
		mine.addDamagedAlready(source);
		mine.setNoMineFFConcerns(true);
	}

	public Vector2f pickNoTargetDest(BeamAPI beam, WeaponAPI weapon, CombatEngineAPI engine) {
		Vector2f from = beam.getFrom();
		Vector2f to = beam.getRayEndPrevFrame();
		float length = beam.getLengthPrevFrame();

		float f = 0.65f + (float)Math.random() * 0.35f;
		Vector2f loc = Misc.getUnitVectorAtDegreeAngle(Misc.getAngleInDegrees(from, to));
		loc.scale(length * f);
		Vector2f.add(from, loc, loc);

		return Misc.getPointWithinRadius(loc, RIFT_RANGE);
	}

	public CombatEntityAPI findTarget(BeamAPI beam, WeaponAPI weapon, CombatEngineAPI engine) {
		Vector2f to = beam.getRayEndPrevFrame();

		Iterator<Object> iter = Global.getCombatEngine().getAllObjectGrid().getCheckIterator(to, RIFT_RANGE * 3f, RIFT_RANGE * 8f);
		int owner = weapon.getShip().getOwner();
		WeightedRandomPicker<CombatEntityAPI> picker = new WeightedRandomPicker<>();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (!(o instanceof MissileAPI) && !(o instanceof ShipAPI)) continue;
			CombatEntityAPI other = (CombatEntityAPI)o;
			if (other.getOwner() == owner) continue;
			if (other instanceof ShipAPI) {
				ShipAPI ship = (ShipAPI)other;
				if (!ship.isFighter() && !ship.isDrone()) continue;
			}

			float radius = Misc.getTargetingRadius(to, other, false);
			Vector2f p = Misc.closestPointOnSegmentToPoint(beam.getFrom(), beam.getRayEndPrevFrame(), other.getLocation());
			float dist = Misc.getDistance(p, other.getLocation()) - radius;
			if (dist > TARGET_RANGE) continue;

			picker.add(other);

		}
		return picker.pick();
	}
}