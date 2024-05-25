package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.CollisionUtils;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicAnim;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class goat_MoonStats extends BaseShipSystemScript {

	public static final float VORTEX_SHOWN_THRESHOLD = 0.75f;
	private static final float MOON_HIDDEN_THRESHOLD = 0.95f;
	private static final float DAMAGE_INTERVAL = 0.25f; // 每x秒造成一次伤害
	private static final float BASE_DAMAGE = 20f; //基础伤害
	private static final float EFFECT_RANGE = 1500f;
	private goat_MoonSystemPlugin plugin;

	private static Map mag = new HashMap();
	public static Object KEY_SHIP = new Object();
	public static float REPAIR_RATE_MULT = 3f;
	public static final float DAMAGE_BONUS_PERCENT = 80f;
	public static float a = 0;
	public static float X = 0;

	private float ExplosionTimer = 0f;
	private float ExplosionTimerfast = 0f;
	private float ExplosionTimerSlow = 0f;

	private float ExplosionTimer1 = 0f;
	private float ExplosionTimerfast1 = 0f;
	private float ExplosionTimerSlow1 = 0f;

	static {
		mag.put(ShipAPI.HullSize.FIGHTER, 0.75f);
		mag.put(ShipAPI.HullSize.FRIGATE, 0.75f);
		mag.put(ShipAPI.HullSize.DESTROYER, 0.75f);
		mag.put(ShipAPI.HullSize.CRUISER, 0.75f);
		mag.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.9f);
	}

	protected Object STATUSKEY1 = new Object();
	protected Object STATUSKEY2 = new Object();
	protected Object STATUSKEY3 = new Object();

	//public static final float INCOMING_DAMAGE_MULT = 0.25f;
	//public static final float INCOMING_DAMAGE_CAPITAL = 0.5f;

	public static class TargetData {

		public ShipAPI target;

		public TargetData(ShipAPI target) {
			this.target = target;
		}
	}

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		if (plugin == null && stats.getEntity() instanceof ShipAPI) {
			plugin = new goat_MoonSystemPlugin((ShipAPI)stats.getEntity());
			Global.getCombatEngine().addLayeredRenderingPlugin(plugin);
		}

		float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		stats.getEnergyWeaponDamageMult().modifyPercent(id, bonusPercent);

		//effectLevel = 1f;

		CombatEngineAPI engine = Global.getCombatEngine();

		ShipAPI ship = null;
		boolean player = false;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI)stats.getEntity();
			player = ship == Global.getCombatEngine().getPlayerShip();
		} else {
			return;
		}

		ship.setMass(Math.min(40000f, 5 * ship.getMass()));

		ship.fadeToColor(KEY_SHIP, new Color(134, 173, 189, 255), 0.1f, 0.1f, effectLevel);
		a = MathUtils.getRandomNumberInRange(-3.1415f, 3.1415f);
		X = 0.3f * effectLevel;

		if (ship.getSystem().isChargeup() && effectLevel < VORTEX_SHOWN_THRESHOLD) {
			if (ExplosionTimerfast >= 0.005f) {
				ExplosionTimerfast = 0f;
				engine.addNebulaParticle(moveVec(ship.getLocation(), (float)(480.1 * Math.cos(a)), (float)(480.0 * Math.sin(a)), ship.getFacing()), moveVec(new Vector2f(MathUtils.getRandomNumberInRange((float)(-90f * Math.cos(a)), (float)(-60f * Math.cos(a))), MathUtils.getRandomNumberInRange((float)(-90f * Math.sin(a)), (float)(-60f * Math.sin(a)))), MathUtils.getRandomNumberInRange((float)(-340f * Math.cos(a)), (float)(-20f * Math.cos(a))), MathUtils.getRandomNumberInRange((float)(-340f * Math.sin(a)), (float)(-20f * Math.sin(a))), ship.getFacing()), MathUtils.getRandomNumberInRange(45f, 410f * X), 2f, 0.2f, 0.1f, 0.5f, new Color(32, 68, 68, 220));
				engine.addHitParticle(moveVec(ship.getLocation(), (float)(410.1 * Math.cos(a * a)), (float)(410.0 * Math.sin(a * a)), ship.getFacing()), ship.getVelocity(), 90f, 1f, 0.3f, new Color(14, 51, 87, 250));

			} else {
				ExplosionTimerfast += engine.getElapsedInLastFrame();
			}

			if (ExplosionTimerSlow >= 0.7f) {
				ExplosionTimerSlow = 0f;

				//        engine.addNebulaParticle(moveVec(ship.getLocation(), 0f, 20f, ship.getFacing()), moveVec(ship.getVelocity(), MathUtils.getRandomNumberInRange(-15f, 15f), 50f, ship.getFacing()), 80f, 2f, 0.5f, 0.5f, 4f, new Color(95, 110, 115, 111));
				//            engine.addNebulaParticle(moveVec(ship.getLocation(), 0f, -80f, ship.getFacing()), moveVec(ship.getVelocity(), MathUtils.getRandomNumberInRange(-25f, 25f), 30f, ship.getFacing()), 80f, 2f, 0.5f, 0.5f, 4f, new Color(91, 134, 143, 111));
				engine.addNebulaParticle(moveVec(ship.getLocation(), 0f, 0f, ship.getFacing()), moveVec(ship.getVelocity(), MathUtils.getRandomNumberInRange(-180f, 180f), 20f, ship.getFacing()), 80f, 2f, 0.8f, 0.2f, 1f, new Color(64, 170, 183, 81));

			} else {
				ExplosionTimerSlow += engine.getElapsedInLastFrame();
			}

		}

		if (!ship.getSystem().isChargeup() && ship.getSystem().isActive()) {
			if (ExplosionTimerfast1 >= 0.005f) {
				ExplosionTimerfast1 = 0f;
				engine.addNebulaParticle(moveVec(ship.getLocation(), (float)(1400.1 * Math.cos(a)), (float)(1400.0 * Math.sin(a)), ship.getFacing()), moveVec(new Vector2f(MathUtils.getRandomNumberInRange((float)(-90f * Math.cos(a)), (float)(-30f * Math.cos(a))), MathUtils.getRandomNumberInRange((float)(-90f * Math.sin(a)), (float)(-30f * Math.sin(a)))), MathUtils.getRandomNumberInRange((float)(-940f * Math.cos(a)), (float)(-20f * Math.cos(a))), MathUtils.getRandomNumberInRange((float)(-940f * Math.sin(a)), (float)(-20f * Math.sin(a))), ship.getFacing()), MathUtils.getRandomNumberInRange(45f, 410f * X), 4f, 0.2f, 0.1f, 0.9f, new Color(6, 13, 40, 144));
				engine.addNebulaParticle(moveVec(ship.getLocation(), (float)(1400.1 * Math.cos(a)), (float)(1400.0 * Math.sin(a)), ship.getFacing()), moveVec(new Vector2f(MathUtils.getRandomNumberInRange((float)(-90f * Math.cos(a)), (float)(-30f * Math.cos(a))), MathUtils.getRandomNumberInRange((float)(-90f * Math.sin(a)), (float)(-30f * Math.sin(a)))), MathUtils.getRandomNumberInRange((float)(-540f * Math.cos(a)), (float)(-20f * Math.cos(a))), MathUtils.getRandomNumberInRange((float)(-540f * Math.sin(a)), (float)(-20f * Math.sin(a))), ship.getFacing()), MathUtils.getRandomNumberInRange(45f, 510f * X), 4f, 0.1f, 0.2f, 0.5f, new Color(36, 42, 41, 140));

			} else {
				ExplosionTimerfast1 += engine.getElapsedInLastFrame();
			}

			if (ExplosionTimer1 >= 0.1f) {
				ExplosionTimer1 = 0f;

				//            engine.spawnExplosion(moveVec(ship.getLocation(), MathUtils.getRandomNumberInRange(-60f, 60f), MathUtils.getRandomNumberInRange(-20f, 40f), ship.getFacing()), ship.getVelocity(), new Color(189, 195, 231, 240), MathUtils.getRandomNumberInRange(3f, 15f), 0.1f);
				engine.spawnExplosion(moveVec(ship.getLocation(), MathUtils.getRandomNumberInRange(-60f, 60f), MathUtils.getRandomNumberInRange(-20f, 40f), ship.getFacing()), ship.getVelocity(), new Color(157, 215, 232, 100), 25f, 0.2f);
				//            engine.spawnExplosion(moveVec(ship.getLocation(), MathUtils.getRandomNumberInRange(-60f, 60f), MathUtils.getRandomNumberInRange(-20f, 40f), ship.getFacing()), ship.getVelocity(), new Color(235, 238, 255, 210), 15f, 0.1f);

				//            engine.addHitParticle(moveVec(ship.getLocation(), MathUtils.getRandomNumberInRange(-60f, 60f), MathUtils.getRandomNumberInRange(-20f, 40f), ship.getFacing()), ship.getVelocity(), 20f, 1f, 0.5f,new Color(235, 238, 255, 250));

			} else {
				ExplosionTimer1 += engine.getElapsedInLastFrame();
			}

			if (ExplosionTimerSlow1 >= 0.5f) {
				ExplosionTimerSlow1 = 0f;

				//        engine.addNebulaParticle(moveVec(ship.getLocation(), 0f, 20f, ship.getFacing()), moveVec(ship.getVelocity(), MathUtils.getRandomNumberInRange(-15f, 15f), 50f, ship.getFacing()), 80f, 2f, 0.5f, 0.5f, 4f, new Color(95, 110, 115, 111));
				//            engine.addNebulaParticle(moveVec(ship.getLocation(), 0f, -80f, ship.getFacing()), moveVec(ship.getVelocity(), MathUtils.getRandomNumberInRange(-25f, 25f), 30f, ship.getFacing()), 80f, 2f, 0.5f, 0.5f, 4f, new Color(91, 134, 143, 111));
				engine.addNebulaParticle(moveVec(ship.getLocation(), 0f, 0f, ship.getFacing()), moveVec(ship.getVelocity(), MathUtils.getRandomNumberInRange(-180f, 180f), 20f, ship.getFacing()), 80f, 2f, 0.8f, 0.2f, 1f, new Color(64, 170, 183, 81));

			} else {
				ExplosionTimerSlow1 += engine.getElapsedInLastFrame();
			}
		}

		float mult = (Float)mag.get(ShipAPI.HullSize.CRUISER);
		if (stats.getVariant() != null) {
			mult = (Float)mag.get(stats.getVariant().getHullSize());
		}
		stats.getHullDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);
		stats.getArmorDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);
		stats.getEmpDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);

		if (player) {
			ShipSystemAPI system = getDamper(ship);
			if (system != null) {
				float percent = (1f - mult) * effectLevel * 100;
				Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY1, system.getSpecAPI().getIconSpriteName(), system.getDisplayName(), (int)Math.round(percent) + "% less damage taken", false);
			}
		}
	}

	public StatusData getStatusData(int index, State state, float effectLevel) {
		float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		//        float damageTakenPercent = (Float) mag.get(ShipAPI.HullSize.CRUISER) * effectLevel;
		if (index == 0) {
			return new StatusData("+" + (int)bonusPercent + "% damage dealt", false);
		}
		return null;
	}

	public static ShipSystemAPI getDamper(ShipAPI ship) {
		//		ShipSystemAPI system = ship.getSystem();
		//		if (system != null && system.getId().equals("damper")) return system;
		//		if (system != null && system.getId().equals("damper_omega")) return system;
		//		if (system != null && system.getSpecAPI() != null && system.getSpecAPI().hasTag(Tags.SYSTEM_USES_DAMPER_FIELD_AI)) return system;
		//		return ship.getPhaseCloak();
		ShipSystemAPI system = ship.getPhaseCloak();
		if (system != null && system.getId().equals("goat_damper")) return system;
		if (system != null && system.getSpecAPI() != null && system.getSpecAPI().hasTag(Tags.SYSTEM_USES_DAMPER_FIELD_AI)) {
			return system;
		}
		return ship.getSystem();
	}

	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getHullDamageTakenMult().unmodify(id);
		stats.getArmorDamageTakenMult().unmodify(id);
		stats.getEmpDamageTakenMult().unmodify(id);
		if (plugin != null && plugin.isExpired()) {
			// 后备措施，置空以预防内存泄漏
			plugin = null;
		}
	}

	private static class goat_MoonSystemPlugin extends BaseCombatLayeredRenderingPlugin {

		private SpriteAPI vortexSprite; //漩涡贴图，你需要让贴图内容撑满贴图，不要留空
		private SpriteAPI vortexSprite1; //漩涡贴图，你需要让贴图内容撑满贴图，不要留空
		private SpriteAPI moonSprite; //月面贴图
		private ShipAPI ship;
		private IntervalUtil damageInterval = new IntervalUtil(DAMAGE_INTERVAL, DAMAGE_INTERVAL); // 每x秒造成一次伤害
		private float vortexAngle = 0f;
		private float vortexAngle1 = 0f;

		public goat_MoonSystemPlugin(ShipAPI ship) {
			this.ship = ship;
			// 注意，因为测试用的贴图存在留空，所以显示效果会小一圈
			this.vortexSprite = Global.getSettings().getSprite("graphics/goat/fx/TheVortex.png");
			this.vortexSprite1 = Global.getSettings().getSprite("graphics/goat/fx/TheVortex.png");
			this.moonSprite = Global.getSettings().getSprite("graphics/goat/fx/TheMoon.png");
			vortexSprite.setColor(new Color(36, 29, 140, 255)); // 这里设置颜色
			vortexSprite.setAdditiveBlend(); // 叠加模式
			vortexSprite1.setColor(new Color(134, 173, 189, 255)); // 这里设置颜色
			vortexSprite1.setAdditiveBlend(); // 叠加模式
			moonSprite.setColor(Color.white); // 这里设置颜色
			moonSprite.setAdditiveBlend(); // 叠加模式
		}

		@Override
		public void advance(float amount) {
			super.advance(amount);
			entity.getLocation().set(ship.getLocation());
			float effectLevel = ship.getSystem().getEffectLevel();
			float actualRange = getCurRange(effectLevel);
			// 当且仅当战术系统开启时， 使用这个plugin的advance进行逻辑处理。
			if (ship.getSystem().isActive()) {
				damageInterval.advance(amount);
				if (damageInterval.intervalElapsed()) {
					damageNearbyEnemies(actualRange);
				}
			}
			// 每秒钟顺时针 5 度
			vortexAngle += -5f * amount;
			if (vortexAngle < 0) vortexAngle += 360;
			vortexAngle1 += -50f * amount;
			if (vortexAngle1 < 0) vortexAngle1 += 360;

			// 漩涡展开时的星云粒子
			if (ship.getSystem().isChargeup() && effectLevel > VORTEX_SHOWN_THRESHOLD) {
				float nebulaFactor = 1f - (effectLevel - VORTEX_SHOWN_THRESHOLD) / (1f - VORTEX_SHOWN_THRESHOLD);
				if (nebulaFactor > 0.7) {
					// 随机
					int spawnCount = (int)(180f * amount * nebulaFactor);
					for (int i = 0; i < spawnCount; i++) {
						Vector2f spawnPoint = MathUtils.getRandomPointInCircle(ship.getLocation(), actualRange);
						float nebulaSpawnAngle = VectorUtils.getAngle(ship.getLocation(), spawnPoint);
						Global.getCombatEngine().addNebulaParticle(spawnPoint, MathUtils.getPointOnCircumference(null, MathUtils.getRandomNumberInRange(300f, 400f) * nebulaFactor, nebulaSpawnAngle), MathUtils.getRandomNumberInRange(60f, 90f) * nebulaFactor + 30f, 2.5f, 0.5f, 0f, MathUtils.getRandomNumberInRange(0.6f, 0.9f), Color.white);
					}
				}
			}
		}

		/**
		 * 对周围的敌对目标造成一次伤害。
		 */
		private void damageNearbyEnemies(float actualRange) {
			// 战术系统
			float effectLevel = ship.getSystem().getEffectLevel();
			// 获取范围内的所有实体
			List<CombatEntityAPI> entitiesWithinRange = new ArrayList<>();
			entitiesWithinRange.addAll(Global.getCombatEngine().getShips());
			entitiesWithinRange.addAll(Global.getCombatEngine().getMissiles());
			for (CombatEntityAPI entity : entitiesWithinRange) {
				if (entity.isExpired()) continue;
				// 对友军无效
				if (entity.getOwner() == ship.getOwner()) continue;
				// 距离
				float distance = MathUtils.getDistance(ship, entity.getLocation());
				// 距离不能大于指定值
				if (distance > actualRange) continue;
				// 根据距离调整伤害，最大距离时只有50%的伤害
				float distanceLevel = Math.max(0.5f, 1f - distance / actualRange);
				Vector2f dmgPoint;
				// 伤害系数
				float dmgMult = distanceLevel;
				if (entity instanceof MissileAPI) {
					dmgPoint = entity.getLocation();
					// 如果是导弹
					dmgMult *= 2.5f;
				} else if (entity instanceof ShipAPI) {
					ShipAPI targetShip = (ShipAPI)entity;
					ShipAPI.HullSize hullSize = targetShip.getHullSize();
					// 获取伤害点，为在碰撞内的随机点或目标中心
					dmgPoint = getDamagePoint(targetShip, hullSize);
					switch (hullSize) {
						case FIGHTER:
							dmgMult *= 5f;
							break;
						case FRIGATE:
							dmgMult *= 4f;
							break;
						case DESTROYER:
							dmgMult *= 3f;
							break;
						case CRUISER:
							dmgMult *= 2f;
							break;
						case DEFAULT:
						case CAPITAL_SHIP:
							break;
					}
				} else {
					dmgPoint = entity.getLocation();
				}
				// 造成伤害
				Global.getCombatEngine().applyDamage(entity, dmgPoint, BASE_DAMAGE * dmgMult, DamageType.ENERGY, 0f, false, false, ship, true);
				// 获取粒子迸发角度，先是舰船中心到目标中心，然后顺时针，即-90度
				float particleAngle = VectorUtils.getAngle(ship.getLocation(), entity.getLocation()) - 90f;
				for (int i = 0; i < 4f * dmgMult; i++) {
					// 单个粒子飞行角度
					float angle = MathUtils.clampAngle(particleAngle + MathUtils.getRandomNumberInRange(-30f, 60f));
					// 粒子速度矢量
					Vector2f vel = MathUtils.getPointOnCircumference(null, MathUtils.getRandomNumberInRange(60f, 100f), angle);
					Global.getCombatEngine().addHitParticle(dmgPoint, vel, MathUtils.getRandomNumberInRange(6f, 12f), 0.8f, MathUtils.getRandomNumberInRange(0.2f, 0.4f), Color.white);
				}

			}
		}

		/**
		 * 计算现在的实际范围，同时用于渲染和伤害
		 *
		 * @param effectLevel
		 * @return
		 */
		private float getCurRange(float effectLevel) {
			return MagicAnim.smooth(getVortexVisualMultiplier(effectLevel)) * EFFECT_RANGE;
		}

		/**
		 * 前60%的动画需要用于月面
		 *
		 * @param effectLevel
		 * @return
		 */
		private float getVortexVisualMultiplier(float effectLevel) {
			if (effectLevel < VORTEX_SHOWN_THRESHOLD) return 0;
			return ((effectLevel - VORTEX_SHOWN_THRESHOLD) / (1f - VORTEX_SHOWN_THRESHOLD));
		}

		/**
		 * 获取碰撞内一点
		 *
		 * @param target
		 * @param hullSize
		 * @return
		 */
		private Vector2f getDamagePoint(ShipAPI target, ShipAPI.HullSize hullSize) {
			if (hullSize == ShipAPI.HullSize.FIGHTER) return target.getLocation();
			int check = 10;
			Vector2f point;
			do {
				check--;
				point = MathUtils.getRandomPointInCircle(target.getLocation(), target.getCollisionRadius());
				if (CollisionUtils.isPointWithinBounds(point, target)) {
					break;
				} else {
					point = null;
				}
			} while (check > 0);
			if (point != null) return point;
			return target.getLocation();
		}

		@Override
		public void render(CombatEngineLayers layer, ViewportAPI viewport) {
			ShipSystemAPI system = ship.getSystem();
			float effectLevel = system.getEffectLevel();
			if (system.isActive()) {
				if (effectLevel > VORTEX_SHOWN_THRESHOLD) {
					float visualRadius = getCurRange(effectLevel);
					float visualAlphaMult = getVortexVisualMultiplier(effectLevel);
					vortexSprite.setSize(visualRadius * 2f, visualRadius * 2f);
					vortexSprite.setAlphaMult(visualAlphaMult * 0.4f); // 0.4 <- 基础透明度
					vortexSprite.setAngle(-vortexAngle);
					vortexSprite.renderAtCenter(ship.getLocation().x, ship.getLocation().y);
					vortexSprite1.setSize(visualRadius * 2f, visualRadius * 2f);
					vortexSprite1.setAlphaMult(visualAlphaMult * 0.2f); // 0.4 <- 基础透明度
					vortexSprite1.setAngle(vortexAngle1);
					vortexSprite1.renderAtCenter(ship.getLocation().x, ship.getLocation().y);
				}
				if (system.isChargeup() && effectLevel < MOON_HIDDEN_THRESHOLD) {
					float moonAnimFactor = effectLevel / MOON_HIDDEN_THRESHOLD;
					moonAnimFactor = (float)FastTrig.sin(moonAnimFactor * Math.PI);
					float moonSize = ship.getCollisionRadius() * moonAnimFactor;
					moonSprite.setSize(moonSize * 2f, moonSize * 2f);
					moonSprite.setAlphaMult(moonAnimFactor * 0.8f);
					moonSprite.setAngle(MathUtils.clampAngle(-vortexAngle * 4f)); // *x及反向x倍速率
					moonSprite.renderAtCenter(ship.getLocation().x, ship.getLocation().y);
				}
			}
		}

		@Override
		public float getRenderRadius() {
			return ship.getCollisionRadius() + EFFECT_RANGE;
		}

		@Override
		public boolean isExpired() {
			return !ship.isAlive();
		}
	}

	public Vector2f moveVec(Vector2f vec, float x, float y, float facing) {
		return new Vector2f(vec.x + addVec(x, y, facing).x, vec.y + addVec(x, y, facing).y);
	}

	public Vector2f addVec(float x, float y, float facing) {
		return new Vector2f(x * (float)Math.cos(Math.toRadians(facing - 90f)) - y * (float)Math.sin(Math.toRadians(facing - 90f)), x * (float)Math.sin(Math.toRadians(facing - 90f)) + y * (float)Math.cos(Math.toRadians(facing - 90f)));
	}

}

