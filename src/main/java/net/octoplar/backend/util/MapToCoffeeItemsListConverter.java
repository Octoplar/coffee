package net.octoplar.backend.util;

import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.entity.CoffeeType;
import net.octoplar.backend.service.CoffeeTypeService;
import net.octoplar.backend.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Octoplar.
 */
@Component
public class MapToCoffeeItemsListConverter {

    @Autowired
    private CoffeeTypeService coffeeTypeService;

    //converter + validation
    public Set<CoffeeOrderItem> convertToItems(Map<String, String> map) throws ValidationException {
        Set<CoffeeOrderItem> result=new LinkedHashSet<>();
        //read only enabled types
        List<CoffeeType> coffeeTypes= coffeeTypeService.readAvailable();
        Map<Integer, CoffeeType> coffeeTypeMap=new HashMap<>();
        for (CoffeeType ct: coffeeTypes)
            coffeeTypeMap.put(ct.getId(), ct);

        for (Map.Entry<String, String> e: map.entrySet()){
            Integer id;
            Integer quantity;
            try{
                id=new Integer(e.getKey());
                quantity=new Integer(e.getValue());
            }
            catch (NumberFormatException nfe){
                throw new ValidationException(nfe.getMessage());
            }
            //check for coffee type is available
            CoffeeType coffeeType=coffeeTypeMap.get(id);
            if (coffeeType==null)
                throw new ValidationException("coffee type unavailable or not exists: id="+id);
            CoffeeOrderItem coi=new CoffeeOrderItem(coffeeType, quantity);
            result.add(coi);
        }
        return result;
    }
}

