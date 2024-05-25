package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lwjgl.util.vector.Vector2f;
import static com.fs.starfarer.api.combat.DamageType.KINETIC;

public class CrunMedOnHitEffect implements OnHitEffectPlugin {

    private static final float EXTRA_DAMAGE = 200f;


    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (target instanceof ShipAPI && ((ShipAPI)target).isFighter()){
            engine.applyDamage (target, point, EXTRA_DAMAGE, KINETIC, 0f, false, true, projectile.getSource());

        }
    }
}
