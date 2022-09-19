package fr.blockincraft.kingdoms.core.repository;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.core.entity.KingdomClaim;
import fr.blockincraft.kingdoms.core.entity.PersonalClaim;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.UUID;

public class PersonalClaimRepositoryImpl {
    private final SessionFactory sessionFactory;

    public PersonalClaimRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public PersonalClaim getById(long id) {
        Session session = sessionFactory.getCurrentSession();
        PersonalClaim personalClaim = null;

        personalClaim = session.get(PersonalClaim.class, id);

        return personalClaim;
    }

    public List<PersonalClaim> getAll() {
        Session session = sessionFactory.getCurrentSession();
        List<PersonalClaim> personalClaims = null;

        personalClaims = session.createNamedQuery("getAllPersonalClaims", PersonalClaim.class).getResultList();

        return personalClaims;
    }

    public List<PersonalClaim> getAllInWorld(UUID world) {
        Session session = sessionFactory.getCurrentSession();
        List<PersonalClaim> personalClaims = null;

        personalClaims = session.createNamedQuery("getAllPersonalClaimsInWorld", PersonalClaim.class).setParameter("worldId", world).getResultList();

        return personalClaims;
    }

    public void create(PersonalClaim personalClaim) {
        Session session = sessionFactory.getCurrentSession();

        session.persist(personalClaim);
    }

    public void merge(PersonalClaim personalClaim) {
        Session session = sessionFactory.getCurrentSession();

        session.merge(personalClaim);
    }

    public void delete(PersonalClaim personalClaim) {
        Session session = sessionFactory.getCurrentSession();

        session.remove(personalClaim);
    }
}
