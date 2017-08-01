package net.octoplar.vaadin.components;

import com.vaadin.ui.Component;
import net.octoplar.backend.entity.CoffeeOrderItem;

import java.util.Collection;

/**
 * Created by Octoplar.
 */
public interface CoffeeOrderItemManager extends Component, International {
    void addItem(CoffeeOrderItem item);
    void addItems(Collection<CoffeeOrderItem> list);
    void clear();
    Collection<CoffeeOrderItem> getItems();
}

