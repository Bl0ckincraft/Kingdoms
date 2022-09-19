package fr.blockincraft.kingdoms.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum KingdomLevels {
    NEW("new_kingdom", 1, 0),
    LITTLE("little_kingdom", 2, 12),
    BIG("big_kingdom", 3, 30),
    RICH("rich_kingdom", 4, 55),
    EMPIRE("empire", 5, 100);

    private static Map<Integer, KingdomLevels> levelByInt = new HashMap<>();

    static {
        for (KingdomLevels value : KingdomLevels.values()) {
            levelByInt.put(value.level, value);
        }
    }

    public final String langName;
    public final int level;
    public final int rankingPoints;

    KingdomLevels(String langName, int level, int rankingPoints) {
        this.langName = "kingdom.level." + langName;
        this.level = level;
        this.rankingPoints = rankingPoints;
    }

    public static KingdomLevels getByInt(int level) {
        return levelByInt.get(level);
    }

    public static boolean hasLevel(KingdomLevels levelRequired, KingdomLevels actualLevel) {
        return levelRequired.level <= actualLevel.level;
    }

    public static boolean hasLevel(KingdomLevels levelRequired, int rankingPoints) {
        return hasLevel(levelRequired, getKingdomLevel(rankingPoints));
    }

    public static KingdomLevels getKingdomLevel(int rankingPoints) {
        KingdomLevels level = null;

        for (KingdomLevels value : KingdomLevels.values()) {
            if (value.rankingPoints <= rankingPoints && (level == null || level.level < value.level)) {
                level = value;
            }
        }

        return level;
    }
}
