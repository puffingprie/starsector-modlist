package data.scripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.CampaignPlugin;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.impl.campaign.procgen.ProcgenUsedNames;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.characters.ImportantPeopleAPI;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Personalities;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.intel.bar.events.BarEventManager;
import com.fs.starfarer.api.impl.campaign.intel.deciv.DecivTracker;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.campaign.BaseCampaignPlugin;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.SetPersonHidden;

import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import com.fs.starfarer.api.util.Misc;

import data.scripts.campaign.dpl_campaign_relations_plugin;
import data.scripts.campaign.dpl_exploratory_fleet_plugin;
import data.scripts.campaign.dpl_system_defense_plugin;
import data.scripts.world.systems.dpl_horizon;
import data.scripts.world.systems.dpl_proving_ground;

 
public class dpl_phase_labModPlugin extends BaseModPlugin {
	public static String ELIZA_LOVELACE = "eliza_lovelace";
	public static String NELSON_BONAPARTE = "nelson_bonaparte";
	public static String BANACH_SALAZAR = "banach_salazar";
	public static String VLADIMIR_VASSILIEV = "vladimir_vassiliev";
	public static String ELLY_LOVELACE = "elly_lovelace";
	public static String ROSS_HIGGS = "ross_higgs";
	
	public static void newGenerate(SectorAPI sector) {
		ProcgenUsedNames.notifyUsed("dpl_phase_lab");
		ProcgenUsedNames.notifyUsed("dpl_persean_imperium");
	}
	
	@Override
	public void onNewGame() {SharedData.getData().getPersonBountyEventData().addParticipatingFaction("dpl_phase_lab");
		SectorAPI sector = Global.getSector();
		boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
		StarSystemAPI corvus = sector.getStarSystem("Corvus");
		Global.getSector().addScript(new dpl_exploratory_fleet_plugin());
		Global.getSector().addScript(new dpl_system_defense_plugin());
		if(!haveNexerelin || corvus != null) {
			Global.getSector().addScript(new dpl_campaign_relations_plugin());
			new dpl_proving_ground().generate(sector);
			new dpl_horizon().generate(sector);
		} else {
			new dpl_horizon().generate(sector);
		}
		FactionAPI player = sector.getFaction("player");
		FactionAPI hegemony = sector.getFaction("hegemony");
		FactionAPI tritachyon = sector.getFaction("tritachyon");
		FactionAPI pirates = sector.getFaction("pirates");
		FactionAPI church = sector.getFaction("luddic_church");
		FactionAPI path = sector.getFaction("luddic_path");
		FactionAPI indep = sector.getFaction("independent");
		FactionAPI diktat = sector.getFaction("sindrian_diktat");
		FactionAPI persean = sector.getFaction("persean");
		FactionAPI dpl_phase_lab = sector.getFaction("dpl_phase_lab");
		FactionAPI dpl_persean_imperium = sector.getFaction("dpl_persean_imperium");
		dpl_phase_lab.setRelationship(hegemony.getId(), 0.3f);
		dpl_phase_lab.setRelationship(persean.getId(), 0.1f);
		dpl_phase_lab.setRelationship(church.getId(), 0.0f);
		dpl_phase_lab.setRelationship(tritachyon.getId(), -0.5f);
		dpl_phase_lab.setRelationship(pirates.getId(), -0.5f);
		dpl_phase_lab.setRelationship(path.getId(), -0.5f);
		
		player.setRelationship(dpl_persean_imperium.getId(), -0.5f);
		dpl_persean_imperium.setRelationship(hegemony.getId(), -0.5f);
		dpl_persean_imperium.setRelationship(tritachyon.getId(), -0.5f);
		dpl_persean_imperium.setRelationship(church.getId(), -0.5f);
		dpl_persean_imperium.setRelationship(persean.getId(), -0.5f);
		dpl_persean_imperium.setRelationship(diktat.getId(), -0.5f);
		dpl_persean_imperium.setRelationship(path.getId(), -0.5f);
		dpl_persean_imperium.setRelationship(pirates.getId(), -0.5f);
		dpl_persean_imperium.setRelationship(indep.getId(), -0.5f);
		dpl_persean_imperium.setRelationship(dpl_phase_lab.getId(), -0.5f);
	}
	
    @Override
    public void onNewGameAfterEconomyLoad() {
    	Global.getSettings().resetCached();
        if (Global.getSettings().getMissionScore("dpl_onemanwithcourage") > 0) {
        	Global.getSector().getMemoryWithoutUpdate().set("$dpl_test_pilot", true);
        }
        if (Global.getSettings().getMissionScore("dpl_mothtoaflame") > 0) {
            Global.getSector().getMemoryWithoutUpdate().set("$dpl_exp_drone", true);
        }
        if (Global.getSettings().getMissionScore("dpl_thinredline") > 0) {
            Global.getSector().getMemoryWithoutUpdate().set("$dpl_dirge_access", true);
        }
        if (Global.getSettings().getMissionScore("dpl_ittakesone") > 0) {
            Global.getSector().getMemoryWithoutUpdate().set("$dpl_lyre_access", true);
        }
        MarketAPI sindria = Global.getSector().getEconomy().getMarket("sindria");
        if (sindria != null) {
        	Industry sindrian_fuel = sindria.getIndustry("fuelprod");
        	sindrian_fuel.setImproved(true);
        }
        
        ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
        
    	MarketAPI market1 = Global.getSector().getEconomy().getMarket("dpl_security");
    	if (market1 != null) {
    		Industry highcommand1 = market1.getIndustry("highcommand");
			highcommand1.setImproved(true);
    	}

    	MarketAPI market2 = Global.getSector().getEconomy().getMarket("dpl_factory");
    	if (market2 != null) {
    		market2.addTag("$tag:story_critical");
    		//Generating Ross Higgs.       	
            PersonAPI ross_higgs = Global.getFactory().createPerson();
            ross_higgs.setId(ROSS_HIGGS);
            ross_higgs.setFaction("dpl_phase_lab");
            ross_higgs.setGender(FullName.Gender.FEMALE);
            ross_higgs.setImportance(PersonImportance.VERY_HIGH);
            ross_higgs.setPostId("engineeringChair");
            ross_higgs.setRankId("spaceMarshal");
            ross_higgs.getName().setFirst("Ross");
            ross_higgs.getName().setLast("Higgs");
            ross_higgs.setPortraitSprite(Global.getSettings().getSpriteName("characters", "ross_higgs"));
            market2.getCommDirectory().addPerson(ross_higgs,0);
            market2.addPerson(ross_higgs);
			ip.addPerson(ross_higgs);
			ross_higgs.getMarket().getCommDirectory().getEntryForPerson(ross_higgs.getId()).setHidden(true);
			Industry orbitalworks2 = market2.getIndustry("orbitalworks");
			orbitalworks2.setImproved(true);
    	}
    	
        MarketAPI market3 = Global.getSector().getEconomy().getMarket("dpl_research_site_v");
        if (market3 != null) {
        	//Generating Eliza Storyline boolean values.
        	market3.addTag("$tag:story_critical");
        	market3.getMemoryWithoutUpdate().set("$dpl_metEliza", false);
        	market3.getMemoryWithoutUpdate().set("$dpl_asked_remnant", false);
        	market3.getMemoryWithoutUpdate().set("$dpl_asked_leader", false);
        	market3.getMemoryWithoutUpdate().set("$dpl_asked_age", false);
        	market3.getMemoryWithoutUpdate().set("$dpl_asked_person", false);
        	market3.getMemoryWithoutUpdate().set("$dpl_asked_well", false);
        	market3.getMemoryWithoutUpdate().set("$dpl_asked_luddic", false);
        	market3.getMemoryWithoutUpdate().set("$dpl_asked_colony", false);
        	
        	//Generating Eliza Lovelace.       	
            PersonAPI eliza_lovelace = Global.getFactory().createPerson();
            eliza_lovelace.setId(ELIZA_LOVELACE);
            eliza_lovelace.setFaction("dpl_phase_lab");
            eliza_lovelace.setGender(FullName.Gender.FEMALE);
            eliza_lovelace.setImportance(PersonImportance.VERY_HIGH);
            eliza_lovelace.setPostId(Ranks.POST_FACTION_LEADER);
            eliza_lovelace.setRankId(Ranks.FACTION_LEADER);
            eliza_lovelace.getName().setFirst("Eliza");
            eliza_lovelace.getName().setLast("Lovelace");
            eliza_lovelace.setPortraitSprite(Global.getSettings().getSpriteName("characters", "eliza_lovelace"));
            market3.getCommDirectory().addPerson(eliza_lovelace, 0);
			market3.addPerson(eliza_lovelace);
			ip.addPerson(eliza_lovelace);
			
			//Generating Banach Salazar.       	
            PersonAPI banach_salazar = Global.getFactory().createPerson();
            banach_salazar.setId(BANACH_SALAZAR);
            banach_salazar.setFaction("dpl_phase_lab");
            banach_salazar.setGender(FullName.Gender.MALE);
            banach_salazar.setPostId("seniorCommander");
            banach_salazar.setRankId("spaceMarshal");
            banach_salazar.getName().setFirst("Banach");
            banach_salazar.getName().setLast("Salazar");
            banach_salazar.setPortraitSprite(Global.getSettings().getSpriteName("characters", "banach_salazar"));
            market3.getCommDirectory().addPerson(banach_salazar);
			market3.addPerson(banach_salazar);
			ip.addPerson(banach_salazar);
			banach_salazar.getMarket().getCommDirectory().getEntryForPerson(banach_salazar.getId()).setHidden(true);
			
			banach_salazar.setPersonality(Personalities.STEADY);
			banach_salazar.getStats().setLevel(9);
			banach_salazar.getStats().setSkillLevel(Skills.TARGET_ANALYSIS, 2);
			banach_salazar.getStats().setSkillLevel(Skills.FIELD_MODULATION, 2);
			banach_salazar.getStats().setSkillLevel(Skills.COMBAT_ENDURANCE, 2);
			banach_salazar.getStats().setSkillLevel(Skills.SUPPORT_DOCTRINE, 1);
			banach_salazar.getStats().setSkillLevel(Skills.CARRIER_GROUP, 1);
			banach_salazar.getStats().setSkillLevel(Skills.FIGHTER_UPLINK, 1);
			banach_salazar.getStats().setSkillLevel(Skills.WOLFPACK_TACTICS, 1);
			banach_salazar.getStats().setSkillLevel(Skills.CREW_TRAINING, 1);
			banach_salazar.getStats().setSkillLevel(Skills.TACTICAL_DRILLS, 1);
			banach_salazar.getStats().setSkillLevel(Skills.FLUX_REGULATION, 1);
			banach_salazar.getStats().setSkillLevel(Skills.PHASE_CORPS, 1);
			banach_salazar.getStats().setSkillLevel(Skills.AUTOMATED_SHIPS, 1);
			
			Industry militarybase3 = market3.getIndustry("militarybase");
			militarybase3.setImproved(true);
        } else {
        	MarketAPI largestMarket = null;
        	int size = 0;
        	List<MarketAPI> allMarkets = Global.getSector().getEconomy().getMarketsCopy();
    		for (MarketAPI market : allMarkets) {
    			if (market.getFaction().equals(Global.getSector().getFaction("dpl_phase_lab"))) {
    				if (market.getSize() >= size) {
    					largestMarket = market;
    					size = market.getSize();
    				}
    			}
    		}
    		
    		//Generating Eliza Lovelace.       	
            PersonAPI eliza_lovelace = Global.getFactory().createPerson();
            eliza_lovelace.setId(ELIZA_LOVELACE);
            eliza_lovelace.setFaction("dpl_phase_lab");
            eliza_lovelace.setGender(FullName.Gender.FEMALE);
            eliza_lovelace.setImportance(PersonImportance.VERY_HIGH);
            eliza_lovelace.setPostId(Ranks.POST_FACTION_LEADER);
            eliza_lovelace.setRankId(Ranks.FACTION_LEADER);
            eliza_lovelace.getName().setFirst("Eliza");
            eliza_lovelace.getName().setLast("Lovelace");
            eliza_lovelace.setPortraitSprite(Global.getSettings().getSpriteName("characters", "eliza_lovelace"));
            largestMarket.getCommDirectory().addPerson(eliza_lovelace, 0);
            largestMarket.addPerson(eliza_lovelace);
			ip.addPerson(eliza_lovelace);
        }
        
    	MarketAPI market4 = Global.getSector().getEconomy().getMarket("dpl_research_site_v_moon");
    	if (market4 != null) {
    		Industry lightindustry4 = market4.getIndustry("lightindustry");
    		lightindustry4.setImproved(true);
    	}
    	
    	
    	//Generating the deserter
    	PersonAPI nelson_bonaparte = Global.getFactory().createPerson();
            nelson_bonaparte.setId(NELSON_BONAPARTE);
            nelson_bonaparte.setFaction("dpl_persean_imperium");
            nelson_bonaparte.setGender(FullName.Gender.MALE);
            nelson_bonaparte.setPostId(Ranks.POST_FACTION_LEADER);
            nelson_bonaparte.setRankId(Ranks.FACTION_LEADER);
            nelson_bonaparte.getName().setFirst("Nelson");
            nelson_bonaparte.getName().setLast("Bonaparte");
            nelson_bonaparte.setPortraitSprite(Global.getSettings().getSpriteName("characters", "nelson_bonaparte"));
			ip.addPerson(nelson_bonaparte);
			
			nelson_bonaparte.setPersonality(Personalities.RECKLESS);
			nelson_bonaparte.getStats().setLevel(10);
			nelson_bonaparte.getStats().setSkillLevel(Skills.HELMSMANSHIP, 2);
			nelson_bonaparte.getStats().setSkillLevel(Skills.TARGET_ANALYSIS, 2);
			nelson_bonaparte.getStats().setSkillLevel(Skills.FIELD_MODULATION, 2);
			nelson_bonaparte.getStats().setSkillLevel(Skills.COMBAT_ENDURANCE, 2);
			nelson_bonaparte.getStats().setSkillLevel(Skills.SYSTEMS_EXPERTISE, 2);
			nelson_bonaparte.getStats().setSkillLevel(Skills.MISSILE_SPECIALIZATION, 2);
			nelson_bonaparte.getStats().setSkillLevel(Skills.CREW_TRAINING, 1);
			nelson_bonaparte.getStats().setSkillLevel(Skills.TACTICAL_DRILLS, 1);
			nelson_bonaparte.getStats().setSkillLevel(Skills.FLUX_REGULATION, 1);
			nelson_bonaparte.getStats().setSkillLevel(Skills.PHASE_CORPS, 1);
			nelson_bonaparte.getStats().setSkillLevel(Skills.ORDNANCE_EXPERTISE, 2);
			
	    //Generating Vladimir
	    PersonAPI vladimir_vassiliev = Global.getFactory().createPerson();
	    	vladimir_vassiliev.setId(VLADIMIR_VASSILIEV);
	    	vladimir_vassiliev.setFaction("dpl_phase_lab");
	    	vladimir_vassiliev.setGender(FullName.Gender.MALE);
	    	vladimir_vassiliev.setPostId(Ranks.POST_FACTION_LEADER);
	    	vladimir_vassiliev.setRankId(Ranks.FACTION_LEADER);
	    	vladimir_vassiliev.getName().setFirst("Vladimir");
	    	vladimir_vassiliev.getName().setLast("Vassiliev");
	    	vladimir_vassiliev.setPortraitSprite(Global.getSettings().getSpriteName("characters", "vladimir_vassiliev"));
	        ip.addPerson(vladimir_vassiliev);
				
	        vladimir_vassiliev.setPersonality(Personalities.AGGRESSIVE);
	        vladimir_vassiliev.getStats().setLevel(14);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.HELMSMANSHIP, 2);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.TARGET_ANALYSIS, 2);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.FIELD_MODULATION, 2);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.COMBAT_ENDURANCE, 2);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.SYSTEMS_EXPERTISE, 2);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.MISSILE_SPECIALIZATION, 2);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.DAMAGE_CONTROL, 2);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.IMPACT_MITIGATION, 2);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.POINT_DEFENSE, 2);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.CREW_TRAINING, 1);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.TACTICAL_DRILLS, 1);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.FLUX_REGULATION, 1);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.PHASE_CORPS, 1);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.CYBERNETIC_AUGMENTATION, 1);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.GUNNERY_IMPLANTS, 2);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.POLARIZED_ARMOR, 2);
	        vladimir_vassiliev.getStats().setSkillLevel(Skills.ORDNANCE_EXPERTISE, 2);
	        
	    //Generating Elly Lovelace
	    PersonAPI elly_lovelace = Global.getFactory().createPerson();
	    	elly_lovelace.setId(ELLY_LOVELACE);
	    	elly_lovelace.setFaction("dpl_phase_lab");
	    	elly_lovelace.setGender(FullName.Gender.FEMALE);
	    	elly_lovelace.setPostId(Ranks.UNKNOWN);
	    	elly_lovelace.setRankId(Ranks.UNKNOWN);
	    	elly_lovelace.getName().setFirst("Elly");
	    	elly_lovelace.getName().setLast("Lovelace");
	    	elly_lovelace.setPortraitSprite(Global.getSettings().getSpriteName("characters", "elly_lovelace"));
	        ip.addPerson(elly_lovelace);
	        Misc.setMercenary(elly_lovelace, true);
				
	        elly_lovelace.setPersonality(Personalities.STEADY);
	        elly_lovelace.getStats().setLevel(10);
	        elly_lovelace.getStats().setSkillLevel(Skills.HELMSMANSHIP, 2);
	        elly_lovelace.getStats().setSkillLevel(Skills.TARGET_ANALYSIS, 2);
	        elly_lovelace.getStats().setSkillLevel(Skills.FIELD_MODULATION, 2);
	        elly_lovelace.getStats().setSkillLevel(Skills.COMBAT_ENDURANCE, 2);
	        elly_lovelace.getStats().setSkillLevel(Skills.SYSTEMS_EXPERTISE, 2);
	        elly_lovelace.getStats().setSkillLevel(Skills.MISSILE_SPECIALIZATION, 2);
	        elly_lovelace.getStats().setSkillLevel(Skills.BALLISTIC_MASTERY, 2);
	        elly_lovelace.getStats().setSkillLevel(Skills.GUNNERY_IMPLANTS, 2);
	        elly_lovelace.getStats().setSkillLevel(Skills.ENERGY_WEAPON_MASTERY, 2);
	        elly_lovelace.getStats().setSkillLevel(Skills.ORDNANCE_EXPERTISE, 2);
    }

}