package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.combat.listeners.DamageListener;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import data.scripts.util.goat_Util;

public class goat_GamblingStats extends BaseShipSystemScript implements DamageListener {

	public static float WINING_RATE_PERCENT_FROM_DUEL = 60f;
	public static float SCORE_TO_SPEED_RATE_FACTOR = 0.001f;

	private ShipAPI ship = null;
	private ShipAPI target = null;
	private float totalScore = 0f;

	@Override
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

		CombatEngineAPI engine = Global.getCombatEngine();
		ship = (ShipAPI)stats.getEntity();
		if (ship == null || !engine.isEntityInPlay(ship) || !ship.isAlive()) return;

		// 谜题：如何调整合适的速度公式与限制
		float actualSpeedIncrease = totalScore * SCORE_TO_SPEED_RATE_FACTOR;
		stats.getMaxSpeed().modifyFlat(id, actualSpeedIncrease);
		if (ship == engine.getPlayerShip() && actualSpeedIncrease > 1f) {
			engine.maintainStatusForPlayerShip(this, "graphics/icons/hullsys/maneuvering_jets.png", Global.getSettings().getShipSystemSpec(ship.getSystem().getId()).getName(), "当前速度增益:" + (int)actualSpeedIncrease, false);
		}

		// 谜题：这代表什么？
		if (state == State.COOLDOWN || state == State.IDLE) {
			unapply(stats, id);
			return;
		}

		if (target == null) {
			target = ship.getShipTarget();
			if (target != null) {
				target.addListener(this);
			}
		}

		// 谜题：还缺了什么？
		if (target != null) {

		}
	}

	@Override
	public void unapply(MutableShipStatsAPI stats, String id) {

		ship = (ShipAPI)stats.getEntity();
		if (ship == null) return;

		// 谜题：少了最关键的哪一步？
		if (target != null) {
			target = null;

			float totalDamageThisDuel = damageFromOther + damageFromShip;
			boolean winThisDuel = damageFromShip >= totalDamageThisDuel * WINING_RATE_PERCENT_FROM_DUEL * 0.01f;
			if (winThisDuel) {
				totalScore += damageFromShip;
				goat_Util.showText(ship, ship.getLocation(), "Duel won!");
			} else {
				totalScore = 0f;
				goat_Util.showText(ship, ship.getLocation(), "Duel lost...");

				ship.getFluxTracker().beginOverloadWithTotalBaseDuration(6f);
			}

			damageFromOther = 0f;
			damageFromShip = 0f;
		}
	}

	@Override
	public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
		return canBeTarget(ship, ship.getShipTarget());
	}

	@Override
	public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
		ShipAPI target = ship.getShipTarget();
		if (target == null) return "无目标";
		if (!canBeTarget(ship, target)) return "目标不可行";
		return "就绪";
	}

	private boolean canBeTarget(ShipAPI ship, ShipAPI target) {
		if (ship == null || target == null) return false;
		if (!target.isAlive()) return false;
		if (target.isDrone() || target.isFighter()) return false;
		if (target.isStation() || target.isStationModule()) return false;
		if (ship.getOwner() == target.getOwner()) return false;
		return true;
	}

	// 谜题：这是什么写法？

	private float damageFromOther = 0f;
	private float damageFromShip = 0f;

	@Override
	public void reportDamageApplied(Object source, CombatEntityAPI target, ApplyDamageResultAPI result) {

		if (ship == null) return;
		if (source instanceof EmpArcEntityAPI) return;

		boolean validDamage = false;
		if (source == ship) validDamage = true;
		if (source instanceof DamagingProjectileAPI && ((DamagingProjectileAPI)source).getSource() == ship) {
			validDamage = true;
		}
		if (source instanceof BeamAPI && ((BeamAPI)source).getSource() == ship) validDamage = true;

		if (validDamage) {
			damageFromShip += result.getDamageToHull() + result.getTotalDamageToArmor() + result.getDamageToShields();
		} else {
			damageFromOther += result.getDamageToHull() + result.getTotalDamageToArmor() + result.getDamageToShields();
		}
	}
}