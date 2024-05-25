package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import org.lazywizard.lazylib.MathUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class goat_OverpressureDriftStats extends BaseShipSystemScript {

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

		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI)stats.getEntity();
		} else {
			return;
		}

		ship.addAfterimage(new Color(208, 32, 205, 58), 0f, 0f, -ship.getVelocity().x *  MathUtils.getRandomNumberInRange(0.1f, 2f), -ship.getVelocity().y * MathUtils.getRandomNumberInRange(0.1f, 2f), effectLevel, 0.1f - (effectLevel * 0.2f), effectLevel * 0.2f, MathUtils.getRandomNumberInRange(0.1f, 0.6f), false, false, false);

		effectLevel = 1f;
		float mult = mag.get(HullSize.CRUISER);
		if (stats.getVariant() != null) {
			mult = mag.get(stats.getVariant().getHullSize());
		}

		if (state == State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
			stats.getMaxTurnRate().unmodify(id);
		} else {
			stats.getMaxSpeed().modifyFlat(id, 300f);
			stats.getAcceleration().modifyPercent(id, 400f * effectLevel);
			stats.getDeceleration().modifyPercent(id, 0.05f * effectLevel);
			stats.getTurnAcceleration().modifyFlat(id, 30f * effectLevel);
			stats.getTurnAcceleration().modifyPercent(id, 90f * effectLevel);
			stats.getMaxTurnRate().modifyFlat(id, 15f);
			stats.getMaxTurnRate().modifyPercent(id, 100f);
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
		if (index == 0) return new StatusData("maneuverability maximized", false);
		if (index == 1) return new StatusData("top speed +300", false);
		return null;
	}
}