package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class goat_DisplacerStats extends BaseShipSystemScript {

	private float nebulaTimer = 0f;
	private float smokeTimer = 0f;

	private static final Map<HullSize, Float> mag = new HashMap<>();

	static {
		mag.put(HullSize.FIGHTER, 0.33F);
		mag.put(HullSize.FRIGATE, 0.33F);
		mag.put(HullSize.DESTROYER, 0.33F);
		mag.put(HullSize.CRUISER, 0.5F);
		mag.put(HullSize.CAPITAL_SHIP, 0.5F);
	}

	@Override
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		ShipAPI ship = null;
		boolean player = false;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI)stats.getEntity();
			player = ship == Global.getCombatEngine().getPlayerShip();

		} else {
			return;
		}

		CombatEngineAPI engine = Global.getCombatEngine();
		ShipSystemAPI system = ship.getSystem();

		if (effectLevel >= 1f) {

			if (nebulaTimer >= 0.1f) {
				nebulaTimer = 0f;

				engine.addNebulaParticle(moveVec(ship.getLocation(), 110f, 90f, ship.getFacing()), moveVec(ship.getVelocity(), 15f, 90f, ship.getFacing()), 50f, 2f, 0.1f, 0.1f, 1f, new Color(140, 26, 26, 26));

				engine.spawnExplosion(moveVec(ship.getLocation(), 140f, 110f, ship.getFacing()), ship.getVelocity(), new Color(234, 44, 50, 200), 190f, 0.3f);
				engine.spawnExplosion(moveVec(ship.getLocation(), -140f, 110f, ship.getFacing()), ship.getVelocity(), new Color(234, 44, 50, 200), 190f, 0.3f);

				engine.addNebulaParticle(moveVec(ship.getLocation(), -110f, 90f, ship.getFacing()), moveVec(ship.getVelocity(), -15f, 90f, ship.getFacing()), 50f, 2f, 0.1f, 0.1f, 1f, new Color(140, 26, 26, 26));

			} else {
				nebulaTimer += engine.getElapsedInLastFrame();
			}
		} else if (system.isChargeup()) {
			int num = (int)(effectLevel * 13f);

			engine.addNebulaParticle(moveVec(ship.getLocation(), 50f, -75f, ship.getFacing()), moveVec(ship.getVelocity(), 85f, -30f, ship.getFacing()), 35f, 2f, 0.1f, 0.2f, 2f, new Color(204, 33, 33, 80));

			engine.addNebulaParticle(moveVec(ship.getLocation(), -50f, -75f, ship.getFacing()), moveVec(ship.getVelocity(), -85f, -30f, ship.getFacing()), 35f, 2f, 0.1f, 0.2f, 1f, new Color(204, 33, 33, 80));

			engine.addNegativeNebulaParticle(moveVec(ship.getLocation(), 60f, -35f, ship.getFacing()), moveVec(ship.getVelocity(), -40f, 30f, ship.getFacing()), 35f, 2f, 0.1f, 0.1f, 1.5f, new Color(33, 156, 204, 116));
			engine.addNegativeNebulaParticle(moveVec(ship.getLocation(), -60f, -35f, ship.getFacing()), moveVec(ship.getVelocity(), 40f, 30f, ship.getFacing()), 35f, 2f, 0.1f, 0.1f, 1.5f, new Color(33, 156, 204, 116));

			engine.addNegativeNebulaParticle(moveVec(ship.getLocation(), 75f, -25f, ship.getFacing()), moveVec(ship.getVelocity(), 0f, 30f, ship.getFacing()), 35f, 2f, 0.1f, 0.8f, 0.5f, new Color(33, 156, 204, 116));
			engine.addNegativeNebulaParticle(moveVec(ship.getLocation(), -75f, -25f, ship.getFacing()), moveVec(ship.getVelocity(), 0f, 30f, ship.getFacing()), 35f, 2f, 0.1f, 0.8f, 0.5f, new Color(33, 156, 204, 116));

			engine.addNebulaSmokeParticle(moveVec(ship.getLocation(), 50f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), -25f, 0f, ship.getFacing()), 65f, 2f, 0.1f, 0.8f, 0.5f, new Color(204, 33, 33, 16));
			engine.addNebulaSmokeParticle(moveVec(ship.getLocation(), -50f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), 25f, 0f, ship.getFacing()), 65f, 2f, 0.1f, 0.8f, 0.5f, new Color(204, 33, 33, 16));

			if (smokeTimer >= 0.001f) {
				smokeTimer = 0f;
				addSmoke(engine, ship, 50f, -150f);

				addSmoke(engine, ship, 90f, -120f);

				addSmoke(engine, ship, -90f, -120f);

				addSmoke(engine, ship, -50f, -150f);

			} else {
				smokeTimer += engine.getElapsedInLastFrame();
			}

		}

		effectLevel = 1.0F;
		float mult = mag.get(HullSize.CRUISER);
		if (stats.getVariant() != null) {
			mult = mag.get(stats.getVariant().getHullSize());
		}

		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
			stats.getMaxTurnRate().unmodify(id);
		} else {
			stats.getMaxSpeed().modifyFlat(id, 600f);
			stats.getAcceleration().modifyPercent(id, 300f * effectLevel);
			stats.getDeceleration().modifyPercent(id, 300f * effectLevel);
			stats.getTurnAcceleration().modifyFlat(id, 0.1f * effectLevel);
			stats.getTurnAcceleration().modifyPercent(id, 0.1f * effectLevel);
			stats.getMaxTurnRate().modifyFlat(id, 15f);
			stats.getMaxTurnRate().modifyPercent(id, 100f);
		}

	}

	@Override
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMaxSpeed().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getDeceleration().unmodify(id);
		stats.getBallisticRoFMult().unmodify(id);
		stats.getBallisticWeaponFluxCostMod().unmodify(id);
		stats.getEnergyRoFMult().unmodify(id);
		stats.getBallisticWeaponFluxCostMod().unmodify(id);
	}

	@Override
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) return new StatusData("Top speed +600", false);
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