package data.campaign.rulecmd;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI.SurveyLevel;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.People;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.intel.bases.LuddicPathBaseIntel;
import com.fs.starfarer.api.impl.campaign.missions.RecoverAPlanetkiller;
import com.fs.starfarer.api.impl.campaign.rulecmd.AddShip;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import com.fs.starfarer.api.impl.campaign.submarkets.StoragePlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Misc.Token;

/**
 * 
 *	dpl_colony_CMD <action> <parameters>
 */
public class dpl_colony_CMD extends BaseCommandPlugin {

	public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Token> params, Map<String, MemoryAPI> memoryMap) {
		if (dialog == null) return false;
		
		OptionPanelAPI options = dialog.getOptionPanel();
		TextPanelAPI text = dialog.getTextPanel();
		CampaignFleetAPI pf = Global.getSector().getPlayerFleet();
		CargoAPI cargo = pf.getCargo();
		
		
		String action = params.get(0).getString(memoryMap);
		
		MemoryAPI memory = memoryMap.get(MemKeys.LOCAL);
		if (memory == null) return false; // should not be possible unless there are other big problems already
				
		if ("giveSaxophone".equals(action)) {
			giveSaxophone(dialog, memoryMap);
		} else if ("givePiano".equals(action)) {
			givePiano(dialog, memoryMap);
		} else if ("giveSilence".equals(action)) {
			giveSilence(dialog, memoryMap);
		} else if ("giveDirge".equals(action)) {
			giveDirge(dialog, memoryMap);
		} else if ("giveLyre".equals(action)) {
			giveLyre(dialog, memoryMap);
		} else if ("hasIndustrial".equals(action)) {
			for (MarketAPI market: Misc.getPlayerMarkets(false)) {
				for (Industry ind : market.getIndustries()) {
					if (ind.getSpec().hasTag(Industries.TAG_HEAVYINDUSTRY)) {
						return true;
					}
				}
			}
			return false;
		} else if ("hasMilitary".equals(action)) {
			for (MarketAPI market: Misc.getPlayerMarkets(false)) {
				for (Industry ind : market.getIndustries()) {
					if (ind.getSpec().hasTag(Industries.TAG_MILITARY) || ind.getSpec().hasTag(Industries.TAG_COMMAND)) {
						return true;
					}
				}
			}
			return false;
		} else if ("labLearnOrchestra".equals(action)) {
			FactionAPI faction = Global.getSector().getFaction("dpl_phase_lab");
			String id = "dpl_orchestra";
			faction.clearShipRoleCache();
			if (!faction.knowsShip(id)) {
				faction.addKnownShip(id, true);
				faction.addUseWhenImportingShip(id);
				faction.getHullFrequency().put(id, 0.3f);
			} ;
		} else if ("giveOrchestra".equals(action)) {
			giveOrchestra(dialog, memoryMap);
		} else if ("labLearnOrchestraB".equals(action)) {
			FactionAPI faction = Global.getSector().getFaction("dpl_phase_lab");
			String id = "dpl_orchestraB";
			faction.clearShipRoleCache();
			if (!faction.knowsShip(id)) {
				faction.addKnownShip(id, true);
				faction.addUseWhenImportingShip(id);
				faction.getHullFrequency().put(id, 0.3f);
			} ;
		} else if ("giveOrchestraB".equals(action)) {
			giveOrchestraB(dialog, memoryMap);
		} else if ("labLearnOrchestraC".equals(action)) {
			FactionAPI faction = Global.getSector().getFaction("dpl_phase_lab");
			String id = "dpl_orchestraC";
			faction.clearShipRoleCache();
			if (!faction.knowsShip(id)) {
				faction.addKnownShip(id, true);
				faction.addUseWhenImportingShip(id);
				faction.getHullFrequency().put(id, 0.3f);
			} ;
		} else if ("giveOrchestraC".equals(action)) {
			giveOrchestraC(dialog, memoryMap);
		}
		return false;
	}

	protected void giveSaxophone(InteractionDialogAPI dialog, Map<String, MemoryAPI> memoryMap) {
		
		ShipVariantAPI v = Global.getSettings().getVariant("dpl_saxophone_Hull").clone();
		FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, v);
		Global.getSector().getPlayerFleet().getFleetData().addFleetMember(member);
		AddShip.addShipGainText(member, dialog.getTextPanel());
	}
	
	protected void givePiano(InteractionDialogAPI dialog, Map<String, MemoryAPI> memoryMap) {
		
		ShipVariantAPI v = Global.getSettings().getVariant("dpl_piano_Hull").clone();
		v.addTag(Tags.SHIP_CAN_NOT_SCUTTLE);
		FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, v);
		Global.getSector().getPlayerFleet().getFleetData().addFleetMember(member);
		AddShip.addShipGainText(member, dialog.getTextPanel());
	}
	
	protected void giveSilence(InteractionDialogAPI dialog, Map<String, MemoryAPI> memoryMap) {
		
		ShipVariantAPI v = Global.getSettings().getVariant("dpl_silence_Hull").clone();
		v.addTag(Tags.SHIP_CAN_NOT_SCUTTLE);
		FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, v);
		Global.getSector().getPlayerFleet().getFleetData().addFleetMember(member);
		AddShip.addShipGainText(member, dialog.getTextPanel());
	}
	
	protected void giveDirge(InteractionDialogAPI dialog, Map<String, MemoryAPI> memoryMap) {
		
		ShipVariantAPI v = Global.getSettings().getVariant("dpl_dirge_Hull").clone();
		v.addTag(Tags.SHIP_CAN_NOT_SCUTTLE);
		FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, v);
		Global.getSector().getPlayerFleet().getFleetData().addFleetMember(member);
		AddShip.addShipGainText(member, dialog.getTextPanel());
	}

	protected void giveLyre(InteractionDialogAPI dialog, Map<String, MemoryAPI> memoryMap) {
	
	ShipVariantAPI v = Global.getSettings().getVariant("dpl_lyre_Hull").clone();
	v.addTag(Tags.SHIP_CAN_NOT_SCUTTLE);
	FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, v);
	Global.getSector().getPlayerFleet().getFleetData().addFleetMember(member);
	AddShip.addShipGainText(member, dialog.getTextPanel());
	}
	
	protected void giveOrchestra(InteractionDialogAPI dialog, Map<String, MemoryAPI> memoryMap) {
		
		ShipVariantAPI v = Global.getSettings().getVariant("dpl_orchestra_Hull").clone();
		v.addTag(Tags.SHIP_CAN_NOT_SCUTTLE);
		FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, v);
		Global.getSector().getPlayerFleet().getFleetData().addFleetMember("dpl_orchestra_blank");
		AddShip.addShipGainText(member, dialog.getTextPanel());
	}
	
	protected void giveOrchestraB(InteractionDialogAPI dialog, Map<String, MemoryAPI> memoryMap) {
		
		ShipVariantAPI v = Global.getSettings().getVariant("dpl_orchestraB_Hull").clone();
		v.addTag(Tags.SHIP_CAN_NOT_SCUTTLE);
		FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, v);
		Global.getSector().getPlayerFleet().getFleetData().addFleetMember("dpl_orchestraB_blank");
		AddShip.addShipGainText(member, dialog.getTextPanel());
	}
	
	protected void giveOrchestraC(InteractionDialogAPI dialog, Map<String, MemoryAPI> memoryMap) {
		
		ShipVariantAPI v = Global.getSettings().getVariant("dpl_orchestraC_Hull").clone();
		v.addTag(Tags.SHIP_CAN_NOT_SCUTTLE);
		FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, v);
		Global.getSector().getPlayerFleet().getFleetData().addFleetMember("dpl_orchestraC_blank");
		AddShip.addShipGainText(member, dialog.getTextPanel());
	}
	
}
