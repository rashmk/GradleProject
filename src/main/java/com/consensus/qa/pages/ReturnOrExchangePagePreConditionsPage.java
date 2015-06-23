package com.consensus.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.consensus.qa.framework.BrowserSettings;
import com.consensus.qa.framework.ControlLocators;
import com.consensus.qa.framework.PageBase;
import com.consensus.qa.framework.Utilities;
import com.consensus.qa.framework.Utilities.SelectDropMethod;

public class ReturnOrExchangePagePreConditionsPage {
	public ReturnOrExchangePagePreConditionsPage( WebDriver d)
	{
		PageFactory.initElements(d, this);
	}

	@FindBy(xpath = ControlLocators.DEVICEPOWERON_RADIOBUTTON)
	public WebElement devicePowerOnRadioButton;
	
	@FindBy(xpath = ControlLocators.DEVICECONDITION_RADIOBUTTON)
	public WebElement deviceConditionRadioButton;
	
	@FindBy(xpath = ControlLocators.DEVICEACCESSORY_RADIOBUTTON)
	public WebElement deviceAccessoryRadioButton;
	
	@FindBy(xpath = ControlLocators.DEVICEPACKAGING_RADIOBUTTON)
	public WebElement devicePackagingRadioButton;
	
	@FindBy(name = ControlLocators.CONTINUEREV_BUTTON)
	public WebElement continueREVButton;
	
	//name=continueButton
	
	public void SelectPreconditions()
	{
		devicePowerOnRadioButton.click();
		deviceConditionRadioButton.click();
		deviceAccessoryRadioButton.click();
		devicePackagingRadioButton.click();
	}
}