package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.PhaseCloakSystemAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.impl.combat.PhaseCloakStats;
import data.scripts.util.goat_Util;

public class goat_PhaseCloakStats extends PhaseCloakStats {

	@Override
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = (ShipAPI)stats.getEntity();
		if (ship == null) return;

		ShipSystemAPI cloak = ship.getPhaseCloak();
		if (cloak == null) return;
		if (!(cloak instanceof PhaseCloakSystemAPI)) return;

		goat_Util.phaseOn(ship, id, effectLevel, MAX_TIME_MULT, SHIP_ALPHA_MULT, stats);
		if (ship == Global.getCombatEngine().getPlayerShip()) {
			maintainStatus(ship, state, effectLevel);
		}

		if (state == State.ACTIVE || state == State.OUT || state == State.IN) {
			String id2 = id + "_2";
			float disruptionLevel = getDisruptionLevel(ship);
			float mult = getSpeedMult(ship, effectLevel);

			if (mult < 1f) {
				stats.getMaxSpeed().modifyMult(id2, mult);
			} else {
				stats.getMaxSpeed().unmodifyMult(id2);
			}
			((PhaseCloakSystemAPI)cloak).setMinCoilJitterLevel(disruptionLevel);
		}
	}

	@Override
	public void unapply(MutableShipStatsAPI stats, String id) {
		ShipAPI ship = (ShipAPI)stats.getEntity();
		if (ship == null) return;

		ShipSystemAPI cloak = ship.getPhaseCloak();
		if (cloak == null) return;
		if (!(cloak instanceof PhaseCloakSystemAPI)) return;

		goat_Util.phaseOff(ship, id, stats);
		((PhaseCloakSystemAPI)cloak).setMinCoilJitterLevel(0f);

		String id2 = id + "_2";
		stats.getMaxSpeed().unmodifyMult(id2);
	}

	@Override
	public StatusData getStatusData(int index, State state, float effectLevel) {
		return null;
	}
}