package com.consensus.qa.pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.consensus.qa.framework.AccountDetails;
import com.consensus.qa.framework.CSVOperations;
import com.consensus.qa.framework.ControlLocators;
import com.consensus.qa.framework.CustomerDetails;
import com.consensus.qa.framework.PageBase;
import com.consensus.qa.pages.ServiceProviderVerificationPage.IdType;

public class UECVerificationPage {

	/* region Page Initialization */
	public UECVerificationPage(WebDriver d)
	{
		PageFactory.initElements(d, this);
	}
	/* end region Page Initialization */

	/* region Home Page Retail Elements */
	@FindBy(xpath = ControlLocators.VERIZON_TAB)
	public WebElement verizonTab;

	@FindBy(xpath = ControlLocators.PHONE_NUMBER_VERIZON_TEXTBOX)
	public WebElement phoneNumberVerizonTextbox;

	@FindBy(xpath = ControlLocators.LAST_4_OF_SSN_VERIZON_TEXTBOX)
	public WebElement last4OfSSNVerizonTextbox;

	@FindBy(xpath = ControlLocators.ACCOUNT_PASSWORD_VERIZON_TEXTBOX)
	public WebElement accountPasswordVerizonTextbox;

	@FindBy(xpath = ControlLocators.ACCOUNT_ZIPCODE_VERIZON_TEXTBOX)
	public WebElement accountZipcodeVerizonTextbox;

	@FindBy(xpath = ControlLocators.CONTINUE_VERIZON_BUTTON)
	public WebElement continueVerizonButton;

	@FindBy(xpath = ControlLocators.SPRINT_TAB)
	public WebElement sprintTab;

	@FindBy(xpath = ControlLocators.PHONE_NUMBER_SPRINT_TEXTBOX)
	public WebElement phoneNumberSprintTextbox;

	@FindBy(xpath = ControlLocators.LAST_4_OF_SSN_SPRINT_TEXTBOX)
	public WebElement last4OfSSNSprintTextbox;

	@FindBy(xpath = ControlLocators.PIN_SPRINT_TEXTBOX)
	public WebElement pinSprintTextbox;

	@FindBy(xpath = ControlLocators.CONTINUE_SPRINT_BUTTON)
	public WebElement continueSprintButton;
	/* end region Login Page Elements */

	public void fillVerizonDetails(String phoneNumber,String SSN,String Pwd,String zipCode)
	{
		verizonTab.click();
		phoneNumberVerizonTextbox.sendKeys(phoneNumber);
		last4OfSSNVerizonTextbox.sendKeys(SSN);
		accountPasswordVerizonTextbox.sendKeys(Pwd);
		accountZipcodeVerizonTextbox.sendKeys(zipCode);
	}

	public void fillSprintDetails(String phoneNumber,String SSN,String pin)
	{
		sprintTab.click();
		phoneNumberSprintTextbox.sendKeys(phoneNumber);
		last4OfSSNSprintTextbox.sendKeys(SSN);
		pinSprintTextbox.sendKeys(pin);
	}
}