package data.scripts.weapons;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.BeamEffectPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.util.IntervalUtil;

/**
 * Summarized all effects for oracle
 * @author Vladimir
 */

public class dpl_OracleEffect implements BeamEffectPlugin {

	private IntervalUtil fireInterval = new IntervalUtil(0.1f, 0.2f);
	private boolean wasZero = true;
	
	public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
		
		//Do the simpler check to see if anything gets hit
		if (beam.getWeapon().getShip() == null) {
            return;
        }
		
        if (beam.didDamageThisFrame()) {
        	//Always check the entity instance before calling its methods
        	//Destroys neutral ship hulks
            if (beam.getDamageTarget() instanceof CombatEntityAPI && beam.getDamageTarget().getOwner() == 100){
            ShipAPI ship = beam.getWeapon().getShip();
            engine.applyDamage(beam.getDamageTarget(), beam.getDamageTarget().getLocation(), 100000000f, DamageType.HIGH_EXPLOSIVE, 0f, true, false, ship, false);
            }
    	}
    	
        //Generates high explosive arcs for targets with heavy shield
		CombatEntityAPI target = beam.getDamageTarget();
		if (target instanceof ShipAPI && beam.getBrightness() >= 1f) {
			float dur = beam.getDamage().getDpsDuration();
			// needed because when the ship is in fast-time, dpsDuration will not be reset every frame as it should be
			if (!wasZero) dur = 0;
			wasZero = beam.getDamage().getDpsDuration() <= 0;
			fireInterval.advance(dur);
			boolean hitShield = target.getShield() != null && target.getShield().isWithinArc(beam.getTo());
			ShipAPI ship = (ShipAPI) target;
			if (fireInterval.intervalElapsed()) {
				if (hitShield) {
					Vector2f point = beam.getRayEndPrevFrame();
					float emp = 0;
					float dam = beam.getDamage().getDamage() + beam.getDamageTarget().getHitpoints()*1.25f;
					engine.spawnEmpArcPierceShields(
									   beam.getSource(), point, beam.getDamageTarget(), beam.getDamageTarget(),
									   DamageType.HIGH_EXPLOSIVE, 
									   dam, // damage
									   emp, // emp 
									   100000f, // max range 
									   "shock_repeater_emp_impact",
									   beam.getWidth() + 5f,
									   beam.getFringeColor(),
									   beam.getCoreColor()
									   );
				}
				//Does more damage to more hull points
			}
            if (!hitShield){
            float dam2 = beam.getDamage().getDamage() + beam.getDamageTarget().getHitpoints()*0.5f;
            engine.applyDamage(beam.getDamageTarget(), beam.getDamageTarget().getLocation(), dam2, DamageType.HIGH_EXPLOSIVE, 0f, true, false, ship, false);
            }
		}

	}
}
