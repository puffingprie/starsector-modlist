package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.input.Keyboard;

import java.awt.*;

import static Utilities.mhmods_eneableSmod.getEnable;

public class MHMods_ParticleAccelerator extends mhmods_baseSHmod {

	final float SpeedBonus = 30f,
	speedBonusFlatSmod = 100f;
	{
		id = "mhmods_particleaccelerator";
	}


	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getProjectileSpeedMult().modifyPercent(id , SpeedBonus);
//		if (stats.getVariant().getSMods().contains(this.id) && getEnable())
//			stats.getProjectileSpeedMult().modifyFlat(id, 1);
	}

	public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return Math.round(SpeedBonus) + "%";
        return null;
    }

	@Override
	public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
		return true;
	}

	@Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
//		if (!getEnable()) return;
//		if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1")) && !ship.getVariant().getSMods().contains(id)){
//			tooltip.addPara("Hold F1 to show S-mod effect info", Misc.getGrayColor(),10);
//			return;
//		}
//		Color lableColor = Misc.getTextColor();
//		Color s = Misc.getStoryOptionColor();
//		Color h = Misc.getHighlightColor();
//		if (!ship.getVariant().getSMods().contains(id)){
//			tooltip.addSectionHeading("Effect if S-modded", Alignment.MID, pad);
//			lableColor = Misc.getGrayColor();
//			h = Misc.getGrayColor();
//		}
//		HullModSpecAPI hullmod = Global.getSettings().getHullModSpec(id);
//		String text = hullmod.getDescriptionFormat();
//		text = text.substring(0, text.length() - 1);
//		LabelAPI label = tooltip.addPara(text + " and by flat %s", pad, lableColor, lableColor,
//				Math.round(SpeedBonus) + "%",
//				Math.round(speedBonusFlatSmod) + "");
//
//		label.setHighlight(Math.round(SpeedBonus) + "%",
//				"flat " + Math.round(speedBonusFlatSmod) + "");
//		label.setHighlightColors(h, s);
	}
}



