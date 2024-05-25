package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.IntervalUtil;

public class Arx_Mod_EntropicAutoloader extends BaseHullMod {
    protected static Object STATUSKEY = new Object();
    public static String OA_DATA_KEY = "ARX_ENTROPIC_RELOAD_DATA_KEY";
    private static final float RELOAD_TIME = 60.0f;

    public static class EntropicAutoloader {
        final IntervalUtil interval = new IntervalUtil(RELOAD_TIME, RELOAD_TIME);
    }

    public void advanceInCombat(ShipAPI ship, float amount) {
        CombatEngineAPI engine = Global.getCombatEngine();
        String key = OA_DATA_KEY + "_" + ship.getId();
        if (engine.isPaused() || !ship.isAlive())
            return;
        EntropicAutoloader data = (EntropicAutoloader) engine.getCustomData().get(key);
        if (data == null) {
            data = new EntropicAutoloader();
            engine.getCustomData().put(key, data);
        }
        boolean sneed = false;
        for (WeaponAPI w : ship.getAllWeapons()) {
            if (w.getType() == WeaponAPI.WeaponType.MISSILE &&
                    w.usesAmmo() && w.getAmmo() < w.getMaxAmmo())
                sneed = true;
        }
        if (sneed &&
                !ship.getFluxTracker().isOverloaded()) {
            data.interval.advance(amount);
            int elapsed = Math.round((int) data.interval.getElapsed());
            if (data.interval.intervalElapsed()) {
                for (WeaponAPI w : ship.getAllWeapons()) {
                    if (w.getType() != WeaponAPI.WeaponType.MISSILE ||
                            ship.getFluxLevel() >= 0.5F)
                        continue;
                    int currentAmmo = w.getAmmo();
                    int maxAmmo = w.getMaxAmmo();
                    if (w.usesAmmo() && currentAmmo < maxAmmo) {
                        int numerator = (int) Math.max(1.0F, w.getSpec().getMaxAmmo() / 2.0F);
                        int reloadCount = numerator;
                        int newAmmo = currentAmmo + reloadCount;
                        if (newAmmo + numerator >= maxAmmo) {
                            reloadCount = maxAmmo - currentAmmo;
                            w.setAmmo(maxAmmo);
                        } else {
                            w.setAmmo(newAmmo);
                        }
                    }
                }
            } else if (ship == Global.getCombatEngine().getPlayerShip()) {
                if (ship.getFluxLevel() >= 0.5F)
                    return;
                if (ship.getFluxTracker().isOverloaded())
                    return;
                Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY,
                        Global.getSettings().getSpriteName("tooltip", "arx_entropic_autoloader"),
                        "Entropic Autoloader", elapsed + " / 60 sec until reload", false);
            }
        }
    }
}
