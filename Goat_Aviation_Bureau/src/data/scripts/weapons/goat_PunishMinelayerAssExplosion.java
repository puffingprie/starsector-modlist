package data.scripts.weapons;

import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;
import com.fs.starfarer.api.impl.combat.NegativeExplosionVisual;
import com.fs.starfarer.api.impl.combat.RiftCascadeMineExplosion;

public class goat_PunishMinelayerAssExplosion implements ProximityExplosionEffect {

	@Override
	public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
		NegativeExplosionVisual.NEParams p = RiftCascadeMineExplosion.createStandardRiftParams("all_goat_punish_minelayer", 1f);
		p.hitGlowSizeMult = 4.5f;
		p.thickness = 0.6f;
		RiftCascadeMineExplosion.spawnStandardRift(explosion, p);
	}
}
