package fr.blockincraft.kingdoms.core.dto;

import fr.blockincraft.kingdoms.core.entity.PersonalClaim;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonalClaimLightDTO {
    private long id;
    private UUID owner;
    private List<UUID> trustedPlayers = new ArrayList<>();

    public PersonalClaimLightDTO() {

    }

    public PersonalClaimLightDTO(PersonalClaim model) {
        this.id = model.getId();
        this.owner = model.getOwner();
        this.trustedPlayers = model.getTrustedPlayers();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
