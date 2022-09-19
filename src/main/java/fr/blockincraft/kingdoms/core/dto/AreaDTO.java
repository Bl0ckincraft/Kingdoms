package fr.blockincraft.kingdoms.core.dto;

import fr.blockincraft.kingdoms.core.entity.Area;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AreaDTO {
    private long id;
    private long biggestLocation;
    private long smallestLocation;
    private UUID world;

    public AreaDTO() {

    }

    public AreaDTO(Area model) {
        this.id = model.getId();
        this.biggestLocation = model.getBiggestLocation();
        this.smallestLocation = model.getSmallestLocation();
        this.world = model.getWorld();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBiggestLocation() {
        return biggestLocation;
    }

    public long getSmallestLocation() {
        return smallestLocation;
    }

    public Location getBiggestBukkitLoc() {
        return new Location(Bukkit.getWorld(world), getSmallestX(), getSmallestY(), getSmallestZ());
    }

    public Location getSmallestBukkitLoc() {
        return new Location(Bukkit.getWorld(world), getSmallestX(), getSmallestY(), getSmallestZ());
    }

    public int getBiggestX() {
        return (int)(this.biggestLocation >> 38);
    }

    public int getBiggestY() {
        int y = (int)(this.biggestLocation & 4095L);
        return y >= 4000 ? y - 4096 : y;
    }

    public int getBiggestZ() {
        return (int)(this.biggestLocation << 26 >> 38);
    }

    public int getSmallestX() {
        return (int)(this.smallestLocation >> 38);
    }

    public int getSmallestY() {
        int y = (int)(this.smallestLocation & 4095L);
        return y >= 4000 ? y - 4096 : y;
    }

    public int getSmallestZ() {
        return (int)(this.smallestLocation << 26 >> 38);
    }

    public void setLocations(Location loc1, Location loc2) {
        setLocations(loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
    }

    public void setLocations(int x1, int y1, int z1, int x2, int y2, int z2) {
        int biggestX = Math.max(x1, x2);
        int biggestY = Math.max(y1, y2);
        int biggestZ = Math.max(z1, z2);
        int smallestX = Math.min(x1, x2);
        int smallestY = Math.min(y1, y2);
        int smallestZ = Math.min(z1, z2);

        this.biggestLocation = (long)(biggestX & 67108863) << 38 | (long)(biggestZ & 67108863) << 12 | (long)(biggestY & 4095);
        this.smallestLocation = (long)(smallestX & 67108863) << 38 | (long)(smallestZ & 67108863) << 12 | (long)(smallestY & 4095);
    }

    public UUID getWorld() {
        return world;
    }

    public void setWorld(UUID world) {
        this.world = world;
    }

    public long getSize2D() {
        return (long) (getBiggestX() - getSmallestX() + 1) * (getBiggestZ() - getSmallestZ() + 1);
    }

    public long getSize3D() {
        return getSize2D() * (getBiggestY() - getSmallestY() + 1);
    }

    public boolean isIn(int x, int y, int z) {
        return x >= getSmallestX() && x <= getBiggestX() && y >= getSmallestY() && y <= getBiggestY() && z >= getSmallestZ() && z <= getBiggestZ();
    }
    public boolean isIn(Location location) {
        return isIn(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }


    public boolean mixed(AreaDTO areaDTO) {
        if (areaDTO.world != world) return false;

        return areaDTO.isIn(getBiggestX(), getBiggestY(), getBiggestZ()) ||
        areaDTO.isIn(getSmallestX(), getBiggestY(), getSmallestZ()) ||
        areaDTO.isIn(getBiggestX(), getBiggestY(), getSmallestZ()) ||
        areaDTO.isIn(getSmallestX(), getBiggestY(), getBiggestZ()) ||

        areaDTO.isIn(getBiggestX(), getSmallestY(), getBiggestZ()) ||
        areaDTO.isIn(getSmallestX(), getSmallestY(), getSmallestZ()) ||
        areaDTO.isIn(getBiggestX(), getSmallestY(), getSmallestZ()) ||
        areaDTO.isIn(getSmallestX(), getSmallestY(), getBiggestZ()) ||

        this.isIn(areaDTO.getBiggestX(), areaDTO.getBiggestY(), areaDTO.getBiggestZ()) ||
        this.isIn(areaDTO.getSmallestX(), areaDTO.getBiggestY(), areaDTO.getSmallestZ()) ||
        this.isIn(areaDTO.getBiggestX(), areaDTO.getBiggestY(), areaDTO.getSmallestZ()) ||
        this.isIn(areaDTO.getSmallestX(), areaDTO.getBiggestY(), areaDTO.getBiggestZ()) ||

        this.isIn(areaDTO.getBiggestX(), areaDTO.getSmallestY(), areaDTO.getBiggestZ()) ||
        this.isIn(areaDTO.getSmallestX(), areaDTO.getSmallestY(), areaDTO.getSmallestZ()) ||
        this.isIn(areaDTO.getBiggestX(), areaDTO.getSmallestY(), areaDTO.getSmallestZ()) ||
        this.isIn(areaDTO.getSmallestX(), areaDTO.getSmallestY(), areaDTO.getBiggestZ());
    }

    public Set<Location> getBorderBlocks3D() {
        Set<Location> borderBlocks = new HashSet<>();

        for (int i = getSmallestX(); i <= getBiggestX(); i++) {
            borderBlocks.add(new Location(Bukkit.getWorld(world), i, getSmallestY(), getSmallestZ()));
            borderBlocks.add(new Location(Bukkit.getWorld(world), i, getSmallestY(), getBiggestZ()));
            borderBlocks.add(new Location(Bukkit.getWorld(world), i, getBiggestY(), getSmallestZ()));
            borderBlocks.add(new Location(Bukkit.getWorld(world), i, getBiggestY(), getBiggestZ()));
        }
        for (int i = getSmallestZ(); i <= getBiggestZ(); i++) {
            borderBlocks.add(new Location(Bukkit.getWorld(world), getSmallestX(), getSmallestY(), i));
            borderBlocks.add(new Location(Bukkit.getWorld(world), getBiggestX(), getSmallestY(), i));
            borderBlocks.add(new Location(Bukkit.getWorld(world), getSmallestX(), getBiggestY(), i));
            borderBlocks.add(new Location(Bukkit.getWorld(world), getBiggestX(), getBiggestY(), i));
        }
        for (int i = getSmallestY(); i <= getBiggestY(); i++) {
            borderBlocks.add(new Location(Bukkit.getWorld(world), getSmallestX(), i, getSmallestZ()));
            borderBlocks.add(new Location(Bukkit.getWorld(world), getBiggestX(), i, getSmallestZ()));
            borderBlocks.add(new Location(Bukkit.getWorld(world), getSmallestX(), i, getBiggestZ()));
            borderBlocks.add(new Location(Bukkit.getWorld(world), getBiggestX(), i, getBiggestZ()));
        }

        return borderBlocks;
    }

    public Set<Location> getBorderBlocks2D() {
        Set<Location> borderBlocks = new HashSet<>();

        for (int i = getSmallestX(); i <= getBiggestX(); i++) {
            borderBlocks.add(new Location(Bukkit.getWorld(world), i, 100, getSmallestZ()));
            borderBlocks.add(new Location(Bukkit.getWorld(world), i, 100, getBiggestZ()));
        }
        for (int i = getSmallestZ(); i <= getBiggestZ(); i++) {
            borderBlocks.add(new Location(Bukkit.getWorld(world), getSmallestX(), 100, i));
            borderBlocks.add(new Location(Bukkit.getWorld(world), getBiggestX(), 100, i));
        }

        return borderBlocks;
    }

    public boolean isTotallyIn(AreaDTO area) {
        return isIn(area.getBiggestBukkitLoc()) && isIn(area.getSmallestBukkitLoc());
    }

    public boolean extend(BlockFace direction, int length) {
        World world = Bukkit.getWorld(this.world);
        if (world == null) return false;
        if (direction == BlockFace.UP && (getBiggestY() + length > world.getMaxHeight() || getSmallestY() + length < world.getMinHeight() - 1)) return false;
        if (direction == BlockFace.DOWN && (getSmallestY() - length < world.getMinHeight() - 1 || getSmallestY() - length > world.getMaxHeight())) return false;

        switch (direction) {
            case UP:
                setLocations(getSmallestX(), getSmallestY(), getSmallestZ(), getBiggestX(), getBiggestY() + length, getBiggestZ());
                break;
            case DOWN:
                setLocations(getSmallestX(), getSmallestY() - length, getSmallestZ(), getBiggestX(), getBiggestY(), getBiggestZ());
                break;
            case NORTH:
                setLocations(getSmallestX(), getSmallestY(), getSmallestZ() - length, getBiggestX(), getBiggestY(), getBiggestZ());
                break;
            case EAST:
                setLocations(getSmallestX(), getSmallestY(), getSmallestZ(), getBiggestX() + length, getBiggestY(), getBiggestZ());
                break;
            case WEST:
                setLocations(getSmallestX() - length, getSmallestY(), getSmallestZ(), getBiggestX(), getBiggestY(), getBiggestZ());
                break;
            case SOUTH:
                setLocations(getSmallestX(), getSmallestY(), getSmallestZ(), getBiggestX(), getBiggestY(), getBiggestZ() + length);
                break;
        }

        return true;
    }
}
