package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class DroneShield extends BaseHullMod {
	
	public static final float MIN_DR_CR = 0.704f;

	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getMaxSpeed().modifyFlat(id, (Float) mag.get(hullSize) * -1f);
		stats.getMaxCombatReadiness().modifyFlat(id, MIN_DR_CR);
	}



	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) {
			return "Can maintain minimal combat readiness without the automated ship skill.";
		}
		
		return null;
	}

}
