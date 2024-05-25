package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class UNSC_hullconstruction extends BaseHullMod {

	public static final float HEALTH_BONUS = 50f; //100
	public static final float FLUX_RESISTANCE = 50f; //50
	//public static final float REPAIR_BONUS = 25f; //50
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getWeaponHealthBonus().modifyPercent(id, HEALTH_BONUS);
		stats.getEngineHealthBonus().modifyPercent(id, HEALTH_BONUS);
		stats.getEmpDamageTakenMult().modifyMult(id, 1f - FLUX_RESISTANCE * 0.01f);
		//stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);
		//stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HEALTH_BONUS + "%";
		if (index == 0) return "" + (int) FLUX_RESISTANCE + "%";
		//if (index == 0) return "" + (int) REPAIR_BONUS + "%";
		
		return null;
	}

}
