package fr.blockincraft.kingdoms.core.repository;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.core.entity.CurrentCommission;
import fr.blockincraft.kingdoms.core.entity.KingdomClaim;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.UUID;

public class KingdomClaimRepositoryImpl {
    private final SessionFactory sessionFactory;

    public KingdomClaimRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public KingdomClaim getById(long id) {
        Session session = sessionFactory.getCurrentSession();
        KingdomClaim kingdomClaim = null;

        kingdomClaim = session.get(KingdomClaim.class, id);

        return kingdomClaim;
    }

    public List<KingdomClaim> getAll() {
        Session session = sessionFactory.getCurrentSession();
        List<KingdomClaim> kingdomClaims = null;

        kingdomClaims = session.createNamedQuery("getAllKingdomClaims", KingdomClaim.class).getResultList();

        return kingdomClaims;
    }

    public List<KingdomClaim> getAllInWorld(UUID world) {
        Session session = sessionFactory.getCurrentSession();
        List<KingdomClaim> kingdomClaims = null;

        kingdomClaims = session.createNamedQuery("getAllKingdomClaimsInWorld", KingdomClaim.class).setParameter("worldId", world).getResultList();

        return kingdomClaims;
    }

    public void create(KingdomClaim kingdomClaim) {
        Session session = sessionFactory.getCurrentSession();

        session.persist(kingdomClaim);
    }

    public void merge(KingdomClaim kingdomClaim) {
        Session session = sessionFactory.getCurrentSession();

        session.merge(kingdomClaim);
    }

    public void delete(KingdomClaim kingdomClaim) {
        Session session = sessionFactory.getCurrentSession();

        session.remove(kingdomClaim);
    }
}
