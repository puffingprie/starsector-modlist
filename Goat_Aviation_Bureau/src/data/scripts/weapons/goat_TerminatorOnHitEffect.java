package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class goat_TerminatorOnHitEffect implements OnHitEffectPlugin {

	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
		if ((float)Math.random() > 0.1f && target instanceof ShipAPI) {

			float emp = projectile.getEmpAmount();
			float dam = projectile.getDamageAmount();

			engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam * 0.5f, emp, // emp
					100000f, // max range
					"tachyon_lance_emp_impact", 30f, // thickness
					new Color(14, 157, 136, 187), new Color(5, 37, 141, 75));

			engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam * 0.5f, emp, // emp
					100000f, // max range
					"tachyon_lance_emp_impact", 6f, // thickness
					new Color(37, 255, 255, 224), new Color(5, 136, 141, 75));

			//engine.spawnProjectile(null, null, "plasma", point, 0, new Vector2f(0, 0));
		}
	}
}
