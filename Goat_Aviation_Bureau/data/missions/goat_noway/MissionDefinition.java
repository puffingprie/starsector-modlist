package data.missions.goat_noway;

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
		api.initFleet(FleetSide.ENEMY, "XXX", FleetGoal.ATTACK, true);

//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.COORDINATED_MANEUVERS, 3);
//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.ELECTRONIC_WARFARE, 3);
		
		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "The Butcher");
		api.setFleetTagline(FleetSide.ENEMY, "Lambs");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Leave none alive.");
		
		boolean testMode = false;
		// Set up the player's fleet.  Variant names come from the
		// files in data/variants and data/variants/fighters
		//api.addToFleet(FleetSide.PLAYER, "station_small_Standard", FleetMemberType.SHIP, "Test Station", false);
		if (!testMode) {
			api.addToFleet(FleetSide.PLAYER, "goat_stormlight_variant", FleetMemberType.SHIP, "GAB Butcher", true);
			
			
			// Set up the enemy fleet.

		api.addToFleet(FleetSide.ENEMY, "eradicator_Overdriven", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "eradicator_Overdriven", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "falcon_xiv_Escort", FleetMemberType.SHIP, false);
			
		api.addToFleet(FleetSide.ENEMY, "enforcer_XIV_Elite", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "enforcer_XIV_Elite", FleetMemberType.SHIP, false);
		
		api.addToFleet(FleetSide.ENEMY, "condor_Attack", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "condor_Attack", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "condor_Support", FleetMemberType.SHIP, false);
		
		api.addToFleet(FleetSide.ENEMY, "lasher_Standard", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "lasher_Assault", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "lasher_Standard", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "lasher_Assault", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "lasher_Assault", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "vanguard_Attack", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "vanguard_Strike", FleetMemberType.SHIP, false);
			
			api.defeatOnShipLoss("GAB Butcher");
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
		float width = 12000f;
		float height = 15000f;
		
		if (testMode) {
			width += 6000;
			height += 8000;
		}
		
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		api.addObjective(minX + width * 0.3f - 4000, minY + height * 0.3f, "nav_buoy");
		
		api.addObjective(minX + width * 0.8f + 2000, minY + height * 0.3f, "nav_buoy");
		
		// Add an asteroid field
		api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);
		 api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);
		
		api.addPlanet(0, 0, 50f, StarTypes.BLACK_HOLE, 500f, true);
		api.addPlanet(1111, 1115, 40f, StarTypes.BLACK_HOLE, 5900f, true);
		
	}

}
