package cz.cvut.fel.ear.meetingroomreservation.repository;

import java.util.Collection;
import java.util.List;

public interface GenericDao<T> {
    T find(Long id);
    List<T> findAll();
    void persist(T entity);
    void persist(Collection<T> entities);
    T update(T entity);
    void remove(T entity);
    boolean exists(Long id);
}
