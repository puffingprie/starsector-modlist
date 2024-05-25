package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import org.dark.shaders.util.ShaderLib;

import java.awt.Color;

public class goat_StringwindDynamicSprite extends BaseHullMod {

	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		// 检测是否有 辅助燃油罐 这个船插
		if (ship.getVariant().hasHullMod(HullMods.AUXILIARY_FUEL_TANKS)) {
			SpriteAPI sprite;
			String spriteCategory = "misc"; // settings中graphics下一级
			String spriteId = "goat_stringwind_T"; //贴图id
			try {
				// 此处查找是否有这个贴图
				sprite = Global.getSettings().getSprite(spriteCategory, spriteId, false);
			} catch (RuntimeException ex) {
				// 如果没有sprite设为null
				sprite = null;
			}
			// sprite 非null的时候继续运行
			if (sprite != null) {
				// 原贴图的一些数据
				float x = ship.getSpriteAPI().getCenterX();
				float y = ship.getSpriteAPI().getCenterY();
				float alpha = ship.getSpriteAPI().getAlphaMult();
				float angle = ship.getSpriteAPI().getAngle();
				Color color = ship.getSpriteAPI().getColor();

				// 关键代码，设置舰船贴图
				ship.setSprite(spriteCategory, spriteId);
				ShaderLib.overrideShipTexture(ship, spriteId);

				// 重设新贴图的数据，照抄就完事儿了
				ship.getSpriteAPI().setCenter(x, y);
				ship.getSpriteAPI().setAlphaMult(alpha);
				ship.getSpriteAPI().setAngle(angle);
				ship.getSpriteAPI().setColor(color);
			}
		}
	}
}
