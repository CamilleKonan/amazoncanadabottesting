package com.cabottesting.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class AmazonChatPage {

    WebDriver driver;
    WebDriverWait wait;

    public AmazonChatPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Opens Amazon home page
    public void openAmazon() {
        driver.get("https://www.amazon.ca/-/fr/gp/css/homepage.html?ref_=nav_youraccount_btn");
        driver.manage().window().maximize();
    }

    // Navigates to Help page
    public void goToHelpPage() {
        WebElement helpLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'/-/fr/gp/help/customer/display.html?ref_=nav_cs_help')]")));
        helpLink.click();
    }

    // Clicks buttons to start chat
    public void startChat() {
        WebElement helpButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Aide pour autre chose']")));
        helpButton.click();

        WebElement secondButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='page-wrapper hero-banner']//li[11]//div[1]//div[2]")));
        secondButton.click();

        WebElement extraHelp = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(text(),'J’ai besoin d’une aide supplémentaire')]")));
        extraHelp.click();

        switchToChatFrame();
    }

    // Switch to chat iframe
    public void switchToChatFrame() {
        List<WebElement> frames = driver.findElements(By.tagName("iframe"));
        for (WebElement frame : frames) {
            driver.switchTo().frame(frame);
            if (driver.findElements(By.tagName("textarea")).size() > 0) {
                System.out.println("✅ Chat iframe found");
                return;
            }
            driver.switchTo().defaultContent();
        }
        throw new RuntimeException("Chat iframe not found!");
    }

    // Sends a message in chat
    public void sendPrompt(String msg) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("textarea")));
        input.sendKeys(msg);
        input.sendKeys(Keys.ENTER);
    }
}
