package DAO;

import entity.DoctorEntity;
import entity.DonationEntity;
import entity.DonorEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonationDAO implements DAO<DonationEntity> {
    private List<DonationEntity> donationEntities = new ArrayList<>();

    @Override
    public Optional<DonationEntity> get(int id) {
        return Optional.ofNullable(donationEntities.get(id));
    }

    @Override
    public List<DonationEntity> getAll() {
        return donationEntities;
    }

    @Override
    public void create(DonationEntity donationEntity) {
        donationEntities.add(donationEntity);
    }

    @Override
    public void update(DonationEntity donationEntity, String[] params) {
        if (params.length < 8 ) {
            System.err.println("You need to pass all 8 arguments to change record!");
            throw new RuntimeException();
        }
        int newDonorPass = Integer.parseInt(params[0]);
        String newDonorMedCard = params[1];
        String newDonorName = params[2];
        String newDonorPhone = params[3];
        int newDoctorPass = Integer.parseInt(params[4]);
        int newDoctorWorkerID = Integer.parseInt(params[5]);
        String newDoctorName= params[6];
        String newDoctorPhone = params[7];
        DonorEntity currDonor = donationEntity.getDonorByDonorPass();
        DoctorEntity currDoctor = donationEntity.getDoctorByDoctorpass();
        currDonor.setPass(newDonorPass);
        currDonor.setMedcard(newDonorMedCard);
        currDonor.setName(newDonorName);
        currDoctor.setPhone(newDonorPhone);
        currDoctor.setPass(newDoctorPass);
        currDoctor.setWorkerid(newDoctorWorkerID);
        currDoctor.setName(newDoctorName);
        currDoctor.setPhone(newDoctorPhone);
        donationEntity.setDonorByDonorPass(currDonor);
        donationEntity.setDoctorByDoctorpass(currDoctor);
    }

    @Override
    public void delete(DonationEntity donationEntity) {
        donationEntities.remove(donationEntity);
    }
}
