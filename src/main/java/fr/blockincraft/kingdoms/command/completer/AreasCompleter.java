package fr.blockincraft.kingdoms.command.completer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AreasCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completion = new ArrayList<>();

        if (sender instanceof Player player) {
            if (args.length == 1) {
                if ("help".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("help");
                if ("pos1".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("pos1");
                if ("pos2".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("pos2");
                if ("view".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("view");
                if ("info".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("info");
            }
        }

        return completion;
    }
}
