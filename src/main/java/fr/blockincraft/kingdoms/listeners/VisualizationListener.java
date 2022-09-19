package fr.blockincraft.kingdoms.listeners;

import fr.blockincraft.kingdoms.task.VisualizeTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class VisualizationListener implements Listener {
    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        VisualizeTask.removePlayer(event.getPlayer());
    }
}
