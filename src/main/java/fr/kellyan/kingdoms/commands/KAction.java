package fr.kellyan.kingdoms.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the interface of all plugin actions.
 */
public interface KAction {
    /**
     * Try to execute the action.
     * @param sender the sender of the action.
     * @param args the args of the action.
     * @return if all the needed arguments of the action are defined.
     */
    boolean execute(CommandSender sender, String[] args);

    /**
     * @return if the action can be executed only by players.
     */
    boolean onlyPlayers();

    /**
     * @return the command with the format of : "/kingdoms cmd args..."
     */
    String getCommandHasHelp();

    /**
     * This method is used to get the next of the action (Called by the {@link CmdCompleter}).
     * @param sender the sender of the command.
     * @param args the current arguments of the action.
     * @return the next of the action.
     */
    default List<String> getNextValues(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
