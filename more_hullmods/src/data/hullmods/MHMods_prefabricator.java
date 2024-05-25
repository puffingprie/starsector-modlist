package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static Utilities.MHMods_utilities.floatToString;
import static Utilities.mhmods_eneableSmod.getEnable;

public class MHMods_prefabricator extends mhmods_baseSHmod {

    private final Map<HullSize, Integer> fabricationTime = new HashMap<>();

    float ammoregenSmod = 0.35f;

    {
        fabricationTime.put(HullSize.FIGHTER, 155);
        fabricationTime.put(HullSize.FRIGATE, 155);
        fabricationTime.put(HullSize.DESTROYER, 255);
        fabricationTime.put(HullSize.CRUISER, 330);
        fabricationTime.put(HullSize.CAPITAL_SHIP, 510);

        id = "MHMods_prefabricator";
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return String.valueOf(fabricationTime.get(HullSize.FRIGATE));
        if (index == 1) return String.valueOf(fabricationTime.get(HullSize.DESTROYER));
        if (index == 2) return String.valueOf(fabricationTime.get(HullSize.CRUISER));
        if (index == 3) return String.valueOf(fabricationTime.get(HullSize.CAPITAL_SHIP));
        if (index == 4) return "removes";
        return null;
    }

    boolean makeCurrentRed = false;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive()) return;
        if (ship.getFullTimeDeployed() >= 0.5f) return;

        Map<String, Object> customCombatData = Global.getCombatEngine().getCustomData();
        String id = ship.getId();

        if (customCombatData.get("MHMods_prefabricator" + id) instanceof Boolean) return;

        for (WeaponAPI w : ship.getAllWeapons()) {
            float reloadRate = w.getAmmoPerSecond();
            if (w.getType() == WeaponType.MISSILE && !w.getSlot().isBuiltIn() && w.usesAmmo() && reloadRate > 0) {
                float ammo = w.getSpec().getAmmoPerSecond() * fabricationTime.get(ship.getHullSize()) * ship.getMutableStats().getMissileAmmoRegenMult().getModifiedValue();
                w.setMaxAmmo(w.getMaxAmmo() + Math.round(ammo));
                w.getAmmoTracker().setAmmo(w.getAmmo() + Math.round(ammo));

                if (ship.getVariant().getSMods().contains(this.id) && getEnable()) w.getAmmoTracker().setAmmoPerSecond(w.getSpec().getAmmoPerSecond() * ammoregenSmod);
                    else w.getAmmoTracker().setAmmoPerSecond(0);
            }
        }

        customCombatData.put("MHMods_prefabricator" + id, true);
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        makeCurrentRed = true;
        if (ship.getOriginalOwner() == -1) {
            for (WeaponAPI w : ship.getAllWeapons()) {
                float reloadRate = w.getAmmoPerSecond();
                if (w.getType() == WeaponType.MISSILE && !w.getSlot().isBuiltIn() && w.usesAmmo() && reloadRate > 0) {
                    makeCurrentRed = false;
                }
            }
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        if (getEnable()) {
            if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1")) && (ship == null || !ship.getVariant().getSMods().contains(id))) {
                tooltip.addPara("Hold F1 to show S-mod effect info", Misc.getGrayColor(), 10);
            } else {
                Color lableColor = Misc.getTextColor();
                Color h = Misc.getHighlightColor();
                if (ship == null || !ship.getVariant().getSMods().contains(id)) {
                    tooltip.addSectionHeading("Effect if S-modded", Alignment.MID, pad);
                    lableColor = Misc.getGrayColor();
                    h = Misc.getGrayColor();
                }
                HullModSpecAPI hullmod = Global.getSettings().getHullModSpec(id);
                String text = hullmod.getDescriptionFormat();
                text = text.replace("their ammo recharge", "their ammo recharge rate by %s");
                LabelAPI label = tooltip.addPara(text, pad, lableColor, h,
                        "" + fabricationTime.get(HullSize.FRIGATE),
                        "" + fabricationTime.get(HullSize.DESTROYER),
                        "" + fabricationTime.get(HullSize.CRUISER),
                        "" + fabricationTime.get(HullSize.CAPITAL_SHIP),
                        "reduces ",
                        floatToString((1f - ammoregenSmod) * 100) + "%");


                label.setHighlight(
                        "" + fabricationTime.get(HullSize.FRIGATE),
                        "" + fabricationTime.get(HullSize.DESTROYER),
                        "" + fabricationTime.get(HullSize.CRUISER),
                        "" + fabricationTime.get(HullSize.CAPITAL_SHIP),
                        "reduces",
                        floatToString((1f - ammoregenSmod) * 100) + "%");

                label.setHighlightColors(h, h, h, h, s, s);
            }
        }
        if (ship != null) {
            Map<WeaponAPI, Integer> weapons = new HashMap<>();

            Map<WeaponAPI, Integer> weaponsMed = new HashMap<>();
            Map<WeaponAPI, Integer> weaponsSmall = new HashMap<>();
            Map<WeaponAPI, Integer> weaponsSorted = new HashMap<>();
            tooltip.addSectionHeading("Effects", Alignment.MID, pad);
            for (WeaponAPI w : ship.getAllWeapons()) {
                float reloadRate = w.getAmmoPerSecond();
                if (w.getType() == WeaponType.MISSILE && !w.getSlot().isBuiltIn() && w.usesAmmo() && reloadRate > 0) {
                    boolean had = false;
                    if (w.getSize().equals(WeaponAPI.WeaponSize.SMALL)) {
                        weapons = weaponsSmall;
                    } else if (w.getSize().equals(WeaponAPI.WeaponSize.MEDIUM)) {
                        weapons = weaponsMed;
                    } else {
                        weapons = weaponsSorted;
                    }
                    for (Map.Entry<WeaponAPI, Integer> entry : weapons.entrySet()) {
                        if (entry.getKey().getId().equals(w.getId())) {
                            weapons.put(entry.getKey(), entry.getValue() + 1);
                            had = true;
                            break;
                        }
                    }
                    if (!had) {
                        weapons.put(w, 1);
                    }
                }
            }
            if (!weapons.isEmpty()) {
                tooltip.addPara("Affected weapons:", pad);
                tooltip.setBulletedListMode("  • ");
                addMap(tooltip, weaponsSorted, ship);
                addMap(tooltip, weaponsMed, ship);
                addMap(tooltip, weaponsSmall, ship);
                tooltip.setBulletedListMode(null);
            } else {
                tooltip.addPara("HAS NO EFFECT ON CURRENT SHIP", Color.RED, pad);
            }
        }
    }

    public void addMap(TooltipMakerAPI tooltip, Map<WeaponAPI, Integer> weapons, ShipAPI ship) {
        float padS = 3f;
        for (Map.Entry<WeaponAPI, Integer> entry : weapons.entrySet()) {
            WeaponAPI w = entry.getKey();
            Color h = Misc.getHighlightColor();
            Color mountColor = Misc.MOUNT_MISSILE;
            if (w.getSpec().getMountType().equals(WeaponType.COMPOSITE)) {
                mountColor = Misc.MOUNT_COMPOSITE;
            } else if (w.getSpec().getMountType().equals(WeaponType.SYNERGY)) {
                mountColor = Misc.MOUNT_SYNERGY;
            }

            float ammo = w.getSpec().getAmmoPerSecond() * fabricationTime.get(ship.getHullSize()) * ship.getMutableStats().getMissileAmmoRegenMult().getModifiedValue();

            LabelAPI label = tooltip.addPara(entry.getValue() + "x " + w.getDisplayName() + " : +" + Math.round(ammo) + " ammo", padS);
            label.setHighlight(entry.getValue() + "x", w.getDisplayName() + "", "+" + Math.round(ammo));
            label.setHighlightColors(h, mountColor, h);
        }
    }

    @Override
    public Color getNameColor() {
        if (makeCurrentRed) return Color.RED;
        return super.getNameColor();
    }
}
