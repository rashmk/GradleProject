package com.consensus.qa.pages;

import com.consensus.qa.framework.ControlLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by tarunk on 6/3/2015.
 */
public class SprintEasyPayPage {

    public SprintEasyPayPage(WebDriver d)
    {
        PageFactory.initElements(d, this);
    }

    @FindBy(xpath = ControlLocators.PRICE_BOX)
    public WebElement priceBox;

    @FindBy(xpath = ControlLocators.DECLINE_BUTTON)
    public WebElement declineButton;

    @FindBy(id = ControlLocators.YES_CHECK_ELIGIBILITY)
    public WebElement yesButton;

}
