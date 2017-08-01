package net.octoplar.vaadin.components;

import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.entity.CoffeeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Octoplar.
 * Component to build CoffeeOrderItem instances.
 * Every time user build new instance it passed through all registered listeners
 */
@org.springframework.stereotype.Component
@UIScope
public class CoffeeOrderItemSelectorImpl extends VerticalLayout implements CoffeeOrderItemSelector{

    @Autowired
    private MessageSource ms;

    //listeners
    private Set<ItemSelectListener> listeners=new HashSet<>();

    private Panel table;
    //shared popup content
    private PopupContent popupContent;





    public CoffeeOrderItemSelectorImpl() {
        //popupContent config
        popupContent=new PopupContent();

        table=new Panel();
        table.setSizeFull();
        addComponent(table);

    }


    //=======================================implementation=============================================================
    @Override
    public void setCoffeeTypes(Set<CoffeeType> set){
        VerticalLayout content=new VerticalLayout();
        for(CoffeeType i:set){
            content.addComponent(new ItemRow(i));
        }
        content.setSizeUndefined();
        content.addStyleName("cc-selector");
        table.setContent(content);
    }

    @Override
    public void addItemSelectListener(ItemSelectListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeItemSelectListener(ItemSelectListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        popupContent.setLanguage(locale);
        VerticalLayout content= (VerticalLayout) table.getContent();
        for (int i = 0; i < content.getComponentCount(); i++) {
            ((ItemRow) content.getComponent(i)).setLanguage(locale);
        }
    }
    //==========================================privates================================================================
    //fires all events with shared CoffeeOrderItem instance
    private void fireSelectEvent(CoffeeOrderItem item){
        for(ItemSelectListener l: listeners)
            l.callback(item);
    }

    //=========================================inner====================================================================


    //item row component
    private class ItemRow extends HorizontalLayout{
        //========components================
        private Label name;
        private Label price;
        private Button order;
        private PopupView popup;

        private CoffeeType item;

        private ItemRow(CoffeeType item) {
            this.item=item;

            name=new Label(item.getTypeName());
            price =new Label(item.getPrice().toString());
            order=new Button();
            order.addClickListener(e->onButtonClick());

            //popup config
            popup=new PopupView(null, popupContent);
            popup.setHideOnMouseOut(false);
            popup.setPopupVisible(false);




            //size
            this.setSizeUndefined();
            this.addComponents(name, price, order, popup);
            this.setExpandRatio(name, 70);
            this.setExpandRatio(price, 10);
            this.setExpandRatio(order, 20);
            //this.setWidth("400px");
            name.setWidth("200px");
            //styles
            this.addStyleName("cc-order-item");
            name.addStyleName("cc-wrap-text");
            name.addStyleName("cc-order-item-name");
            price.addStyleName("cc-order-item-price");
            order.addStyleName("cc-button-zoom");
        }

        private void setLanguage(Locale locale){
            order.setCaption(ms.getMessage("order" ,null, locale));
        }

        //shows pop-up to select quantity or cancel
        private void onButtonClick(){
            popupContent.setCaption(item.getTypeName());
            popupContent.setSelectedType(item, popup);
            popupContent.selectQuantityField.focus();
            popup.setPopupVisible(true);
        }
    }

    //shared content
    private class PopupContent extends Panel{
        private Label selectQuantityMessage;
        private TextField selectQuantityField;
        private Button selectQuantityOk;
        private Button selectQuantityCancel;
        private CoffeeType selectedType;
        private PopupView owner;

        private PopupContent() {
            selectQuantityMessage=new Label();
            selectQuantityField=new TextField();
            selectQuantityField.setMaxLength(4);
            selectQuantityField.setValueChangeMode(ValueChangeMode.LAZY);
            selectQuantityField.addValueChangeListener(this::selectQuantityFieldOnValueChange);

            selectQuantityOk=new Button();
            selectQuantityOk.setClickShortcut(ShortcutAction.KeyCode.ENTER);
            selectQuantityOk.setStyleName(ValoTheme.BUTTON_PRIMARY);
            selectQuantityOk.addClickListener(e->popupOkClick());
            selectQuantityCancel=new Button();
            selectQuantityCancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
            selectQuantityCancel.setStyleName(ValoTheme.BUTTON_DANGER);
            selectQuantityCancel.addClickListener(e->popupCancelClick());
            Layout buttons=new HorizontalLayout(selectQuantityOk, selectQuantityCancel);
            Layout content=new VerticalLayout(selectQuantityMessage, selectQuantityField, buttons);
            content.setSizeUndefined();
            this.setContent(content);
        }

        private void setSelectedType(CoffeeType selectedType, PopupView owner) {
            this.selectedType = selectedType;
            this.owner=owner;

            //clear old input
            selectQuantityField.setValue("");
            setError(null);
        }

        private void popupOkClick() {
            String value=selectQuantityField.getValue();
            String validationResult=validateQuantity(value);
            if (validationResult!=null){
                setError(validationResult);
                return;
            }
            else
                fireSelectEvent(new CoffeeOrderItem(selectedType, new Integer(value)));
            owner.setPopupVisible(false);
        }
        private void popupCancelClick() {
            owner.setPopupVisible(false);
        }
        //returns error message or null if all ok
        private String validateQuantity(String value){
            Locale locale=UI.getCurrent().getSession().getLocale();
            try{
                int q=Integer.parseInt(value);
                if (q<1)
                    return ms.getMessage("error.quantityUnderflow", null, locale);
                if (q>100)
                    return ms.getMessage("error.quantityOverflow", null, locale);
                return null;
            }
            catch (Exception e){
                return ms.getMessage("error", null, locale);
            }
        }

        private void selectQuantityFieldOnValueChange(HasValue.ValueChangeEvent<String> e){

            if (e.isUserOriginated()){
                String value=e.getValue();
                String validationResult=validateQuantity(value);
                if (validationResult!=null)
                    setError(validationResult);
                else
                    setError(null);
            }
            else
                setError(null);
        }

        //change visible text content
        private void setLanguage(Locale locale){
            selectQuantityMessage.setValue(ms.getMessage("selector.popup.label", null, locale));
            selectQuantityField.setPlaceholder(ms.getMessage("selector.popup.fieldPlaceholder", null, locale));
            selectQuantityOk.setCaption(ms.getMessage("ok", null, locale));
            selectQuantityCancel.setCaption(ms.getMessage("cancel", null, locale));
        }

        private void setError(String message){
            if (message!=null){
                selectQuantityField.setComponentError(new ErrorMessage() {
                    @Override
                    public ErrorMessage.ErrorLevel getErrorLevel() {
                        return null;
                    }

                    @Override
                    public String getFormattedHtmlMessage() {
                        return message;
                    }
                });
            }
            else
                //clear error
                selectQuantityField.setComponentError(null);

        }
    }




}

