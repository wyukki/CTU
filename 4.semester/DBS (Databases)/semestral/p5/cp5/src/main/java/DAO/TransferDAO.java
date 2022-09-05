package DAO;
import entity.BranchEntity;
import entity.TransferEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransferDAO implements DAO<TransferEntity> {

    private final List<TransferEntity> transfers = new ArrayList<>();

    @Override
    public Optional<TransferEntity> get(int id) {
        if (transfers.isEmpty()) return Optional.empty();
        Optional<TransferEntity> entity;
        for (TransferEntity transfer : transfers) {
            if (transfer.getArtkey() == id) {
                return Optional.of(transfer);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<TransferEntity> getAll() {
        return transfers;
    }

    @Override
    public void create(TransferEntity transferEntity) {
        transfers.add(transferEntity);
    }

    @Override
    public void update(TransferEntity transferEntity, String[] params) {
        if (params.length < 12) {
            System.err.println("You need to pass all 3 arguments to change record!");
            throw new RuntimeException();
        }
        BranchEntity currBranch = transferEntity.getBranch();
        BranchDAO branchDAO = new BranchDAO();
        String[] branchParams = new String[10];
        System.arraycopy(params, 2, branchParams, 0, 10);
        branchDAO.update(currBranch, branchParams);
        int newArtKey = Integer.parseInt(params[0]);
        int newAmount = Integer.parseInt(params[1]);
        transferEntity.setAmount(newArtKey);
        transferEntity.setBranch(currBranch);
        transferEntity.setAmount(newAmount);
    }

    @Override
    public void delete(TransferEntity transferEntity) {
        transfers.remove(transferEntity);
    }
}
