package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import org.lazywizard.lazylib.MathUtils;

import java.util.HashMap;
import java.util.Map;


public class NES_ColorSelector_HF extends BaseHullMod {

    private final Map<Integer,String> DECO_COUNTER = new HashMap<>();
    {
        DECO_COUNTER.put(0, "nes_fluorspar_deco_counter0");
        DECO_COUNTER.put(1, "nes_fluorspar_deco_counter1");
        DECO_COUNTER.put(2, "nes_fluorspar_deco_counter2");
        DECO_COUNTER.put(3, "nes_fluorspar_deco_counter3");
    }

    private final Map<String, Integer> SWITCH_TO = new HashMap<>();
    {
        SWITCH_TO.put("nes_fluorspar_deco_counter0",1);
        SWITCH_TO.put("nes_fluorspar_deco_counter1",2);
        SWITCH_TO.put("nes_fluorspar_deco_counter2",3);
        SWITCH_TO.put("nes_fluorspar_deco_counter3",0);
    }

    private final Map<Integer,String> SWITCH = new HashMap<>();
    {
        SWITCH.put(0,"NES_Explorer");
        SWITCH.put(1,"NES_Surveyor");
        SWITCH.put(2,"NES_Rogue");
        SWITCH.put(3,"NES_Infiltrator");
    }

    private final String decocounterID = "NES_DECO_COUNTER";

    //faction specific paintjob selector for Hammerfall
    private static int PAINTJOB_HF;

    @Override
    public void advanceInCampaign(FleetMemberAPI member, float amount) {

        //skip this if player
        if (member.getFleetCommander().isPlayer() || member.getFleetCommander().isDefault()) {
            return;
        }

        else if (member.getFleetData() != null && member.getFleetData().getFleet() != null && !member.getFleetCommander().isPlayer()
                && member.getFleetCommander().getFaction().getId().contains("independent")
                || member.getFleetCommander().getFaction().getId().contains("mercenary")
                || member.getFleetCommander().getFaction().getId().contains("tritachyon")){
            PAINTJOB_HF = 1;//Surveyor white
            return;
        }

        else if (member.getFleetData() != null && member.getFleetData().getFleet() != null && !member.getFleetCommander().isPlayer()
                && member.getFleetCommander().getFaction().getId().contains("pirates")){
            PAINTJOB_HF = 2;//Rogue red
            return;
        }

        else PAINTJOB_HF = 0;//Explorer yellow
        return;
    }

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

        //swap livery if hullmod is removed / not present
        boolean toSwitch=true;
        for(int i=0; i<SWITCH.size(); i++){
            if(stats.getVariant().getHullMods().contains(SWITCH.get(i))){
                toSwitch=false;
            }
        }

        if(toSwitch){
            //select new livery
            int selected;
            if(stats.getVariant().getWeaponSpec(decocounterID)!=null){
                selected=SWITCH_TO.get(stats.getVariant().getWeaponSpec(decocounterID).getWeaponId());
            }
            else{
                selected=PAINTJOB_HF;
            }

            //add the proper hullmod
            stats.getVariant().addMod(SWITCH.get(selected));

            //clear the deco_counter slot that is to be refilled
            stats.getVariant().clearSlot(decocounterID);
            String toInstallDeco=DECO_COUNTER.get(selected);

            //refill deco_counter slot with the selected deco
            stats.getVariant().addWeapon(decocounterID, toInstallDeco);

        }
    }

    //fix for deco_counter appearing in cargo
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

        if (ship.getOriginalOwner() < 0) {
            if (
                    Global.getSector() != null &&
                            Global.getSector().getPlayerFleet() != null &&
                            Global.getSector().getPlayerFleet().getCargo() != null &&
                            Global.getSector().getPlayerFleet().getCargo().getStacksCopy() != null &&
                            !Global.getSector().getPlayerFleet().getCargo().getStacksCopy().isEmpty()
            ) {
                for (CargoStackAPI s : Global.getSector().getPlayerFleet().getCargo().getStacksCopy()) {
                    if (
                            s.isWeaponStack() && (
                                    DECO_COUNTER.containsValue(s.getWeaponSpecIfWeapon().getWeaponId())
                            )
                    ) {
                        Global.getSector().getPlayerFleet().getCargo().removeStack(s);
                    }
                }
            }
        }
    }
}