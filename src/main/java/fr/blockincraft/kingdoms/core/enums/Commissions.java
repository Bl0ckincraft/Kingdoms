package fr.blockincraft.kingdoms.core.enums;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.security.SecureRandom;
import java.util.Arrays;

public enum Commissions {
    DIAMOND("diamond_collector", commissionsCompleted -> {
        return 10 + commissionsCompleted * commissionsCompleted / 10;
    }, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE),
    GOLD("gold_collector", commissionsCompleted -> {
        return 20 + commissionsCompleted * commissionsCompleted / 6;
    }, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE),
    IRON("iron_miner", commissionsCompleted -> {
        return 30 + commissionsCompleted * commissionsCompleted / 4;
    }, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE),
    COAL("coal_miner", commissionsCompleted -> {
        return 40 + commissionsCompleted * commissionsCompleted / 2;
    }, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE),
    ZOMBIE("zombie_killer", commissionsCompleted -> {
        return 10 + commissionsCompleted * commissionsCompleted / 2;
    }, EntityType.ZOMBIE);

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
