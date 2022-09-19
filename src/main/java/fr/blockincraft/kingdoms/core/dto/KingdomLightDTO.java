package fr.blockincraft.kingdoms.core.dto;

import fr.blockincraft.kingdoms.core.entity.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.time.LocalDate;

public class KingdomLightDTO {
    private long id;
    private String name;
    private int penalty = 0;
    private String penaltyReason = "";
    private LocalDate lastPenalty = LocalDate.now();
    private long bank = 0;
    private long home;

    public KingdomLightDTO() {

    }

    public KingdomLightDTO(Kingdom model) {
        this.id = model.getId();
        this.name = model.getName();
        this.penalty = model.getPenalty();
        this.penaltyReason = model.getPenaltyReason();
        this.lastPenalty = model.getLastPenalty();
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

    public void setHome(Location loc) {
        setLocations(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public void setLocations(int x, int y, int z) {
        this.home = (long)(x & 67108863) << 38 | (long)(z & 67108863) << 12 | (long)(y & 4095);
    }
}