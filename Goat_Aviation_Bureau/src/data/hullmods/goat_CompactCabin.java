package data.hullmods;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;

public class goat_CompactCabin extends BaseLogisticsHullMod {

	public static float RATE_DECREASE_MODIFIER = 100f;
	public static float RATE_INCREASE_MODIFIER = 100f;

	public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_DECREASE_MULT).modifyMult(id, 1f + RATE_DECREASE_MODIFIER / 100f);
		stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyPercent(id, -RATE_INCREASE_MODIFIER);

	}

	public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
		if (index == 0) return "" + (int)RATE_DECREASE_MODIFIER + "%";
		if (index == 1) return "" + (int)RATE_INCREASE_MODIFIER + "%";
		return null;
	}

	public boolean isApplicableToShip(ShipAPI ship) {
		int baysModified = (int)ship.getMutableStats().getNumFighterBays().getModifiedValue();
		if (baysModified <= 0) return false; // only count removed bays, not added bays for this

		int bays = (int)ship.getMutableStats().getNumFighterBays().getBaseValue();
		//		if (ship != null && ship.getVariant().getHullSpec().getBuiltInWings().size() >= bays) {
		//			return false;
		//		}
		return ship != null && bays > 0;
	}

}