package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class goat_GentilesSystemAnimation implements EveryFrameWeaponEffectPlugin {

	private float reloadTime = 1f;
	private boolean systemUsedBefore = false;
	private final FrameControl frameControl = new FrameControl(0);

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

		if (!weapon.getShip().isAlive()) return;
		if (weapon.getShip().getSystem() == null) return;

		//第一次运行检测该武器动画的最大帧数和播放速度
		if (frameControl.totalFrames <= 0f && weapon.getAnimation() != null) {
			frameControl.setTotalFrames(weapon.getAnimation().getNumFrames());
			reloadTime = frameControl.totalFrames / weapon.getAnimation().getFrameRate();
			return;
		}

		//系统被激活时，持续添加系统之前被激活过的flag
		if (weapon.getShip().getSystem().getEffectLevel() > 0f) {
			systemUsedBefore = true;
		}

		//系统被激活过后，第一次进入冷却时将武器当前帧数设置0并放出油罐，并取消flag
		if (systemUsedBefore && weapon.getShip().getSystem().getEffectLevel() <= 0f) {
			systemUsedBefore = false;
			frameControl.setCurrState(0f);
			engine.spawnMuzzleFlashOrSmoke(weapon.getShip(), weapon.getSlot(), weapon.getSpec(), 0, weapon.getCurrAngle());
			DamagingProjectileAPI tank = (DamagingProjectileAPI)engine.spawnProjectile(weapon.getShip(), null, "goat_gentiles_system_R", weapon.getFirePoint(0), weapon.getCurrAngle(), weapon.getShip().getVelocity());
			tank.setAngularVelocity(MathUtils.getRandomNumberInRange(-15, 15));

			Global.getSoundPlayer().playSound("goat_gentiles_system", 0.6f, 0.6f, weapon.getLocation(), new Vector2f());
		}

		//播放装填动画
		frameControl.advance(amount);
		if (weapon.getAnimation() != null) {
			frameControl.setTotalFrames(weapon.getAnimation().getNumFrames());
			if (weapon.getAnimation().getFrame() <= 0) {
				frameControl.setTargetState(1f, reloadTime);
			}
			weapon.getAnimation().setFrame(frameControl.getFrame());
		}
	}

	private static class FrameControl {

		private int totalFrames;

		private int currFrame = 0;
		private float changeSpeed = 0f;
		private float targetState = 0f;
		private float currState = 0f;

		FrameControl(int totalFrames) {
			this.totalFrames = totalFrames;
		}

		public void advance(float amount) {
			currFrame = (int)(totalFrames * currState);

			float diff = targetState - currState;
			//无需帧数变化就return
			if (diff == 0f) return;
			float max = changeSpeed * amount;
			diff = MathUtils.clamp(diff, -max, max);
			currState += diff;
			currState = MathUtils.clamp(currState, 0, 1f);
		}

		public int getFrame() {
			return MathUtils.clamp(currFrame, 0, totalFrames - 1);
		}

		public void setTargetState(float targetState, float time) {
			this.targetState = MathUtils.clamp(targetState, 0, 1f);
			changeSpeed = Math.abs(targetState - currState) / time;
		}

		public void setCurrState(float currState) {
			this.currState = currState;
			this.targetState = currState;
			this.changeSpeed = 0f;
		}

		public void setTotalFrames(int totalFrames) {
			this.totalFrames = totalFrames;
		}
	}
}
