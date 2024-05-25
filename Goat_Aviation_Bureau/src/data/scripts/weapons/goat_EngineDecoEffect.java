package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class goat_EngineDecoEffect implements EveryFrameWeaponEffectPlugin {

	public static final String ID = "goat_EngineDecoEffect";

	private boolean init = false;
	private float move = 8f;
	private float level = 0f;
	private float timer = 0f;

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

		if (!init) {
			if (weapon.getId().contentEquals("goat_engine_longer1")) move = 12f;
			init = true;
		}

		ShipAPI ship = weapon.getShip();
		if (ship != null) {
			if (ship.getShield() != null && ship.getShield().isOn()) {
				level = Math.min(level + amount, 1f);
			} else {
				level = Math.max(0f, level - amount);
			}
			weapon.getSprite().setCenterY(weapon.getSprite().getHeight() / 2 - move * level);
			if (level >= 1f) {
				//smoke here
				if (timer < 0.05f) {
					timer += amount;
				} else {
					Vector2f loc = new Vector2f(weapon.getLocation().x + (float)(Math.cos(weapon.getCurrAngle() / 180 * Math.PI) * move), weapon.getLocation().y + (float)(Math.sin(weapon.getCurrAngle() / 180 * Math.PI) * move));
					engine.addSmokeParticle(loc, new Vector2f((Misc.random.nextFloat() - 0.5f) * 2f * 20f, (Misc.random.nextFloat() - 0.5f) * 2f * 20f), 15f, 0.01f, 1f, new Color(255, 255, 255, 25));
					timer = 0f;
				}

			}
		}
	}
}