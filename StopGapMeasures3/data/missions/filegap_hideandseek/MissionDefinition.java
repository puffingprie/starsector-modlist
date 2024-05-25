package data.missions.filegap_hideandseek;

import java.util.List;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		
		// Set up the fleets so we can add ships and fighter wings to them.
		// In this scenario, the fleets are attacking each other, but
		// in other scenarios, a fleet may be defending or trying to escape
		api.initFleet(FleetSide.PLAYER, "TTS", FleetGoal.ATTACK, false, 5);
		api.initFleet(FleetSide.ENEMY, "PLS", FleetGoal.ATTACK, true);

		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "Tri-Tachyon strike force Ambrose");
		api.setFleetTagline(FleetSide.ENEMY, "Maysura patrol squadron");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Defeat all enemy forces");
		
		// Set up the player's fleet.  Variant names come from the
		// files in data/variants and data/variants/fighters

		api.addToFleet(FleetSide.PLAYER, "filgap_orthus_Attack", FleetMemberType.SHIP, "TTS Ancient Danger", false);
		api.addToFleet(FleetSide.PLAYER, "filgap_orthus_Attack", FleetMemberType.SHIP, "TTS Ancient Danger", false);
		api.addToFleet(FleetSide.PLAYER, "filgap_pyrrhocorax_Standard", FleetMemberType.SHIP, "TTS Beholder", false);
		api.addToFleet(FleetSide.PLAYER, "tempest_Attack", FleetMemberType.SHIP, "TTS Excellence", true);
		api.addToFleet(FleetSide.PLAYER, "wolf_Assault", FleetMemberType.SHIP, "TTS Persuasion", true);
		api.addToFleet(FleetSide.PLAYER, "wolf_Assault", FleetMemberType.SHIP, "TTS Hostility", true);




		
		// Set up the enemy fleet.

		api.addToFleet(FleetSide.ENEMY, "filgap_wagner_Standard", FleetMemberType.SHIP, "PLS Contemplation", true);
		api.addToFleet(FleetSide.ENEMY, "filgap_herder_Attack", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "filgap_velite_Standard", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "filgap_velite_Standard", FleetMemberType.SHIP, false);


		
		
		// Set up the map.
		float width = 24000f;
		float height = 18000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		api.addNebula(minX + width * 0.5f - 300, minY + height * 0.5f, 1000);
		api.addNebula(minX + width * 0.5f + 300, minY + height * 0.5f, 1000);
		
		for (int i = 0; i < 5; i++) {
			float x = (float) Math.random() * width - width/2;
			float y = (float) Math.random() * height - height/2;
			float radius = 100f + (float) Math.random() * 400f; 
			api.addNebula(x, y, radius);
		}
		
		// Add an asteroid field
		api.addAsteroidField(minX + width/2f, minY + height/2f, 0, 8000f,
								20f, 70f, 100);
		
		api.addPlugin(new BaseEveryFrameCombatPlugin() {
			public void init(CombatEngineAPI engine) {
				engine.getContext().setStandoffRange(6000f);
			}
			public void advance(float amount, List events) {
			}
		});
		
	}

}




