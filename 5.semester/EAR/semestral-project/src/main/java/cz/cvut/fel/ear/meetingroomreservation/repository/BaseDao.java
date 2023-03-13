package cz.cvut.fel.ear.meetingroomreservation.repository;

import cz.cvut.fel.ear.meetingroomreservation.exception.PersistenceException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public abstract class BaseDao<T> implements GenericDao<T> {
    @PersistenceContext
    protected EntityManager em;

    protected final Class<T> type;

    public BaseDao(Class<T> type) {
        this.type = type;
    }

    @Override
    public T find(Long id) {
        Objects.requireNonNull(id);
        return em.find(type, id);
    }

    @Override
    public List<T> findAll() {
        try {
            return em.createQuery("SELECT e FROM " + type.getSimpleName() + " e", type).getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void persist(T entity) {
        Objects.requireNonNull(entity);
        try {
            em.persist(entity);
        } catch (RuntimeException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void persist(Collection<T> entities) {
        Objects.requireNonNull(entities);
        if (entities.isEmpty()) return;
        try {
            entities.forEach(this::persist);
        } catch (RuntimeException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public T update(T entity) {
        Objects.requireNonNull(entity);
        try {
            return em.merge(entity);
        } catch (RuntimeException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void remove(T entity) {
        Objects.requireNonNull(entity);
        try {
            final T toRemove = em.merge(entity);
            if (toRemove != null) em.remove(entity);
        } catch (RuntimeException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public boolean exists(Long id) {
        return id != null && em.find(type, id) != null;
    }
}