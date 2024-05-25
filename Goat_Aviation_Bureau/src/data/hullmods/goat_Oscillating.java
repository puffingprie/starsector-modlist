package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

public class goat_Oscillating extends BaseHullMod {

	public static final float BEAM_DPS_DURATION = 0.1f;
	public static final float BEAM_DPS_DURATION_CONPUTE_MULT = 10f;
	public static final float BEAM_REDUCE_PERCENT = 170f;

	public static final float E_DAMAGE = 0.1f;
	public static final float EMP_DAMAGE = 0.9f;

	public static final float MAX_DAMAGE_REDUCE_PERCENT = 60f;
	public static final float DAMAGE_REDUCE_TEST1 = 40f;
	public static final float DAMAGE_REDUCE_TEST2 = 150f;
	public static final float DAMAGE_REDUCE_TEST3 = 200f;
	public static final float DAMAGE_REDUCE_TEST4 = 300f;
	public static final float DAMAGE_THRESHOLD_TO_SPAWN_EFFECT = 40f;

	public static float SMOD_EFFECT_PERCENT = 50f;

	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getBeamDamageTakenMult().modifyMult(id, 1f - BEAM_REDUCE_PERCENT * 0.01f);

		boolean sMod = isSMod(stats);
		if (sMod) {
			stats.getShieldTurnRateMult().modifyMult(id, 1f - 0.01f * SMOD_EFFECT_PERCENT);
			stats.getShieldUnfoldRateMult().modifyMult(id, 1f - 0.01f * SMOD_EFFECT_PERCENT);
		}

	}

	@Override
	public String getSModDescriptionParam(int index, ShipAPI.HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int)SMOD_EFFECT_PERCENT + "%";
		if (index == 1) return "" + (int)SMOD_EFFECT_PERCENT + "%";
		return null;
	}

	@Override
	public boolean isSModEffectAPenalty() {
		return true;
	}

	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ship.addListener(new goat_OscillatingListener());
		if (ship.getShield() != null) {
			ship.getShield().setRadius(ship.getShield().getRadius(), Global.getSettings().getSpriteName("GOAT_TECH", "GOAT_shield_inner_s"), Global.getSettings().getSpriteName("GOAT_TECH", "GOAT_shield_outer"));
		}
	}

	public static class goat_OscillatingListener implements DamageTakenModifier {

		public static final String id = "goat_Oscillating1";
		public static final float ARC_THRESHOLD = 100f;

		public static float getReduceInPercent(float damageAmount) {
			return (float)Math.exp(-0.004f * damageAmount + 4.4f);
		}

		private float storagedDamage = 0f;

		@Override
		public String modifyDamageTaken(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {

			if (!shieldHit) return null;

			float damageAmount = 0f;
			float damageForReduce = 0f;
			if (param instanceof BeamAPI) {
				//要减伤光束吗?
				damageAmount = damage.computeDamageDealt(BEAM_DPS_DURATION);
				damageForReduce = damageAmount * 10f;
			} else if (param != null) {
				damageAmount = damage.getDamage();
				damageForReduce = damageAmount;
			}

			float y = getReduceInPercent(damageForReduce);
			if (y > MAX_DAMAGE_REDUCE_PERCENT) y = MAX_DAMAGE_REDUCE_PERCENT;
			if (y < 1f) y = 0f;

			if (y > 0f) {

				ShipAPI ship = (ShipAPI)target;
				if (y > DAMAGE_THRESHOLD_TO_SPAWN_EFFECT) {
					new goat_OscillatingVisual(ship, VectorUtils.getAngle(ship.getShieldCenterEvenIfNoShield(), point));

				}

				float reduceMult = 1f - y * 0.01f;
				damage.getModifier().modifyMult(id, reduceMult);

				float reduced = damageAmount * (1f - reduceMult);
				storagedDamage += reduced;

				if (storagedDamage >= ARC_THRESHOLD) {

					float facing = ship.getShield().getFacing();
					facing += MathUtils.getRandomNumberInRange(ship.getShield().getActiveArc() * -0.5f, ship.getShield().getActiveArc() * 0.5f);

					float range = ship.getShield().getRadius() * MathUtils.getRandomNumberInRange(0.5f, 0.9f);

					Vector2f location = MathUtils.getPoint(ship.getShieldCenterEvenIfNoShield(), range, facing);
					Global.getCombatEngine().spawnEmpArcPierceShields(ship, location, null, ship,DamageType.ENERGY, (E_DAMAGE * storagedDamage), (EMP_DAMAGE * storagedDamage), 999999999f, null, 3f, ship.getShield().getInnerColor(), ship.getShield().getRingColor());

					storagedDamage = 0f;
				}

				return id;
			}
			return null;
		}
	}

	@Override
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 6) return (int)DAMAGE_THRESHOLD_TO_SPAWN_EFFECT + "%";
		if (index == 0) return (int)DAMAGE_REDUCE_TEST1 + "";
		if (index == 1) return (int)goat_OscillatingListener.getReduceInPercent(DAMAGE_REDUCE_TEST1) + "%";
		if (index == 2) return (int)DAMAGE_REDUCE_TEST2 + "";
		if (index == 3) return (int)goat_OscillatingListener.getReduceInPercent(DAMAGE_REDUCE_TEST2) + "%";
		if (index == 4) return (int)DAMAGE_REDUCE_TEST4 + "";
		if (index == 5) return (int)goat_OscillatingListener.getReduceInPercent(DAMAGE_REDUCE_TEST4) + "%";
		if (index == 7) return (int)(E_DAMAGE * 100) + "%";
		if (index == 8) return (int)(EMP_DAMAGE * 100) + "%";

		return null;
	}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && ship.getShield() != null && ship.getHullSpec().getHullId().startsWith("goat_");
	}

	@Override
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship == null) return "";
		if (ship.getHullSpec().getHullId().startsWith("goat_")) return "Ship does not have a shield";
		if (ship.getShield() != null) return "Can only be installed on Goathead Aviation ships";
		return "Can only be installed on Goathead Aviation ships with shields";
	}
}

