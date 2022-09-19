package fr.blockincraft.kingdoms.core.repository;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.core.entity.Area;
import org.bukkit.World;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.UUID;

public class AreaRepositoryImpl {
    private final SessionFactory sessionFactory;

    public AreaRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Area getById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Area area = null;

        area = session.get(Area.class, id);

        return area;
    }

    public List<Area> getAll() {
        Session session = sessionFactory.getCurrentSession();
        List<Area> areas = null;

        areas = session.createNamedQuery("getAllAreas", Area.class).getResultList();

        return areas;
    }

    public List<Area> getAllInWorld(UUID world) {
        Session session = sessionFactory.getCurrentSession();
        List<Area> areas = null;

        areas = session.createNamedQuery("getAllAreasInWorld", Area.class).setParameter("worldId", world).getResultList();

        return areas;
    }

    public void create(Area area) {
        Session session = sessionFactory.getCurrentSession();

        session.persist(area);
    }

    public void merge(Area area) {
        Session session = sessionFactory.getCurrentSession();

        session.merge(area);
    }

    public void delete(Area area) {
        Session session = sessionFactory.getCurrentSession();

        session.remove(area);
    }
}
