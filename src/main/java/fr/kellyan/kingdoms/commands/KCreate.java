package fr.kellyan.kingdoms.commands;

import fr.kellyan.kingdoms.Kingdoms;
import fr.kellyan.kingdoms.configurations.Lang;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The action to create a kingdom.
 */
public class KCreate implements KAction {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            Player player = (Player) sender;
            String name = args[0];
            for (int i = 1; i < args.length; i++) {
                name += " " + args[i];
            }
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null);
            if (claim == null) {
                Lang.sendMessage(sender, Lang.youMustBeInAClaimToExecuteThisCommand.toString());
            } else {
                if (claim.hasExplicitPermission(player, ClaimPermission.Build)) {
                    Kingdoms.getInstance().getData().createKingdom(name, claim, player);
                } else {
                    Lang.sendMessage(sender, Lang.haveNotClaimPermissionToDoThat.toString());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onlyPlayers() {
        return true;
    }

    @Override
    public String getCommandHasHelp() {
        return "/kingdoms " + Lang.kCreate + " <" + Lang.name + ">";
    }

    @Override
    public List<String> getNextValues(CommandSender sender, String[] args) {
        List<String> values = new ArrayList<>();
        values.add("<" + Lang.name.toString() +">");
        return values;
    }
}
