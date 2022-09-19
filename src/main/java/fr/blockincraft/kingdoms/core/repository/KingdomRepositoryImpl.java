package fr.blockincraft.kingdoms.core.repository;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.core.entity.Kingdom;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.UUID;

public class KingdomRepositoryImpl {
    private final SessionFactory sessionFactory;

    public KingdomRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Kingdom getById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Kingdom kingdom = null;

        kingdom = session.get(Kingdom.class, id);

        return kingdom;
    }

    public List<Kingdom> getAll() {
        Session session = sessionFactory.getCurrentSession();
        List<Kingdom> kingdoms = null;

        kingdoms = session.createNamedQuery("getAllKingdoms", Kingdom.class).getResultList();

        return kingdoms;
    }

    public List<Kingdom> getAllInWorld(UUID world) {
        Session session = sessionFactory.getCurrentSession();
        List<Kingdom> kingdoms = null;

        kingdoms = session.createNamedQuery("getAllKingdomsInWorld", Kingdom.class).setParameter("worldId", world).getResultList();

        return kingdoms;
    }

    public void create(Kingdom kingdom) {
        Session session = sessionFactory.getCurrentSession();

        session.persist(kingdom);
    }

    public void merge(Kingdom kingdom) {
        Session session = sessionFactory.getCurrentSession();

        session.merge(kingdom);
    }

    public void delete(Kingdom kingdom) {
        Session session = sessionFactory.getCurrentSession();

        session.remove(kingdom);
    }
}
