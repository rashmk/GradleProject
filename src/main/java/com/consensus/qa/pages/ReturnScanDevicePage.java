package com.consensus.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.consensus.qa.framework.BrowserSettings;
import com.consensus.qa.framework.ControlLocators;
import com.consensus.qa.framework.Utilities;
import com.consensus.qa.framework.Utilities.SelectDropMethod;

public class ReturnScanDevicePage {
	public ReturnScanDevicePage( WebDriver d)
	{
		PageFactory.initElements(d, this);
	}

	@FindBy(id = ControlLocators.ESNIEMEID_TEXTBOX)
	public WebElement esnIemeidTextbox;

	@FindBy(xpath = ControlLocators.ORDER_ID)
	public WebElement orderID;
	//continueButton
	
}