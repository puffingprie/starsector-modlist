package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class goat_FortressShield extends BaseShipSystemScript {

	public static float DAMAGE_MULT = 0.95f;
	//public static float DAMAGE_MULT = 0.8f;
	private float nebulaTimer = 0f;
	private float nebulaTimer1 = 0f;
	private float smokeTimer = 0f;

	//public static double pi = Math.PI;

	//public static float a = MathUtils.getRandomNumberInRange((float) -pi,(float)  pi);
	public static float a = 0;
	public static float X = 0;

	public static class TargetData {

		public ShipAPI target;

		public TargetData(ShipAPI target) {
			this.target = target;
		}
	}

	public static Object KEY_SHIP = new Object();

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

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

		ship.fadeToColor(KEY_SHIP, new Color(25, 30, 33, 255), 1.1f, 1.1f, effectLevel);

		//stats.getShieldTurnRateMult().modifyMult(id, 100f);
		stats.getShieldUnfoldRateMult().modifyPercent(id, 4000);
		stats.getShieldArcBonus().modifyPercent(id, 360);

		//stats.getShieldDamageTakenMult().modifyMult(id, 0.1f);
		stats.getShieldDamageTakenMult().modifyMult(id, 1f - DAMAGE_MULT * effectLevel);

		stats.getShieldUpkeepMult().modifyMult(id, 0f);

		//System.out.println("level: " + effectLevel);

		//if (effectLevel >= 1.0f) {
		a = MathUtils.getRandomNumberInRange(-3.1415f, 3.1415f);
		X = 0.3f * effectLevel;

		if (nebulaTimer >= 0.32f - X) {
			nebulaTimer = 0f;

			engine.spawnExplosion(moveVec(ship.getLocation(), (float)(150.1 * Math.cos(a)), (float)(150.0 * Math.sin(a)), ship.getFacing()), ship.getVelocity(), new Color(105, 67, 183, 120), MathUtils.getRandomNumberInRange(5f, 150f), 0.6f);
			engine.spawnExplosion(moveVec(ship.getLocation(), (float)(150.1 * Math.cos(a)), (float)(150.0 * Math.sin(a)), ship.getFacing()), ship.getVelocity(), new Color(229, 211, 255, 200), MathUtils.getRandomNumberInRange(5f, 20f), 0.3f);
			engine.spawnExplosion(moveVec(ship.getLocation(), (float)(140.1 * Math.cos(a)), (float)(140.0 * Math.sin(a)), ship.getFacing()), ship.getVelocity(), new Color(255, 255, 255, 250), MathUtils.getRandomNumberInRange(5f, 10f), 0.1f);

			engine.addNebulaParticle(moveVec(ship.getLocation(), (float)(150.1 * Math.cos(a)), (float)(150.0 * Math.sin(a)), ship.getFacing()), moveVec(new Vector2f(0.0f, 0.0f), MathUtils.getRandomNumberInRange(-10f, 10f), MathUtils.getRandomNumberInRange(-10f, 10f), ship.getFacing()), 65f, 2f, 0.1f, 0.2f, 0.8f, new Color(96, 33, 204, 220));
			engine.addSwirlyNebulaParticle(moveVec(ship.getLocation(), (float)(170.1 * Math.cos(a)), (float)(170.0 * Math.sin(a)), ship.getFacing()), ship.getVelocity(), MathUtils.getRandomNumberInRange(5f, 40f), 1.0f, 0.5f, 0.5f, 1.0f, new Color(255, 255, 255, 250), true);

		} else {
			nebulaTimer += engine.getElapsedInLastFrame();
		}
		//}
	}

	public void unapply(MutableShipStatsAPI stats, String id) {
		//stats.getShieldAbsorptionMult().unmodify(id);
		stats.getShieldArcBonus().unmodify(id);
		stats.getShieldDamageTakenMult().unmodify(id);
		stats.getShieldTurnRateMult().unmodify(id);
		stats.getShieldUnfoldRateMult().unmodify(id);
		stats.getShieldUpkeepMult().unmodify(id);
	}

	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("-95% shield damage taken", false);
		}
		//		else if (index == 1) {
		//			return new StatusData("shield upkeep reduced to 0", false);
		//		} else if (index == 2) {
		//			return new StatusData("shield upkeep reduced to 0", false);
		//		}
		return null;
	}

	public Vector2f moveVec(Vector2f vec, float x, float y, float facing) {
		return new Vector2f(vec.x + addVec(x, y, facing).x, vec.y + addVec(x, y, facing).y);
	}

	public Vector2f addVec(float x, float y, float facing) {
		return new Vector2f(x * (float)Math.cos(Math.toRadians(facing - 90f)) - y * (float)Math.sin(Math.toRadians(facing - 90f)), x * (float)Math.sin(Math.toRadians(facing - 90f)) + y * (float)Math.cos(Math.toRadians(facing - 90f)));
	}

	private void addSmoke(CombatEngineAPI engine, ShipAPI ship, float x, float y) {
		engine.addSmokeParticle(moveVec(ship.getLocation(), x, y, ship.getFacing()), new Vector2f((Misc.random.nextFloat() - 0.5f) * 2f * 20f, (Misc.random.nextFloat() - 0.5f) * 2f * 20f), 85, 0.5f, 0.3f, new Color(31, 31, 31, 145));
	}
}
