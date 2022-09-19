package fr.blockincraft.kingdoms.api.objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.api.serializer.AreaAOSerializer;
import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import fr.blockincraft.kingdoms.core.dto.CurrentCommissionDTO;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.enums.Collections;
import fr.blockincraft.kingdoms.core.enums.Constructions;
import fr.blockincraft.kingdoms.core.enums.KingdomLevels;
import fr.blockincraft.kingdoms.core.enums.KingdomPermissionLevels;
import org.bukkit.ChatColor;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KingdomAO {
    private static final Registry registry = Kingdoms.getInstance().getRegistry();

    private final long id;
    private final String name;
    private final int penalty;
    private final String penaltyReason;
    private final long lastPenalty;
    private final KingdomClaimAO claim;
    private final Map<Collections, Integer> collections;
    private final Map<UUID, KingdomPermissionLevels> members;
    @JsonSerialize(keyUsing = AreaAOSerializer.class)
    private final Map<AreaAO, Constructions> constructions;
    private final Map<UUID, Integer> completedCommissions;
    private final Map<UUID, CurrentCommissionAO> currentCommissions;
    private final long bank;
    private final int homeX;
    private final int homeY;
    private final int homeZ;
    private final KingdomLevels level;
    private final int points;
    private final Map<Long, Integer> constructionsPoints;
    private final Map<Long, Integer> constructionsLevels;
    private final long rank;

    private KingdomAO(long id, String name, int penalty, String penaltyReason, long lastPenalty, KingdomClaimAO claim, Map<Collections, Integer> collections, Map<UUID, KingdomPermissionLevels> members, Map<AreaAO, Constructions> constructions, Map<UUID, Integer> completedCommissions, Map<UUID, CurrentCommissionAO> currentCommissions, long bank, int homeX, int homeY, int homeZ, KingdomLevels level, int points, Map<Long, Integer> constructionsPoints, Map<Long, Integer> constructionsLevels, long rank) {
        this.id = id;
        this.name = name;
        this.penalty = penalty;
        this.penaltyReason = penaltyReason;
        this.lastPenalty = lastPenalty;
        this.claim = claim;
        this.collections = collections;
        this.members = members;
        this.constructions = constructions;
        this.completedCommissions = completedCommissions;
        this.currentCommissions = currentCommissions;
        this.bank = bank;
        this.homeX = homeX;
        this.homeY = homeY;
        this.homeZ = homeZ;
        this.level = level;
        this.points = points;
        this.constructionsPoints = constructionsPoints;
        this.constructionsLevels = constructionsLevels;
        this.rank = rank;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPenalty() {
        return penalty;
    }

    public String getPenaltyReason() {
        return penaltyReason;
    }

    public long getLastPenalty() {
        return lastPenalty;
    }

    public KingdomClaimAO getClaim() {
        return claim;
    }

    public Map<Collections, Integer> getCollections() {
        return collections;
    }

    public Map<UUID, KingdomPermissionLevels> getMembers() {
        return members;
    }

    public Map<AreaAO, Constructions> getConstructions() {
        return constructions;
    }

    public Map<UUID, Integer> getCompletedCommissions() {
        return completedCommissions;
    }

    public Map<UUID, CurrentCommissionAO> getCurrentCommissions() {
        return currentCommissions;
    }

    public long getBank() {
        return bank;
    }

    public int getHomeX() {
        return homeX;
    }

    public int getHomeY() {
        return homeY;
    }

    public int getHomeZ() {
        return homeZ;
    }

    public KingdomLevels getLevel() {
        return level;
    }

    public int getPoints() {
        return points;
    }

    public Map<Long, Integer> getConstructionsPoints() {
        return constructionsPoints;
    }

    public Map<Long, Integer> getConstructionsLevels() {
        return constructionsLevels;
    }

    public long getRank() {
        return rank;
    }

    public static KingdomAO fromDTO(KingdomFullDTO dto) {
        KingdomLevels level = dto.getKingdomLevel();

        Map<AreaAO, Constructions> constructions = new HashMap<>();

        Map<Long, Integer> constructionsPoints = new HashMap<>();
        Map<Long, Integer> constructionsLevels = new HashMap<>();

        for (Map.Entry<AreaDTO, Constructions> entry : dto.getConstructions().entrySet()) {
            constructions.put(AreaAO.fromDTO(entry.getKey()), entry.getValue());

            int cLevel = Constructions.getLevel(entry.getValue(), entry.getKey(), level);

            constructionsPoints.put(entry.getKey().getId(), Constructions.getPoints(entry.getValue(), cLevel));
            constructionsLevels.put(entry.getKey().getId(), cLevel);
        }

        Map<UUID, CurrentCommissionAO> currentCommissions = new HashMap<>();

        for (Map.Entry<UUID, CurrentCommissionDTO> entry : dto.getCurrentCommissions().entrySet()) {
            currentCommissions.put(entry.getKey(), CurrentCommissionAO.fromDTO(entry.getValue()));
        }

        return new KingdomAO(
                dto.getId(),
                ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', dto.getName())),
                dto.getPenalty(),
                dto.getPenaltyReason(),
                dto.getLastPenalty().toEpochSecond(LocalTime.MIN, ZoneOffset.UTC),
                KingdomClaimAO.fromDTO(dto.getClaim()),
                dto.getCollections(),
                dto.getMembers(),
                constructions,
                dto.getCompletedCommissions(),
                currentCommissions,
                dto.getBank(),
                dto.getHomeX(),
                dto.getHomeY(),
                dto.getHomeZ(),
                level,
                dto.getRankingPoints(),
                constructionsPoints,
                constructionsLevels,
                registry.getRank(dto)
        );
    }
}
