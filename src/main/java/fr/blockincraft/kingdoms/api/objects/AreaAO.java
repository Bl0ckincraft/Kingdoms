package fr.blockincraft.kingdoms.api.objects;

import com.fasterxml.jackson.core.SerializableString;
import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import org.bukkit.Bukkit;

public class AreaAO {
    private final long id;
    private final int smallestX;
    private final int smallestY;
    private final int smallestZ;
    private final int biggestX;
    private final int biggestY;
    private final int biggestZ;
    private final String world;

    private AreaAO(long id, int smallestX, int smallestY, int smallestZ, int biggestX, int biggestY, int biggestZ, String world) {
        this.id = id;
        this.smallestX = smallestX;
        this.smallestY = smallestY;
        this.smallestZ = smallestZ;
        this.biggestX = biggestX;
        this.biggestY = biggestY;
        this.biggestZ = biggestZ;
        this.world = world;
    }

    public long getId() {
        return id;
    }

    public int getSmallestX() {
        return smallestX;
    }

    public int getSmallestY() {
        return smallestY;
    }

    public int getSmallestZ() {
        return smallestZ;
    }

    public int getBiggestX() {
        return biggestX;
    }

    public int getBiggestY() {
        return biggestY;
    }

    public int getBiggestZ() {
        return biggestZ;
    }

    public String getWorld() {
        return world;
    }

    public static AreaAO fromDTO(AreaDTO dto) {
        String worldName = Bukkit.getWorld(dto.getWorld()) == null ? "" : Bukkit.getWorld(dto.getWorld()).getName();
        return new AreaAO(dto.getId(), dto.getSmallestX(), dto.getSmallestY(), dto.getSmallestZ(), dto.getBiggestX(), dto.getBiggestY(), dto.getBiggestZ(), worldName);
    }
}