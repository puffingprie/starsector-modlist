package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.intel.bar.events.BarEventManager;
import com.fs.starfarer.api.impl.campaign.procgen.ProcgenUsedNames;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import org.dark.shaders.light.LightData;
import org.dark.shaders.util.ShaderLib;
import org.dark.shaders.util.TextureData;

public class GoatModPlugin extends BaseModPlugin {

	public static final String CLASS_MARK = "GoatModPlugin";

	@Override
	public void onApplicationLoad() {

		ShaderLib.init();
		LightData.readLightDataCSV("data/lights/goat_light_data.csv");
		TextureData.readTextureDataCSV("data/lights/goat_texture_data.csv");
	}

	@Override
	public void onNewGame() {
		ProcgenUsedNames.notifyUsed("Taivassija");
		ProcgenUsedNames.notifyUsed("Caller");
		ProcgenUsedNames.notifyUsed("Huitzilopochtli");
		ProcgenUsedNames.notifyUsed("Theia");
		ProcgenUsedNames.notifyUsed("Fomalhaut");
		ProcgenUsedNames.notifyUsed("Coatepec");
		ProcgenUsedNames.notifyUsed("Babel");
		ProcgenUsedNames.notifyUsed("Xiuhcoatl");

		afterNewGameLoad = false;
		onGameLoad(true);
		afterNewGameLoad = true;
	}

	boolean afterNewGameLoad = true;

	@Override
	public void onGameLoad(boolean newGame) {

		boolean classLoad = false;
		if (Global.getSector().getPersistentData().containsKey(CLASS_MARK)) {
			classLoad = (Boolean)Global.getSector().getPersistentData().get(CLASS_MARK);
		}

		if (!classLoad) {
			Global.getSector().getPersistentData().put(CLASS_MARK, true);

			if (NEX()) {
				new goat_NEXGenerate().generate(Global.getSector());
			} else {
				new goat_NormalGenerate().generate(Global.getSector());
			}

			SharedData.getData().getPersonBountyEventData().addParticipatingFaction("Goat_Aviation_Bureau");
		}

		onDevModeF8Reload();

		if (afterNewGameLoad) {
			BarEventManager manager = BarEventManager.getInstance();
			// bar events
		}
	}

	public static boolean NEX() {
		return Global.getSettings().getModManager().isModEnabled("nexerelin");
	}
}