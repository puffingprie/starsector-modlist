package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import data.scripts.util.goat_Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class goat_PointDefense extends BaseHullMod implements AdvanceableListener {

	public static final String id = "goat_pointdefense";

	private static Map mag = new HashMap();

	static {
		mag.put(HullSize.FIGHTER, 0f);
		mag.put(HullSize.FRIGATE, 10f);
		mag.put(HullSize.DESTROYER, 20f);
		mag.put(HullSize.CRUISER, 30f);
		mag.put(HullSize.CAPITAL_SHIP, 40f);
	}

	public static float TIEM = 50f;
	public static float REPAIR_BONUS = 50f;
	public static float DEBUFF = 200f;

	public static float SMOD_MANEUVER_PENALTY1 = 140;
	public static float SMOD_MANEUVER_PENALTY2 = 420;
	public static float SMOD_MANEUVER_PENALTY3 = 1260;
	public static float SMOD_P = 1;
	public static float SMOD_P1 = 1.16f;

	public static final int BAY_REQUIRED = 5;
	public static final int WING_NUM_REQUIRED = 2;


	//-------------------//
	//以下是listener使用的变量
	public ShipAPI ship;
	public float sModFactor = SMOD_P;
	public Map<WeaponAPI, WeaponData> dataMap = new HashMap<>();

	private float elapsed=0; // 逝去时间
	private int lastDisabledWeaponCount; // 上一帧停机的武器总数
	private boolean enabled = true; // 是否运行

	public String getDescriptionParam(int index, HullSize hullSize) {

		if (index == 0) return "" + (int)SMOD_MANEUVER_PENALTY1;
		if (index == 1) return "" + (int)SMOD_MANEUVER_PENALTY2;
		if (index == 2) return "" + (int)SMOD_MANEUVER_PENALTY3;

		if (index == 3) return "" + (int)DEBUFF + "%";

		if (index == 4) return "" + (int)TIEM;

		return null;
	}



	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getMaxRecoilMult().modifyMult(id, 1f + (0.01f * DEBUFF));
		stats.getRecoilPerShotMult().modifyMult(id, 1f + (0.01f * DEBUFF));
		stats.getRecoilDecayMult().modifyMult(id, 1f + (0.01f * DEBUFF));
	}

	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

		if(!ship.hasListenerOfClass(this.getClass())){
			goat_PointDefense c = new goat_PointDefense();
			//给ship赋值激活listener运行
			c.ship = ship;
			c.sModFactor = isSMod(ship)?SMOD_P1:SMOD_P;
			ship.addListener(c);
		}
	}

	@Override
	public String getSModDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int)((SMOD_P1 - 1) * 100) + "%";
		return null;
	}

	//@Override
	//public boolean isSModEffectAPenalty() {
	//    return true;
	//}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getHullSpec().getHullId().startsWith("goat_");
	}

	@Override
	public String getUnapplicableReason(ShipAPI ship) {
		return "Can only be installed on Goathead Aviation ships";
	}


	//-----------------//
	//以下是listener的方法

	@Override
	public void advance(float amount) {
		CombatEngineAPI engine = Global.getCombatEngine();

		if (!enabled) return;
		if (ship == null) return;

		elapsed += amount;
		if (elapsed > TIEM) {
			// 超过20秒
			enabled = false;
			return;
		}

		int curDisabledWeaponCount = 0;//这一帧停机的武器总数

		for(WeaponAPI w : ship.getAllWeapons()){
			//装饰武器，安装在装饰武器槽位的武器不参与计算
			if(w.isDecorative() || w.getSlot().isDecorative()) continue;
			//隐藏槽位里的武器不参与计算
			if(w.getSlot().isHidden()) continue;
			if(!dataMap.containsKey(w)){
				dataMap.put(w,new WeaponData());
			}else {
				int lastFrameStatus = dataMap.get(w).status;

				int nextFrameStatus = 0;
				if(!w.isDisabled() && !w.isPermanentlyDisabled()){
					nextFrameStatus = 1;
				}

				if(lastFrameStatus == 0 && nextFrameStatus == 1){
					onOnlineEffect(w);
				}
				if(lastFrameStatus == 1 && nextFrameStatus == 0){
					onOfflineEffect(w);
				}
				dataMap.get(w).status = nextFrameStatus;
			}
			if(w.isDisabled())
				curDisabledWeaponCount++; // 记录
		}
		// 如果现在停机的武器数量小于上一帧武器停机的数量，那说明有武器被修复了
		if (curDisabledWeaponCount < lastDisabledWeaponCount) {
			elapsed = 0f;// 重置逝去时间
		}
		lastDisabledWeaponCount = curDisabledWeaponCount;

		if (ship == engine.getPlayerShip()) {
			engine.maintainStatusForPlayerShip(this, "graphics/icons/hullsys/recall_device.png", Global.getSettings().getHullModSpec(id).getDisplayName(), "寄存器停机倒数:" + (int)(TIEM - elapsed), false);
		}

	}

	void onOnlineEffect(WeaponAPI w){

		float hullDamaged = ship.getMaxHitpoints() - ship.getHitpoints();
		float toHeal = SMOD_MANEUVER_PENALTY1 * sModFactor ;
		if(w.getSize() == WeaponAPI.WeaponSize.MEDIUM) toHeal = SMOD_MANEUVER_PENALTY2 * sModFactor ;
		else if(w.getSize() == WeaponAPI.WeaponSize.LARGE) toHeal = SMOD_MANEUVER_PENALTY3 * sModFactor ;
		toHeal = MathUtils.clamp(toHeal,0f,hullDamaged);
		if(toHeal > 10f){
			Global.getCombatEngine().addFloatingText(w.getLocation(),
					String.format("HP + %.0f",toHeal),
					20f,Color.green,ship,1f,1f);

		}

		ship.setHitpoints(ship.getHitpoints() + toHeal);
	}

	void onOfflineEffect(WeaponAPI w){
		//todo
	}

	//-----------------//
	//数据类
	public static class WeaponData{
		/**
		 * 0 = 下线
		 * 1 = 正常
		 */
		public int status = 1;

		//todo
	}

	private static class ShieldState1 {

		boolean valid = true;
		int wingTotal = 0;

		public ShieldState1(ShipAPI ship) {
			if (ship.getVariant().getWings().size() < BAY_REQUIRED) valid = false;

			for (int i = 0; i < ship.getVariant().getWings().size(); i++) {
				FighterWingSpecAPI spec = ship.getVariant().getWing(i);
				wingTotal += spec.getNumFighters();

				if (spec.getNumFighters() < WING_NUM_REQUIRED) valid = false;
			}
		}
	}
}
