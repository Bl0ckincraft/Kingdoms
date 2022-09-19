package fr.blockincraft.kingdoms.core.entity;

import fr.blockincraft.kingdoms.core.dto.KingdomClaimDTO;
import jakarta.persistence.*;

@NamedQuery(query = "SELECT k FROM KingdomClaim k", name = "getAllKingdomClaims")
@NamedQuery(query = "SELECT k FROM KingdomClaim k WHERE k.area.world LIKE :worldId", name = "getAllKingdomClaimsInWorld")
@Entity
public class KingdomClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Area area;

    public KingdomClaim() {

    }

    public KingdomClaim(KingdomClaimDTO dto) {
        this.id = dto.getId();
        this.area = new Area(dto.getArea());
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
}
