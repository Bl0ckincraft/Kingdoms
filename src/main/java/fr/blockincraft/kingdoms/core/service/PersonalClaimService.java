package fr.blockincraft.kingdoms.core.service;

import fr.blockincraft.kingdoms.core.dto.PersonalClaimFullDTO;
import fr.blockincraft.kingdoms.core.dto.PersonalClaimLightDTO;
import fr.blockincraft.kingdoms.core.entity.PersonalClaim;
import fr.blockincraft.kingdoms.core.repository.PersonalClaimRepositoryImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonalClaimService {
    private final SessionFactory sessionFactory;
    private final PersonalClaimRepositoryImpl personalClaimRepository;

    public PersonalClaimService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        personalClaimRepository = new PersonalClaimRepositoryImpl(sessionFactory);
    }

    public PersonalClaimFullDTO getPersonalClaimFullById(long id) {
        Session session = null;
        Transaction transaction = null;
        PersonalClaimFullDTO dto = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            PersonalClaim claim = personalClaimRepository.getById(id);
            dto = new PersonalClaimFullDTO(claim);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return dto;
    }

    public PersonalClaimLightDTO getPersonalClaimLightById(long id) {
        Session session = null;
        Transaction transaction = null;
        PersonalClaimLightDTO dto = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            PersonalClaim claim = personalClaimRepository.getById(id);
            dto = new PersonalClaimLightDTO(claim);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return dto;
    }

    public List<PersonalClaimFullDTO> getAllPersonalClaimFull() {
        Session session = null;
        Transaction transaction = null;
        List<PersonalClaimFullDTO> dtoList = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            dtoList = new ArrayList<>();

            for (PersonalClaim claim : personalClaimRepository.getAll()) {
                dtoList.add(new PersonalClaimFullDTO(claim));
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

    public List<PersonalClaimLightDTO> getAllPersonalClaimLight() {
        Session session = null;
        Transaction transaction = null;
        List<PersonalClaimLightDTO> dtoList = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            dtoList = new ArrayList<>();

            for (PersonalClaim claim : personalClaimRepository.getAll()) {
                dtoList.add(new PersonalClaimLightDTO(claim));
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

    public List<PersonalClaimFullDTO> getAllPersonalClaimFullInWorld(UUID world) {
        Session session = null;
        Transaction transaction = null;
        List<PersonalClaimFullDTO> dtoList = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            dtoList = new ArrayList<>();

            for (PersonalClaim claim : personalClaimRepository.getAllInWorld(world)) {
                dtoList.add(new PersonalClaimFullDTO(claim));
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

    public void createPersonalClaim(PersonalClaimFullDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            personalClaimRepository.create(new PersonalClaim(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public void mergePersonalClaim(PersonalClaimFullDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            personalClaimRepository.merge(new PersonalClaim(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public void deletePersonalClaim(PersonalClaimFullDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            personalClaimRepository.delete(new PersonalClaim(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
