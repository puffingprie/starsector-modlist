package data.missions.goat_painting;

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
		api.initFleet(FleetSide.PLAYER, "GAB", FleetGoal.ATTACK, false);
		api.initFleet(FleetSide.ENEMY, "ISS", FleetGoal.ATTACK, true);

//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.COORDINATED_MANEUVERS, 3);
//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.ELECTRONIC_WARFARE, 3);
		
		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "Painted ships");
		api.setFleetTagline(FleetSide.ENEMY, "Unsuspecting originals");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("This is a test mission.");
		api.addBriefingItem("Show them the true meaning of pain...t.");
		
		boolean testMode = false;
		// Set up the player's fleet.  Variant names come from the
		// files in data/variants and data/variants/fighters
		//api.addToFleet(FleetSide.PLAYER, "station_small_Standard", FleetMemberType.SHIP, "Test Station", false);
		if (!testMode) {
			
			api.addToFleet(FleetSide.PLAYER, "paragon_Nature", FleetMemberType.SHIP,  true);
			
			api.addToFleet(FleetSide.PLAYER, "hammerhead_Nature", FleetMemberType.SHIP, false);
			
			api.addToFleet(FleetSide.PLAYER, "colossus_goat_painting_nature", FleetMemberType.SHIP, false);

			api.addToFleet(FleetSide.PLAYER, "falcon_goat_painting_nature", FleetMemberType.SHIP, false);

			api.addToFleet(FleetSide.PLAYER, "eagle_goat_painting_nature", FleetMemberType.SHIP, false);

			
			
			
			// Set up the enemy fleet.
			
		api.addToFleet(FleetSide.ENEMY, "paragon_Raider", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "hammerhead_d_CS", FleetMemberType.SHIP, false);;
		api.addToFleet(FleetSide.ENEMY, "colossus_Standard", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "falcon_Attack", FleetMemberType.SHIP, false);;
		api.addToFleet(FleetSide.ENEMY, "eagle_Assault", FleetMemberType.SHIP, false);
		
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
		float width = 16000f;
		float height = 16000f;
		
		if (testMode) {
			width += 4000;
			height += 8000;
		}
		
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		// Add an asteroid field
		api.addObjective(minX + width * 0.25f, minY + 5500, "nav_buoy");
		api.addObjective(minX + width * 0.75f, minY + 5500, "sensor_array");
		
		
		api.addObjective(minX + width * 0.5f, minY + height * 0.5f, "sensor_array");
		
		api.addObjective(minX + width * 0.3f, minY + height * 0.75f, "comm_relay");
		api.addObjective(minX + width * 0.7f, minY + height * 0.7f, "nav_buoy");
		api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);
		
	
		
	}

}
