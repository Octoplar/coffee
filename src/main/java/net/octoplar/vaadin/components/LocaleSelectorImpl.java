package net.octoplar.vaadin.components;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Octoplar.
 */

@Component
@UIScope
public class LocaleSelectorImpl extends HorizontalLayout implements LocaleSelector {
    @Autowired
    private MessageSource ms;

    private Button eng;
    private Button rus;

    private Set<LocaleChangeListener> listeners;

    private Locale localeRus=new Locale("ru","Russia");
    private Locale localeEng=Locale.ENGLISH;


    public LocaleSelectorImpl() {
        eng=new Button();
        rus=new Button();
        eng.setSizeFull();
        rus.setSizeFull();
        eng.addClickListener(e->onButtonClick(localeEng));
        rus.addClickListener(e->onButtonClick(localeRus));
        this.addComponents(eng,rus);
        this.setExpandRatio(eng,1);
        this.setExpandRatio(rus,1);

        listeners=new HashSet<>();
    }

    //==========================implementation==========================================================================
    @Override
    public void addLocaleChangeListener(LocaleChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLocaleChangeListener(LocaleChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        eng.setCaption(ms.getMessage("english", null, locale));
        rus.setCaption(ms.getMessage("russian", null, locale));
    }
    //==========================private=================================================================================
    private void onButtonClick(Locale locale){
        for(LocaleChangeListener lcl: listeners)
            lcl.callback(locale);
    }


}

