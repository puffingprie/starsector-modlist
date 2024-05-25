package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.EnumSet;

public class goat_OscillatingVisual extends BaseCombatLayeredRenderingPlugin {

	public static final float MAX_EFFECT_TIME = 1.6f;
	public static final float EFFECT_TIME = 0.02f;
	public static final float SIZE_FACTOR = 0.004f;

	private CombatEngineAPI engine;
	private final ShipAPI ship;
	private final float angle;
	private final SpriteAPI sprite;

	private float timer = MAX_EFFECT_TIME;
	private boolean done;

	public goat_OscillatingVisual(ShipAPI ship, float angle) {
		this.ship = ship;
		this.angle = angle;
		this.sprite = Global.getSettings().getSprite("fx", "goat_oscillating");
		this.done = false;

		sprite.setAngle(angle - MathUtils.getRandomNumberInRange(87f, 93f));
		sprite.setSize(0.50f, 0.4f);
		sprite.setAlphaMult(0.2f);
		sprite.setColor(ship.getShield().getRingColor());

		Global.getCombatEngine().addLayeredRenderingPlugin(this);
	}

	@Override
	public void init(CombatEntityAPI entity) {
		super.init(entity);
		engine = Global.getCombatEngine();
	}

	@Override
	public void advance(float amount) {
		if (engine == null || engine.isPaused()) return;

		entity.getLocation().set(ship.getShieldCenterEvenIfNoShield());

		timer -= amount;
		if (timer <= 0f) {
			done = true;
			return;
		}

		float size = timer * ship.getShieldRadiusEvenIfNoShield() * SIZE_FACTOR;
		sprite.setWidth(size * 92f);
		sprite.setHeight(size * 10f);
	}

	@Override
	public void render(CombatEngineLayers layer, ViewportAPI viewport) {

		if (layer == CombatEngineLayers.ABOVE_PARTICLES_LOWER) {

			float alphaMult = viewport.getAlphaMult();
			alphaMult *= Math.max((timer / MAX_EFFECT_TIME) * 3f - 2f, 0f);
			sprite.setAlphaMult(alphaMult);

			float radius = ship.getShieldRadiusEvenIfNoShield();
			radius *= timer / MAX_EFFECT_TIME;
			Vector2f point = MathUtils.getPoint(entity.getLocation(), radius, angle);
			sprite.renderAtCenter(point.getX(), point.getY());
		}
	}

	@Override
	public EnumSet<CombatEngineLayers> getActiveLayers() {
		return EnumSet.of(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
	}

	@Override
	public float getRenderRadius() {
		return ship.getShieldRadiusEvenIfNoShield() + 1000f;
	}

	@Override
	public boolean isExpired() {
		return done;
	}
}

