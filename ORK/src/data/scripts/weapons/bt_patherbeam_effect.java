package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import data.scripts.utils.bultach_utils;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static data.scripts.utils.bultach_utils.lerp;

public class bt_patherbeam_effect implements BeamEffectPlugin
{
    private Color COLOR = new Color(255, 26, 26, 225);
    IntervalUtil interval = new IntervalUtil(0.2f, 0.2f);
    final float ARC_DAMAGE_AMOUNT = 300f;

    final float ARC_SPREAD_ANGLE = 35f;
    final float ARC_DISTANCE = 0.75f;
    private float Damage = 100f;

    @Override
    public void advance(float amount, CombatEngineAPI engine, BeamAPI beam)
    {
        interval.advance(amount);
        if (!interval.intervalElapsed()) return;

        // Modify to remove the shield check
        // CombatEntityAPI target = beam.getDamageTarget();
        Vector2f startloc = lerp(beam.getFrom(), beam.getTo(), Misc.random.nextFloat() * ARC_DISTANCE + (1f - ARC_DISTANCE));

// Check if the beam hits anything
        Vector2f endloc = beam.getTo();
        float hitRange = beam.getWeapon().getRange();
        if (beam.didDamageThisFrame() && beam.getDamageTarget() != null && beam.getDamageTarget().getCollisionClass() != CollisionClass.NONE) {
            hitRange = Math.min(hitRange, Vector2f.dot(beam.getFrom(), beam.getDamageTarget().getLocation()) - 10f);
            endloc = MathUtils.getPoint(beam.getFrom(), beam.getWeapon().getCurrAngle(), hitRange);
        }

// ...


        // Emp arcs share their colors with the beam
        engine.spawnEmpArcVisual(
                startloc,
                null,
                endloc,
                beam.getSource(),
                20f,
                beam.getFringeColor(),
                beam.getCoreColor()
        );

        // Apply damage to the hit target
        if (beam.didDamageThisFrame() && beam.getDamageTarget() != null) {
            CombatEntityAPI target = beam.getDamageTarget();
            engine.applyDamage(
                    target,
                    endloc,
                    ARC_DAMAGE_AMOUNT,
                    beam.getDamage().getType(),
                    0f,
                    false,
                    true,
                    beam.getSource()
            );
        }

        // Spawn emp arcs to pierce shields
        engine.spawnEmpArcPierceShields(beam.getSource(), beam.getFrom(), beam.getSource(), beam.getSource(),
                DamageType.ENERGY,
                Damage * 0.0f,
                Damage * 1f,
                100000f,
                null,
                10f,
                COLOR,
                COLOR
        );

        // Play the sound when the beam hits a target
        Global.getSoundPlayer().playSound("tachyon_lance_emp_impact", 1f, 1f, lerp(startloc, endloc, 0.5f), Misc.ZERO);
    }
}
