package net.octoplar.backend.service;

import net.octoplar.backend.entity.CoffeeOrder;

/**
 * Created by Octoplar.
 */
public interface CoffeeOrderService {
    void create(CoffeeOrder entity);
    CoffeeOrder read(int id);
    void update(CoffeeOrder entity);
    void delete(CoffeeOrder entity);
}