package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.util.HashMap;
import java.util.Map;

public class goat_LesspressureDriftStats extends BaseShipSystemScript {

	private static final Map<HullSize, Float> mag = new HashMap<>();
	static {
		mag.put(HullSize.FIGHTER, 0.33F);
		mag.put(HullSize.FRIGATE, 0.33F);
		mag.put(HullSize.DESTROYER, 0.33F);
		mag.put(HullSize.CRUISER, 0.5F);
		mag.put(HullSize.CAPITAL_SHIP, 0.5F);
	}

	@Override
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		float mult = mag.get(HullSize.CRUISER);
		if (stats.getVariant() != null) {
			mult = mag.get(stats.getVariant().getHullSize());
		}

		if (state == State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
			stats.getMaxTurnRate().unmodify(id);
		} else {
			stats.getMaxSpeed().modifyFlat(id, 75f);
			stats.getAcceleration().modifyPercent(id, 120f * effectLevel);
			stats.getDeceleration().modifyPercent(id, 0.7f * effectLevel);
			stats.getTurnAcceleration().modifyFlat(id, 10f * effectLevel);
			stats.getTurnAcceleration().modifyPercent(id, 40f * effectLevel);
			stats.getMaxTurnRate().modifyFlat(id, 25f);
			stats.getMaxTurnRate().modifyPercent(id, 50f);
		}
	}

	@Override
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMaxSpeed().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getDeceleration().unmodify(id);
	}

	@Override
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) return new StatusData("Slightly improved maneuverability", false);
		if (index == 1) return new StatusData("top speed +75", false);
		return null;
	}
}