package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.input.Keyboard;

import java.awt.*;

import static Utilities.mhmods_eneableSmod.getEnable;

public class MHMods_EmergencyVentingSystemV2 extends mhmods_baseSHmod {

    final float maxOverload = 2f,
            ventBonusSMod = 25f;

    {
        id = "mhmods_emergencyventingsystem";
    }

    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0 || index == 1) return Math.round(maxOverload) + "";
        if (index == 2) return "";
        return null;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {

        if (!ship.isAlive()) return;

        boolean ShouldVent = false;
        boolean ventFromSystem = false;

        if (Global.getCombatEngine().getCustomData().get("MHM_EVS_ShouldVent" + ship.getId()) instanceof Boolean)
            ShouldVent = (boolean) Global.getCombatEngine().getCustomData().get("MHM_EVS_ShouldVent" + ship.getId());

        if (Global.getCombatEngine().getCustomData().get("MHM_EVS_ventFromSystem" + ship.getId()) instanceof Boolean)
            ventFromSystem = (boolean) Global.getCombatEngine().getCustomData().get("MHM_EVS_ventFromSystem" + ship.getId());

        if (ship.getFluxTracker().getOverloadTimeRemaining() >= maxOverload && ship.getMutableStats().getVentRateMult().getModifiedValue() > 0 && !ShouldVent) {
            ship.getFluxTracker().stopOverload();
            ship.getFluxTracker().beginOverloadWithTotalBaseDuration(maxOverload);
            ShouldVent = true;
        }
        if (ventFromSystem && ship.getFluxTracker().isVenting() && ship.getVariant().getSMods().contains(id) && getEnable()) {
            ship.getMutableStats().getVentRateMult().modifyPercent(id, ventBonusSMod);
            if (ship == Global.getCombatEngine().getPlayerShip()) {
                Global.getCombatEngine().maintainStatusForPlayerShip(id, "graphics/icons/hullsys/infernium_injector.png", "Emergency Venting System", "Venting speed increased by " + Math.round(ventBonusSMod) + "%", false);
            }
        } else {
            ship.getMutableStats().getVentRateMult().unmodify(id);
            ventFromSystem = false;
        }
        if (ShouldVent && !ship.getFluxTracker().isOverloadedOrVenting()) {
            ship.giveCommand(ShipCommand.VENT_FLUX, true, 1);
            ShouldVent = false;
            ventFromSystem = true;
        }

        Global.getCombatEngine().getCustomData().put("MHM_EVS_ShouldVent" + ship.getId(), ShouldVent);
        Global.getCombatEngine().getCustomData().put("MHM_EVS_ventFromSystem" + ship.getId(), ventFromSystem);
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        if (!getEnable()) return;
        if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1")) && (ship == null || !ship.getVariant().getSMods().contains(id))){
            tooltip.addPara("Hold F1 to show S-mod effect info", Misc.getGrayColor(), 10);
            return;
        }
        Color lableColor = Misc.getTextColor();
        Color h = Misc.getHighlightColor();
        if (ship == null || !ship.getVariant().getSMods().contains(id)) {
            tooltip.addSectionHeading("Effect if S-modded", Alignment.MID, pad);
            lableColor = Misc.getGrayColor();
            h = Misc.getGrayColor();
        }
        HullModSpecAPI hullmod = Global.getSettings().getHullModSpec(id);
        LabelAPI label = tooltip.addPara(hullmod.getDescriptionFormat(), pad, lableColor, h,
                Math.round(maxOverload) + "",
                Math.round(maxOverload) + "",
                "Increases venting rate by " + Math.round(ventBonusSMod) + "% if venting was triggered by this hullmod.");

        label.setHighlight(Math.round(maxOverload) + "",
                Math.round(maxOverload) + "",
                "Increases venting rate by " + Math.round(ventBonusSMod) + "% if venting was triggered by this hullmod.");

        label.setHighlightColors(h, h, s);
    }
}
