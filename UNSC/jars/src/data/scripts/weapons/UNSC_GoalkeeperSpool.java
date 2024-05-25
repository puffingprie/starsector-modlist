package data.scripts.weapons;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;

public class UNSC_GoalkeeperSpool implements EveryFrameWeaponEffectPlugin{
    
    private float refire=1, BASE_REFIRE;
    private int spoolup=30; //Cooldown time of 30 frames?
    private boolean runOnce=false;
    private ShipAPI ship;
    
    @Override
    public void advance (float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        
        if(engine.isPaused()){return;}

        if(!runOnce){
            runOnce=true;
            ship=weapon.getShip();
            BASE_REFIRE=weapon.getCooldown();
        }

        if(weapon.isFiring()){

            spoolup=30; //30 frames before weapon unspools

            float rof = (1/ship.getMutableStats().getEnergyRoFMult().computeMultMod());
            
            if( weapon.getChargeLevel()==1){
                refire=Math.max(0,refire - 0.13f); //Increment RoF by 87% each shot
                weapon.setRemainingCooldownTo(Math.max(0.025f, refire*BASE_REFIRE)*rof); //min cooldown of 0.025s?
            }

        } else if(spoolup>0){
            spoolup--; //Remove 1 from "spool" counter until it hits zero, where it de-spools
        } else {
            refire=1; //reset the refire delay
        }
    }
}