package fr.blockincraft.kingdoms.core.dto;

import fr.blockincraft.kingdoms.core.entity.KingdomClaim;

public class KingdomClaimDTO implements ClaimDTO {
    private long id;
    private AreaDTO area;

    public KingdomClaimDTO() {

    }

    public KingdomClaimDTO(KingdomClaim model) {
        this.id = model.getId();
        this.area = new AreaDTO(model.getArea());
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
}
