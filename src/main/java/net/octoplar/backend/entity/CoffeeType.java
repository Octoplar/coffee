package net.octoplar.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Octoplar.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "CoffeeType.available", query = "SELECT e FROM CoffeeType AS e WHERE e.disabled <>'Y'"),
        @NamedQuery(name = "CoffeeType.all", query = "SELECT e FROM CoffeeType AS e"),
        @NamedQuery(name = "CoffeeType.byId", query = "SELECT e FROM CoffeeType AS e WHERE e.id =:id")
})
@Table(name="CoffeeType")
public class CoffeeType extends AbstractEntity{


    @Column(name = "type_name")
    @NotNull(message = "name required")
    @Size(max = 200, message = "max name length is 200")
    private String typeName;

    @Column(name = "price")
    @NotNull(message = "price required")
    @Min(value = 0, message = "price must be positive value")
    private Double price;

    @Column(name = "disabled")
    private Character disabled;


    public CoffeeType() {
    }

    public CoffeeType(String typeName, Double price, boolean disabled) {
        this.typeName = typeName;
        this.price = price;
        if (disabled)
            this.disabled='Y';
        else
            this.disabled='N';
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isDisabled() {
        return this.disabled != null && this.disabled.equals('Y');
    }

    public void setDisabled(boolean disabled) {
        if (disabled)
            this.disabled='Y';
        else
            this.disabled='N';
    }
}

