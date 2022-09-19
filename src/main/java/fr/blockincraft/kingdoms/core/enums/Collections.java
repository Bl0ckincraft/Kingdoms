package fr.blockincraft.kingdoms.core.enums;

import fr.blockincraft.kingdoms.util.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * To a good display please make 7 rewards and 5 collections per level with 3 levels
 */
public enum Collections {
    DIAMOND(1, Material.DIAMOND, "&9&lDiamant", new String[]{"", "&bTotal: %collected%/%max%", "&bProchain: %collected%/%next%"}, new Reward[]{
            new Reward(100, 2)
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
