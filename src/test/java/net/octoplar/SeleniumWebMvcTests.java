package net.octoplar;

import net.octoplar.backend.util.NameAndAddress;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Created by Octoplar.
 * Simple Selenium test:
 * 1.enter order
 * 2.read saved order
 * 3. compare orders
 */


public class SeleniumWebMvcTests extends SeleniumAbstract{

    public static final String URL_WEBMVC="http://localhost:8080//order_webmvc";

    private WebDriverWait myWait;

    @Test
    public void makeOrderWebMvc() throws InterruptedException {

        driver.get(URL_WEBMVC);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        //Create explicit wait.
        myWait = new WebDriverWait(driver, 10);

        //======================================================================================
        //get list of coffee type names
        List<String> menu=getMenu();

        //build order. distinct names.
        Map<String, String> order=new HashMap<>(menu.size()*2);
        for (int i = 0; i < menu.size(); i++) {
            //max quantity==100, min quantity=1
            order.put(menu.get(i), Integer.toString(i%100+1));
        }
        //fill order fields and press order button
        enterOder(order);

        //create name and address
        NameAndAddress nameAndAddress=new NameAndAddress("$name","$address");

        //input name and address
        inputNameAndAddress(nameAndAddress);

        //click link
        clickLink();

        //read registered order
        Map<String, String> registeredOrder=parseOrderInfo();

        //compare orders
        assertTrue("orders not equals: "+order +"<<<>>>"+registeredOrder
                , compareOrders(order, registeredOrder));


    }

    //coffee menu must be opened
    private List<String> getMenu(){
        //find table
        myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("coffeeTable")));
        WebElement coffeeTable=driver.findElement(By.id("coffeeTable"));


        List<WebElement> nameCells=coffeeTable.findElements(By.name("typeName"));
        List<String> nameList=new ArrayList<>(nameCells.size());
        for(WebElement e:nameCells)
            nameList.add(e.getText());
        return nameList;

    }

    //coffee menu must be opened
    private void enterOder(Map<String, String> order) throws InterruptedException {
        //find table
        myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("coffeeTable")));
        WebElement coffeeTable=driver.findElement(By.id("coffeeTable"));


        List<WebElement> rows=coffeeTable.findElements(By.tagName("tr"));

        //iterate rows and input order
        //skip first and last row: header+footed
        for (int i = 1; i < rows.size()-1; i++) {
            WebElement row=rows.get(i);
            String typeName= row.findElements(By.name("typeName")).get(0).getText();
            if (order.containsKey(typeName))
                orderPoint(row, order.get(typeName));
        }
        //find button
        myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orderButton")));
        WebElement orderButton=driver.findElement(By.id("orderButton"));

        //press button
        Thread.sleep(100);
        orderButton.click();

    }

    //coffee menu must be opened
    private void orderPoint(WebElement row, String quantity) throws InterruptedException {
        Thread.sleep(100);
        List<WebElement> inputs=row.findElements(By.cssSelector("input"));
        WebElement checkBox=inputs.get(0);
        WebElement textField=inputs.get(1);
        checkBox.click();
        Thread.sleep(100);
        textField.sendKeys(quantity);
    }

    //address input page must be opened
    private void inputNameAndAddress(NameAndAddress nameAndAddress){
        //find name field
        myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement nameField=driver.findElement(By.id("name"));
        nameField.sendKeys(nameAndAddress.getName());

        //find address field
        myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("address")));
        WebElement addressField=driver.findElement(By.id("address"));
        addressField.sendKeys(nameAndAddress.getAddress());

        //find orderButton
        myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orderButton")));
        WebElement orderButton=driver.findElement(By.id("orderButton"));
        orderButton.click();
    }

    //thank user page must be opened
    private void clickLink(){
        myWait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("")));
        WebElement link=driver.findElement(By.partialLinkText(""));
        link.click();
    }

    //order info page must be opened
    private Map<String, String> parseOrderInfo(){
        myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("infoTable")));
        WebElement table=driver.findElement(By.id("infoTable"));

        List<WebElement> rows=table.findElements(By.tagName("tr"));

        Map<String, String> result=new HashMap<>();

        //iterate rows
        //skip first row: header
        for (int i = 1; i < rows.size(); i++) {
            WebElement row=rows.get(i);
            String typeName= row.findElements(By.name("typeName")).get(0).getText();
            String quantity= row.findElements(By.name("quantity")).get(0).getText();
            result.put(typeName, quantity);
        }
        return result;
    }

    //compare map content
    private boolean compareOrders(Map<String, String> f, Map<String, String> s){
        if (f.size()!=s.size())
            return false;
        for(Map.Entry<String, String> e:f.entrySet()){
            String q2=s.get(e.getKey());
            if (q2==null || !q2.equals(e.getValue()))
                return false;
        }
        return true;

    }



}
