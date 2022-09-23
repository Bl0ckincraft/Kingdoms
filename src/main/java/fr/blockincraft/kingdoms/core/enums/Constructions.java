package fr.blockincraft.kingdoms.core.enums;

import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import fr.blockincraft.kingdoms.data.ConstructionsData;
import fr.blockincraft.kingdoms.util.Lang;
import org.apache.commons.compress.compressors.pack200.Pack200Utils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Door;

import java.util.*;

/**
 * For beds only foot part count and for doors only bottom half count <br/><br/>
 *
 * Don't use ice, snow, leaves, cobblestone, stone, obsidian, basalt, fire, lava, water, dragon_egg, end_portal,
 * nether_portal, chorus_flower, falling blocks as BlockRequirement or actualize the {@link ConstructionsData} working !
 */
public enum Constructions {
    TOWN_HALL(0x5a03fc, 1, "town_hall", new int[]{10, 25, 50},
            new Requirements(9, 10, 9, KingdomLevels.NEW,
                    new BlockRequirement(Material.DIAMOND_BLOCK, 1),
                    new BlockRequirement(Material.LECTERN, 1)
            ),
            new Requirements(16, 14, 16, KingdomLevels.LITTLE,
                    new BlockRequirement(Material.GOLD_BLOCK, 36),
                    new BlockRequirement(Material.BEACON, 4)
            ),
            new Requirements(32, 18, 32, KingdomLevels.BIG,
                    new BlockRequirement(Material.NETHERITE_BLOCK, 18),
                    new BlockRequirement(Material.DRAGON_HEAD, 4)
            )
    ),
    HOME(0x00ff26, 15, "home", new int[]{2},
            new Requirements(6, 5, 6, KingdomLevels.NEW,
                    new BlockRequirement(Material.WHITE_BED, 1),
                    new BlockRequirement(Material.CHEST, 1),
                    new BlockRequirement(Material.OAK_DOOR, 1)
            )
    ),
    WAREHOUSE(0xa36b39, 1, "warehouse", new int[]{6, 14, 18, 26, 30},
            new Requirements(12, 6, 12, KingdomLevels.LITTLE,
                    new BlockRequirement(Material.BARREL, 16),
                    new BlockRequirement(Material.LECTERN, 1),
                    new BlockRequirement(Material.IRON_DOOR, 2)
            ),
            new Requirements(18, 8, 18, KingdomLevels.BIG,
                    new BlockRequirement(Material.BARREL, 48),
                    new BlockRequirement(Material.LECTERN, 2),
                    new BlockRequirement(Material.DIAMOND_BLOCK, 10)
            ),
            new Requirements(18, 8, 18, KingdomLevels.BIG,
                    new BlockRequirement(Material.BARREL, 80),
                    new BlockRequirement(Material.LECTERN, 3),
                    new BlockRequirement(Material.DIAMOND_BLOCK, 20)
            ),
            new Requirements(28, 12, 28, KingdomLevels.RICH,
                    new BlockRequirement(Material.BARREL, 192),
                    new BlockRequirement(Material.LECTERN, 4),
                    new BlockRequirement(Material.NETHERITE_BLOCK, 2)
            ),
            new Requirements(28, 12, 28, KingdomLevels.EMPIRE,
                    new BlockRequirement(Material.BARREL, 224),
                    new BlockRequirement(Material.LECTERN, 5),
                    new BlockRequirement(Material.NETHERITE_BLOCK, 10)
            )),
    BANK(0xffd900, 1, "bank", new int[]{8, 16, 30},
            new Requirements(12, 8, 12, KingdomLevels.BIG,
                    new BlockRequirement(Material.GOLD_BLOCK, 16),
                    new BlockRequirement(Material.LECTERN, 1),
                    new BlockRequirement(Material.IRON_DOOR, 2)
            ),
            new Requirements(18, 12, 18, KingdomLevels.RICH,
                    new BlockRequirement(Material.GOLD_BLOCK, 128),
                    new BlockRequirement(Material.DIAMOND_BLOCK, 16)
            ),
            new Requirements(22, 16, 22, KingdomLevels.EMPIRE,
                    new BlockRequirement(Material.GOLD_BLOCK, 800),
                    new BlockRequirement(Material.DIAMOND_BLOCK, 100),
                    new BlockRequirement(Material.NETHERITE_BLOCK, 12)
            ));

    public final int color;
    public final int maxAmount;
    public final String langName;
    public final int[] points;
    public final Map<Integer, Requirements> requirements;

    Constructions(int color, int maxAmount, String langName, int[] points, Requirements requirement, Requirements... requirements) {
        this.color = color;
        this.maxAmount = maxAmount;
        this.langName = "construction.name." + langName;
        this.points = points;
        this.requirements = new HashMap<>();
        this.requirements.put(1, requirement);
        for (int i = 0; i < requirements.length; i++) {
            this.requirements.put(i + 2, requirements[i]);
        }
    }

    public static int getPoints(Constructions construction, int level) {
        return level > 0 ? construction.points[level - 1] : 0;
    }

    public static int getPoints(Constructions construction, AreaDTO area, KingdomLevels kingdomLevel) {
        return getPoints(construction, getLevel(construction, area, kingdomLevel));
    }

    public List<Material> getRequirementMaterials() {
        List<Material> materials = new ArrayList<>();

        requirements.forEach((level, requirement) -> {
            requirement.blocks.forEach((material, amount) -> {
                materials.add(material);
            });
        });

        return materials;
    }

    public static int getLevel(Constructions construction, AreaDTO area, KingdomLevels kingdomLevel) {
        World world = Bukkit.getWorld(area.getWorld());
        if (world == null) return 0;

        Map<Material, Long> blocks = ConstructionsData.constructionsMaterials.get(area.getId());
        if (blocks == null) {
            blocks = new HashMap<>();
            List<Material> materials = construction.getRequirementMaterials();

            for (int i = area.getSmallestX(); i <= area.getBiggestX(); i++) {
                for (int l = area.getSmallestZ(); l <= area.getBiggestZ(); l++) {
                    for (int j = area.getSmallestY(); j <= area.getBiggestY(); j++) {
                        BlockState blockState = world.getBlockState(i, j, l);
                        Material material = world.getType(i, j, l);
                        if (materials.contains(material)) {
                            long value = blocks.containsKey(material) ? blocks.get(material) + 1 : 1;
                            if (blockState.getBlockData() instanceof Bed bed) {
                                if (bed.getPart() == Bed.Part.FOOT) {
                                    blocks.put(material, value);
                                }
                            } else if (blockState.getBlockData() instanceof Door door) {
                                if (door.getHalf() == Bisected.Half.BOTTOM) {
                                    blocks.put(material, value);
                                }
                            } else {
                                blocks.put(material, value);
                            }
                        }
                    }
                }
            }

            ConstructionsData.constructionsMaterials.put(area.getId(), blocks);
        }

        int level = 0;

        for (Map.Entry<Integer, Requirements> entry : construction.requirements.entrySet()) {
            Requirements requirement = entry.getValue();
            
            if (requirement.level != null && !KingdomLevels.hasLevel(requirement.level, kingdomLevel)) return entry.getKey() - 1;
            if (area.getBiggestX() - area.getSmallestX() + 1 < requirement.minSizeX) return entry.getKey() - 1;
            if (area.getBiggestY() - area.getSmallestY() + 1 < requirement.minSizeY) return entry.getKey() - 1;
            if (area.getBiggestZ() - area.getSmallestZ() + 1 < requirement.minSizeZ) return entry.getKey() - 1;

            for (Map.Entry<Material, Integer> entry2 : requirement.blocks.entrySet()) {
                if (woodenDoorMaterials.contains(entry2.getKey())) {
                    int amount = 0;

                    for (Material material : woodenDoorMaterials) {
                        if (blocks.containsKey(material)) amount += blocks.get(material);
                    }

                    if (amount < entry2.getValue()) return entry.getKey() - 1;
                } else if (bedMaterials.contains(entry2.getKey())) {
                    int amount = 0;

                    for (Material material : bedMaterials) {
                        if (blocks.containsKey(material)) amount += blocks.get(material);
                    }

                    if (amount < entry2.getValue()) return entry.getKey() - 1;
                } else {
                    if (!blocks.containsKey(entry2.getKey())) return entry.getKey() - 1;
                    if (blocks.get(entry2.getKey()) < entry2.getValue()) return entry.getKey() - 1;
                }
            }

            level = entry.getKey();
        }

        return level;
    }

    public static class Requirements {
        public final int minSizeX;
        public final int minSizeY;
        public final int minSizeZ;
        public final KingdomLevels level;
        public final Map<Material, Integer> blocks;

        public Requirements(int minSizeX, int minSizeY, int minSizeZ, KingdomLevels level, BlockRequirement... blocks) {
            this.minSizeX = minSizeX;
            this.minSizeY = minSizeY;
            this.minSizeZ = minSizeZ;
            this.level = level;
            this.blocks = new HashMap<>();
            for (BlockRequirement block : blocks) {
                this.blocks.put(block.material, block.amount);
            }
        }
    }

    public record BlockRequirement(Material material, int amount) {

    }

    public static final List<Material> woodenDoorMaterials = Arrays.asList(
            Material.OAK_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,
            Material.CRIMSON_DOOR,
            Material.WARPED_DOOR
    );

    public static final List<Material> bedMaterials = Arrays.asList(
            Material.WHITE_BED,
            Material.ORANGE_BED,
            Material.MAGENTA_BED,
            Material.LIGHT_BLUE_BED,
            Material.YELLOW_BED,
            Material.LIME_BED,
            Material.PINK_BED,
            Material.GRAY_BED,
            Material.LIGHT_GRAY_BED,
            Material.CYAN_BED,
            Material.PURPLE_BED,
            Material.BLUE_BED,
            Material.BROWN_BED,
            Material.GREEN_BED,
            Material.RED_BED,
            Material.BLACK_BED
    );
}
