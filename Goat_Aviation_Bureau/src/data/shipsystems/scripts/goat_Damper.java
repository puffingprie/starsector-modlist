package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class goat_Damper extends BaseShipSystemScript {

	private static Map mag = new HashMap();
	public static Object KEY_SHIP = new Object();
	public static float REPAIR_RATE_MULT = 8f;

	private float ExplosionTimer = 0f;
	private float ExplosionTimerSlow = 0f;

	static {
		mag.put(ShipAPI.HullSize.FIGHTER, 0.75f);
		mag.put(ShipAPI.HullSize.FRIGATE, 0.75f);
		mag.put(ShipAPI.HullSize.DESTROYER, 0.75f);
		mag.put(ShipAPI.HullSize.CRUISER, 0.75f);
		mag.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.9f);
	}

	protected Object STATUSKEY1 = new Object();
	protected Object STATUSKEY2 = new Object();

	//public static final float INCOMING_DAMAGE_MULT = 0.25f;
	//public static final float INCOMING_DAMAGE_CAPITAL = 0.5f;

	public static class TargetData {

		public ShipAPI target;

		public TargetData(ShipAPI target) {
			this.target = target;
		}
	}

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

		ship.fadeToColor(KEY_SHIP, new Color(134, 173, 189, 255), 0.1f, 0.1f, effectLevel);

		if (ExplosionTimer >= 0.1f) {
			ExplosionTimer = 0f;

			engine.spawnExplosion(moveVec(ship.getLocation(), MathUtils.getRandomNumberInRange(-60f, 60f), MathUtils.getRandomNumberInRange(-20f, 40f), ship.getFacing()), ship.getVelocity(), new Color(189, 195, 231, 240), MathUtils.getRandomNumberInRange(3f, 15f), 0.1f);
			engine.spawnExplosion(moveVec(ship.getLocation(), MathUtils.getRandomNumberInRange(-60f, 60f), MathUtils.getRandomNumberInRange(-20f, 40f), ship.getFacing()), ship.getVelocity(), new Color(157, 215, 232, 100), 25f, 0.2f);
			engine.spawnExplosion(moveVec(ship.getLocation(), MathUtils.getRandomNumberInRange(-60f, 60f), MathUtils.getRandomNumberInRange(-20f, 40f), ship.getFacing()), ship.getVelocity(), new Color(235, 238, 255, 210), 15f, 0.1f);

			engine.addHitParticle(moveVec(ship.getLocation(), MathUtils.getRandomNumberInRange(-60f, 60f), MathUtils.getRandomNumberInRange(-20f, 40f), ship.getFacing()), ship.getVelocity(), 20f, 1f, 0.5f, new Color(235, 238, 255, 250));

		} else {
			ExplosionTimer += engine.getElapsedInLastFrame();
		}

		if (ExplosionTimerSlow >= 0.7f) {
			ExplosionTimerSlow = 0f;

			engine.addNebulaParticle(moveVec(ship.getLocation(), 0f, 20f, ship.getFacing()), moveVec(ship.getVelocity(), MathUtils.getRandomNumberInRange(-15f, 15f), 50f, ship.getFacing()), 80f, 2f, 0.5f, 0.5f, 4f, new Color(95, 110, 115, 111));
			engine.addNebulaParticle(moveVec(ship.getLocation(), 0f, -80f, ship.getFacing()), moveVec(ship.getVelocity(), MathUtils.getRandomNumberInRange(-25f, 25f), 30f, ship.getFacing()), 80f, 2f, 0.5f, 0.5f, 4f, new Color(91, 134, 143, 111));
			engine.addNebulaParticle(moveVec(ship.getLocation(), 0f, 0f, ship.getFacing()), moveVec(ship.getVelocity(), MathUtils.getRandomNumberInRange(-180f, 180f), 20f, ship.getFacing()), 80f, 2f, 0.8f, 0.2f, 1f, new Color(64, 170, 183, 81));

		} else {
			ExplosionTimerSlow += engine.getElapsedInLastFrame();
		}

		float mult = (Float)mag.get(ShipAPI.HullSize.CRUISER);
		if (stats.getVariant() != null) {
			mult = (Float)mag.get(stats.getVariant().getHullSize());
		}
		stats.getHullDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);
		stats.getArmorDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);
		stats.getEmpDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);

		stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f / (1f + (REPAIR_RATE_MULT - 1f) * effectLevel));
		stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f / (1f + (REPAIR_RATE_MULT - 1f) * effectLevel));

		if (player) {
			ShipSystemAPI system = getDamper(ship);
			if (system != null) {
				float percent = (1f - mult) * effectLevel * 100;
				Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY1, system.getSpecAPI().getIconSpriteName(), system.getDisplayName(), (int)Math.round(percent) + "% less damage taken", false);
				Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY2, system.getSpecAPI().getIconSpriteName(), system.getDisplayName(), "rapidly repairing", false);
			}
		}
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
		stats.getEnergyWeaponDamageMult().unmodify(id);
		//		stats.getEnergyWeaponRangeBonus().unmodify(id);
		stats.getHullDamageTakenMult().unmodify(id);
		stats.getArmorDamageTakenMult().unmodify(id);
		stats.getEmpDamageTakenMult().unmodify(id);
	}

	public Vector2f moveVec(Vector2f vec, float x, float y, float facing) {
		return new Vector2f(vec.x + addVec(x, y, facing).x, vec.y + addVec(x, y, facing).y);
	}

	public Vector2f addVec(float x, float y, float facing) {
		return new Vector2f(x * (float)Math.cos(Math.toRadians(facing - 90f)) - y * (float)Math.sin(Math.toRadians(facing - 90f)), x * (float)Math.sin(Math.toRadians(facing - 90f)) + y * (float)Math.cos(Math.toRadians(facing - 90f)));
	}

}
