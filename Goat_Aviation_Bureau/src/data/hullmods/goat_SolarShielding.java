package data.hullmods;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;

public class goat_SolarShielding extends BaseLogisticsHullMod {

	public static final float CORONA_EFFECT_REDUCTION = 0.1f;
	public static final float ENERGY_DAMAGE_REDUCTION = 0.85f;

	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getEnergyDamageTakenMult().modifyMult(id, ENERGY_DAMAGE_REDUCTION);
		stats.getEnergyShieldDamageTakenMult().modifyMult(id, ENERGY_DAMAGE_REDUCTION);

		//stats.getBeamDamageTakenMult().modifyMult(id, BEAM_DAMAGE_REDUCTION);
		stats.getDynamic().getStat(Stats.CORONA_EFFECT_MULT).modifyMult(id, CORONA_EFFECT_REDUCTION);

	}

	@Override
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + Math.round((1f - CORONA_EFFECT_REDUCTION) * 100f) + "%";
		if (index == 1) return "" + Math.round((1f - ENERGY_DAMAGE_REDUCTION) * 100f) + "%";
		return null;
	}
}