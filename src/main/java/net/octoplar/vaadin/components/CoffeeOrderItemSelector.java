package net.octoplar.vaadin.components;

import com.vaadin.ui.Component;
import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.entity.CoffeeType;

import java.util.Set;

/**
 * Created by Octoplar.
 */

public interface CoffeeOrderItemSelector extends Component, International {
    //items to display
    void setCoffeeTypes(Set<CoffeeType> set);

    //listeners
    void addItemSelectListener(ItemSelectListener listener);
    void removeItemSelectListener(ItemSelectListener listener);



    abstract class ItemSelectListener{
        public abstract void callback(CoffeeOrderItem selectedItem);
    }
}

