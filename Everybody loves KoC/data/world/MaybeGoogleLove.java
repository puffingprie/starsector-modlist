package data.world;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.Global;

public class MaybeGoogleLove implements SectorGeneratorPlugin {
	@Override
    public void generate(SectorAPI sector) {
		initFactionRelationships(sector);
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
        FactionAPI MaybeGoogle = sector.getFaction("MaybeGoogle");
		FactionAPI derelict = sector.getFaction(Factions.DERELICT);

/// REP LEVELS: VENGEFUL/HOSTILE/INHOSPITABLE/SUSPICIOUS/NEUTRAL/FAVORABLE/WELCOMING/FRIENDLY/COOPERATIVE

		MaybeGoogle.setRelationship(Factions.HEGEMONY, RepLevel.FRIENDLY);
		MaybeGoogle.setRelationship(Factions.DIKTAT, RepLevel.FRIENDLY);
		MaybeGoogle.setRelationship(Factions.TRITACHYON, RepLevel.COOPERATIVE);
		MaybeGoogle.setRelationship(Factions.LUDDIC_CHURCH, RepLevel.HOSTILE);
		MaybeGoogle.setRelationship(Factions.LUDDIC_PATH, RepLevel.HOSTILE);
		MaybeGoogle.setRelationship(Factions.PIRATES, RepLevel.HOSTILE);

		MaybeGoogle.setRelationship(Factions.PLAYER, RepLevel.FAVORABLE);
		MaybeGoogle.setRelationship(Factions.DERELICT, RepLevel.FRIENDLY);
    }

}
