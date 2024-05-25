package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.hullmods.CompromisedStructure;
import data.scripts.plugins.goat_ProjEveryFramePlugin;


public class goat_StateLunar extends BaseHullMod {

	public static final float ENERGY_RANGE_BONUS = 550f;
	public static float DAMAGE_BONUS_MULT = 100f;
	public static float FLUX_REDUCTION = 200f;
	public static final float DEGRADE_INCREASE_PERCENT = 60f;
	public static final float COST_REDUCTION = 5;

	@Override
	public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getEnergyWeaponRangeBonus().modifyFlat(id, ENERGY_RANGE_BONUS);
		stats.getEnergyWeaponDamageMult().modifyPercent(id, DAMAGE_BONUS_MULT);
		stats.getEnergyWeaponFluxCostMod().modifyMult(id, 1f + FLUX_REDUCTION * 0.01f);
		stats.getCRLossPerSecondPercent().modifyPercent(id, DEGRADE_INCREASE_PERCENT);
		stats.getDynamic().getMod(Stats.SMALL_ENERGY_MOD).modifyFlat(id, COST_REDUCTION);
		stats.getDynamic().getMod(Stats.MEDIUM_ENERGY_MOD).modifyFlat(id, COST_REDUCTION);

	}

	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		goat_ProjEveryFramePlugin.setSaxState(ship);
		ship.setInvalidTransferCommandTarget(true);
	}

	@Override
	public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
		if (index == 0) return "" + (int)FLUX_REDUCTION + "%";
		if (index == 1) return "" + (int)ENERGY_RANGE_BONUS;
		if (index == 2) return "" + (int)DAMAGE_BONUS_MULT + "%";
		if (index == 3) return "" + (int)COST_REDUCTION + "";
		if (index == 4) return "" + (int)DEGRADE_INCREASE_PERCENT + "%";
		return index >= 5 ? CompromisedStructure.getCostDescParam(index, 2) : null;
	}

	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && (ship.getHullSpec().getNoCRLossTime() < 10000 || ship.getHullSpec().getCRLossPerSecond() > 0);
	}

	public boolean affectsOPCosts() {
		return true;
	}
}





