package net.octoplar.backend.validation;

import net.octoplar.backend.entity.CoffeeOrder;
import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.entity.CoffeeType;
import net.octoplar.backend.service.CoffeeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Octoplar.
 */

@Component
public class CoffeeOrderFinalValidatorImpl implements CoffeeOrderFinalValidator {

    @Autowired
    private CoffeeTypeService coffeeTypeService;

    @Override
    public boolean isValid(CoffeeOrder order) {
        if (order.getName()!=null&&order.getName().length()>100)
            return false;
        if (order.getAddress()==null||order.getAddress().length()>200)
            return false;
        //check every order item
        Set<CoffeeType> available=new HashSet<>(coffeeTypeService.readAvailable());
        for(CoffeeOrderItem coi:order.getItems()) {
            if (!available.contains(coi.getCoffeeType()))
                return false;
            if (coi.getQuantity()>100)
                return false;
        }
        return true;
    }
}

