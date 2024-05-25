package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class goat_AmmoFeedStats extends BaseShipSystemScript {

	public static final float ROF_BONUS = 3.5f;
	public static final float FLUX_REDUCTION = 10f;
	public static final float DAM = 1.5f;

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		float mult = 1f + ROF_BONUS * effectLevel;
		float mult1 = 1f + DAM * effectLevel;
		stats.getBallisticRoFMult().modifyMult(id, mult);
		stats.getBallisticWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_REDUCTION * 0.01f));
		stats.getEnergyRoFMult().modifyMult(id, mult);
		stats.getEnergyWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_REDUCTION * 0.01f));
		stats.getHullDamageTakenMult().modifyMult(id, 1f - (1f - mult1) * effectLevel);
		stats.getArmorDamageTakenMult().modifyMult(id, 1f - (1f - mult1) * effectLevel);

	}

	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getBallisticRoFMult().unmodify(id);
		stats.getBallisticWeaponFluxCostMod().unmodify(id);
		stats.getEnergyRoFMult().unmodify(id);
		stats.getEnergyWeaponFluxCostMod().unmodify(id);
		stats.getHullDamageTakenMult().unmodify(id);
		stats.getArmorDamageTakenMult().unmodify(id);
	}

	public StatusData getStatusData(int index, State state, float effectLevel) {
		float mult = 1f + ROF_BONUS * effectLevel;
		float mult1 = 1f + DAM * effectLevel;
		float bonusPercent = (int)((mult - 1f) * 100f);
		float damPercent = (int)((mult1 - 1f) * 100f);
		if (index == 0) {
			return new StatusData("Fire rate +" + (int)bonusPercent + "%", false);
		}
		if (index == 1) {
			return new StatusData("weapon flux generation -" + (int)FLUX_REDUCTION + "%", false);
		}
		if (index == 2) {
			return new StatusData("Hull/armor damage taken +" + (int)damPercent + "%" , false);
		}
		return null;
	}
}
