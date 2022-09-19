package fr.blockincraft.kingdoms.core.service;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.dto.KingdomLightDTO;
import fr.blockincraft.kingdoms.core.entity.Area;
import fr.blockincraft.kingdoms.core.entity.Kingdom;
import fr.blockincraft.kingdoms.core.repository.AreaRepositoryImpl;
import fr.blockincraft.kingdoms.core.repository.KingdomRepositoryImpl;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class KingdomService {
    private final SessionFactory sessionFactory;
    private final KingdomRepositoryImpl kingdomRepository;
    private final AreaRepositoryImpl areaRepository;

    public KingdomService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.kingdomRepository = new KingdomRepositoryImpl(sessionFactory);
        this.areaRepository = new AreaRepositoryImpl(sessionFactory);
    }

    public KingdomFullDTO getKingdomFullById(long id) {
        Session session = null;
        Transaction transaction = null;
        KingdomFullDTO dto = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            Kingdom kingdom = kingdomRepository.getById(id);
            Hibernate.initialize(kingdom);
            dto = new KingdomFullDTO(kingdom);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return dto;
    }

    public KingdomLightDTO getKingdomLightById(long id) {
        Session session = null;
        Transaction transaction = null;
        KingdomLightDTO dto = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            Kingdom kingdom = kingdomRepository.getById(id);
            dto = new KingdomLightDTO(kingdom);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return dto;
    }

    public List<KingdomFullDTO> getAllKingdomFull() {
        Session session = null;
        Transaction transaction = null;
        List<KingdomFullDTO> dtoList = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            dtoList = new ArrayList<>();

            for (Kingdom kingdom : kingdomRepository.getAll()) {
                Hibernate.initialize(kingdom);
                dtoList.add(new KingdomFullDTO(kingdom));
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

    public List<KingdomLightDTO> getAllKingdomLight() {
        Session session = null;
        Transaction transaction = null;
        List<KingdomLightDTO> dtoList = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            dtoList = new ArrayList<>();

            for (Kingdom kingdom : kingdomRepository.getAll()) {
                dtoList.add(new KingdomLightDTO(kingdom));
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

    public List<KingdomFullDTO> getAllKingdomFullInWorld(UUID world) {
        Session session = null;
        Transaction transaction = null;
        List<KingdomFullDTO> dtoList = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            dtoList = new ArrayList<>();

            for (Kingdom kingdom : kingdomRepository.getAllInWorld(world)) {
                Hibernate.initialize(kingdom);
                dtoList.add(new KingdomFullDTO(kingdom));
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

    public void createKingdom(KingdomFullDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            Kingdom kingdom = new Kingdom(dto);

            for (Area area : kingdom.getConstructions().keySet()) {
                areaRepository.create(area);
            }

            kingdomRepository.create(kingdom);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        } finally {
            if (session != null) session.close();
        }
    }

    public void mergeKingdom(KingdomFullDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            Kingdom kingdom = new Kingdom(dto);

            for (Area area : kingdom.getConstructions().keySet()) {
                if (area.getId() > 0) {
                    areaRepository.merge(area);
                } else {
                    areaRepository.create(area);
                }
            }

            kingdomRepository.merge(kingdom);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public void deleteKingdom(KingdomFullDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            Kingdom kingdom = new Kingdom(dto);
            Set<Area> areas = kingdom.getConstructions().keySet();

            kingdomRepository.delete(kingdom);

            for (Area area : areas) {
                areaRepository.delete(area);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public KingdomFullDTO getPlayerKingdom(UUID player) {
        Session session = null;
        Transaction transaction = null;
        KingdomFullDTO dto = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            for (Kingdom kingdom : kingdomRepository.getAll()) {
                if (kingdom.getMembers().containsKey(player)) {
                    dto = new KingdomFullDTO(kingdom);
                    break;
                }
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return dto;
    }
}
