package fr.blockincraft.kingdoms.core.enums;

import fr.blockincraft.kingdoms.util.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * To have a good display please make 7 rewards per collections, 5 collections per level, 3 levels
 */
public enum Collections {
    DRIED_KELP(1, Material.DRIED_KELP, "&2&lAlgues Séchées", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(100, 0),
            new Reward(200, 1),
            new Reward(400, 0),
            new Reward(600, 2),
            new Reward(1000, 0),
            new Reward(1600, 3),
            new Reward(2400, 4)
    }),
    COAL(1, Material.COAL, "&8&lCharbon", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(100, 0),
            new Reward(200, 1),
            new Reward(400, 0),
            new Reward(600, 2),
            new Reward(1000, 0),
            new Reward(1600, 3),
            new Reward(2400, 4)
    }),
    FEATHER(1, Material.FEATHER, "&f&lPlumes", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(100, 0),
            new Reward(200, 1),
            new Reward(400, 0),
            new Reward(600, 2),
            new Reward(1000, 0),
            new Reward(1600, 3),
            new Reward(2400, 4)
    }),
    SWEET_BERRIES(1, Material.SWEET_BERRIES, "&c&lBaies Sucrées", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(100, 0),
            new Reward(200, 1),
            new Reward(400, 0),
            new Reward(600, 2),
            new Reward(1000, 0),
            new Reward(1600, 3),
            new Reward(2400, 4)
    }),
    GOLD_INGOT(1, Material.GOLD_INGOT, "&6&lLingots d'Or", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(100, 0),
            new Reward(200, 1),
            new Reward(400, 0),
            new Reward(600, 2),
            new Reward(1000, 0),
            new Reward(1600, 3),
            new Reward(2400, 4)
    }),
    CARROT(2, Material.CARROT, "&6&lCarottes", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(200, 0),
            new Reward(400, 1),
            new Reward(600, 0),
            new Reward(1000, 2),
            new Reward(1600, 0),
            new Reward(2400, 3),
            new Reward(3000, 4)
    }),
    COPPER_INGOT(2, Material.COPPER_INGOT, "&6&lLingots de Cuivre", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(200, 0),
            new Reward(400, 1),
            new Reward(600, 0),
            new Reward(1000, 2),
            new Reward(1600, 0),
            new Reward(2400, 3),
            new Reward(3000, 4)
    }),
    RABBIT_HIDE(2, Material.RABBIT_HIDE, "&7&lPeau de Lapin", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(200, 0),
            new Reward(400, 1),
            new Reward(600, 0),
            new Reward(1000, 2),
            new Reward(1600, 0),
            new Reward(2400, 3),
            new Reward(3000, 4)
    }),
    POTATO(2, Material.POTATO, "&e&lPatates", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(200, 0),
            new Reward(400, 1),
            new Reward(600, 0),
            new Reward(1000, 2),
            new Reward(1600, 0),
            new Reward(2400, 3),
            new Reward(3000, 4)
    }),
    GOLD_INGOT2(2, Material.GOLD_INGOT, "&6&lLingot d'Or", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(200, 0),
            new Reward(400, 1),
            new Reward(600, 0),
            new Reward(1000, 2),
            new Reward(1600, 0),
            new Reward(2400, 3),
            new Reward(3000, 4)
    }),
    SUGAR_CANE(3, Material.SUGAR_CANE, "&a&lCanne à Sucre", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(400, 0),
            new Reward(600, 1),
            new Reward(1000, 0),
            new Reward(1600, 2),
            new Reward(2400, 0),
            new Reward(3000, 3),
            new Reward(3600, 4)
    }),
    BONE(3, Material.BONE, "&f&lOs", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(400, 0),
            new Reward(600, 1),
            new Reward(1000, 0),
            new Reward(1600, 2),
            new Reward(2400, 0),
            new Reward(3000, 3),
            new Reward(3600, 4)
    }),
    IRON_INGOT(3, Material.IRON_INGOT, "&f&lLingots de Fer", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(400, 0),
            new Reward(600, 1),
            new Reward(1000, 0),
            new Reward(1600, 2),
            new Reward(2400, 0),
            new Reward(3000, 3),
            new Reward(3600, 4)
    }),
    WHEAT(3, Material.WHEAT, "&e&lBlé", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(400, 0),
            new Reward(600, 1),
            new Reward(1000, 0),
            new Reward(1600, 2),
            new Reward(2400, 0),
            new Reward(3000, 3),
            new Reward(3600, 4)
    }),
    GOLD_INGOT3(3, Material.GOLD_INGOT, "&a&lLingots d'Or", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(400, 0),
            new Reward(600, 1),
            new Reward(1000, 0),
            new Reward(1600, 2),
            new Reward(2400, 0),
            new Reward(3000, 3),
            new Reward(3600, 4)
    }),
    WHEAT2(4, Material.WHEAT, "&e&lBlé", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(600, 0),
            new Reward(1000, 1),
            new Reward(1600, 0),
            new Reward(2400, 2),
            new Reward(3000, 0),
            new Reward(3600, 3),
            new Reward(4200, 4)
    }),
    EMERALD(4, Material.EMERALD, "&a&lÉmeraudes", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(600, 0),
            new Reward(1000, 1),
            new Reward(1600, 0),
            new Reward(2400, 2),
            new Reward(3000, 0),
            new Reward(3600, 3),
            new Reward(4200, 4)
    }),
    LEATHER(4, Material.LEATHER, "&6&lCuir", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(600, 0),
            new Reward(1000, 1),
            new Reward(1600, 0),
            new Reward(2400, 2),
            new Reward(3000, 0),
            new Reward(3600, 3),
            new Reward(4200, 4)
    }),
    HONEYCOMB(4, Material.HONEYCOMB, "&6&lMiel", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(600, 0),
            new Reward(1000, 1),
            new Reward(1600, 0),
            new Reward(2400, 2),
            new Reward(3000, 0),
            new Reward(3600, 3),
            new Reward(4200, 4)
    }),
    GOLD_INGOT4(4, Material.GOLD_INGOT, "&e&lLingots d'Or", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(600, 0),
            new Reward(1000, 1),
            new Reward(1600, 0),
            new Reward(2400, 2),
            new Reward(3000, 0),
            new Reward(3600, 3),
            new Reward(4200, 4)
    }),
    BEETROOT(5, Material.BEETROOT, "&4&lBetteraves", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(1000, 0),
            new Reward(1600, 1),
            new Reward(2400, 0),
            new Reward(3000, 2),
            new Reward(3600, 0),
            new Reward(4200, 3),
            new Reward(4800, 4)
    }),
    DIAMOND(5, Material.DIAMOND, "&b&lDiamants", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(1000, 0),
            new Reward(1600, 1),
            new Reward(2400, 0),
            new Reward(3000, 2),
            new Reward(3600, 0),
            new Reward(4200, 3),
            new Reward(4800, 4)
    }),
    BLAZE_ROD(5, Material.BLAZE_ROD, "&e&lBatons de Blaze", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(1000, 0),
            new Reward(1600, 1),
            new Reward(2400, 0),
            new Reward(3000, 2),
            new Reward(3600, 0),
            new Reward(4200, 3),
            new Reward(4800, 4)
    }),
    NETHER_WART(5, Material.NETHER_WART, "&4&lVerrues du Nether", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(1000, 0),
            new Reward(1600, 1),
            new Reward(2400, 0),
            new Reward(3000, 2),
            new Reward(3600, 0),
            new Reward(4200, 3),
            new Reward(4800, 4)
    }),
    GOLD_INGOT5(5, Material.GOLD_INGOT, "&e&lLingots d'Or", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(1000, 0),
            new Reward(1600, 1),
            new Reward(2400, 0),
            new Reward(3000, 2),
            new Reward(3600, 0),
            new Reward(4200, 3),
            new Reward(4800, 4)
    });

    public final int level;
    public final Material material;
    public final String name;
    public final String[] lore;
    public final Reward[] rewards;

    Collections(int level, Material material, String name, String[] lore, Reward[] rewards) {
        this.level = level;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.rewards = rewards;
    }

    public int getMaxValue() {
        int max = 0;

        for (Reward reward : rewards) {
            if (max < reward.amount) max = reward.amount;
        }

        return max;
    }

    public int getNext(int collected) {
        int next = -1;

        for (Reward reward : rewards) {
            if (reward.amount > collected && (reward.amount < next || next == -1)) next = reward.amount;
        }

        return next;
    }

    public ItemStack getForDisplay(int collected) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(Arrays.stream(lore).map(text -> ChatColor.translateAlternateColorCodes('&', text
                        .replace("%collected%", TextUtils.valueWithCommas(collected))
                        .replace("%max%", TextUtils.valueWithCommas(getMaxValue()))
                        .replace("%next%", TextUtils.valueWithCommas(getNext(collected)))
                )
        ).toList());

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public record Reward(int amount, int points) {
    }
}
