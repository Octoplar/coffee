package net.octoplar.vaadin.components;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;
import net.octoplar.backend.entity.CoffeeOrder;
import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.entity.CoffeeType;
import net.octoplar.backend.service.CostCalculationService;
import net.octoplar.backend.util.DetailedOrderCost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Octoplar.
 * Manageable CoffeeOrderItem container.
 * This implementation shows current cost of all items and allows drop any item
 */
@Component
@UIScope
public class CoffeeOrderItemManagerImpl extends Panel implements CoffeeOrderItemManager {
    @Autowired
    private MessageSource ms;
    @Autowired
    private CostCalculationService ccs;

    //managed items
    private Map<CoffeeType, CoffeeOrderItem> items;

    //components
    private Grid<Map.Entry<CoffeeOrderItem, Double>> table;
    private Grid.Column coffeeTypeColumn;
    private Grid.Column quantityColumn;
    private Grid.Column priceColumn;
    private Grid.Column deleteColumn;
    private Label deliveryCost;
    private Label orderCost;
    private Label totalCost;



    public CoffeeOrderItemManagerImpl() {
        items=new HashMap<>();

        //item table
        table=new Grid<>();
        coffeeTypeColumn=table.addColumn(i->i.getKey().getCoffeeType().getTypeName());
        quantityColumn=table.addColumn(i->i.getKey().getQuantity());
        priceColumn=table.addColumn(i->i.getValue());
        deleteColumn=table.addColumn(i->"delete", new ButtonRenderer<>(new ClickableRenderer.RendererClickListener<Map.Entry<CoffeeOrderItem, Double>>() {
            @Override
            public void click(ClickableRenderer.RendererClickEvent<Map.Entry<CoffeeOrderItem, Double>> e) {
                dropItem(e.getItem().getKey());
                refresh();
            }
        }))
                .setId("idDelete");


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

    //====================implementation================================================================================
    @Override
    public void addItem(CoffeeOrderItem item) {
        put(item);
        refresh();
    }

    @Override
    public void addItems(Collection<CoffeeOrderItem> list) {
        for(CoffeeOrderItem coi: list)
            put(coi);
        refresh();
    }

    @Override
    public void clear() {
        items.clear();
        refresh();
    }

    @Override
    public Collection<CoffeeOrderItem> getItems() {
        return items.values();
    }

    @Override
    public void setLocale(Locale locale){
        super.setLocale(locale);
        //table
        coffeeTypeColumn.setCaption(ms.getMessage("coffeeType" ,null, locale));
        quantityColumn.setCaption(ms.getMessage("quantity" ,null, locale));
        priceColumn.setCaption(ms.getMessage("price" ,null, locale));
        table.removeColumn("idDelete");
        String deleteButtonCaption=ms.getMessage("delete" ,null, locale);
        table.addColumn(i->deleteButtonCaption, new ButtonRenderer<>(new ClickableRenderer.RendererClickListener<Map.Entry<CoffeeOrderItem, Double>>() {
            @Override
            public void click(ClickableRenderer.RendererClickEvent<Map.Entry<CoffeeOrderItem, Double>> e) {
                dropItem(e.getItem().getKey());
                refresh();
            }
        }))
                .setCaption(ms.getMessage("delete" ,null, locale))
                .setId("idDelete");

        //cost info
        deliveryCost.setCaption(ms.getMessage("deliveryCost" ,null, locale));
        orderCost.setCaption(ms.getMessage("orderCost" ,null, locale));
        totalCost.setCaption(ms.getMessage("totalCost" ,null, locale));
    }
    //==================private=========================================================================================



    //add item to map, merge copies of same item
    private void put(CoffeeOrderItem item){
        //check quantity
        if (item.getQuantity()<1){
            Locale locale=UI.getCurrent().getSession().getLocale();
            Notification.show(ms.getMessage("error" ,null, locale),
                    ms.getMessage("error.quantityUnderflow" ,null, locale),
                    Notification.Type.ERROR_MESSAGE);
            return;
        }
        if (item.getQuantity()>100){
            Locale locale=UI.getCurrent().getSession().getLocale();
            Notification.show(ms.getMessage("error" ,null, locale),
                    ms.getMessage("error.quantityOverflow" ,null, locale),
                    Notification.Type.ERROR_MESSAGE);
            return;
        }

        CoffeeOrderItem coi=items.get(item.getCoffeeType());
        if (coi==null)
            items.put(item.getCoffeeType(), item);
        else {
            //check result quantity
            int newQuantity=coi.getQuantity() + item.getQuantity();
            if (isCorrectQuantity(newQuantity))
                coi.setQuantity(newQuantity);
            else {
                Locale locale=UI.getCurrent().getSession().getLocale();
                Notification.show(ms.getMessage("error" ,null, locale),
                        ms.getMessage("error.quantityOverflow" ,null, locale),
                        Notification.Type.ERROR_MESSAGE);
            }

        }
    }
    //refresh grid content
    private void refresh(){
        //build order to calculate detailed cost
        CoffeeOrder order=new CoffeeOrder();
        order.setItems(new LinkedHashSet<>(items.values()));
        DetailedOrderCost cost=ccs.calculateDetailedOrderCost(order);
        //set table content
        table.setItems(cost.getCostMap().entrySet());

        //set total content
        if (items.size()>0){
            deliveryCost.setValue(Double.toString(cost.getDeliveryCost()));
            orderCost.setValue(Double.toString(cost.getOrderCost()));
            totalCost.setValue(Double.toString(cost.getOrderCost()+cost.getDeliveryCost()));
        }
        else {
            deliveryCost.setValue("");
            orderCost.setValue("");
            totalCost.setValue("");
        }

    }
    //remove item
    private void dropItem(CoffeeOrderItem i){
        items.remove(i.getCoffeeType());
    }

    //quantity validation
    private boolean isCorrectQuantity(int q){
        return q>0 && q<=100;
    }

}

