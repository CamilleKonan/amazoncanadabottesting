package com.cabottesting.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class AmazonChatPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    public AmazonChatPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // ==============================
    // 1. Open Amazon
    // ==============================
    public void openAmazon() {
        driver.get("https://www.amazon.ca/-/fr/gp/css/homepage.html?ref_=nav_youraccount_btn");
        driver.manage().window().maximize();
    }

    // ==============================
    // 2. Navigate to Help Section
    // ==============================
    public void goToHelpSection() {

        // Click Help link
        driver.findElement(By.xpath(
                "//a[contains(@href,'/-/fr/gp/help/customer/display.html?ref_=nav_cs_help')]"
        )).click();

        // Click “Aide pour autre chose”
        WebElement helpButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[normalize-space()='Aide pour autre chose']")
                )
        );

        js.executeScript("arguments[0].scrollIntoView(true);", helpButton);
        helpButton.click();

        // Click specific help category
        WebElement secondButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@class='page-wrapper hero-banner']//li[11]//div[1]//div[2]")
                )
        );

        js.executeScript("arguments[0].scrollIntoView(true);", secondButton);
        secondButton.click();

        // Click additional help
        WebElement extraHelp = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[contains(text(),'J’ai besoin d’une aide supplémentaire')]")
                )
        );

        extraHelp.click();

        // Switch to new window (chat window)
        switchToNewWindow();
    }

    // ==============================
    // 3. Switch to New Window
    // ==============================
    private void switchToNewWindow() {

        String originalWindow = driver.getWindowHandle();

        wait.until(d -> d.getWindowHandles().size() > 1);

        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    // ==============================
    // 4. Switch to Chat Iframe
    // ==============================
    public void switchToChatFrame() {

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("iframe")));

        List<WebElement> frames = driver.findElements(By.tagName("iframe"));

        for (WebElement frame : frames) {
            driver.switchTo().frame(frame);

            if (driver.findElements(By.tagName("textarea")).size() > 0) {
                System.out.println("✅ Chat iframe found");
                return;
            }

            driver.switchTo().defaultContent();
        }

        throw new RuntimeException("❌ Chat iframe not found!");
    }

    // ==============================
    // 5. Send Message
    // ==============================
    public void sendPrompt(String message) {

        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("textarea"))
        );

        input.sendKeys(message);
        input.sendKeys(Keys.ENTER);
    }

    // ==============================
    // 6. Handle Chat Workflow
    // ==============================
    public void handleChatWorkflow(String message) {

        String originalWindow = driver.getWindowHandle();

        sendPrompt(message);

        List<String> possibleAnswers = Arrays.asList(
                "Your order is on the way!",
                "Please provide your order ID.",
                "Sorry, I couldn’t find your order."
        );

        String matchedAnswer = waitForAnyResponse(possibleAnswers);

        System.out.println("Bot responded with: " + matchedAnswer);

        if (matchedAnswer.contains("order ID")) {
            WebElement reply = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.tagName("textarea"))
            );
            reply.sendKeys("Order12345");
            reply.sendKeys(Keys.ENTER);
        }

        // Capture chat URL in new tab (optional)
        driver.switchTo().newWindow(WindowType.TAB);
        String chatURL = driver.getCurrentUrl();
        System.out.println("Captured chat window URL: " + chatURL);

        driver.switchTo().window(originalWindow);
    }

    // ==============================
    // 7. Wait for Bot Response
    // ==============================
    private String waitForAnyResponse(List<String> possibleAnswers) {

        for (String answer : possibleAnswers) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(.,'" + answer + "')]")
                ));
                return answer;
            } catch (TimeoutException ignored) {
            }
        }

        throw new RuntimeException("❌ No known bot response appeared within timeout!");
    }
}