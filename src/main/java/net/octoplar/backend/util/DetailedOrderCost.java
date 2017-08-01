package net.octoplar.backend.util;

import net.octoplar.backend.entity.CoffeeOrderItem;

import java.util.Map;

/**
 * Created by Octoplar.
 */
public class DetailedOrderCost extends OrderCost {
    private Map<CoffeeOrderItem, Double> costMap;

    public DetailedOrderCost() {
    }

    public DetailedOrderCost(double orderCost, double deliveryCost, Map<CoffeeOrderItem, Double> costMap) {
        super(orderCost, deliveryCost);
        this.costMap = costMap;
    }

    public Map<CoffeeOrderItem, Double> getCostMap() {
        return costMap;
    }

    public void setCostMap(Map<CoffeeOrderItem, Double> costMap) {
        this.costMap = costMap;
    }
}

