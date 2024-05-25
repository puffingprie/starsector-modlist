package data.scripts;

/*import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;*/

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.CampaignPlugin;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import com.fs.starfarer.api.loading.HullModSpecAPI;

import data.scripts.world.UNSCWorldGen;
import org.apache.log4j.Logger;

public class UNSCModPlugin extends BaseModPlugin
{

    public static Logger log = Global.getLogger(UNSCModPlugin.class);

    public void onNewGame()
    {
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("unsc");
        initUNSC();
    }
	
	private static void initUNSC()
    {
        new UNSCWorldGen().generate(Global.getSector());
    }

}