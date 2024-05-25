package roiderUnion.weapons

import com.fs.starfarer.api.combat.*
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.util.IntervalUtil
import com.fs.starfarer.api.util.Misc
import org.lazywizard.lazylib.MathUtils
import roiderUnion.helpers.Helper
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

/**
 * Author: SafariJohn
 */
class ZapArcKeeper(private val missile: MissileAPI) : BaseEveryFrameCombatPlugin() {
    companion object {
        const val ARMING_TIME = 0.5f
        const val ZAP_ARC = 5f // degrees
        const val ZAP_RANGE = 500f
        const val ZAP_ARC_SOUND = "roider_zap_arc"
    }

    private val interval = IntervalUtil(0.08f, 0.12f)
    private var arming = ARMING_TIME
    private var armed = false
    private var hasZapped = false

    override fun advance(amount: Float, events: List<InputEventAPI>) {
        if (Helper.combatEngine?.isPaused == true) return
        if (hasZapped) return

        if (arming > 0) {
            arming -= amount
            return
        }

        if (!armed) {
            armed = true
            arcIfCan()

            if (isMissileDone()) {
                hasZapped = true
                Helper.combatEngine?.removePlugin(this)
                return
            }

            missile.collisionClass = CollisionClass.MISSILE_NO_FF
        }

        interval.advance(amount)
        if (interval.intervalElapsed()) {
            if (isMissileDone()) {
                hasZapped = true
                Helper.combatEngine?.removePlugin(this)
                return
            }
            arcIfCan()
        }
    }

    private fun isMissileDone(): Boolean = missile.isExpired || missile.didDamage()
            || Helper.combatEngine?.isEntityInPlay(missile) == false

    private fun arcIfCan() {
        // Will just zap first thing that comes up for now
        for (target in Helper.combatEngine?.ships ?: listOf()) {
            if (target.owner == missile.owner) continue
            if (!target.isAlive) continue
            if (target.isPhased) continue

            val distance = MathUtils.getDistance(target.location, missile.location)
            if (distance > ZAP_RANGE) continue

            if (!isTargetInArc(target)) continue

            zap(target, distance)

            return
        }
    }

    private fun isTargetInArc(target: ShipAPI): Boolean {
        val targetAngle = Misc.getAngleInDegrees(missile.location, target.location)
        val diff = Misc.getAngleDiff(missile.facing, targetAngle)
        return diff <= ZAP_ARC
    }

    private fun zap(target: ShipAPI, distance: Float) {
        hasZapped = true

        var emp = missile.empAmount
        if (target.isFighter) emp *= 5f
        val range = missile.weapon.range * 2f
        val brightness = (255f * max(min((range - distance) / distance, 0f), 1f)).toInt()
        val arc = Helper.combatEngine?.spawnEmpArc(
            missile.source, missile.location, missile, target,
            DamageType.ENERGY,
            missile.damageAmount,
            emp,
            100000f,
            ZAP_ARC_SOUND,
            20f,
            Color(100, 125, 200, brightness),
            Color(240, 250, 255, brightness)
        )
        arc?.setSingleFlickerMode()
        missile.explode()
        Helper.combatEngine?.removeEntity(missile)

    }
}