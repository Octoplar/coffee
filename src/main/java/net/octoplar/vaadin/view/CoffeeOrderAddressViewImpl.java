package net.octoplar.vaadin.view;

import com.vaadin.data.Binder;
import com.vaadin.data.ErrorMessageProvider;
import com.vaadin.data.ValueContext;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import net.octoplar.backend.entity.CoffeeOrder;
import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.service.CoffeeOrderService;
import net.octoplar.backend.service.CostCalculationService;
import net.octoplar.backend.util.OrderCost;
import net.octoplar.backend.validation.CoffeeOrderFinalValidator;
import net.octoplar.vaadin.components.CoffeeOrderBill;
import net.octoplar.vaadin.components.Navigable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.*;

/**
 * Created by Octoplar.
 * Component to build second part of order (collection of CoffeeOrderItems)
 */
@org.springframework.stereotype.Component
@UIScope
public class CoffeeOrderAddressViewImpl extends VerticalLayout implements CoffeeOrderAddressView {
    //service
    private final MessageSource ms;
    private final CoffeeOrderService coffeeOrderService;
    private final CostCalculationService costCalculationService;
    private final CoffeeOrderFinalValidator coffeeOrderFinalValidator;


    //components
    private final CoffeeOrderBill coffeeOrderBill;
    private TextField address;
    private TextField name;
    private Button orderButton;
    private Button backButton;
    private Binder<CoffeeOrder> binder;


    private Set<Navigable.NavigationListener> listeners=new HashSet<>();
    private Locale currentLocale;





    @Autowired
    public CoffeeOrderAddressViewImpl(MessageSource ms,
                                      CoffeeOrderService cos,
                                      CostCalculationService ccs,
                                      CoffeeOrderBill coffeeOrderBill,
                                      CoffeeOrderFinalValidator coffeeOrderFinalValidator) {
        this.ms = ms;
        this.coffeeOrderService = cos;
        this.costCalculationService = ccs;
        this.coffeeOrderBill = coffeeOrderBill;
        this.coffeeOrderFinalValidator = coffeeOrderFinalValidator;

        //components config
        address=new TextField();
        address.setMaxLength(200);
        name=new TextField();
        name.setMaxLength(100);
        orderButton=new Button();
        orderButton.addClickListener(e->onOrderButtonClick());
        backButton=new Button();
        backButton.addClickListener(e->onBackButtonClick());

        //binder config
        binder=new Binder<>();
        binder.forField(address).asRequired(new ErrorMessageProvider() {
            @Override
            public String apply(ValueContext valueContext) {
                Locale locale=UI.getCurrent().getSession().getLocale();
                return ms.getMessage("address.required", null, locale);
            }
        })
                .withValidator(i -> i.length() <= 200, new ErrorMessageProvider() {
                    @Override
                    public String apply(ValueContext valueContext) {
                        Locale locale=UI.getCurrent().getSession().getLocale();
                        return ms.getMessage("error.address.long", null, locale);
                    }
                })
                .bind(CoffeeOrder::getAddress, CoffeeOrder::setAddress);

        binder.forField(name)
                .withValidator(i -> i==null || i.length() <= 100, new ErrorMessageProvider() {
                    @Override
                    public String apply(ValueContext valueContext) {
                        Locale locale=UI.getCurrent().getSession().getLocale();
                        return ms.getMessage("error.name.long", null, locale);
                    }
                })
                .bind(CoffeeOrder::getName, CoffeeOrder::setName);


        VerticalLayout addressLayout=new VerticalLayout(address, name, orderButton, backButton);
        HorizontalLayout content=new HorizontalLayout(addressLayout, coffeeOrderBill);

        this.addComponent(content);

    }

    //===========================implementation=========================================================================
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //create new CoffeeOrder instance
        CoffeeOrder coffeeOrder=new CoffeeOrder();

        //read parameters from session
        Collection<CoffeeOrderItem> items=(Collection<CoffeeOrderItem>) UI.getCurrent().getSession().getAttribute(CoffeeOrderItemsViewImpl.COFFEE_ORDER_ITEMS);
        //redirect to error page if items is null or empty
        if (items==null || items.size()==0){
            getUI().getPage().setLocation("/error");
            return;
        }
        // set items to order instance if all ok
        coffeeOrder.setItems(new LinkedHashSet<>(items));

        //set bill value
        coffeeOrderBill.setValue(costCalculationService.calculateDetailedOrderCost(coffeeOrder));

        //synchronize view locale with session
        Locale locale=UI.getCurrent().getSession().getLocale();
        refreshLocale(locale);
        //bind instance properties to view fields
        binder.setBean(coffeeOrder);
    }

    @Override
    public void addNavigationListener(Navigable.NavigationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeNavigationListener(Navigable.NavigationListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        refreshLocale(locale);
    }
    //======================================private=====================================================================

    private void refreshLocale(Locale locale){
        //if view already has the required locale then skip translate
        if (locale.equals(currentLocale))
            return;
        //else save new locale as current and translate
        currentLocale=locale;
        address.setPlaceholder(ms.getMessage("placeholder.address", null, locale));
        address.setCaption(ms.getMessage("caption.address", null, locale));
        name.setPlaceholder(ms.getMessage("placeholder.name", null, locale));
        name.setCaption(ms.getMessage("caption.name", null, locale));
        orderButton.setCaption(ms.getMessage("order", null, locale));
        backButton.setCaption(ms.getMessage("back", null, locale));
        coffeeOrderBill.setLocale(locale);
    }

    private void fireEvents(String viewName){
        for (Navigable.NavigationListener nl:listeners)
            nl.callback(viewName);
    }

    private void onOrderButtonClick(){
        //force field validation
        binder.validate();
        if(binder.isValid()){
            CoffeeOrder coffeeOrder=binder.getBean();
            //set date and cost
            coffeeOrder.setDate(new Date());
            OrderCost orderCost= costCalculationService.calculateOrderCost(coffeeOrder);
            coffeeOrder.setCost(orderCost.getOrderCost()+orderCost.getDeliveryCost());

            //validation before save
            if(coffeeOrderFinalValidator.isValid(coffeeOrder)){
                //save
                coffeeOrderService.create(coffeeOrder);
                Integer id=coffeeOrder.getId();
                //redirect
                String url="/orderInfo/"+id;
                getUI().getPage().setLocation(url);
            }
            else {
                //redirect to error page because current content out of date
                String url="/error";
                getUI().getPage().setLocation(url);
            }
            //close session
            getUI().getSession().close();
        }
        else{
            Locale locale=UI.getCurrent().getSession().getLocale();
            Notification.show(ms.getMessage("order.errorMessage", null, locale));
        }

    }

    private void onBackButtonClick(){
        fireEvents(CoffeeOrderItemsView.NAME);
    }


}

