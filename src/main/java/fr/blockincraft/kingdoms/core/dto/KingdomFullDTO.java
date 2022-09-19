    package fr.blockincraft.kingdoms.core.dto;

    import fr.blockincraft.kingdoms.core.entity.Area;
    import fr.blockincraft.kingdoms.core.entity.CurrentCommission;
    import fr.blockincraft.kingdoms.core.entity.Kingdom;
    import fr.blockincraft.kingdoms.core.enums.Collections;
    import fr.blockincraft.kingdoms.core.enums.Constructions;
    import fr.blockincraft.kingdoms.core.enums.KingdomLevels;
    import fr.blockincraft.kingdoms.core.enums.KingdomPermissionLevels;
    import org.bukkit.Bukkit;
    import org.bukkit.Location;
    import org.bukkit.Material;
    import org.bukkit.World;
    import org.bukkit.block.Block;
    import org.bukkit.block.BlockFace;
    import org.bukkit.entity.Player;

    import java.time.LocalDate;
    import java.util.*;

    public class KingdomFullDTO {
        private long id;
        private String name;
        private int penalty = 0;
        private String penaltyReason = "";
        private LocalDate lastPenalty = LocalDate.now();
        private KingdomClaimDTO claim;
        private Map<Collections, Integer> collections = new HashMap<>();
        private Map<UUID, KingdomPermissionLevels> members = new HashMap<>();
        private Map<AreaDTO, Constructions> constructions = new HashMap<>();
        private Map<UUID, Integer> completedCommissions = new HashMap<>();
        private Map<UUID, CurrentCommissionDTO> currentCommissions = new HashMap<>();
        private long bank = 0;
        private long home;

        public KingdomFullDTO() {

        }

        public KingdomFullDTO(Kingdom model) {
            this.id = model.getId();
            this.name = model.getName();
            this.penalty = model.getPenalty();
            this.penaltyReason = model.getPenaltyReason();
            this.lastPenalty = model.getLastPenalty();
            this.claim = new KingdomClaimDTO(model.getClaim());
            this.collections.putAll(model.getCollections());
            this.members.putAll(model.getMembers());
            this.collections.putAll(model.getCollections());
            for (Map.Entry<Area, Constructions> entry : model.getConstructions().entrySet()) {
                this.constructions.put(new AreaDTO(entry.getKey()), entry.getValue());
            }
            this.completedCommissions.putAll(model.getCompletedCommissions());
            for (Map.Entry<UUID, CurrentCommission> entry : model.getCurrentCommissions().entrySet()) {
                this.currentCommissions.put(entry.getKey(), new CurrentCommissionDTO(entry.getValue()));
            }
            this.bank = model.getBank();
            this.home = model.getHome();
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

        public KingdomClaimDTO getClaim() {
            return claim;
        }

        public void setClaim(KingdomClaimDTO claim) {
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

        public Map<AreaDTO, Constructions> getConstructions() {
            return constructions;
        }

        public void setConstructions(Map<AreaDTO, Constructions> constructions) {
            this.constructions = constructions;
        }

        public Map<UUID, Integer> getCompletedCommissions() {
            return completedCommissions;
        }

        public void setCompletedCommissions(Map<UUID, Integer> completedCommissions) {
            this.completedCommissions = completedCommissions;
        }

        public Map<UUID, CurrentCommissionDTO> getCurrentCommissions() {
            return currentCommissions;
        }

        public void setCurrentCommissions(Map<UUID, CurrentCommissionDTO> currentCommissions) {
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

        public int getHomeX() {
            return (int)(this.home >> 38);
        }

        public int getHomeY() {
            int y = (int)(this.home & 4095L);
            return y >= 4000 ? y - 4096 : y;
        }

        public int getHomeZ() {
            return (int)(this.home << 26 >> 38);
        }

        public Location getHomeAsBukkitLoc() {
            return new Location(Bukkit.getWorld(claim.getArea().getWorld()), getHomeX(), getHomeY(), getHomeZ());
        }

        public void setHome(Location loc) {
            setLocations(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        }

        public void setLocations(int x, int y, int z) {
            this.home = (long)(x & 67108863) << 38 | (long)(z & 67108863) << 12 | (long)(y & 4095);
        }

        public int getConstructionAmount(Constructions construction) {
            int amount = 0;

            for (Map.Entry<AreaDTO, Constructions> entry : constructions.entrySet()) {
                if (entry.getValue() == construction) amount ++;
            }

            return amount;
        }

        public int getKingdomLevelAsInt() {
            return getKingdomLevel().level;
        }

        public KingdomLevels getKingdomLevel() {
            return KingdomLevels.getKingdomLevel(getRankingPoints());
        }

        public int getRankingPoints() {
            for (int i = 1; true; i++) {
                int points = 0;

                for (Map.Entry<AreaDTO, Constructions> entry : constructions.entrySet()) {
                    if (entry.getValue() == Constructions.WAREHOUSE) {
                        int level = Constructions.getLevel(entry.getValue(), entry.getKey(), KingdomLevels.getByInt(i));

                        for (Map.Entry<Collections, Integer> entry2 : collections.entrySet()) {
                            if (entry2.getKey().level > level) continue;

                            for (Collections.Reward reward : entry2.getKey().rewards) {
                                if (entry2.getValue() >= reward.amount()) points += reward.points();
                            }
                        }
                    }
                    points += Constructions.getPoints(entry.getValue(), entry.getKey(), KingdomLevels.getByInt(i));
                }



                if (KingdomLevels.getKingdomLevel(points).level <= i) {
                    return points;
                }
            }
        }

        public int getWarehouseLevel() {
            for (Map.Entry<AreaDTO, Constructions> entry : constructions.entrySet()) {
                if (entry.getValue() == Constructions.WAREHOUSE) {
                    return Constructions.getLevel(Constructions.WAREHOUSE, entry.getKey(), getKingdomLevel());
                }
            }

            return 0;
        }

        public int getBankLevel() {
            for (Map.Entry<AreaDTO, Constructions> entry : constructions.entrySet()) {
                if (entry.getValue() == Constructions.BANK) {
                    return Constructions.getLevel(Constructions.BANK, entry.getKey(), getKingdomLevel());
                }
            }

            return 0;
        }

        public int getTownHallLevel() {
            for (Map.Entry<AreaDTO, Constructions> entry : constructions.entrySet()) {
                if (entry.getValue() == Constructions.TOWN_HALL) {
                    return Constructions.getLevel(Constructions.TOWN_HALL, entry.getKey(), getKingdomLevel());
                }
            }

            return 0;
        }

        public AreaDTO getTownHallArea() {
            for (Map.Entry<AreaDTO, Constructions> entry : constructions.entrySet()) {
                if (entry.getValue() == Constructions.TOWN_HALL) {
                    return entry.getKey();
                }
            }

            return null;
        }

        public Location warpLocation() {
            if (getTownHallLevel() == 0) return null;

            AreaDTO area = getTownHallArea();

            if (area == null) return null;

            World world = Bukkit.getWorld(getTownHallArea().getWorld());

            if (world == null) return null;

            for (int i = area.getSmallestX(); i <= area.getBiggestX(); i++) {
                for (int l = area.getSmallestZ(); l <= area.getBiggestZ(); l++) {
                    for (int j = area.getSmallestY(); j <= area.getBiggestY(); j++) {
                        Block block = world.getBlockAt(i, j, l);
                        Material material = block.getType();
                        if (material == Material.DIAMOND_BLOCK) {
                            if (airMaterials.contains(block.getRelative(BlockFace.UP).getType()) && airMaterials.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType())) {
                                return block.getLocation();
                            }
                        }
                    }
                }
            }

            return null;
        }

        public boolean canBuildMore(Constructions construction) {
            return getConstructionAmount(construction) < construction.maxAmount;
        }

        public boolean touchOtherConstructions(AreaDTO newConstruction) {
            return touchOtherConstructions(newConstruction, -1);
        }

        public boolean touchOtherConstructions(AreaDTO newConstruction, long exceptId) {
            for (Map.Entry<AreaDTO, Constructions> entry : constructions.entrySet()) {
                if (entry.getKey().mixed(newConstruction) && entry.getKey().getId() != exceptId) return true;
            }

            return false;
        }

        private static final List<Material> airMaterials = Arrays.asList(
                Material.AIR,
                Material.CAVE_AIR,
                Material.VOID_AIR);

        public boolean isOwner(UUID player) {
            return members.get(player) == KingdomPermissionLevels.OWNER;
        }

        public boolean isMember(UUID player) {
            return members.containsKey(player);
        }

        public boolean isInConstruction(Location location) {
            return getConstructionAt(location) != null;
        }

        public Constructions getConstructionAt(Location location) {
            if (!claim.getArea().isIn(location)) return null;

            for (Map.Entry<AreaDTO, Constructions> entry : constructions.entrySet()) {
                if (entry.getKey().isIn(location)) {
                    return entry.getValue();
                }
            }

            return null;
        }

        public AreaDTO getConstructionAreaAt(Location location) {
            for (Map.Entry<AreaDTO, Constructions> entry : constructions.entrySet()) {
                if (entry.getKey().isIn(location)) {
                    return entry.getKey();
                }
            }

            return null;
        }

        public UUID getOwner() {
            for (Map.Entry<UUID, KingdomPermissionLevels> entry : members.entrySet()) {
                if (entry.getValue() == KingdomPermissionLevels.OWNER) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public List<Player> getOnlineMembers() {
            List<Player> onlineMembers = new ArrayList<>();

            for (UUID uuid : members.keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    onlineMembers.add(player);
                }
            }

            return onlineMembers;
        }
    }
