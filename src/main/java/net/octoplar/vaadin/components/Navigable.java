package net.octoplar.vaadin.components;

/**
 * Created by Octoplar.
 */
public interface Navigable {

    void addNavigationListener(NavigationListener listener);
    void removeNavigationListener(NavigationListener listener);
    abstract class NavigationListener{
        public abstract void callback(String event);
    }


}
