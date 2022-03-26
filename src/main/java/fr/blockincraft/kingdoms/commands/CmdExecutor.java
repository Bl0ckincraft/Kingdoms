package fr.blockincraft.kingdoms.commands;

import fr.blockincraft.kingdoms.configurations.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * The kingdoms command executor.
 */
public class CmdExecutor implements CommandExecutor {
    //This contains all actions example : /kingdoms create -> {"create", new KCreate()}.
    public static final Map<String, KAction> actions = new HashMap<>();

    /**
     * This method load the actions and save it in map. We don't use {@code static { }} method because the {@link Lang}
     * data must be loaded before.
     */
    public static void loadActions() {
        actions.put(Lang.kCreate.toString(), new KCreate());
        actions.put(Lang.kDelete.toString(), new KDelete());
    }

    /**
     * Redirect the execution of the command to the action or send a help message to the player.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            String actionName = args[0];
            if (actions.containsKey(actionName.toLowerCase(Locale.ROOT))) {
                KAction action = actions.get(actionName.toLowerCase(Locale.ROOT));
                if (action.onlyPlayers() && !(sender instanceof Player)) {
                    Lang.sendMessage(sender, Lang.onlyPlayersCanExecuteThisCommand.toString());
                    return true;
                } else {
                    List<String> argsList = new ArrayList<>();
                    for (int i = 1; i < args.length; i++) {
                        argsList.add(args[i]);
                    }
                    if (action.execute(sender, argsList.toArray(new String[0]))) return true;
                }
            }
        }

        //Send help message if nothing happens.
        Lang.sendHelpMessage(sender);
        return true;
    }
}
