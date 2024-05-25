package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import data.scripts.util.goat_Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class goat_ParallelAssemblyLine extends BaseHullMod {

	public static final String id = "goat_parallel_assembly_line";
	public static final int BAY_REQUIRED = 5;
	public static final int WING_NUM_REQUIRED = 2;

	@Override
	public void advanceInCombat(ShipAPI ship, float amount) {
		CombatEngineAPI engine = Global.getCombatEngine();
		if (!engine.getCustomData().containsKey(id)) {
			engine.getCustomData().put(id, new HashMap<>());
		}

		Map<ShipAPI, ShieldState> shipsMap = (Map<ShipAPI, ShieldState>)engine.getCustomData().get(id);
		if (!engine.isEntityInPlay(ship) || !ship.isAlive()) {
			if (!ship.isAlive()) {
				shipsMap.remove(ship);
			}
			return;
		}

		if (!shipsMap.containsKey(ship)) {
			shipsMap.put(ship, new ShieldState(ship));
		} else {
			ShieldState data = shipsMap.get(ship);
			if (!data.valid) return;

			if (ship == engine.getPlayerShip()) {
				engine.maintainStatusForPlayerShip(this, "graphics/icons/hullsys/recall_device.png", Global.getSettings().getHullModSpec(id).getDisplayName(), "起飞战机数:" + data.wingsTakeOff, data.wingsTakeOff <= 0);
			}

			List<ShipAPI> currentWings = goat_Util.getFighters(ship);
			for (ShipAPI wing : currentWings) {
				if (!data.savedWingIds.contains(wing.getId())) {
					data.savedWingIds.add(wing.getId());
					data.wingsTakeOff++;
				}
			}

			if (data.wingsTakeOff >= 3 * data.wingTotal) {
				data.wingsTakeOff -= 3 * data.wingTotal;

				if (ship == engine.getPlayerShip()) {
					goat_Util.showText(ship, ship.getLocation(), "Missiles regenerated!");
				}

				for (WeaponAPI weapon : ship.getAllWeapons()) {
					if (weapon.getType() != WeaponType.MISSILE) continue;
					if (weapon.getMaxAmmo() <= 1) continue;

					if (weapon.usesAmmo() && weapon.getAmmo() < weapon.getMaxAmmo()) {
						weapon.setAmmo(weapon.getMaxAmmo());
					}
				}
			}
		}
	}

	@Override
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + BAY_REQUIRED;
		if (index == 1) return "" + WING_NUM_REQUIRED;
		return null;
	}

	private static class ShieldState {

		boolean valid = true;
		int wingTotal = 0;

		List<String> savedWingIds = new ArrayList<>();
		float wingsTakeOff = 0.0f;

		public ShieldState(ShipAPI ship) {
			if (ship.getVariant().getWings().size() < BAY_REQUIRED) valid = false;

			for (int i = 0; i < ship.getVariant().getWings().size(); i++) {
				FighterWingSpecAPI spec = ship.getVariant().getWing(i);
				wingTotal += spec.getNumFighters();

				if (spec.getNumFighters() < WING_NUM_REQUIRED) valid = false;
			}
		}
	}
}
