package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

public class CossackJetsStats extends BaseShipSystemScript {

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
			stats.getMaxTurnRate().unmodify(id);
		} else {
			stats.getMaxSpeed().modifyFlat(id, 70f);
			stats.getAcceleration().modifyPercent(id, 150f * effectLevel);
			stats.getDeceleration().modifyPercent(id, 150f * effectLevel);
			stats.getTurnAcceleration().modifyFlat(id, 20f * effectLevel);
			stats.getTurnAcceleration().modifyPercent(id, 130f * effectLevel);
			stats.getEnergyWeaponDamageMult().modifyMult(id, 1.2f * effectLevel);
			stats.getBallisticWeaponDamageMult().modifyMult(id, 1.2f * effectLevel);
			stats.getBeamWeaponDamageMult().modifyMult(id, 1.2f * effectLevel);
			stats.getMaxTurnRate().modifyFlat(id, 15f);
			stats.getMaxTurnRate().modifyPercent(id, 110f);
		}
		
		if (stats.getEntity() instanceof ShipAPI && false) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			String key = ship.getId() + "_" + id;
			Object test = Global.getCombatEngine().getCustomData().get(key);
			if (state == State.IN) {
				if (test == null && effectLevel > 0.2f) {
					Global.getCombatEngine().getCustomData().put(key, new Object());
					ship.getEngineController().getExtendLengthFraction().advance(1f);
					for (ShipEngineAPI engine : ship.getEngineController().getShipEngines()) {
						if (engine.isSystemActivated()) {
							ship.getEngineController().setFlameLevel(engine.getEngineSlot(), 1f);
						}
					}
				}

			} else {
				Global.getCombatEngine().getCustomData().remove(key);
			}
		}
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMaxSpeed().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getDeceleration().unmodify(id);
		stats.getEnergyWeaponDamageMult().unmodify(id);
		stats.getBallisticWeaponDamageMult().unmodify(id);
		stats.getBeamWeaponDamageMult().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("improved maneuverability", false);
		} else if (index == 1) {
			return new StatusData("+70 top speed", false);
		} else if (index ==2){
			return new StatusData("20% increased non-missile weapon damage",false);
		}
		return null;
	}
}
