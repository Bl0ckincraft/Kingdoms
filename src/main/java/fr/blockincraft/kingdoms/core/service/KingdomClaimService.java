package fr.blockincraft.kingdoms.core.service;

import fr.blockincraft.kingdoms.core.dto.KingdomClaimDTO;
import fr.blockincraft.kingdoms.core.entity.KingdomClaim;
import fr.blockincraft.kingdoms.core.repository.KingdomClaimRepositoryImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KingdomClaimService {
    private final SessionFactory sessionFactory;
    private final KingdomClaimRepositoryImpl kingdomClaimRepository;

    public KingdomClaimService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.kingdomClaimRepository = new KingdomClaimRepositoryImpl(sessionFactory);
    }

    public KingdomClaimDTO getKingdomClaimById(long id) {
        Session session = null;
        Transaction transaction = null;
        KingdomClaimDTO dto = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            KingdomClaim claim = kingdomClaimRepository.getById(id);
            dto = new KingdomClaimDTO(claim);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return dto;
    }

    public List<KingdomClaimDTO> getAllKingdomClaims() {
        Session session = null;
        Transaction transaction = null;
        List<KingdomClaimDTO> dtoList = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            dtoList = new ArrayList<>();

            for (KingdomClaim claim : kingdomClaimRepository.getAll()) {
                dtoList.add(new KingdomClaimDTO(claim));
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

    public List<KingdomClaimDTO> getAllKingdomClaimsInWorld(UUID world) {
        Session session = null;
        Transaction transaction = null;
        List<KingdomClaimDTO> dtoList = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            dtoList = new ArrayList<>();

            for (KingdomClaim claim : kingdomClaimRepository.getAllInWorld(world)) {
                dtoList.add(new KingdomClaimDTO(claim));
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

    public void createKingdomClaim(KingdomClaimDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            kingdomClaimRepository.create(new KingdomClaim(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public void mergeKingdomClaim(KingdomClaimDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            kingdomClaimRepository.merge(new KingdomClaim(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public void deleteKingdomClaim(KingdomClaimDTO dto) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            kingdomClaimRepository.delete(new KingdomClaim(dto));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
