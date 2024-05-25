package data.scripts.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.PlanetConditionGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import org.lazywizard.lazylib.MathUtils;

public class UNSCStar {

   public void generate(SectorAPI sector) {
			StarSystemAPI system = sector.createStarSystem("Audere");
			system.getLocation().set(23117,10777); // Is this enough 7's to please the ghost of Bungie
			PlanetAPI audere_star = system.initStar("UNSCStar",
					"star_red_dwarf",//set star type, the type IDs come from starsector-core/data/campaign/procgen/star_gen_data.csv
					700, //set radius, 900 is a typical radius size
					750); //radius of corona terrain around star
				
	final float new_harvest_distance = 3000;
	final float ordelia_distance = 4000;
	final float ice_ring_distance = 6000;
	final float ostia_distance = 7000;
	
	system.addRingBand(audere_star,"misc" , "rings_ice0" , 500, 0, Color.yellow, 500, ice_ring_distance-150, 150);
    system.addRingBand(audere_star,"misc" , "rings_ice0" , 80, 0, Color.green, 80, ice_ring_distance+150, 150);
	
	final float new_harvest_angle = 360 * (float) Math.random();
	PlanetAPI new_harvest = system.addPlanet("new_harvest", //unique id
                audere_star, //orbiting target
                "New Harvest", //name
                "frozen", //set planet type, the type IDs come from starsector-core/data/campaign/procgen/planet_gen_data.csv
                new_harvest_angle, //angle
                160f, //radius
                new_harvest_distance, //distance from orbiting target
                280f); //orbit days
	new_harvest.setCustomDescriptionId("unscnewharvest"); //reference descriptions.csv
	MarketAPI new_harvest_market = UNSC_AddMarketplace.addMarketplace("unsc", new_harvest, null,
			"New Harvest",
			6,
			new ArrayList<String>(
					Arrays.asList(
							Conditions.POPULATION_6,
							Conditions.ORE_SPARSE,
							Conditions.RARE_ORE_SPARSE,
							Conditions.ORGANICS_PLENTIFUL,
							Conditions.COLD,
							Conditions.FARMLAND_POOR,
							Conditions.HABITABLE,
							Conditions.EXTREME_WEATHER
					)
			),
			new ArrayList<String>(
					Arrays.asList(
							Submarkets.SUBMARKET_OPEN,
							Submarkets.GENERIC_MILITARY,
							Submarkets.SUBMARKET_BLACK,
							Submarkets.SUBMARKET_STORAGE
					)
			),
			new ArrayList<String>(
					Arrays.asList(
							Industries.POPULATION,
							Industries.MEGAPORT,
							Industries.MINING,
							Industries.FARMING,
							Industries.LIGHTINDUSTRY,
							Industries.STARFORTRESS_MID,
							Industries.HEAVYBATTERIES,
							Industries.HIGHCOMMAND,
							Industries.WAYSTATION
					)
			),
			true,
			false);
	Industry newharvestfort = new_harvest.getMarket().getIndustry(Industries.STARFORTRESS_MID); 
	newharvestfort.setAICoreId("alpha_core");
	Industry newharvestcommand = new_harvest.getMarket().getIndustry(Industries.HIGHCOMMAND); 
	newharvestcommand.setAICoreId("alpha_core");
	
	new_harvest.setInteractionImage("illustrations", "unscnewharvest");
	
	JumpPointAPI jumpPointHarvest = Global.getFactory().createJumpPoint("unscharvest_jump", "New Harvest Jump Point");
	jumpPointHarvest.setCircularOrbit(audere_star, new_harvest_angle+15, new_harvest_distance, 400);
	jumpPointHarvest.setRelatedPlanet(new_harvest);
	
	system.addEntity(jumpPointHarvest);
	
	float ordelia_angle= 360 * (float) Math.random();
	PlanetAPI ordelia = system.addPlanet("ordelia", 
                audere_star, 
                "Ordelia", 
                "barren",
                ordelia_angle ,
                120, 
                ordelia_distance,
                320);
	ordelia.setCustomDescriptionId("unscordelia"); //reference descriptions.csv
	MarketAPI ordelia_market = UNSC_AddMarketplace.addMarketplace("unsc", ordelia, null,
			"Ordelia",
			5,
			new ArrayList<String>(
					Arrays.asList(
							Conditions.POPULATION_5,
							Conditions.COLD,
							Conditions.POOR_LIGHT,
							Conditions.THIN_ATMOSPHERE,
							Conditions.ORE_MODERATE,
							Conditions.RARE_ORE_ABUNDANT
					)
			),
			new ArrayList<String>(
					Arrays.asList(
							Submarkets.SUBMARKET_OPEN,
							Submarkets.SUBMARKET_BLACK,
							Submarkets.SUBMARKET_STORAGE
					)
			),
			new ArrayList<String>(
					Arrays.asList(
							Industries.POPULATION,
							Industries.SPACEPORT,
							Industries.WAYSTATION,
							Industries.MINING,
							Industries.REFINING,
							Industries.BATTLESTATION_MID,
							Industries.HEAVYBATTERIES,
							Industries.PATROLHQ
					)
			),
			true,
			false);
	ordelia_market.addIndustry(Industries.ORBITALWORKS,new ArrayList<String>(Arrays.asList(Items.CORRUPTED_NANOFORGE)));
	Industry ordeliafort = ordelia.getMarket().getIndustry(Industries.BATTLESTATION_MID); 
	ordeliafort.setAICoreId("alpha_core");
	
	ordelia.setInteractionImage("illustrations", "unscordelia");
	
	PlanetAPI ostia = system.addPlanet("ostia", //unique id
                audere_star, //orbiting target
                "Ostia", //name
                "ice_giant", //set planet type, the type IDs come from starsector-core/data/campaign/procgen/planet_gen_data.csv
                360 * (float) Math.random(), //angle
                275f, //radius
                ostia_distance, //distance from orbiting target
                517f); //orbit days
	PlanetConditionGenerator.generateConditionsForPlanet(ostia, StarAge.AVERAGE);
	
	SectorEntityToken ostiastation = system.addCustomEntity("ostiastation", "Ostia Station", "station_midline2", "unsc");
	ostiastation.setCircularOrbitPointingDown(ostia, 0, 300, 117);
	ostiastation.setCustomDescriptionId("unscostiastation"); //reference descriptions.csv
	MarketAPI ostiastation_market = UNSC_AddMarketplace.addMarketplace("unsc", ostiastation, null,
			"Ostia Mining Station",
			4,
			new ArrayList<String>(
					Arrays.asList(
							Conditions.POPULATION_4,
							Conditions.VOLATILES_ABUNDANT
					)
			),
			new ArrayList<String>(
					Arrays.asList(
							Submarkets.SUBMARKET_OPEN,
							Submarkets.SUBMARKET_BLACK,
							Submarkets.SUBMARKET_STORAGE
					)
			),
			new ArrayList<String>(
					Arrays.asList(
							Industries.POPULATION,
							Industries.SPACEPORT,
							Industries.WAYSTATION,
							Industries.MINING,
							Industries.FUELPROD,
							Industries.BATTLESTATION_MID,
							Industries.GROUNDDEFENSES,
							Industries.PATROLHQ
					)
			),
			true,
			false);
			
	ostiastation.setInteractionImage("illustrations", "unscostia");
	
	system.autogenerateHyperspaceJumpPoints(true, true);
	
	SectorEntityToken relay = system.addCustomEntity("audere_relay", // unique id
		"Audere Relay", // name - if null, defaultName from custom_entities.json will be used
		"comm_relay_makeshift", // type of object, defined in custom_entities.json
		"unsc"); // faction
	relay.setCircularOrbit( audere_star, MathUtils.clampAngle(new_harvest_angle - 65), new_harvest_distance+250, 500f);

	//set up hyperspace editor plugin
	HyperspaceTerrainPlugin hyperspaceTerrainPlugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin(); //get instance of hyperspace terrain
	NebulaEditor nebulaEditor = new NebulaEditor(hyperspaceTerrainPlugin); //object used to make changes to hyperspace nebula

	//set up radiuses in hyperspace of system
	float minHyperspaceRadius = hyperspaceTerrainPlugin.getTileSize() * 2f; //minimum radius is two 'tiles'
	float maxHyperspaceRadius = system.getMaxRadiusInHyperspace();

	//hyperstorm-b-gone (around system in hyperspace)
	nebulaEditor.clearArc(system.getLocation().x, system.getLocation().y, 0, minHyperspaceRadius + maxHyperspaceRadius, 0f, 360f, 0.25f);

    }



}
