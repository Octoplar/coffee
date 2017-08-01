package net.octoplar.webmvc;

import net.octoplar.backend.entity.CoffeeOrder;
import net.octoplar.backend.service.CoffeeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Octoplar.
 */
@Controller
public class OrderInfoController {
    @Autowired
    private CoffeeOrderService cos;

    @RequestMapping(value = "/orderInfo/{orderId}", method = RequestMethod.GET)
    public Callable<String> welcome(Map<String, Object> model, @PathVariable String orderId) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                int id=0;
                try{
                    id=Integer.parseInt(orderId);
                }
                catch (Exception e){
                    return "error";
                }

                CoffeeOrder order=cos.read(id);
                if (order==null)
                    return "error";

                model.put("order", order);
                return "orderInfo";
            }
        };
    }
}

