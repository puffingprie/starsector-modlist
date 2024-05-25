package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;

public class sa_refined_targeting_core extends BaseHullMod {

//	public static final float BONUS = 100f;
//	
//	public String getDescriptionParam(int index, HullSize hullSize) {
//		if (index == 0) return "" + (int)BONUS + "%";
//		return null;
//	}
//	
//	
//	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
//		stats.getBallisticWeaponRangeBonus().modifyPercent(id, BONUS);
//		stats.getEnergyWeaponRangeBonus().modifyPercent(id, BONUS);
//	}


	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return true;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return null;
		//return "Incompatible with Dedicated Targeting Core";
	}
	
	public static float RANGE_BONUS = 80f;
	public static float PD_MINUS = 20f;
	public static final float AUTOFIRE_BONUS = 20f; // ditto
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getBallisticWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
		stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
		stats.getAutofireAimAccuracy().modifyFlat(id, AUTOFIRE_BONUS); // Autofire aim bonus modifier
		
		stats.getNonBeamPDWeaponRangeBonus().modifyPercent(id, -PD_MINUS);
		stats.getBeamPDWeaponRangeBonus().modifyPercent(id, -PD_MINUS);
	}
	@Override
	public String getDescriptionParam(final int index, final HullSize hullSize) {
		//Since we are going to use a much better looking tooltip, all you need to do is to put the
		//Descriptions in the hullmod.csv
		return null;
	}
	//The fancy hullmod tooltips goes here
	@Override
	public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
		final Color green = new Color(55,245,65,255);
		final Color red = new Color(255,0,0,255);
		final Color flavor = new Color(110,110,110,255);
		float pad = 10f;
		float padNeg = 0f;
		float padQuote = 6f;
		float padSig = 1f;
		tooltip.addSectionHeading("Technical Details", Alignment.MID, pad);
		tooltip.addPara("• Increased autofire accuracy: %s"
		             +"\n• Increased Range to Ballistics and Energy Weapons: %s",
					 pad, green, new String[] {
						 Misc.getRoundedValue(20.0f) + "%",
						 Misc.getRoundedValue(80.0f) + "%"
						 });
		tooltip.addPara("• Point Defense is only increased by: %s", padNeg, red, new String[] {
				Misc.getRoundedValue(20f) + "%"
		});
		tooltip.addPara("%s", padQuote, flavor, new String[] { "\"It's scary, gazing into the void, let's get it over with.\"" }).italicize();
		tooltip.addPara("%s", padSig, flavor, new String[] { "         \u2014 Overheard during routine maintenance" });
	}

}
