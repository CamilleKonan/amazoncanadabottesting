package com.cabottesting.tests;

import com.cabottesting.base.BaseTest;
import com.cabottesting.pages.AmazonChatPage;
import org.testng.annotations.Test;

public class AmazonChatTest extends BaseTest {

    @Test
    public void testAmazonChatWorkflow() throws Exception {

        AmazonChatPage chatPage = new AmazonChatPage(driver);

        chatPage.openAmazon();
        chatPage.goToHelpPage();
        chatPage.startChat();

        chatPage.sendPrompt("Hello, I need help with my order");
    }
}

