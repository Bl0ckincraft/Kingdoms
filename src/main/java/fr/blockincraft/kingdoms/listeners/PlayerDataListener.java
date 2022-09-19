package fr.blockincraft.kingdoms.listeners;

import fr.blockincraft.kingdoms.data.PlayerData;
import fr.blockincraft.kingdoms.data.PlayerDataStore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerDataListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerData data = PlayerDataStore.getOrCreate(event.getPlayer().getUniqueId());
        data.setFirstLocation(null);
        data.setSecondLocation(null);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        PlayerData data = PlayerDataStore.getOrCreate(event.getPlayer().getUniqueId());
        data.setFirstLocation(null);
        data.setSecondLocation(null);
    }
}
