package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class goat_flarelaunchertrail implements EveryFrameWeaponEffectPlugin {

	public static final float FIRE_DURATION = 1f;

	private float fireTimer = 0f;
	private float timer = 0f;
	private boolean doOnce = false;

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

		//系统被激活过后，第一次进入充能时放出油罐
		if (weapon.getShip().getSystem().isActive()) {
			if (!doOnce && fireTimer <= FIRE_DURATION) {
				fireTimer += engine.getElapsedInLastFrame();
			} else {
				doOnce = true;
				fireTimer = 0f;
			}

			if (timer < 0.58f) {
				timer += amount;
			} else {
				//smoke
				engine.spawnMuzzleFlashOrSmoke(weapon.getShip(), weapon.getSlot(), weapon.getSpec(), 0, weapon.getCurrAngle());
				DamagingProjectileAPI tank = (DamagingProjectileAPI)engine.spawnProjectile(weapon.getShip(), null, "goat_flarelauncher2", weapon.getFirePoint(0), weapon.getCurrAngle() + MathUtils.getRandomNumberInRange(-25, 25), weapon.getShip().getVelocity());
				tank.setAngularVelocity(MathUtils.getRandomNumberInRange(-15, 15));

				Global.getSoundPlayer().playSound("launch_flare_goat", 0.6f, 0.6f, weapon.getLocation(), new Vector2f());
				timer = 0f;
			}
		}
	}
}
