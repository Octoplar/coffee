package net.octoplar.backend.service;

import net.octoplar.backend.entity.CoffeeOrder;
import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.repository.ConfigurationDao;
import net.octoplar.backend.util.DetailedOrderCost;
import net.octoplar.backend.util.OrderCost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Octoplar.
 */
@Service
@Transactional(readOnly = true, timeout = 30, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
public class CostCalculationServiceImpl implements CostCalculationService{
    @Autowired
    private ConfigurationDao configurationDao;


    @Override
    public OrderCost calculateOrderCost(CoffeeOrder order) {
        double delCost=Double.parseDouble(configurationDao.get("delivery_cost"));
        double delFree=Double.parseDouble(configurationDao.get("free_delivery"));
        int freeCup=Integer.parseInt(configurationDao.get("free_cup"));

        double cost=0;
        for(CoffeeOrderItem coi:order.getItems()){
            cost+=coi.getCoffeeType().getPrice()*(coi.getQuantity()- coi.getQuantity()/freeCup);
        }

        if (cost<delFree)
            return new OrderCost(cost, delCost);
        else
            return  new OrderCost(cost, 0);
    }

    @Override
    public DetailedOrderCost calculateDetailedOrderCost(CoffeeOrder order) {
        double delCost=Double.parseDouble(configurationDao.get("delivery_cost"));
        double delFree=Double.parseDouble(configurationDao.get("free_delivery"));
        int freeCup=Integer.parseInt(configurationDao.get("free_cup"));

        Map<CoffeeOrderItem, Double> costMap=new HashMap<>();
        double cost=0;
        for(CoffeeOrderItem coi:order.getItems()){
            double itemCost=coi.getCoffeeType().getPrice()*(coi.getQuantity()- coi.getQuantity()/freeCup);
            cost+=itemCost;
            costMap.put(coi,itemCost);
        }

        if (cost<delFree)
            return new DetailedOrderCost(cost, delCost, costMap);
        else
            return  new DetailedOrderCost(cost, 0, costMap);
    }
}

