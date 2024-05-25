package data.scripts.shipsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicRender;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class bt_reckoning extends BaseShipSystemScript {
    boolean madeaura = false;
    boolean secondcooldown = false;
    boolean finishedcooldown = true;

    IntervalUtil interval = new IntervalUtil(0, 2);

    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {


        ShipAPI ship = (ShipAPI) stats.getEntity();
        CombatEngineAPI engine = Global.getCombatEngine();
        secondcooldown = false;
        finishedcooldown = false;
        float totalcooldown = ship.getSystem().getCooldown();
        float remainingcooldown = ship.getSystem().getCooldownRemaining();
        CombatEntityAPI shipe = stats.getEntity();
        ShieldAPI shielde = stats.getEntity().getShield();
        List<DamagingProjectileAPI> projs = engine.getProjectiles();

        if (ship.getSystem().getState().equals(ShipSystemAPI.SystemState.IDLE)) {
            stats.getHullDamageTakenMult().unmodify(id);
            stats.getArmorDamageTakenMult().unmodify(id);
            stats.getEmpDamageTakenMult().unmodify(id);
        }

        if (ship.getSystem().isChargedown() && !madeaura) {
            Vector2f reckonloc = ship.getLocation();
            Vector2f reckongrowth = new Vector2f(0, 0);
            DamagingExplosionSpec explosion = new DamagingExplosionSpec(0.8F, 950.0F, 500.0F, 250.0F, 125.0F, CollisionClass.PROJECTILE_FF, CollisionClass.PROJECTILE_FIGHTER, 10.0F, 5.0F, 0.5F, 20, new Color(255, 255, 255, 255), new Color(255, 255, 255, 255));
            engine.spawnDamagingExplosion(explosion, ship, new Vector2f(ship.getLocation()), false);
            Global.getSoundPlayer().playSound("Bt_Holy_Teleporter", 1f, 2f, ship.getLocation(), ship.getVelocity());


            MagicRender.battlespace(
                    Global.getSettings().getSprite("campaignEntities", "fusion_lamp_glow"),
                    reckonloc,
                    new Vector2f(),
                    new Vector2f(600 * MathUtils.getRandomNumberInRange(0.8f, 1.2f), 2500 * MathUtils.getRandomNumberInRange(0.8f, 1.2f)),
                    new Vector2f(),
                    360 * (float) Math.random(),
                    9,
                    new Color(255, 237, 168, 255),
                    true,
                    20,
                    3,
                    1.5f,
                    0.6f,
                    MathUtils.getRandomNumberInRange(0.05f, 0.2f),
                    0,
                    MathUtils.getRandomNumberInRange(0.4f, 0.6f),
                    MathUtils.getRandomNumberInRange(0.1f, 0.3f),
                    CombatEngineLayers.CONTRAILS_LAYER
            );

            madeaura = true;


        }
        if ((remainingcooldown < 3 * (totalcooldown / 4)) && !finishedcooldown) {
            secondcooldown = true;
        }
        if (ship.getSystem().isCoolingDown() && !secondcooldown) {
            stats.getTurnAcceleration().modifyMult(id, 3f);
            Global.getCombatEngine().maintainStatusForPlayerShip("ReckoningBuff", "graphics/icons/hullsys/fortress_shield.png", "Divine Reckoning", "Faith Is Your Shield", false);
            finishedcooldown = false;

        }
        if (ship.getSystem().isCoolingDown() && secondcooldown) {
            stats.getTurnAcceleration().unmodify();
            finishedcooldown = true;
            madeaura = false;
            stats.getHullDamageTakenMult().unmodify(id);
            stats.getArmorDamageTakenMult().unmodify(id);
            stats.getEmpDamageTakenMult().unmodify(id);
        }
        if (ship.getSystem().isCoolingDown() && !secondcooldown) {
            SpriteAPI pulse = Global.getSettings().getSprite("graphics/fx/bultach_holy_shockwave.png");
            Vector2f pulseloc = ship.getLocation();
            Vector2f pulsegrowth = new Vector2f(7000, 7000);

            interval.advance(.25f);
            if (interval.intervalElapsed()) {
                MagicRender.objectspace(
                        pulse,
                        shipe,
                        new Vector2f(0, 0),
                        new Vector2f(0, 0),
                        new Vector2f(0, 0),
                        pulsegrowth,
                        MathUtils.getRandomNumberInRange(0, 360),
                        MathUtils.getRandomNumberInRange(30, 70),
                        true,
                        Color.WHITE,
                        false,
                        0f,
                        0f,
                        0,
                        0,
                        0,
                        0f,
                        0.2f,
                        0.4f,
                        true,
                        CombatEngineLayers.BELOW_SHIPS_LAYER
                );
                MagicRender.objectspace(
                        pulse,
                        shipe,
                        new Vector2f(0, 0),
                        new Vector2f(0, 0),
                        new Vector2f(0, 0),
                        pulsegrowth,
                        MathUtils.getRandomNumberInRange(0, 360),
                        MathUtils.getRandomNumberInRange(30, 70),
                        true,
                        Color.WHITE,
                        false,
                        0f,
                        0f,
                        0,
                        0,
                        0,
                        0f,
                        0.02f,
                        0.002f,
                        true,
                        CombatEngineLayers.BELOW_SHIPS_LAYER
                );
            }
            Iterator projcounter = projs.iterator();
            while (projcounter.hasNext()) {
                DamagingProjectileAPI boolet = (DamagingProjectileAPI) projcounter.next();
                double bulletX = boolet.getLocation().x;
                double bulletY = boolet.getLocation().y;
                double shipX = ship.getLocation().x;
                double shipY = ship.getLocation().y;

                double distance = Math.sqrt(Math.pow(shipX - bulletX, 2) + Math.pow(shipY - bulletY, 2));

                if (boolet.getSource() == ship) {
                    continue;
                }

                if ((distance <= 500)) {
                    engine.addSwirlyNebulaParticle(boolet.getLocation(), new Vector2f(0, 0), MathUtils.getRandomNumberInRange(10, 30), MathUtils.getRandomNumberInRange(1, 2), MathUtils.getRandomNumberInRange(0.8f, 1.2f), MathUtils.getRandomNumberInRange(0.3f, 0.7f), MathUtils.getRandomNumberInRange(0.4f, 0.6f), new Color(MathUtils.getRandomNumberInRange(200, 255), MathUtils.getRandomNumberInRange(100, 160), MathUtils.getRandomNumberInRange(0, 60), MathUtils.getRandomNumberInRange(100, 200)), true);
                    engine.addNebulaParticle(boolet.getLocation(), new Vector2f(0, 0), MathUtils.getRandomNumberInRange(10, 30), MathUtils.getRandomNumberInRange(1, 2), MathUtils.getRandomNumberInRange(0.8f, 1.2f), MathUtils.getRandomNumberInRange(0.3f, 0.7f), MathUtils.getRandomNumberInRange(0.4f, 0.6f), new Color(MathUtils.getRandomNumberInRange(200, 255), MathUtils.getRandomNumberInRange(100, 160), MathUtils.getRandomNumberInRange(0, 60), MathUtils.getRandomNumberInRange(100, 200)), true);
                    boolet.getLocation().set(10000, 10000);

                }
            }
            if (shielde.isOn()) {
                shielde.toggleOff();
            }
            stats.getHullDamageTakenMult().modifyMult(id, 0);
            stats.getArmorDamageTakenMult().modifyMult(id, 0);
            stats.getEmpDamageTakenMult().modifyMult(id, 0);

        }
    }

        public void unapply (MutableShipStatsAPI stats, String id){
        }
    }

