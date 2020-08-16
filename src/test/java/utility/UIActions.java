package utility;


import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class UIActions {


    private static final Integer WAIT_TIME = 30;
    public static WebDriver driver;
    private static WebDriverWait wait;
    private static ArrayList<String> tabs;
    private static StandByUntil standByUntil;


    public UIActions() {
        driver = Browser.getDriver();
        wait = new WebDriverWait(driver, WAIT_TIME);
        standByUntil = new StandByUntil(wait);
    }

    public void fullScreen() {
        driver.manage().window().fullscreen();
    }

    public void maximizeWindow() {
        driver.manage().window().maximize();
    }


    public void switchToIFrame() {
        WebElement iframe = waitUntilElementVisible(By.tagName("iframe"));
        driver = driver.switchTo().frame(iframe);
    }


    public void switchToIFrame(By locator) {
        WebElement iframe = waitUntilElementVisible(locator);
        driver = driver.switchTo().frame(iframe);
    }


    public void switchBackFromIframe() {
        driver = driver.switchTo().defaultContent();
    }


    public void openNewTab() {
        ((JavascriptExecutor) driver).executeScript("window.open()");
        tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));
    }

    public void switchToTab(int tabNumber) {
        tabs = new ArrayList<String>(driver.getWindowHandles());
        for (String s : tabs) {
            System.out.println("Tab: " + s + "     title:" + driver.getTitle());
        }
        System.out.println("\n\n");
        driver.switchTo().window(tabs.get(tabNumber));
    }

    public void switchToTab(String tabTitle) {
        tabs = new ArrayList<String>(driver.getWindowHandles());
        for (int i = 0; i < tabs.size(); i++) {
            driver.switchTo().window(tabs.get(i));
            String windowTitle = driver.getTitle();
            if (windowTitle.equalsIgnoreCase(tabTitle)) {
                break;
            }
        }
    }


    public void closeTab() {
        driver.close();
        tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));
    }


    public void loadCookie(String name, String token) {
        Cookie cookie = new Cookie(name, token);
        driver.manage().addCookie(cookie);
    }


    public void deleteCookie(String name) {
        driver.manage().deleteCookieNamed(name);
    }


    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }
    //endregion


    //region Page Actions
    public void gotoSite(String url) {
        driver.get(url);
    }


    public void reload() {
        driver.navigate().refresh();
    }


    public void goBack() {
        driver.navigate().back();
    }


    public void goFoward() {
        driver.navigate().forward();
    }


    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }


    public String title() {
        return driver.getTitle();
    }


    public void click(By locator) {
        try {
            highlight(locator);
            WebElement element = standByUntil.elementIsClickable(locator);
            element.click();
        } catch (Exception ex) {
            System.out.println("Element was not clickalbe. Check its locators logic ( Ex: css, xpath .etc");
        }
    }

    public void tapOnce(By locator) {
        try {
            WebElement element = standByUntil.elementIsThereAndVisibleToUser(locator);
            element.click();
        } catch (Exception ex) {
            System.out.println("Element was not clickalbe. Check its locators logic ( Ex: css, xpath .etc");
        }
    }


    public void click(String text) {
        String expression = "//*[text()='" + text + "']";
        By selector = By.xpath(expression);
        waitUntilElementVisible(selector);
        List<WebElement> foundElems = driver.findElements(selector);
        if (foundElems.size() == 0) {
            StringBuilder strb = new StringBuilder();
            strb.append("\n\nException Message : \n");
            strb.append("\tUnable to find the element with text{ +" + text + " } from UI");
            strb.append("\tAny typo? Also text matching is case sensitive, if you cannot \n");
            strb.append("\tresolve this, please use another locators to locate to element. \n");
            strb.append("Exception Location:  Class  -> UIActions\n");
            strb.append("Exception Occured :  Method -> click(String text)\n");
            throw new NotFoundException(strb.toString());
        } else {
            for (WebElement elem : foundElems) {
                elem.click();
                break;
            }
        }
    }


    public void doubleClick(By locator) {
        new Actions(driver)
                .doubleClick(standByUntil.elementIsClickable(locator))
                .build()
                .perform();
    }

    public void rightClick(By locator) {
        new Actions(driver)
                .contextClick(standByUntil.elementIsClickable(locator))
                .build()
                .perform();
    }

    public void hover(By locator) {
        WebElement elem = waitUntilElementVisible(locator);
        new Actions(driver).moveToElement(elem).build().perform();
    }


    public void focus(By locator) {
        WebElement element = waitUntilElementVisible(locator);
        if ("input".equals(element.getTagName())) {
            element.sendKeys("");
        } else {
            new Actions(driver).moveToElement(element).perform();
        }
    }


    public void mouseDownOn(By element) {
        new Actions(driver)
                .moveToElement(waitUntilElementVisible(element))
                .clickAndHold().perform();
    }


    public void moveTo(By element) {
        new Actions(driver).moveToElement(waitUntilElementVisible(element))
                .build()
                .perform();
    }


    public void mouseUpOn(By element) {
        new Actions(driver).moveToElement(waitUntilElementVisible(element))
                .release()
                .perform();
    }


    public void dragAndDrop(By base, By target) {
        mouseDownOn(base);
        moveTo(target);
        mouseUpOn(target);
    }


    public void moveViewToElement(By selector) {
        WebElement where;
        Actions builder = new Actions(driver);
        where = waitUntilElementVisible(selector);
        builder.moveToElement(where).perform();
    }


    public void scrollToBottom() {
        String jscode = "window.scrollTo(0, document.body.scrollHeight)";
        ((JavascriptExecutor) driver).executeScript(jscode);
    }


    public void scrollToTop() {
        String jscode = "window.scrollTo(0, 0)";
        ((JavascriptExecutor) driver).executeScript(jscode);
    }


    public void scrollDownByPixel(int pixelnum) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0," + pixelnum + ")");
    }


    public void scrollUpByPixel(int pixelnum) {
        pixelnum = pixelnum - (pixelnum * 2);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0," + pixelnum + ")");
    }


    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }


    public void highlight(By locator) {
        WebElement element = waitUntilElementVisible(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'border: 3px solid red;');", element);
    }


    public void textHighlight(By locator) {
        WebElement element = waitUntilElementVisible(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
    }

    public void clear(By locator) {
        waitUntilElementVisible(locator).clear();
    }

    public void write(By locator, String text) {
        highlight(locator);
        standByUntil.elementIsThereAndVisibleToUser(locator).sendKeys(text);
    }

    public void clearThenWrite(By locator, String text) {
        WebElement inputElem = waitUntilElementVisible(locator);
        inputElem.clear();
        inputElem.sendKeys(text);
    }

    public void waitfor(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (Exception e) {
            // do nothing
        }
    }


    public void submit(By element) {
        waitUntilElementVisible(element).submit();
    }


    public WebElement element(By locator) {
        // WebElement elem = waitUntilElementVisible(locator);
        highlight(locator);
        WebElement element = standByUntil.elementIsThereAndVisibleToUser(locator);
        return element;
    }


    public List<WebElement> listOfElements(By locator) {
        waitUntilElementVisible(locator);
        return driver.findElements(locator);
    }
    //endregion


    //region Selectors
    public By css(String expression) {
        return By.cssSelector(expression);
    }


    public By id(String expression) {
        return By.id(expression);
    }


    public By xpath(String expression) {
        return By.xpath(expression);
    }


    public By link(String expression) {
        return By.linkText(expression);
    }


    public By linktextContains(String expression) {
        return By.partialLinkText(expression);
    }


    public By name(String expression) {
        return By.name(expression);
    }


    public By withTag(String expression) {
        return By.tagName(expression);
    }
    //endregion


    //region Waiters
    private WebElement waitUntilElementVisible(By locator) {
        WebElement elem = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return elem;
    }


    public boolean waitUntilElementInvisible(By locator) {
        highlight(locator);
        standByUntil.elementIsInvisible(locator);
        return false;
    }


    public boolean isElementDisplayed() {
        return false;
    }
    //endregion


    //region Private Helper Methods
    private void click_NthElement(By element, int index) {
        waitUntilElementVisible(element);
        List<WebElement> elementAllElements = driver.findElements(element);
        WebElement elementElementByIdx = elementAllElements.get(index);
        elementElementByIdx.click();
    }
    //endregion


    // File Actions
    //    DropFile(new File("C:\\Downloads\\image.png"), droparea, 0, 0);
    public void DropFile(File filePath, By uploadElem) {
        WebElement target = element(uploadElem);

        if (!filePath.exists())
            throw new WebDriverException("File not found: " + filePath.toString());

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, 30);

        String JS_DROP_FILE =
                "var target = arguments[0]," +
                        "    offsetX = arguments[1]," +
                        "    offsetY = arguments[2]," +
                        "    document = target.ownerDocument || document," +
                        "    window = document.defaultView || window;" +
                        "" +
                        "var input = document.createElement('INPUT');" +
                        "input.type = 'file';" +
                        "input.style.display = 'none';" +
                        "input.onchange = function () {" +
                        "  var rect = target.getBoundingClientRect()," +
                        "      x = rect.left + (offsetX || (rect.width >> 1))," +
                        "      y = rect.top + (offsetY || (rect.height >> 1))," +
                        "      dataTransfer = { files: this.files };" +
                        "" +
                        "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {" +
                        "    var evt = document.createEvent('MouseEvent');" +
                        "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);" +
                        "    evt.dataTransfer = dataTransfer;" +
                        "    target.dispatchEvent(evt);" +
                        "  });" +
                        "" +
                        "  setTimeout(function () { document.body.removeChild(input); }, 25);" +
                        "};" +
                        "document.body.appendChild(input);" +
                        "return input;";

        WebElement input = (WebElement) jse.executeScript(JS_DROP_FILE, target, 0, 0);
        input.sendKeys(filePath.getAbsoluteFile().toString());
        wait.until(ExpectedConditions.stalenessOf(input));
    }
}
