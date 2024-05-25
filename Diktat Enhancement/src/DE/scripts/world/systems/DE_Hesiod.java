package DE.scripts.world.systems;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial;
import com.fs.starfarer.api.impl.campaign.terrain.*;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;


// sekrit!
public class DE_Hesiod {
    boolean isPAGSM = Global.getSettings().getModManager().isModEnabled("PAGSM");
    boolean isHMI = Global.getSettings().getModManager().isModEnabled("HMI");

    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Hesiod");
        LocationAPI hyper = Global.getSector().getHyperspace();

        system.setBackgroundTextureFilename("graphics/backgrounds/background6.jpg");

        // create the star and generate the hyperspace anchor for this system
        PlanetAPI star = system.initStar("hesiod",
                StarTypes.WHITE_DWARF, // id in planets.json
                500f, // size of star
                500, // extent of corona outside star
                5f, // solar wind burn level
                0.5f, // flare probability
                2.0f); // CR loss multiplier, good values are in the range of 1-5
        float radius = 10000;
        clearDeepHyper(star, radius);
        star.setCustomDescriptionId("star_hesiod");

        /*
         * addPlanet() parameters:
         * 1. Unique id for this planet (or null to have it be autogenerated)
         * 2. What the planet orbits (orbit is always circular)
         * 3. Name
         * 4. Planet type id in planets.json
         * 5. Starting angle in orbit, i.e. 0 = to the right of the star
         * 6. Planet radius, pixels at default zoom
         * 7. Orbit radius, pixels at default zoom
         * 8. Days it takes to complete an orbit. 1 day = 10 seconds.
         */

        // A suspicious asteroid belt
        system.addRingBand(star, "misc", "rings_dust0", 256f, 0, Color.white, 256f, 1500, 100f, null, null);
        system.addRingBand(star, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 1500, 100f, null, null);
        system.addRingBand(star, "misc", "rings_asteroids0", 256f, 0, Color.white, 256f, 1500, 126f, null, null);
        system.addAsteroidBelt(star, 200, 1500, 256f, 100, 130, Terrain.ASTEROID_BELT, "Apollodorus Remnant");

        // Brontes - thunder
        SectorEntityToken brontes = system.addCustomEntity("brontes_module", "Brontes Bay Module", "station_side06", "neutral");
        brontes.setCircularOrbitPointingDown(star, 0, 1500, 100);
        brontes.setCustomDescriptionId("brontes_bay");
        brontes.setSensorProfile(500f);
        brontes.setDiscoverable(true);
        brontes.setInteractionImage("illustrations", "abandoned_station2");
        Misc.setAbandonedStationMarket("brontes_module_market", brontes);

        // Steropes - lightning
        SectorEntityToken steropes = system.addCustomEntity("steropes_module", "Steropes Power Module", "station_side06", "neutral");
        steropes.setCircularOrbitPointingDown(star, 120, 1500, 100);
        steropes.setCustomDescriptionId("steropes_power");
        steropes.setSensorProfile(500f);
        steropes.setDiscoverable(true);
        // Arges - light
        SectorEntityToken arges = system.addCustomEntity("arges_module", "Arges Collection Module", "station_side06", "neutral");
        arges.setCircularOrbitPointingDown(star, 240, 1500, 100);
        arges.setCustomDescriptionId("arges_collection");
        arges.setSensorProfile(500f);
        arges.setDiscoverable(true);

        // Cronus - only planet to survive Hesiod's collapse and former volatiles mining site/fuel prod facil
        PlanetAPI cronus = system.addPlanet("cronus", star, "Cronus", "gas_giant", 180, 500, 4000, 300);
        cronus.setCustomDescriptionId("planet_cronus");
        cronus.getMarket().addCondition(Conditions.DENSE_ATMOSPHERE);
        cronus.getMarket().addCondition(Conditions.EXTREME_WEATHER);
        cronus.getMarket().addCondition(Conditions.HIGH_GRAVITY);
        cronus.getMarket().addCondition(Conditions.VOLATILES_PLENTIFUL);

        // L3 jump point
        JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("cronus_jump", "Cronus L3 Jump Point");
        OrbitAPI orbit = Global.getFactory().createCircularOrbit(star, 0, 500, 30);
        jumpPoint.setOrbit(orbit);
        jumpPoint.setRelatedPlanet(cronus);
        jumpPoint.setCircularOrbit(star, 0, 4000, 300);
        system.addEntity(jumpPoint);

        // Hundred-Handed dynamo stations - all destroyed
        SectorEntityToken cottus = system.addCustomEntity("cottus_volatiles", "Cottus Volatiles Harvester", "station_side03", "neutral");
        cottus.setCircularOrbitPointingDown(star, 60, 6000f, 300);
        cottus.setCustomDescriptionId("cottus_volatiles");
        cottus.setSensorProfile(250f);
        cottus.setDiscoverable(true);

        SectorEntityToken briareus = system.addCustomEntity("briareus_volatiles", "Briareus Volatiles Harvester", "station_side03", "neutral");
        briareus.setCircularOrbitPointingDown(star, 180, 7000f, 300);
        briareus.setCustomDescriptionId("briareus_volatiles");
        briareus.setSensorProfile(250f);
        briareus.setDiscoverable(true);

        SectorEntityToken gyges = system.addCustomEntity("gyges_volatiles", "Gyges Volatiles Harvester", "station_side03", "neutral");
        gyges.setCircularOrbitPointingDown(star, 300, 8000f, 300);
        gyges.setCustomDescriptionId("gyges_volatiles");
        gyges.setSensorProfile(250f);
        gyges.setDiscoverable(true);

        // shhhhh
        PlanetAPI secret = system.addPlanet("secret", star, "[DATA REDACTED]", "barren-bombarded", 270, 80, 15000, 1000);
        secret.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
        secret.getMarket().addCondition(Conditions.METEOR_IMPACTS);
        secret.getMarket().addCondition(Conditions.EXTREME_TECTONIC_ACTIVITY);
        secret.getMarket().addCondition(Conditions.VERY_COLD);
        secret.getMarket().addCondition(Conditions.VOLATILES_PLENTIFUL);
        secret.setCustomDescriptionId("planet_redacted");

        SectorEntityToken secret2 = system.addCustomEntity("weapons", "[DATA REDACTED]", "station_side05", "neutral");
        secret2.setCircularOrbitPointingDown(secret, 173, 140, 100);
        secret2.setCustomDescriptionId("weapons_plat");
        secret2.addTag("weaponsPlat");
        secret2.setInteractionImage("illustrations", "abandoned_station");
        secret2.setSensorProfile(50f);
        secret2.setDiscoverable(true);

        // Mission debris
        DebrisFieldTerrainPlugin.DebrisFieldParams params = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                50f, // field radius - should not go above 1000 for performance reasons
                1f, // density, visual - affects number of debris pieces
                10000000f, // duration in days
                0f); // days the field will keep generating glowing pieces
        params.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
        params.baseSalvageXP = 1000; // base XP for scavenging in field
        SectorEntityToken MissionDebris = Misc.addDebrisField(system, params, StarSystemGenerator.random);
        MissionDebris.setSensorProfile(500f);
        MissionDebris.setDiscoverable(true);
        MissionDebris.setCircularOrbit(secret, 0f, 750f, 50f);
        MissionDebris.setId("hesiod_MissionDebris");

        // Dead ships lmao
        if (!isHMI) {
            addDerelict(system, MissionDebris, "brilliant_Standard", ShipRecoverySpecial.ShipCondition.BATTERED, 10f, true);
            addDerelict(system, MissionDebris, "fulgent_Support", ShipRecoverySpecial.ShipCondition.WRECKED, 75f, false);
            addDerelict(system, MissionDebris, "lumen_Standard", ShipRecoverySpecial.ShipCondition.AVERAGE, 100f, false);
            addDerelict(system, MissionDebris, "vanguard_Attack", ShipRecoverySpecial.ShipCondition.GOOD, 135f, false);
        } else {
            addDerelict(system, MissionDebris, "hmi_coelum_attack", ShipRecoverySpecial.ShipCondition.BATTERED, 10f, true);
            addDerelict(system, MissionDebris, "fulgent_Support", ShipRecoverySpecial.ShipCondition.WRECKED, 75f, false);
            addDerelict(system, MissionDebris, "aureole_sabot", ShipRecoverySpecial.ShipCondition.AVERAGE, 100f, false);
            addDerelict(system, MissionDebris, "vanguard_Attack", ShipRecoverySpecial.ShipCondition.GOOD, 135f, false);
        }

        SectorEntityToken nebula1 = system.addTerrain(Terrain.NEBULA, new BaseTiledTerrain.TileParams(
                   "      " +
                        "  xx  " +
                        " xxxx " +
                        " xxxx " +
                        "  xx  " +
                        "      ",
                6, 6, // size of the nebula grid, should match above string
                "terrain", "nebula", 10, 10, "Hesiod Stellar Remnant"));
        nebula1.getLocation().set(star.getLocation().x, star.getLocation().y);
        nebula1.setCircularOrbit(star, 60f, 1, 1000000);

        SectorEntityToken nebula2 = system.addTerrain(Terrain.NEBULA, new BaseTiledTerrain.TileParams(
                   " xxxx " +
                        "xx  xx" +
                        "x    x" +
                        "x    x" +
                        "xx  xx" +
                        " xxxx ",
                6, 6, // size of the nebula grid, should match above string
                "terrain", "nebula", 10, 10, "Hesiod Stellar Remnant"));
        nebula2.getLocation().set(star.getLocation().x, star.getLocation().y);
        nebula2.setCircularOrbit(star, 300f, 1, 1000000);

        StarSystemGenerator.addSystemwideNebula(system, StarAge.OLD);

        float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, star, StarAge.OLD,
                0, 0, // min/max entities to add
                17000, // radius to start adding at
                1, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                true, // whether to use custom or system-name based names
                true); // whether to allow habitable worlds

        system.autogenerateHyperspaceJumpPoints(true, false);
        }

    // Function for adding derelicts, just copy-paste this into your system's java file to use it
    private void addDerelict (StarSystemAPI system,
                              SectorEntityToken focus,
                              String variantId,
                              ShipRecoverySpecial.ShipCondition condition,
                              float orbitRadius,
                              boolean recoverable){
        DerelictShipEntityPlugin.DerelictShipData params = new DerelictShipEntityPlugin.DerelictShipData(new ShipRecoverySpecial.PerShipData(variantId, condition), false);
        SectorEntityToken ship = BaseThemeGenerator.addSalvageEntity(system, Entities.WRECK, Factions.NEUTRAL, params);
        ship.setDiscoverable(true);

        float orbitDays = orbitRadius / (10f + (float) Math.random() * 5f);
        ship.setCircularOrbit(focus, (float) Math.random() * 360f, orbitRadius, orbitDays);

        if (recoverable) {
            SalvageSpecialAssigner.ShipRecoverySpecialCreator creator = new SalvageSpecialAssigner.ShipRecoverySpecialCreator(null, 0, 0, false, null, null);
            Misc.setSalvageSpecial(ship, creator.createSpecial(ship, null));
        }
    }

    public static void clearDeepHyper(SectorEntityToken entity, float radius) {
        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);

        float minRadius = plugin.getTileSize() * 2f;
        editor.clearArc(entity.getLocation().x, entity.getLocation().y, 0, radius + minRadius * 0.5f, 0, 360f);
        editor.clearArc(entity.getLocation().x, entity.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);
    }
}





