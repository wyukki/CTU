package Service;

import DAO.DAO;
import DAO.DonationDAO;
import entity.DoctorEntity;
import entity.DonationEntity;
import entity.DonorEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class DonationService {
    private final DAO<DonationEntity> donationDAO;
    private final EntityManager em;
    private final EntityTransaction et;

    public DonationService(EntityManager manager, EntityTransaction transaction) {
        this.em = manager;
        this.et = transaction;
        donationDAO = new DonationDAO();
    }

    private void newDonation(int artKey, DoctorEntity doctor, DonorEntity donor) {
        et.begin();
        DonationEntity donationEntity = new DonationEntity();
        donationEntity.setArtkey(artKey);
        donationEntity.setDoctorByDoctorpass(doctor);
        donationEntity.setDonorByDonorPass(donor);
        donationDAO.create(donationEntity);
        em.persist(donationEntity);
        et.commit();
    }

}
