package net.octoplar.webflow;

import net.octoplar.backend.entity.CoffeeOrder;
import net.octoplar.backend.entity.CoffeeType;
import net.octoplar.backend.service.CoffeeOrderService;
import net.octoplar.backend.service.CoffeeTypeService;
import net.octoplar.backend.service.CostCalculationService;
import net.octoplar.backend.util.DetailedOrderCost;
import net.octoplar.backend.util.MapToCoffeeItemsListConverter;
import net.octoplar.backend.util.OrderCost;
import net.octoplar.backend.validation.CoffeeOrderFinalValidator;
import net.octoplar.backend.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.LocalParameterMap;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Octoplar.
 */
@Component("webflowController")
public class CoffeeOrderWebflowController {


    @Autowired
    private MapToCoffeeItemsListConverter converter;

    @Autowired
    private CoffeeTypeService coffeeTypeService;
    @Autowired
    private CoffeeOrderService coffeeOrderService;
    @Autowired
    private CostCalculationService costCalculationService;

    @Autowired
    private CoffeeOrderFinalValidator coffeeOrderFinalValidator;

    public List<CoffeeType> getCoffeeTypes(){
        return coffeeTypeService.readAvailable();
    }

    public void setItems(CoffeeOrder order, LocalParameterMap itemsMap) throws ValidationException {
        Map<String, String> map =new LinkedHashMap<>();
        //parse parameters
        for(Map.Entry<String, Object> me:itemsMap.asMap().entrySet())
            //take only params started with digit
            if (Character.isDigit(me.getKey().charAt(0)))
                map.put(me.getKey(), me.getValue().toString());
        order.setItems(converter.convertToItems(map));
    }

    public DetailedOrderCost calculateCost(CoffeeOrder order){
        return costCalculationService.calculateDetailedOrderCost(order);
    }

    public boolean validateNameAndAddress(CoffeeOrder order){
        String name=order.getName();
        String address=order.getAddress();
        return (address!=null && address.length()<=200)
                &&(name==null || name.length()<=100);
    }

    public void fillDateAndCost(CoffeeOrder order){
        order.setDate(new Date());
        OrderCost cost=costCalculationService.calculateOrderCost(order);
        order.setCost(cost.getOrderCost()+cost.getDeliveryCost());
    }

    //validation before save
    public boolean validateOrder(CoffeeOrder order){
        return coffeeOrderFinalValidator.isValid(order);
    }

    //returns info order information reference
    public String save(CoffeeOrder order){
        coffeeOrderService.create(order);
        return "/orderInfo/"+order.getId();
    }
}

