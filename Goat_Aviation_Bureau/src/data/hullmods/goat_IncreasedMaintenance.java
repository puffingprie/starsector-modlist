package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.hullmods.CompromisedStructure;

public class goat_IncreasedMaintenance extends BaseHullMod {

	public static float SUPPLY_USE_MULT = 2f;
	public static float MAX_CR_PENALTY = 0.05f;

	@Override
	public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
		float effect = stats.getDynamic().getValue("dmod_effect_mult");
		stats.getSuppliesPerMonth().modifyPercent(id, (float)Math.round((SUPPLY_USE_MULT - 1f) * effect * 100f));
		stats.getMaxCombatReadiness().modifyFlat(id, (float)(-Math.round(MAX_CR_PENALTY * effect * 100f)) * 0.01f, "船舱维护");
		CompromisedStructure.modifyCost(hullSize, stats, id);

	}

	@Override
	public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
		float effect = 1f;

		if (index == 0) return (int)((1f + (SUPPLY_USE_MULT - 1f) * effect - 1f) * 100f) + "%";
		if (index == 1) return Math.round(MAX_CR_PENALTY * 100f * effect) + "%";
		return index >= 2 ? CompromisedStructure.getCostDescParam(index, 2) : null;
	}
}
