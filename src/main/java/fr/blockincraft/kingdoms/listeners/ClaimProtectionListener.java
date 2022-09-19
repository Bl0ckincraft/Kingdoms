package fr.blockincraft.kingdoms.listeners;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.core.dto.*;
import fr.blockincraft.kingdoms.core.enums.Constructions;
import fr.blockincraft.kingdoms.core.enums.KingdomPermissionLevels;
import fr.blockincraft.kingdoms.data.ConstructionsData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Inspired from GriefPrevention Listeners : <a href="https://github.com/TechFortress/GriefPrevention">GriefPrevention Github</a>
 */

//Todo ? : prevent activation of dispenser/piston at border & hopper pass border
public class ClaimProtectionListener implements Listener {
    private final Registry registry = Kingdoms.getInstance().getRegistry();

    //Block handlers part

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), block.getLocation())) {
            event.setCancelled(true);
        }

        if (block.getBlockData() instanceof Bed bed && bed.getPart() == Bed.Part.HEAD) {
            block = block.getRelative(bed.getFacing().getOppositeFace());
        } else if (block.getBlockData() instanceof Door door && door.getHalf() == Bisected.Half.TOP) {
            block = block.getRelative(BlockFace.DOWN);
        }
        KingdomFullDTO kingdom = registry.getKingdomAt(block.getLocation());
        if (kingdom != null) {
            Constructions construction = kingdom.getConstructionAt(block.getLocation());
            Material type = block.getType();
            if (construction != null) {
                if (construction.getRequirementMaterials().contains(type)) {
                    AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                    Map<Material, Long> blocks = ConstructionsData.constructionsMaterials.get(area.getId());
                    if (blocks != null) {
                        blocks.put(type, blocks.containsKey(block.getType()) ? blocks.get(type) - 1 : 1);
                    }
                } else if (Constructions.bedMaterials.contains(type)) {
                    for (Material bed : Constructions.bedMaterials) {
                        if (construction.getRequirementMaterials().contains(bed)) {
                            AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                            Map<Material, Long> blocks = ConstructionsData.constructionsMaterials.get(area.getId());
                            if (blocks != null) {
                                blocks.put(type, blocks.containsKey(block.getType()) ? blocks.get(type) - 1 : 1);
                            }
                            break;
                        }
                    }
                } else if (Constructions.woodenDoorMaterials.contains(type)) {
                    for (Material door : Constructions.woodenDoorMaterials) {
                        if (construction.getRequirementMaterials().contains(door)) {
                            AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                            Map<Material, Long> blocks = ConstructionsData.constructionsMaterials.get(area.getId());
                            if (blocks != null) {
                                blocks.put(type, blocks.containsKey(block.getType()) ? blocks.get(type) - 1 : 1);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignChanged(SignChangeEvent event) {
        Player player = event.getPlayer();
        Block sign = event.getBlock();

        if (player == null || sign == null) return;

        if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), sign.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlocksPlace(BlockMultiPlaceEvent event) {
        Player player = event.getPlayer();

        for (BlockState block : event.getReplacedBlockStates()) {
            if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), block.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @SuppressWarnings("null")
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), block.getLocation())) {
            if (block.getType() == Material.LECTERN && event.getBlockReplacedState().getType() == Material.LECTERN) {
                if (registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, player.getUniqueId(), block.getLocation())) {
                    return;
                } else {
                    event.setCancelled(true);
                    return;
                }
            }
            event.setCancelled(true);
            if (event.getItemInHand().getType() == Material.POWDER_SNOW_BUCKET) {
                Block snowyBlock = block.getRelative(BlockFace.DOWN);
                if (snowyBlock.getBlockData() instanceof Snowable snowable) {
                    snowable.setSnowy(false);
                    snowyBlock.setBlockData(snowable);
                }
            }
            return;
        }

        ClaimDTO claim = registry.getClaimAt(block.getLocation());

        denyConnectingDoubleChestsAcrossClaimBoundary(claim, block, player);

        KingdomFullDTO kingdom = registry.getKingdomAt(block.getLocation());
        if (kingdom != null) {
            Constructions construction = kingdom.getConstructionAt(block.getLocation());
            Material type = block.getType();
            if (construction != null) {
                if (construction.getRequirementMaterials().contains(type)) {
                    AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                    Map<Material, Long> blocks = ConstructionsData.constructionsMaterials.get(area.getId());
                    if (blocks != null) {
                        blocks.put(type, blocks.containsKey(block.getType()) ? blocks.get(type) + 1 : 1);
                    }
                } else if (Constructions.bedMaterials.contains(type)) {
                    for (Material bed : Constructions.bedMaterials) {
                        if (construction.getRequirementMaterials().contains(bed)) {
                            AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                            Map<Material, Long> blocks = ConstructionsData.constructionsMaterials.get(area.getId());
                            if (blocks != null) {
                                blocks.put(type, blocks.containsKey(block.getType()) ? blocks.get(type) + 1 : 1);
                            }
                            break;
                        }
                    }
                } else if (Constructions.woodenDoorMaterials.contains(type)) {
                    for (Material door : Constructions.woodenDoorMaterials) {
                        if (construction.getRequirementMaterials().contains(door)) {
                            AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                            Map<Material, Long> blocks = ConstructionsData.constructionsMaterials.get(area.getId());
                            if (blocks != null) {
                                blocks.put(type, blocks.containsKey(block.getType()) ? blocks.get(type) + 1 : 1);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private static final BlockFace[] HORIZONTAL_DIRECTIONS = new BlockFace[] {
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

    private void denyConnectingDoubleChestsAcrossClaimBoundary(ClaimDTO claim, Block block, Player player) {
        if (block.getBlockData() instanceof Chest) {
            for (BlockFace face : HORIZONTAL_DIRECTIONS) {
                Block relative = block.getRelative(face);
                if (!(relative.getBlockData() instanceof Chest)) continue;

                ClaimDTO relativeClaim = registry.getClaimAt(relative.getLocation());

                if (claim == null && relativeClaim == null) break;
                if (claim != null && relativeClaim != null && claim.getClass() == relativeClaim.getClass() && claim.getId() == relativeClaim.getId()) break;

                Chest chest = (Chest) block.getBlockData();
                chest.setType(Chest.Type.SINGLE);
                block.setBlockData(chest);

                Chest relativeChest = (Chest) relative.getBlockData();
                relativeChest.setType(Chest.Type.SINGLE);
                relative.setBlockData(relativeChest);

                player.sendBlockChange(relative.getLocation(), relativeChest);
                break;
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        onPistonEvent(event, event.getBlocks(), false);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        onPistonEvent(event, event.getBlocks(), true);
    }

    private void onPistonEvent(BlockPistonEvent event, List<Block> blocks, boolean isRetract) {
        BlockFace direction = event.getDirection();
        Block pistonBlock = event.getBlock();
        ClaimDTO pistonClaim = registry.getClaimAt(pistonBlock.getLocation());

        if (blocks.isEmpty()) {
            if (isRetract) return;

            Block invadedBlock = pistonBlock.getRelative(direction);
            ClaimDTO invadedClaim = registry.getClaimAt(invadedBlock.getLocation());

            if (invadedClaim != null && (pistonClaim == null || pistonClaim.getClass() != invadedClaim.getClass() || pistonClaim.getId() != invadedClaim.getId())) {
                event.setCancelled(true);
            }

            return;
        }

        Set<Block> blocksToCheck = new HashSet<>(blocks);
        blocksToCheck.add(blocks.get(blocks.size() - 1).getRelative(direction));

        for (Block block : blocksToCheck) {
            ClaimDTO claim = registry.getClaimAt(block.getLocation());
            if (claim != null && (pistonClaim == null || pistonClaim.getClass() != claim.getClass() || pistonClaim.getId() != claim.getId())) {
                event.setCancelled(true);
                pistonBlock.getWorld().createExplosion(pistonBlock.getLocation(), 0);
                pistonBlock.getWorld().dropItem(pistonBlock.getLocation(), new ItemStack(event.isSticky() ? Material.STICKY_PISTON : Material.PISTON));
                pistonBlock.setType(Material.AIR);
                return;
            }
        }

        KingdomFullDTO kingdom = registry.getKingdomAt(pistonBlock.getLocation());
        if (kingdom != null) {
            for (Block block : blocksToCheck) {
                if (block.getPistonMoveReaction() == PistonMoveReaction.BREAK) {
                    Constructions construction = kingdom.getConstructionAt(block.getLocation());
                    Material type = block.getType();
                    if (construction != null) {
                        if (construction.getRequirementMaterials().contains(type)) {
                            AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                            Map<Material, Long> cBlocks = ConstructionsData.constructionsMaterials.get(area.getId());
                            if (cBlocks != null) {
                                cBlocks.put(type, cBlocks.containsKey(block.getType()) ? cBlocks.get(type) - 1 : 1);
                            }
                        } else if (Constructions.bedMaterials.contains(type)) {
                            for (Material bed : Constructions.bedMaterials) {
                                if (construction.getRequirementMaterials().contains(bed)) {
                                    AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                                    Map<Material, Long> cBlocks = ConstructionsData.constructionsMaterials.get(area.getId());
                                    if (cBlocks != null) {
                                        cBlocks.put(type, cBlocks.containsKey(block.getType()) ? cBlocks.get(type) - 1 : 1);
                                    }
                                    break;
                                }
                            }
                        } else if (Constructions.woodenDoorMaterials.contains(type)) {
                            for (Material door : Constructions.woodenDoorMaterials) {
                                if (construction.getRequirementMaterials().contains(door)) {
                                    AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                                    Map<Material, Long> cBlocks = ConstructionsData.constructionsMaterials.get(area.getId());
                                    if (cBlocks != null) {
                                        cBlocks.put(type, cBlocks.containsKey(block.getType()) ? cBlocks.get(type) - 1 : 1);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    break;
                } else {
                    Block relativeBlock = block.getRelative(direction);
                    Constructions construction = kingdom.getConstructionAt(block.getLocation());
                    Constructions construction2 = kingdom.getConstructionAt(relativeBlock.getLocation());

                    Material type = block.getType();

                    if (construction == null && construction2 != null) {
                        if (construction2.getRequirementMaterials().contains(type)) {
                            AreaDTO area = kingdom.getConstructionAreaAt(relativeBlock.getLocation());
                            Map<Material, Long> cBlocks = ConstructionsData.constructionsMaterials.get(area.getId());
                            if (cBlocks != null) {
                                cBlocks.put(type, cBlocks.containsKey(type) ? cBlocks.get(type) + 1 : 1);
                            }
                        }
                    } else if (construction != null && construction2 == null) {
                        if (construction.getRequirementMaterials().contains(type)) {
                            AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                            Map<Material, Long> cBlocks = ConstructionsData.constructionsMaterials.get(area.getId());
                            if (cBlocks != null) {
                                cBlocks.put(type, cBlocks.containsKey(type) ? cBlocks.get(type) - 1 : 0);
                            }
                        }
                    } else if (construction != null && construction2 != null) {
                        if (construction.getRequirementMaterials().contains(type) || construction2.getRequirementMaterials().contains(type)) {
                            AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                            AreaDTO area2 = kingdom.getConstructionAreaAt(relativeBlock.getLocation());
                            if (area.getId() != area2.getId()) {
                                if (construction.getRequirementMaterials().contains(type)) {
                                    Map<Material, Long> cBlocks = ConstructionsData.constructionsMaterials.get(area.getId());
                                    if (cBlocks != null) {
                                        cBlocks.put(type, cBlocks.containsKey(type) ? cBlocks.get(type) - 1 : 0);
                                    }
                                }
                                if (construction2.getRequirementMaterials().contains(type)) {
                                    Map<Material, Long> cBlocks = ConstructionsData.constructionsMaterials.get(area2.getId());
                                    if (cBlocks != null) {
                                        cBlocks.put(type, cBlocks.containsKey(type) ? cBlocks.get(type) + 1 : 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockIgnite(BlockIgniteEvent igniteEvent) {
        if (igniteEvent.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING && registry.getClaimAt(igniteEvent.getIgnitingEntity().getLocation()) != null) {
            igniteEvent.setCancelled(true);
        }

        if (igniteEvent.getCause() == BlockIgniteEvent.IgniteCause.FIREBALL && igniteEvent.getIgnitingEntity() instanceof Fireball) {
            ProjectileSource shooter = ((Fireball) igniteEvent.getIgnitingEntity()).getShooter();
            if (shooter instanceof BlockProjectileSource) {
                ClaimDTO claim = registry.getClaimAt(igniteEvent.getIgnitingBlock().getLocation());
                ClaimDTO shooterClaim = registry.getClaimAt(((BlockProjectileSource) shooter).getBlock().getLocation());
                if (claim != null && shooterClaim != null && claim.getClass() == shooterClaim.getClass() && claim.getId() == shooterClaim.getId()) {
                    return;
                }
            }
        }

        if (igniteEvent.getCause() == BlockIgniteEvent.IgniteCause.ARROW && igniteEvent.getIgnitingEntity() != null) {
            BlockData blockData = igniteEvent.getBlock().getBlockData();
            if (blockData instanceof Lightable lightable) {
                lightable.setLit(true);

                EntityChangeBlockEvent changeBlockEvent = new EntityChangeBlockEvent(igniteEvent.getIgnitingEntity(), igniteEvent.getBlock(), blockData);
                onEntityChangeBLock(changeBlockEvent);

                if (changeBlockEvent.isCancelled()) {
                    igniteEvent.setCancelled(true);
                }
            }
            return;
        }

        if (igniteEvent.getCause() != BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL && igniteEvent.getCause() != BlockIgniteEvent.IgniteCause.LIGHTNING) {
            igniteEvent.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockSpread(BlockSpreadEvent spreadEvent) {
        if (spreadEvent.getSource().getType() == Material.BUDDING_AMETHYST) {
            ClaimDTO sourceClaim = registry.getClaimAt(spreadEvent.getSource().getLocation());
            ClaimDTO claim = registry.getClaimAt(spreadEvent.getBlock().getLocation());

            if (claim == null && sourceClaim == null) return;
            if (claim != null && sourceClaim != null && claim.getClass() == sourceClaim.getClass() && claim.getId() == sourceClaim.getId()) return;

            spreadEvent.setCancelled(true);
            return;
        }

        if (spreadEvent.getSource().getType() != Material.FIRE) return;

        if (registry.getClaimAt(spreadEvent.getBlock().getLocation()) != null) {
            spreadEvent.setCancelled(true);

            Block source = spreadEvent.getSource();
            if (source.getRelative(BlockFace.DOWN).getType() != Material.NETHERRACK) {
                source.setType(Material.AIR);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBurn(BlockBurnEvent burnEvent) {
        if (registry.getClaimAt(burnEvent.getBlock().getLocation()) != null) {
            burnEvent.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockFromTo(BlockFromToEvent spreadEvent) {
        if (spreadEvent.getFace() == BlockFace.DOWN) return;

        Block toBlock = spreadEvent.getToBlock();
        Location toLocation = toBlock.getLocation();
        ClaimDTO toClaim = registry.getClaimAt(toLocation);

        if (toClaim != null) {
            if (!toClaim.getArea().isIn(spreadEvent.getBlock().getLocation())) {
                spreadEvent.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void chorusFlower(ProjectileHitEvent event) {
        Block block = event.getHitBlock();

        if (block == null || block.getType() != Material.CHORUS_FLOWER) return;

        ClaimDTO claim = registry.getClaimAt(block.getLocation());
        if (claim == null) return;

        Player shooter = null;
        Projectile projectile = event.getEntity();

        if (projectile.getShooter() instanceof Player) shooter = (Player) projectile.getShooter();

        if (shooter == null) {
            event.setCancelled(true);
            return;
        }

        if (!registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, shooter.getUniqueId(), block.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onDispense(BlockDispenseEvent dispenseEvent) {
        Block fromBlock = dispenseEvent.getBlock();
        BlockData fromData = fromBlock.getBlockData();
        if (!(fromData instanceof Dispenser)) return;
        Dispenser dispenser = (Dispenser) fromData;

        Block toBlock = fromBlock.getRelative(dispenser.getFacing());
        ClaimDTO fromClaim = registry.getClaimAt(fromBlock.getLocation());
        ClaimDTO toClaim = registry.getClaimAt(toBlock.getLocation());

        Material materialDispensed = dispenseEvent.getItem().getType();

        if (fromClaim == null && toClaim == null) return;

        if (fromClaim != null && toClaim != null && fromClaim.getClass() == toClaim.getClass() && fromClaim.getId() == toClaim.getId()) return;

        dispenseEvent.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onTreeGrow(StructureGrowEvent event) {
        Location rootLocation = event.getLocation();
        World world = rootLocation.getWorld();

        List<PersonalClaimFullDTO> personalClaims = registry.getPersonalClaims();
        List<KingdomClaimDTO> kingdomClaims = registry.getKingdoms().stream().map(KingdomFullDTO::getClaim).collect(Collectors.toList());

        ClaimDTO rootClaim = registry.getClaimAt(rootLocation, kingdomClaims, personalClaims);

        for (int i = 0; i < event.getBlocks().size(); i++) {
            BlockState block = event.getBlocks().get(i);
            ClaimDTO blockClaim = registry.getClaimAt(block.getLocation(), kingdomClaims, personalClaims);

            if (blockClaim != null) {
                if (rootClaim == null || rootClaim.getClass() != blockClaim.getClass() || rootClaim.getId() != rootClaim.getId()) {
                    event.getBlocks().remove(i--);
                }
            }
        }

        if (rootClaim != null) {
            KingdomFullDTO kingdom = registry.getKingdomAt(rootLocation);
            if (kingdom != null) {
                for (BlockState block : event.getBlocks()) {
                    Constructions construction = kingdom.getConstructionAt(block.getLocation());
                    if (construction != null && construction.getRequirementMaterials().contains(block.getType())) {
                        AreaDTO area = kingdom.getConstructionAreaAt(block.getLocation());
                        Map<Material, Long> blocks = ConstructionsData.constructionsMaterials.get(area.getId());
                        if (blocks != null) {
                            Material type = block.getType();
                            blocks.put(type, blocks.containsKey(block.getType()) ? blocks.get(type) + 1 : 1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemFrameBrokenByBoat(HangingBreakEvent event)
    {
        if (event.getCause() != HangingBreakEvent.RemoveCause.PHYSICS) {
            return;
        }

        if (registry.getClaimAt(event.getEntity().getLocation()) != null) {
            event.setCancelled(true);
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onNetherPortalCreate(PortalCreateEvent event) {
        if (event.getReason() != PortalCreateEvent.CreateReason.NETHER_PAIR) {
            return;
        }

        for (BlockState blockState : event.getBlocks()) {
            ClaimDTO claim = registry.getClaimAt(blockState.getLocation());
            if (claim != null) {
                if (event.getEntity() instanceof Player player) {
                    if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, event.getEntity().getUniqueId(), blockState.getLocation())) {
                        event.setCancelled(true);
                        return;
                    }
                } else {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {
        if (event.getNewState().getType() == Material.SNOW || event.getNewState().getType() == Material.ICE) return;
        ClaimDTO claim = registry.getClaimAt(event.getBlock().getLocation());

        if (claim == null) return;

        Set<Block> blocksToCheck = new HashSet<>();
        Material newType = event.getNewState().getType();

        int generatorType = 0; //O = concrete, 1 = cobblestone generator or obsidian formation, 2 = basalt generator
        if (newType == Material.COBBLESTONE || newType == Material.OBSIDIAN) generatorType = 1;
        else if (newType == Material.BASALT) generatorType = 2;

        for (BlockFace direction : HORIZONTAL_DIRECTIONS) {
            Block b = event.getBlock().getRelative(direction);
            if (generatorType == 0 && b.getType() == Material.WATER) blocksToCheck.add(b);
            else if (generatorType == 1 && (b.getType() == Material.WATER || b.getType() == Material.LAVA)) blocksToCheck.add(b);
            else if (generatorType == 2 && b.getType() == Material.BLUE_ICE) blocksToCheck.add(b);
        }

        for (Block block : blocksToCheck) {
            ClaimDTO otherClaim = registry.getClaimAt(block.getLocation());

            if (otherClaim == null || claim.getClass() != otherClaim.getClass() || claim.getId() != claim.getId()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    //Entity handlers part

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityFormBlock(EntityBlockFormEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), event.getBlock().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityChangeBLock(EntityChangeBlockEvent event) {
        if (registry.isInClaim(event.getBlock().getLocation())) {
            if (event.getEntityType() == EntityType.ENDERMAN) {
                event.setCancelled(true);
            } else if (event.getEntityType() == EntityType.SILVERFISH) {
                event.setCancelled(true);
            } else if (event.getEntityType() == EntityType.RABBIT) {
                event.setCancelled(true);
            } else if (event.getEntityType() == EntityType.RAVAGER) {
                event.setCancelled(true);
            } else if (event.getEntityType() == EntityType.WITHER) {
                event.setCancelled(true);
            }
        }

        if (event.getEntity() instanceof Projectile) {
            handleProjectileChangeBlock(event, (Projectile) event.getEntity());
        } else if (event.getTo() == Material.DIRT && event.getBlock().getType() == Material.FARMLAND) {
            if (event.getEntityType() != EntityType.PLAYER) {
                event.setCancelled(true);
            } else {
                Player player = (Player) event.getEntity();
                Block block = event.getBlock();
                if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), block.getLocation())) {
                    event.setCancelled(true);
                }
            }
        } else if (event.getEntity() instanceof Vehicle && !event.getEntity().getPassengers().isEmpty()) {
            Entity driver = event.getEntity().getPassengers().get(0);
            if (driver instanceof Player) {
                Block block = event.getBlock();
                if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, driver.getUniqueId(), block.getLocation())) {
                    event.setCancelled(true);
                }
            }
        } else if (event.getEntityType() == EntityType.FALLING_BLOCK) {
            FallingBlock entity = (FallingBlock) event.getEntity();
            Block block = event.getBlock();

            if (event.getTo() == Material.AIR) {
                entity.setMetadata("KINGDOMS_FALLINGBLOCK", new FixedMetadataValue(Kingdoms.getInstance(), block.getLocation()));
            } else {
                List<MetadataValue> values = entity.getMetadata("KINGDOMS_FALLINGBLOCK");
                if (values.size() < 1) return;

                Location originalLocation = (Location) (values.get(0).value());
                Location newLocation = block.getLocation();

                if (originalLocation.getBlockX() != newLocation.getBlockX() || originalLocation.getBlockZ() != newLocation.getBlockZ()) {
                    ClaimDTO claim = registry.getClaimAt(newLocation);
                    if (claim != null && !claim.getArea().isIn(originalLocation)) {
                        event.setCancelled(true);

                        if (entity.isDead()) {
                            return;
                        }

                        entity.remove();

                        ItemStack itemStack = new ItemStack(entity.getBlockData().getMaterial(), 1);
                        block.getWorld().dropItemNaturally(entity.getLocation(), itemStack);
                    }
                }
            }
        }
    }

    private void handleProjectileChangeBlock(EntityChangeBlockEvent event, Projectile projectile) {
        Block block = event.getBlock();
        ClaimDTO claim = registry.getClaimAt(block.getLocation());

        if (claim == null) return;

        ProjectileSource shooter = projectile.getShooter();

        if (shooter instanceof Player) {
            if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, ((Player) shooter).getUniqueId(), block.getLocation())) {
                event.setCancelled(true);
            }

            return;
        }

        if (isBlockSourceInClaim(shooter, claim)) return;

        event.setCancelled(true);
    }

    private boolean isBlockSourceInClaim(ProjectileSource projectileSource, ClaimDTO claim) {
        if (!(projectileSource instanceof BlockProjectileSource)) return false;
        ClaimDTO sourceClaim = registry.getClaimAt(((BlockProjectileSource) projectileSource).getBlock().getLocation());
        return claim != null && sourceClaim != null && claim.getClass() == sourceClaim.getClass() && claim.getId() == sourceClaim.getId();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onFallingBlockEnterPortal(EntityPortalEnterEvent event) {
        if (event.getEntityType() != EntityType.FALLING_BLOCK) return;
        event.getEntity().removeMetadata("KINGDOMS_FALLINGBLOCK", Kingdoms.getInstance());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityInteract(EntityInteractEvent event) {
        Material material = event.getBlock().getType();
        if (material == Material.FARMLAND) {
            Entity rider = event.getEntity().getPassenger();
            if (rider != null && rider.getType() == EntityType.PLAYER) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent explodeEvent) {
        this.handleExplosion(explodeEvent.getLocation(), explodeEvent.getEntity(), explodeEvent.blockList());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockExplode(BlockExplodeEvent explodeEvent) {
        this.handleExplosion(explodeEvent.getBlock().getLocation(), null, explodeEvent.blockList());
    }


    void handleExplosion(Location location, Entity entity, List<Block> blocks) {
        World world = location.getWorld();

        List<PersonalClaimFullDTO> personalClaims = registry.getPersonalClaims();
        List<KingdomClaimDTO> kingdomClaims = registry.getKingdoms().stream().map(KingdomFullDTO::getClaim).collect(Collectors.toList());

        List<Block> explodedBlocks = new ArrayList<>();
        for (Block block : blocks) {
            if (block.getType() == Material.AIR) continue;

            ClaimDTO claim = registry.getClaimAt(block.getLocation(), kingdomClaims, personalClaims);

            if (claim == null) {
                explodedBlocks.add(block);
            }
        }

        blocks.clear();
        blocks.addAll(explodedBlocks);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityPickup(EntityChangeBlockEvent event) {
        if (event.getEntity().getType() == EntityType.ENDERMAN) {
            if (registry.getClaimAt(event.getBlock().getLocation()) != null) {
                event.setCancelled(true);
            }
        }
    }

    //when a painting is broken
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onHangingBreak(HangingBreakEvent event) {
        if (event.getCause() == HangingBreakEvent.RemoveCause.PHYSICS) return;

        if (event.getCause() == HangingBreakEvent.RemoveCause.EXPLOSION) {
            event.setCancelled(true);
            return;
        }

        if (!(event instanceof HangingBreakByEntityEvent)) {
            event.setCancelled(true);
            return;
        }

        HangingBreakByEntityEvent entityEvent = (HangingBreakByEntityEvent) event;

        Entity remover = entityEvent.getRemover();

        if (remover.getType() != EntityType.PLAYER) {
            event.setCancelled(true);
            return;
        }

        Player playerRemover = (Player) entityEvent.getRemover();
        if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, playerRemover.getUniqueId(), event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPaintingPlace(HangingPlaceEvent event) {
        if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, event.getPlayer().getUniqueId(), event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityTarget(EntityTargetEvent event) {
        EntityType entityType = event.getEntityType();
        if (entityType != EntityType.HOGLIN && entityType != EntityType.POLAR_BEAR) return;

        if (event.getReason() == EntityTargetEvent.TargetReason.TEMPT) {
            event.getEntity().getPersistentDataContainer().set(new NamespacedKey(Kingdoms.getInstance(), "lured_by_player"), PersistentDataType.BYTE, (byte) 1);
        } else {
            event.getEntity().getPersistentDataContainer().remove(new NamespacedKey(Kingdoms.getInstance(), "lured_by_player"));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        this.handleEntityDamageEvent(event, true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
        EntityDamageByEntityEvent eventWrapper = new EntityDamageByEntityEvent(event.getCombuster(), event.getEntity(), EntityDamageEvent.DamageCause.FIRE_TICK, event.getDuration());
        this.handleEntityDamageEvent(eventWrapper, false);
        event.setCancelled(eventWrapper.isCancelled());
    }

    //Todo : repair damage protections
    private void handleEntityDamageEvent(EntityDamageEvent event, boolean sendErrorMessagesToPlayers) {
        if (event.getEntity() instanceof Tameable) {
            Tameable tameable = (Tameable) event.getEntity();
            if (tameable.isTamed()) {
                EntityDamageEvent.DamageCause cause = event.getCause();
                if (cause != null && (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION ||
                        cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
                        cause == EntityDamageEvent.DamageCause.FALLING_BLOCK ||
                        cause == EntityDamageEvent.DamageCause.FIRE ||
                        cause == EntityDamageEvent.DamageCause.FIRE_TICK ||
                        cause == EntityDamageEvent.DamageCause.LAVA ||
                        cause == EntityDamageEvent.DamageCause.SUFFOCATION ||
                        cause == EntityDamageEvent.DamageCause.CONTACT ||
                        cause == EntityDamageEvent.DamageCause.DROWNING)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (event.getEntity() instanceof Player player) {
            if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), player.getLocation())) {
                event.setCancelled(true);
                return;
            }
        }

        if (handleExplosionDamage(event)) return;

        if (event instanceof EntityDamageByEntityEvent subEvent) {
            if (subEvent.getDamager() instanceof LightningStrike && subEvent.getDamager().hasMetadata("KINGDOMS_TRIDENT")) {
                event.setCancelled(true);
                return;
            }

            Player attacker = null;
            Projectile arrow = null;
            Firework firework = null;
            BlockProjectileSource projectileSource = null;
            Entity damageSource = subEvent.getDamager();

            if (damageSource != null) {
                if (damageSource.getType() == EntityType.WITHER || damageSource.getType() == EntityType.WITHER_SKULL) {
                    if (!(event.getEntity() instanceof Player)) {
                        ClaimDTO claim = registry.getClaimAt(event.getEntity().getLocation());
                        if (claim != null) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }

                if (damageSource.getType() == EntityType.PLAYER) {
                    attacker = (Player) damageSource;
                } else if (damageSource instanceof Projectile) {
                    arrow = (Projectile) damageSource;
                    if (arrow.getShooter() instanceof Player) {
                        attacker = (Player) arrow.getShooter();
                    } else if (arrow.getShooter() instanceof BlockProjectileSource) {
                        projectileSource = (BlockProjectileSource) arrow.getShooter();
                    }
                } else if (subEvent.getDamager() instanceof Firework fireworkIn) {
                    if (damageSource.hasMetadata("KINGDOMS_FIREWORK")) {
                        List<MetadataValue> data = damageSource.getMetadata("KINGDOMS_FIREWORK");
                        if (data != null && data.size() > 0) {
                            firework = fireworkIn;
                            attacker = (Player) data.get(0).value();
                        }
                    }
                }
            }

            ClaimDTO claim = registry.getClaimAt(subEvent.getEntity().getLocation());
            if (attacker != null) {
                ClaimDTO attackerClaim = registry.getClaimAt(attacker.getLocation());
                boolean attackerCanBuild = registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, attacker.getUniqueId(), attacker.getLocation());
                if (subEvent.getEntity() instanceof Player player) {
                    if (attackerClaim == null) return;
                    if (attackerCanBuild) return;

                    event.setCancelled(true);
                } else {
                    if (claim == null && attackerClaim == null) return;
                    if (claim != null && attackerClaim != null && claim.getClass() == attackerClaim.getClass() && claim.getId() == attackerClaim.getId() && attackerCanBuild) return;
                    if (claim != null && attackerClaim != null && attackerCanBuild && registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, attacker.getUniqueId(), subEvent.getEntity().getLocation())) return;
                    if (claim != null && attackerClaim == null && registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, attacker.getUniqueId(), subEvent.getEntity().getLocation())) return;
                    if (claim == null && attackerClaim != null && attackerCanBuild) return;

                    event.setCancelled(true);
                }
            } else if (projectileSource != null) {
                ClaimDTO sourceClaim = registry.getClaimAt(projectileSource.getBlock().getLocation());
                if (claim == null && sourceClaim == null) return;
                if (claim != null && sourceClaim != null && claim.getClass() == sourceClaim.getClass() && claim.getId() == sourceClaim.getId()) return;

                event.setCancelled(true);
            }
        }
    }

    private boolean handleExplosionDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return false;
        if (event.getEntity() instanceof Player) return false;

        Entity entity = event.getEntity();

        ClaimDTO claim = registry.getClaimAt(entity.getLocation());

        if (claim == null) return false;

        event.setCancelled(true);
        return true;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCrossbowFireWork(EntityShootBowEvent shootEvent) {
        if (shootEvent.getEntity() instanceof Player && shootEvent.getProjectile() instanceof Firework) {
            shootEvent.getProjectile().setMetadata("KINGDOMS_FIREWORK", new FixedMetadataValue(Kingdoms.getInstance(), shootEvent.getEntity()));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void entityPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() != EntityPotionEffectEvent.Cause.WITHER_ROSE) return;

        ClaimDTO claim = registry.getClaimAt(event.getEntity().getLocation());
        if (claim == null) return;

        if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, event.getEntity().getUniqueId(), event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();

        ProjectileSource projectileSource = potion.getShooter();
        if (projectileSource == null) return;

        Player thrower = null;
        if ((projectileSource instanceof Player)) thrower = (Player) projectileSource;

        Collection<PotionEffect> effects = potion.getEffects();
        for (PotionEffect effect : effects) {
            PotionEffectType effectType = effect.getType();

            if (PotionEffectType.HARM.equals(effectType)
                    || PotionEffectType.POISON.equals(effectType)
                    || PotionEffectType.JUMP.equals(effectType)
                    || PotionEffectType.WITHER.equals(effectType)) {
                for (LivingEntity affected : event.getAffectedEntities()) {
                    if (affected == thrower) continue;

                    ClaimDTO claim = registry.getClaimAt(affected.getLocation());
                    if (claim != null) {
                        if (thrower == null) {
                            if (!isBlockSourceInClaim(projectileSource, claim)) {
                                event.setIntensity(affected, 0);
                            }
                        } else {
                            if (!registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, thrower.getUniqueId(), affected.getLocation())) {
                                event.setIntensity(affected, 0);
                            }
                        }
                    }

                }
            }

            if (positiveEffects.contains(effectType)) continue;

            for (LivingEntity affected : event.getAffectedEntities()) {
                if (affected == thrower) {
                    continue;
                } else if (registry.isInClaim(affected.getLocation())) {
                    ClaimDTO claim = registry.getClaimAt(affected.getLocation());
                    if (registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, thrower.getUniqueId(), affected.getLocation())) {
                        if (affected instanceof Player) {
                            Player player = (Player) affected;

                            boolean isMember = false;

                            if (claim instanceof KingdomFullDTO) {
                                KingdomFullDTO kingdom = registry.getKingdomAt(player.getLocation());
                                isMember = kingdom.getMembers().containsKey(player.getUniqueId());
                            } else if (claim instanceof PersonalClaimFullDTO) {
                                PersonalClaimFullDTO personalClaim = (PersonalClaimFullDTO) claim;
                                isMember = personalClaim.getOwner() == player.getUniqueId() || personalClaim.getTrustedPlayers().contains(player.getUniqueId());
                            }

                            if (!isMember) {
                                event.setIntensity(affected, 0);
                            }
                        }
                    } else {
                        event.setIntensity(affected, 0);
                    }
                } else if (registry.isInClaim(thrower.getLocation())) {
                    ClaimDTO claim = registry.getClaimAt(thrower.getLocation());
                    if (registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, affected.getUniqueId(), thrower.getLocation())) {
                        if (affected instanceof Player) {
                            Player player = (Player) affected;

                            boolean isMember = false;

                            if (claim instanceof KingdomFullDTO) {
                                KingdomFullDTO kingdom = registry.getKingdomAt(thrower.getLocation());
                                isMember = kingdom.getMembers().containsKey(thrower.getUniqueId());
                            } else if (claim instanceof PersonalClaimFullDTO) {
                                PersonalClaimFullDTO personalClaim = (PersonalClaimFullDTO) claim;
                                isMember = personalClaim.getOwner() == thrower.getUniqueId() || personalClaim.getTrustedPlayers().contains(thrower.getUniqueId());
                            }

                            if (!isMember) {
                                event.setIntensity(affected, 0);
                            }
                        } else {
                            event.setIntensity(affected, 0);
                        }
                    }
                }
            }
        }
    }

    public static final HashSet<PotionEffectType> positiveEffects = new HashSet<>(Arrays.asList(
                    PotionEffectType.ABSORPTION,
                    PotionEffectType.DAMAGE_RESISTANCE,
                    PotionEffectType.FAST_DIGGING,
                    PotionEffectType.FIRE_RESISTANCE,
                    PotionEffectType.HEAL,
                    PotionEffectType.HEALTH_BOOST,
                    PotionEffectType.INCREASE_DAMAGE,
                    PotionEffectType.INVISIBILITY,
                    PotionEffectType.JUMP,
                    PotionEffectType.NIGHT_VISION,
                    PotionEffectType.REGENERATION,
                    PotionEffectType.SATURATION,
                    PotionEffectType.SPEED,
                    PotionEffectType.WATER_BREATHING
    ));

    //Player handlers part

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            this.onPlayerInteractEntity((PlayerInteractEntityEvent) event);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity.getType() == EntityType.ARMOR_STAND || entity instanceof Hanging) {
            if (!registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, player.getUniqueId(), entity.getLocation())) {
                event.setCancelled(true);
                return;
            }
        }

        if (entity instanceof Vehicle) {
            ClaimDTO claim = registry.getClaimAt(entity.getLocation());
            if (claim != null) {
                if (entity instanceof InventoryHolder) {
                    if (!registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, player.getUniqueId(), entity.getLocation())) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

        if (entity instanceof Animals || entity instanceof Fish || entity.getType() == EntityType.VILLAGER) {
            ClaimDTO claim = registry.getClaimAt(entity.getLocation());
            if (claim != null) {
                if (!registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, player.getUniqueId(), entity.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        ItemStack itemInHand = event.getHand() == EquipmentSlot.OFF_HAND ? player.getInventory().getItemInOffHand() : player.getInventory().getItemInMainHand();

        if (entity instanceof Creature && itemInHand.getType() == Material.LEAD) {
            ClaimDTO claim = registry.getClaimAt(entity.getLocation());
            if (claim != null) {
                if (!registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, player.getUniqueId(), entity.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (itemInHand.getType() == Material.NAME_TAG) {
            EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(player, entity, EntityDamageEvent.DamageCause.CUSTOM, 0);
            onEntityDamage(damageEvent);

            if (damageEvent.isCancelled()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerThrowEgg(PlayerEggThrowEvent event) {
        Player player = event.getPlayer();
        ClaimDTO claim = registry.getClaimAt(event.getEgg().getLocation());

        if (claim == null) return;

        if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), event.getEgg().getLocation())) {
            event.setHatching(false);

            if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                player.getInventory().addItem(event.getEgg().getItem());
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerFish(PlayerFishEvent event) {
        Entity entity = event.getCaught();
        if (entity == null) return;

        if (entity.getType() == EntityType.ARMOR_STAND || entity instanceof Animals) {
            Player player = event.getPlayer();
            ClaimDTO claim = registry.getClaimAt(entity.getLocation());
            if (claim != null) {
                if (!registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, player.getUniqueId(), entity.getLocation())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent bucketEvent) {
        Player player = bucketEvent.getPlayer();
        Block block = bucketEvent.getBlockClicked().getRelative(bucketEvent.getBlockFace());
        int minLavaDistance = 10;

        if (bucketEvent.getBucket() == Material.WATER_BUCKET && bucketEvent.getBlockClicked().getBlockData() instanceof Waterlogged) {
            block = bucketEvent.getBlockClicked();
        }

        if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), block.getLocation())) {
            bucketEvent.setCancelled(true);
            return;
        }

        ClaimDTO claim = registry.getClaimAt(block.getLocation());
        if (claim != null) {
            minLavaDistance = 3;
        }

        if (bucketEvent.getBucket() == Material.LAVA_BUCKET) {
            List<Player> players = block.getWorld().getPlayers();
            for (Player otherPlayer : players) {
                Location location = otherPlayer.getLocation();
                if (!otherPlayer.equals(player) && otherPlayer.getGameMode() == GameMode.SURVIVAL && player.canSee(otherPlayer) && block.getY() >= location.getBlockY() - 1 && location.distanceSquared(block.getLocation()) < minLavaDistance * minLavaDistance) {
                    bucketEvent.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerBucketFill(PlayerBucketFillEvent bucketEvent) {
        Player player = bucketEvent.getPlayer();
        Block block = bucketEvent.getBlockClicked();
        System.out.println(10);

        if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), block.getLocation())) {
            Material blockType = block.getType();
            if (blockType == Material.AIR)
                return;
            if (blockType.isSolid()) {
                BlockData blockData = block.getBlockData();
                if (!(blockData instanceof Waterlogged) || !((Waterlogged) blockData).isWaterlogged())
                    return;
            }

            bucketEvent.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_AIR) return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        Material clickedBlockType = null;
        if (clickedBlock != null) {
            clickedBlockType = clickedBlock.getType();
        } else {
            clickedBlockType = Material.AIR;
        }

        if (action == Action.PHYSICAL) {
            if (clickedBlockType != Material.TURTLE_EGG) return;
            ClaimDTO claim = registry.getClaimAt(clickedBlock.getLocation());
            if (claim != null) {
                if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), clickedBlock.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
            return;
        }

        if (action == Action.LEFT_CLICK_BLOCK && clickedBlock != null) {
            if (clickedBlock.getY() < clickedBlock.getWorld().getMaxHeight() - 1 || event.getBlockFace() != BlockFace.UP) {
                Block adjacentBlock = clickedBlock.getRelative(event.getBlockFace());
                byte lightLevel = adjacentBlock.getLightFromBlocks();
                if (lightLevel == 15 && adjacentBlock.getType() == Material.FIRE) {
                    ClaimDTO claim = registry.getClaimAt(clickedBlock.getLocation());
                    if (claim != null) {
                        if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), clickedBlock.getLocation())) {
                            event.setCancelled(true);
                            player.sendBlockChange(adjacentBlock.getLocation(), adjacentBlock.getType(), adjacentBlock.getData());
                            return;
                        }
                    }
                }
            }

            if (!this.onLeftClickWatchList(clickedBlockType)) {
                return;
            }
        }

        if (clickedBlock != null && (event.getAction() == Action.RIGHT_CLICK_BLOCK && ((this.isInventoryHolder(clickedBlock) &&
                                clickedBlock.getType() != Material.LECTERN) ||
                                clickedBlockType == Material.ANVIL ||
                                clickedBlockType == Material.BEACON ||
                                clickedBlockType == Material.BEE_NEST ||
                                clickedBlockType == Material.BEEHIVE ||
                                clickedBlockType == Material.BELL ||
                                clickedBlockType == Material.CAKE ||
                                clickedBlockType == Material.CARTOGRAPHY_TABLE ||
                                clickedBlockType == Material.CAULDRON ||
                                clickedBlockType == Material.WATER_CAULDRON ||
                                clickedBlockType == Material.LAVA_CAULDRON ||
                                clickedBlockType == Material.CAVE_VINES ||
                                clickedBlockType == Material.CAVE_VINES_PLANT ||
                                clickedBlockType == Material.CHIPPED_ANVIL ||
                                clickedBlockType == Material.DAMAGED_ANVIL ||
                                clickedBlockType == Material.GRINDSTONE ||
                                clickedBlockType == Material.JUKEBOX ||
                                clickedBlockType == Material.LOOM ||
                                clickedBlockType == Material.PUMPKIN ||
                                clickedBlockType == Material.RESPAWN_ANCHOR ||
                                clickedBlockType == Material.ROOTED_DIRT ||
                                clickedBlockType == Material.STONECUTTER ||
                                clickedBlockType == Material.SWEET_BERRY_BUSH))) {

            ClaimDTO claim = registry.getClaimAt(clickedBlock.getLocation());
            if (claim != null) {
                if (!registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, player.getUniqueId(), clickedBlock.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else if (clickedBlock != null && (Tag.BUTTONS.isTagged(clickedBlockType) || clickedBlockType == Material.LEVER)) {
            ClaimDTO claim = registry.getClaimAt(clickedBlock.getLocation());
            if (claim != null) {
                if (!registry.canDoAt(KingdomPermissionLevels.Permissions.USE_REDSTONE, player.getUniqueId(), clickedBlock.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else if (clickedBlock != null &&  (clickedBlockType == Material.CAKE || Tag.CANDLE_CAKES.isTagged(clickedBlockType))) {
            ClaimDTO claim = registry.getClaimAt(clickedBlock.getLocation());
            if (claim != null) {
                if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), clickedBlock.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else if (clickedBlock != null && (clickedBlockType == Material.NOTE_BLOCK ||
                                clickedBlockType == Material.REPEATER ||
                                clickedBlockType == Material.DRAGON_EGG ||
                                clickedBlockType == Material.DAYLIGHT_DETECTOR ||
                                clickedBlockType == Material.COMPARATOR ||
                                clickedBlockType == Material.REDSTONE_WIRE ||
                                Tag.FLOWER_POTS.isTagged(clickedBlockType) ||
                                Tag.CANDLES.isTagged(clickedBlockType))) {

            ClaimDTO claim = registry.getClaimAt(clickedBlock.getLocation());
            if (claim != null) {
                if (!registry.canDoAt(KingdomPermissionLevels.Permissions.USE_REDSTONE, player.getUniqueId(), clickedBlock.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else {
            if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return;

            EquipmentSlot hand = event.getHand();
            ItemStack itemInHand = hand == EquipmentSlot.OFF_HAND ? player.getInventory().getItemInOffHand() : player.getInventory().getItemInMainHand();
            Material materialInHand = itemInHand.getType();

            Set<Material> spawn_eggs = new HashSet<>();
            Set<Material> dyes = new HashSet<>();

            for (Material material : Material.values()) {
                if (material.isLegacy()) continue;
                if (material.name().endsWith("_SPAWN_EGG"))
                    spawn_eggs.add(material);
                else if (material.name().endsWith("_DYE"))
                    dyes.add(material);
            }

            if (clickedBlock != null && (materialInHand == Material.BONE_MEAL
                    || materialInHand == Material.ARMOR_STAND
                    || spawn_eggs.contains(materialInHand)
                    || materialInHand == Material.END_CRYSTAL
                    || materialInHand == Material.FLINT_AND_STEEL
                    || materialInHand == Material.INK_SAC
                    || materialInHand == Material.GLOW_INK_SAC
                    || dyes.contains(materialInHand))) {

                ClaimDTO claim = registry.getClaimAt(clickedBlock.getLocation());
                if (claim != null) {
                    if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), clickedBlock.getLocation())) {
                        event.setCancelled(true);
                    }
                }

                return;
            } else if (clickedBlock != null && Tag.ITEMS_BOATS.isTagged(materialInHand)) {
                ClaimDTO claim = registry.getClaimAt(clickedBlock.getLocation());
                if (claim != null) {
                    if (!registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, player.getUniqueId(), clickedBlock.getLocation())) {
                        event.setCancelled(true);
                    }
                }

                return;
            } else if (clickedBlock != null && (materialInHand == Material.MINECART ||
                            materialInHand == Material.FURNACE_MINECART ||
                            materialInHand == Material.CHEST_MINECART ||
                            materialInHand == Material.TNT_MINECART ||
                            materialInHand == Material.HOPPER_MINECART)) {

                ClaimDTO claim = registry.getClaimAt(clickedBlock.getLocation());
                if (claim != null) {
                    if (!registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, player.getUniqueId(), clickedBlock.getLocation())) {
                        event.setCancelled(true);
                    }
                }

                return;
            } else if (clickedBlock != null && (materialInHand == Material.MINECART ||
                    materialInHand == Material.FURNACE_MINECART ||
                    materialInHand == Material.CHEST_MINECART ||
                    materialInHand == Material.TNT_MINECART ||
                    materialInHand == Material.ARMOR_STAND ||
                    materialInHand == Material.ITEM_FRAME ||
                    materialInHand == Material.GLOW_ITEM_FRAME ||
                    spawn_eggs.contains(materialInHand) ||
                    materialInHand == Material.INFESTED_STONE ||
                    materialInHand == Material.INFESTED_COBBLESTONE ||
                    materialInHand == Material.INFESTED_STONE_BRICKS ||
                    materialInHand == Material.INFESTED_MOSSY_STONE_BRICKS ||
                    materialInHand == Material.INFESTED_CRACKED_STONE_BRICKS ||
                    materialInHand == Material.INFESTED_CHISELED_STONE_BRICKS ||
                    materialInHand == Material.HOPPER_MINECART)) {

                if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, player.getUniqueId(), clickedBlock.getLocation())) {
                    event.setCancelled(true);
                    return;
                }

                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    void onTakeBook(PlayerTakeLecternBookEvent event) {
        Player player = event.getPlayer();
        ClaimDTO claim = registry.getClaimAt(event.getLectern().getLocation());
        if (claim != null) {
            if (!registry.canDoAt(KingdomPermissionLevels.Permissions.OPEN_CONTAINERS, player.getUniqueId(), event.getLectern().getLocation())) {
                event.setCancelled(true);
                player.closeInventory();
            }
        }
    }

    private final ConcurrentHashMap<Material, Boolean> inventoryHolderCache = new ConcurrentHashMap<>();

    private boolean isInventoryHolder(Block clickedBlock) {

        Material cacheKey = clickedBlock.getType();
        Boolean cachedValue = this.inventoryHolderCache.get(cacheKey);
        if (cachedValue != null) {
            return cachedValue.booleanValue();
        } else {
            boolean isHolder = clickedBlock.getState() instanceof InventoryHolder;
            this.inventoryHolderCache.put(cacheKey, isHolder);
            return isHolder;
        }
    }

    private boolean onLeftClickWatchList(Material material) {
        switch (material) {
            case OAK_BUTTON:
            case SPRUCE_BUTTON:
            case BIRCH_BUTTON:
            case JUNGLE_BUTTON:
            case ACACIA_BUTTON:
            case DARK_OAK_BUTTON:
            case STONE_BUTTON:
            case LEVER:
            case REPEATER:
            case CAKE:
            case DRAGON_EGG:
                return true;
            default:
                return false;
        }
    }
}
