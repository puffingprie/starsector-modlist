package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.ImportantPeopleAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import data.scripts.world.ork_Gen;
import data.scripts.fleets.bt_PersonalFleetAdmiral1;
import exerelin.campaign.SectorManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ork_ModPlugin extends BaseModPlugin {

    @Override
    public void onNewGame() {
        SectorAPI sector = Global.getSector();

        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        if (!haveNexerelin || SectorManager.getManager().isCorvusMode()) {
            new ork_Gen().generate(sector);
        }
    }

    public static void OrkLoveSettings() {
        FactionAPI orks = Global.getSector().getFaction("orks");

        for (FactionAPI faction : Global.getSector().getAllFactions()){
            //if (faction.getRelationship("orks") > 0.2f) continue; //I guess if someone loves orks that much, they can be above that???
            //if (faction.getRelationship("orks") < 0f) continue; //I guess if someone hates orks that much, they can be below that.
            if (Objects.equals(faction.getId(), "orks")) continue;
            orks.setRelationship(faction.getId(), -0.2f); //Otherwise set all factions to -0.2.
        }

        //Vanilla factions
        orks.setRelationship(Factions.LUDDIC_CHURCH, 0.2f);
        orks.setRelationship(Factions.LUDDIC_PATH, -0.3f);
        orks.setRelationship(Factions.TRITACHYON, -1f);
        orks.setRelationship(Factions.PERSEAN, -0.3f);
        orks.setRelationship(Factions.PIRATES, -0.5f);
        orks.setRelationship(Factions.INDEPENDENT, 0.6f);
        orks.setRelationship(Factions.DIKTAT, -0.5f);
        orks.setRelationship(Factions.LIONS_GUARD, -0.5f);
        orks.setRelationship(Factions.HEGEMONY, 0.3f);
        orks.setRelationship(Factions.REMNANTS, -1f);
        //Modded ones
        if(Global.getSettings().getModManager().isModEnabled("blade_breakers")) {
            orks.setRelationship("blade_breakers", -0.8f);
        }
        if(Global.getSettings().getModManager().isModEnabled("exipirated")) {
            orks.setRelationship("exipirated", -0.6f);
        }
        if(Global.getSettings().getModManager().isModEnabled("gmda")) {
            orks.setRelationship("gmda", -0.6f);
        }
        if(Global.getSettings().getModManager().isModEnabled("gmda_patrol")) {
            orks.setRelationship("gmda_patrol", -0.6f);
        }
        if(Global.getSettings().getModManager().isModEnabled("draco")) {
            orks.setRelationship("draco", -0.6f);
        }
        if(Global.getSettings().getModManager().isModEnabled("fang")) {
            orks.setRelationship("fang", -0.6f);
        }
        if(Global.getSettings().getModManager().isModEnabled("HMI")) {
            orks.setRelationship("mess", -0.8f);
        }
        if(Global.getSettings().getModManager().isModEnabled("pigeonpun_projectsolace")) {
            orks.setRelationship("projectsolace", 0.5f);
        }
        if(Global.getSettings().getModManager().isModEnabled("tahlan")) {
            orks.setRelationship("tahlan_legioinfernalis", -0.7f);
        }
        if(Global.getSettings().getModManager().isModEnabled("diableavionics")) {
            orks.setRelationship("diableavionics", -0.7f);
        }
        if(Global.getSettings().getModManager().isModEnabled("scalartech")) {
            orks.setRelationship("scalartech", -0.7f);
        }
              if(Global.getSettings().getModManager().isModEnabled("HIVER")) {
            orks.setRelationship("HIVER", -0.7f);
        }


    }

    @Override
    public void onNewGameAfterEconomyLoad() {
        OrkLoveSettings();
        SectorAPI sector = Global.getSector();
        MarketAPI dregruk = Global.getSector().getEconomy().getMarket("dregruk");
        if (dregruk != null){
            {data.campaign.econ.bt_People.create();}
        }
        if (!sector.hasScript(bt_PersonalFleetAdmiral1.class)) {
            sector.addScript(new bt_PersonalFleetAdmiral1());
        }
    }
}