package cz.cvut.fel.ear.meetingroomreservation.repository;

import cz.cvut.fel.ear.meetingroomreservation.model.Building;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class BuildingDao extends BaseDao<Building> {

    private Logger log = LoggerFactory.getLogger(BuildingDao.class);

    public BuildingDao() {
        super(Building.class);
    }

    public Building findByName(String name) {
        try {
            return em.createNamedQuery("Building.findByName", Building.class)
                    .setParameter("name", name).getSingleResult();
        } catch (NoResultException exception) {
            log.error("[NamedQueryError] Error: Building with name " + name + " does not exists!", exception);
            return null;
        }
    }

    public Building findByAddress(String address) {
        try {
            return em.createNamedQuery("Building.findByAddress", Building.class)
                    .setParameter("address", address).getSingleResult();
        } catch (NoResultException exception) {
            log.error("[NamedQueryError] Error: Building with address " + address + " does not exists!", exception);
            return null;
        }
    }
}
