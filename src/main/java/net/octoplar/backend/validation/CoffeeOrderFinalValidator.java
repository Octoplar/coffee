package net.octoplar.backend.validation;

import net.octoplar.backend.entity.CoffeeOrder;

/**
 * Created by Octoplar.
 */
public interface CoffeeOrderFinalValidator {
    boolean isValid(CoffeeOrder order);
}
