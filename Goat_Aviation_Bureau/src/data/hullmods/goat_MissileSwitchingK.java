package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;

public class goat_MissileSwitchingK extends BaseHullMod {

	public static final String TO_DISABLE = "all_goat_artichoke_h";

	@Override
	public void advanceInCombat(ShipAPI ship, float amount) {

		//if (ship.getTimeDeployedForCRReduction() > 5f) return;

		for (WeaponAPI weapon : ship.getAllWeapons()) {
			if (weapon.getId().startsWith(TO_DISABLE)) {
				// weapon.disable(true);

				weapon.setRemainingCooldownTo(1000f);
				ship.removeWeaponFromGroups(weapon);

				weapon.getAnimation().setAlphaMult(0f);
			}
		}
	}
}