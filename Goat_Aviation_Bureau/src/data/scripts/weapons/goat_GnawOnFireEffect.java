package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.Misc;

public class goat_GnawOnFireEffect implements OnFireEffectPlugin {

	public static final String ID = "goat_GnawOnFireEffect";

	@Override
	public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
		if (engine.getCustomData().containsKey(goat_GnawDecoEffect.ID) && ((goat_GnawDecoEffect.LocalData)engine.getCustomData().get(goat_GnawDecoEffect.ID)).fireData.containsKey(weapon)) {
			goat_GnawDecoEffect.FireData value = ((goat_GnawDecoEffect.LocalData)engine.getCustomData().get(goat_GnawDecoEffect.ID)).fireData.get(weapon);
			int num = 3;
			if (value.more) {
				value.more = false;
				num = 4;
			} else {
				value.more = true;
			}

			for (int i = 0; i < num; i++) {
				DamagingProjectileAPI proj = (DamagingProjectileAPI)engine.spawnProjectile(weapon.getShip(), weapon, "all_goat_gnaw1", projectile.getLocation(), projectile.getFacing() + (Misc.random.nextFloat() - 0.5f) * weapon.getSpec().getMaxSpread(), weapon.getShip().getVelocity());
				if (proj != null) {
					proj.getDamage().setDamage(weapon.getDamage().getDamage() / 3.5f);

				}
			}
			engine.removeEntity(projectile);
		}
	}
}
