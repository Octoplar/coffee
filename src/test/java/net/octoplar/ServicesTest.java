package net.octoplar;

import net.octoplar.backend.entity.CoffeeOrder;
import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.entity.CoffeeType;
import net.octoplar.backend.service.CoffeeOrderService;
import net.octoplar.backend.service.CoffeeTypeService;
import net.octoplar.backend.service.CostCalculationService;
import net.octoplar.backend.util.OrderCost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Octoplar on 31.07.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicesTest {


    @Autowired
    private CoffeeTypeService cts;
    @Autowired
    private CoffeeOrderService cos;
    @Autowired
    private CostCalculationService ccs;




    /*
    CoffeeTypeService + OrderService + CostCalculationService test.
    0. test for items in db(it will be needed to make order)
    1. create order (order)
    2. save order and get its ID
    3. get order by id (fromDb)
    4. shallow content comparison
    5. deep content comparison
    6. delete created order

    !!! update method not supported in current implementation(too much code+not required)!!!
     */
    @Test
    public void MixedServiceTest() {

        //========== 0 =============
        List<CoffeeType> all=cts.readAll();
        List<CoffeeType> available=cts.readAvailable();

        //test for available items to make order
        assertNotEquals(0, available.size());
        assertNotEquals(0, all.size());
        assert(all.size()>=available.size());

        //========== 1 =============
        CoffeeOrder order=new CoffeeOrder("$name", "$address88888888888888", new Date());
        Set<CoffeeOrderItem> items= new HashSet<>();

        //different quantity for each item(it will deed later)
        for (int i = 0; i < available.size(); i++) {
            items.add(new CoffeeOrderItem(available.get(i), i+1));
        }
        order.setItems(items);
        OrderCost orderCost = ccs.calculateOrderCost(order);
        order.setCost(orderCost.getOrderCost()+orderCost.getDeliveryCost());



        //========== 2 =============
        cos.create(order);


        //========== 3 =============
        CoffeeOrder fromDb=cos.read(order.getId());


        //========== 4 =============
        assertEquals(fromDb.getAddress(), order.getAddress());
        assertEquals(fromDb.getName(), order.getName());
        assertEquals(fromDb.getItems().size(), order.getItems().size());
        //time difference <2000 ms
        assert(Math.abs(fromDb.getDate().getTime()-order.getDate().getTime())<2000);
        assert(order.getCost().equals(fromDb.getCost()));


        //========== 5 =============
        //comparing order items(quantity as key)
        List<CoffeeOrderItem> ourList=new ArrayList<>(order.getItems());
        List<CoffeeOrderItem> fromDbList=new ArrayList<>(fromDb.getItems());

        class ItemComparator implements Comparator<CoffeeOrderItem>{
            @Override
            public int compare(CoffeeOrderItem o1, CoffeeOrderItem o2) {

                if (o1.getQuantity().equals(o2.getQuantity()))
                    return 0;
                if (o1.getQuantity()>o2.getQuantity())
                    return 1;
                else
                    return -1;
            }
        }
        ItemComparator ic=new ItemComparator();

        ourList.sort(ic);
        fromDbList.sort(ic);

        for (int i = 0; i < ourList.size(); i++) {
            CoffeeOrderItem o1=ourList.get(i);
            CoffeeOrderItem o2=fromDbList.get(i);
            assert(o1.getCoffeeOrder().getId().equals(o2.getCoffeeOrder().getId()));
            assert(o1.getCoffeeType().getId().equals(o2.getCoffeeType().getId()));
            assert(o1.getQuantity().equals(o2.getQuantity()));
        }

        //========== 6 =============
        cos.delete(fromDb);
        CoffeeOrder deleted=cos.read(fromDb.getId());
        assertEquals(deleted, null);


    }

}
