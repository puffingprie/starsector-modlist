package data.scripts.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.NegativeExplosionVisual;
import com.fs.starfarer.api.impl.combat.RealityDisruptorChargeGlow;
import com.fs.starfarer.api.impl.combat.RiftCascadeEffect;
import com.fs.starfarer.api.impl.combat.RiftLanceEffect;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.CollisionUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class goat_ProjEveryFramePlugin extends BaseEveryFrameCombatPlugin {

	public static final String PLUGIN_ID = "goat_ProjEveryFramePlugin";

	private CombatEngineAPI engine;

	@Override
	public void init(CombatEngineAPI engine) {
		this.engine = engine;
		engine.getCustomData().put(PLUGIN_ID, new LocalData());
	}

	@Override
	public void advance(float amount, List<InputEventAPI> events) {
		if (engine == null || engine.isPaused()) return;

		final LocalData localData = (LocalData)engine.getCustomData().get(PLUGIN_ID);

		//proj input

		final Map<MissileAPI, GiantHalberdData> giantHalberdData = localData.giantHalberdData;
		final Map<MissileAPI, GiantHalberdMrm1Data> giantHalberdMrm1Data = localData.giantHalberdMrm1Data;
		final Map<MissileAPI, FireFireData> fireFireData = localData.fireFireData;
		final Map<DamagingProjectileAPI, FireFireBData> fireFireBData = localData.fireFireBData;
		final Map<DamagingProjectileAPI, DazzleWarheadData> dazzleWarheadData = localData.dazzleWarheadData;
		final Map<DamagingProjectileAPI, SaxtonAmmoData> saxtonAmmoData = localData.saxtonAmmoData;
		final Map<BeamAPI, SaxtonBeamData> saxtonBeamData = localData.saxtonBeamData;

		List<DamagingProjectileAPI> projs = engine.getProjectiles();
		for (DamagingProjectileAPI proj : projs) {
			if (!engine.isEntityInPlay(proj) || proj.getProjectileSpecId() == null || proj.didDamage() || proj.isFading()) {
				continue;
			}
			if (proj instanceof MissileAPI) {
				MissileAPI missile = (MissileAPI)proj;
				if (missile.getProjectileSpecId().contentEquals("goat_giant_halberd_mrm") && !giantHalberdData.containsKey(missile)) {
					giantHalberdData.put(missile, new GiantHalberdData());
				}
				if (missile.getProjectileSpecId().contentEquals("goat_giant_halberd_mrm1") && !giantHalberdMrm1Data.containsKey(missile)) {
					giantHalberdMrm1Data.put(missile, new GiantHalberdMrm1Data());
				}
				if (missile.getProjectileSpecId().contentEquals("goat_fire_fire_mrm") && !fireFireData.containsKey(missile)) {
					fireFireData.put(missile, new FireFireData());
				}
			}

			if (proj.getProjectileSpecId().contentEquals("goat_dazzle_warhead") && !dazzleWarheadData.containsKey(proj)) {
				dazzleWarheadData.put(proj, new DazzleWarheadData());
			}

			if (proj.getWeapon() != null && proj.getWeapon().getSpec().getType().equals(WeaponAPI.WeaponType.ENERGY)) {
				WeaponAPI weapon = proj.getWeapon();
				WeaponAPI.WeaponSize size = weapon.getSpec().getSize();
				if (size.equals(WeaponAPI.WeaponSize.SMALL) || size.equals(WeaponAPI.WeaponSize.MEDIUM)) {
					if (isSaxState(proj.getSource()) && !saxtonAmmoData.containsKey(proj)) {
						saxtonAmmoData.put(proj, new SaxtonAmmoData(size));
					}
				}
			}

		}

		//giant_halberd operate
		List<MissileAPI> missileRemoveList = new ArrayList<>();
		for (Map.Entry<MissileAPI, GiantHalberdData> entry : giantHalberdData.entrySet()) {
			MissileAPI missile = entry.getKey();
			GiantHalberdData value = entry.getValue();
			if (!engine.isEntityInPlay(missile) || missile.getProjectileSpecId() == null || missile.didDamage() || missile.isFading()) {
				missileRemoveList.add(missile);
				continue;
			}

			//do something here

			value.time += amount;
			if (value.time < 2f) {
				missile.resetEngineGlowBrightness();
                /*
                for(ShipEngineControllerAPI.ShipEngineAPI shipEngine : missile.getEngineController().getShipEngines()){
                    shipEngine.disable();
                    missile.getEngineController().setFlameLevel(shipEngine.getEngineSlot(), 0f);
                }
                missile.getEngineController().forceFlameout();
                 */

			} else if (value.time < 3f) {
				missile.setShineBrightness(1f);
                /*
                for(ShipEngineControllerAPI.ShipEngineAPI shipEngine : missile.getEngineController().getShipEngines()){
                    if(shipEngine.isDisabled()){
                        shipEngine.setHitpoints(shipEngine.getMaxHitpoints());
                    }
                    missile.getEngineController().setFlameLevel(shipEngine.getEngineSlot(), 1f);
                }
                 */

			} else {
				if (!value.spawned) {
					Global.getCombatEngine().spawnProjectile(missile.getSource(), missile.getWeapon(), "all_goat_giant_halberd_s1", missile.getLocation(), missile.getFacing(), new Vector2f());
					Global.getCombatEngine().spawnProjectile(missile.getSource(), missile.getWeapon(), "all_goat_giant_halberd_s2", missile.getLocation(), missile.getFacing(), missile.getVelocity());
					Global.getCombatEngine().spawnProjectile(missile.getSource(), missile.getWeapon(), "all_goat_giant_halberd_s3", missile.getLocation(), missile.getFacing(), new Vector2f(missile.getVelocity().getX() + 20f * (float)(Math.cos(Math.toRadians(missile.getFacing())) - Math.sin(Math.toRadians(missile.getFacing()))), missile.getVelocity().getY() + 20f * (float)(Math.sin(Math.toRadians(missile.getFacing())) + Math.cos(Math.toRadians(missile.getFacing())))));
					Global.getCombatEngine().spawnProjectile(missile.getSource(), missile.getWeapon(), "all_goat_giant_halberd_s4", missile.getLocation(), missile.getFacing(), new Vector2f(missile.getVelocity().getX() + 20f * (float)(Math.cos(Math.toRadians(missile.getFacing())) + Math.sin(Math.toRadians(missile.getFacing()))), missile.getVelocity().getY() + 20f * (float)(Math.sin(Math.toRadians(missile.getFacing())) - Math.cos(Math.toRadians(missile.getFacing())))));

					for (int i = 0; i < 6; i++) {
						Global.getCombatEngine().addSmokeParticle(missile.getLocation(), new Vector2f(missile.getVelocity().getX() + 50f * (Misc.random.nextFloat() - 0.5f), missile.getVelocity().getY() + 50f * (Misc.random.nextFloat() - 0.5f)), 20f, 0.2f, 1f, new Color(40, 40, 40, 200));
					}
					value.spawned = true;
				}
				missileRemoveList.add(missile);
				engine.removeEntity(missile);
			}

		}
		//remove
		if (!missileRemoveList.isEmpty()) {
			for (MissileAPI toRemove : missileRemoveList) {
				giantHalberdData.remove(toRemove);
			}
			missileRemoveList.clear();
		}

		//goat_giant_halberd_mrm1 oprate

		for (Map.Entry<MissileAPI, GiantHalberdMrm1Data> entry : giantHalberdMrm1Data.entrySet()) {

			MissileAPI missile = entry.getKey();
			GiantHalberdMrm1Data value = entry.getValue();
			if (!engine.isEntityInPlay(missile) || missile.getProjectileSpecId() == null || missile.didDamage() || missile.isFading()) {
				missileRemoveList.add(missile);
				continue;
			}

			//do something here

			value.interval.advance(amount);
			if (value.interval.intervalElapsed()) {
				addParticlesRift(missile);
			}
		}

		//remove
		if (!missileRemoveList.isEmpty()) {
			for (MissileAPI toRemove : missileRemoveList) {
				giantHalberdMrm1Data.remove(toRemove);
			}
			missileRemoveList.clear();
		}

		//goat_fire_fire_mrm oprate

		for (Map.Entry<MissileAPI, FireFireData> entry : fireFireData.entrySet()) {

			MissileAPI missile = entry.getKey();
			FireFireData value = entry.getValue();
			if (!engine.isEntityInPlay(missile) || missile.getProjectileSpecId() == null || missile.didDamage() || missile.isFading()) {
				missileRemoveList.add(missile);
				continue;
			}

			//do something here

			if (value.time >= 1f || missile.getWeapon().getSlot().isHidden()) {
				value.forced = true;
				CombatEntityAPI fFB = engine.spawnProjectile(missile.getSource(), missile.getWeapon(), "all_goat_fire_fire_m1", missile.getLocation(), missile.getFacing(), new Vector2f());
				fireFireBData.put((DamagingProjectileAPI)fFB, new FireFireBData());
				engine.removeEntity(missile);
				missileRemoveList.add(missile);
				continue;
			} else {
				value.time += amount;
			}

			if (!value.forced) {
				value.forced = true;
				float acc = 40f;
				float arc = 0f;

				float disAbsFpt0P2 = (float)(Math.pow(missile.getWeapon().getFirePoint(0).x - missile.getLocation().x, 2f) + Math.pow(missile.getWeapon().getFirePoint(0).y - missile.getLocation().y, 2f));
				float disAbsFpt1P2 = (float)(Math.pow(missile.getWeapon().getFirePoint(1).x - missile.getLocation().x, 2f) + Math.pow(missile.getWeapon().getFirePoint(1).y - missile.getLocation().y, 2f));

				if (disAbsFpt0P2 - disAbsFpt1P2 < 0) {
					acc = -acc;
					arc = -arc;
				}

				missile.getVelocity().set(missile.getVelocity().x + acc * (float)Math.sin(Math.toRadians(missile.getFacing() + arc)), missile.getVelocity().y - acc * (float)Math.cos(Math.toRadians(missile.getFacing() + arc)));
			} else {
				missile.getVelocity().set(missile.getVelocity().x * (1f - amount), missile.getVelocity().y * (1f - amount));
			}
		}

		//remove
		if (!missileRemoveList.isEmpty()) {
			for (MissileAPI toRemove : missileRemoveList) {
				fireFireData.remove(toRemove);
			}
			missileRemoveList.clear();
		}

		//goat_dazzle_warhead operate
		List<DamagingProjectileAPI> projRemoveList = new ArrayList<>();
		for (Map.Entry<DamagingProjectileAPI, DazzleWarheadData> entry : dazzleWarheadData.entrySet()) {
			DamagingProjectileAPI proj = entry.getKey();
			DazzleWarheadData value = entry.getValue();
			if (!engine.isEntityInPlay(proj) || proj.getProjectileSpecId() == null || proj.didDamage() || proj.isFading()) {
				projRemoveList.add(proj);
				continue;
			}

			//do something here

			value.interval.advance(amount);
			if (value.interval.intervalElapsed()) {

				for (int i = 0; i < 1; i++) {
					engine.addNebulaParticle(new Vector2f(proj.getLocation().x + 10f * (Misc.random.nextFloat() - 0.5f), proj.getLocation().y + 10f * (Misc.random.nextFloat() - 0.5f)), new Vector2f(proj.getVelocity().x * 0f + 10f * (Misc.random.nextFloat() - 0.5f), proj.getVelocity().y * 0f + 10f * (Misc.random.nextFloat() - 0.5f)), 15f * 5f * (0.5f + (float)Math.random() * 1f), 1.5f, 0.8f, 0f, (1f + (float)Math.random()) * 1f, new Color(200, 225, 255, 50));
				}
			}
		}
		if (!projRemoveList.isEmpty()) {
			for (DamagingProjectileAPI d : projRemoveList) {
				dazzleWarheadData.remove(d);
			}
			projRemoveList.clear();
		}

		//goat_fire_b_ass
		for (Map.Entry<DamagingProjectileAPI, FireFireBData> entry : fireFireBData.entrySet()) {
			DamagingProjectileAPI proj = entry.getKey();
			FireFireBData value = entry.getValue();
			if (!engine.isEntityInPlay(proj) || proj.getProjectileSpecId() == null || proj.didDamage() || proj.isFading()) {
				projRemoveList.add(proj);
				continue;
			}

			//do something here
			Vector2f tp = new Vector2f(proj.getLocation().x + (float)Math.cos(Math.toRadians(proj.getFacing())) * 300f, proj.getLocation().y + (float)Math.sin(Math.toRadians(proj.getFacing())) * 300f);
			for (ShipAPI target : AIUtils.getNearbyEnemies(proj, 1000f)) {
				Vector2f piont = CollisionUtils.getCollisionPoint(proj.getLocation(), tp, target);
				if (piont != null) {
					for (int i = 0; i < 8; i++) {
						DamagingProjectileAPI dp = (DamagingProjectileAPI)engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "all_goat_fire_b", proj.getLocation(), proj.getFacing() + (Misc.random.nextFloat() - 0.5f) * 10f, new Vector2f());
						engine.addSmokeParticle(dp.getLocation(), new Vector2f((float)Math.cos(Math.toRadians(proj.getFacing() + (Misc.random.nextFloat() - 0.5f) * 10f)) * 200f, (float)Math.sin(Math.toRadians(proj.getFacing() + (Misc.random.nextFloat() - 0.5f) * 10f)) * 200f), dp.getCollisionRadius(), 200f / 255f, 1.5f, new Color(100, 100, 100));
						engine.addHitParticle(dp.getLocation(), new Vector2f((float)Math.cos(Math.toRadians(proj.getFacing() + (Misc.random.nextFloat() - 0.5f) * 20f)) * 150f, (float)Math.sin(Math.toRadians(proj.getFacing() + (Misc.random.nextFloat() - 0.5f) * 20f)) * 150f), dp.getCollisionRadius() / 4f, 200f / 255f, 1f, new Color(221, 63, 30));
					}

					Global.getSoundPlayer().playSound("hurricane_mirv_fire", 1f, 1f, proj.getLocation(), new Vector2f());
					engine.removeEntity(proj);
					projRemoveList.add(proj);
					break;
				}
			}

		}
		if (!projRemoveList.isEmpty()) {
			for (DamagingProjectileAPI d : projRemoveList) {
				fireFireBData.remove(d);
			}
			projRemoveList.clear();
		}

		//saxtonAmmoData
		for (Map.Entry<DamagingProjectileAPI, SaxtonAmmoData> entry : saxtonAmmoData.entrySet()) {
			DamagingProjectileAPI proj = entry.getKey();
			SaxtonAmmoData value = entry.getValue();
			if (!engine.isEntityInPlay(proj) || proj.getProjectileSpecId() == null) {
				projRemoveList.add(proj);
				continue;
			}

			//do something here
			if (proj.didDamage() && proj.getDamageTarget() != null) {
				CombatEntityAPI target = proj.getDamageTarget();
				WeaponAPI weapon = proj.getWeapon();

				if (value.size.equals(WeaponAPI.WeaponSize.SMALL)) {

					Color color = getColorForDarkening(proj.getProjectileSpec().getFringeColor());
					Color undercolor = RiftCascadeEffect.EXPLOSION_UNDERCOLOR;
					float size = 75f;
					size *= 0.5f;

					float dur = Math.max(1f, weapon.getSpec().getChargeTime());

					spawnHitDarkening(color, undercolor, proj.getLocation(), target.getVelocity(), size, dur);
					projRemoveList.add(proj);
				} else {
					Color color = new Color(182, 5, 5, 255);

					CombatEntityAPI prev = null;
					for (int i = 0; i < 2; i++) {
						NegativeExplosionVisual.NEParams params = createStandardRiftParams(color, 8f);

						params.radius *= 0.75f + 0.5f * (float)Math.random();

						params.withHitGlow = prev == null;

						Vector2f loc = new Vector2f(proj.getLocation());
						//loc = Misc.getPointWithinRadius(loc, params.radius * 1f);
						loc = Misc.getPointAtRadius(loc, params.radius * 0.4f);

						CombatEntityAPI e = engine.addLayeredRenderingPlugin(new NegativeExplosionVisual(params));
						e.getLocation().set(loc);

						if (prev != null) {
							float dist = Misc.getDistance(prev.getLocation(), loc);
							Vector2f vel = Misc.getUnitVectorAtDegreeAngle(Misc.getAngleInDegrees(loc, prev.getLocation()));
							vel.scale(dist / (params.fadeIn + params.fadeOut) * 0.7f);
							e.getVelocity().set(vel);
						}

						prev = e;
					}
				}
			}
		}
		if (!projRemoveList.isEmpty()) {
			for (DamagingProjectileAPI d : projRemoveList) {
				saxtonAmmoData.remove(d);
			}
			projRemoveList.clear();
		}

		//beams
		for (BeamAPI beam : engine.getBeams()) {
			if (beam.getWeapon().getSpec().getSize().equals(WeaponAPI.WeaponSize.SMALL) || beam.getWeapon().getSpec().getSize().equals(WeaponAPI.WeaponSize.MEDIUM)) {
				if (beam.didDamageThisFrame() && isSaxState(beam.getSource()) && !saxtonBeamData.containsKey(beam)) {
					saxtonBeamData.put(beam, new SaxtonBeamData(beam.getWeapon().getSpec().getSize()));
				}
			}
		}

		List<BeamAPI> beamsToRemove = new ArrayList<>();
		for (Map.Entry<BeamAPI, SaxtonBeamData> entry : saxtonBeamData.entrySet()) {
			BeamAPI beam = entry.getKey();
			SaxtonBeamData value = entry.getValue();
			if (beam == null || value.delay >= 0.5f) {
				beamsToRemove.add(beam);
				continue;
			}
			if (!beam.didDamageThisFrame()) {
				value.delay += amount;
				value.timer += amount;
			} else {
				value.delay = 0f;
				if (value.timer >= 0.3f) {
					value.timer = 0f;

					engine.spawnEmpArc(beam.getSource(), beam.getSource().getLocation(), beam.getSource(), beam.getDamageTarget(), DamageType.FRAGMENTATION, 40f, 500f, 10000f, "tachyon_lance_emp_impact", 16f, beam.getFringeColor(), beam.getCoreColor());
					if (!value.size.equals(WeaponAPI.WeaponSize.SMALL)) {
						float thickness = 30f;
						float coreWidthMult = 0.67f;
						Color color = beam.getWeapon().getSpec().getGlowColor();

						Vector2f from = new Vector2f(beam.getTo());
						float range = 300f;
						Vector2f dir = Misc.getUnitVectorAtDegreeAngle((float)Math.random() * 360f);
						dir.scale(range);
						Vector2f.add(from, dir, dir);
						dir = Misc.getPointWithinRadius(dir, range * 0.8f);
						Vector2f to = dir;

						EmpArcEntityAPI arc = engine.spawnEmpArcVisual(from, null, to, null, thickness, color, Color.white);
						arc.setCoreWidthOverride(thickness * coreWidthMult);
						Global.getSoundPlayer().playSound("realitydisruptor_emp_impact", 0.3f, 0.3f, to, new Vector2f());

						float facing = beam.getWeapon().getArcFacing();
						//spawnEMPParticles(RealityDisruptorChargeGlow.EMPArcHitType.SOURCE, from, null, facing);
						spawnEMPParticles(RealityDisruptorChargeGlow.EMPArcHitType.DEST_NO_TARGET, to, null, facing);

					}

				}
			}

		}

		if (!beamsToRemove.isEmpty()) {
			for (BeamAPI b : beamsToRemove) {
				saxtonBeamData.remove(b);
			}
			beamsToRemove.clear();
		}
		//engine.maintainStatusForPlayerShip("0", "graphics/icons/blackhole.png", "saxtonBeamData", "" + saxtonBeamData.size(), false);

	}

	public static final class LocalData {

		public final Map<MissileAPI, GiantHalberdData> giantHalberdData = new HashMap<>();
		public final Map<MissileAPI, GiantHalberdMrm1Data> giantHalberdMrm1Data = new HashMap<>();
		public final Map<MissileAPI, FireFireData> fireFireData = new HashMap<>();
		public final Map<DamagingProjectileAPI, FireFireBData> fireFireBData = new HashMap<>();
		public final Map<DamagingProjectileAPI, DazzleWarheadData> dazzleWarheadData = new HashMap<>();
		public final Map<DamagingProjectileAPI, SaxtonAmmoData> saxtonAmmoData = new HashMap<>();
		public final Map<BeamAPI, SaxtonBeamData> saxtonBeamData = new HashMap<>();
	}

	public static final class GiantHalberdData {

		public float time;
		public boolean spawned;

		public GiantHalberdData() {
			this.time = 0f;
			this.spawned = false;
		}
	}

	public static final class GiantHalberdMrm1Data {

		public IntervalUtil interval;

		public GiantHalberdMrm1Data() {
			this.interval = new IntervalUtil(0.01f, 0.03f);
		}
	}

	public static final class FireFireData {

		public boolean forced;
		public float time;

		public FireFireData() {
			this.forced = false;
			this.time = 0f;
		}
	}

	public static final class FireFireBData {

		public float time;

		public FireFireBData() {
			this.time = 0f;
		}
	}

	public static final class DazzleWarheadData {

		public boolean spawned;
		public IntervalUtil interval;

		public DazzleWarheadData() {
			this.spawned = false;
			this.interval = new IntervalUtil(0.01f, 0.03f);
		}
	}

	private static final class SaxtonAmmoData {

		private final WeaponAPI.WeaponSize size;

		private SaxtonAmmoData(WeaponAPI.WeaponSize size) {
			this.size = size;
		}
	}

	private static final class SaxtonBeamData {

		private final WeaponAPI.WeaponSize size;
		private float timer = 100f;
		private float delay = 0f;

		private SaxtonBeamData(WeaponAPI.WeaponSize size) {
			this.size = size;
		}
	}

	public void addParticlesRift(MissileAPI missile) {
		CombatEngineAPI engine = Global.getCombatEngine();
		Color c = RiftLanceEffect.getColorForDarkening(RiftCascadeEffect.STANDARD_RIFT_COLOR);
		Color undercolor = RiftCascadeEffect.EXPLOSION_UNDERCOLOR;

		float b = missile.getCurrentBaseAlpha();
		c = Misc.scaleAlpha(c, b);
		undercolor = Misc.scaleAlpha(undercolor, b);

		float baseDuration = 4f;
		float size = 30f;
		size = missile.getSpec().getGlowRadius() * 0.5f;

		Vector2f point = new Vector2f(missile.getLocation());
		Vector2f pointOffset = new Vector2f(missile.getVelocity());
		pointOffset.scale(0.1f);
		Vector2f.add(point, pointOffset, point);

		Vector2f vel = new Vector2f();

		for (int i = 0; i < 1; i++) {
			float dur = baseDuration + baseDuration * (float)Math.random();
			float nSize = size;
			Vector2f pt = Misc.getPointWithinRadius(point, nSize * 0.5f);
			Vector2f v = Misc.getUnitVectorAtDegreeAngle((float)Math.random() * 360f);
			v.scale(nSize + nSize * (float)Math.random() * 0.5f);
			v.scale(0.2f);
			Vector2f.add(vel, v, v);

			float maxSpeed = nSize * 1.5f * 0.2f;
			float minSpeed = nSize * 1f * 0.2f;
			float overMin = v.length() - minSpeed;
			if (overMin > 0) {
				float durMult = 1f - overMin / (maxSpeed - minSpeed);
				if (durMult < 0.1f) durMult = 0.1f;
				dur *= 0.5f + 0.5f * durMult;
			}
			engine.addNegativeNebulaParticle(pt, v, nSize * 1f, 2f, 0.5f, 0f, dur, c);
		}

		float dur = baseDuration;
		float rampUp = 0f;
		rampUp = 0.5f;
		c = undercolor;
		for (int i = 0; i < 2; i++) {
			Vector2f loc = new Vector2f(point);
			loc = Misc.getPointWithinRadius(loc, size * 1f);
			float s = size * 3f * (0.5f + (float)Math.random() * 0.5f);
			engine.addNebulaParticle(loc, vel, s, 1.5f, rampUp, 0f, dur, c);
		}
	}

	public void spawnHitDarkening(Color color, Color undercolor, Vector2f point, Vector2f vel, float size, float baseDuration) {
		CombatEngineAPI engine = Global.getCombatEngine();
		if (!engine.getViewport().isNearViewport(point, 100f + size * 2f)) return;

		Color c = color;

		for (int i = 0; i < 5; i++) {
			float dur = baseDuration + baseDuration * (float)Math.random();
			float nSize = size;
			Vector2f pt = Misc.getPointWithinRadius(point, nSize * 0.5f);
			Vector2f v = Misc.getUnitVectorAtDegreeAngle((float)Math.random() * 360f);
			v.scale(nSize + nSize * (float)Math.random() * 0.5f);
			v.scale(0.2f);
			Vector2f.add(vel, v, v);

			float maxSpeed = nSize * 1.5f * 0.2f;
			float minSpeed = nSize * 1f * 0.2f;
			float overMin = v.length() - minSpeed;
			if (overMin > 0) {
				float durMult = 1f - overMin / (maxSpeed - minSpeed);
				if (durMult < 0.1f) durMult = 0.1f;
				dur *= 0.5f + 0.5f * durMult;
			}
			engine.addNegativeNebulaParticle(pt, v, nSize * 1f, 2f, 0.5f / dur, 0f, dur, c);
		}

		float dur = baseDuration;
		float rampUp = 0f;
		c = undercolor;
		for (int i = 0; i < 12; i++) {
			Vector2f loc = new Vector2f(point);
			loc = Misc.getPointWithinRadius(loc, size * 1f);
			float s = size * 3f * (0.5f + (float)Math.random() * 0.5f);
			engine.addNebulaParticle(loc, vel, s, 1.5f, rampUp, 0f, dur, c);
		}
	}

	public static Color getColorForDarkening(Color from) {
		Color c = new Color(255 - from.getRed(), 255 - from.getGreen(), 255 - from.getBlue(), 127);
		c = Misc.interpolateColor(c, Color.white, 0.4f);
		return c;
	}

	public static NegativeExplosionVisual.NEParams createStandardRiftParams(Color borderColor, float radius) {
		NegativeExplosionVisual.NEParams p = new NegativeExplosionVisual.NEParams();
		p.hitGlowSizeMult = .75f;
		p.spawnHitGlowAt = 0f;
		p.noiseMag = 1f;
		p.fadeIn = 0.1f;
		p.underglow = RiftCascadeEffect.EXPLOSION_UNDERCOLOR;
		p.withHitGlow = true;
		p.radius = radius;
		p.color = borderColor;
		p.fadeOut = 1f;
		return p;
	}

	public void spawnEMPParticles(RealityDisruptorChargeGlow.EMPArcHitType type, Vector2f point, CombatEntityAPI target, float facing) {
		CombatEngineAPI engine = Global.getCombatEngine();

		Color color = RiftLanceEffect.getColorForDarkening(RiftCascadeEffect.STANDARD_RIFT_COLOR);

		float size = 30f;
		float baseDuration = 1.5f;
		Vector2f vel = new Vector2f();
		int numNegative = 5;
		switch (type) {
			case DEST:
				size = 50f;
				vel.set(target.getVelocity());
				break;
			case DEST_NO_TARGET:
				break;
			case SOURCE:
				size = 40f;
				numNegative = 10;
				break;
		}
		Vector2f dir = Misc.getUnitVectorAtDegreeAngle(facing + 180f);
		//dir.negate();
		//numNegative = 0;
		for (int i = 0; i < numNegative; i++) {
			float dur = baseDuration + baseDuration * (float)Math.random();
			//float nSize = size * (1f + 0.0f * (float) Math.random());
			//float nSize = size * (0.75f + 0.5f * (float) Math.random());
			float nSize = size;
			if (type == RealityDisruptorChargeGlow.EMPArcHitType.SOURCE) {
				nSize *= 1.5f;
			}
			Vector2f pt = Misc.getPointWithinRadius(point, nSize * 0.5f);
			Vector2f v = Misc.getUnitVectorAtDegreeAngle((float)Math.random() * 360f);
			v.scale(nSize + nSize * (float)Math.random() * 0.5f);
			v.scale(0.2f);

			float endSizeMult = 2f;
			if (type == RealityDisruptorChargeGlow.EMPArcHitType.SOURCE) {
				pt = Misc.getPointWithinRadius(point, nSize * 0f);
				Vector2f offset = new Vector2f(dir);
				offset.scale(size * 0.2f * i);
				Vector2f.add(pt, offset, pt);
				endSizeMult = 1.5f;
				v.scale(0.5f);
			}
			Vector2f.add(vel, v, v);

			float maxSpeed = nSize * 1.5f * 0.2f;
			float minSpeed = nSize * 1f * 0.2f;
			float overMin = v.length() - minSpeed;
			if (overMin > 0) {
				float durMult = 1f - overMin / (maxSpeed - minSpeed);
				if (durMult < 0.1f) durMult = 0.1f;
				dur *= 0.5f + 0.5f * durMult;
			}
			engine.addNegativeNebulaParticle(pt, v, nSize * 1f, endSizeMult,
					//engine.addNegativeSwirlyNebulaParticle(pt, v, nSize * 1f, endSizeMult,
					0.25f / dur, 0f, dur, color);
		}

		float dur = baseDuration;
		float rampUp = 0.5f / dur;
		color = RiftCascadeEffect.EXPLOSION_UNDERCOLOR;
		for (int i = 0; i < 7; i++) {
			Vector2f loc = new Vector2f(point);
			loc = Misc.getPointWithinRadius(loc, size * 1f);
			float s = size * 4f * (0.5f + (float)Math.random() * 0.5f);
			engine.addSwirlyNebulaParticle(loc, vel, s, 1.5f, rampUp, 0f, dur, color, false);
		}
	}

	public static final String SAX_STATE = "goat_sax_state";

	public static boolean isSaxState(ShipAPI ship) {
		if (ship == null) return false;
		if (ship.getCustomData().containsKey(SAX_STATE)) return (boolean)ship.getCustomData().get(SAX_STATE);
		return false;
	}

	public static void setSaxState(ShipAPI ship) {
		ship.setCustomData(SAX_STATE, true);
	}
}