package fr.kellyan.kingdoms.commands;

import fr.kellyan.kingdoms.Kingdom;
import fr.kellyan.kingdoms.Kingdoms;
import fr.kellyan.kingdoms.configurations.Lang;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KDelete implements KAction {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Player player = (Player) sender;
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null);
            if (claim == null) {
                Lang.sendMessage(sender, Lang.youMustBeInAClaimToExecuteThisCommand.toString());
            } else {
                Kingdom kingdom = Kingdoms.getInstance().getData().getKingdomInClaim(claim);
                if (kingdom == null) {
                    Lang.sendMessage(sender, Lang.youMustBeInAKingdomToExecuteThisCommand.toString());
                } else {
                    if (claim.hasExplicitPermission(player, ClaimPermission.Build)) {
                        Kingdoms.getInstance().getData().deleteKingdom(kingdom);
                        Lang.sendMessage(sender, Lang.kingdomDelete.toString().replace("%name%", kingdom.getName()));
                    } else {
                        Lang.sendMessage(sender, Lang.haveNotClaimPermissionToDoThat.toString());
                    }
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
        return "/kingdoms " + Lang.kDelete;
    }
}
