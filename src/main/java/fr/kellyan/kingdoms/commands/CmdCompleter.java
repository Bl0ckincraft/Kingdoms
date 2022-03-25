package fr.kellyan.kingdoms.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

/**
 * The kingdoms command completer.
 */
public class CmdCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> values = new ArrayList<>();
        Map<String, KAction> actions = CmdExecutor.actions;

        if (args.length > 0) {
            if (args.length == 1) {
                //If the args is the action (the first), it returns all the actions.
                for (String actionName : actions.keySet()) {
                    if (actionName.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT))) {
                        values.add(actionName);
                    }
                }
            } else {
                //Else get the next values from the action.
                if (actions.containsKey(args[0].toLowerCase(Locale.ROOT))) {
                    //Create a new array of args without the action name.
                    KAction action = actions.get(args[0].toLowerCase(Locale.ROOT));
                    List<String> argsList = new ArrayList<>();
                    for (int i = 1; i < args.length; i++) {
                        argsList.add(args[i]);
                    }

                    values.addAll(action.getNextValues(sender, argsList.toArray(new String[0])));
                }
            }
        }

        return values;
    }
}
