package data.scripts.weapons;

import com.fs.starfarer.api.AnimationAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.magiclib.plugins.MagicRenderPlugin;

import java.awt.Color;

public class goat_BreathingDecoEffect implements EveryFrameWeaponEffectPlugin {

	public static final String ID = "goat_BreathingDecoEffect";
	public static final float FRAME_TIME = 0.04f;

	private float alpha = 0f;
	private float timer = 0f;

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

		if (weapon.getAnimation().getFrameRate() > 0) {
			weapon.getAnimation().setFrameRate(0);
		}

		ShipAPI ship = weapon.getShip();
		AnimationAPI animation = weapon.getAnimation();
		if (ship.getEngineController().isAccelerating()) {
			if (animation.getFrame() < animation.getNumFrames() - 1) {
				timer += amount;
				if (timer >= FRAME_TIME) {
					timer -= FRAME_TIME;

					animation.setFrame(animation.getFrame() + 1);
				}
			} else {

				alpha = Math.min(alpha + amount, 1f);

				SpriteAPI goat_breathing_glow = Global.getSettings().getSprite("misc", "goat_breathing_glow");
				goat_breathing_glow.setColor(Color.RED);
				goat_breathing_glow.setAngle(weapon.getCurrAngle() - 90f);
				goat_breathing_glow.setAlphaMult(alpha);

				MagicRenderPlugin.addSingleframe(goat_breathing_glow, weapon.getLocation(), CombatEngineLayers.ABOVE_SHIPS_LAYER);
			}

		} else {
			if (animation.getFrame() > 0) {
				if (alpha > 0f) {
					alpha = Math.max(0f, alpha - amount);

					SpriteAPI goat_breathing_glow = Global.getSettings().getSprite("misc", "goat_breathing_glow");
					goat_breathing_glow.setColor(Color.RED);
					goat_breathing_glow.setAngle(weapon.getCurrAngle() - 90f);
					goat_breathing_glow.setAlphaMult(alpha);

					MagicRenderPlugin.addSingleframe(goat_breathing_glow, weapon.getLocation(), CombatEngineLayers.ABOVE_SHIPS_LAYER);
				} else {
					timer += amount;
					if (timer >= FRAME_TIME) {
						timer -= FRAME_TIME;

						animation.setFrame(animation.getFrame() - 1);
					}
				}
			}
		}
	}
}