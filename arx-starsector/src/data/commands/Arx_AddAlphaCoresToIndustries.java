package data.commands;

import java.util.List;

import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.Console;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;

public class Arx_AddAlphaCoresToIndustries implements BaseCommand {
  public BaseCommand.CommandResult runCommand(String args, BaseCommand.CommandContext context) {
    if (!context.isInMarket()) {
      Console.showMessage("Error: This command can only be used when interacting with a market.");
      return BaseCommand.CommandResult.WRONG_CONTEXT;
    }
    MarketAPI market = context.getMarket();
    List<Industry> industries = market.getIndustries();
    for (Industry industry : industries) {
      if (!industry.canInstallAICores())
        continue;
      industry.setAICoreId("alpha_core");
      Console.showMessage("Added Alpha Core to " + industry.getCurrentName());
    }
    return BaseCommand.CommandResult.SUCCESS;
  }
}
