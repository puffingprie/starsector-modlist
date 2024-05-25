package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class goat_HowlOnFireEffect implements OnFireEffectPlugin {

	@Override
	public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {

		addHitParticles(engine, 60f, 9f, 30f, 34.0f, 0.35f, 10, new Color(195, 20, 69, 155), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() + 0f);
		addHitParticles(engine, 20f, 10f, 40f, 25f, 0.25f, 20, new Color(10, 10, 199, 155), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
		addHitParticles(engine, 15f, 10f, 10f, 75f, 0.1f, 10, new Color(47, 47, 229, 252), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
		addHitParticles(engine, 80f, 10f, 25f, 35.0f, 0.25f, 10, new Color(175, 52, 27, 155), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
		addHitParticles(engine, 0f, 0f, 30f, 185f, 0.08f, 2, new Color(88, 88, 238, 252), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
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
