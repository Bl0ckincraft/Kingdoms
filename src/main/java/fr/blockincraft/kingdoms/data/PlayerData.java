package fr.blockincraft.kingdoms.data;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerData {
    private Location firstLocation = null;
    private Location secondLocation = null;
    private List<Long> invites = new ArrayList<>();

    public Location getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(Location firstLocation) {
        this.firstLocation = firstLocation;
    }

    public Location getSecondLocation() {
        return secondLocation;
    }

    public void setSecondLocation(Location secondLocation) {
        this.secondLocation = secondLocation;
    }

    public List<Long> getInvites() {
        return invites;
    }

    public void setInvites(List<Long> invites) {
        this.invites = invites;
    }
}
