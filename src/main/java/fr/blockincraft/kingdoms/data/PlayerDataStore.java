package fr.blockincraft.kingdoms.data;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerDataStore {
    private static final Map<UUID, PlayerData> playersData = new HashMap<>();

    public static Map<UUID, PlayerData> getPlayersData() {
        return playersData;
    }

    public static PlayerData getOrCreate(UUID player) {
        if (!playersData.containsKey(player) || playersData.get(player) == null) {
            playersData.put(player, new PlayerData());
        }

        return playersData.get(player);
    }

    public static void invite(Player player, Player inviter, KingdomFullDTO kingdom) {
        PlayerData data = getOrCreate(player.getUniqueId());
        data.getInvites().add(kingdom.getId());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_INVITE_YOU.get()
                        .replace("<inviter_name>", inviter.getDisplayName())
                        .replace("<kingdom_name>", kingdom.getName())
                        .replace("<kingdom_id>", String.valueOf(kingdom.getId())))
                .split("\\|"));

        String playerName = player.getDisplayName();
        String inviterName = inviter.getDisplayName();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Kingdoms.getInstance(), () -> {
            if (data.getInvites().contains(kingdom.getId())) {
                data.getInvites().remove(kingdom.getId());
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INVITE_EXPIRE_MESSAGE_FOR_INVITED.get()
                                    .replace("<inviter_name>", inviterName)
                                    .replace("<kingdom_name>", kingdom.getName()))
                            .split("\\|"));
                }
                for (Player member : kingdom.getOnlineMembers()) {
                    member.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INVITE_EXPIRE_MESSAGE_FOR_KINGDOM.get()
                                    .replace("<invited_name>", playerName)
                                    .replace("<kingdom_name>", kingdom.getName()))
                            .split("\\|"));
                }
            }
        }, 1200L);
    }
}
