package data.scripts.shipsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import data.scripts.plugins.bt_temporaryHPPlugin;
import org.lazywizard.lazylib.MathUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lazywizard.lazylib.combat.AIUtils.getNearbyAllies;

public class bt_temporaryHP_unique extends BaseShipSystemScript {

    boolean doOnce = true;
    public static final float maxRange = 600f;
    public static final int maxTargets = 7;

    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        if (doOnce) {
            ShipAPI ship = (ShipAPI) stats.getEntity();
            for (ShipAPI target : getTargets(ship)) {
                Global.getCombatEngine().spawnEmpArcVisual(ship.getLocation(), ship, target.getLocation(), target, 10, new Color(243, 142, 13, 255), new Color(245, 89, 22, 255));
                if (target.hasListenerOfClass(bt_temporaryHPPlugin.class)) {
                    target.getListeners(bt_temporaryHPPlugin.class).get(0).reapllyBuff();
                } else {
                    target.addListener(new bt_temporaryHPPlugin(target));
                }
            }
            doOnce = false;
        }
    }

    @Override
    public void unapply(MutableShipStatsAPI stats, String id) {
        doOnce = true;
    }

    public List<ShipAPI> getTargets(ShipAPI ship) {
        List<ShipAPI> targets = new ArrayList<>();
        for (ShipAPI target : getNearbyAllies(ship, maxRange)) {
            if (target.isFighter()) continue;
            if (targets.size() < maxTargets) {
                targets.add(target);
            } else {
                for (ShipAPI toCompare : targets) {
                    if (MathUtils.getDistanceSquared(ship, toCompare) > MathUtils.getDistanceSquared(ship, target)) {
                        targets.remove(toCompare);
                        targets.add(target);
                        break;
                    }
                }
            }
        }
        return targets;
    }
}
