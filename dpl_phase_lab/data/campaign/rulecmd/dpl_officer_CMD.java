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
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI.SurveyLevel;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.campaign.econ.MonthlyReport;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.OfficerDataAPI;
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
import com.fs.starfarer.api.impl.campaign.intel.bases.LuddicPathBaseIntel;
import com.fs.starfarer.api.impl.campaign.missions.RecoverAPlanetkiller;
import com.fs.starfarer.api.impl.campaign.rulecmd.AddRemoveCommodity;
import com.fs.starfarer.api.impl.campaign.rulecmd.AddShip;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import com.fs.starfarer.api.impl.campaign.submarkets.StoragePlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Misc.Token;

/**
 * 
 *	dpl_ptrs_CMD <action> <parameters>
 */
public class dpl_officer_CMD extends BaseCommandPlugin {

	public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Token> params, Map<String, MemoryAPI> memoryMap) {
		if (dialog == null) return false;
		
		OptionPanelAPI options = dialog.getOptionPanel();
		TextPanelAPI text = dialog.getTextPanel();	
		
		String action = params.get(0).getString(memoryMap);
		
		MemoryAPI memory = memoryMap.get(MemKeys.LOCAL);
		if (memory == null) return false; // should not be possible unless there are other big problems already
				
		if ("EllyJoins".equals(action)) {
			PersonAPI elly_lovelace = Global.getSector().getImportantPeople().getData("elly_lovelace").getPerson();
			CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
			
			if (elly_lovelace != null) {
				playerFleet.getFleetData().addOfficer(elly_lovelace);
				Misc.setMercHiredNow(elly_lovelace);
				AddRemoveCommodity.addOfficerGainText(elly_lovelace, text);
			}
		} else if ("CheckElly".equals(action)) {
			PersonAPI elly_lovelace = Global.getSector().getImportantPeople().getData("elly_lovelace").getPerson();
			CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
			
			if (elly_lovelace != null) {
				List<OfficerDataAPI> mercs = Misc.getMercs(playerFleet);
				if (mercs.isEmpty()) return false;
				
				for (OfficerDataAPI od : mercs) {
					if (od.getPerson().equals(elly_lovelace)) {
						return true;
					}
				}
				return false;
			}
			return false;
			
		} else if ("EllyLeaves".equals(action)) {
			PersonAPI elly_lovelace = Global.getSector().getImportantPeople().getData("elly_lovelace").getPerson();
			dialog.getInteractionTarget().setActivePerson(null);
			CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
			
			if (elly_lovelace != null) {
				FleetMemberAPI member = playerFleet.getFleetData().getMemberWithCaptain(elly_lovelace);
				if (member != null) {
					member.setCaptain(null);
				}
				playerFleet.getFleetData().removeOfficer(elly_lovelace);
				AddRemoveCommodity.addOfficerLossText(elly_lovelace, text);
			}

		}
		return false;
	}
	
}
