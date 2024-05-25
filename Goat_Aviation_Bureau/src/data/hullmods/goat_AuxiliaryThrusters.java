package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;


public class goat_AuxiliaryThrusters extends BaseHullMod {

	public static float MANEUVER_BONUS = 30;
	public static float BONUS = 30f;
	public static float SMODBONUS = 40f;
	public static float smodBONUS1 = 80f;
	public static final float HEALTH_BONUS = 80f;
	private static Map speed = new HashMap();
	private static Map speedx = new HashMap();
	public static Object KEY_SHIP = new Object();


	static {
		speed.put(ShipAPI.HullSize.FRIGATE, 10f);
		speed.put(ShipAPI.HullSize.DESTROYER, 20f);
		speed.put(ShipAPI.HullSize.CRUISER, 15f);
		speed.put(ShipAPI.HullSize.CAPITAL_SHIP, 5f);
	}

	static {
		speedx.put(ShipAPI.HullSize.FRIGATE, 25f);
		speedx.put(ShipAPI.HullSize.DESTROYER, 40f);
		speedx.put(ShipAPI.HullSize.CRUISER, 25f);
		speedx.put(ShipAPI.HullSize.CAPITAL_SHIP, 10f);
	}

	@Override
	public void advanceInCombat(ShipAPI ship, float amount) {
		String id = ship.getId();

		boolean sMod = isSMod(ship);
		if (!sMod) return;
        float x = 0;
		float y = 0;
		boolean Venting = ship.getFluxTracker().isVenting();

		while (!Venting) {
			ship.getMutableStats().getMaxSpeed().modifyFlat(id, /*(Float)speed.get(ship.getHullSize())*/0);
			break;
		}

		if (!Venting) return;

		ship.getMutableStats().getMaxSpeed().modifyFlat(id, (Float)speedx.get(ship.getHullSize()));
		ship.setJitterUnder(KEY_SHIP, new Color(10, 161, 175, 155), 15, 15, 3f);

		/*
		ship.getMutableStats().getMaxSpeed().modifyFlat(id, -smodBONUS1);
		ship.getMutableStats().getAcceleration().modifyFlat(id, -smodBONUS);
		ship.getMutableStats().getDeceleration().modifyFlat(id, -smodBONUS);
		ship.getMutableStats().getTurnAcceleration().modifyFlat(id, -smodBONUS);
		ship.getMutableStats().getMaxTurnRate().modifyFlat(id, -smodBONUS);
		*/
			}




	public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {

		stats.getAcceleration().modifyPercent(id, MANEUVER_BONUS * 0.6f);
		stats.getDeceleration().modifyPercent(id, MANEUVER_BONUS);
		stats.getTurnAcceleration().modifyPercent(id, MANEUVER_BONUS * 1.1f);
		stats.getMaxTurnRate().modifyPercent(id, MANEUVER_BONUS * 1.3f);
		stats.getMaxSpeed().modifyFlat(id, (Float)speed.get(hullSize));
		stats.getCombatEngineRepairTimeMult().modifyPercent(id, -HEALTH_BONUS);

		boolean sMod = isSMod(stats);
		if (sMod) {
			stats.getVentRateMult().modifyPercent(id, SMODBONUS);
			}
		}

	public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {

		if (index == 0) return "" + ((Float)speed.get(ShipAPI.HullSize.FRIGATE)).intValue();
		if (index == 1) return "" + ((Float)speed.get(ShipAPI.HullSize.DESTROYER)).intValue();
		if (index == 2) return "" + ((Float)speed.get(ShipAPI.HullSize.CRUISER)).intValue();
		if (index == 3) return "" + ((Float)speed.get(ShipAPI.HullSize.CAPITAL_SHIP)).intValue();

		if (index == 4) return "" + (int)BONUS + "%";

		if (index == 5) return "" + (int)HEALTH_BONUS + "%";

		return null;
	}

	@Override
	public String getSModDescriptionParam(int index, ShipAPI.HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int)SMODBONUS + "%";
		if (index == 1) return "" + (((Float)speedx.get(ShipAPI.HullSize.FRIGATE)).intValue() - ((Float)speed.get(ShipAPI.HullSize.FRIGATE)).intValue());
		if (index == 2) return "" + (((Float)speedx.get(ShipAPI.HullSize.DESTROYER)).intValue()-((Float)speed.get(ShipAPI.HullSize.DESTROYER)).intValue());
		if (index == 3) return "" + (((Float)speedx.get(ShipAPI.HullSize.CRUISER)).intValue()-((Float)speed.get(ShipAPI.HullSize.CRUISER)).intValue());
		if (index == 4) return "" + (((Float)speedx.get(ShipAPI.HullSize.CAPITAL_SHIP)).intValue()-((Float)speed.get(ShipAPI.HullSize.CAPITAL_SHIP)).intValue());
		return null;
	}

	private Color color = new Color(175, 83, 0, 255);



	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getHullSpec().getHullId().startsWith("goat_");
	}

	@Override
	public String getUnapplicableReason(ShipAPI ship) {
		return "Can only be installed on Goathead Aviation ships";
	}

}
