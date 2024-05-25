package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class goat_AuxiliaryThrusters1 extends BaseHullMod {

	public static float MANEUVER_BONUS = -40;
	public static float BONUS = 50f;
	public static float SMODBONUS = 50f;
	public static float REPAIR_BONUS = 60f;
	public static final float HEALTH_BONUS = 60f;
	private static Map speed = new HashMap();


	public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getEngineHealthBonus().modifyPercent(id, HEALTH_BONUS);

		boolean sMod = isSMod(stats);
		if (sMod) {
			stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);
		}

	}

	public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {


		if (index == 0) return "" + (int)HEALTH_BONUS + "%";



		return null;
	}

	@Override
	public String getSModDescriptionParam(int index, ShipAPI.HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int)REPAIR_BONUS + "%";
		return null;
	}


	private Color color = new Color(255, 162, 0, 255);

	@Override
	public void advanceInCombat(ShipAPI ship, float amount) {
		//ship.getFluxTracker().setHardFlux(ship.getFluxTracker().getCurrFlux());
		//		if (ship.getEngineController().isAccelerating() ||
		//				ship.getEngineController().isAcceleratingBackwards() ||
		//				ship.getEngineController().isDecelerating() ||
		//				ship.getEngineController().isTurningLeft() ||
		//				ship.getEngineController().isTurningRight() ||
		//				ship.getEngineController().isStrafingLeft() ||
		//				ship.getEngineController().isStrafingRight()) {
		ship.getEngineController().fadeToOtherColor(this, color, null, 1f, 0.4f);
		//ship.getEngineController().extendFlame(this, 0.25f, 0.25f, 0.25f);
		//		}
	}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getHullSpec().getHullId().startsWith("goat_");
	}

	@Override
	public String getUnapplicableReason(ShipAPI ship) {
		return "仅限 羊头航务局 舰船安装";
	}

}
