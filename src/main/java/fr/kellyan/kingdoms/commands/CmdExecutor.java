package fr.kellyan.kingdoms.commands;

import fr.kellyan.kingdoms.configurations.Lang;
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

    //Register the actions.
    static {
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
                    List<String> argsList = Arrays.asList(args);
                    argsList.remove(0);
                    if (action.execute(sender, argsList.toArray(new String[0]))) return true;
                }
            }
        }

        //Send help message if nothing happens.
        Lang.sendHelpMessage(sender);
        return true;
    }
}
