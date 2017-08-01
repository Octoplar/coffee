package net.octoplar.backend.service;

import net.octoplar.backend.entity.CoffeeType;

import java.util.List;

/**
 * Created by Octoplar.
 */
public interface CoffeeTypeService {
    List<CoffeeType> readAll();
    List<CoffeeType> readAvailable();
}
