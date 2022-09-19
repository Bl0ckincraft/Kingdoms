package fr.blockincraft.kingdoms.compat;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class WorldGuardWrapper {
    private final WorldGuardPlugin worldGuard;
    private static StateFlag claimFlag;

    public WorldGuardWrapper() throws IllegalArgumentException, IllegalStateException, ClassCastException {
        this.worldGuard = JavaPlugin.getPlugin(WorldGuardPlugin.class);
    }

    public void initWorldGuardFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        try {
            StateFlag flag = new StateFlag("kingdom-claim", true);
            registry.register(flag);
            claimFlag = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("kingdom-claim");
            if (existing instanceof StateFlag) {
                claimFlag = (StateFlag) existing;
            } else {
                Kingdoms.getInstance().getLogger().log(Level.SEVERE, "WARNING: VERIFY WORLDGUARD/WORLDEDIT");
                e.printStackTrace();
            }
        }
    }

    public boolean canClaim(AreaDTO area, Player creatingPlayer) {
        try {

            if (Bukkit.getWorld(area.getWorld()) == null) {
                return true;
            }

            LocalPlayer localPlayer = this.worldGuard.wrapPlayer(creatingPlayer);
            WorldGuardPlatform platform = WorldGuard.getInstance().getPlatform();
            World world = BukkitAdapter.adapt(Bukkit.getWorld(area.getWorld()));

            RegionManager manager = platform.getRegionContainer().get(world);
            if (manager == null) {
                return true;
            }

            ProtectedCuboidRegion tempRegion = new ProtectedCuboidRegion(
                    "KINGDOMS_TEMP",
                    BlockVector3.at(area.getBiggestX(), area.getBiggestY(), area.getBiggestZ()),
                    BlockVector3.at(area.getSmallestX(), area.getSmallestY(), area.getSmallestZ())
            );

            return manager.getApplicableRegions(tempRegion).queryState(null, claimFlag) == StateFlag.State.ALLOW;
        } catch (Throwable rock) {
            Kingdoms.getInstance().getLogger().log(Level.SEVERE, "ATTENTION: VERIFIER WORLDGUARD/WORLDEDIT");
            rock.printStackTrace();
        }

        return true;
    }
}
