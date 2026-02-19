package com.cabottesting.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

    public class DriverFactory {

        public static WebDriver createDriver() {
            ChromeOptions options = new ChromeOptions();

            options.addArguments("--start-maximized");
            options.addArguments("--disable-blink-features=AutomationControlled");

            return new ChromeDriver(options);
        }
    }


