package fr.blockincraft.kingdoms.core.entity;

import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import jakarta.persistence.*;

import java.util.UUID;

@NamedQuery(query = "SELECT a FROM Area a", name = "getAllAreas")
@NamedQuery(query = "SELECT a FROM Area a WHERE a.world LIKE :worldId", name = "getAllAreasInWorld")
@Entity
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "biggest_location", nullable = false)
    private long biggestLocation;
    @Column(name = "smallest_location", nullable = false)
    private long smallestLocation;
    @Column(nullable = false)
    private UUID world;

    public Area() {

    }

    public Area(AreaDTO dto) {
        this.id = dto.getId();
        this.biggestLocation = dto.getBiggestLocation();
        this.smallestLocation = dto.getSmallestLocation();
        this.world = dto.getWorld();
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

    public void setBiggestLocation(long biggestLocation) {
        this.biggestLocation = biggestLocation;
    }

    public long getSmallestLocation() {
        return smallestLocation;
    }

    public void setSmallestLocation(long smallestLocation) {
        this.smallestLocation = smallestLocation;
    }

    public UUID getWorld() {
        return world;
    }

    public void setWorld(UUID world) {
        this.world = world;
    }
}
