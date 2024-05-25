package data.scripts.weapons.ai;
 
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;
 
public class Xhan_Mega_Buster_Bomb_AI implements MissileAIPlugin
{
    private static final int FRAG_COUNT = 150;
    private static final float FRAG_VELOCITY_MOD_MAX = 40f;
    private static final float FRAG_VELOCITY_MOD_MIN = 40f;
    private static final float EXPLOSION_SIZE_OUTER = 400f;
    private static final float EXPLOSION_SIZE_INNER = 200f;
    private static final float EXPLOSION_DAMAGE_MAX = 500f;
    private static final float EXPLOSION_DAMAGE_MIN = 1f;
    private static final float EXPLOSION_DURATION = 0.1f;
    private static final float PARTICLE_DURATION = 0.2f;
    private static final int PARTICLE_COUNT = 50;
    private static final int PARTICLE_SIZE_MIN = 1;
    private static final int PARTICLE_SIZE_RANGE = 10;
    private static final String FRAG_WEAPON_ID = "Xhan_pharrek_fraggun";
    private static final float TIMER_LENGTH = 0.001f;
    private static final Color VFX_COLOR = new Color(153, 255, 51, 150);
    private static final String SOUND_ID = "XHAN_PHARREK_PULSE_SOUND";
    private float timeLive = 0f;
    private final MissileAPI missile;
 
    public static final String MEGA_BUSTER_BOMB_ID = "Xhan_Pharrek_pulse";
    public Xhan_Mega_Buster_Bomb_AI(MissileAPI missile)
    {
        this.missile = missile;
    }
 
    @Override
    public void advance(float amount)
    {
 
        timeLive += amount;  // This ticks our timer down
 
        // Has our timer expired?
        if (timeLive >= TIMER_LENGTH)
        {
            timeLive = -99999f;
            explode();
        }
    }
 
    private void explode()
    {
        // This removes the bomb
        Global.getCombatEngine().removeEntity(missile);
 
        // This spawns some custom vfx stacked with the "normal" ones done by spawnDamagingExplosion
        Global.getCombatEngine().spawnExplosion(missile.getLocation(), missile.getVelocity(), VFX_COLOR, EXPLOSION_SIZE_INNER, EXPLOSION_DURATION);
 
        // This spawns the damaging explosion
        /*
                float duration
                float radius
                float coreRadius
                float maxDamage
                float minDamage
                CollisionClass collisionClass
                CollisionClass collisionClassByFighter
                float particleSizeMin
                float particleSizeRange
                float particleDuration
                int particleCount
                Color particleColor
                Color explosionColor
         */
        DamagingExplosionSpec boom = new DamagingExplosionSpec(
                EXPLOSION_DURATION,
                EXPLOSION_SIZE_OUTER,
                EXPLOSION_SIZE_INNER,
                EXPLOSION_DAMAGE_MAX,
                EXPLOSION_DAMAGE_MIN,
                CollisionClass.PROJECTILE_FF,
                CollisionClass.PROJECTILE_FIGHTER,
                PARTICLE_SIZE_MIN,
                PARTICLE_SIZE_RANGE,
                PARTICLE_DURATION,
                PARTICLE_COUNT,
                VFX_COLOR,
                VFX_COLOR
        );
        boom.setDamageType(DamageType.ENERGY);
        boom.setShowGraphic(true);
        boom.setSoundSetId(SOUND_ID);
        Global.getCombatEngine().spawnDamagingExplosion(boom, missile.getSource(), missile.getLocation(), false);
 
        // This spawns the frag, also distributing them in a nice even 360 degree arc
        Vector2f vel = new Vector2f();
        for (int i = 0; i < FRAG_COUNT; i++)
        {
            float angle = missile.getFacing() + i * 360f / FRAG_COUNT + (float) Math.random() * 180f / FRAG_COUNT;
            angle %= 360f;
            vel.set((float) Math.random() * FRAG_VELOCITY_MOD_MAX - FRAG_VELOCITY_MOD_MIN, 0f);
            VectorUtils.rotate(vel, angle, vel);
            Vector2f location = MathUtils.getPointOnCircumference(missile.getLocation(), 3f, angle);
            Global.getCombatEngine().spawnProjectile(missile.getSource(), missile.getWeapon(), FRAG_WEAPON_ID, location, angle, vel);
        }
    }
}