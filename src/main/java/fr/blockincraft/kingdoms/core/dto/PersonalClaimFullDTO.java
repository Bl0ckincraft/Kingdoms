package fr.blockincraft.kingdoms.core.dto;

import fr.blockincraft.kingdoms.core.entity.PersonalClaim;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonalClaimFullDTO implements ClaimDTO {
    private long id;
    private AreaDTO area;
    private UUID owner;
    private List<UUID> trustedPlayers = new ArrayList<>();

    public PersonalClaimFullDTO() {

    }

    public PersonalClaimFullDTO(PersonalClaim model) {
        this.id = model.getId();
        this.area = new AreaDTO(model.getArea());
        this.owner = model.getOwner();
        this.trustedPlayers = model.getTrustedPlayers();
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public AreaDTO getArea() {
        return area;
    }

    public void setArea(AreaDTO area) {
        this.area = area;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public List<UUID> getTrustedPlayers() {
        return trustedPlayers;
    }

    public void setTrustedPlayers(List<UUID> trustedPlayers) {
        this.trustedPlayers = trustedPlayers;
    }

    public boolean isTrusted(UUID player) {
        return trustedPlayers.contains(player);
    }

    public String getTrustedAsString() {
        StringBuilder str = new StringBuilder();

        for (UUID trusted : getTrustedPlayers()) {
            Player player = Bukkit.getPlayer(trusted);
            if (player != null) {
                if (!str.toString().equalsIgnoreCase("")) {
                    str.append(", ");
                }
                str.append(player.getDisplayName());
            }
        }

        return str.toString();
    }
}
