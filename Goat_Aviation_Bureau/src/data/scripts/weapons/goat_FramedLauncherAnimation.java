package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;

public class goat_FramedLauncherAnimation implements EveryFrameWeaponEffectPlugin {

	public static final float MAX_ANIMATION_TIME = 1.5f;

	private float lastChargeLevel = 0f;
	private boolean isCoolingDown = false;

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
		ShipAPI ship = weapon.getShip();
		if (engine.isPaused() || ship == null || !ship.isAlive()) {
			return;
		}

		if (weapon.getAnimation().getAlphaMult() <= 0f) return;

		float chargeLevel = weapon.getChargeLevel();
		float frameLevel = Math.min(chargeLevel * 1.25f, 1f);
		if (isCoolingDown && weapon.getCooldown() > MAX_ANIMATION_TIME) {
			float cooled = weapon.getCooldown() - weapon.getCooldownRemaining();
			frameLevel = Math.max(1f - cooled / MAX_ANIMATION_TIME, 0f);
		}

		float maxFrame = weapon.getAnimation().getNumFrames() - 1;
		weapon.getAnimation().setFrame((int)(maxFrame * frameLevel));

		isCoolingDown = chargeLevel < lastChargeLevel;
		lastChargeLevel = chargeLevel;
	}
}