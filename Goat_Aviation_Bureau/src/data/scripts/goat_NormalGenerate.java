package data.scripts;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import data.scripts.world.GABsystems.goat_Taivassija;

public class goat_NormalGenerate implements SectorGeneratorPlugin {

	@Override
	public void generate(SectorAPI sector) {
		new goat_Taivassija().generate(sector);

		relationAdj(sector);
	}

	private void relationAdj(SectorAPI sector) {

		FactionAPI gab = sector.getFaction("Goat_Aviation_Bureau");

		FactionAPI player = sector.getFaction(Factions.PLAYER);
		FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
		FactionAPI pirates = sector.getFaction(Factions.PIRATES);
		FactionAPI independent = sector.getFaction(Factions.INDEPENDENT);
		FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
		FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);

		gab.setRelationship(player.getId(), 0.0f);
		gab.setRelationship(tritachyon.getId(), 0.5f);
		gab.setRelationship(pirates.getId(), -0.4f);
		gab.setRelationship(independent.getId(), 0.5f);
		gab.setRelationship(church.getId(), 0.6f);
		gab.setRelationship(path.getId(), -0.7f);

	}
}