package fr.blockincraft.kingdoms.command.completer;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.core.dto.PersonalClaimFullDTO;
import fr.blockincraft.kingdoms.core.dto.PersonalClaimLightDTO;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class ClaimsCompleter implements TabCompleter {
    private final Registry registry = Kingdoms.getInstance().getRegistry();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completion = new ArrayList<>();

        if (sender instanceof Player player) {
            if (args.length == 1) {
                if ("help".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("help");
                if ("create".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("create");
                if ("extend".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("extend");
                if ("delete".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("delete");
                if ("trust".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("trust");
                if ("untrust".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("untrust");
                if ("info".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("info");
            } else {
                if (args[0].equalsIgnoreCase("extend")) {
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("")) completion.add("<length>");
                    }
                    if (args.length == 3) {
                        if ("north".startsWith(args[2].toLowerCase(Locale.ROOT))) completion.add("north");
                        if ("south".startsWith(args[2].toLowerCase(Locale.ROOT))) completion.add("south");
                        if ("east".startsWith(args[2].toLowerCase(Locale.ROOT))) completion.add("east");
                        if ("west".startsWith(args[2].toLowerCase(Locale.ROOT))) completion.add("west");
                    }
                } else if (args[0].equalsIgnoreCase("trust")) {
                    if (args.length == 2) {
                        PersonalClaimFullDTO claim = registry.getPlayerPersonalClaimFull(player.getUniqueId());

                        if (claim != null) {
                            Set<Player> players = new HashSet<>(Bukkit.getOnlinePlayers());

                            players.removeIf(playerIn -> {
                                return claim.getTrustedPlayers().contains(playerIn.getUniqueId()) || playerIn.getUniqueId() == player.getUniqueId();
                            });

                            players.forEach(playerIn -> {
                                if (playerIn.getName().toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))) {
                                    completion.add(playerIn.getName());
                                }
                            });
                        }
                    }
                } else if (args[0].equalsIgnoreCase("untrust")) {
                    if (args.length == 2) {
                        PersonalClaimFullDTO claim = registry.getPlayerPersonalClaimFull(player.getUniqueId());

                        if (claim != null) {
                            Set<Player> players = new HashSet<>();

                            claim.getTrustedPlayers().forEach(uuid -> {
                                if (Bukkit.getPlayer(uuid) != null) {
                                    players.add(Bukkit.getPlayer(uuid));
                                }
                            });

                            players.forEach(playerIn -> {
                                if (playerIn.getName().toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))) {
                                    completion.add(playerIn.getName());
                                }
                            });
                        }
                    }
                } else if (args[0].equalsIgnoreCase("info")) {
                    if (args.length == 2) {
                        if ("all".startsWith(args[1].toLowerCase(Locale.ROOT))) completion.add("all");
                        if ("mine".startsWith(args[1].toLowerCase(Locale.ROOT))) completion.add("mine");
                        if ("trusted".startsWith(args[1].toLowerCase(Locale.ROOT))) completion.add("trusted");
                    }
                }
            }
        }

        return completion;
    }
}
