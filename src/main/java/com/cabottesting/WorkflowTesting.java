package com.cabottesting;

import com.cabottesting.config.DriverFactory;
import com.cabottesting.pages.AmazonChatPage;
import com.cabottesting.utils.ExcelUtils;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class WorkflowTesting {

    public static void main(String[] args) throws Exception {

        WebDriver driver = DriverFactory.createDriver();
        driver.get("https://www.amazon.ca/gp/help/customer/display.html");

        AmazonChatPage chat = new AmazonChatPage(driver);

        // Load prompts from Excel
        List<String> prompts = ExcelUtils.readPrompts("C:\\Users\\gkkcw\\Documents\\SeleniumWMStesting.xlsx");

        chat.switchToChatFrame();

        for (String p : prompts) {
            System.out.println("Sending: " + p);
            chat.sendPrompt(p);
            Thread.sleep(5000);
        }

        driver.quit();
    }
}
