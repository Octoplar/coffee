package net.octoplar.backend.util;

import java.io.Serializable;

/**
 * Created by Octoplar.
 */
//contains order cost and delivery cost separately
public class OrderCost implements Cloneable, Serializable {
    private double orderCost;
    private double deliveryCost;

    public OrderCost() {
    }

    public OrderCost(double orderCost, double deliveryCost) {
        this.orderCost = orderCost;
        this.deliveryCost = deliveryCost;
    }

    public double getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(double orderCost) {
        this.orderCost = orderCost;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }
}
