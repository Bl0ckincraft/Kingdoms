package fr.blockincraft.kingdoms.core.repository;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.core.entity.Area;
import fr.blockincraft.kingdoms.core.entity.CurrentCommission;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.UUID;

public class CurrentCommissionRepositoryImpl {
    private final SessionFactory sessionFactory;

    public CurrentCommissionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public CurrentCommission getById(long id) {
        Session session = sessionFactory.getCurrentSession();
        CurrentCommission currentCommission = null;

        currentCommission = session.get(CurrentCommission.class, id);

        return currentCommission;
    }

    public List<CurrentCommission> getAll() {
        Session session = sessionFactory.getCurrentSession();
        List<CurrentCommission> currentCommissions = null;

        currentCommissions = session.createNamedQuery("getAllCurrentCommissions", CurrentCommission.class).getResultList();

        return currentCommissions;
    }

    public void create(CurrentCommission currentCommission) {
        Session session = sessionFactory.getCurrentSession();

        session.persist(currentCommission);
    }

    public void merge(CurrentCommission currentCommission) {
        Session session = sessionFactory.getCurrentSession();

        session.merge(currentCommission);
    }

    public void delete(CurrentCommission currentCommission) {
        Session session = sessionFactory.getCurrentSession();

        session.remove(currentCommission);
    }
}
