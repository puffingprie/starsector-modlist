package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class goat_ImplementOnHitEffect implements OnHitEffectPlugin {

	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
		if ((float)Math.random() > 0.6f && target instanceof ShipAPI) {

			float emp = projectile.getEmpAmount();
			float dam = projectile.getDamageAmount();

			engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam * 2f, emp, // emp
					100000f, // max range
					"tachyon_lance_emp_impact", 30f, // thickness
					new Color(213, 149, 134, 75), new Color(141, 5, 65, 75));

			//engine.spawnProjectile(null, null, "plasma", point, 0, new Vector2f(0, 0));
		}
	}
}
