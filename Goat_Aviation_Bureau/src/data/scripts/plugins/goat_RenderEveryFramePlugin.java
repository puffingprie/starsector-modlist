package data.scripts.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class goat_RenderEveryFramePlugin extends BaseCombatLayeredRenderingPlugin {

	public static final String PLUGIN_ID = "goat_RenderEveryFramePlugin";

	private CombatEngineAPI engine;

	@Override
	public void init(CombatEntityAPI entity) {
		super.init(entity);
		engine = Global.getCombatEngine();
		engine.getCustomData().put(PLUGIN_ID, new LocalData());
	}

	@Override
	public void advance(float amount) {
		if (engine == null || engine.isPaused()) return;
		final LocalData localData = (LocalData)engine.getCustomData().get(PLUGIN_ID);

		final List<RenderData> renderData = localData.renderData;
	}

	@Override
	public void render(CombatEngineLayers layer, ViewportAPI view) {
		if (engine == null) {
			return;
		}

		// add
	}

	@Override
	public float getRenderRadius() {
		return Float.MAX_VALUE;
	}

	@Override
	public EnumSet<CombatEngineLayers> getActiveLayers() {
		return EnumSet.allOf(CombatEngineLayers.class);
	}

	private static final class LocalData {

		final List<RenderData> renderData = new ArrayList<>();
	}

	public static final class RenderData {

		public Vector2f loc;
		public Vector2f vel;
		public float angle;
		public float SizeMult;
		public float alphaMult;
		public float inDur;
		public float activeDur;
		public float outDur;
		public Color color;

		public RenderData() {
		}
	}

}