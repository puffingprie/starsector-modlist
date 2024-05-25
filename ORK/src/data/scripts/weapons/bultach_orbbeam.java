package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import org.dark.shaders.distortion.DistortionShader;
import org.dark.shaders.distortion.WaveDistortion;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static data.scripts.utils.bultach_utils.lerp;


public class bultach_orbbeam implements EveryFrameWeaponEffectPlugin, BeamEffectPlugin
{

    static final Color TRANSITION_PARTICLE_COLOR = new Color(128, 128, 255, 255);
    static final Color DISTORTION_FLASH_COLOR = new Color(138, 22, 215, 157);

    static final float TIER_TIME = 2.5f; // seconds of firing to go up a tier

    // used to tell the other beam plugins (beam effect, for arcing, and listener, for damage) what stage it's at
    // this is a little janky
    static final float T1_WIDTH = 10f;
    static final float T2_WIDTH = 20f;
    static final float T3_WIDTH = 30f;

    // self-explanatory
    static final float T2_DAMAGE_MULT = 1.5f;
    static final float T3_DAMAGE_MULT = 2.0f;

    // how often it arcs at different power levels
    static final IntervalUtil t2_arc_interval = new IntervalUtil(0.25f, 0.33f);
    static final IntervalUtil t3_arc_interval = new IntervalUtil(0.15f, 0.25f);

    // self-explanatory
    static final float ARC_DAMAGE = 100f;
    static final float ARC_EMP = 200f;

    // how fast the distortion flashes (it spawns a few particles inside at random angles)
    private final IntervalUtil particle_interval = new IntervalUtil(0.05f, 0.1f);
    private boolean did_t2 = false;
    private boolean did_t3 = false;

    float time_firing = 0f;
    boolean wasZero = false;
    WaveDistortion wave;

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon)
    {
        if (engine == null || weapon == null || engine.isPaused() || weapon.getShip() == null) return;
        if (!weapon.getShip().hasListenerOfClass(bultach_orbbeam_listener.class))
            weapon.getShip().addListener(new bultach_orbbeam_listener());

        particle_interval.advance(amount);
        /*if (flare_interval.intervalElapsed())
            bultach_utils.createSharpFlare(
                    engine,
                    weapon.getShip(),
                    weapon.getShip(),
                    weapon.getLocation(),
                    10f,
                    100f,
                    weapon.getCurrAngle() + 90f,
                    ARC_COLOR_FRINGE,
                    ARC_COLOR_CORE);*/

        WaveDistortion wave = new WaveDistortion();
        wave.setLocation(weapon.getLocation());
        wave.setIntensity(20);
        wave.setSize(20);
        wave.setLifetime(0.02f);
        DistortionShader.addDistortion(wave);



        if (weapon.isFiring()) {
            time_firing += amount;
            if (particle_interval.intervalElapsed())
                addRadialParticles(engine, weapon.getLocation(), weapon.getShip().getVelocity(), DISTORTION_FLASH_COLOR, 50, 300, 0.2f, 10f, 2);
        }
        else {
            if (particle_interval.intervalElapsed())
                addRadialParticles(engine, weapon.getLocation(), weapon.getShip().getVelocity(), DISTORTION_FLASH_COLOR, 50, 300, 0.2f, 10f, 1);
            did_t2 = false;
            did_t3 = false;
            time_firing = 0f;
            return;
        }

        // transition effects
        if (time_firing > TIER_TIME && !did_t2)
        {
            did_t2 = true;
            // do t1 -> t2 transition vfx
            addRadialParticles(engine, weapon.getLocation(), weapon.getShip().getVelocity(), TRANSITION_PARTICLE_COLOR, 50, 300, 0.5f, 15f, 30);
            //Global.getSoundPlayer().playSound("transition_1_sound", 1f, 1f, weapon.getLocation(), weapon.getShip().getVelocity());
        } else if (time_firing > 2 * TIER_TIME && !did_t3)
        {
            did_t3 = true;
            // do t2 -> t3 transition
            addRadialParticles(engine, weapon.getLocation(), weapon.getShip().getVelocity(), TRANSITION_PARTICLE_COLOR, 100, 500, 0.5f, 20f, 30);
            //Global.getSoundPlayer().playSound("transition_2_sound", 1f, 1f, weapon.getLocation(), weapon.getShip().getVelocity());
        }

        // continuing effects
        if (time_firing < TIER_TIME - 0.25f)
            weapon.getBeams().get(0).setWidth(T1_WIDTH);
        else if (time_firing > (TIER_TIME - 0.25f) && time_firing < (TIER_TIME + 0.25f)) {
            weapon.getBeams().get(0).setWidth(lerp(T1_WIDTH, T2_WIDTH, (time_firing - TIER_TIME + 0.25f) * 2f));
        } else if (time_firing > (TIER_TIME * 2f - 0.25f) && time_firing < (TIER_TIME * 2f + 0.25f)) {
            weapon.getBeams().get(0).setWidth(lerp(T2_WIDTH, T3_WIDTH, (time_firing - TIER_TIME * 2f + 0.25f) * 2f));
        }

    }

    @Override
    public void advance(float amount, CombatEngineAPI engine, BeamAPI beam)
    {
        CombatEntityAPI target = beam.getDamageTarget();
        if (target instanceof ShipAPI && beam.getBrightness() >= 1f) {
            float dur = beam.getDamage().getDpsDuration();
            // needed because when the ship is in fast-time, dpsDuration will not be reset every frame as it should be
            if (!wasZero) dur = 0;
            wasZero = beam.getDamage().getDpsDuration() <= 0;
            IntervalUtil fireInterval;
            if (beam.getWidth() >= T3_WIDTH - 0.1f)
                fireInterval = t3_arc_interval;
            else if (beam.getWidth() >= T2_WIDTH)
                fireInterval = t2_arc_interval;
            else
                return;

            fireInterval.advance(dur);
            if (fireInterval.intervalElapsed()) {
                ShipAPI ship = (ShipAPI) target;
                boolean hitShield = target.getShield() != null && target.getShield().isWithinArc(beam.getTo());
                float pierceChance = ((ShipAPI) target).getHardFluxLevel() - 0.1f;
                pierceChance *= ship.getMutableStats().getDynamic().getValue(Stats.SHIELD_PIERCED_MULT);

                boolean piercedShield = hitShield && (float) Math.random() < pierceChance;
                //piercedShield = true;

                if (!hitShield || piercedShield) {
                    Vector2f point = beam.getRayEndPrevFrame();
                    engine.spawnEmpArcPierceShields(
                            beam.getSource(), point, beam.getDamageTarget(), beam.getDamageTarget(),
                            DamageType.ENERGY,
                            ARC_DAMAGE, // damage
                            ARC_EMP, // emp
                            100000f, // max range
                            "tachyon_lance_emp_impact",
                            beam.getWidth() + 5f,
                            beam.getFringeColor(),
                            beam.getCoreColor()
                    );
                }
            }
        }
    }

    private static class bultach_orbbeam_listener implements DamageDealtModifier
    {

        @Override
        public String modifyDamageDealt(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit)
        {
            if (param instanceof BeamAPI && ((BeamAPI) param).getWeapon() != null)
            {
                BeamAPI beam = (BeamAPI) param;
                WeaponAPI wep = ((BeamAPI) param).getWeapon();
                if (wep.getSpec().getWeaponId().equals("bultach_orbbeam"))
                {
                    if (beam.getWidth() > T1_WIDTH * 1.5f && beam.getWidth() <= T2_WIDTH) {
                        damage.getModifier().modifyMult("orbbeam", T2_DAMAGE_MULT);
                        return "orbbeam";
                    }
                    else if (beam.getWidth() > T2_WIDTH * 1.5f) {
                        damage.getModifier().modifyMult("orbbeam", T3_DAMAGE_MULT);
                        return "orbbeam";
                    }
                }
            }
            return null;
        }
    }

    private static void addRadialParticles(CombatEngineAPI engine, Vector2f loc, Vector2f baseVel, Color color, float minvel, float maxvel, float dur, float size, int num)
    {
        for (int i = 0; i < num; i++)
        {
            Vector2f vel = MathUtils.getRandomPointOnCircumference(baseVel, lerp(minvel, maxvel, Misc.random.nextFloat()));
            engine.addHitParticle(
                    loc,
                    vel,
                    size,
                    1f,
                    dur,
                    color
            );
        }
    }
}
