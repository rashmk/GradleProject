package com.consensus.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.consensus.qa.framework.BrowserSettings;
import com.consensus.qa.framework.ControlLocators;
import com.consensus.qa.framework.Utilities;
import com.consensus.qa.framework.Utilities.SelectDropMethod;

public class CustomerLookupPage {
	public CustomerLookupPage( WebDriver d)
	{
		PageFactory.initElements(d, this);
	}

	@FindBy(id = ControlLocators.RECEIPTID_TEXTBOX)
	public WebElement receiptIdTextbox;
	
	@FindBy(xpath = ControlLocators.FIRSTNAME_TEXTBOX)
	public WebElement firstnameTextbox;
	
	@FindBy(xpath = ControlLocators.LASTNAME_TEXTBOX)
	public WebElement lastnameTextbox;	
	
	@FindBy(xpath = ControlLocators.EMAILID_TEXTBOX)
	public WebElement emailidTextbox;
	
	@FindBy(xpath = ControlLocators.PHONE_TEXTBOX)
	public WebElement phoneTextbox;
	
	@FindBy(xpath = ControlLocators.ORDERLOOKUP_TAB)
	public WebElement orderLookupTab;

	@FindBy(id = ControlLocators.CONTINUE_CUSTOMERLOOKUP)
	public WebElement continueButton;

	@FindBy(xpath = ControlLocators.VIEW_GUEST_ORDERS)
	public WebElement viewGuestOrders;

	@FindBy(xpath = ControlLocators.SUBMIT_CUSTOMER_LOOKUP_BUTTON)
	public WebElement submitButton;
	
	public void PopulateOrderLookUpDetails(String fname,String lname,String phone,String email)
	{
		orderLookupTab.click();
		firstnameTextbox.sendKeys(fname);
		lastnameTextbox.sendKeys(lname);
		phoneTextbox.sendKeys(phone);
		emailidTextbox.sendKeys(email);
	}
	
}