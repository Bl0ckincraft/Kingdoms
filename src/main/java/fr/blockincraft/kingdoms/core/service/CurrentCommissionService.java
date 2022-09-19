package fr.blockincraft.kingdoms.core.service;

import fr.blockincraft.kingdoms.core.dto.CurrentCommissionDTO;
import fr.blockincraft.kingdoms.core.entity.CurrentCommission;
import fr.blockincraft.kingdoms.core.repository.CurrentCommissionRepositoryImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CurrentCommissionService {
    private final SessionFactory sessionFactory;
    private final CurrentCommissionRepositoryImpl currentCommissionRepository;

    public CurrentCommissionService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.currentCommissionRepository = new CurrentCommissionRepositoryImpl(sessionFactory);
    }

    public CurrentCommissionDTO getCurrentCommissionById(long id) {
        Session session = null;
        Transaction transaction = null;
        CurrentCommissionDTO dto = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            CurrentCommission currentCommission = currentCommissionRepository.getById(id);
            dto = new CurrentCommissionDTO(currentCommission);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return dto;
    }

    public List<CurrentCommissionDTO> getAllCurrentCommissions() {
        Session session = null;
        Transaction transaction = null;
        List<CurrentCommissionDTO> dtoList = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            dtoList = new ArrayList<>();

            for (CurrentCommission currentCommission : currentCommissionRepository.getAll()) {
                dtoList.add(new CurrentCommissionDTO(currentCommission));
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

    public void create(CurrentCommissionDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            currentCommissionRepository.create(new CurrentCommission(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public void merge(CurrentCommissionDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            currentCommissionRepository.merge(new CurrentCommission(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public void delete(CurrentCommissionDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            currentCommissionRepository.delete(new CurrentCommission(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
