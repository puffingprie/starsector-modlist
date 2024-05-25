package data.scripts;

import com.fs.starfarer.api.campaign.SectorAPI;
import exerelin.campaign.SectorManager;

public class goat_NEXGenerate extends goat_NormalGenerate {

	@Override
	public void generate(SectorAPI sector) {
		if (SectorManager.getManager().isCorvusMode()) {
			super.generate(sector);
		}
	}
}