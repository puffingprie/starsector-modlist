package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class goat_StressDispersion extends BaseHullMod {

	public static final float PROFILE_MULT = 0.7f;

	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getSensorProfile().modifyMult(id, PROFILE_MULT);

	}

	@Override
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)((1f - PROFILE_MULT) * 100f) + "%";
		return null;
	}
}