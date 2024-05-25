//By VladimirVV. Implements the active skill that decreases enemy CR.
package data.scripts.campaign;

import java.awt.Color;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ExplosionEntityPlugin.ExplosionFleetDamage;
import com.fs.starfarer.api.impl.campaign.ExplosionEntityPlugin.ExplosionParams;
import com.fs.starfarer.api.impl.campaign.GateEntityPlugin;
import com.fs.starfarer.api.impl.campaign.JumpPointInteractionDialogPluginImpl;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.util.FaderUtil;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.JitterUtil;
import com.fs.starfarer.api.util.Misc;

public class dpl_relay_explosion_plugin implements EveryFrameScript {

	public static class SystemCutOffRemoverScript implements EveryFrameScript {
		public StarSystemAPI system;
		public IntervalUtil interval = new IntervalUtil(0.5f, 1.5f);
		public boolean done;
		public float elapsed = 0f;
		
		public SystemCutOffRemoverScript(StarSystemAPI system) {
			super();
			this.system = system;
		}
		public boolean isDone() {
			return done;
		}
		public boolean runWhilePaused() {
			return false;
		}

		public void advance(float amount) {
			if (done) return;
			
			float days = Global.getSector().getClock().convertToDays(amount);
			elapsed += days;
			interval.advance(days);
			if (interval.intervalElapsed() && elapsed > 10f) { // make sure gate's already exploded
				boolean allJPUsable = true;
				boolean anyJPUsable = false;
				for (SectorEntityToken jp : system.getJumpPoints()) {
					allJPUsable &= !jp.getMemoryWithoutUpdate().getBoolean(JumpPointInteractionDialogPluginImpl.UNSTABLE_KEY);
					anyJPUsable |= !jp.getMemoryWithoutUpdate().getBoolean(JumpPointInteractionDialogPluginImpl.UNSTABLE_KEY);
				}
				//if (allJPUsable) {
				if (anyJPUsable) {
					system.removeTag(Tags.SYSTEM_CUT_OFF_FROM_HYPER);
					done = true;
				}
			}
		}
		
	}
	
	public static float UNSTABLE_DAYS_MIN = 200;
	public static float UNSTABLE_DAYS_MAX = 400;
	
	protected boolean done = false;
	protected boolean playedWindup = false;
	protected SectorEntityToken explosion = null;
	protected float delay = 0.5f;
	protected float delay2 = 1f;
	
	protected SectorEntityToken gate;
	
	
	public dpl_relay_explosion_plugin(SectorEntityToken gate) {
		this.gate = gate;
		//GateEntityPlugin plugin = (GateEntityPlugin) gate.getCustomPlugin();
		
		// do this immediately so player can't establish a colony between when the gate explosion begins
		// and when it ends
		StarSystemAPI system = gate.getStarSystem();
		if (system != null) {
			system.addTag(Tags.SYSTEM_CUT_OFF_FROM_HYPER);
			system.addScript(new SystemCutOffRemoverScript(system));
		}
		
		delay = 3.2f; // plus approximately 2 seconds from how long plugin.jitter() takes to build up
		
	}

	public void advance(float amount) {
		if (done) return;
		
		if (!playedWindup) {
			if (gate.isInCurrentLocation()) {
				Global.getSoundPlayer().playSound("gate_explosion_windup", 1f, 1f, gate.getLocation(), Misc.ZERO);
			}
			playedWindup = true;
		}
		
		delay -= amount;
		LocationAPI cl = gate.getContainingLocation();
		Vector2f loc = gate.getLocation();
		Vector2f vel = gate.getVelocity();
		
		if (delay > 0) {
			Vector2f finalVel = new Vector2f();
			finalVel.x = vel.x + (float) (2*Math.random()-1)*500;
			finalVel.y = vel.y + (float) (2*Math.random()-1)*500;
					
			cl.addParticle(loc, finalVel, 5f, 255f, 0f, 1f, new Color(165, 100, 255));
		}
		
		if (delay <= 0 && explosion == null) {
			//Misc.fadeAndExpire(gate);

			//LocationAPI cl = gate.getContainingLocation();
			//Vector2f loc = gate.getLocation();
			//Vector2f vel = gate.getVelocity();
			
			float size = gate.getRadius() + 800f;
			Color color = new Color(165, 100, 255);
			color = new Color(150, 100, 255, 255);
			//color = new Color(255, 155, 255);
			//ExplosionParams params = new ExplosionParams(color, cl, loc, size, 1f);
			ExplosionParams params = new ExplosionParams(color, cl, loc, size, 2f);
			params.damage = ExplosionFleetDamage.NONE;
			
			explosion = cl.addCustomEntity(Misc.genUID(), "Gate Explosion", 
											Entities.EXPLOSION, Factions.NEUTRAL, params);
			explosion.setLocation(loc.x, loc.y);
			if (!(gate instanceof CampaignFleetAPI)) {
				SectorEntityToken built = cl.addCustomEntity(null,
						 null,
				Entities.STABLE_LOCATION, // type of object, defined in custom_entities.json
				Factions.NEUTRAL); // faction
				if (gate.getOrbit() != null) {
				built.setOrbit(gate.getOrbit().makeCopy());
				}
				cl.removeEntity(gate);
				updateOrbitingEntities(cl, gate, built);
			} else {
				CampaignFleetAPI fleet = (CampaignFleetAPI) gate;
				List<FleetMemberAPI> allMembers = fleet.getFleetData().getMembersListCopy();
				for (FleetMemberAPI ship : allMembers) {
					ship.getRepairTracker().setCR(0f);
				}
			}
		}
		
		if (explosion != null) {
			delay2 -= amount;
			if (!explosion.isAlive() || delay2 <= 0) {
				done = true;
				
				StarSystemAPI system = gate.getStarSystem();
				if (system != null) {
					for (SectorEntityToken jp : system.getJumpPoints()) {
						float days = UNSTABLE_DAYS_MIN + (UNSTABLE_DAYS_MAX - UNSTABLE_DAYS_MIN) * (float) Math.random();
						jp.getMemoryWithoutUpdate().set(JumpPointInteractionDialogPluginImpl.UNSTABLE_KEY, true, days);
					}
				}
			}
		}
	}
	
	public void updateOrbitingEntities(LocationAPI loc, SectorEntityToken prev, SectorEntityToken built) {
		if (loc == null) return;
		for (SectorEntityToken other : loc.getAllEntities()) {
			if (other == prev) continue;
			if (other.getOrbit() == null) continue;
			if (other.getOrbitFocus() == prev) {
				other.setOrbitFocus(built);
			}
		}
	}
	
	public boolean isDone() {
		return done;
	}

	public boolean runWhilePaused() {
		return false;
	}
	
}




