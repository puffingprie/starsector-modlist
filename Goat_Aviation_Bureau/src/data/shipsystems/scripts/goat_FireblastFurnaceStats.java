package data.shipsystems.scripts;

import com.fs.starfarer.api.AnimationAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.plugins.MagicRenderPlugin;

import java.awt.Color;

public class goat_FireblastFurnaceStats extends BaseShipSystemScript {

	public static final float ROF_BONUS = 20f;
	public static final float FLUX_REDUCTION = 20f;
	public static final float AMMO = 6f;

	public static final float TOTAL_ACTIVE_DURATION = Global.getSettings().getShipSystemSpec("goat_fireblast_furnace").getActive();
	public static final Vector2f ZERO = new Vector2f();

	private float progressLevel = 0f;
	private float timer = 0f;

	private final SpriteAPI jitter = Global.getSettings().getSprite("misc", "goat_ironpavilion_glow");

	@Override
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		float mult = 1f + ROF_BONUS * effectLevel;
		stats.getEnergyRoFMult().modifyMult(id, mult);
		stats.getEnergyWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_REDUCTION * 0.01f));
		stats.getEnergyAmmoBonus().modifyMult(id, AMMO);

		ShipAPI ship = (ShipAPI)stats.getEntity();
		if (ship == null || !ship.isAlive()) return;

		CombatEngineAPI engine = Global.getCombatEngine();
		float amount = engine.getElapsedInLastFrame();
		if (engine.isPaused()) return;

		if (state == State.IN) {

			WeaponAPI ignite1 = getWeaponFromId(ship, "goat_ironpavilion_4");
			if (ignite1 != null) {
				SpriteAPI sprite = ignite1.getSprite();
				float offset = effectLevel / 0.65f;
				if (offset > 1f) offset = 4f * (1f - effectLevel);

				Vector2f location = ignite1.getLocation();
				location = MathUtils.getPoint(location, sprite.getWidth() * offset * MathUtils.getRandomNumberInRange(0.1f, 0.2f), ignite1.getCurrAngle() + MathUtils.getRandomNumberInRange(10f, 80f));
				location = MathUtils.getPoint(location, sprite.getHeight() * offset * MathUtils.getRandomNumberInRange(0.1f, 0.2f), ignite1.getCurrAngle() + MathUtils.getRandomNumberInRange(10f, 80f));
				// 不常用颜色
				engine.addSmokeParticle(location, ZERO, 9f, 2f, 4f, new Color(114, 107, 107, 245));
			}

			WeaponAPI ignite = getWeaponFromId(ship, "goat_ironpavilion_side_32");
			if (ignite != null) {
				SpriteAPI sprite = ignite.getSprite();
				float offset = effectLevel / 0.65f;
				if (offset > 1f) offset = 4f * (1f - effectLevel);

				sprite.setCenterX(sprite.getWidth() * (0.5f + offset * 0.3f));
				sprite.setCenterY(sprite.getHeight() * (0.5f + offset * 0.3f));

				Vector2f location = ignite.getLocation();
				location = MathUtils.getPoint(location, sprite.getWidth() * offset * MathUtils.getRandomNumberInRange(-0.6f, 0.1f), ignite.getCurrAngle() + MathUtils.getRandomNumberInRange(-180f, -10f));
				location = MathUtils.getPoint(location, sprite.getHeight() * offset * MathUtils.getRandomNumberInRange(-0.6f, 0.1f), ignite.getCurrAngle() + MathUtils.getRandomNumberInRange(-180f, -10f));

				Vector2f location1 = ignite.getLocation();
				location1 = MathUtils.getPoint(location1, sprite.getWidth() * offset * MathUtils.getRandomNumberInRange(-0.6f, 0.1f), ignite.getCurrAngle() + MathUtils.getRandomNumberInRange(-180f, -10f));
				location1 = MathUtils.getPoint(location1, sprite.getHeight() * offset * MathUtils.getRandomNumberInRange(-0.6f, 0.1f), ignite.getCurrAngle() + MathUtils.getRandomNumberInRange(-180f, -10f));

				// 三个常用颜色，记得替换
				engine.addSmokeParticle(location1, ZERO, 10f, 7f, 3f, new Color(45, 45, 45, 145));
				engine.addSmokeParticle(location1, ZERO, 7f, 3f, 2f, new Color(91, 76, 76, 145));
				engine.addNebulaParticle(location, ZERO, 5f, 8f, 0.1f, 0.1f, 0.5f, new Color(201, 62, 7, 245));
			}
		} else if (state == State.ACTIVE) {
			timer += 0.005f;
			progressLevel = Math.min(timer / TOTAL_ACTIVE_DURATION, 4f);

			// 在此自行加入 “这里排除一点点的烟雾”、“几个那种”

		} else if (state == State.OUT) {
			progressLevel = Math.min(progressLevel, effectLevel);
		}

		WeaponAPI side = getWeaponFromId(ship, "goat_ironpavilion_side_21");
		if (side != null) {
			SpriteAPI sprite = side.getSprite();
			float offset = convertRate(progressLevel, 0f, 0.15f);
			sprite.setCenterX(sprite.getWidth() * (0.5f + offset * 0.6f));
		}

		WeaponAPI flip1 = getWeaponFromId(ship, "goat_ironpavilion_1");
		if (flip1 != null) {
			AnimationAPI animation = flip1.getAnimation();
			int maxFrame = animation.getNumFrames() - 1;
			int currentFrame = (int)(convertRate(progressLevel, 0.1f, 0.18f) * maxFrame);
			animation.setFrame(currentFrame);

			SpriteAPI sprite = flip1.getSprite();
			float offset = convertRate(progressLevel, 0.5f, 0.11f);
			sprite.setCenterX(sprite.getWidth() * (0.5f - offset * 0.05f));
		}

		WeaponAPI flip2 = getWeaponFromId(ship, "goat_ironpavilion_2");
		if (flip2 != null) {
			AnimationAPI animation = flip2.getAnimation();
			int maxFrame = animation.getNumFrames() - 1;
			int currentFrame = (int)(convertRate(progressLevel, 0.15f, 0.23f) * maxFrame);
			animation.setFrame(currentFrame);

		}

		WeaponAPI flip3 = getWeaponFromId(ship, "goat_ironpavilion_3");
		if (flip3 != null) {
			AnimationAPI animation = flip3.getAnimation();
			int maxFrame = animation.getNumFrames() - 1;
			int currentFrame = (int)(convertRate(progressLevel, 0.20f, 0.28f) * maxFrame);
			animation.setFrame(currentFrame);

			SpriteAPI sprite = flip3.getSprite();
			float offset = convertRate(progressLevel, 0.18f, 0.22f);
			sprite.setCenterX(sprite.getWidth() * (0.5f - offset * 0.1f));
		}

		WeaponAPI stickLong = getWeaponFromId(ship, "goat_ironpavilion_side_22");
		if (stickLong != null) {
			SpriteAPI sprite = stickLong.getSprite();
			float offset = convertRate(progressLevel, 0.15f, 0.28f);

			sprite.setCenterY(sprite.getHeight() * (0.5f - offset * 1.3f));
		}

		WeaponAPI stickLong1 = getWeaponFromId(ship, "goat_ironpavilion_side_4");
		if (stickLong1 != null) {
			SpriteAPI sprite = stickLong1.getSprite();
			float offset = convertRate(progressLevel, 0.18f, 0.24f);
			sprite.setCenterX(sprite.getWidth() * (0.5f + offset * 0.6f));
		}

		WeaponAPI stickShort = getWeaponFromId(ship, "goat_ironpavilion_side_12");
		if (stickShort != null) {
			SpriteAPI sprite = stickShort.getSprite();
			float offset = convertRate(progressLevel, 0.22f, 0.26f);
			sprite.setCenterY(sprite.getHeight() * (0.5f - offset));
		}

		if (progressLevel > 0.22f) {

			WeaponSlotAPI jitterSlot1 = ship.getHullSpec().getWeaponSlotAPI("glow");
			if (jitterSlot1 != null) {
				float jitterLevel = convertRate(progressLevel, 0.22f, 0.55f);
				if (jitterLevel > 0f) {
					Vector2f location = jitterSlot1.computePosition(ship);
					float angle = jitterSlot1.computeMidArcAngle(ship) - 90f;

					jitter.setAngle(angle);
					jitter.setColor(new Color(201, 75, 7, 155));
					jitter.setAlphaMult(0.4f);
					for (int i = 1; i < 11; i++) {
						Vector2f jitterLocation = MathUtils.getRandomPointOnCircumference(location, 10f * jitterLevel);
						MagicRenderPlugin.addSingleframe(jitter, jitterLocation, CombatEngineLayers.ABOVE_SHIPS_LAYER);
					}
				}
			}

			WeaponAPI ignite1 = getWeaponFromId(ship, "goat_ironpavilion_side_4");
			WeaponAPI ignite2 = getWeaponFromId(ship, "goat_ironpavilion_glow1");
			if (ignite1 != null & ignite2 != null) {
				SpriteAPI sprite = ignite1.getSprite();
				float offset = effectLevel / 0.75f;

				Vector2f location = ignite1.getLocation();
				location = MathUtils.getPoint(location, sprite.getWidth() * offset * MathUtils.getRandomNumberInRange(0.0f, 0.001f), ignite1.getCurrAngle() + MathUtils.getRandomNumberInRange(10f, 180f));
				location = MathUtils.getPoint(location, sprite.getHeight() * offset * MathUtils.getRandomNumberInRange(-0.4f, 0.4f), ignite1.getCurrAngle() + MathUtils.getRandomNumberInRange(10f, 180f));

				engine.addSmokeParticle(location, ZERO, 9f, 2f, 0.3f, new Color(162, 149, 149, 245));
				engine.addSmokeParticle(location, ZERO, 8f, 5f, 1f, new Color(114, 107, 107, 245));

				SpriteAPI sprite1 = ignite2.getSprite();
				float offset1 = effectLevel / 0.75f;

				Vector2f location1 = ignite2.getLocation();
				location1 = MathUtils.getPoint(location1, sprite1.getWidth() * offset * MathUtils.getRandomNumberInRange(0.0f, 0.001f), ignite2.getCurrAngle() + MathUtils.getRandomNumberInRange(10f, 80f));
				location1 = MathUtils.getPoint(location1, sprite1.getHeight() * offset * MathUtils.getRandomNumberInRange(-0.4f, 0.4f), ignite2.getCurrAngle() + MathUtils.getRandomNumberInRange(10f, 80f));
				// 不常用颜色
				engine.addHitParticle(location1, ZERO, 15f, 2f, 0.5f, new Color(232, 108, 17, 245));
			}

		}

	}

	@Override
	public void unapply(MutableShipStatsAPI stats, String id) {
		ShipAPI ship = (ShipAPI)stats.getEntity();
		CombatEngineAPI engine = Global.getCombatEngine();

		stats.getEnergyRoFMult().unmodify(id);
		stats.getEnergyWeaponFluxCostMod().unmodify(id);
		stats.getEnergyAmmoRegenMult().unmodify(id);

		timer = 0f;
		progressLevel = 0f;
	}

	@Override
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float mult = 1f + ROF_BONUS * effectLevel;
		float bonusPercent = (int)((mult - 1f) * 100f);
		if (index == 0) return new StatusData("Meltbore Rate of fire +" + (int) bonusPercent + "%", false);
		if (index == 1) return new StatusData("Meltbore flux generation -" + (int) FLUX_REDUCTION + "%", false);
		return null;
	}

	public static WeaponAPI getWeaponFromSlot(ShipAPI ship, String slot) {
		for (WeaponAPI weapon : ship.getAllWeapons()) {
			if (weapon.getSlot().getId().contentEquals(slot)) return weapon;
		}
		return null;
	}

	public static WeaponAPI getWeaponFromId(ShipAPI ship, String id) {
		for (WeaponAPI weapon : ship.getAllWeapons()) {
			if (weapon.getId().contentEquals(id)) return weapon;
		}
		return null;
	}

	public static float convertRate(float level, float min, float max) {
		if (level <= min) return 0f;
		if (level >= max) return 1f;
		return (level - min) / (max - min);
	}

}