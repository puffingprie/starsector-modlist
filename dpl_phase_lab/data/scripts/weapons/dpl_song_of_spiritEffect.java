package data.scripts.weapons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.BaseCombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EmpArcEntityAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.combat.RiftTrailEffect;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.FaderUtil;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

public class dpl_song_of_spiritEffect extends BaseCombatLayeredRenderingPlugin implements OnHitEffectPlugin {

	// each tick is on average .9 seconds
	// ticks can't be longer than a second or floating damage numbers separate
	
	public dpl_song_of_spiritEffect() {
	}
	
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
		if (projectile.isFading()) return;
		if (!(target instanceof ShipAPI)) return;
		if (shieldHit) {
			float dam = projectile.getDamageAmount();
			final ShipAPI theShip = (ShipAPI) target;
			theShip.getFluxTracker().increaseFlux(dam, true);
		} else {
			final ShipAPI theShip = (ShipAPI) target;
			Color OVERLOAD_COLOR = new Color(125,50,255,255);
			float DISRUPTION_DUR = 1.35f;
				
			float crLoss = 0.05f;
			theShip.setCurrentCR(theShip.getCurrentCR()-crLoss);
				
			theShip.setOverloadColor(OVERLOAD_COLOR);
			theShip.getFluxTracker().beginOverloadWithTotalBaseDuration(DISRUPTION_DUR);
			//target.getEngineController().forceFlameout(true);
				
			Global.getCombatEngine().addPlugin(new BaseEveryFrameCombatPlugin() {
				@Override
				public void advance(float amount, List<InputEventAPI> events) {
					if (!theShip.getFluxTracker().isOverloadedOrVenting()) {
						theShip.resetOverloadColor();
						Global.getCombatEngine().removePlugin(this);
					}
				}
			});
		}
	}
}




