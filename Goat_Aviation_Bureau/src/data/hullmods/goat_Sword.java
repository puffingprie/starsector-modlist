package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import org.dark.shaders.util.ShaderLib;

import java.awt.*;

import static com.fs.starfarer.api.impl.campaign.ids.HullMods.CONVERTED_HANGAR;
import static com.fs.starfarer.api.impl.campaign.ids.HullMods.MISSLERACKS;

public class goat_Sword extends BaseHullMod {

	public static int CONVERTED_HANGAR_BONUS = 1;

	public static float AMMO_BONUS = 0f;
	public static float AMMO_BONUS_Description = 50f;
	public static int A = 1;

	public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getSuppliesPerMonth().modifyMult(id, 1f - SUPPLY_COST_REDUCTION * 0.01f);

		stats.getDynamic().getMod(Stats.CONVERTED_HANGAR_MOD).modifyFlat(id, CONVERTED_HANGAR_BONUS);

//		stats.getDynamic().getMod(Stats.CONVERTED_HANGAR_NO_PERFORMANCE_PENALTY).modifyFlat(id, 1f);
//		stats.getDynamic().getMod(Stats.CONVERTED_HANGAR_NO_COST_INCREASE).modifyFlat(id, 1f);


		stats.getMissileAmmoBonus().modifyPercent(id, AMMO_BONUS);




	}

	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

		if (ship.getVariant().hasHullMod(MISSLERACKS)) {
			AMMO_BONUS = 50f;
		}if (!ship.getVariant().hasHullMod(MISSLERACKS)) {
			AMMO_BONUS = 0f;
		}

		// 检测是否有 改装甲板 这个船插
		if (ship.getVariant().hasHullMod(CONVERTED_HANGAR)) {
			SpriteAPI sprite;
			String spriteCategory = "misc"; // settings中graphics下一级
			String spriteId = "goat_sword_D"; //贴图id
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

	public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
		//if (index == 0) return "" + (int) SUPPLY_COST_REDUCTION + "%";
		if (index == 0) return "" + (int) CONVERTED_HANGAR_BONUS + "";
		if (index == 1) return "" + (int) AMMO_BONUS_Description + "%";
		return null;
	}
}
