package fr.blockincraft.kingdoms.core.entity;

import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import fr.blockincraft.kingdoms.core.dto.CurrentCommissionDTO;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.enums.Collections;
import fr.blockincraft.kingdoms.core.enums.Constructions;
import fr.blockincraft.kingdoms.core.enums.KingdomPermissionLevels;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NamedQuery(query = "SELECT k FROM Kingdom k", name = "getAllKingdoms")
@NamedQuery(query = "SELECT k FROM Kingdom k WHERE k.claim.area.world LIKE :worldId", name = "getAllKingdomsInWorld")
@Entity
public class Kingdom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private int penalty;
    @Column(name = "penalty_reason", nullable = false)
    private String penaltyReason;
    @Column(name = "last_penalty")
    private LocalDate lastPenalty;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private KingdomClaim claim;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "kingdom_collections_mappings",
            joinColumns = {@JoinColumn(name = "kingdom_id", nullable = false)}
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "collection_name")
    @Column(name = "amount", nullable = false)
    private Map<Collections, Integer> collections;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "kingdom_members_mappings",
            joinColumns = {@JoinColumn(name = "kingdom_id", nullable = false)}
    )
    @MapKeyColumn(name = "player_id")
    @Column(name = "permission_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private Map<UUID, KingdomPermissionLevels> members;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "kingdom_constructions_mappings",
            joinColumns = {@JoinColumn(name = "kingdom_id", nullable = false)}
    )
    @MapKeyJoinColumn(name = "area_id")
    @Column(name = "construction_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private Map<Area, Constructions> constructions;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "kingdom_completed_commissions_mappings",
            joinColumns = {@JoinColumn(name = "kingdom_id", nullable = false)}
    )
    @MapKeyColumn(name = "player")
    @Column(name = "amount", nullable = false)
    private Map<UUID, Integer> completedCommissions;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "kingdom_current_commissions_mappings",
            joinColumns = {@JoinColumn(name = "kingdom_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "current_commission_id", nullable = false)}
    )
    @MapKeyColumn(name = "player")
    private Map<UUID, CurrentCommission> currentCommissions;
    @Column(nullable = false)
    private long bank;
    @Column(nullable = false)
    private long home;

    public Kingdom() {

    }

    public Kingdom(KingdomFullDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.penalty = dto.getPenalty();
        this.penaltyReason = dto.getPenaltyReason();
        this.lastPenalty = dto.getLastPenalty();
        this.claim = new KingdomClaim(dto.getClaim());
        this.collections = dto.getCollections();
        this.members = dto.getMembers();
        this.constructions = new HashMap<>();
        for (Map.Entry<AreaDTO, Constructions> entry : dto.getConstructions().entrySet()) {
            this.constructions.put(new Area(entry.getKey()), entry.getValue());
        }
        this.completedCommissions = dto.getCompletedCommissions();
        this.currentCommissions = new HashMap<>();
        for (Map.Entry<UUID, CurrentCommissionDTO> entry : dto.getCurrentCommissions().entrySet()) {
            this.currentCommissions.put(entry.getKey(), new CurrentCommission(entry.getValue()));
        }
        this.bank = dto.getBank();
        this.home = dto.getHome();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public String getPenaltyReason() {
        return penaltyReason;
    }

    public void setPenaltyReason(String penaltyReason) {
        this.penaltyReason = penaltyReason;
    }

    public LocalDate getLastPenalty() {
        return lastPenalty;
    }

    public void setLastPenalty(LocalDate lastPenalty) {
        this.lastPenalty = lastPenalty;
    }

    public KingdomClaim getClaim() {
        return claim;
    }

    public void setClaim(KingdomClaim claim) {
        this.claim = claim;
    }

    public Map<Collections, Integer> getCollections() {
        return collections;
    }

    public void setCollections(Map<Collections, Integer> collections) {
        this.collections = collections;
    }

    public Map<UUID, KingdomPermissionLevels> getMembers() {
        return members;
    }

    public void setMembers(Map<UUID, KingdomPermissionLevels> members) {
        this.members = members;
    }

    public Map<Area, Constructions> getConstructions() {
        return constructions;
    }

    public void setConstructions(Map<Area, Constructions> constructions) {
        this.constructions = constructions;
    }

    public Map<UUID, Integer> getCompletedCommissions() {
        return completedCommissions;
    }

    public void setCompletedCommissions(Map<UUID, Integer> completedCommissions) {
        this.completedCommissions = completedCommissions;
    }

    public Map<UUID, CurrentCommission> getCurrentCommissions() {
        return currentCommissions;
    }

    public void setCurrentCommissions(Map<UUID, CurrentCommission> currentCommissions) {
        this.currentCommissions = currentCommissions;
    }

    public long getBank() {
        return bank;
    }

    public void setBank(long bank) {
        this.bank = bank;
    }

    public long getHome() {
        return home;
    }

    public void setHome(long home) {
        this.home = home;
    }
}