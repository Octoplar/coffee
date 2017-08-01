package net.octoplar;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;

/**
 * Created by Octoplar.
 */
//basic class for selenium tests
public abstract class SeleniumAbstract {
    public static final String OPERA_DRIVER="d:\\PROG\\Java\\install\\operadriver_win64\\operadriver.exe";
    public static final String CHROME_DRIVER="d:\\PROG\\Java\\install\\chromedriver_win32\\ochromedriver.exe";
    protected WebDriver driver;


    public SeleniumAbstract() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER);
        System.setProperty("webdriver.opera.driver", OPERA_DRIVER);
    }


    @Before
    public void init(){
        driver=new OperaDriver();
    }

    @After
    public void destroy() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}

