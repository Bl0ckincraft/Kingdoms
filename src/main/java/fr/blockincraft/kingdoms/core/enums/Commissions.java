package fr.blockincraft.kingdoms.core.enums;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.security.SecureRandom;
import java.util.Arrays;

public enum Commissions {
    CHICKEN("chicken_killer", commissionsCompleted -> 10 + commissionsCompleted * 5, EntityType.CHICKEN),
    RABBIT("rabbit_killer", commissionsCompleted -> 20 + commissionsCompleted * 10, EntityType.RABBIT),
    SKELETON("skeleton_killer", commissionsCompleted -> 30 + commissionsCompleted * 15, EntityType.SKELETON),
    COW("cow_killer", commissionsCompleted -> 40 + commissionsCompleted * 20, EntityType.COW),
    BLAZE("blaze_killer", commissionsCompleted -> 50 + commissionsCompleted * 25, EntityType.BLAZE);

    private static final SecureRandom random = new SecureRandom();

    public final String langName;
    public final String langDesc;
    public final ObjectiveCalculation objective;
    public final CommissionTypes type;
    public final Material[] blocksToBreak;
    public final EntityType[] entitiesToKill;
    
    Commissions(String langName, ObjectiveCalculation objective, Material... blocksToBreak) {
        this.langName = "commissions." + langName + ".name";
        this.langDesc = "commissions." + langName + ".desc";
        this.type = CommissionTypes.MINE;
        this.objective = objective;
        this.blocksToBreak = blocksToBreak;
        this.entitiesToKill = null;
    }

    Commissions(String langName, ObjectiveCalculation objective, EntityType... entitiesToKill) {
        this.langName = "commissions." + langName + ".name";
        this.langDesc = "commissions." + langName + ".desc";
        this.type = CommissionTypes.KILL;
        this.objective = objective;
        this.blocksToBreak = null;
        this.entitiesToKill = entitiesToKill;
    }

    public boolean countForThis(Material block) {
        if (blocksToBreak == null) return false;
        return Arrays.asList(blocksToBreak).contains(block);
    }

    public boolean countForThis(EntityType entity) {
        if (entitiesToKill == null) return false;
        return Arrays.asList(entitiesToKill).contains(entity);
    }
    
    public static interface ObjectiveCalculation {
        int calculation(int commissionsCompleted);
    }

    public static Commissions getRandom() {
        int i = random.nextInt(Commissions.values().length);
        return Commissions.values()[i];
    }

    public enum CommissionTypes {
        MINE,
        KILL;
    }
}
