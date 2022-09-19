package fr.blockincraft.kingdoms.command.executor;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import fr.blockincraft.kingdoms.core.dto.CurrentCommissionDTO;
import fr.blockincraft.kingdoms.core.dto.KingdomClaimDTO;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.enums.*;
import fr.blockincraft.kingdoms.data.PlayerData;
import fr.blockincraft.kingdoms.data.PlayerDataStore;
import fr.blockincraft.kingdoms.menu.RankingMenu;
import fr.blockincraft.kingdoms.menu.TeleportMenu;
import fr.blockincraft.kingdoms.util.Lang;
import fr.blockincraft.kingdoms.util.Parameters;
import fr.blockincraft.kingdoms.util.TextUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KingdomsExecutor implements CommandExecutor {
    private static final Registry registry = Kingdoms.getInstance().getRegistry();

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
            if (args.length >= 2) {
                if (!player.hasPermission("kingdoms.kingdoms.create")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom != null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.ALREADY_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                PlayerData playerData = PlayerDataStore.getOrCreate(player.getUniqueId());
                if (playerData.getFirstLocation() == null || playerData.getSecondLocation() == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NEED_TO_SELECT_A_AREA_TO_CREATE_A_KINGDOM.get())
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

                if (area.getSize2D() > Parameters.kingdomClaimBlocks) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_IS_TOO_BIG.get()
                                    .replace("<size>", String.valueOf(area.getSize2D()))
                                    .replace("<max_size>", String.valueOf(Parameters.kingdomClaimBlocks)))
                            .split("\\|"));
                    return true;
                }

                if (area.getSize2D() < Parameters.minKingdomClaimSize) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_IS_TOO_SMALL.get()
                                    .replace("<size>", String.valueOf(area.getSize2D()))
                                    .replace("<min_size>", String.valueOf(Parameters.minKingdomClaimSize)))
                            .split("\\|"));
                    return true;
                }

                if (!area.isIn(player.getLocation())) {
                    return true;
                }

                StringBuilder name = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    if (!name.toString().equalsIgnoreCase("")) {
                        name.append(" ");
                    }
                    name.append(args[i]);
                }

                String noColorName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name.toString()));

                if (noColorName.replace(" ", "").equalsIgnoreCase("")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NAME_CANNOT_BE_EMPTY.get())
                            .split("\\|"));
                    return true;
                }

                if (noColorName.replace(" ", "").length() < Parameters.minNameLength) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NAME_TOO_SHORT.get()
                                    .replace("<min_length>", String.valueOf(Parameters.minNameLength)))
                            .split("\\|"));
                    return true;
                }

                if (noColorName.length() > Parameters.maxNameLength) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NAME_TOO_LONG.get()
                                    .replace("<max_length>", String.valueOf(Parameters.maxNameLength)))
                            .split("\\|"));
                    return true;
                }

                if (registry.nameAlreadyTaken(noColorName)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NAME_ALREADY_TAKEN.get())
                            .split("\\|"));
                    return true;
                }

                KingdomClaimDTO claim = new KingdomClaimDTO();
                claim.setArea(area);
                CurrentCommissionDTO cc = new CurrentCommissionDTO();
                cc.setCommission(Commissions.getRandom());
                cc.setProgression(0);
                cc.setObjective(cc.getCommission().objective.calculation(0));
                KingdomFullDTO newKingdom = new KingdomFullDTO();
                newKingdom.setName(name.toString());
                newKingdom.setHome(player.getLocation());
                newKingdom.setClaim(claim);
                newKingdom.getMembers().put(player.getUniqueId(), KingdomPermissionLevels.OWNER);
                newKingdom.getCurrentCommissions().put(player.getUniqueId(), cc);
                newKingdom.getCompletedCommissions().put(player.getUniqueId(), 0);
                registry.registerKingdom(newKingdom);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_KINGDOM_CREATION.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("extend")) {
            if (args.length == 3) {
                if (!player.hasPermission("kingdoms.kingdoms.extend")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.EDIT_CLAIMS, player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
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
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INVALID_KINGDOM_DIRECTION.get())
                            .split("\\|"));
                    return true;
                }

                AreaDTO area = kingdom.getClaim().getArea();
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

                if (area.getSize2D() > Parameters.kingdomClaimBlocks) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_IS_TOO_BIG.get()
                                    .replace("<size>", String.valueOf(area.getSize2D()))
                                    .replace("<max_size>", String.valueOf(Parameters.kingdomClaimBlocks)))
                            .split("\\|"));
                    area.extend(direction, -length);
                    return true;
                }

                if (area.getSize2D() < Parameters.minKingdomClaimSize) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_IS_TOO_SMALL.get()
                                    .replace("<size>", String.valueOf(area.getSize2D()))
                                    .replace("<min_size>", String.valueOf(Parameters.minKingdomClaimSize)))
                            .split("\\|"));
                    area.extend(direction, -length);
                    return true;
                }

                if (!area.isIn(kingdom.getHomeAsBukkitLoc())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.KINGDOM_HOME_MUST_BE_IN_KINGDOM.get()
                                    .replace("<home_x>", String.valueOf(kingdom.getHomeAsBukkitLoc().getBlockX()))
                                    .replace("<home_y>", String.valueOf(kingdom.getHomeAsBukkitLoc().getBlockY()))
                                    .replace("<home_z>", String.valueOf(kingdom.getHomeAsBukkitLoc().getBlockZ())))
                            .split("\\|"));
                    area.extend(direction, -length);
                    return true;
                }

                registry.updateKingdom(kingdom);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_KINGDOM_EXTENSION.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("rename")) {
            if (args.length >= 2) {
                if (!player.hasPermission("kingdoms.kingdoms.rename")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.EDIT_TOWN_HALL, player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                StringBuilder name = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    if (!name.toString().equalsIgnoreCase("")) {
                        name.append(" ");
                    }
                    name.append(args[i]);
                }

                String noColorName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name.toString()));

                if (noColorName.replace(" ", "").equalsIgnoreCase("")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NAME_CANNOT_BE_EMPTY.get())
                            .split("\\|"));
                    return true;
                }

                if (noColorName.replace(" ", "").length() < Parameters.minNameLength) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NAME_TOO_SHORT.get()
                                    .replace("<min_length>", String.valueOf(Parameters.minNameLength)))
                            .split("\\|"));
                    return true;
                }

                if (noColorName.length() > Parameters.maxNameLength) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NAME_TOO_LONG.get()
                                    .replace("<max_length>", String.valueOf(Parameters.maxNameLength)))
                            .split("\\|"));
                    return true;
                }

                if (registry.nameAlreadyTaken(noColorName)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NAME_ALREADY_TAKEN.get())
                            .split("\\|"));
                    return true;
                }

                kingdom.setName(name.toString());
                registry.updateKingdom(kingdom);

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_KINGDOM_RENAME.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.kingdoms.delete")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!kingdom.isOwner(player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.ONLY_OWNER_CAN_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SEND_DELETE_CONFIRM.get()
                                .replace("<command>", command.getName()))
                        .split("\\|"));
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!kingdom.isOwner(player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.ONLY_OWNER_CAN_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                registry.removeKingdom(kingdom);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_KINGDOM_DELETION.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("sethome")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.kingdoms.sethome")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.EDIT_CLAIMS, player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                if (!kingdom.getClaim().getArea().isIn(player.getLocation())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.KINGDOM_HOME_MUST_BE_IN_KINGDOM.get()
                                    .replace("<home_x>", String.valueOf(kingdom.getHomeAsBukkitLoc().getBlockX()))
                                    .replace("<home_y>", String.valueOf(kingdom.getHomeAsBukkitLoc().getBlockY()))
                                    .replace("<home_z>", String.valueOf(kingdom.getHomeAsBukkitLoc().getBlockZ())))
                            .split("\\|"));
                    return true;
                }

                kingdom.setHome(player.getLocation());
                registry.updateKingdom(kingdom);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_KINGDOM_SETHOME.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("home")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.kingdoms.home")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                Location location = kingdom.getHomeAsBukkitLoc();
                location.setYaw(player.getLocation().getYaw());
                location.setPitch(player.getLocation().getPitch());

                player.teleport(location);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_TELEPORT_TO_KINGDOM_HOME.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("invite")) {
            if (args.length == 2) {
                if (!player.hasPermission("kingdoms.kingdoms.invite")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.INVITE_PLAYERS, player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                Player playerToInvite = Bukkit.getPlayer(args[1]);

                if (playerToInvite == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.UNKNOWN_PLAYER.get()
                            .replace("<player_name>", args[1]))
                            .split("\\|"));
                    return true;
                }

                if (playerToInvite.getUniqueId() == player.getUniqueId()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CANNOT_INVITE_YOURSELF.get())
                            .split("\\|"));
                    return true;
                }

                if (kingdom.isMember(playerToInvite.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_ALREADY_MEMBER.get()
                            .replace("<player_name>", playerToInvite.getDisplayName()))
                            .split("\\|"));
                    return true;
                }

                PlayerData playerToInviteData = PlayerDataStore.getOrCreate(playerToInvite.getUniqueId());
                if (playerToInviteData.getInvites().contains(kingdom.getId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_ALREADY_INVITED.get()
                            .replace("<player_name>", playerToInvite.getDisplayName()))
                            .split("\\|"));
                    return true;
                }

                PlayerDataStore.invite(playerToInvite, player, kingdom);
                for (Player member : kingdom.getOnlineMembers()) {
                    member.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.MEMBER_INVITE_PLAYER.get()
                                    .replace("<player_name>", playerToInvite.getDisplayName())
                                    .replace("<inviter_name>", player.getDisplayName()))
                            .split("\\|"));
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("kick")) {
            if (args.length == 2) {
                if (!player.hasPermission("kingdoms.kingdoms.kick")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.KICK_MEMBERS, player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                Player playerToKick = Bukkit.getPlayer(args[1]);

                if (playerToKick == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.UNKNOWN_PLAYER.get()
                            .replace("<player_name>", args[1]))
                            .split("\\|"));
                    return true;
                }

                if (playerToKick.getUniqueId() == player.getUniqueId()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CANNOT_KICK_YOURSELF.get())
                            .split("\\|"));
                    return true;
                }

                if (!kingdom.isMember(playerToKick.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_WAS_NOT_MEMBER.get()
                            .replace("<player_name>", playerToKick.getDisplayName()))
                            .split("\\|"));
                    return true;
                }

                if (kingdom.getMembers().get(player.getUniqueId()).getRankLevel() >= kingdom.getMembers().get(playerToKick.getUniqueId()).getRankLevel()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CANNOT_KICK_THIS_PLAYER.get()
                            .replace("<player_name>", playerToKick.getDisplayName()))
                            .split("\\|"));
                    return true;
                }

                kingdom.getMembers().remove(playerToKick.getUniqueId());
                registry.updateKingdom(kingdom);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_KICK_PLAYER.get()
                                .replace("<player_name>", playerToKick.getDisplayName()))
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("promote")) {
            if (args.length == 2) {
                if (!player.hasPermission("kingdoms.kingdoms.promote")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.PROMOTE_MEMBERS, player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                Player playerToPromote = Bukkit.getPlayer(args[1]);

                if (playerToPromote == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.UNKNOWN_PLAYER.get()
                                    .replace("<player_name>", args[1]))
                            .split("\\|"));
                    return true;
                }

                if (playerToPromote.getUniqueId() == player.getUniqueId()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CANNOT_PROMOTE_YOURSELF.get())
                            .split("\\|"));
                    return true;
                }

                if (!kingdom.isMember(playerToPromote.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_WAS_NOT_MEMBER.get()
                                    .replace("<player_name>", playerToPromote.getDisplayName()))
                            .split("\\|"));
                    return true;
                }

                if (kingdom.getMembers().get(player.getUniqueId()).getRankLevel() + 1 >= kingdom.getMembers().get(playerToPromote.getUniqueId()).getRankLevel()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CANNOT_PROMOTE_THIS_PLAYER.get()
                                    .replace("<player_name>", playerToPromote.getDisplayName()))
                            .split("\\|"));
                    return true;
                }

                kingdom.getMembers().put(playerToPromote.getUniqueId(), KingdomPermissionLevels.getByLevel(kingdom.getMembers().get(playerToPromote.getUniqueId()).getRankLevel() - 1));
                registry.updateKingdom(kingdom);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_PROMOTE_PLAYER.get()
                                .replace("<player_name>", playerToPromote.getDisplayName())
                                .replace("<rank>", Lang.getFrom(kingdom.getMembers().get(playerToPromote.getUniqueId()))))
                        .split("\\|"));
                playerToPromote.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_PROMOTED_YOU.get()
                                .replace("<player_name>", player.getDisplayName())
                                .replace("<rank>", Lang.getFrom(kingdom.getMembers().get(playerToPromote.getUniqueId()))))
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("demote")) {
            if (args.length == 2) {
                if (!player.hasPermission("kingdoms.kingdoms.demote")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.DEMOTE_MEMBERS, player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                Player playerToDemote = Bukkit.getPlayer(args[1]);

                if (playerToDemote == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.UNKNOWN_PLAYER.get()
                            .replace("<player_name>", args[1]))
                            .split("\\|"));
                    return true;
                }

                if (playerToDemote.getUniqueId() == player.getUniqueId()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CANNOT_DEMOTE_YOURSELF.get())
                            .split("\\|"));
                    return true;
                }

                if (!kingdom.isMember(playerToDemote.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_WAS_NOT_MEMBER.get()
                            .replace("<player_name>", playerToDemote.getDisplayName()))
                            .split("\\|"));
                    return true;
                }

                if (kingdom.getMembers().get(player.getUniqueId()).getRankLevel() >= kingdom.getMembers().get(playerToDemote.getUniqueId()).getRankLevel()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CANNOT_DEMOTE_THIS_PLAYER.get()
                                    .replace("<player_name>", playerToDemote.getDisplayName()))
                            .split("\\|"));
                    return true;
                }

                if (kingdom.getMembers().get(playerToDemote.getUniqueId()).getRankLevel() == KingdomPermissionLevels.getLowestRank().getRankLevel()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CANNOT_DEMOTE_THIS_PLAYER.get()
                                    .replace("<player_name>", playerToDemote.getDisplayName()))
                            .split("\\|"));
                    return true;
                }

                kingdom.getMembers().put(playerToDemote.getUniqueId(), KingdomPermissionLevels.getByLevel(kingdom.getMembers().get(playerToDemote.getUniqueId()).getRankLevel() + 1));
                registry.updateKingdom(kingdom);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_DEMOTE_PLAYER.get()
                                .replace("<player_name>", playerToDemote.getDisplayName())
                                .replace("<rank>", Lang.getFrom(kingdom.getMembers().get(playerToDemote.getUniqueId()))))
                        .split("\\|"));
                playerToDemote.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_DEMOTED_YOU.get()
                                .replace("<player_name>", player.getDisplayName())
                                .replace("<rank>", Lang.getFrom(kingdom.getMembers().get(playerToDemote.getUniqueId()))))
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("teleport")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.kingdoms.teleport")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                TeleportMenu menu = new TeleportMenu();
                menu.open(player);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("commission")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.kingdoms.commission")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                CurrentCommissionDTO commission = kingdom.getCurrentCommissions().get(player.getUniqueId());
                if (commission == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.UNEXPECTED_ERROR_OCCURRED.get())
                            .split("\\|"));
                    return true;
                }

                String[] messages = ChatColor.translateAlternateColorCodes('&', Lang.KINGDOM_COMMISSION_INFO_MESSAGE.get()
                                .replace("<commission_name>", Lang.getFrom(commission.getCommission()))
                                .replace("<commission_desc>", Lang.getCommissionDesc(commission.getCommission()))
                                .replace("<progression>", String.valueOf(commission.getProgression()))
                                .replace("<objective>", String.valueOf(commission.getObjective()))
                                .replace("<completed_commissions>", String.valueOf(kingdom.getCompletedCommissions().get(player.getUniqueId()))))
                                .split("\\|");

                int barLength = Lang.KINGDOM_COMMISSION_PROGRESS_BAR.get().length();
                int barProgress = (int) ((double) barLength / (double) commission.getObjective() * (double) commission.getProgression());
                StringBuilder bar = new StringBuilder(Lang.KINGDOM_COMMISSION_PROGRESS_BAR.get());
                bar.insert(barProgress, "&8");

                for (int i = 0; i < messages.length; i++) {
                    String message = messages[i];
                    messages[i] = message.replace("<progress_bar>", ChatColor.translateAlternateColorCodes('&', bar.toString()));
                }

                sender.sendMessage(messages);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("construction")) {
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("info")) {
                    if (!player.hasPermission("kingdoms.kingdoms.constructions.info")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                                .split("\\|"));
                        return true;
                    }

                    KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                    if (kingdom == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                                .split("\\|"));
                        return true;
                    }

                    if (kingdom.getConstructions().size() == 0) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_CONSTRUCTIONS_IN_KINGDOM.get())
                                .split("\\|"));
                        return true;
                    }

                    KingdomLevels kingdomLevel = kingdom.getKingdomLevel();
                    for (Map.Entry<AreaDTO, Constructions> entry : kingdom.getConstructions().entrySet()) {
                        Constructions construction = entry.getValue();
                        AreaDTO area = entry.getKey();

                        int level = Constructions.getLevel(construction, area, kingdomLevel);
                        int points = Constructions.getPoints(construction, level);

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CONSTRUCTION_INFO_MESSAGE.get()
                                        .replace("<construction_name>", Lang.getFrom(construction))
                                        .replace("<level>", String.valueOf(level))
                                        .replace("<max_level>", String.valueOf(construction.requirements.size()))
                                        .replace("<points>", String.valueOf(points))
                                        .replace("<size>", String.valueOf(area.getSize3D()))
                                        .replace("<smallest_x>", String.valueOf(area.getSmallestX()))
                                        .replace("<smallest_z>", String.valueOf(area.getSmallestZ()))
                                        .replace("<biggest_x>", String.valueOf(area.getBiggestX()))
                                        .replace("<biggest_z>", String.valueOf(area.getBiggestZ())))
                                .split("\\|"));
                    }
                    return true;
                } else if (args[1].equalsIgnoreCase("delete")) {
                    if (!player.hasPermission("kingdoms.kingdoms.constructions.delete")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                                .split("\\|"));
                        return true;
                    }

                    KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                    if (kingdom == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                                .split("\\|"));
                        return true;
                    }

                    if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.DELETE_CONSTRUCTIONS, player.getUniqueId())) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                                .split("\\|"));
                        return true;
                    }

                    if (kingdom.getConstructions().size() == 0) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_CONSTRUCTIONS_IN_KINGDOM.get())
                                .split("\\|"));
                        return true;
                    }

                    if (!kingdom.isInConstruction(player.getLocation())) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NEED_TO_BE_IN_A_CONSTRUCTION.get())
                                .split("\\|"));
                        return true;
                    }

                    if (kingdom.getConstructionAt(player.getLocation()) == Constructions.TOWN_HALL && !registry.canDoInKingdom(KingdomPermissionLevels.Permissions.EDIT_TOWN_HALL, player.getUniqueId())) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                                .split("\\|"));
                        return true;
                    }

                    kingdom.getConstructions().remove(kingdom.getConstructionAreaAt(player.getLocation()));
                    registry.updateKingdom(kingdom);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_CONSTRUCTION_DELETION.get())
                            .split("\\|"));
                    return true;
                }
            } else if (args.length == 3 && args[1].equalsIgnoreCase("create")) {
                if (!player.hasPermission("kingdoms.kingdoms.constructions.create")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.CREATE_CONSTRUCTIONS, player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                Constructions construction = null;

                for (Constructions value : Constructions.values()) {
                    if (value.name().equalsIgnoreCase(args[2])) {
                        construction = value;
                        break;
                    }
                }

                if (construction == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INVALID_CONSTRUCTION_NAME.get())
                            .split("\\|"));
                    return true;
                }

                if (!kingdom.canBuildMore(construction)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_REACHED_THE_LIMIT_OF_THIS_CONSTRUCTION.get()
                                    .replace("<max>", String.valueOf(construction.maxAmount))
                                    .replace("<construction>", Lang.getFrom(construction)))
                            .split("\\|"));
                    return true;
                }

                PlayerData playerData = PlayerDataStore.getOrCreate(player.getUniqueId());
                if (playerData.getFirstLocation() == null || playerData.getSecondLocation() == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NEED_TO_SELECT_A_AREA_TO_CREATE_A_CONSTRUCTION.get())
                            .split("\\|"));
                    return true;
                }

                AreaDTO area = new AreaDTO();
                World world = playerData.getFirstLocation().getWorld();
                Location firstLoc = playerData.getFirstLocation();
                Location secondLoc = playerData.getSecondLocation();

                area.setLocations(firstLoc, secondLoc);
                area.setWorld(world.getUID());

                if (!kingdom.getClaim().getArea().isTotallyIn(area)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_NEED_TO_BE_IN_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (kingdom.touchOtherConstructions(area)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_TOUCH_OTHER_CONSTRUCTION.get())
                            .split("\\|"));
                    return true;
                }

                if (area.getBiggestX() - area.getSmallestX() + 1 < construction.requirements.get(1).minSizeX ||
                    area.getBiggestY() - area.getSmallestY() + 1 < construction.requirements.get(1).minSizeY ||
                    area.getBiggestZ() - area.getSmallestZ() + 1 < construction.requirements.get(1).minSizeZ) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CONSTRUCTION_MINIMUM_SIZE.get()
                                    .replace("<min_x>", String.valueOf(construction.requirements.get(1).minSizeX))
                                    .replace("<min_y>", String.valueOf(construction.requirements.get(1).minSizeY))
                                    .replace("<min_z>", String.valueOf(construction.requirements.get(1).minSizeZ)))
                            .split("\\|"));
                    return true;
                }

                kingdom.getConstructions().put(area, construction);
                registry.updateKingdom(kingdom);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_CONSTRUCTION_CREATION.get()
                                .replace("<construction_name>", Lang.getFrom(construction)))
                        .split("\\|"));
                return true;
            } else if (args.length == 4 && args[1].equalsIgnoreCase("extend")) {
                if (!player.hasPermission("kingdoms.kingdoms.constructions.extend")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.EDIT_CONSTRUCTIONS, player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                Constructions construction = kingdom.getConstructionAt(player.getLocation());

                if (construction == null) {
                    return true;
                }

                int length = 0;
                try {
                    length = Integer.parseInt(args[2]);

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

                BlockFace direction = getDirectionWithUpAndDown(args[3]);

                if (direction == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INVALID_CONSTRUCTION_DIRECTION.get())
                            .split("\\|"));
                    return true;
                }

                AreaDTO area = kingdom.getConstructionAreaAt(player.getLocation());
                area.extend(direction, length);

                if (!kingdom.getClaim().getArea().isTotallyIn(area)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_NEED_TO_BE_IN_KINGDOM.get())
                            .split("\\|"));
                    area.extend(direction, -length);
                    return true;
                }

                if (kingdom.touchOtherConstructions(area)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_TOUCH_OTHER_CONSTRUCTION.get())
                            .split("\\|"));
                    area.extend(direction, -length);
                    return true;
                }

                if (area.getBiggestX() - area.getSmallestX() + 1 < construction.requirements.get(1).minSizeX ||
                        area.getBiggestY() - area.getSmallestY() + 1 < construction.requirements.get(1).minSizeY ||
                        area.getBiggestZ() - area.getSmallestZ() + 1 < construction.requirements.get(1).minSizeZ) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CONSTRUCTION_MINIMUM_SIZE.get()
                                    .replace("<min_x>", String.valueOf(construction.requirements.get(1).minSizeX))
                                    .replace("<min_y>", String.valueOf(construction.requirements.get(1).minSizeY))
                                    .replace("<min_z>", String.valueOf(construction.requirements.get(1).minSizeZ)))
                            .split("\\|"));
                    area.extend(direction, -length);
                    return true;
                }

                registry.updateKingdom(kingdom);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_CONSTRUCTION_EXTENSION.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("ranking")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.kingdoms.ranking")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                RankingMenu menu = new RankingMenu();
                menu.open(player);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("join")) {
            if (args.length == 2) {
                if (!player.hasPermission("kingdoms.kingdoms.join")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom != null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.ALREADY_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                long id = 0;
                try {
                    id = Long.parseLong(args[1]);

                    if (id == 0) {
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

                PlayerData playerData = PlayerDataStore.getOrCreate(player.getUniqueId());
                List<Long> invites = playerData.getInvites();
                if (invites == null || invites.size() == 0 || !invites.contains(id)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_ARE_NOT_INVITED_IN_THIS_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdomToJoin = registry.getKingdomById(id);
                if (kingdomToJoin == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.THIS_KINGDOM_NO_LONGER_EXIST.get())
                            .split("\\|"));
                    return true;
                }

                kingdomToJoin.getMembers().put(player.getUniqueId(), KingdomPermissionLevels.getLowestRank());
                CurrentCommissionDTO cc = new CurrentCommissionDTO();
                cc.setCommission(Commissions.getRandom());
                cc.setProgression(0);
                cc.setObjective(cc.getCommission().objective.calculation(0));
                kingdomToJoin.getCurrentCommissions().put(player.getUniqueId(), cc);
                kingdomToJoin.getCompletedCommissions().put(player.getUniqueId(), 0);
                invites.remove(id);
                registry.updateKingdom(kingdomToJoin);
                for (Player member : kingdomToJoin.getOnlineMembers()) {
                    member.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_JOIN_KINGDOM.get()
                                    .replace("<player_name>", player.getDisplayName()))
                            .split("\\|"));
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.kingdoms.leave")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (kingdom.isOwner(player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_CANNOT_LEAVE_YOUR_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                kingdom.getMembers().remove(player.getUniqueId());
                //Todo : keep progression when leave ???
                kingdom.getCompletedCommissions().remove(player.getUniqueId());
                kingdom.getCurrentCommissions().remove(player.getUniqueId());
                registry.updateKingdom(kingdom);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_KINGDOM_LEAVE.get())
                        .split("\\|"));
                for (Player member : kingdom.getOnlineMembers()) {
                    member.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.MEMBER_LEAVE_KINGDOM.get()
                            .replace("<player_name>", player.getDisplayName()))
                            .split("\\| "));
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("deposit")) {
            if (args.length == 2) {
                if (!player.hasPermission("kingdoms.kingdoms.deposit")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.ACCESS_THE_BANK, player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                int amount = 0;
                try {
                    amount = Integer.parseInt(args[1]);

                    if (amount < 1) {
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


                int maxAmount = Parameters.maxMoneyPerBankLevel[kingdom.getBankLevel()];
                if (amount + kingdom.getBank() > maxAmount) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BANK_CAN_ONLY_CONTAIN.get()
                            .replace("%max_amount%", TextUtils.valueWithCommas(maxAmount))
                            .replace("%current_amount%", TextUtils.valueWithCommas(kingdom.getBank())))
                            .split("\\|"));
                    return true;
                }

                Economy economy = Kingdoms.getInstance().getEconomy();

                if (economy.getBalance(player) < amount) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_ENOUGH_MONEY.get())
                            .split("\\|"));
                    return true;
                }

                EconomyResponse response = economy.withdrawPlayer(player, amount);

                if (response.type != EconomyResponse.ResponseType.SUCCESS) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.ERROR_OCCURRED_DURING_TRANSACTION.get())
                            .split("\\|"));
                    return true;
                }

                kingdom.setBank(kingdom.getBank() + amount);

                registry.updateKingdom(kingdom);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_DEPOSIT.get()
                                .replace("%amount%", TextUtils.valueWithCommas(amount)))
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("withdraw")) {
            if (args.length == 2) {
                if (!player.hasPermission("kingdoms.kingdoms.withdraw")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                if (!registry.canDoInKingdom(KingdomPermissionLevels.Permissions.ACCESS_THE_BANK, player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                int amount = 0;
                try {
                    amount = Integer.parseInt(args[1]);

                    if (amount < 1) {
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


                if (amount > kingdom.getBank()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_ENOUGH_MONEY_IN_BANK.get())
                            .split("\\|"));
                    return true;
                }

                Economy economy = Kingdoms.getInstance().getEconomy();

                EconomyResponse response = economy.depositPlayer(player, amount);

                if (response.type != EconomyResponse.ResponseType.SUCCESS) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.ERROR_OCCURRED_DURING_TRANSACTION.get())
                            .split("\\|"));
                    return true;
                }

                kingdom.setBank(kingdom.getBank() - amount);

                registry.updateKingdom(kingdom);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_WITHDRAW.get()
                                .replace("%amount%", TextUtils.valueWithCommas(amount)))
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("consume")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.kingdoms.consume")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                KingdomFullDTO kingdom = registry.getPlayerKingdom(player.getUniqueId());
                if (kingdom == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.DO_NOT_HAVE_A_KINGDOM.get())
                            .split("\\|"));
                    return true;
                }

                ItemStack itemStack = player.getInventory().getItemInMainHand();

                if (itemStack.getType() == Material.AIR) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NEED_ITEM_IN_HAND.get())
                            .split("\\|"));
                    return true;
                }

                if (Kingdoms.getInstance().isSlimefun()) {
                    if (SlimefunItem.getByItem(itemStack) != null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_COLLECTION_OF_THIS_MATERIAL.get())
                                .split("\\|"));
                        return true;
                    }
                }

                if (Kingdoms.getInstance().isItemsAdder()) {
                    if (CustomStack.byItemStack(itemStack) != null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_COLLECTION_OF_THIS_MATERIAL.get())
                                .split("\\|"));
                        return true;
                    }
                }

                Material material = itemStack.getType();
                Collections col = null;

                for (Collections c : Collections.values()) {
                    if (c.material == material) {
                        col = c;
                        break;
                    }
                }

                if (col == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_COLLECTION_OF_THIS_MATERIAL.get())
                            .split("\\|"));
                    return true;
                }

                Integer value = kingdom.getCollections().get(col);
                int collected = value == null ? 0 : value;

                int maxAmount = col.getMaxValue() - collected;
                int amount = itemStack.getAmount();

                if (maxAmount < 1) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.COLLECTION_ALREADY_MAXED.get())
                            .split("\\|"));
                    return true;
                }

                itemStack.setAmount(maxAmount < amount ? amount - maxAmount : 0);
                kingdom.getCollections().put(col, maxAmount < amount ? maxAmount : amount + collected);
                registry.updateKingdom(kingdom);
                if (maxAmount <= amount) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_CONSUME_AND_FINISH.get()
                                    .replace("%amount%", TextUtils.valueWithCommas(amount - maxAmount))
                                    .replace("%material%", material.name().toLowerCase(Locale.ROOT)))
                            .split("\\|"));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_CONSUME.get()
                                    .replace("%amount%", TextUtils.valueWithCommas(amount))
                                    .replace("%material%", material.name().toLowerCase(Locale.ROOT)))
                            .split("\\|"));
                }
                return true;
            }
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAD_COMMAND_USAGE.get())
                .replace("<command>", command.getName())
                .split("\\|"));
        return true;
    }

    public void sendHelpMessage(CommandSender receiver) {
        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.KINGDOM_HELP_MESSAGE.get())
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

    public BlockFace getDirectionWithUpAndDown(String text) {
        return switch (text.toLowerCase(Locale.ROOT)) {
            case "north" -> BlockFace.NORTH;
            case "south" -> BlockFace.SOUTH;
            case "east" -> BlockFace.EAST;
            case "west" -> BlockFace.WEST;
            case "up" -> BlockFace.UP;
            case "down" -> BlockFace.DOWN;
            default -> null;
        };
    }
}
