package fr.blockincraft.kingdoms.task;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.core.dto.ClaimDTO;
import fr.blockincraft.kingdoms.core.dto.KingdomClaimDTO;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.dto.PersonalClaimFullDTO;
import fr.blockincraft.kingdoms.core.enums.KingdomPermissionLevels;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class VisualizeTask extends BukkitRunnable {
    private static VisualizeTask instance;
    private static final Registry registry = Kingdoms.getInstance().getRegistry();
    private static final Map<OfflinePlayer, Boolean> active = new ConcurrentHashMap();
    private static final Random random = new Random();
    int radius;

    public VisualizeTask() {
        radius = Bukkit.getServer().getViewDistance();
    }

    public static VisualizeTask startTask() {
        if (instance == null) {
            instance = new VisualizeTask();
            instance.runTaskTimerAsynchronously(Kingdoms.getInstance(), 60, 10);
        }

        return instance;
    }

    public static boolean togglePlayer(Player p) {
        Boolean isActive = active.get(p);
        active.put(p, isActive = (isActive == null || !isActive));
        return isActive;
    }

    public static void removePlayer(Player p) {
        active.remove(p);
    }

    @Override
    public void run() {
        active.entrySet().stream()
                .filter(e -> e.getValue() && e.getKey().isOnline())
                .forEach(e -> particleTick((Player) e.getKey()));
    }

    private void particleTick(Player player) {
        final Location playerLocation = player.getLocation();
        final World world = playerLocation.getWorld();

        int startY = playerLocation.getBlockY() + 1;
        int cxi = (playerLocation.getBlockX() >> 4) - radius;
        int cxn = cxi + radius * 2;
        int czi = (playerLocation.getBlockZ() >> 4) - radius;
        int czn = czi + radius * 2;

        List<Chunk> chunks = new ArrayList<>();

        for (int cx = cxi; cx < cxn; cx++) {
            for (int cz = czi; cz < czn; cz++) {
                if (world.isChunkLoaded(cx, cz)) {
                    chunks.add(world.getChunkAt(cx, cz));
                }
            }
        }

        chunks.remove(null);
        for (ClaimDTO claim : registry.getClaimsInChunks(chunks.toArray(new Chunk[0]))) {
            showClaimParticles(player, claim, startY);
        }
    }

    private void showClaimParticles(Player player, ClaimDTO claim, int startY) {
        World world = Bukkit.getWorld(claim.getArea().getWorld());

        Color color = Color.RED;
        boolean canBuild = registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), claim.getArea().getBiggestBukkitLoc());
        if (claim instanceof KingdomClaimDTO) {
            color = canBuild ? ParticlesColor.KINGDOM_CLAIM_WITH_ACCESS.color : ParticlesColor.KINGDOM_CLAIM_WITHOUT_ACCESS.color;

            KingdomFullDTO kingdom = registry.getKingdomAt(claim.getArea().getBiggestBukkitLoc());

            if (kingdom != null) {
                kingdom.getConstructions().forEach((area, construction) -> {
                    Set<Location> constructionBorder = area.getBorderBlocks3D();
                    constructionBorder.removeIf(location -> !location.getBlock().getType().isTransparent() && !glassBlocks.contains(location.getBlock().getType()));

                    constructionBorder.forEach(location -> {
                        long t = location.getBlockX() + location.getBlockY() + location.getBlockZ();
                        Location particleLocation = location.add(.5, .5, .5);
                        player.spawnParticle(Particle.REDSTONE, particleLocation, 0, 0, 0, 0, 1, new Particle.DustOptions(Color.fromRGB(construction.color), 2F));
                    });
                });
            }
        } else if (claim instanceof PersonalClaimFullDTO) {
            color = canBuild ? ParticlesColor.PERSONAL_CLAIM_WITH_ACCESS.color : ParticlesColor.PERSONAL_CLAIM_WITHOUT_ACCESS.color;
        }

        Set<Location> borderLocations = claim.getArea().getBorderBlocks2D();

        for (Location location : borderLocations) {
            if (startY >= world.getMaxHeight()) continue;
            Block block = world.getBlockAt(location.getBlockX(), startY, location.getBlockZ());

            boolean show = false;

            int maxUp = 5;
            int maxDownFirst = 7;
            int maxDownSecond = 3;

            Block blockForUp = block.getRelative(BlockFace.UP);
            Block blockForDown = block;

            while (--maxDownFirst >= 0 && !show) {
                show = blockForDown.getType().isTransparent() && !(blockForDown = blockForDown.getRelative(BlockFace.DOWN)).getType().isTransparent();
                if (show) block = blockForDown;
            }
            while (--maxUp >= 0 && !show) {
                show = blockForUp.getType().isTransparent() && !(blockForUp.getRelative(BlockFace.DOWN)).getType().isTransparent();
                blockForUp = blockForUp.getRelative(BlockFace.UP);

                if (show) {
                    block = blockForUp.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
                }
            }
            blockForDown = blockForDown.getRelative(BlockFace.DOWN);
            while (--maxDownSecond >= 0 && !show) {
                show = blockForDown.getType().isTransparent() && !(blockForDown = blockForDown.getRelative(BlockFace.DOWN)).getType().isTransparent();
                if (show) block = blockForDown;
            }


            if (show) {
                Location particleLocation = block.getLocation().add(.5, 1.5, .5);
                player.spawnParticle(Particle.REDSTONE, particleLocation, 0, 0, 0, 0, 1, new Particle.DustOptions(color, 2F));
            }
        }
    }

    private final List<Material> glassBlocks = Arrays.asList(
            Material.TINTED_GLASS,
            Material.GLASS,
            Material.WHITE_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS,
            Material.CYAN_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.BROWN_STAINED_GLASS,
            Material.GREEN_STAINED_GLASS,
            Material.RED_STAINED_GLASS,
            Material.BLACK_STAINED_GLASS,
            Material.GLASS_PANE,
            Material.WHITE_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE,
            Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.BLUE_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE,
            Material.GREEN_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE
    );

    private enum ParticlesColor {
        KINGDOM_CLAIM_WITH_ACCESS(Color.LIME),
        KINGDOM_CLAIM_WITHOUT_ACCESS(Color.ORANGE),
        PERSONAL_CLAIM_WITH_ACCESS(Color.GREEN),
        PERSONAL_CLAIM_WITHOUT_ACCESS(Color.RED);

        public final Color color;

        ParticlesColor(Color color) {
            this.color = color;
        }
    }
}
