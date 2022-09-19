package fr.blockincraft.kingdoms.command.executor;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import fr.blockincraft.kingdoms.core.dto.PersonalClaimFullDTO;
import fr.blockincraft.kingdoms.data.PlayerData;
import fr.blockincraft.kingdoms.data.PlayerDataStore;
import fr.blockincraft.kingdoms.util.Lang;
import fr.blockincraft.kingdoms.util.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public class ClaimsExecutor implements CommandExecutor {
    private final Registry registry = Kingdoms.getInstance().getRegistry();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.ONLY_PLAYERS_CAN_EXECUTE_THIS_COMMAND.get())
                    .split("\\|"));
            return true;
        }

        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.claims.create")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                if (registry.getPlayerPersonalClaimFull(player.getUniqueId()) != null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.ALREADY_HAVE_A_CLAIM.get())
                            .split("\\|"));
                    return true;
                }

                PlayerData playerData = PlayerDataStore.getOrCreate(player.getUniqueId());
                if (playerData.getFirstLocation() == null || playerData.getSecondLocation() == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NEED_TO_SELECT_A_AREA_TO_CLAIM.get())
                            .split("\\|"));
                    return true;
                }

                AreaDTO area = new AreaDTO();
                World world = playerData.getFirstLocation().getWorld();
                Location firstLoc = playerData.getFirstLocation();
                Location secondLoc = playerData.getSecondLocation();

                area.setLocations(firstLoc.getBlockX(), world.getMinHeight(), firstLoc.getBlockZ(), secondLoc.getBlockX(), world.getMaxHeight(), secondLoc.getBlockZ());
                area.setWorld(world.getUID());

                if (registry.areaTouchAPersonalClaim(area) || registry.areaTouchAKingdomClaim(area)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.A_CLAIM_IS_ALREADY_IN_AREA.get())
                            .split("\\|"));
                    return true;
                }

                if (Kingdoms.getInstance().getWorldGuardWrapper() != null && !Kingdoms.getInstance().getWorldGuardWrapper().canClaim(area, player)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_CONTAIN_A_PROTECTED_REGION.get())
                            .split("\\|"));
                    return true;
                }

                if (area.getSize2D() > Parameters.personalClaimBlocks) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_IS_TOO_BIG.get()
                                    .replace("<size>", String.valueOf(area.getSize2D()))
                                    .replace("<max_size>", String.valueOf(Parameters.personalClaimBlocks)))
                            .split("\\|"));
                    return true;
                }

                if (area.getSize2D() < Parameters.minPersonalClaimSize) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_IS_TOO_SMALL.get()
                                    .replace("<size>", String.valueOf(area.getSize2D()))
                                    .replace("<min_size>", String.valueOf(Parameters.minPersonalClaimSize)))
                            .split("\\|"));
                    return true;
                }

                PersonalClaimFullDTO dto = new PersonalClaimFullDTO();
                dto.setOwner(player.getUniqueId());
                dto.setArea(area);
                registry.registerClaim(dto);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_CLAIM_CREATION.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("extend")) {
            if (args.length == 3) {
                if (!player.hasPermission("kingdoms.claims.extend")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                if (registry.getPlayerPersonalClaimFull(player.getUniqueId()) == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_CLAIM.get())
                            .split("\\|"));
                    return true;
                }

                int length = 0;
                try {
                    length = Integer.parseInt(args[1]);

                    if (length == 0) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INVALID_NUMBER.get())
                                .split("\\|"));
                        return true;
                    }
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INVALID_NUMBER.get())
                            .split("\\|"));
                    e.printStackTrace();
                    return true;
                }

                BlockFace direction = getDirection(args[2]);

                if (direction == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INVALID_CLAIM_DIRECTION.get())
                            .split("\\|"));
                    return true;
                }

                AreaDTO area = registry.getPlayerPersonalClaimFull(player.getUniqueId()).getArea();
                area.extend(direction, length);

                if (registry.areaTouchAKingdomClaim(area, area.getId()) || registry.areaTouchAPersonalClaim(area, area.getId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.A_CLAIM_IS_ALREADY_IN_AREA.get())
                            .split("\\|"));
                    area.extend(direction, -length);
                    return true;
                }

                if (Kingdoms.getInstance().getWorldGuardWrapper() != null && !Kingdoms.getInstance().getWorldGuardWrapper().canClaim(area, player)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_CONTAIN_A_PROTECTED_REGION.get())
                            .split("\\|"));
                    area.extend(direction, -length);
                    return true;
                }

                if (area.getSize2D() > Parameters.personalClaimBlocks) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_IS_TOO_BIG.get()
                                    .replace("<size>", String.valueOf(area.getSize2D()))
                                    .replace("<max_size>", String.valueOf(Parameters.personalClaimBlocks)))
                            .split("\\|"));
                    area.extend(direction, -length);
                    return true;
                }

                if (area.getSize2D() < Parameters.minPersonalClaimSize) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_IS_TOO_SMALL.get()
                                    .replace("<size>", String.valueOf(area.getSize2D()))
                                    .replace("<min_size>", String.valueOf(Parameters.minPersonalClaimSize)))
                            .split("\\|"));
                    area.extend(direction, -length);
                    return true;
                }

                registry.updateClaim(registry.getPlayerPersonalClaimFull(player.getUniqueId()));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_CLAIM_EXTENSION.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.claims.delete")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                if (registry.getPlayerPersonalClaimFull(player.getUniqueId()) == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_CLAIM.get())
                            .split("\\|"));
                    return true;
                }

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SEND_DELETE_CONFIRM.get()
                                .replace("<command>", command.getName()))
                        .split("\\|"));
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
                if (registry.getPlayerPersonalClaimFull(player.getUniqueId()) == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_CLAIM.get())
                            .split("\\|"));
                    return true;
                }

                registry.removeClaim(registry.getPlayerPersonalClaimFull(player.getUniqueId()));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_CLAIM_DELETION.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("trust")) {
            if (args.length == 2) {
                if (!player.hasPermission("kingdoms.claims.trust")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                PersonalClaimFullDTO claim = registry.getPlayerPersonalClaimFull(player.getUniqueId());
                if (claim == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_CLAIM.get())
                            .split("\\|"));
                    return true;
                }

                Player playerToTrust = Bukkit.getPlayer(args[1]);

                if (playerToTrust == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.UNKNOWN_PLAYER.get()
                            .replace("<player_name>", args[1]))
                            .split("\\|"));
                    return true;
                }

                if (playerToTrust.getUniqueId() == player.getUniqueId()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CANNOT_TRUST_YOURSELF.get())
                            .split("\\|"));
                    return true;
                }

                if (claim.getTrustedPlayers().contains(playerToTrust.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_ALREADY_TRUST.get()
                            .replace("<player_name>", playerToTrust.getDisplayName()))
                            .split("\\|"));
                    return true;
                }

                claim.getTrustedPlayers().add(playerToTrust.getUniqueId());
                registry.updateClaim(claim);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_PLAYER_TRUST.get()
                        .replace("<player_name>", playerToTrust.getDisplayName()))
                        .split("\\|"));
                playerToTrust.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_TRUST_YOU.get()
                        .replace("<player_name>", player.getDisplayName()))
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("untrust")) {
            if (args.length == 2) {
                if (!player.hasPermission("kingdoms.claims.untrust")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                PersonalClaimFullDTO claim = registry.getPlayerPersonalClaimFull(player.getUniqueId());
                if (claim == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_CLAIM.get())
                            .split("\\|"));
                    return true;
                }

                Player playerToUnTrust = Bukkit.getPlayer(args[1]);

                if (playerToUnTrust == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.UNKNOWN_PLAYER.get()
                            .replace("<player_name>", args[1]))
                            .split("\\|"));
                    return true;
                }

                if (playerToUnTrust.getUniqueId() == player.getUniqueId()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CANNOT_UNTRUST_YOURSELF.get())
                            .split("\\|"));
                    return true;
                }

                if (!claim.getTrustedPlayers().contains(playerToUnTrust.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_WAS_NOT_TRUST.get()
                            .replace("<player_name>", playerToUnTrust.getDisplayName()))
                            .split("\\|"));
                    return true;
                }

                claim.getTrustedPlayers().remove(playerToUnTrust.getUniqueId());
                registry.updateClaim(claim);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_PLAYER_UNTRUST.get()
                        .replace("<player_name>", playerToUnTrust.getDisplayName()))
                        .split("\\|"));
                playerToUnTrust.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_UNTRUST_YOU.get()
                        .replace("<player_name>", player.getDisplayName()))
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length <= 2) {
                if (!player.hasPermission("kingdoms.claims.info")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                PersonalClaimFullDTO claim = registry.getPlayerPersonalClaimFull(player.getUniqueId());
                List<PersonalClaimFullDTO> trustedClaims = registry.getPlayerTrustedPersonalClaimsFull(player.getUniqueId());

                boolean all = args.length == 1 || args[1].equalsIgnoreCase("all");

                if (all || args[1].equalsIgnoreCase("mine")) {
                    if (claim == null) {
                        if (!all || trustedClaims.size() == 0) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_CLAIM.get())
                                    .split("\\|"));
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_CLAIM_INFO.get()
                                .replace("<size>", String.valueOf(claim.getArea().getSize2D()))
                                .replace("<max_size>", String.valueOf(Parameters.personalClaimBlocks))
                                .replace("<smallest_x>", String.valueOf(claim.getArea().getSmallestX()))
                                .replace("<smallest_z>", String.valueOf(claim.getArea().getSmallestZ()))
                                .replace("<biggest_x>", String.valueOf(claim.getArea().getBiggestX()))
                                .replace("<biggest_z>", String.valueOf(claim.getArea().getBiggestZ()))
                                .replace("<owner>", Bukkit.getPlayer(claim.getOwner()) == null ? "" : Bukkit.getPlayer(claim.getOwner()).getDisplayName())
                                .replace("<trusted_players>", claim.getTrustedAsString()))
                                .split("\\|"));
                    }
                }
                if (all || args[1].equalsIgnoreCase("trusted")) {
                    if (trustedClaims.size() == 0) {
                        if (!all) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TRUST_IN_NO_CLAIM.get())
                                    .split("\\|"));
                            return true;
                        }
                    } else {
                        for (PersonalClaimFullDTO trustedClaim : trustedClaims) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TRUSTED_CLAIM_INFO.get()
                                    .replace("<size>", String.valueOf(trustedClaim.getArea().getSize2D()))
                                    .replace("<max_size>", String.valueOf(Parameters.personalClaimBlocks))
                                    .replace("<smallest_x>", String.valueOf(trustedClaim.getArea().getSmallestX()))
                                    .replace("<smallest_z>", String.valueOf(trustedClaim.getArea().getSmallestZ()))
                                    .replace("<biggest_x>", String.valueOf(trustedClaim.getArea().getBiggestX()))
                                    .replace("<biggest_z>", String.valueOf(trustedClaim.getArea().getBiggestZ()))
                                    .replace("<owner>", Bukkit.getPlayer(trustedClaim.getOwner()) == null ? "" : Bukkit.getPlayer(trustedClaim.getOwner()).getDisplayName())
                                    .replace("<trusted_players>", trustedClaim.getTrustedAsString()))
                                    .split("\\|"));
                        }
                    }
                }

                return true;
            }
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAD_COMMAND_USAGE.get()
                .replace("<command>", command.getName()))
                .split("\\|"));
        return true;
    }

    public void sendHelpMessage(CommandSender receiver) {
        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CLAIM_HELP_MESSAGE.get())
                .split("\\|"));
    }

    public BlockFace getDirection(String text) {
        return switch (text.toLowerCase(Locale.ROOT)) {
            case "north" -> BlockFace.NORTH;
            case "south" -> BlockFace.SOUTH;
            case "east" -> BlockFace.EAST;
            case "west" -> BlockFace.WEST;
            default -> null;
        };
    }
}
