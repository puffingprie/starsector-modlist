package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SoundAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import org.lazywizard.lazylib.FastTrig;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class goat_StormSurgeStats extends BaseShipSystemScript {

	public static final float ROF_INCREASE_IN_MULT = 3f;
	public static final float FLUX_REDUCTION_IN_PERCENT = 70f;

	public static final float MAX_TIME_MULT = 3f;
	public static final float MIN_TIME_MULT = 0.1f;

	private static final Map<HullSize, Float> mag = new HashMap<>();
	private static final Vector2f ZERO = new Vector2f();

	static {
		mag.put(HullSize.FIGHTER, 0.33F);
		mag.put(HullSize.FRIGATE, 0.33F);
		mag.put(HullSize.DESTROYER, 0.33F);
		mag.put(HullSize.CRUISER, 0.5F);
		mag.put(HullSize.CAPITAL_SHIP, 0.5F);
	}

	private boolean Explosion = true;
	private float nebulaTimer = 0f;

	private float clockTimer = 0f;
	private int tickTimes = 0;

	private float loopInitTimer = 0f;
	private SoundAPI loopInit = null;
	private float loopBuildTimerTrigger = 0f;
	private float loopBuildTimerVol = 0f;
	private int loopBuildTimerAmount = 0;
	private SoundAPI loopBuild = null;

	@Override
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		ShipAPI ship;
		boolean player;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI)stats.getEntity();
			player = ship == Global.getCombatEngine().getPlayerShip();
			id = id + "_" + ship.getId();
		} else {
			return;
		}

		CombatEngineAPI engine = Global.getCombatEngine();
		float amount = Global.getCombatEngine().getElapsedInLastFrame();

		ShipSystemAPI system = ship.getSystem();

		float percent = getWeaponBoostInPercent(effectLevel);
		stats.getBallisticRoFMult().modifyPercent(id, percent);
		stats.getEnergyRoFMult().modifyPercent(id, percent);
		stats.getEnergyWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_REDUCTION_IN_PERCENT * 0.01f));
		stats.getBallisticWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_REDUCTION_IN_PERCENT * 0.01f));

		// the ui sound hack, bypassing LOWPASS
		float amountForSound = amount / (engine.getTimeMult().getModifiedValue() * ship.getMutableStats().getTimeMult().getModifiedValue());
		if (Float.isNaN(amountForSound)) amountForSound = amount;

		clockTimer += amountForSound;
		if (clockTimer >= 1f) {
			clockTimer -= 1f;

			if (player) {
				float vol = 1f + tickTimes * 0.2f;
				if ((tickTimes & 1) != 1) {
					Global.getSoundPlayer().playUISound("goat_storm_surge_time1", 1f, vol);
				} else {
					Global.getSoundPlayer().playUISound("goat_storm_surge_time2", 1f, vol);
				}
			}

			tickTimes++;
		}

		// the ui sound hack, bypassing LOWPASS
		if (player) {
			loopInitTimer += amountForSound;
			loopBuildTimerTrigger += amountForSound;

			if (loopInit == null) {
				loopInit = Global.getSoundPlayer().playUISound("goat_storm_surge_loop_init", 1f, 1f);
			}

			float volForLoopInit = 1f - loopInitTimer * 0.25f;
			if (volForLoopInit > 0f) {
				loopInit.setVolume(volForLoopInit);
			} else {
				loopInit.stop();
			}

			float volForLoopBuild = loopBuildTimerVol * 0.15f;
			if (loopBuild != null) {
				loopBuildTimerVol += amountForSound;

				loopBuild.setVolume(volForLoopBuild * effectLevel);
			}

			if (loopBuildTimerTrigger >= 2f) {
				loopBuildTimerTrigger -= 0.95f; // not actually 1s

				Global.getLogger(this.getClass()).info(loopBuildTimerVol);
				loopBuild = Global.getSoundPlayer().playUISound("goat_storm_surge_loop_build_" + (loopBuildTimerAmount % 6), 1f, volForLoopBuild);

				loopBuildTimerAmount++;
			}
		}

		effectLevel = 1f;
		float mult = mag.get(ship.getHullSize());
		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
			stats.getMaxTurnRate().unmodify(id);
		} else {
			stats.getMaxSpeed().modifyFlat(id, 30f);
			stats.getMaxTurnRate().modifyFlat(id, 100f);
			stats.getMaxTurnRate().modifyPercent(id, 100f);
		}

		float shipTimeMult = 1f + (MAX_TIME_MULT - 1f) * effectLevel;
		stats.getTimeMult().modifyMult(id, shipTimeMult);
		if (player) {
			engine.getTimeMult().modifyMult(id, 1f / shipTimeMult);
		} else {
			engine.getTimeMult().unmodify(id);
		}

		if (effectLevel >= 1f) {

			if (!Explosion) {
				Explosion = true;

				engine.addNegativeNebulaParticle(moveVec(ship.getLocation(), 10f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), 135f, 160f, ship.getFacing()), 25f, 15f, 4.1f, 0.5f, 1f, new Color(50, 33, 204, 150));
				engine.addNegativeNebulaParticle(moveVec(ship.getLocation(), 10f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), -135f, 160f, ship.getFacing()), 25f, 15f, 4.1f, 0.5f, 1f, new Color(50, 33, 204, 150));
				engine.spawnExplosion(moveVec(ship.getLocation(), 30f, 11f, ship.getFacing()), ship.getVelocity(), new Color(69, 44, 234, 250), 190f, 0.2f);
				engine.spawnExplosion(moveVec(ship.getLocation(), -30f, 10f, ship.getFacing()), ship.getVelocity(), new Color(69, 44, 234, 250), 190f, 0.2f);
			}

			if (nebulaTimer >= 0.1f) {
				nebulaTimer -= 0.1f;

				engine.addSwirlyNebulaParticle(moveVec(ship.getLocation(), 0f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), 0f, 60f, ship.getFacing()), 35f, 2f, 0.1f, 0.1f, 2f, new Color(35, 3, 75, 215), true);

				engine.addNegativeSwirlyNebulaParticle(moveVec(ship.getLocation(), 0f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), 0f, 60f, ship.getFacing()), 35f, 3f, 0.1f, 0.1f, 2f, new Color(51, 121, 35, 255));

				engine.addNebulaParticle(moveVec(ship.getLocation(), 10f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), 45f, 60f, ship.getFacing()), 15f, 2f, 0.1f, 0.1f, 2f, new Color(50, 33, 204, 50));
				engine.addNebulaParticle(moveVec(ship.getLocation(), -10f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), -45f, 60f, ship.getFacing()), 15f, 2f, 0.1f, 0.1f, 2f, new Color(50, 33, 204, 50));

				engine.addNebulaParticle(moveVec(ship.getLocation(), 10f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), 45f, -60f, ship.getFacing()), 15f, 2f, 0.1f, 0.1f, 2f, new Color(50, 33, 204, 80));
				engine.addNebulaParticle(moveVec(ship.getLocation(), -10f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), -45f, -60f, ship.getFacing()), 15f, 2f, 0.1f, 0.1f, 2f, new Color(50, 33, 204, 80));

				engine.addSmoothParticle(moveVec(ship.getLocation(), 70f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), -45f, 30f, ship.getFacing()), 55f, 0.5f, 2.4f, new Color(14, 149, 162, 97));
				engine.addSmoothParticle(moveVec(ship.getLocation(), -70f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), 35f, 30f, ship.getFacing()), 55f, 0.5f, 2.4f, new Color(14, 149, 162, 97));

				engine.addHitParticle(moveVec(ship.getLocation(), 70f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), -25f, 4f, ship.getFacing()), 25f, 0.8f, new Color(14, 149, 162, 255));
				engine.addHitParticle(moveVec(ship.getLocation(), -70f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), 25f, 4f, ship.getFacing()), 25f, 0.8f, new Color(14, 149, 162, 255));

				engine.addNebulaParticle(moveVec(ship.getLocation(), 50f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), -45f, 60f, ship.getFacing()), 65f, 2f, 0.1f, 0.1f, 2f, new Color(61, 25, 107, 79));
				engine.addNebulaParticle(moveVec(ship.getLocation(), -50f, 15f, ship.getFacing()), moveVec(ship.getVelocity(), 45f, 60f, ship.getFacing()), 65f, 2f, 0.1f, 0.1f, 2f, new Color(61, 25, 107, 79));

			} else {
				nebulaTimer += engine.getElapsedInLastFrame();
			}

		} else if (system.isChargeup()) {

			Explosion = true;

			int num = (int)(effectLevel * 13f);
			engine.addNebulaParticle(moveVec(ship.getLocation(), 110f, 90f, ship.getFacing()), moveVec(ship.getVelocity(), 15f, 90f, ship.getFacing()), 50f, 2f, 0.1f, 0.1f, 1f, new Color(140, 26, 26, 26));
			engine.addNebulaParticle(moveVec(ship.getLocation(), -110f, 90f, ship.getFacing()), moveVec(ship.getVelocity(), -15f, 90f, ship.getFacing()), 50f, 2f, 0.1f, 0.1f, 1f, new Color(140, 26, 26, 26));
		}
	}

	@Override
	public void unapply(MutableShipStatsAPI stats, String id) {

		ShipAPI ship;
		boolean player;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI)stats.getEntity();
			player = ship == Global.getCombatEngine().getPlayerShip();
			id = id + "_" + ship.getId();
		} else {
			return;
		}

		stats.getMaxSpeed().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getBallisticRoFMult().unmodify(id);
		stats.getBallisticWeaponFluxCostMod().unmodify(id);
		stats.getEnergyRoFMult().unmodify(id);
		stats.getBallisticWeaponFluxCostMod().unmodify(id);

		Explosion = false;
		clockTimer = 0f;
		tickTimes = 0;
		loopInitTimer = 0f;
		loopInit = null;
		loopBuildTimerTrigger = 0f;
		loopBuildTimerVol = 0f;
		loopBuildTimerAmount = 0;
		loopBuild = null;

		Global.getCombatEngine().getTimeMult().unmodify(id);
		stats.getTimeMult().unmodify(id);
	}

	@Override
	public StatusData getStatusData(int index, State state, float effectLevel) {

		float bonusPercent = getWeaponBoostInPercent(effectLevel);
		if (index == 0) return new StatusData("Weapon rate of fire +" + (int) bonusPercent + "%", false);
		if (index == 1) return new StatusData("Weapon flux generation -" + (int) FLUX_REDUCTION_IN_PERCENT + "%", false);
		if (index == 3) return new StatusData("Improved maneuverability", false);
		if (index == 4) return new StatusData("Timeflow drastically altered", false);
		return null;
	}

	private float getWeaponBoostInPercent(float effectLevel) {
		float mult = 1f + ROF_INCREASE_IN_MULT * effectLevel;

		float tick = 0.3f + tickTimes * 0.1f;
		if (tick > 1f) tick = 1f;
		mult *= tick;

		return (mult - 1f) * 100f;
	}

	public Vector2f moveVec(Vector2f vec, float x, float y, float facing) {
		return new Vector2f(vec.x + addVec(x, y, facing).x, vec.y + addVec(x, y, facing).y);
	}

	public Vector2f addVec(float x, float y, float facing) {
		return new Vector2f(x * (float)FastTrig.cos(Math.toRadians(facing - 90f)) - y * (float)FastTrig.sin(Math.toRadians(facing - 90f)), x * (float)FastTrig.sin(Math.toRadians(facing - 90f)) + y * (float)FastTrig.cos(Math.toRadians(facing - 90f)));
	}
}