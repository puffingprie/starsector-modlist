package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class goat_DiisciplineOnFireEffect implements OnFireEffectPlugin {

	@Override
	public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {

		addSmoothParticle(engine, 450f, 5f, 9f, 24f, 1f, 20, new Color(87, 2, 24, 32), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);

		addSmokeParticle(engine, 80f, 90f, 9f, 34f, 1.5f, 40, new Color(22, 22, 31, 65), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);

		addHitParticles(engine, 180f, 3f, 3f, 20f, 1.0f, 50, new Color(107, 84, 126, 132), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
		addHitParticles(engine, 60f, 5f, 3f, 45f, 1.3f, 10, new Color(96, 136, 129, 130), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
		addHitParticles(engine, 45f, 80f, 3f, 50f, 0.6f, 10, new Color(65, 38, 145, 139), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
		addHitParticles(engine, 15f, 180f, 3f, 167f, 0.1f, 10, new Color(70, 50, 255, 255), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
	}

	private void addHitParticles(CombatEngineAPI engine, float length, float spread, float particleSizeMin, float particleSizeRange, float particleDuration, int particleCount, Color particleColor, Vector2f location, Vector2f velocity, float facing) {
		for (int i = 0; i < particleCount; i++) {
			float random = Misc.random.nextFloat();
			float random2 = Misc.random.nextFloat();
			Vector2f loc = new Vector2f(location.x + random * length * (float)Math.cos(Math.toRadians(facing + spread * (random2 - 0.5f))), location.y + random * length * (float)Math.sin(Math.toRadians(facing + spread * (random2 - 0.5f))));
			Vector2f vel = new Vector2f(velocity.x + 10f * random * (float)Math.cos(Math.toRadians(facing + spread * (random2 - 0.5f))), velocity.y + 10f * random * (float)Math.sin(Math.toRadians(facing + spread * (random2 - 0.5f))));
			engine.addHitParticle(loc, vel, particleSizeMin + (particleSizeRange - particleSizeMin) * Misc.random.nextFloat(), Math.max(0, Math.min(Math.round(particleColor.getAlpha() / 255f), 255)), particleDuration, new Color(particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue()));
		}
	}

	private void addSmoothParticle(CombatEngineAPI engine, float length, float spread, float particleSizeMin, float particleSizeRange, float particleDuration, int particleCount, Color particleColor, Vector2f location, Vector2f velocity, float facing) {
		for (int i = 0; i < particleCount; i++) {
			float random = Misc.random.nextFloat();
			float random2 = Misc.random.nextFloat();
			Vector2f loc = new Vector2f(location.x + random * length * (float)Math.cos(Math.toRadians(facing + spread * (random2 - 0.5f))), location.y + random * length * (float)Math.sin(Math.toRadians(facing + spread * (random2 - 0.5f))));
			Vector2f vel = new Vector2f(velocity.x + 10f * random * (float)Math.cos(Math.toRadians(facing + spread * (random2 - 0.5f))), velocity.y + 10f * random * (float)Math.sin(Math.toRadians(facing + spread * (random2 - 0.5f))));
			engine.addSmoothParticle(loc, vel, particleSizeMin + (particleSizeRange - particleSizeMin) * Misc.random.nextFloat(), Math.max(0, Math.min(Math.round(particleColor.getAlpha() / 255f), 255)), particleDuration, new Color(particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue()));
		}
	}

	private void addSmokeParticle(CombatEngineAPI engine, float length, float spread, float particleSizeMin, float particleSizeRange, float particleDuration, int particleCount, Color particleColor, Vector2f location, Vector2f velocity, float facing) {
		for (int i = 0; i < particleCount; i++) {
			float random = Misc.random.nextFloat();
			float random2 = Misc.random.nextFloat();
			Vector2f loc = new Vector2f(location.x + random * length * (float)Math.cos(Math.toRadians(facing + spread * (random2 - 0.5f))), location.y + random * length * (float)Math.sin(Math.toRadians(facing + spread * (random2 - 0.5f))));
			Vector2f vel = new Vector2f(velocity.x + 10f * random * (float)Math.cos(Math.toRadians(facing + spread * (random2 - 0.5f))), velocity.y + 10f * random * (float)Math.sin(Math.toRadians(facing + spread * (random2 - 0.5f))));
			engine.addSmokeParticle(loc, vel, particleSizeMin + (particleSizeRange - particleSizeMin) * Misc.random.nextFloat(), Math.max(0, Math.min(Math.round(particleColor.getAlpha() / 255f), 255)), particleDuration, new Color(particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue()));
		}
	}
}
