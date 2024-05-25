package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import org.magiclib.util.MagicRender;
import data.scripts.utilities.bt_yoinked_graphicLibEffects;
import org.dark.shaders.distortion.DistortionShader;
import org.dark.shaders.distortion.WaveDistortion;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static com.fs.starfarer.api.util.Misc.ZERO;


public class bt_eradication_laser_onhit implements OnHitEffectPlugin {

    static final Color CORE_EXPLOSION_COLOR = new Color(0, 255, 11, 255);
    static final Color CORE_GLOW_COLOR = new Color(218, 241, 213, 150);
    static final Color EXPLOSION_COLOR = new Color(187, 255, 176, 10);
    static final Color FLASH_GLOW_COLOR = new Color(224, 241, 215, 200);
    static final Color GLOW_COLOR = new Color(193, 255, 172, 50);
    static final Vector2f ZERO = new Vector2f();

    static final int NUM_PARTICLES = 50;

    static final DamagingExplosionSpec explosion = new DamagingExplosionSpec(0.05f,
            350,
            200f,
            250,
            125f,
            CollisionClass.PROJECTILE_FF,
            CollisionClass.PROJECTILE_FIGHTER,
            0,
            0,
            0,
            0,
            new Color(68, 255, 57, 255),
            new Color(182, 255, 35, 255)
    );

    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (target instanceof MissileAPI) return;

        explosion(point, engine, projectile);

        if (shieldHit){
            if (damageResult.getDamageToShields() < ((ShipAPI)target).getFluxTracker().getMaxFlux()/10){
                ((ShipAPI)target).getFluxTracker().increaseFlux(((ShipAPI)target).getFluxTracker().getMaxFlux()/10, false);
            }
        }
        if (damageResult.getTotalDamageToArmor() < target.getMaxHitpoints()/10){
            damageResult.setTotalDamageToArmor(target.getMaxHitpoints()/20);
        }
        if (damageResult.getDamageToHull() < target.getMaxHitpoints()/10){
            damageResult.setDamageToHull(target.getMaxHitpoints()/20);
        }

    }

    static void explosion(Vector2f point, CombatEngineAPI engine, DamagingProjectileAPI projectile) {
        // Blast visuals
        float CoreExplosionRadius = 70f;
        float CoreExplosionDuration = 1f;
        float ExplosionRadius = 200f;
        float ExplosionDuration = 1f;
        float CoreGlowRadius = 300f;
        float CoreGlowDuration = 1f;
        float GlowRadius = 400f;
        float GlowDuration = 1f;
        float FlashGlowRadius = 500f;
        float FlashGlowDuration = 0.05f;

        engine.spawnExplosion(point, ZERO, CORE_EXPLOSION_COLOR, CoreExplosionRadius, CoreExplosionDuration);
        engine.spawnExplosion(point, ZERO, EXPLOSION_COLOR, ExplosionRadius, ExplosionDuration);
        engine.addHitParticle(point, ZERO, CoreGlowRadius, 1f, CoreGlowDuration, CORE_GLOW_COLOR);
        engine.addSmoothParticle(point, ZERO, GlowRadius, 1f, GlowDuration, GLOW_COLOR);
        engine.addHitParticle(point, ZERO, FlashGlowRadius, 1f, FlashGlowDuration, FLASH_GLOW_COLOR);

        for (int x = 0; x < NUM_PARTICLES; x++) {
            engine.addHitParticle(point,
                    MathUtils.getPointOnCircumference(null, MathUtils.getRandomNumberInRange(50f, 150f), (float) Math.random() * 360f),
                    MathUtils.getRandomNumberInRange(4, 8), 1f, MathUtils.getRandomNumberInRange(0.4f, 0.9f), CORE_EXPLOSION_COLOR);
        }

        explosion.setDamageType(DamageType.FRAGMENTATION);
        explosion.setShowGraphic(false);
        explosion.setMaxDamage(projectile.getDamageAmount());
        explosion.setMinDamage(explosion.getMaxDamage() * 0.5f);
        engine.spawnDamagingExplosion(explosion, projectile.getSource(), point);

        Global.getSoundPlayer().playSound("ork_siegelaser_impact", 1f + MathUtils.getRandomNumberInRange(-0.1f, 0.1f), 1f, point, ZERO);

        MagicRender.battlespace(
                Global.getSettings().getSprite("fx", "bultach_siege_impact_shockwave"),
                point,
                new Vector2f(),
                new Vector2f(50, 50),
                new Vector2f(1500, 1500),
                //angle,
                360 * (float) Math.random(),
                0,
                new Color(112, 222, 49, 255),
                true,
                0,
                0f,
                0.5f
        );

        MagicRender.battlespace(
                Global.getSettings().getSprite("fx", "bultach_siege_explosion"),
                point,
                new Vector2f(),
                new Vector2f(96, 96),
                new Vector2f(480, 480),
                //angle,
                360 * (float) Math.random(),
                0,
                new Color(255, 255, 255, 255),
                true,
                0,
                0.1f,
                0.3f
        );

        MagicRender.battlespace(
                Global.getSettings().getSprite("fx", "bultach_siege_explosion"),
                point,
                new Vector2f(),
                new Vector2f(128, 128),
                new Vector2f(500, 500),
                //angle,
                360 * (float) Math.random(),
                0,
                new Color(157, 255, 135, 255),
                true,
                0.2f,
                0.0f,
                0.4f
        );

        MagicRender.battlespace(
                Global.getSettings().getSprite("fx", "bultach_siege_explosion"),
                point,
                new Vector2f(),
                new Vector2f(250, 250),
                new Vector2f(150, 150),
                //angle,
                360 * (float) Math.random(),
                0,
                new Color(39, 149, 35, 200),
                true,
                0.35f,
                0.0f,
                1f
        );

        MagicRender.battlespace(
                Global.getSettings().getSprite("fx", "bultach_siege_explosion"),
                point,
                new Vector2f(),
                new Vector2f(200, 200),
                new Vector2f(125, 125),
                //angle,
                360 * (float) Math.random(),
                0,
                new Color(190, 255, 153, 100),
                true,
                0.35f,
                0.0f,
                1.5f
        );

        WaveDistortion wave = new WaveDistortion(point, ZERO);
        wave.setIntensity(1.5f);
        wave.setSize(225f);
        wave.flip(true);
        wave.setLifetime(0f);
        wave.fadeOutIntensity(1f);
        wave.setLocation(projectile.getLocation());
        DistortionShader.addDistortion(wave);

        boolean light = false;
        if (Global.getSettings().getModManager().isModEnabled("shaderLib")) {
            light = true;
        }

        if (light) {
            bt_yoinked_graphicLibEffects.CustomRippleDistortion(
                    point,
                    ZERO,
                    225,
                    3,
                    false,
                    0,
                    360,
                    1f,
                    0.1f,
                    0.25f,
                    0.5f,
                    0.5f,
                    0f
            );

            MagicRender.battlespace(
                    Global.getSettings().getSprite("campaignEntities", "fusion_lamp_glow"),
                    point,
                    new Vector2f(),
                    new Vector2f(100 * MathUtils.getRandomNumberInRange(0.8f, 1.2f), 1000 * MathUtils.getRandomNumberInRange(0.8f, 1.2f)),
                    new Vector2f(),
                    360 * (float) Math.random(),
                    0,
                    new Color(128, 255, 64, 255),
                    true,
                    0,
                    0,
                    0.5f,
                    0.15f,
                    MathUtils.getRandomNumberInRange(0.05f, 0.2f),
                    0,
                    MathUtils.getRandomNumberInRange(0.4f, 0.6f),
                    MathUtils.getRandomNumberInRange(0.1f, 0.3f),
                    CombatEngineLayers.CONTRAILS_LAYER
            );
        }
    }
}
