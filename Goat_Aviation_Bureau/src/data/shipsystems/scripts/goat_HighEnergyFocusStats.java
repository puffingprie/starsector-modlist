package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class goat_HighEnergyFocusStats extends BaseShipSystemScript {

	public static final float DAMAGE_BONUS_PERCENT = 120f;
	public static final float EXTRA_DAMAGE_TAKEN_PERCENT = 60f;
	public static final float HULL_DAMAGE_TAKEN_PERCENT = 50f;
	private float nebulaTimer = 0f;
	private float nebulaTimer1 = 0f;
	private float smokeTimer = 0f;
	public static float a = 0;
	public static float b = 0;
	public static float c = 0;
	public static float X = 0;

	public static class TargetData {

		public ShipAPI target;

		public TargetData(ShipAPI target) {
			this.target = target;
		}
	}

	public static Object KEY_SHIP = new Object();

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		CombatEngineAPI engine = Global.getCombatEngine();

		ShipAPI ship = null;
		boolean player = false;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI)stats.getEntity();
			player = ship == Global.getCombatEngine().getPlayerShip();
		} else {
			return;
		}

		ship.fadeToColor(KEY_SHIP, new Color(27, 39, 42, 255), 1.1f, 1.1f, effectLevel);

		stats.getMaxSpeed().modifyFlat(id, -80f);
		stats.getAcceleration().modifyPercent(id, 0.7f * effectLevel);
		stats.getDeceleration().modifyPercent(id, 0.7f * effectLevel);
		stats.getTurnAcceleration().modifyFlat(id, -0.9f * effectLevel);
		stats.getTurnAcceleration().modifyPercent(id, -0.9f * effectLevel);

		float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		stats.getEnergyWeaponDamageMult().modifyPercent(id, bonusPercent);
		stats.getEnergyWeaponRangeBonus().modifyPercent(id, bonusPercent);

		float damageTakenPercent1 = -EXTRA_DAMAGE_TAKEN_PERCENT * effectLevel;
		float damageTakenPercent2 = -HULL_DAMAGE_TAKEN_PERCENT * effectLevel;
		stats.getArmorDamageTakenMult().modifyPercent(id, damageTakenPercent1);
		stats.getHullDamageTakenMult().modifyPercent(id, damageTakenPercent2);
		//		stats.getShieldDamageTakenMult().modifyPercent(id, damageTakenPercent);
		stats.getWeaponDamageTakenMult().modifyPercent(id, damageTakenPercent1);
		stats.getEngineDamageTakenMult().modifyPercent(id, damageTakenPercent1);

		//stats.getBeamWeaponFluxCostMult().modifyMult(id, 10f);

		//if (effectLevel >= 1.0f) {
		a = MathUtils.getRandomNumberInRange(0.0f, 3.1415f);
		b = MathUtils.getRandom().nextBoolean() ? 1 : 0;
		if (b == 1) {c = MathUtils.getRandomNumberInRange(1.87075f, 3.1415f);}
		if (b == 0) {c = MathUtils.getRandomNumberInRange(0.0f, 1.27075f);}

		X = 0.3f * effectLevel;

		if (nebulaTimer >= 0.32f - X) {
			nebulaTimer = 0f;

			//            engine.spawnExplosion(moveVec(ship.getLocation(), (float) (150.1 * Math.cos(a)), (float) (150.0 * Math.sin(a)), ship.getFacing()), ship.getVelocity(), new Color(105, 67, 183, 120), MathUtils.getRandomNumberInRange(5f,150f), 0.6f);
			//           engine.spawnExplosion(moveVec(ship.getLocation(), (float) (160.1 * Math.cos(a)), (float) (160.0 * Math.sin(a)), ship.getFacing()), ship.getVelocity(), new Color(106, 78, 189, 250), MathUtils.getRandomNumberInRange(15f,30f), 0.3f);
			//            engine.spawnExplosion(moveVec(ship.getLocation(), (float) (140.1 * Math.cos(a)), (float) (140.0 * Math.sin(a)), ship.getFacing()), ship.getVelocity(), new Color(255, 255, 255, 250), MathUtils.getRandomNumberInRange(5f,10f), 0.1f);

			engine.addNebulaParticle(moveVec(ship.getLocation(), (float)(178.1 * Math.cos(a)), (float)(175.0 * Math.sin(a)), ship.getFacing()), moveVec(new Vector2f(0.0f, 0.0f), MathUtils.getRandomNumberInRange((float)(-60f * Math.cos(a)), (float)(-20f * Math.cos(a))), MathUtils.getRandomNumberInRange((float)(-60f * Math.sin(a)), (float)(-20f * Math.sin(a))), ship.getFacing()), MathUtils.getRandomNumberInRange(45f, 210f * X), 2f, 0.1f, 0.2f, 1.2f, new Color(60, 25, 164, 152));
			engine.addNebulaParticle(moveVec(ship.getLocation(), (float)(178.1 * Math.cos(c)), (float)(-175.0 * Math.sin(c)), ship.getFacing()), moveVec(new Vector2f(MathUtils.getRandomNumberInRange((float)(80f * Math.cos(c)), (float)(130f * Math.cos(c))), 0.0f), MathUtils.getRandomNumberInRange((float)(30f * Math.cos(c)), (float)(230f * Math.cos(c))), MathUtils.getRandomNumberInRange((float)(30f * Math.sin(c)), (float)(80f * Math.sin(c))), ship.getFacing()), MathUtils.getRandomNumberInRange(35f, 220f * X), 2f, 0.1f, 0.2f, 2.5f, new Color(129, 112, 220, 220));
			engine.addNebulaParticle(moveVec(ship.getLocation(), (float)(178.1 * Math.cos(c)), (float)(-115.0 * Math.sin(c)), ship.getFacing()), moveVec(new Vector2f(0.0f, 50.0f), MathUtils.getRandomNumberInRange((float)(30f * Math.cos(c)), (float)(50f * Math.cos(c))), MathUtils.getRandomNumberInRange((float)(230f * Math.sin(c)), (float)(380f * Math.sin(c))), ship.getFacing()), MathUtils.getRandomNumberInRange(20f, 290f * X), 2f, 0.1f, 0.2f, 0.8f, new Color(8, 21, 162, 220));
			engine.spawnExplosion(moveVec(ship.getLocation(), (float)(MathUtils.getRandomNumberInRange(160f, 178f) * Math.cos(c)), (float)(MathUtils.getRandomNumberInRange(-225f, -170f) * Math.sin(c)), ship.getFacing()), ship.getVelocity(), new Color(196, 178, 246, 250), MathUtils.getRandomNumberInRange(35f, 60f), 0.5f);

			//            engine.addSwirlyNebulaParticle(moveVec(ship.getLocation(), (float) (178.1 * Math.cos(c)), (float) (-175.0 * Math.sin(c)), ship.getFacing()), ship.getVelocity(), MathUtils.getRandomNumberInRange(15f,40f),1.0f,0.5f,0.5f,1.0f,new Color(234, 239, 248, 250),true);

		} else {
			nebulaTimer += engine.getElapsedInLastFrame();
		}
		//}
	}

	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getEnergyWeaponDamageMult().unmodify(id);
		stats.getEnergyWeaponRangeBonus().unmodify(id);
		stats.getArmorDamageTakenMult().unmodify(id);
		stats.getHullDamageTakenMult().unmodify(id);
		//		stats.getShieldDamageTakenMult().unmodify(id);
		stats.getWeaponDamageTakenMult().unmodify(id);
		stats.getEngineDamageTakenMult().unmodify(id);
		stats.getMaxSpeed().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getDeceleration().unmodify(id);
	}

	public Vector2f moveVec(Vector2f vec, float x, float y, float facing) {
		return new Vector2f(vec.x + addVec(x, y, facing).x, vec.y + addVec(x, y, facing).y);
	}

	public Vector2f addVec(float x, float y, float facing) {
		return new Vector2f(x * (float)Math.cos(Math.toRadians(facing - 90f)) - y * (float)Math.sin(Math.toRadians(facing - 90f)), x * (float)Math.sin(Math.toRadians(facing - 90f)) + y * (float)Math.cos(Math.toRadians(facing - 90f)));
	}

	public StatusData getStatusData(int index, State state, float effectLevel) {
		float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		float damageTakenPercent = EXTRA_DAMAGE_TAKEN_PERCENT * effectLevel;
		if (index == 0) {
			return new StatusData("+" + (int)bonusPercent + "% energy weapon damage/range", false);
		} else if (index == 1) {
			return new StatusData("+" + (int)damageTakenPercent + "% armor effectiveness", false);
		} else if (index == 2) {
			return new StatusData("reduced mobility", true);
			//return null;
		}
		return null;
	}
}
