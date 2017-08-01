package net.octoplar.backend.repository;

import net.octoplar.backend.entity.CoffeeType;

import java.util.List;

/**
 * Created by Octoplar.
 */
public interface CoffeeTypeDao {
    void create(CoffeeType entity);

    List<CoffeeType> readAll();
    List<CoffeeType> readAvailable();
    CoffeeType byId(int id);

    void update(CoffeeType entity);

    void delete(CoffeeType entity);
}
