package com.consensus.qa.pages;

import java.util.Date;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.consensus.qa.framework.ControlLocators;
import com.consensus.qa.framework.PageBase;
import com.consensus.qa.framework.RetailBaseClass;
import com.consensus.qa.framework.Utilities;

public class LoginPageRetailPage extends RetailBaseClass{
	
	/* region Page Initialization */
	public LoginPageRetailPage(WebDriver d)
    {
        PageFactory.initElements(d, this);
    }
	/* end region Page Initialization */
	
	/* region Login Page Retail Elements */
	@FindBy(id = ControlLocators.USERNAME_TEXTBOX)
	public WebElement usernameTextbox;
	
	@FindBy(id = ControlLocators.PASSWORD_TEXTBOX)
	public WebElement passwordTextbox;
	
	@FindBy(id = ControlLocators.STORE_ID_TEXTBOX)
	public WebElement storeIdTextbox;
	
	@FindBy(xpath = ControlLocators.STORE_ID_BUTTON)
	public WebElement storeIdButton;
	
	@FindBy(xpath = ControlLocators.LOGIN_BUTTON)
	public WebElement loginButton;
	/* end region Login Page Retail Elements */
	
	/* region Login Page Retail Methods */
	public void poaLogin(String username, String password, String storeid)
	{
		Utilities.waitForElementVisible(usernameTextbox);
		PageBase.LoginPageRetail().usernameTextbox.sendKeys(username);
		PageBase.LoginPageRetail().passwordTextbox.sendKeys(password);
		PageBase.LoginPageRetail().storeIdTextbox.sendKeys(storeid);
		Utilities.waitForElementVisible(storeIdButton);
		PageBase.LoginPageRetail().storeIdButton.click();
		lStartTime = new Date().getTime();
		pageName = readPageName("MobileActivationCenter");
		PageBase.LoginPageRetail().loginButton.click();
		Utilities.webPageLoadTime(lStartTime, pageName);
		Utilities.waitForElementVisible(PageBase.HomePageRetail().newGuestButton);
	}
	/* end region Login Page Retail Methods */
}