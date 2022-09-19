package fr.blockincraft.kingdoms.api.objects;

import fr.blockincraft.kingdoms.core.dto.CurrentCommissionDTO;
import fr.blockincraft.kingdoms.core.enums.Commissions;

public class CurrentCommissionAO {
    private final long id;
    private final int progression;
    private final int objective;
    private final Commissions commission;

    private CurrentCommissionAO(long id, int progression, int objective, Commissions commission) {
        this.id = id;
        this.progression = progression;
        this.objective = objective;
        this.commission = commission;
    }

    public long getId() {
        return id;
    }

    public int getProgression() {
        return progression;
    }

    public int getObjective() {
        return objective;
    }

    public Commissions getCommission() {
        return commission;
    }

    public static CurrentCommissionAO fromDTO(CurrentCommissionDTO dto) {
        return new CurrentCommissionAO(dto.getId(), dto.getProgression(), dto.getObjective(), dto.getCommission());
    }
}
