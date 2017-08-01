package net.octoplar.backend.repository;

import net.octoplar.backend.entity.CoffeeOrder;

/**
 * Created by Octoplar.
 */
public interface CoffeeOrderDao {
    void create (CoffeeOrder entity);

    CoffeeOrder byId (int id);

    void update (CoffeeOrder entity);

    void delete (CoffeeOrder entity);
}

