package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.awt.*;

public class phasearray extends BaseShipSystemScript {

    public static final Color JITTER_COLOR = new Color(255,175,255,255);
    public static final float JITTER_FADE_TIME = 0.5f;
    public static final float GOTTAGOFAST = 100f;
    public static final float SHIP_ALPHA_MULT = 0.25f;
	//public static final float VULNERABLE_FRACTION = 0.875f;
    public static final float VULNERABLE_FRACTION = 0f;
    public static final float INCOMING_DAMAGE_MULT = 0.25f;
	
    public static final float MAX_TIME_MULT = 3f;
	
    protected Object STATUSKEY1 = new Object();
    protected Object STATUSKEY2 = new Object();
    protected Object STATUSKEY3 = new Object();
    protected Object STATUSKEY4 = new Object();
    
    private void maintainStatus(ShipAPI playerShip, State state, float effectLevel) {
	float level = effectLevel;
	float f = VULNERABLE_FRACTION;
	
	ShipSystemAPI cloak = playerShip.getPhaseCloak();
	if (cloak == null) cloak = playerShip.getSystem();
	if (cloak == null) return;
	
	if (level > f) {
//		Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY1,
//				cloak.getSpecAPI().getIconSpriteName(), cloak.getDisplayName(), "can not be hit", false);
		Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY2,
				cloak.getSpecAPI().getIconSpriteName(), cloak.getDisplayName(), "time flow altered", false);
	} else {
//		float INCOMING_DAMAGE_MULT = 0.25f;
//		float percent = (1f - INCOMING_DAMAGE_MULT) * getEffectLevel() * 100;
//		Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY3,
//				spec.getIconSpriteName(), cloak.getDisplayName(), "damage mitigated by " + (int) percent + "%", false);
	}
    }
    
    @Override
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        ShipAPI ship = null;
        boolean player = false;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI) stats.getEntity();
            player = ship == Global.getCombatEngine().getPlayerShip();
            id = id + "_" + ship.getId();
        } else {
            return;
        }
        
        if (player) {
            maintainStatus(ship, state, effectLevel);
        }
        
        if (Global.getCombatEngine().isPaused()) {
            return;
        }
        
        if (state == State.COOLDOWN || state == State.IDLE) {
            unapply(stats, id);
            return;
        }
        
        float level = effectLevel;
	//float f = VULNERABLE_FRACTION;
	
	float jitterLevel = 0f;
	float jitterRangeBonus = 0f;
	float levelForAlpha = level;
	
	ShipSystemAPI cloak = ship.getPhaseCloak();
	if (cloak == null) cloak = ship.getSystem();
        
        if (state == State.IN || state == State.ACTIVE) {
            ship.setPhased(true);
            levelForAlpha = level;
            
            stats.getAcceleration().modifyFlat(id, GOTTAGOFAST * effectLevel);
            stats.getDeceleration().modifyFlat(id, GOTTAGOFAST * effectLevel);

            stats.getTurnAcceleration().modifyFlat(id, 80f * effectLevel);
            stats.getMaxTurnRate().modifyFlat(id, 80f);

            stats.getMaxSpeed().modifyFlat(id, GOTTAGOFAST * effectLevel);
        } else if (state == State.OUT) {
            ship.setPhased(true);
            levelForAlpha = level;
            
            stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
            stats.getMaxTurnRate().unmodify(id);
        }
        
        ship.setExtraAlphaMult(1f - (1f - SHIP_ALPHA_MULT) * levelForAlpha);
	ship.setApplyExtraAlphaToEngines(true);
	
	
	float shipTimeMult = 1f + (MAX_TIME_MULT - 1f) * levelForAlpha;
	stats.getTimeMult().modifyMult(id, shipTimeMult);
	if (player) {
		Global.getCombatEngine().getTimeMult().modifyMult(id, 1f / shipTimeMult);
	} else {
		Global.getCombatEngine().getTimeMult().unmodify(id);
	}
    }

    @Override
    public void unapply(MutableShipStatsAPI stats, String id) {
        ShipAPI ship = null;
	//boolean player = false;
	if (stats.getEntity() instanceof ShipAPI) {
		ship = (ShipAPI) stats.getEntity();
		//player = ship == Global.getCombatEngine().getPlayerShip();
		//id = id + "_" + ship.getId();
	} else {
		return;
	}
        
        Global.getCombatEngine().getTimeMult().unmodify(id);
	stats.getTimeMult().unmodify(id);
	
	ship.setPhased(false);
	ship.setExtraAlphaMult(1f);
        
        stats.getMaxSpeed().unmodify(id);
        stats.getMaxTurnRate().unmodify(id);
        stats.getTurnAcceleration().unmodify(id);
        stats.getAcceleration().unmodify(id);
        stats.getDeceleration().unmodify(id);
    }

    @Override
    public StatusData getStatusData(int index, State state, float effectLevel) {
        /*if (index == 0) {
            return new StatusData("phase anchors latched", false);
        }
        if (index == 1) {
            return new StatusData("+ 90 top speed", false);
        }*/
        return null;
    }
}
