package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class blockade extends BaseHullMod {

	public static final float BLOCKADE_BOOST = 25f;
	public static final float HE_DAMAGE_REDUCTION = 0.25f;
	public static float ZERO_FLUX_LEVEL = 10f;

///Oddy Koc+1 Burn (Only 1 ship with blockade is cruiser(oddykoc))
//	private static Map kocburn = new HashMap();
//	static {
//		kocburn.put(HullSize.FRIGATE, 0f);
//		kocburn.put(HullSize.DESTROYER, 0f);
//		kocburn.put(HullSize.CRUISER, 0f);
//		kocburn.put(HullSize.CAPITAL_SHIP, 0f);
//	}


	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getZeroFluxSpeedBoost().modifyFlat(id, BLOCKADE_BOOST);
		stats.getHighExplosiveDamageTakenMult().modifyMult(id, 1f - HE_DAMAGE_REDUCTION);
		stats.getZeroFluxMinimumFluxLevel().modifyFlat(id, ZERO_FLUX_LEVEL * 0.01f);

///Oddy Koc+1 Burn (Only 1 ship with blockade is cruiser(oddykoc))
//		stats.getMaxBurnLevel().modifyFlat(id, (Float) kocburn.get(hullSize));

///Damper +1 Use and faster charge (Damper = only ship system with uses/regen on ships with blockade)
//		stats.getSystemUsesBonus().modifyFlat(id, 1f);
//		stats.getSystemRegenBonus().modifyFlat(id, 0.05f);


	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) (BLOCKADE_BOOST);
		if (index == 1) return "" + (int) (ZERO_FLUX_LEVEL) + "%";
		if (index == 2) return "" + (int) Math.round(HE_DAMAGE_REDUCTION * 100f) + "%";
		return null;
	}


}








