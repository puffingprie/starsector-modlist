package data.scripts.shipsystems.ai;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

public class bt_ramming_ai implements ShipSystemAIScript {
    private ShipAPI ship;
    private CombatEngineAPI engine;
    private ShipwideAIFlags flags;
    private ShipSystemAPI system;
    private final IntervalUtil tracker = new IntervalUtil(1.0f, 1.0f);

    @Override
    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.flags = flags;
        this.engine = engine;
        this.system = system;
    }

    @Override
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        if (!engine.isPaused() && !system.isActive()) {
            tracker.advance(amount);
            float turnpoint;
            if (tracker.intervalElapsed()) {
                if (ship.getShipTarget() == null) {
                    ship.setShipTarget(target);
                    return;
                }
                if (!target.isAlive() || target.isFighter() || target.isDrone()) {
                    return;
                }
                if (!MathUtils.isWithinRange(ship, target, 800.0f) || !AIUtils.canUseSystemThisFrame(ship)) {
                    return;
                }

                if (MathUtils.getShortestRotation(ship.getFacing(), VectorUtils.getAngle(ship.getLocation(), target.getLocation())) > 20f) {
                    turnpoint = MathUtils.getShortestRotation(ship.getFacing(), VectorUtils.getAngle(ship.getLocation(), target.getLocation()));
                    ship.setAngularVelocity(Math.min(ship.getMaxTurnRate(), Math.max(-ship.getMaxTurnRate(), turnpoint)));
                    return;
                }
                for (ShipAPI allyships : CombatUtils.getShipsWithinRange(ship.getLocation(), 800f)) {
                    Vector2f point = MathUtils.getNearestPointOnLine(allyships.getLocation(), ship.getLocation(), ship.getShipTarget().getLocation());
                    if (MathUtils.isPointWithinCircle(point, allyships.getLocation(), allyships.getCollisionRadius()) && allyships.getOwner() == ship.getOwner()) {
                        return;
                    }
                }
                if (flags.hasFlag(AIFlags.MANEUVER_TARGET) || flags.hasFlag(AIFlags.PURSUING) || flags.hasFlag(AIFlags.HARASS_MOVE_IN)) {
                    ship.useSystem();
                }
            }
        }
    }
}
