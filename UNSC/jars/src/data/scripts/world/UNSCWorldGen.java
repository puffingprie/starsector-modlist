package data.scripts.world;



import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.Global;
import exerelin.campaign.SectorManager;

public class UNSCWorldGen implements SectorGeneratorPlugin {
	@Override
    public void generate(SectorAPI sector) {
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("unsc");


		//No Audere if Nex Enabled
		boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
		if (!haveNexerelin || SectorManager.getManager().isCorvusMode()){
			new UNSCStar().generate(sector);
			initFactionRelationships(sector);
		}
		if (!haveNexerelin) {
			initFactionRelationships(sector);
		}

    }

	public static void initFactionRelationships(SectorAPI sector) {
        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
		FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
		FactionAPI pirates = sector.getFaction(Factions.PIRATES);
		FactionAPI independent = sector.getFaction(Factions.INDEPENDENT);
		FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
		FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);
		FactionAPI player = sector.getFaction(Factions.PLAYER);
		FactionAPI diktat = sector.getFaction(Factions.DIKTAT);
        FactionAPI league = sector.getFaction(Factions.PERSEAN);
        FactionAPI UNSC = sector.getFaction("unsc");

		UNSC.setRelationship(Factions.PERSEAN, RepLevel.FAVORABLE);
		UNSC.setRelationship(Factions.INDEPENDENT, RepLevel.FAVORABLE);
		UNSC.setRelationship(Factions.TRITACHYON, RepLevel.FAVORABLE);
		UNSC.setRelationship(Factions.HEGEMONY, RepLevel.SUSPICIOUS);
		UNSC.setRelationship(Factions.LUDDIC_CHURCH, RepLevel.SUSPICIOUS);
		UNSC.setRelationship(Factions.PIRATES, RepLevel.HOSTILE);
		UNSC.setRelationship(Factions.LUDDIC_PATH, RepLevel.HOSTILE);
    }

}