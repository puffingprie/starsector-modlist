package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.WeaponAPI.AIHints;
import com.fs.starfarer.api.combat.listeners.WeaponRangeModifier;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicRender;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class goat_CallStats extends BaseShipSystemScript {

	public static final String ID = "Goat_Call";

	public static final float ROF_BONUS = 2.0f;
	public static final float FLUX_REDUCTION = 50f;

	private boolean initialExplosion = false;
	private float nebulaTimer = 0f;
	private float smokeTimer = 0f;
	private final SpriteAPI[] sprites = {Global.getSettings().getSprite("misc", "goat_immortality_glow00"), Global.getSettings().getSprite("misc", "goat_immortality_glow01"), Global.getSettings().getSprite("misc", "goat_immortality_glow02"), Global.getSettings().getSprite("misc", "goat_immortality_glow03"), Global.getSettings().getSprite("misc", "goat_immortality_glow04"), Global.getSettings().getSprite("misc", "goat_immortality_glow05"), Global.getSettings().getSprite("misc", "goat_immortality_glow06"), Global.getSettings().getSprite("misc", "goat_immortality_glow07"), Global.getSettings().getSprite("misc", "goat_immortality_glow08"), Global.getSettings().getSprite("misc", "goat_immortality_glow09"), Global.getSettings().getSprite("misc", "goat_immortality_glow10"), Global.getSettings().getSprite("misc", "goat_immortality_glow11"), Global.getSettings().getSprite("misc", "goat_immortality_glow12"), Global.getSettings().getSprite("misc", "goat_immortality_glow13"), Global.getSettings().getSprite("misc", "goat_immortality_glow14")};

	//@Override
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		float mult = 1f + ROF_BONUS * effectLevel;
		stats.getBallisticRoFMult().modifyMult(id, mult);
		stats.getBallisticWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_REDUCTION * 0.01f));
		stats.getEnergyRoFMult().modifyMult(id, mult);
		stats.getEnergyWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_REDUCTION * 0.01f));

		ShipAPI ship = (ShipAPI)stats.getEntity();
		CombatEngineAPI engine = Global.getCombatEngine();
		ShipSystemAPI system = ship.getSystem();

		float longest = -1f;
		for (WeaponAPI weaponTemp : ship.getAllWeapons()) {

			if (weaponTemp.hasAIHint(AIHints.PD)) continue;
			if (weaponTemp.getType() == WeaponAPI.WeaponType.MISSILE) continue;

			float range = weaponRangeBonus(stats, weaponTemp);
			longest = Math.max(longest, range);
		}

		Map<WeaponAPI, Float> mults = new HashMap<>();
		for (WeaponAPI weaponTemp : ship.getAllWeapons()) {
			float range = weaponRangeBonus(stats, weaponTemp);

			mults.put(weaponTemp, Math.max(1f, 1f + (longest / range - 1f) * effectLevel));
		}

		if (!engine.getCustomData().containsKey(ID)) {
			engine.getCustomData().put(ID, new HashMap<ShipAPI, Map<WeaponAPI, Float>>());
		}
		((Map<ShipAPI, Map<WeaponAPI, Float>>)engine.getCustomData().get(ID)).put(ship, mults);

		if (!ship.hasListenerOfClass(Goat_CallRangeMod.class)) {
			ship.addListener(new Goat_CallRangeMod());
		}

		if (effectLevel >= 1f) {
			jitter(sprites[12], moveVec(ship.getLocation(), 0f, 88f, ship.getFacing()), ship.getFacing(), new Color(44, 133, 234, 70), 8f, 3, 10f);
			jitter(sprites[13], moveVec(ship.getLocation(), 0f, 88f, ship.getFacing()), ship.getFacing(), new Color(76, 49, 203, 129), 1.2f, 3, 3f);
			jitter(sprites[14], moveVec(ship.getLocation(), 0f, 88f, ship.getFacing()), ship.getFacing(), new Color(229, 39, 115, 240), 0.8f, 3, 2f);

			if (!initialExplosion) {
				initialExplosion = true;
				engine.spawnExplosion(moveVec(ship.getLocation(), 140f, 110f, ship.getFacing()), ship.getVelocity(), new Color(234, 44, 50, 200), 190f, 0.3f);
				engine.spawnExplosion(moveVec(ship.getLocation(), -140f, 110f, ship.getFacing()), ship.getVelocity(), new Color(234, 44, 50, 220), 190f, 0.3f);
			}

			if (nebulaTimer >= 0.1f) {
				nebulaTimer = 0f;
				engine.addNebulaParticle(moveVec(ship.getLocation(), 120f, 120f, ship.getFacing()), moveVec(ship.getVelocity(), 15f, -20f, ship.getFacing()), 40f, 1f, 0.1f, 0.1f, 1f, new Color(217, 238, 235, 104));
				engine.addNebulaParticle(moveVec(ship.getLocation(), 115f, 100f, ship.getFacing()), moveVec(ship.getVelocity(), 15f, -20f, ship.getFacing()), 40f, 1f, 0.1f, 0.1f, 1f, new Color(217, 238, 235, 104));
				engine.addNebulaParticle(moveVec(ship.getLocation(), 130f, 110f, ship.getFacing()), moveVec(ship.getVelocity(), 30f, -20f, ship.getFacing()), 110f, 0.7f, 0.1f, 0.1f, 1f, new Color(229, 39, 115, 132));
				engine.addNebulaParticle(moveVec(ship.getLocation(), 140f, 110f, ship.getFacing()), moveVec(ship.getVelocity(), 170f, -30f, ship.getFacing()), 100f, 1f, 0.1f, 0.1f, 1f, new Color(21, 81, 171, 150));
				engine.addNebulaParticle(moveVec(ship.getLocation(), 140f, 100f, ship.getFacing()), moveVec(ship.getVelocity(), 85f, -30f, ship.getFacing()), 15f, 1.5f, 0.1f, 0.1f, 1.2f, new Color(204, 33, 64, 210));
				engine.addNebulaParticle(moveVec(ship.getLocation(), 140f, 120f, ship.getFacing()), moveVec(ship.getVelocity(), 85f, -30f, ship.getFacing()), 15f, 1.5f, 0.1f, 0.1f, 1.2f, new Color(204, 33, 64, 210));
				engine.addNebulaParticle(moveVec(ship.getLocation(), 140f, 85f, ship.getFacing()), moveVec(ship.getVelocity(), 85f, -60f, ship.getFacing()), 30f, 1f, 0.1f, 0.1f, 1f, new Color(50, 33, 204, 250));
				engine.addNebulaParticle(moveVec(ship.getLocation(), 140f, 135f, ship.getFacing()), moveVec(ship.getVelocity(), 85f, 30f, ship.getFacing()), 30f, 1f, 0.1f, 0.1f, 1f, new Color(50, 33, 204, 250));

				engine.addNebulaParticle(moveVec(ship.getLocation(), -120f, 120f, ship.getFacing()), moveVec(ship.getVelocity(), -15f, -20f, ship.getFacing()), 40f, 1f, 0.1f, 0.1f, 1f, new Color(217, 238, 235, 104));
				engine.addNebulaParticle(moveVec(ship.getLocation(), -115f, 100f, ship.getFacing()), moveVec(ship.getVelocity(), -15f, -20f, ship.getFacing()), 40f, 1f, 0.1f, 0.1f, 1f, new Color(217, 238, 235, 104));
				engine.addNebulaParticle(moveVec(ship.getLocation(), -130f, 110f, ship.getFacing()), moveVec(ship.getVelocity(), -30f, -20f, ship.getFacing()), 110f, 0.7f, 0.1f, 0.1f, 1f, new Color(229, 39, 115, 132));
				engine.addNebulaParticle(moveVec(ship.getLocation(), -140f, 110f, ship.getFacing()), moveVec(ship.getVelocity(), -170f, -30f, ship.getFacing()), 100f, 1f, 0.1f, 0.1f, 1f, new Color(21, 81, 171, 150));
				engine.addNebulaParticle(moveVec(ship.getLocation(), -140f, 100f, ship.getFacing()), moveVec(ship.getVelocity(), -85f, -30f, ship.getFacing()), 15f, 1.5f, 0.1f, 0.1f, 1.2f, new Color(204, 33, 64, 210));
				engine.addNebulaParticle(moveVec(ship.getLocation(), -140f, 120f, ship.getFacing()), moveVec(ship.getVelocity(), -85f, -30f, ship.getFacing()), 15f, 1.5f, 0.1f, 0.1f, 1.2f, new Color(204, 33, 64, 210));
				engine.addNebulaParticle(moveVec(ship.getLocation(), -140f, 85f, ship.getFacing()), moveVec(ship.getVelocity(), -85f, -60f, ship.getFacing()), 30f, 1f, 0.1f, 0.1f, 1f, new Color(50, 33, 204, 250));
				engine.addNebulaParticle(moveVec(ship.getLocation(), -140f, 135f, ship.getFacing()), moveVec(ship.getVelocity(), -85f, 30f, ship.getFacing()), 30f, 1f, 0.1f, 0.1f, 1f, new Color(50, 33, 204, 250));

			} else {
				nebulaTimer += engine.getElapsedInLastFrame();
			}
		} else if (system.isChargeup()) {
			int num = (int)(effectLevel * 13f);
			jitter(sprites[num], moveVec(ship.getLocation(), 0f, 88f, ship.getFacing()), ship.getFacing(), new Color(206, 32, 9, 216), 0.2f, 5, 6f);
			engine.addNebulaParticle(moveVec(ship.getLocation(), 110f, 90f, ship.getFacing()), moveVec(ship.getVelocity(), 15f, 90f, ship.getFacing()), 50f, 2f, 0.1f, 0.1f, 1f, new Color(140, 26, 26, 26));

			engine.addNebulaParticle(moveVec(ship.getLocation(), -110f, 90f, ship.getFacing()), moveVec(ship.getVelocity(), -15f, 90f, ship.getFacing()), 50f, 2f, 0.1f, 0.1f, 1f, new Color(140, 26, 26, 26));

		} else if (system.isChargedown()) {

			jitter(sprites[12], moveVec(ship.getLocation(), 0f, 88f, ship.getFacing()), ship.getFacing(), new Color(218, 102, 86, 20), 4f, 1, 5f);

			if (smokeTimer >= 0.001f) {
				smokeTimer = 0f;
				addSmoke(engine, ship, 120f, 155f);
				addSmoke(engine, ship, 100f, 135f);
				addSmoke(engine, ship, 90f, 110f);
				addSmoke(engine, ship, 100f, 85f);
				addSmoke(engine, ship, 120f, 65f);

				addSmoke(engine, ship, 120f, 150f);
				addSmoke(engine, ship, 100f, 130f);
				addSmoke(engine, ship, 90f, 115f);
				addSmoke(engine, ship, 100f, 80f);
				addSmoke(engine, ship, 120f, 60f);

				addSmoke(engine, ship, 110f, 150f);
				addSmoke(engine, ship, 90f, 130f);
				addSmoke(engine, ship, 70f, 115f);
				addSmoke(engine, ship, -110f, 150f);
				addSmoke(engine, ship, -90f, 130f);
				addSmoke(engine, ship, -80f, 115f);

				addSmoke(engine, ship, -120f, 155f);
				addSmoke(engine, ship, -100f, 135f);
				addSmoke(engine, ship, -90f, 110f);
				addSmoke(engine, ship, -100f, 85f);
				addSmoke(engine, ship, -120f, 65f);

				addSmoke(engine, ship, -120f, 150f);
				addSmoke(engine, ship, -100f, 130f);
				addSmoke(engine, ship, -90f, 115f);
				addSmoke(engine, ship, -100f, 80f);
				addSmoke(engine, ship, -120f, 60f);
			} else {
				smokeTimer += engine.getElapsedInLastFrame();
			}

		}
	}

	private float weaponRangeBonus(MutableShipStatsAPI stats, WeaponAPI weapon) {
		float range = weapon.getSpec().getMaxRange();
		float percent, flat, mult;
		if (weapon.getSpec().getType().equals(WeaponAPI.WeaponType.ENERGY)) {
			percent = stats.getEnergyWeaponRangeBonus().getPercentMod();
			flat = stats.getEnergyWeaponRangeBonus().getFlatBonus();
			mult = stats.getEnergyWeaponRangeBonus().getBonusMult();

			if (weapon.isBeam()) {
				percent += stats.getBeamWeaponRangeBonus().getPercentMod();
				flat += stats.getBeamWeaponRangeBonus().getFlatBonus();
				mult *= stats.getBeamWeaponRangeBonus().getBonusMult();
			}
		} else if (weapon.getSpec().getType().equals(WeaponAPI.WeaponType.BALLISTIC)) {
			percent = stats.getBallisticWeaponRangeBonus().getPercentMod();
			flat = stats.getBallisticWeaponRangeBonus().getFlatBonus();
			mult = stats.getBallisticWeaponRangeBonus().getBonusMult();

		} else {
			percent = 0f;
			flat = 0f;
			mult = 1f;
		}

		return (range * (1f + percent / 100f) + flat) * mult;
	}

	@Override
	public void unapply(MutableShipStatsAPI stats, String id) {

		ShipAPI ship = (ShipAPI)stats.getEntity();
		if (ship.hasListenerOfClass(Goat_CallRangeMod.class)) {
			ship.removeListenerOfClass(Goat_CallRangeMod.class);
		}

		initialExplosion = false;
	}

	@Override
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float mult = 1f + ROF_BONUS * effectLevel;
		float bonusPercent = (int)((mult - 1f) * 100f);
		if (index == 0) return new StatusData("Non-missile weapon rate of fire +" + (int) bonusPercent + "%", false);
		if (index == 1) return new StatusData("Non-missile weapon flux generation -" + (int) FLUX_REDUCTION + "%", false);
		if (index == 2) return new StatusData("Small/medium weapon range increased", false);
		return null;
	}

	public Vector2f moveVec(Vector2f vec, float x, float y, float facing) {
		return new Vector2f(vec.x + addVec(x, y, facing).x, vec.y + addVec(x, y, facing).y);
	}

	public Vector2f addVec(float x, float y, float facing) {
		return new Vector2f(x * (float)Math.cos(Math.toRadians(facing - 90f)) - y * (float)Math.sin(Math.toRadians(facing - 90f)), x * (float)Math.sin(Math.toRadians(facing - 90f)) + y * (float)Math.cos(Math.toRadians(facing - 90f)));
	}

	private void addSmoke(CombatEngineAPI engine, ShipAPI ship, float x, float y) {
		engine.addSmokeParticle(moveVec(ship.getLocation(), x, y, ship.getFacing()), new Vector2f((Misc.random.nextFloat() - 0.5f) * 2f * 20f, (Misc.random.nextFloat() - 0.5f) * 2f * 20f), 15f, 0.01f, 1f, new Color(255, 255, 255, 25));
	}

	private void jitter(SpriteAPI sprite, Vector2f loc, float facing, Color color, float intensity, int copies, float range) {

		sprite.setAlphaMult(intensity);
		float c = copies > 0 ? copies : 1;
		Random random = Misc.random;

		for (int i = 0; i < c; i++) {
			MagicRender.singleframe(sprite, moveVec(loc, random.nextFloat() * range, 0f, random.nextFloat() * 360f), new Vector2f(sprite.getWidth(), sprite.getHeight()), facing - 90f, color, false);
		}

	}

	public static class Goat_CallRangeMod implements WeaponRangeModifier {

		@Override
		public float getWeaponRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
			return 0;
		}

		@Override
		public float getWeaponRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
			return 0;
		}

		@Override
		public float getWeaponRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
			CombatEngineAPI engine = Global.getCombatEngine();
			if (engine == null) return 1f;
			if (weapon == null || ship == null || !ship.getSystem().isActive()) return 1f;
			if (weapon.getSpec().getType().equals(WeaponAPI.WeaponType.MISSILE) || weapon.getSize().equals(WeaponAPI.WeaponSize.LARGE)) {
				return 1f;
			}
			if (!engine.getCustomData().containsKey(ID)) return 1f;

			Map<ShipAPI, Map<WeaponAPI, Float>> map = (Map<ShipAPI, Map<WeaponAPI, Float>>)engine.getCustomData().get(ID);

			if (map.containsKey(ship) && map.get(ship).containsKey(weapon)) {
				return map.get(ship).get(weapon);
			}
			return 1f;
		}
	}
}