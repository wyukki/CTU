package DAO;

import entity.BranchEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BranchDAO implements DAO<BranchEntity> {
    private final List<BranchEntity> branches = new ArrayList<>();

    @Override
    public Optional<BranchEntity> get(int id) {
        return Optional.ofNullable(branches.get(id));
    }

    @Override
    public List<BranchEntity> getAll() {
        return branches;
    }

    @Override
    public void create(BranchEntity branchEntity) {
        branches.add(branchEntity);
    }

    @Override
    public void update(BranchEntity branchEntity, String[] params) {
        if (params.length < 10) {
            System.err.println("You need to pass all 10 arguments to change record!");
            throw new RuntimeException();
        }
        int newBranchNumber = Integer.parseInt(params[0]);
        int newCID  = Integer.parseInt(params[1]);
        String newCEO = params[2];
        int newBankID = Integer.parseInt(params[3]);
        int newBankCapacity = Integer.parseInt(params[4]);
        int newBankUsedPlace = Integer.parseInt(params[5]);
        String newHospital = params[6];
        String newCity = params[7];
        String newStreet = params[8];
        String newZIP = params[9];
        branchEntity.setBranchnumber(newBranchNumber);
        branchEntity.setCid(newCID);
        branchEntity.setCeo(newCEO);
        branchEntity.setBankid(newBankID);
        branchEntity.setBankcapacity(newBankCapacity);
        branchEntity.setBankusedplace(newBankUsedPlace);
        branchEntity.setHospital(newHospital);
        branchEntity.setCity(newCity);
        branchEntity.setStreet(newStreet);
        branchEntity.setZip(newZIP);
    }

    @Override
    public void delete(BranchEntity branchEntity) {
        branches.remove(branchEntity);
    }
}
