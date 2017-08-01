package net.octoplar.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import net.octoplar.vaadin.components.*;
import net.octoplar.vaadin.view.CoffeeOrderAddressView;
import net.octoplar.vaadin.view.CoffeeOrderItemsView;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

/**
 * Created by Octoplar.
 */
@SpringUI(path = "/order_vaadin")
@Theme("mytheme")
public class CoffeeOrderUI extends UI {

    //components
    @Autowired
    private CoffeeOrderItemsView coffeeOrderItemsView;
    @Autowired
    private CoffeeOrderAddressView coffeeOrderAddressView;
    @Autowired
    private LocaleSelector localeSelector;
    private Navigator navigator;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Panel panel=new Panel();
        navigator=new Navigator(this,panel);
        navigator.addView(CoffeeOrderAddressView.NAME, coffeeOrderAddressView);
        navigator.addView(CoffeeOrderItemsView.NAME, coffeeOrderItemsView);
        navigator.navigateTo(CoffeeOrderItemsView.NAME);

        VerticalLayout content=new VerticalLayout(localeSelector, panel);

        localeSelector.addLocaleChangeListener(new LocaleSelector.LocaleChangeListener() {
            @Override
            public void callback(Locale locale) {
                onLocaleChange(locale);
            }
        });
        coffeeOrderAddressView.addNavigationListener(new Navigable.NavigationListener() {
            @Override
            public void callback(String event) {
                onViewChange(event);
            }
        });
        coffeeOrderItemsView.addNavigationListener(new Navigable.NavigationListener() {
            @Override
            public void callback(String event) {
                onViewChange(event);
            }
        });

        this.setContent(content);
        refreshLocale();

    }
    //================private===========================================================================================
    private void onViewChange(String viewName){
        navigator.navigateTo(viewName);
    }

    private void onLocaleChange(Locale locale){
        //save new locale to session
        getSession().setLocale(locale);
        //translate current view (if internationalization is supported)
        View currentView= navigator.getCurrentView();
        if (currentView instanceof International)
            ((International)currentView).setLocale(locale);

        //translate other components
        localeSelector.setLocale(locale);

    }

    private void refreshLocale(){
        localeSelector.setLocale(getSession().getLocale());
    }

}

