package utility;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Browser {

    private static WebDriver driver;
    private static boolean isFirefox;


    public static void open() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    public static void open(String browserType) {
        if(browserType.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }
        else if(browserType.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            isFirefox = true;
        }
    }

    public static WebDriver getDriver() {
        if(driver == null) {
            StringBuilder strb = new StringBuilder();
            strb.append("\n\nException Message : \n");
            strb.append("\tWebDriver is not initiated, please instantiate WebDriver by\n");
            strb.append("\tcalling openBrowser() method. \n\n");
            strb.append("Exception Location:  Class  -> UIActions\n");
            strb.append("Exception Occured :  Method -> getDriver()\n");
            throw new NullPointerException(strb.toString());
        }
        return driver;
    }

    public static void close() {
        if(isFirefox) {
            if(driver != null) {
                driver.quit();
                return;
            }
            return;
        }

        // Othewise, it is chrome
        // just close the driver normally
        if(driver != null){
            driver.close();
            driver.quit();
        }
    }
}