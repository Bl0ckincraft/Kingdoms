package fr.blockincraft.kingdoms.task;

import fr.blockincraft.kingdoms.Kingdoms;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadDynmapTask extends BukkitRunnable {
    private static ReloadDynmapTask instance;

    public static ReloadDynmapTask startTask() {
        if (instance == null) {
            instance = new ReloadDynmapTask();
            instance.runTaskTimerAsynchronously(Kingdoms.getInstance(), 20, 200);
        }

        return instance;
    }

    @Override
    public void run() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Kingdoms.getInstance(), () -> Kingdoms.getInstance().getDynmapManager().refresh());
    }
}
