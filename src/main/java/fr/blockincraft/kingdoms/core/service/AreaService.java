package fr.blockincraft.kingdoms.core.service;

import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import fr.blockincraft.kingdoms.core.entity.Area;
import fr.blockincraft.kingdoms.core.repository.AreaRepositoryImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AreaService {
    private final SessionFactory sessionFactory;
    private final AreaRepositoryImpl areaRepository;

    public AreaService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.areaRepository = new AreaRepositoryImpl(sessionFactory);
    }

    public AreaDTO getAreaById(long id) {
        Session session = null;
        Transaction transaction = null;
        AreaDTO dto = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            Area area = areaRepository.getById(id);
            dto = new AreaDTO(area);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return dto;
    }

    public List<AreaDTO> getAllAreas() {
        Session session = null;
        Transaction transaction = null;
        List<AreaDTO> dtoList = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            dtoList = new ArrayList<>();

            for (Area area : areaRepository.getAll()) {
                AreaDTO dto = new AreaDTO(area);
                dtoList.add(dto);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return dtoList;
    }

    public List<AreaDTO> getAllAreasInWorld(UUID world) {
        Session session = null;
        Transaction transaction = null;
        List<AreaDTO> dtoList = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            dtoList = new ArrayList<>();

            for (Area area : areaRepository.getAllInWorld(world)) {
                AreaDTO dto = new AreaDTO(area);
                dtoList.add(dto);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return dtoList;
    }

    public void createArea(AreaDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            areaRepository.create(new Area(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public void mergeArea(AreaDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            areaRepository.merge(new Area(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public void deleteArea(AreaDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            areaRepository.delete(new Area(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
