package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipVariantAPI;

import java.util.Collection;

public class goat_MissileSwitching extends BaseHullMod {

	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

		ShipVariantAPI variant = stats.getVariant();
		Collection<String> hullmods = variant.getHullMods();
		if (!hullmods.contains("goat_missile_switching_h") && !hullmods.contains("goat_missile_switching_k")) {

			if (hullmods.contains("goat_missile_switching_mark")) {
				variant.addMod("goat_missile_switching_k");
				variant.removeMod("goat_missile_switching_mark");
			} else {
				variant.addMod("goat_missile_switching_h");
				variant.addMod("goat_missile_switching_mark");
			}
		}

	}
}