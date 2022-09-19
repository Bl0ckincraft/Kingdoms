package fr.blockincraft.kingdoms;

import fr.blockincraft.kingdoms.core.dto.*;
import fr.blockincraft.kingdoms.core.enums.Constructions;
import fr.blockincraft.kingdoms.core.enums.KingdomPermissionLevels;
import fr.blockincraft.kingdoms.core.service.KingdomService;
import fr.blockincraft.kingdoms.core.service.PersonalClaimService;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;
import java.util.stream.Collectors;

public class Registry {
    private static final KingdomService kingdomService = new KingdomService(Kingdoms.getInstance().getSessionFactory());
    private static final PersonalClaimService personalClaimService = new PersonalClaimService(Kingdoms.getInstance().getSessionFactory());
    private static final List<KingdomPermissionLevels.Permissions> permissionsWithoutLocation;

    static  {
        permissionsWithoutLocation = new ArrayList<>();

        permissionsWithoutLocation.add(KingdomPermissionLevels.Permissions.ACCESS_THE_BANK);
        permissionsWithoutLocation.add(KingdomPermissionLevels.Permissions.COLLECT_COLLECTION_REWARDS);
        permissionsWithoutLocation.add(KingdomPermissionLevels.Permissions.EDIT_CLAIMS);
        permissionsWithoutLocation.add(KingdomPermissionLevels.Permissions.EDIT_TOWN_HALL);
        permissionsWithoutLocation.add(KingdomPermissionLevels.Permissions.CREATE_CONSTRUCTIONS);
        permissionsWithoutLocation.add(KingdomPermissionLevels.Permissions.EDIT_CONSTRUCTIONS);
        permissionsWithoutLocation.add(KingdomPermissionLevels.Permissions.DELETE_CONSTRUCTIONS);
        permissionsWithoutLocation.add(KingdomPermissionLevels.Permissions.INVITE_PLAYERS);
        permissionsWithoutLocation.add(KingdomPermissionLevels.Permissions.KICK_MEMBERS);
        permissionsWithoutLocation.add(KingdomPermissionLevels.Permissions.PROMOTE_MEMBERS);
        permissionsWithoutLocation.add(KingdomPermissionLevels.Permissions.DEMOTE_MEMBERS);
    }

    private final List<KingdomFullDTO> kingdoms = new ArrayList<>();
    private final List<PersonalClaimFullDTO> personalClaims = new ArrayList<>();

    public List<KingdomFullDTO> getKingdoms() {
        return kingdoms;
    }

    public List<PersonalClaimFullDTO> getPersonalClaims() {
        return personalClaims;
    }

    public boolean canDoAt(KingdomPermissionLevels.Permissions permission, UUID player, Location location, List<KingdomFullDTO> from, List<PersonalClaimFullDTO> from2) {
        if (!permissionsWithoutLocation.contains(permission)) {
            if (location == null || location.getWorld() == null) return false;

            for (PersonalClaimFullDTO claim : from2) {
                if (claim.getArea().isIn(location)) {
                    return claim.getOwner().equals(player) || claim.getTrustedPlayers().contains(player);
                }
            }

            for (KingdomFullDTO kingdom : from) {
                if (kingdom.getClaim().getArea().isIn(location)) {
                    if (!kingdom.getMembers().containsKey(player)) return false;

                    List<KingdomPermissionLevels.Permissions> permissions = Arrays.asList(kingdom.getMembers().get(player).getPermissions());

                    if (permission == KingdomPermissionLevels.Permissions.BUILD) {
                        for (Map.Entry<AreaDTO, Constructions> entry : kingdom.getConstructions().entrySet()) {
                            if (entry.getValue() == Constructions.TOWN_HALL && entry.getKey().isIn(location)) {
                                return permissions.contains(KingdomPermissionLevels.Permissions.EDIT_TOWN_HALL) || permissions.contains(KingdomPermissionLevels.Permissions.ALL);
                            }
                        }
                    }

                    return permissions.contains(permission) || permissions.contains(KingdomPermissionLevels.Permissions.ALL);
                }
            }

            return true;
        }

        return false;
    }

    public boolean canDoAt(KingdomPermissionLevels.Permissions permission, UUID player, Location location) {
        if (!permissionsWithoutLocation.contains(permission)) {
            if (location == null || location.getWorld() == null) return false;

            List<PersonalClaimFullDTO> personalClaims = new ArrayList<>();
            for (PersonalClaimFullDTO personalClaim : this.personalClaims) {
                if (personalClaim.getArea().getWorld() == location.getWorld().getUID()) {
                    personalClaims.add(personalClaim);
                }
            }

            List<KingdomClaimDTO> kingdomClaims = new ArrayList<>();
            for (KingdomFullDTO kingdom : this.kingdoms) {
                if (kingdom.getClaim().getArea().getWorld() == location.getWorld().getUID()) {
                    kingdomClaims.add(kingdom.getClaim());
                }
            }

            return canDoAt(permission, player, location, kingdoms, personalClaims);
        }

        return false;
    }

    public ClaimDTO getClaimAt(Location location, List<KingdomClaimDTO> from, List<PersonalClaimFullDTO> from2) {
        for (PersonalClaimFullDTO dto : from2) {
            if (dto.getArea().isIn(location)) {
                return dto;
            }
        }

        for (KingdomClaimDTO dto : from) {
            if (dto.getArea().isIn(location)) {
                return dto;
            }
        }

        return null;
    }

    public ClaimDTO getClaimAt(Location location) {
        if (location.getWorld() == null) return null;

        List<PersonalClaimFullDTO> personalClaims = new ArrayList<>();
        for (PersonalClaimFullDTO personalClaim : this.personalClaims) {
            if (personalClaim.getArea().getWorld() == location.getWorld().getUID()) {
                personalClaims.add(personalClaim);
            }
        }

        List<KingdomClaimDTO> kingdomClaims = new ArrayList<>();
        for (KingdomFullDTO kingdom : this.kingdoms) {
            if (kingdom.getClaim().getArea().getWorld() == location.getWorld().getUID()) {
                kingdomClaims.add(kingdom.getClaim());
            }
        }

        return getClaimAt(location, kingdomClaims, personalClaims);
    }

    public boolean isInClaim(Location location) {
        return getClaimAt(location) != null;
    }

    public List<ClaimDTO> getClaimsInWorld(World world) {
        List<ClaimDTO> claimsInWorld = new ArrayList<>();

        for (PersonalClaimFullDTO personalClaim : this.personalClaims) {
            if (personalClaim.getArea().getWorld().equals(world.getUID())) {
                claimsInWorld.add(personalClaim);
            }
        }

        for (KingdomFullDTO kingdom : this.kingdoms) {
            if (kingdom.getClaim().getArea().getWorld().equals(world.getUID())) {
                claimsInWorld.add(kingdom.getClaim());
            }
        }

        return claimsInWorld;
    }

    public List<ClaimDTO> getClaimsInChunk(Chunk chunk) {
        List<ClaimDTO> claimInWorld = getClaimsInWorld(chunk.getWorld());

        List<ClaimDTO> claims = new ArrayList<>();

        for (ClaimDTO claim : claimInWorld) {
            boolean claimFound = false;

            for (int i = 0; i < 16; i++) {
                if (claimFound) break;

                for (int l = 0; l < 16; l++) {
                    if (claimFound) break;

                    Location blockLocation = chunk.getBlock(i, 100, l).getLocation();
                    if (claim.getArea().isIn(blockLocation)) {
                        claims.add(claim);
                        claimFound = true;
                    }
                }
            }
        }

        return claims;
    }

    public List<ClaimDTO> getClaimsInChunks(Chunk... chunks) {
        if (chunks.length == 0) return new ArrayList<>();

        List<ClaimDTO> claimInWorld = getClaimsInWorld(chunks[0].getWorld());

        List<ClaimDTO> claims = new ArrayList<>();

        outer: for (ClaimDTO claim : claimInWorld) {
            for (Chunk chunk : chunks) {
                for (int i = 0; i < 16; i++) {
                    for (int l = 0; l < 16; l++) {
                        Location blockLocation = chunk.getBlock(i, 100, l).getLocation();
                        if (claim.getArea().isIn(blockLocation)) {
                            claims.add(claim);
                            continue outer;
                        }
                    }
                }
            }
        }

        return claims;
    }

    public KingdomFullDTO getPlayerKingdom(UUID player) {
        for (KingdomFullDTO kingdom : kingdoms) {
            if (kingdom.getMembers().containsKey(player)) {
                return kingdom;
            }
        }

        return null;
    }

    public boolean canDoInKingdom(KingdomPermissionLevels.Permissions permission, UUID player) {
        if (!permissionsWithoutLocation.contains(permission)) return false;

        KingdomFullDTO playerKingdomDto = this.getPlayerKingdom(player);

        if (playerKingdomDto == null) return false;

        List<KingdomPermissionLevels.Permissions> playerPermissions = Arrays.asList(playerKingdomDto.getMembers().get(player).getPermissions());

        return playerPermissions.contains(KingdomPermissionLevels.Permissions.ALL) || playerPermissions.contains(permission);
    }

    public boolean areaTouchAKingdomClaim(AreaDTO area) {
        return areaTouchAKingdomClaim(area, -1);
    }

    public boolean areaTouchAKingdomClaim(AreaDTO area, long exceptId) {
        List<KingdomClaimDTO> kingdomClaims = new ArrayList<>();
        for (KingdomFullDTO kingdom : this.kingdoms) {
            if (kingdom.getClaim().getArea().getWorld() == area.getWorld()) {
                kingdomClaims.add(kingdom.getClaim());
            }
        }

        for (KingdomClaimDTO claim : kingdomClaims) {
            if (claim.getArea().mixed(area) && exceptId != claim.getArea().getId()) {
                return true;
            }
        }

        return false;
    }

    public KingdomFullDTO getKingdomAt(Location location, List<KingdomFullDTO> from) {
        for (KingdomFullDTO dto : from) {
            if (dto.getClaim().getArea().isIn(location)) {
                return dto;
            }
        }

        return null;
    }

    public KingdomFullDTO getKingdomAt(Location location) {
        return getKingdomAt(location, kingdoms);
    }

    public boolean nameAlreadyTaken(String name) {
        for (KingdomFullDTO dto : kingdoms) {
            if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', dto.getName())).equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    public int getRank(KingdomFullDTO kingdom) {
        List<KingdomFullDTO> ranking = getRanking();
        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).getId() == kingdom.getId()) {
                return i + 1;
            }
        }
        return 0;
    }

    public List<KingdomFullDTO> getRanking() {
        return kingdoms.stream().sorted((o1, o2) -> {
            return o2.getRankingPoints() - o1.getRankingPoints();
        }).collect(Collectors.toList());
    }

    public PersonalClaimFullDTO getPlayerPersonalClaimFull(UUID player) {
        for (PersonalClaimFullDTO claim : personalClaims) {
            if (claim.getOwner().equals(player)) {
                return claim;
            }
        }

        return null;
    }

    public List<PersonalClaimFullDTO> getPlayerTrustedPersonalClaimsFull(UUID player) {
        List<PersonalClaimFullDTO> trustedClaims = new ArrayList<>();

        for (PersonalClaimFullDTO claim : personalClaims) {
            if (claim.getTrustedPlayers().contains(player)) {
                trustedClaims.add(claim);
            }
        }

        return trustedClaims;
    }

    public boolean areaTouchAPersonalClaim(AreaDTO area) {
        return areaTouchAPersonalClaim(area, -1);
    }

    public boolean areaTouchAPersonalClaim(AreaDTO area, long exceptId) {
        for (PersonalClaimFullDTO claim : personalClaims) {
            if (claim.getArea().mixed(area) && exceptId != claim.getArea().getId()) {
                return true;
            }
        }

        return false;
    }

    public void registerKingdom(KingdomFullDTO dto) {
        kingdoms.add(dto);
        kingdomService.createKingdom(dto);
    }

    public void updateKingdom(KingdomFullDTO dto) {
        kingdomService.mergeKingdom(dto);
    }

    public void removeKingdom(KingdomFullDTO dto) {
        kingdoms.remove(dto);
        kingdomService.deleteKingdom(dto);
    }

    public void registerClaim(PersonalClaimFullDTO dto) {
        personalClaims.add(dto);
        personalClaimService.createPersonalClaim(dto);
    }

    public void updateClaim(PersonalClaimFullDTO dto) {
        personalClaimService.mergePersonalClaim(dto);
    }

    public void removeClaim(PersonalClaimFullDTO dto) {
        personalClaims.remove(dto);
        personalClaimService.deletePersonalClaim(dto);
    }

    public KingdomFullDTO getKingdomById(long id) {
        for (KingdomFullDTO kingdom : kingdoms) {
            if (kingdom.getId() == id) {
                return kingdom;
            }
        }

        return null;
    }

    public void init() {
        kingdoms.addAll(kingdomService.getAllKingdomFull());
        personalClaims.addAll(personalClaimService.getAllPersonalClaimFull());
    }
}
