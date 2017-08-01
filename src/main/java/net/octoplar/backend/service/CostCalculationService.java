package net.octoplar.backend.service;

import net.octoplar.backend.entity.CoffeeOrder;
import net.octoplar.backend.util.DetailedOrderCost;
import net.octoplar.backend.util.OrderCost;

/**
 * Created by Octoplar.
 */
public interface CostCalculationService {
    OrderCost calculateOrderCost(CoffeeOrder order);
    DetailedOrderCost calculateDetailedOrderCost(CoffeeOrder order);
}

