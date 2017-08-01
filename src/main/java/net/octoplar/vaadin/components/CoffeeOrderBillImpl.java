package net.octoplar.vaadin.components;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.util.DetailedOrderCost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Map;

/**
 * Created by Octoplar.
 * This component display bill to user
 */
@org.springframework.stereotype.Component
@UIScope
public class CoffeeOrderBillImpl extends Panel implements CoffeeOrderBill {
    @Autowired
    private MessageSource ms;

    //components
    private Grid<Map.Entry<CoffeeOrderItem, Double>> table;
    private Grid.Column coffeeTypeColumn;
    private Grid.Column quantityColumn;
    private Grid.Column priceColumn;
    private Label deliveryCost;
    private Label orderCost;
    private Label totalCost;

    public CoffeeOrderBillImpl() {

        //item table
        table=new Grid<>();
        coffeeTypeColumn=table.addColumn(i->i.getKey().getCoffeeType().getTypeName());
        quantityColumn=table.addColumn(i->i.getKey().getQuantity());
        priceColumn=table.addColumn(i->i.getValue());


        //cost info
        deliveryCost=new Label();
        orderCost=new Label();
        totalCost=new Label();
        FormLayout costLayout=new FormLayout(deliveryCost, orderCost, totalCost);
        costLayout.setMargin(false);


        Layout content=new VerticalLayout();
        content.addComponents(table, costLayout);

        this.setContent(content);
    }

    //=======================================implementation=============================================================
    @Override
    public void setValue(DetailedOrderCost value) {
        //set table content
        table.setItems(value.getCostMap().entrySet());

        //set total content
        if (value.getCostMap().size()>0){
            deliveryCost.setValue(Double.toString(value.getDeliveryCost()));
            orderCost.setValue(Double.toString(value.getOrderCost()));
            totalCost.setValue(Double.toString(value.getOrderCost()+value.getDeliveryCost()));
        }
        else {
            deliveryCost.setValue("");
            orderCost.setValue("");
            totalCost.setValue("");
        }
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        //table
        coffeeTypeColumn.setCaption(ms.getMessage("coffeeType" ,null, locale));
        quantityColumn.setCaption(ms.getMessage("quantity" ,null, locale));
        priceColumn.setCaption(ms.getMessage("price" ,null, locale));

        //cost info
        deliveryCost.setCaption(ms.getMessage("deliveryCost" ,null, locale));
        orderCost.setCaption(ms.getMessage("orderCost" ,null, locale));
        totalCost.setCaption(ms.getMessage("totalCost" ,null, locale));
    }
}
