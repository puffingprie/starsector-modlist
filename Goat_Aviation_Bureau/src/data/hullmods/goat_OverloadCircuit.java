package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

import java.util.HashMap;
import java.util.Map;

public class goat_OverloadCircuit extends BaseHullMod {

	public static String id = "goat_OverloadCircuit";

	private static Map mag = new HashMap();
	public static float REPAIR_BONUS = 50f;
	public static float HEALTH_BONUS = 20f;

	public static float BONUS_TIME = 2.0f;
	public static float BONUS_LEVEL = 300f;

	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

		stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);

		boolean sMod = isSMod(stats);
		if (sMod) {
			stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);
			stats.getWeaponHealthBonus().modifyPercent(id, -HEALTH_BONUS);
		}
	}

	@Override
	public void advanceInCombat(ShipAPI ship, float amount) {

		//boolean sMod = isSMod(ship);
		//if (!sMod) return;

		CombatEngineAPI engine = Global.getCombatEngine();
		if (engine.isPaused()) return;

		if (!ship.isAlive()) return;
		if (!engine.isEntityInPlay(ship)) return;

		if (!ship.getCustomData().containsKey(id)) {
			ship.setCustomData(id, new TotalOverloadState(ship));
		}

		TotalOverloadState data = (TotalOverloadState)ship.getCustomData().get(id);
		for (WeaponAPI weapon : data.allValidWeapons.keySet()) {

			WeaponOverloadState weaponData = data.allValidWeapons.get(weapon);
			WeaponState state = weaponData.currentState;

			if (weapon.isDisabled() && state != WeaponState.DISABLED) {
				weaponData.currentState = WeaponState.DISABLED;
				continue;
			}

			if (!weapon.isDisabled() && state == WeaponState.DISABLED) {
				weaponData.currentState = WeaponState.BOOST;
				weaponData.timeLeft = BONUS_TIME;
				continue;
			}

			if (state == WeaponState.BOOST) {
				float timeLeft = weaponData.timeLeft;

				if (timeLeft <= 0f) {
					weaponData.currentState = WeaponState.NORMAL;
					continue;
				}

				if (weapon.getCooldownRemaining() > 0f) {
					float rate = 2f * timeLeft / BONUS_TIME; // if progressive is not wanted, set: rate = 1f;
					weapon.setRemainingCooldownTo(weapon.getCooldownRemaining() - amount * rate * BONUS_LEVEL * 0.01f);
				}

				weaponData.timeLeft -= amount;
				continue;
			}
		}
	}

	@Override
	public String getSModDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {

		if (index == 0) return "" + (int)HEALTH_BONUS + "%";
		return null;
	}

	@Override
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)BONUS_LEVEL + "%";
		if (index == 1) return "" + (int)BONUS_TIME;
		if (index == 2) return "" + (int)REPAIR_BONUS + "%";
		return null;
	}

	//@Override
	//public boolean isSModEffectAPenalty() {
	//    return true;
	//}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getHullSpec().getHullId().startsWith("goat_");
	}

	@Override
	public String getUnapplicableReason(ShipAPI ship) {
		return "Can only be installed on Goathead Aviation ships";
	}

	private static class TotalOverloadState {

		Map<WeaponAPI, WeaponOverloadState> allValidWeapons = new HashMap<>();

		public TotalOverloadState(ShipAPI ship) {
			for (WeaponAPI weapon : ship.getAllWeapons()) {
				if (weapon.isDecorative()) continue;

				allValidWeapons.put(weapon, new WeaponOverloadState());
			}
		}
	}

	private static class WeaponOverloadState {

		WeaponState currentState = WeaponState.NORMAL;
		float timeLeft = 0f;
	}

	private enum WeaponState {
		NORMAL, DISABLED, BOOST
	}
}
