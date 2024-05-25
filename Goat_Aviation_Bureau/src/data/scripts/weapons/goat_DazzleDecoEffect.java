package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.magiclib.plugins.MagicRenderPlugin;

import java.awt.Color;

public class goat_DazzleDecoEffect implements EveryFrameWeaponEffectPlugin {

	public static final String ID = "goat_DazzleDecoEffect";

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
		if (weapon != null && weapon.getShip() != null && weapon.getShip().isAlive() && !weapon.getShip().isHulk()) {
			if (!weapon.getSlot().isHidden()) {
				SpriteAPI goat_dazzle_m_r = Global.getSettings().getSprite("misc", "goat_dazzle_m_r_t");
				if (weapon.getSlot().isHardpoint()) {
					goat_dazzle_m_r = Global.getSettings().getSprite("misc", "goat_dazzle_m_r");
				}

				goat_dazzle_m_r.setColor(Color.WHITE);
				goat_dazzle_m_r.setAngle(weapon.getCurrAngle() - 90f);
				goat_dazzle_m_r.setAlphaMult((float)Math.pow(weapon.getBarrelSpriteAPI().getAlphaMult(), 2f));
				MagicRenderPlugin.addSingleframe(goat_dazzle_m_r, weapon.getLocation(), CombatEngineLayers.STATION_WEAPONS_LAYER);
			}
		}
	}
}