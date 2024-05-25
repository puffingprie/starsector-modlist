package data.scripts.plugins;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI;
import com.fs.starfarer.api.input.InputEventAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class goat_ShipEveryFramePlugin extends BaseEveryFrameCombatPlugin {

	public static final String PLUGIN_ID = "goat_ShipEveryFramePlugin";

	private CombatEngineAPI engine;

	@Override
	public void init(CombatEngineAPI engine) {
		this.engine = engine;
		engine.getCustomData().put(PLUGIN_ID, new LocalData());
	}

	@Override
	public void advance(float amount, List<InputEventAPI> events) {
		if (engine == null || engine.isPaused()) return;
		final LocalData localData = (LocalData)engine.getCustomData().get(PLUGIN_ID);

		final Map<ShipAPI, DazzleWarheadData> dazzleWarheadData = localData.dazzleWarheadData;

		//ship operate
		List<ShipAPI> shipRemoveList = new ArrayList<>();
		for (Map.Entry<ShipAPI, DazzleWarheadData> entry : dazzleWarheadData.entrySet()) {
			ShipAPI ship = entry.getKey();
			DazzleWarheadData value = entry.getValue();
			if (!engine.isEntityInPlay(ship) || !ship.isAlive()) {
				shipRemoveList.add(ship);
				continue;
			}

			//do something here

			if (value.hit >= 3) {
				for (ShipEngineControllerAPI.ShipEngineAPI shipEngine : ship.getEngineController().getShipEngines()) {
					if (!shipEngine.isDisabled()) {
						shipEngine.disable(false);
					}
				}
				value.time = 0f;
				value.hit = 0;
				shipRemoveList.add(ship);
			} else {
				value.time += amount;
				if (value.time >= 2f) {
					value.time = 0f;
					value.hit = 0;
					shipRemoveList.add(ship);
				}
			}
		}
		if (!shipRemoveList.isEmpty()) {
			for (ShipAPI ship : shipRemoveList) {
				dazzleWarheadData.remove(ship);
			}
			shipRemoveList.clear();
		}
	}

	public static final class LocalData {

		public Map<ShipAPI, DazzleWarheadData> dazzleWarheadData = new HashMap<>();
	}

	public static final class DazzleWarheadData {

		public float time;
		public int hit;

		public DazzleWarheadData() {
			time = 0f;
			hit = 1;
		}
	}

}