package data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class goat_BashModeAI implements ShipSystemAIScript {

	private ShipAPI ship;
	private ShipSystemAPI system;
	private ShipwideAIFlags flags;
	private CombatEngineAPI engine;
	private final IntervalUtil tracker = new IntervalUtil(0.2f, 0.3f);

	private WeaponAPI fakeWeapon;

	@Override
	public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
		this.ship = ship;
		this.system = system;
		this.flags = flags;
		this.engine = engine;

		this.fakeWeapon = engine.createFakeWeapon(ship, "goat_pd_bashmode");
	}

	@Override
	public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {

		if (engine.isPaused()) return;
		if (!engine.isEntityInPlay(ship)) return;
		if (!ship.isAlive()) return;

		tracker.advance(amount);
		if (!tracker.intervalElapsed()) return;
		if (!AIUtils.canUseSystemThisFrame(ship)) return;

		if (ship.getFluxLevel() > 0.8f) return;

		if (target == null) return;
		if (!target.isAlive()) return;
		if (target.getOwner() == ship.getOwner()) return;
		if (target.isFighter()) return;
		if (target.isDrone()) return;
		if (target.isPhased()) return;

		float facing = ship.getFacing();
		float angle = VectorUtils.getAngle(ship.getLocation(), target.getLocation());
		float diff = MathUtils.getShortestRotation(facing, angle);
		if (Math.abs(diff) < 30f) return;
		if (Math.abs(diff) > 150f) return;

		float range = fakeWeapon.getRange() * 0.8f;
		if (!MathUtils.isWithinRange(ship, target, range)) return;

		ship.getMouseTarget().set(target.getLocation());
		ship.useSystem();
	}
}
