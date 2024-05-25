package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class CHM_eridaniTM extends BaseHullMod {

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

			stats.getDynamic().getMod(Stats.SMALL_BALLISTIC_MOD).modifyFlat(id, -1f);
			stats.getDynamic().getMod(Stats.MEDIUM_BALLISTIC_MOD).modifyFlat(id, -2f);
			stats.getDynamic().getMod(Stats.LARGE_BALLISTIC_MOD).modifyFlat(id, -4f);

			stats.getDynamic().getMod(Stats.SMALL_MISSILE_MOD).modifyFlat(id, -1f);
			stats.getDynamic().getMod(Stats.MEDIUM_MISSILE_MOD).modifyFlat(id, -2f);
			stats.getDynamic().getMod(Stats.LARGE_MISSILE_MOD).modifyFlat(id, -4f);

			stats.getDynamic().getMod(Stats.SMALL_ENERGY_MOD).modifyFlat(id, -1f);
			stats.getDynamic().getMod(Stats.MEDIUM_ENERGY_MOD).modifyFlat(id, -2f);
			stats.getDynamic().getMod(Stats.LARGE_ENERGY_MOD).modifyFlat(id, -4f);

			stats.getCargoMod().modifyMult(id, 0.85f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "1/2/4";
		if (index == 1) return "15%";
		return null;
	}

	@Override
	public boolean affectsOPCosts() {
		return true;
	}

}
