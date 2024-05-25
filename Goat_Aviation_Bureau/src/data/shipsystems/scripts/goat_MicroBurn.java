package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.EnumSet;

public class goat_MicroBurn extends BaseShipSystemScript {

	public static float SPEED_BONUS = 500f;
	public static float TURN_BONUS = -120f;
	public static Object KEY_SHIP = new Object();
	public static float INCOMING_DAMAGE_MULT = 0.3f;
	public static float REPAIR_RATE_MULT = 3f;
	private float nebulaTimer = 0f;
	private float nebulaTimer1 = 0f;

	private Color color = new Color(238, 174, 54, 255);

	//	private Color [] colors = new Color[] {
	//			new Color(140, 100, 235),
	//			new Color(180, 110, 210),
	//			new Color(150, 140, 190),
	//			new Color(140, 190, 210),
	//			new Color(90, 200, 170),
	//			new Color(65, 230, 160),
	//			new Color(20, 220, 70)
	//	};

	public static class TargetData {

		public ShipAPI target;

		public TargetData(ShipAPI target) {
			this.target = target;
		}
	}

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		if (state == State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
			stats.getMaxTurnRate().unmodify(id);
		} else {
			stats.getMaxSpeed().modifyFlat(id, SPEED_BONUS);
			stats.getAcceleration().modifyPercent(id, SPEED_BONUS * 30f * effectLevel);
			stats.getDeceleration().modifyPercent(id, SPEED_BONUS * 3f * effectLevel);
			stats.getTurnAcceleration().modifyFlat(id, TURN_BONUS * -0.2f * effectLevel);
			stats.getTurnAcceleration().modifyPercent(id, TURN_BONUS * -0.2f * effectLevel);
			stats.getMaxTurnRate().modifyFlat(id, 5f);
			stats.getMaxTurnRate().modifyPercent(id, 20f);
		}

		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI)stats.getEntity();

			ship.getEngineController().fadeToOtherColor(this, color, new Color(23, 2, 2, 128), effectLevel, 0.67f);
			//ship.getEngineController().fadeToOtherColor(this, Color.white, new Color(0,0,0,0), effectLevel, 0.67f);
			ship.getEngineController().extendFlame(this, 0.5f * effectLevel, 0f * effectLevel, 0f * effectLevel);

			//			String key = ship.getId() + "_" + id;
			//			Object test = Global.getCombatEngine().getCustomData().get(key);
			//			if (state == State.IN) {
			//				if (test == null && effectLevel > 0.2f) {
			//					Global.getCombatEngine().getCustomData().put(key, new Object());
			//					ship.getEngineController().getExtendLengthFraction().advance(1f);
			//					for (ShipEngineAPI engine : ship.getEngineController().getShipEngines()) {
			//						if (engine.isSystemActivated()) {
			//							ship.getEngineController().setFlameLevel(engine.getEngineSlot(), 1f);
			//						}
			//					}
			//				}
			//			} else {
			//				Global.getCombatEngine().getCustomData().remove(key);
			//			}
		}

		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI)stats.getEntity();
		} else {
			return;
		}
        float A = MathUtils.getRandomNumberInRange(-0.7f, 2f);
		float B = MathUtils.getRandomNumberInRange(-0.7f, 2f);
		//ship.fadeToColor(KEY_SHIP, new Color(18, 84, 83, 255), 0.1f, 0.1f, effectLevel);
		//ship.fadeToColor(KEY_SHIP, new Color(100,100,100,255), 0.1f, 0.1f, effectLevel);
		//ship.setWeaponGlow(effectLevel, new Color(139, 100, 255, 255), EnumSet.of(WeaponAPI.WeaponType.BALLISTIC, WeaponAPI.WeaponType.ENERGY, WeaponAPI.WeaponType.MISSILE));
		//ship.setJitter(KEY_SHIP, new Color(100,165,255,55), effectLevel, 1, 0f, 5f);
		ship.setJitterUnder(KEY_SHIP, new Color(10, 161, 175, 55), effectLevel, 15, 0f, 18f);
		ship.addAfterimage(new Color(31, 14, 138, 76), 0f, 0f, -ship.getVelocity().x * A * B, -ship.getVelocity().y * A, effectLevel, 0.2f - (effectLevel * 0.1f), effectLevel * 0.1f, 0.2f, false, true, false);
		//ship.setShowModuleJitterUnder(true);


		effectLevel = 1f;

		stats.getHullDamageTakenMult().modifyMult(id, 1f - (1f - INCOMING_DAMAGE_MULT) * effectLevel);
		stats.getArmorDamageTakenMult().modifyMult(id, 1f - (1f - INCOMING_DAMAGE_MULT) * effectLevel);
		stats.getEmpDamageTakenMult().modifyMult(id, 1f - (1f - INCOMING_DAMAGE_MULT) * effectLevel);

	}

	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMaxSpeed().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getDeceleration().unmodify(id);
		stats.getBallisticWeaponFluxCostMod().unmodify(id);
		stats.getEnergyWeaponFluxCostMod().unmodify(id);
		stats.getMissileWeaponFluxCostMod().unmodify(id);

		stats.getHullDamageTakenMult().unmodify(id);
		stats.getArmorDamageTakenMult().unmodify(id);
		stats.getEmpDamageTakenMult().unmodify(id);

		stats.getCombatEngineRepairTimeMult().unmodifyMult(id);
		stats.getCombatWeaponRepairTimeMult().unmodifyMult(id);
	}

	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("Improved maneuverability", false);
		} else if (index == 1) {
			return new StatusData("+" + (int)SPEED_BONUS + " top speed", false);
		}
		else if (index == 2) {
			return new StatusData("-" + (int) ((1f - INCOMING_DAMAGE_MULT) * 100) + "%" + " damage taken", false);
		}
		return null;
	}

	public Vector2f moveVec(Vector2f vec, float x, float y, float facing) {
		return new Vector2f(vec.x + addVec(x, y, facing).x, vec.y + addVec(x, y, facing).y);
	}

	public Vector2f addVec(float x, float y, float facing) {
		return new Vector2f(x * (float)Math.cos(Math.toRadians(facing - 90f)) - y * (float)Math.sin(Math.toRadians(facing - 90f)), x * (float)Math.sin(Math.toRadians(facing - 90f)) + y * (float)Math.cos(Math.toRadians(facing - 90f)));
	}

	private void addSmoke(CombatEngineAPI engine, ShipAPI ship, float x, float y) {
		engine.addSmokeParticle(moveVec(ship.getLocation(), x, y, ship.getFacing()), new Vector2f((Misc.random.nextFloat() - 0.5f) * 2f * 20f, (Misc.random.nextFloat() - 0.5f) * 2f * 20f), 85, 0.5f, 0.3f, new Color(114, 107, 107, 145));
	}
}
