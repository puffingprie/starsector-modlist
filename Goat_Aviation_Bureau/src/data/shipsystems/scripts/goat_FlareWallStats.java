package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class goat_FlareWallStats extends BaseShipSystemScript {

	public static final float ROF_BONUS = 0f;
	public static final float FLUX_REDUCTION = 0f;

	@Override
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		float mult = 1f + ROF_BONUS * effectLevel;
		stats.getMissileRoFMult().modifyMult(id, mult);
		stats.getMissileWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_REDUCTION * 0.01f));
	}

	@Override
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getBallisticRoFMult().unmodify(id);
		stats.getBallisticWeaponFluxCostMod().unmodify(id);
	}
}