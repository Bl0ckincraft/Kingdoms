package fr.blockincraft.kingdoms.command.executor;

import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import fr.blockincraft.kingdoms.core.dto.PersonalClaimFullDTO;
import fr.blockincraft.kingdoms.data.PlayerData;
import fr.blockincraft.kingdoms.data.PlayerDataStore;
import fr.blockincraft.kingdoms.task.VisualizeTask;
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

public class AreasExecutor implements CommandExecutor {
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

        if (args[0].equalsIgnoreCase("pos1")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.areas.pos1")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                Location location = player.getLocation();
                PlayerData playerData = PlayerDataStore.getOrCreate(player.getUniqueId());
                playerData.setFirstLocation(location);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_SET_POS_ONE.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("pos2")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.areas.pos2")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                Location location = player.getLocation();
                PlayerData playerData = PlayerDataStore.getOrCreate(player.getUniqueId());
                playerData.setSecondLocation(location);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.SUCCESS_SET_POS_TWO.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("view")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.areas.view")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                boolean viewActive = VisualizeTask.togglePlayer(player);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', viewActive ? Lang.SUCCESS_TOGGLE_VIEW_TO_TRUE.get() : Lang.SUCCESS_TOGGLE_VIEW_TO_FALSE.get())
                        .split("\\|"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 1) {
                if (!player.hasPermission("kingdoms.areas.info")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT.get())
                            .split("\\|"));
                    return true;
                }

                PlayerData playerData = PlayerDataStore.getOrCreate(player.getUniqueId());

                Location firstPos = playerData.getFirstLocation();
                Location secondPos = playerData.getSecondLocation();

                AreaDTO area = null;

                if (firstPos != null && secondPos != null) {
                    area = new AreaDTO();
                    area.setWorld(firstPos.getWorld().getUID());
                    area.setLocations(firstPos, secondPos);
                }

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_INFO_MESSAGE.get()
                        .replace("<size_2d>", area == null ? "0" : String.valueOf(area.getSize2D()))
                        .replace("<size_3d>", area == null ? "0" : String.valueOf(area.getSize3D()))
                        .replace("<x1>", firstPos == null ? "" : String.valueOf(firstPos.getBlockX()))
                        .replace("<y1>", firstPos == null ? "" : String.valueOf(firstPos.getBlockY()))
                        .replace("<z1>", firstPos == null ? "" : String.valueOf(firstPos.getBlockZ()))
                        .replace("<x2>", secondPos == null ? "" : String.valueOf(secondPos.getBlockX()))
                        .replace("<y2>", secondPos == null ? "" : String.valueOf(secondPos.getBlockY()))
                        .replace("<z2>", secondPos == null ? "" : String.valueOf(secondPos.getBlockZ())))
                        .split("\\|"));
                return true;
            }
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAD_COMMAND_USAGE.get()
                .replace("<command>", command.getName()))
                .split("\\|"));
        return true;
    }

    public void sendHelpMessage(CommandSender receiver) {
        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.AREA_INFO_MESSAGE.get())
                .split("\\|"));
    }
}
