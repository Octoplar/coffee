package net.octoplar.vaadin.components;

import com.vaadin.ui.Component;
import net.octoplar.backend.util.DetailedOrderCost;

/**
 * Created by Octoplar.
 */
public interface CoffeeOrderBill extends Component, International{
    void setValue(DetailedOrderCost value);
}
