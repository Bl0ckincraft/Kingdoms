package fr.blockincraft.kingdoms.api.objects;

import fr.blockincraft.kingdoms.core.dto.KingdomClaimDTO;

public class KingdomClaimAO {
    private final long id;
    private final AreaAO area;

    private KingdomClaimAO(long id, AreaAO area) {
        this.id = id;
        this.area = area;
    }

    public long getId() {
        return id;
    }

    public AreaAO getArea() {
        return area;
    }

    public static KingdomClaimAO fromDTO(KingdomClaimDTO dto) {
        return new KingdomClaimAO(dto.getId(), AreaAO.fromDTO(dto.getArea()));
    }
}
