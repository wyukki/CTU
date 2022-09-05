package DAO;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> get(int id);
    List<T> getAll();
    void create(T t);
    void update(T t, String[] params);
    void delete(T t);
}
