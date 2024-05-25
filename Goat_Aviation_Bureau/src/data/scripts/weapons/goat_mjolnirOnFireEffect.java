package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class goat_mjolnirOnFireEffect implements OnFireEffectPlugin {

	@Override
	public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {

		addHitParticles(engine, 75f, 20f, 21f, 40f, 1.0f, 30, new Color(255, 120, 50, 205), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
		addHitParticles(engine, 165f, 5f, 21f, 20f, 1.2f, 36, new Color(255, 120, 50, 205), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
		addHitParticles(engine, 25f, 22f, 21f, 50f, 1.1f, 20, new Color(255, 67, 50, 205), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
		addHitParticles(engine, 5f, 0f, 5f, 280f, 0.08f, 6, new Color(255, 153, 50, 255), projectile.getLocation(), projectile.getWeapon().getShip().getVelocity(), projectile.getFacing() - 0f);
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
}
