package net.octoplar.vaadin.components;

import com.vaadin.ui.Component;

import java.util.Locale;

/**
 * Created by Octoplar.
 */
public interface LocaleSelector extends Component, International{
    void addLocaleChangeListener( LocaleChangeListener listener);
    void removeLocaleChangeListener( LocaleChangeListener listener);
    abstract class LocaleChangeListener{
        public abstract void callback(Locale locale);
    }
}
