package fr.blockincraft.kingdoms.core.dto;

import fr.blockincraft.kingdoms.core.entity.CurrentCommission;
import fr.blockincraft.kingdoms.core.enums.Commissions;
public class CurrentCommissionDTO {
    private long id;
    private Commissions commission;
    private int progression = 0;
    private int objective = 0;

    public CurrentCommissionDTO() {

    }

    public CurrentCommissionDTO(CurrentCommission model) {
        this.id = model.getId();
        this.commission = model.getCommission();
        this.progression = model.getProgression();
        this.objective = model.getObjective();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Commissions getCommission() {
        return commission;
    }

    public void setCommission(Commissions commission) {
        this.commission = commission;
    }

    public int getProgression() {
        return progression;
    }

    public void setProgression(int progression) {
        this.progression = progression;
    }

    public int getObjective() {
        return objective;
    }

    public void setObjective(int objective) {
        this.objective = objective;
    }
}
