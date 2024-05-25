package data.scripts.weapons;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.BeamEffectPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.EmpArcEntityAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.util.IntervalUtil;

public class dpl_song_of_chaosEffect implements BeamEffectPlugin {

	private IntervalUtil fireInterval = new IntervalUtil(0.05f, 0.15f);
	private boolean wasZero = true;
	private float AD_COEFFICIENT = 0.02f;
	
	
	public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
		CombatEntityAPI target = beam.getDamageTarget();
		if (target instanceof ShipAPI && beam.getBrightness() >= 1f) {
			float dur = beam.getDamage().getDpsDuration();
			// needed because when the ship is in fast-time, dpsDuration will not be reset every frame as it should be
			if (!wasZero) dur = 0;
			wasZero = beam.getDamage().getDpsDuration() <= 0;
			fireInterval.advance(dur);
			
			if (fireInterval.intervalElapsed()) {
				ShipAPI ship = (ShipAPI) target;
				boolean hitShield = target.getShield() != null && target.getShield().isWithinArc(beam.getTo());
				float pierceChance = 1f;
				pierceChance *= ship.getMutableStats().getDynamic().getValue(Stats.SHIELD_PIERCED_MULT);
				
				boolean piercedShield = hitShield && (float) Math.random() < pierceChance;
				//piercedShield = true;
				
				if (!hitShield || piercedShield) {
					Vector2f point = beam.getRayEndPrevFrame();
					float emp = beam.getDamage().getFluxComponent() * 1f;
					float dam = beam.getDamage().getDamage() * 1f;
					EmpArcEntityAPI new_arc = engine.spawnEmpArcPierceShields(
									   beam.getSource(), point, beam.getDamageTarget(), beam.getDamageTarget(),
									   DamageType.ENERGY, 
									   dam, // damage
									   emp, // emp 
									   100000f, // max range 
									   "tachyon_lance_emp_impact",
									   beam.getWidth() + 9f,
									   beam.getFringeColor(),
									   beam.getCoreColor()
									   );
					if ((beam.getDamageTarget() instanceof ShipAPI)) {
						ShipAPI targetShip = (ShipAPI) beam.getDamageTarget();
						Vector2f loc = new_arc.getTargetLocation();
						float maxArmor = targetShip.getHullSpec().getArmorRating();
						float armorDam = AD_COEFFICIENT*maxArmor;
						engine.applyDamage(beam.getDamageTarget(), loc, armorDam, DamageType.HIGH_EXPLOSIVE, 0f, true, false, beam.getSource(), true);
					}
				}
			}
		}
//			Global.getSoundPlayer().playLoop("system_emp_emitter_loop", 
//											 beam.getDamageTarget(), 1.5f, beam.getBrightness() * 0.5f,
//											 beam.getTo(), new Vector2f());
	}
}
