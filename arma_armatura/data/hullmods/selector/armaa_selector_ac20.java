package data.hullmods.selector;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.ui.Alignment;
import java.awt.Color;

	public class armaa_selector_ac20 extends BaseHullMod 
	{
		private static final float DISSIPATION_MULT = 1.05f;
		private static final float ARMOR_BONUS = 50f;
	
		@Override
		public int getDisplaySortOrder() 
		{
			return 2000;
		}

		@Override
		public int getDisplayCategoryIndex() 
		{
			return 3;
		}

		public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) 
		{
			stats.getArmorBonus().modifyFlat(id, (Float) ARMOR_BONUS);
			stats.getFluxDissipation().modifyMult(id, DISSIPATION_MULT);	
		}
	
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

	}
	
    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
 		if (index == 0) return "KO-AC20 Autocannon";
		if (index == 1) return "Remove this hullmod to cycle between cores.";
        return null;    
    }

	private final Color HL=Global.getSettings().getColor("hColor");	
	private final Color TT = Global.getSettings().getColor("buttonBgDark");
	private final Color F = Global.getSettings().getColor("textFriendColor");
	private final Color E = Global.getSettings().getColor("textEnemyColor");
	@Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
		float pad = 10f;
		float padS = 2f;
		Color[] arr ={Misc.getHighlightColor(),F};
		Color[] arrB ={Misc.getHighlightColor(),F,F};
		Color[] arr2 ={Misc.getHighlightColor(),E};
		tooltip.addSectionHeading("Details" ,Alignment.MID, 10);
		tooltip.addPara("%s " + "Armor increased by %s.", pad, arr, "-", (int) Math.round(ARMOR_BONUS) + " standard units");
		tooltip.addPara("%s " + "Flux Dissipation increased by %s.", padS, arr, "-", (int) Math.round((DISSIPATION_MULT - 1f) * 100f) + "%");	
	}


}
