package Service;

import DAO.*;
import entity.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;

public class TransferService {
    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final EntityTransaction et;
    private final DAO<TransferEntity> dao;
    private int transferLastKey;
    private final Random random = new Random();

    public TransferService() {
        this.dao = new TransferDAO();
        this.emf = Persistence.createEntityManagerFactory("ApplicationPU");
        this.em = emf.createEntityManager();
        this.et = em.getTransaction();
    }

    public void simulation() {
        addRecordsToDAO();
        System.out.println(transferLastKey);
        getAllTransfers();
        new_transfer(random.nextInt(10), transferLastKey, 832429, "semenvol");
        getAllTransfers();
        delete(random.nextInt(transferLastKey));
    }

    private void addRecordsToDAO() {
        TypedQuery<TransferEntity> q = em.createQuery(
                "select t from TransferEntity as t order by t.artkey asc",
                TransferEntity.class
        );
        ArrayList<TransferEntity> transfers = (ArrayList<TransferEntity>) q.getResultList();
        transfers.forEach(dao::create);
        int max_key = 0;
        for (TransferEntity transfer : transfers) {
            if (max_key < transfer.getArtkey()) {
                max_key = transfer.getArtkey();
            }
        }
        transferLastKey = max_key + 1;
    }

    private void getAllTransfers() {
        dao.getAll().forEach(System.out::println);
    }

    private TransferEntity getTransfer(int artKey) {
        Optional<TransferEntity> entity = dao.get(artKey);
        if (entity.isPresent() && entity.get().getArtkey() == artKey)  {
            return entity.get();
        } else {
            System.out.println("Record transfer with artkey " + artKey + " does not exist!");
        }
        return null;
    }

    private boolean new_transfer(int brNumber, int artKey, int CID, String ceo) {
        et.begin();
        BranchEntityPK pk = new BranchEntityPK();
        pk.setBranchnumber(brNumber);
        pk.setCeo(ceo);
        pk.setCid(CID);
        BranchEntity branch = em.find(BranchEntity.class, pk);
        int usedPlace, capacity;
        usedPlace = branch.getBankusedplace();
        capacity = branch.getBankcapacity();
        if (usedPlace < 10) {
            return false;
        }
        branch.setBankusedplace(usedPlace - 20);
        branch.setBranchnumber(brNumber);
        branch.setCid(CID);
        branch.setCeo(ceo);
        TransferEntity transfer = new TransferEntity();
        System.out.println(transfer);
        transfer.setArtkey(artKey);
        System.out.println(transfer);
        transfer.setBranch(branch);
        System.out.println(transfer);
        transfer.setAmount(usedPlace);
        em.createNativeQuery(
                "insert into transfer(artkey, branchnumber, cid, ceo, amount) values (?,?,?,?,?)")
                .setParameter(1, artKey)
                .setParameter(2, brNumber)
                .setParameter(3, CID)
                .setParameter(4, ceo)
                .setParameter(5, usedPlace)
                .executeUpdate();
        em.merge(branch);
        et.commit();
        transferLastKey++;
        dao.create(transfer);
        return true;
    }


    private void update(TransferEntity transferEntity, BranchEntity branch, String[] params) {
        et.begin();
        dao.update(transferEntity, params);
        transferEntity.setAmount(Integer.parseInt(params[1]));
        transferEntity.setBranch(branch);
        em.merge(transferEntity);
        et.commit();
    }

    private void delete(int artKey) {
        et.begin();
        TransferEntity transfer = em.find(TransferEntity.class, artKey);
        dao.delete(transfer);
        em.remove(transfer);
        et.commit();
    }

}
