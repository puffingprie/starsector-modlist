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
import java.util.Map;

import static Utilities.mhmods_eneableSmod.getEnable;

public class MHMods_HyperEngineUpgrade extends mhmods_baseSHmod {

    //public final float MANEUVER_BONUS = 25f;
    public final Color Engines_color = new Color(27, 238, 178, 255);
    final float fluxThreshold = 0.05f;

    {
        id = "mhmods_hyperengineupgrade";
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        if (stats.getVariant().getSMods().contains(this.id) && getEnable())
            stats.getAllowZeroFluxAtAnyLevel().modifyFlat(id, 1f);
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
    }

    public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
        if (index == 0) return Math.round(fluxThreshold * 100) + "%";
        if (index == 1) return "";
        if (index == 2) {
            if (ship == null) {
                return "zero flux boost - ship base speed/3 + 20 /n (exact number on install)";
            } else {
                float zerofluxboost = 20f + ship.getMutableStats().getZeroFluxSpeedBoost().getModifiedValue() - ship.getMutableStats().getMaxSpeed().getModifiedValue() / 3f;
                return Math.round(zerofluxboost) + "";
            }
        }
        return null;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        if (!getEnable()) return;
        if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1")) && (ship == null || !ship.getVariant().getSMods().contains(id))){
            tooltip.addPara("Hold F1 to show S-mod effect info", Misc.getGrayColor(), 10);
            return;
        }
        Color labelColor = Misc.getTextColor();
        Color h = Misc.getHighlightColor();
        if (ship == null || !ship.getVariant().getSMods().contains(id)) {
            tooltip.addSectionHeading("Effect if S-modded", Alignment.MID, pad);
            labelColor = Misc.getGrayColor();
            h = Misc.getGrayColor();
        }
        String boost = "zero flux boost - ship base speed/3 + 20 /n (exact number on install)";
        if (ship != null) {
            float zeroFluxBoost = 20f + ship.getMutableStats().getZeroFluxSpeedBoost().getModifiedValue() - ship.getMutableStats().getMaxSpeed().getModifiedValue() / 3f;
            boost = String.valueOf(Math.round(zeroFluxBoost));
        }

        HullModSpecAPI hullmod = Global.getSettings().getHullModSpec(id);
        LabelAPI label = tooltip.addPara(hullmod.getDescriptionFormat(), pad, labelColor, h,
                Math.round(fluxThreshold * 100) + "%",
                "The 0-flux speed boost is activated at any flux level, as long as the ship is not generating flux or is venting / overloaded",
                boost);

        label.setHighlight(Math.round(fluxThreshold * 100) + "%", "The 0-flux speed boost is activated at any flux level, as long as the ship is not generating flux or is venting / overloaded", boost);

        label.setHighlightColors(h, s, h);
    }

    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive()) return;

        Map<String, Object> customCombatData = Global.getCombatEngine().getCustomData();
        String id = ship.getId();
        float speedBoost;

        if (customCombatData.get("MHMods_HyperEngineUpgrade" + id) instanceof Float) {
            speedBoost = (float) customCombatData.get("MHMods_HyperEngineUpgrade" + id);
        } else {
            speedBoost = 20f + ship.getMutableStats().getZeroFluxSpeedBoost().base - ship.getMutableStats().getMaxSpeed().getModifiedValue() / 3f;
            customCombatData.put("MHMods_HyperEngineUpgrade" + id, speedBoost);
        }

        if (ship.getFluxTracker().isEngineBoostActive() && ship.getFluxLevel() <= fluxThreshold) {
            ship.getMutableStats().getZeroFluxSpeedBoost().modifyFlat(id, speedBoost);
            ship.getEngineController().fadeToOtherColor(this, Engines_color, null, 1f, 0.6f);
            ship.getEngineController().extendFlame(this, 0.4f, 0.4f, 0.4f);
        } else {
            ship.getMutableStats().getZeroFluxSpeedBoost().modifyFlat(id, 0);
        }
    }

    @Override
    public boolean shouldAddDescriptionToTooltip(HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
        return super.shouldAddDescriptionToTooltip(hullSize, ship, isForModSpec);
    }
}

			
			