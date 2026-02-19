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

    public void sendMessage(String msg) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("textarea")));
        input.sendKeys(msg);
        input.sendKeys(Keys.ENTER);
    }
}
