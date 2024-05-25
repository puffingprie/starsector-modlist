package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.input.Keyboard;

import java.awt.*;

import static Utilities.mhmods_eneableSmod.getEnable;

public class mhmods_reloader extends mhmods_baseSHmod {

    float
            regen = 25f,
            regenSMod = 100f,
            ammoSMod = 0.5f;

    {
        id = "mhmods_reloader";
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getEnergyAmmoRegenMult().modifyPercent(id, regen);
        stats.getBallisticAmmoRegenMult().modifyPercent(id, regen);

        if (stats.getVariant().getSMods().contains(this.id) && getEnable()) {
            stats.getEnergyAmmoRegenMult().modifyPercent(id, regenSMod);
            stats.getEnergyAmmoBonus().modifyMult(id, ammoSMod);
            stats.getBallisticAmmoRegenMult().modifyPercent(id, regenSMod);
            stats.getBallisticAmmoBonus().modifyMult(id, ammoSMod);
        }
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return Math.round(regen) + "%";
        return null;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        if (!getEnable()) return;
        if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1")) && (ship == null || !ship.getVariant().getSMods().contains(id))){
            tooltip.addPara("Hold F1 to show S-mod effect info", Misc.getGrayColor(), 10);
            return;
        }
        Color lableColor = Misc.getTextColor();
        Color s = Misc.getStoryOptionColor();
        if (ship == null || !ship.getVariant().getSMods().contains(id)) {
            tooltip.addSectionHeading("Effect if S-modded", Alignment.MID, pad);
            lableColor = Misc.getGrayColor();
        }
        HullModSpecAPI hullmod = Global.getSettings().getHullModSpec(id);
        LabelAPI label = tooltip.addPara(hullmod.getDescriptionFormat() + " Reduces ammo capacity of of energy and ballistic by %s.", pad, lableColor, lableColor, Math.round(regenSMod) + "%", Math.round(ammoSMod * 100f) + "%");
        label.setHighlight(Math.round(regenSMod) + "%", Math.round(ammoSMod * 100f) + "%");
        label.setHighlightColors(s, s);
        //tooltip.addPara(hullmod.getDescriptionFormat(), 10, Misc.getGrayColor(), Misc.getPositiveHighlightColor(), Math.round(fireRateSmod) + "%", Math.round(fluxSmod) + "%", Math.round(damageSmod) + "%");
    }
}
