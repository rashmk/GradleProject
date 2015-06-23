package com.consensus.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.consensus.qa.framework.ControlLocators;

public class DeviceVerificationaandActivation {

	/* region Page Initialization */
	public DeviceVerificationaandActivation(WebDriver d)
    {
        PageFactory.initElements(d, this);
    }	
	/* end region Page Initialization */
	
	@FindBy(id = ControlLocators.DEVICE_IMEI_TEXTBOX)
	public WebElement deviceIMEITextbox;
	
	@FindBy(id = ControlLocators.SUBMIT_DVA_BUTTON)
	public WebElement submitDVAButton;
	
	@FindBy(xpath = ControlLocators.SIM_TYPE)
	public WebElement SimType;
	
	@FindBy(xpath = ControlLocators.CONTINUE_BUTTON_DVA)
	public WebElement continueButtonDVA;
	
	@FindBy(id = ControlLocators.CVN_NUMBER_DVA_TEXTBOX)
	public WebElement cvnNumberDVATextbox;
}
