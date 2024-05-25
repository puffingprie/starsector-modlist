package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

public class goat_TimeMix extends BaseHullMod {

	public static float RANGE_BONUS = 130f;
	public static float PD_MINUS = 90f;

	public static float AUTOFIRE_AIM = 2.0f;

	@Override
	public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
		if (index == 0) return "" + Math.round(RANGE_BONUS) + "%";
		if (index == 1) return "" + Math.round(PD_MINUS) + "%";
		//if (index == 0) return "" + (int)RANGE_THRESHOLD;
		//if (index == 1) return "" + (int)((RANGE_MULT - 1f) * 100f);
		//if (index == 1) return "" + new Float(VISION_BONUS).intValue();
		return null;
	}

	@Override
	public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {

		float rangeIncrease = RANGE_BONUS - 100f;
		float pdDecrease = PD_MINUS - 100f;

		stats.getBallisticWeaponRangeBonus().modifyPercent(id, rangeIncrease);
		stats.getEnergyWeaponRangeBonus().modifyPercent(id, rangeIncrease);

		stats.getAutofireAimAccuracy().modifyFlat(id, AUTOFIRE_AIM);

	}
}
