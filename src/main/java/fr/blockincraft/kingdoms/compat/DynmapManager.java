package fr.blockincraft.kingdoms.compat;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.dto.PersonalClaimFullDTO;
import fr.blockincraft.kingdoms.core.enums.Constructions;
import fr.blockincraft.kingdoms.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

import java.util.Map;

public class DynmapManager {
    private static final Registry registry = Kingdoms.getInstance().getRegistry();
    private final MarkerSet claimMarkerSet;
    private final MarkerSet kingdomMarkerSet;
    private final MarkerSet constructionMarkerSet;

    public DynmapManager() {
        DynmapAPI dynmapAPI = (DynmapAPI) Bukkit.getPluginManager().getPlugin("dynmap");

        this.claimMarkerSet = dynmapAPI.getMarkerAPI().createMarkerSet("Kingdoms.claims", Lang.DYNMAP_CLAIMS_TITLE.get(), dynmapAPI.getMarkerAPI().getMarkerIcons(), false);
        this.kingdomMarkerSet = dynmapAPI.getMarkerAPI().createMarkerSet("Kingdoms.kingdoms", Lang.DYNMAP_KINGDOMS_TITLE.get(), dynmapAPI.getMarkerAPI().getMarkerIcons(), false);
        this.constructionMarkerSet = dynmapAPI.getMarkerAPI().createMarkerSet("Kingdoms.constructions", Lang.DYNMAP_CONSTRUCTIONS_TITLE.get(), dynmapAPI.getMarkerAPI().getMarkerIcons(), false);
    }

    public void refresh() {
        if (claimMarkerSet == null || kingdomMarkerSet == null) return;

        for (AreaMarker aMarker : claimMarkerSet.getAreaMarkers()) {
            aMarker.deleteMarker();
        }
        for (PersonalClaimFullDTO claim : registry.getPersonalClaims()) {
            if (claimMarkerSet.findAreaMarker("personalClaim:" + claim.getId()) == null) {
                AreaMarker marker = claimMarkerSet.createAreaMarker("personalClaim:" + claim.getId(),
                        "Claim #" + claim.getId(),
                        false,
                        Bukkit.getWorld(claim.getArea().getWorld()).getName(),
                        new double[]{claim.getArea().getSmallestX(), claim.getArea().getBiggestX() + 1},
                        new double[]{claim.getArea().getSmallestZ(), claim.getArea().getBiggestZ() + 1},
                        false);
                marker.setFillStyle(0.5, 0x038cfc);
                marker.setLineStyle(3, 1, 0x038cfc);
            }
            refreshDescription(claim);
        }

        for (AreaMarker aMarker : kingdomMarkerSet.getAreaMarkers()) {
            aMarker.deleteMarker();
        }
        for (AreaMarker aMarker : constructionMarkerSet.getAreaMarkers()) {
            aMarker.deleteMarker();
        }
        for (KingdomFullDTO kingdom : registry.getKingdoms()) {
            if (kingdomMarkerSet.findAreaMarker("kingdomClaim:" + kingdom.getClaim().getId()) == null) {
                AreaMarker marker = kingdomMarkerSet.createAreaMarker("kingdomClaim:" + kingdom.getClaim().getId(),
                        "Kingdom #" + kingdom.getId(),
                        false,
                        Bukkit.getWorld(kingdom.getClaim().getArea().getWorld()).getName(),
                        new double[]{kingdom.getClaim().getArea().getSmallestX(), kingdom.getClaim().getArea().getBiggestX() + 1},
                        new double[]{kingdom.getClaim().getArea().getSmallestZ(), kingdom.getClaim().getArea().getBiggestZ() + 1},
                        false);
                marker.setFillStyle(0.5, 0x8803fc);
                marker.setLineStyle(3, 1, 0x8803fc);
            }
            refreshDescription(kingdom);

            for (Map.Entry<AreaDTO, Constructions> entry : kingdom.getConstructions().entrySet()) {
                if (constructionMarkerSet.findAreaMarker("kingdomConstruction:" + entry.getKey().getId()) == null) {
                    AreaMarker marker = constructionMarkerSet.createAreaMarker("kingdomConstruction:" + entry.getKey().getId(),
                            "Construction #" + entry.getKey().getId(),
                            false,
                            Bukkit.getWorld(entry.getKey().getWorld()).getName(),
                            new double[]{entry.getKey().getSmallestX(), entry.getKey().getBiggestX() + 1},
                            new double[]{entry.getKey().getSmallestZ(), entry.getKey().getBiggestZ() + 1},
                            false);
                    marker.setFillStyle(0.5, entry.getValue().color);
                    marker.setLineStyle(2, 1, entry.getValue().color);
                }
                refreshDescription(kingdom, entry.getKey(), entry.getValue());
            }
        }
    }

    public void refreshDescription(PersonalClaimFullDTO claim) {
        if (claimMarkerSet == null) return;

        String description = Lang.DYNMAP_CLAIMS_BUBBLE_TEXT.get()
                .replace("<owner_name>", Bukkit.getOfflinePlayer(claim.getOwner()).getName())
                .replace("<member_amount>", String.valueOf(claim.getTrustedPlayers().size() + 1))
                .replace("<size>", String.valueOf(claim.getArea().getSize2D()));

        claimMarkerSet.findAreaMarker("personalClaim:" + claim.getId()).setDescription(description);
    }

    public void refreshDescription(KingdomFullDTO kingdom) {
        if (kingdomMarkerSet == null) return;

        try {
            String description = Lang.DYNMAP_KINGDOMS_BUBBLE_TEXT.get()
                    .replace("<kingdom_name>", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', kingdom.getName())))
                    .replace("<rank>", String.valueOf(registry.getRank(kingdom)))
                    .replace("<owner_name>", Bukkit.getOfflinePlayer(kingdom.getOwner()).getName())
                    .replace("<member_amount>", String.valueOf(kingdom.getMembers().size()))
                    .replace("<ranking_points>", String.valueOf(kingdom.getRankingPoints()))
                    .replace("<level>", String.valueOf(Lang.getFrom(kingdom.getKingdomLevel())))
                    .replace("<size>", String.valueOf(kingdom.getClaim().getArea().getSize2D()));


            kingdomMarkerSet.findAreaMarker("kingdomClaim:" + kingdom.getId()).setDescription(description);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void refreshDescription(KingdomFullDTO kingdom, AreaDTO area, Constructions construction) {
        if (constructionMarkerSet == null) return;

        int level = Constructions.getLevel(construction, area, kingdom.getKingdomLevel());

        String description = Lang.DYNMAP_CONSTRUCTIONS_BUBBLE_TEXT.get()
                .replace("<construction_name>", Lang.getFrom(construction))
                .replace("<kingdom_name>", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', kingdom.getName())))
                .replace("<level>", String.valueOf(level))
                .replace("<max_level>", String.valueOf(construction.requirements.size()))
                .replace("<points>", String.valueOf(Constructions.getPoints(construction, level)))
                .replace("<size>", String.valueOf(area.getSize2D()));

        constructionMarkerSet.findAreaMarker("kingdomConstruction:" + area.getId()).setDescription(description);
    }
}
