package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class goat_FireSmokeironpavilionDecoEffect implements EveryFrameWeaponEffectPlugin {

	public static final String ID = "goat_FireSmokeDecoEffect";
	public static final float FIRE_DURATION = 4f;

	private final float move = 0f;
	private float fireTimer = 0f;
	private float timer = 0f;
	private boolean doOnce = false;

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

		ShipAPI ship = weapon.getShip();
		if (ship != null && ship.getSystem() != null) {
			if (ship.getSystem().isActive()) {
				Vector2f loc = weapon.getLocation();
				Vector2f vol = ship.getVelocity();

				if (!doOnce && fireTimer <= FIRE_DURATION) {
					fireTimer += engine.getElapsedInLastFrame();
				} else {
					doOnce = true;
					fireTimer = 0f;
				}

				if (timer < 0.07f) {
					timer += amount;
				} else {
					//fire
					if (!doOnce && fireTimer <= FIRE_DURATION) {
						addHitParticles(engine, 15f, 28f, 7f, 12f, 0.65f, 28, new Color(255, 98, 50, 45), loc, vol, weapon.getCurrAngle());

						addHitParticles(engine, 2f, 18f, 7f, 22f, 0.25f, 8, new Color(255, 171, 50, 245), loc, vol, weapon.getCurrAngle());

					}

					//smoke

					engine.addNebulaParticle(loc, new Vector2f(vol.x + (float)(Math.cos(Math.toRadians(weapon.getCurrAngle())) * 40f) + (Misc.random.nextFloat() - 0.5f) * 2f * 10f, vol.y + (float)(Math.sin(Math.toRadians(weapon.getCurrAngle())) * 40f) + (Misc.random.nextFloat() - 0.5f) * 2f * 10f), 10f, 2.8f, 0.2f, 2.2f, 1.8f, new Color(245, 111, 15, 255));
					engine.addNebulaParticle(loc, new Vector2f(vol.x + (float)(Math.cos(Math.toRadians(weapon.getCurrAngle())) * 40f) + (Misc.random.nextFloat() - 0.5f) * 2f * 10f, vol.y + (float)(Math.sin(Math.toRadians(weapon.getCurrAngle())) * 40f) + (Misc.random.nextFloat() - 0.5f) * 2f * 10f), 10f, 2.5f, 0.2f, 2.2f, 0.6f, new Color(253, 162, 44, 255));
					engine.addNebulaSmokeParticle(loc, new Vector2f(vol.x + (float)(Math.cos(Math.toRadians(weapon.getCurrAngle())) * 40f) + (Misc.random.nextFloat() - 0.5f) * 2f * 10f, vol.y + (float)(Math.sin(Math.toRadians(weapon.getCurrAngle())) * 40f) + (Misc.random.nextFloat() - 0.5f) * 2f * 10f), 28f, 1.1f, 1.6f, 3.0f, 2.3f, new Color(87, 31, 31, 186));

					engine.addNebulaSmokeParticle(loc, new Vector2f(vol.x + (float)(Math.cos(Math.toRadians(weapon.getCurrAngle())) * 40f) + (Misc.random.nextFloat() - 0.5f) * 2f * 10f, vol.y + (float)(Math.sin(Math.toRadians(weapon.getCurrAngle())) * 40f) + (Misc.random.nextFloat() - 0.5f) * 2f * 10f), 34f, 2.1f, 1.9f, 3.0f, 3.4f, new Color(56, 48, 46, 248));

					timer = 0f;
				}

			} else {
				doOnce = false;
			}
		}

	}

	private void addHitParticles(CombatEngineAPI engine, float length, float spread, float particleSizeMin, float particleSizeRange, float particleDuration, int particleCount, Color particleColor, Vector2f location, Vector2f velocity, float facing) {
		for (int i = 0; i < particleCount; i++) {
			float random = Misc.random.nextFloat();
			float random2 = Misc.random.nextFloat();
			Vector2f loc0 = new Vector2f(location.x + (Misc.random.nextFloat() - 0.5f) * particleSizeMin, location.y + (Misc.random.nextFloat() - 0.5f) * particleSizeMin);
			Vector2f vel0 = new Vector2f(velocity.x + length / particleDuration * random * (float)Math.cos(Math.toRadians(facing + spread * (random2 - 0.5f))), velocity.y + length / particleDuration * random * (float)Math.sin(Math.toRadians(facing + spread * (random2 - 0.5f))));

			Vector2f loc = new Vector2f(location.x + random * length * (float)Math.cos(Math.toRadians(facing)), location.y + random * length * (float)Math.sin(Math.toRadians(facing)));
			Vector2f vel = new Vector2f(velocity.x + spread / particleDuration * random * (float)Math.cos(Math.toRadians(facing)), velocity.y + spread / particleDuration * random * (float)Math.sin(Math.toRadians(facing)));
			engine.addHitParticle(loc, vel, particleSizeMin + (particleSizeRange - particleSizeMin) * Misc.random.nextFloat(), Math.max(0, Math.min(Math.round(particleColor.getAlpha() / 255f), 255)), particleDuration, new Color(particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue()));
		}
	}
}