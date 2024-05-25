package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.scripts.plugins.goat_ShipEveryFramePlugin;
import org.lwjgl.util.vector.Vector2f;

import java.util.Map;

public class goat_DazzleWarheadOnHitEffect implements OnHitEffectPlugin {

	@Override
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

		if (target instanceof ShipAPI && !shieldHit && engine.getCustomData().containsKey(goat_ShipEveryFramePlugin.PLUGIN_ID)) {
			ShipAPI targetShip = (ShipAPI)target;
			final goat_ShipEveryFramePlugin.LocalData localData = (goat_ShipEveryFramePlugin.LocalData)engine.getCustomData().get(goat_ShipEveryFramePlugin.PLUGIN_ID);
			final Map<ShipAPI, goat_ShipEveryFramePlugin.DazzleWarheadData> dazzleWarheadData = localData.dazzleWarheadData;
			if (!dazzleWarheadData.containsKey(targetShip)) {
				dazzleWarheadData.put(targetShip, new goat_ShipEveryFramePlugin.DazzleWarheadData());
			} else {
				goat_ShipEveryFramePlugin.DazzleWarheadData value = dazzleWarheadData.get(targetShip);
				value.hit += 1;
				value.time = 0f;
			}
		}
	}
}
