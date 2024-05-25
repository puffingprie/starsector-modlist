package com.fs.starfarer.api.impl.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.util.Misc.Token;
import data.scripts.campaign.intel.UW_StarlightGala;
import java.util.List;
import java.util.Map;

// UW_HasGalaIntel
public class UW_HasGalaIntel extends BaseCommandPlugin {

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Token> params, Map<String, MemoryAPI> memoryMap) {
        return Global.getSector().getIntelManager().getFirstIntel(UW_StarlightGala.class) != null;
    }
}
