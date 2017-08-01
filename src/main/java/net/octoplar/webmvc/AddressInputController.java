package net.octoplar.webmvc;

import net.octoplar.backend.entity.CoffeeOrder;
import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.service.CoffeeOrderService;
import net.octoplar.backend.service.CostCalculationService;
import net.octoplar.backend.util.DetailedOrderCost;
import net.octoplar.backend.util.NameAndAddress;
import net.octoplar.backend.util.OrderCost;
import net.octoplar.backend.validation.CoffeeOrderFinalValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by Octoplar.
 */
@Controller
@RequestMapping("/address_input")
public class AddressInputController {

    @Autowired
    private CostCalculationService costCalculationService;

    @Autowired
    private CoffeeOrderService coffeeOrderService;

    @Autowired
    private CoffeeOrderFinalValidator coffeeOrderFinalValidator;


    @GetMapping
    public Callable<String> showAddressInput(Map<String, Object> model, @SessionAttribute Set<CoffeeOrderItem> items){
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                CoffeeOrder coffeeOrder=new CoffeeOrder();
                coffeeOrder.setItems(items);
                DetailedOrderCost detailedOrderCost=costCalculationService.calculateDetailedOrderCost(coffeeOrder);
                model.put("detailedOrderCost", detailedOrderCost);


                return "address_input";

            }
        };
    }

    @PostMapping
    public Callable<String> InputNameAndAddress(Map<String, Object> model,
                                                @SessionAttribute List<CoffeeOrderItem> items,
                                                @ModelAttribute @Valid NameAndAddress nameAndAddress,
                                                HttpSession session){
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                CoffeeOrder order=new CoffeeOrder();
                //fill CoffeeOrder fields
                order.setItems(new LinkedHashSet<>(items));

                OrderCost orderCost=costCalculationService.calculateOrderCost(order);
                order.setCost(orderCost.getOrderCost()+orderCost.getDeliveryCost());

                order.setName(nameAndAddress.getName());
                order.setAddress(nameAndAddress.getAddress());
                order.setDate(new Date());

                if(coffeeOrderFinalValidator.isValid(order)){
                    //persist
                    coffeeOrderService.create(order);

                    session.setAttribute("orderId", order.getId().toString());
                    //redirect
                    return "redirect:/thank_user";
                }
                else {
                    //close session
                    session.invalidate();
                    //redirect
                    return "redirect:/error";
                }
            }
        };
    }



}

