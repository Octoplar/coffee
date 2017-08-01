package net.octoplar.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Octoplar.
 */
@Entity
@Table(name="CoffeeOrderItem")
public class CoffeeOrderItem extends AbstractEntity {


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @NotNull(message = "coffee type required")
    private CoffeeType coffeeType;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @NotNull(message = "coffee order required")
    private CoffeeOrder coffeeOrder;

    @Column(name = "quantity")
    @NotNull(message = "quantity required")
    @Min(value = 1, message = "quantity must be integer in 1..100 range")
    @Max(value = 100, message = "quantity must be integer in 1..100 range")
    private Integer quantity;

    public CoffeeOrderItem() {
    }

    public CoffeeOrderItem(CoffeeType coffeeType, Integer quantity) {
        this.coffeeType = coffeeType;
        this.quantity = quantity;
    }

    public CoffeeOrder getCoffeeOrder() {
        return coffeeOrder;
    }

    public void setCoffeeOrder(CoffeeOrder coffeeOrder) {
        this.coffeeOrder = coffeeOrder;
    }

    public CoffeeType getCoffeeType() {
        return coffeeType;
    }

    public void setCoffeeType(CoffeeType coffeeType) {
        this.coffeeType = coffeeType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

