package fr.blockincraft.kingdoms.core.entity;

import fr.blockincraft.kingdoms.core.dto.CurrentCommissionDTO;
import fr.blockincraft.kingdoms.core.enums.Commissions;
import jakarta.persistence.*;

@NamedQuery(query = "SELECT c FROM CurrentCommission c", name = "getAllCurrentCommissions")
@Entity
public class CurrentCommission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Commissions commission;
    @Column(nullable = false)
    private int progression;
    @Column(nullable = false)
    private int objective;

    public CurrentCommission() {

    }

    public CurrentCommission(CurrentCommissionDTO dto) {
        this.id = dto.getId();
        this.commission = dto.getCommission();
        this.progression = dto.getProgression();
        this.objective = dto.getObjective();
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
