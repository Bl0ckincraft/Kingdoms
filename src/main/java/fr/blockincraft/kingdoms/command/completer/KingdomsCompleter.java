package fr.blockincraft.kingdoms.command.completer;

import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.enums.Constructions;
import fr.blockincraft.kingdoms.data.PlayerData;
import fr.blockincraft.kingdoms.data.PlayerDataStore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class KingdomsCompleter implements TabCompleter {
    private final Registry registry = new Registry();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completion = new ArrayList<>();

        if (sender instanceof Player player) {
            if (args.length == 1) {
                if ("help".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("help");
                if ("create".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("create");
                if ("extend".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("extend");
                if ("rename".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("rename");
                if ("delete".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("delete");
                if ("invite".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("invite");
                if ("sethome".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("sethome");
                if ("home".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("home");
                if ("kick".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("kick");
                if ("promote".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("promote");
                if ("demote".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("demote");
                if ("teleport".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("teleport");
                if ("commission".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("commission");
                if ("construction".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("construction");
                if ("ranking".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("ranking");
                if ("join".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("join");
                if ("leave".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("leave");
                if ("deposit".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("deposit");
                if ("withdraw".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("withdraw");
                if ("consume".startsWith(args[0].toLowerCase(Locale.ROOT))) completion.add("consume");
             } else {
                if (args[0].equalsIgnoreCase("create")) {
                    if (args.length == 2) {
                        completion.add("<name>");
                    }
                } else if (args[0].equalsIgnoreCase("extend")) {
                    if (args.length == 2) {
                        completion.add("<length>");
                    } else if (args.length == 3) {
                        if ("north".startsWith(args[2].toLowerCase(Locale.ROOT))) completion.add("north");
                        if ("south".startsWith(args[2].toLowerCase(Locale.ROOT))) completion.add("south");
                        if ("east".startsWith(args[2].toLowerCase(Locale.ROOT))) completion.add("east");
                        if ("west".startsWith(args[2].toLowerCase(Locale.ROOT))) completion.add("west");
                    }
                } else if (args[0].equalsIgnoreCase("rename")) {
                    completion.add("<name>");
                } else if (args[0].equalsIgnoreCase("invite")) {
                    if (args.length == 2) {
                        KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());

                        if (kingdom != null) {
                            Set<Player> players = new HashSet<>(Bukkit.getOnlinePlayers());

                            players.removeIf(playerIn -> {
                                return kingdom.getMembers().containsKey(playerIn.getUniqueId()) || playerIn.getUniqueId() == player.getUniqueId();
                            });

                            players.forEach(playerIn -> {
                                if (playerIn.getName().toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))) {
                                    completion.add(playerIn.getName());
                                }
                            });
                        }
                    }
                } else if (args[0].equalsIgnoreCase("kick")) {
                    if (args.length == 2) {
                        KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());

                        if (kingdom != null) {
                            Set<UUID> players = kingdom.getMembers().keySet();

                            players.remove(player.getUniqueId());

                            players.forEach(playerId -> {
                                Player playerIn = Bukkit.getPlayer(playerId);
                                if (playerIn != null) {
                                    if (playerIn.getName().toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))) {
                                        completion.add(playerIn.getName());
                                    }
                                }
                            });
                        }
                    }
                } else if (args[0].equalsIgnoreCase("promote")) {
                    if (args.length == 2) {
                        KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());

                        if (kingdom != null) {
                            Set<UUID> players = kingdom.getMembers().keySet();

                            players.remove(player.getUniqueId());

                            players.forEach(playerId -> {
                                Player playerIn = Bukkit.getPlayer(playerId);
                                if (playerIn != null) {
                                    if (playerIn.getName().toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))) {
                                        completion.add(playerIn.getName());
                                    }
                                }
                            });
                        }
                    }
                } else if (args[0].equalsIgnoreCase("demote")) {
                    if (args.length == 2) {
                        KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());

                        if (kingdom != null) {
                            Set<UUID> players = kingdom.getMembers().keySet();

                            players.remove(player.getUniqueId());

                            players.forEach(playerId -> {
                                Player playerIn = Bukkit.getPlayer(playerId);
                                if (playerIn != null) {
                                    if (playerIn.getName().toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))) {
                                        completion.add(playerIn.getName());
                                    }
                                }
                            });
                        }
                    }
                } else if (args[0].equalsIgnoreCase("construction")) {
                    if (args.length == 2) {
                        if ("info".startsWith(args[1].toLowerCase(Locale.ROOT))) completion.add("info");
                        if ("create".startsWith(args[1].toLowerCase(Locale.ROOT))) completion.add("create");
                        if ("extend".startsWith(args[1].toLowerCase(Locale.ROOT))) completion.add("extend");
                        if ("delete".startsWith(args[1].toLowerCase(Locale.ROOT))) completion.add("delete");
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("create")) {
                        for (Constructions value : Constructions.values()) {
                            if (value.name().toLowerCase(Locale.ROOT).startsWith(args[2].toLowerCase(Locale.ROOT))) completion.add(value.name().toLowerCase(Locale.ROOT));
                        }
                    } else if (args[1].equalsIgnoreCase("extend")) {
                        if (args.length == 3) {
                            completion.add("<length>");
                        } else if (args.length == 4) {
                            if ("north".startsWith(args[3].toLowerCase(Locale.ROOT))) completion.add("north");
                            if ("south".startsWith(args[3].toLowerCase(Locale.ROOT))) completion.add("south");
                            if ("east".startsWith(args[3].toLowerCase(Locale.ROOT))) completion.add("east");
                            if ("west".startsWith(args[3].toLowerCase(Locale.ROOT))) completion.add("west");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("join")) {
                    if (args.length == 2) {
                        PlayerData playerData = PlayerDataStore.getOrCreate(player.getUniqueId());

                        for (Long invite : playerData.getInvites()) {
                            if (invite.toString().startsWith(args[1])) completion.add(invite.toString());
                        }
                    }
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if (args.length == 2) {
                        completion.add("<amount>");
                    }
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    if (args.length == 2) {
                        completion.add("<amount>");
                    }
                }
            }
        }

        return completion;
    }
}
