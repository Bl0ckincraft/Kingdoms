package fr.blockincraft.kingdoms.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum KingdomPermissionLevels {
    OWNER(1, "owner", Permissions.ALL),
    CO_OWNER(2, "co-owner", Permissions.EDIT_CLAIMS, Permissions.CREATE_CONSTRUCTIONS, Permissions.DELETE_CONSTRUCTIONS, Permissions.EDIT_TOWN_HALL, Permissions.KICK_MEMBERS, Permissions.PROMOTE_MEMBERS, Permissions.DEMOTE_MEMBERS, Permissions.ACCESS_THE_BANK, Permissions.INVITE_PLAYERS, Permissions.COLLECT_COLLECTION_REWARDS, Permissions.BUILD, Permissions.OPEN_CONTAINERS, Permissions.USE_REDSTONE),
    MANAGER(3, "manager", Permissions.KICK_MEMBERS, Permissions.PROMOTE_MEMBERS, Permissions.DEMOTE_MEMBERS, Permissions.ACCESS_THE_BANK, Permissions.INVITE_PLAYERS, Permissions.COLLECT_COLLECTION_REWARDS, Permissions.BUILD, Permissions.OPEN_CONTAINERS, Permissions.USE_REDSTONE),
    TRUSTED_MEMBER(4, "trusted_member", Permissions.ACCESS_THE_BANK, Permissions.INVITE_PLAYERS, Permissions.COLLECT_COLLECTION_REWARDS, Permissions.BUILD, Permissions.OPEN_CONTAINERS, Permissions.USE_REDSTONE),
    ESTABLISHED_MEMBER(5, "etablished_member", Permissions.COLLECT_COLLECTION_REWARDS, Permissions.BUILD, Permissions.OPEN_CONTAINERS, Permissions.USE_REDSTONE),
    MEMBER(6, "member", Permissions.BUILD, Permissions.OPEN_CONTAINERS, Permissions.USE_REDSTONE);

    private static final Map<Integer, KingdomPermissionLevels> rankByLevel = new HashMap<>();

    static {
        for (KingdomPermissionLevels value : KingdomPermissionLevels.values()) {
            rankByLevel.put(value.rankLevel, value);
        }
    }

    public static KingdomPermissionLevels getLowestRank() {
        return MEMBER;
    }

    public static KingdomPermissionLevels getByLevel(int level) {
        return rankByLevel.get(level);
    }

    private final int rankLevel;
    private final String langName;
    private final Permissions[] permissions;

    KingdomPermissionLevels(int rankLevel, String langName, Permissions... permissions) {
        this.rankLevel = rankLevel;
        this.langName = "permission.level." + langName;
        this.permissions = permissions;
    }

    public int getRankLevel() {
        return rankLevel;
    }

    public String getLangName() {
        return langName;
    }

    public Permissions[] getPermissions() {
        return permissions;
    }

    public enum Permissions {
        //True when members of a claim
        BUILD,
        OPEN_CONTAINERS,
        USE_REDSTONE,

        COLLECT_COLLECTION_REWARDS,
        ACCESS_THE_BANK,
        INVITE_PLAYERS,
        KICK_MEMBERS,
        PROMOTE_MEMBERS,
        DEMOTE_MEMBERS,
        EDIT_CLAIMS,
        CREATE_CONSTRUCTIONS,
        EDIT_CONSTRUCTIONS,
        DELETE_CONSTRUCTIONS,
        EDIT_TOWN_HALL,
        ALL
    }
}
