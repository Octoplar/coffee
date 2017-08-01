package net.octoplar.backend.entity;

import net.octoplar.backend.entity.AbstractEntity;
import net.octoplar.backend.entity.CoffeeOrderItem;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.atomic.DoubleAccumulator;

/**
 * Created by Octoplar.
 */
@Entity
@Table(name = "CoffeeOrder")
@NamedQueries({
        @NamedQuery(name = "CoffeeOrder.byId", query = "SELECT e FROM CoffeeOrder AS e WHERE e.id =:id")
})
public class CoffeeOrder extends AbstractEntity {


    @Column(name = "name")
    @Size(max = 100, message = "max name length is 100")
    private String name;


    @Column(name = "delivery_address")
    @NotNull(message = "address required")
    @Size(min = 1, max = 200, message = "address length mus be in 1..200 range")
    private String address;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date")
    @NotNull(message = "date required")
    private Date date;

    @Column(name = "cost")
    @NotNull(message = "cost required")
    private Double cost;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="order_id")
    @Size(min = 1, message = "order must contain at least 1 order item")
    @Valid
    private Set<CoffeeOrderItem> items;



    public CoffeeOrder() {
    }

    public CoffeeOrder(String name, String address, Date date) {
        this.name = name;
        this.address = address;
        this.date = date;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Set<CoffeeOrderItem> getItems() {
        return items;
    }

    public void setItems(Set<CoffeeOrderItem> items) {
        this.items = items;
        for(CoffeeOrderItem coi: items)
            coi.setCoffeeOrder(this);
    }
}

