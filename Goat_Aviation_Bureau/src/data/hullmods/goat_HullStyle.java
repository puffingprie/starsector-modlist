package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;

public class goat_HullStyle extends BaseHullMod {

	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		if (ship.getShield() != null) {
			ship.getShield().setRadius(ship.getShield().getRadius(), Global.getSettings().getSpriteName("GOAT_TECH", "GOAT_shield_inner"), Global.getSettings().getSpriteName("GOAT_TECH", "GOAT_shield_outer"));
		}
	}
}