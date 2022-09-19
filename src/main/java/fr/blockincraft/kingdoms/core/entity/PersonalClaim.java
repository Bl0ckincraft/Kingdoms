package fr.blockincraft.kingdoms.core.entity;

import fr.blockincraft.kingdoms.core.dto.PersonalClaimFullDTO;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@NamedQuery(query = "SELECT p FROM PersonalClaim p", name = "getAllPersonalClaims")
@NamedQuery(query = "SELECT p FROM PersonalClaim p WHERE p.area.world LIKE :worldId", name = "getAllPersonalClaimsInWorld")
@Entity
public class PersonalClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Area area;
    @Column(nullable = false, unique = true)
    private UUID owner;
    @Column(name = "trusted_players", nullable = false)
    private List<UUID> trustedPlayers;

    public PersonalClaim() {

    }

    public PersonalClaim(PersonalClaimFullDTO dto) {
        this.id = dto.getId();
        this.area = new Area(dto.getArea());
        this.owner = dto.getOwner();
        this.trustedPlayers = dto.getTrustedPlayers();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
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
}
