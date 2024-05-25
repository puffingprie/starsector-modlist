package data.missions.goat_battlo;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.BattleObjectives;
import com.fs.starfarer.api.impl.campaign.ids.StarTypes;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets so we can add ships and fighter wings to them.
		// In this scenario, the fleets are attacking each other, but
		// in other scenarios, a fleet may be defending or trying to escape
		api.initFleet(FleetSide.PLAYER, "GAB ", FleetGoal.ATTACK, false);
		api.initFleet(FleetSide.ENEMY, "HSS", FleetGoal.ATTACK, true);

//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.COORDINATED_MANEUVERS, 3);
//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.ELECTRONIC_WARFARE, 3);
		
		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "3rd Fire of Desire");
		api.setFleetTagline(FleetSide.ENEMY, "Hegemony 11th Suppression Fleet");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Sunlance must survive.");
		api.addBriefingItem("Do not fight the enemy directly, lest you be crushed by thermal pulses!");
		api.addBriefingItem("Your flagship can outmaneuver any enemy.");
		
		boolean testMode = false;
		// Set up the player's fleet.  Variant names come from the
		// files in data/variants and data/variants/fighters
		//api.addToFleet(FleetSide.PLAYER, "station_small_Standard", FleetMemberType.SHIP, "Test Station", false);
		if (!testMode) {
			api.addToFleet(FleetSide.PLAYER, "goat_rainfarewell_Super", FleetMemberType.SHIP, "GOA Sunlance", true);
			
			api.addToFleet(FleetSide.PLAYER, "goat_ironpavilion_Assault", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_ironpavilion_Assault", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_midnight_book_Assault", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_midnight_book_Assault", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_rainfarewell_Assault", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_rainfarewell_Assault", FleetMemberType.SHIP, false);
			
			api.addToFleet(FleetSide.PLAYER, "goat_gentiles_Assault", FleetMemberType.SHIP, false);
			
			api.addToFleet(FleetSide.PLAYER, "goat_pathogenic_wind_Assault", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_pathogenic_wind_Fire_Support", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_fliegender_stern_Assault", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_fliegender_stern_Fire_Support", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_fliegender_stern_Fire_Support", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_arson_bird_Strike", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_arson_bird_Super", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_arson_bird_Strike", FleetMemberType.SHIP, false);
			api.addToFleet(FleetSide.PLAYER, "goat_arson_bird_Assault", FleetMemberType.SHIP, false);

			
			// Set up the enemy fleet.
		api.addToFleet(FleetSide.ENEMY, "onslaught_Outdated", FleetMemberType.SHIP, "HSS Repressor", false);
			
		api.addToFleet(FleetSide.ENEMY, "dominator_XIV_Elite", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "eradicator_Overdriven", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "eradicator_Overdriven", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "eradicator_Overdriven", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "eradicator_Overdriven", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "falcon_xiv_Escort", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "falcon_xiv_Escort", FleetMemberType.SHIP, false);
		
		api.addToFleet(FleetSide.ENEMY, "enforcer_Assault", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "manticore_Assault", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "manticore_Assault", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "enforcer_XIV_Elite", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "enforcer_XIV_Elite", FleetMemberType.SHIP, false);
		
		api.addToFleet(FleetSide.ENEMY, "condor_Attack", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "condor_Support", FleetMemberType.SHIP, false);
		
		api.addToFleet(FleetSide.ENEMY, "lasher_Standard", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "lasher_Assault", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "lasher_Assault", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "lasher_Assault", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "lasher_Assault", FleetMemberType.SHIP, false);
			
			api.defeatOnShipLoss("Stranger II");
		}
		
		if (testMode) {
//			FleetMemberAPI member = api.addToFleet(FleetSide.PLAYER, "omen_PD", FleetMemberType.SHIP, "Milk Run", true);
//			member.getCaptain().getStats().setSkillLevel(Skills.IMPACT_MITIGATION, 2);
//			member.getCaptain().getStats().setSkillLevel(Skills.SHIELD_MODULATION, 2);
//			member.getCaptain().getStats().setSkillLevel(Skills.HELMSMANSHIP, 2);
			
			api.addToFleet(FleetSide.PLAYER, "falcon_Attack", FleetMemberType.SHIP, "Stranger II", true);
	//		PersonAPI person = new AICoreOfficerPluginImpl().createPerson(Commodities.ALPHA_CORE, null, null);
	//		member.setCaptain(person);
			
			api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, "Cherenkov Bloom", false);
			api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, null, false);
			api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, null, false);
			
			api.addObjective(0, 4000, BattleObjectives.SENSOR_JAMMER);
			api.addObjective(4000, 0, BattleObjectives.COMM_RELAY);
			api.addObjective(-3000, -2000, BattleObjectives.NAV_BUOY);
		}
		
		// Set up the map.
		float width = 18000f;
		float height = 16000f;
		
		if (testMode) {
			width += 6000;
			height += 8000;
		}
		
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		api.addObjective(minX + width * 0.3f + 1000, minY + height * 0.3f, "nav_buoy");
		api.addObjective(minX + width * 0.3f + 2000, minY + height * 0.7f, "sensor_array");
		api.addObjective(minX + width * 0.7f + 2000, minY + height * 0.5f, "nav_buoy");
		
		// Add an asteroid field
		api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);
		 api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);
		
		api.addPlanet(0, 0, 50f, StarTypes.BLACK_HOLE, 500f, true);
		
	}

}
