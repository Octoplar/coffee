package net.octoplar.vaadin.view;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.service.CoffeeTypeService;
import net.octoplar.vaadin.components.CoffeeOrderItemManager;
import net.octoplar.vaadin.components.CoffeeOrderItemSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Octoplar.
 * Component to build first part of order (build collection of order items + validation of items)
 * if user ends to choose items, items will be put into session as parameter and navigation event
 * will be generated(all registered listeners will be notified with event name as argument).
 *
 */
@Component
@UIScope
public class CoffeeOrderItemsViewImpl extends HorizontalLayout implements CoffeeOrderItemsView {
    public static final String COFFEE_ORDER_ITEMS="COFFEE_ORDER_ITEMS";

    private final CoffeeTypeService coffeeTypeService;

    @Autowired
    private MessageSource ms;


    //components
    private final CoffeeOrderItemSelector cois;
    private final CoffeeOrderItemManager coim;
    private Button makeOrderButton;


    private Set<NavigationListener> listeners=new HashSet<>();

    @Autowired
    public CoffeeOrderItemsViewImpl(CoffeeOrderItemManager coim, CoffeeOrderItemSelector cois, CoffeeTypeService coffeeTypeService) {

        this.coim = coim;
        this.cois = cois;

        this.coffeeTypeService = coffeeTypeService;
        cois.setSizeUndefined();
        cois.setHeight("600px");

        coim.setSizeUndefined();
        coim.setHeight("600px");
        makeOrderButton =new Button();
        makeOrderButton.setSizeFull();
        makeOrderButton.setHeight("50px");
        VerticalLayout itemManager=new VerticalLayout(coim,makeOrderButton);



        cois.setCoffeeTypes(new LinkedHashSet<>(coffeeTypeService.readAvailable()));
        cois.addItemSelectListener(new CoffeeOrderItemSelector.ItemSelectListener() {
            @Override
            public void callback(CoffeeOrderItem selectedItem) {
                coim.addItem(selectedItem);
            }
        });
        makeOrderButton.addClickListener(e->onMakeOrderButtonClick());

        this.addComponents(cois, itemManager);

        this.setSizeFull();
        this.setExpandRatio(cois, 6);
        this.setExpandRatio(itemManager, 4);
    }

    //========================implementation============================================================================
    @Override
    public void addNavigationListener(NavigationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeNavigationListener(NavigationListener listener) {
        listeners.remove(listener);
    }

    //receive locale from session
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        refreshLocale(UI.getCurrent().getSession().getLocale());
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        refreshLocale(locale);
    }
    //========================private============================================================================

    private void refreshLocale(Locale locale){
        cois.setLocale(locale);
        coim.setLocale(locale);
        makeOrderButton.setCaption(ms.getMessage("order", null, locale));
    }

    private void fireEvents(String eventName){
        for (NavigationListener nl:listeners)
            nl.callback(eventName);
    }

    //pass items through session
    private void onMakeOrderButtonClick(){

        if (coim.getItems().size()>0)
        {
            //put parameters into session and fire navigation event
            UI.getCurrent().getSession().setAttribute(COFFEE_ORDER_ITEMS, coim.getItems());
            fireEvents(CoffeeOrderAddressView.NAME);
        }
        else {
            //no items -> show notification
            Locale locale=UI.getCurrent().getSession().getLocale();
            Notification.show(ms.getMessage("error" ,null, locale),
                    ms.getMessage("error.noItems" ,null, locale),
                    Notification.Type.HUMANIZED_MESSAGE);
        }

    }


}
