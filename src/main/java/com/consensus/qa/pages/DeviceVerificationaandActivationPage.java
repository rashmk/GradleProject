package com.consensus.qa.pages;

import com.consensus.qa.framework.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import java.io.IOException;

public class DeviceVerificationaandActivationPage extends BrowserSettings {

	public String simTypeM = "simscan-";
	
	/* region Page Initialization */
	public DeviceVerificationaandActivationPage(WebDriver d)
    {
        PageFactory.initElements(d, this);
    }	
	/* end region Page Initialization */
	
	@FindBy(id = ControlLocators.DEVICE_IMEI_TEXTBOX)
	public WebElement deviceIMEITextbox;
	
	@FindBy(id = ControlLocators.SUBMIT_DVA_BUTTON)
	public WebElement submitDVAButton;
	
	@FindBy(xpath = ControlLocators.SIM_TYPE)
	public WebElement simType;
	
	@FindBy(xpath = ControlLocators.SIM_TYPE_2_TEXTBOX)
	public WebElement simType2Textbox;
	
	@FindBy(xpath = ControlLocators.CONTINUE_BUTTON_DVA)
	public WebElement continueButtonDVA;
	
	@FindBy(id = ControlLocators.CVN_NUMBER_DVA_TEXTBOX)
	public WebElement cvnNumberDVATextbox;

	@FindBy(id = ControlLocators.PASSWORD__INVENTORY_TEXTBOX)
	public WebElement passwordInventoryTextbox;

	public void deviceVerificationActiavtionFor3Devices( String imei1, String imei2, String imei3,String sim1,String sim2,String sim3) throws IOException {
		Utilities.waitForElementVisible(deviceIMEITextbox);
		deviceIMEITextbox.sendKeys(imei1);
		submitDVAButton.click();
		driver.findElement(By.id(simTypeM + imei1)).click();
		driver.findElement(By.id(simTypeM + imei1)).sendKeys(sim1);
		PageBase.CommonControls().continueButtonDVA.click();

		if(imei2.length()>0) {
			deviceIMEITextbox.sendKeys(imei2);
			submitDVAButton.click();
			driver.findElement(By.id(simTypeM + imei2)).click();
			driver.findElement(By.id(simTypeM + imei2)).sendKeys(sim2);
			PageBase.CommonControls().continueButtonDVA.click();
		}

		if(imei3.length()>0) {
			deviceIMEITextbox.sendKeys(imei3);
			submitDVAButton.click();
			driver.findElement(By.id(simTypeM + imei3)).click();
			driver.findElement(By.id(simTypeM + imei3)).sendKeys(sim3);
			PageBase.CommonControls().continueButtonDVA.click();
		}
	}
	
	public void deviceVerificationActiavtionFor1Device(String imei, String simType1) throws IOException {
		CreditCardDetails ccDetails= PageBase.CSVOperations().CreditCardDetails(PaymentRequiredPage.CardType.VISA);
		Utilities.waitForElementVisible(deviceIMEITextbox);
		deviceIMEITextbox.sendKeys(imei);
		submitDVAButton.click();
		simType.clear();
		simType.sendKeys(simType1);
		try {
			cvnNumberDVATextbox.sendKeys(ccDetails.CVV);
		}
		catch (Exception ex)
		{
			Reporter.log("CVN Number No Needed");
		}
		PageBase.CommonControls().continueButtonDVA.click();
	}

	public void deviceVerificationActiavtionFor1Device(String imei)
	{
		Utilities.waitForElementVisible(deviceIMEITextbox);
		deviceIMEITextbox.sendKeys(imei);
		submitDVAButton.click();
		simType.clear();
		simType.sendKeys("91728349741683484627");
		cvnNumberDVATextbox.sendKeys();
		//PageBase.CommonControls().continueButtonDVA.click();
	}

	public void deviceVerificationActiavtionFor3Devices( String imei1, String imei2, String imei3)
	{
		Utilities.waitForElementVisible(deviceIMEITextbox);
		deviceIMEITextbox.sendKeys(imei1);
		submitDVAButton.click();
		driver.findElement(By.id(simTypeM + imei1)).click();
		driver.findElement(By.id(simTypeM + imei1)).sendKeys("67531468542647852693");
		PageBase.CommonControls().continueButtonDVA.click();

		if(imei2.length()>0) {
			deviceIMEITextbox.sendKeys(imei2);
			submitDVAButton.click();
			driver.findElement(By.id(simTypeM + imei2)).click();
			driver.findElement(By.id(simTypeM + imei2)).sendKeys("99000342701027964454");
			PageBase.CommonControls().continueButtonDVA.click();
		}

		if(imei3.length()>0) {
			deviceIMEITextbox.sendKeys(imei3);
			submitDVAButton.click();
			driver.findElement(By.id(simTypeM + imei3)).click();
			driver.findElement(By.id(simTypeM + imei3)).sendKeys("99000342701027259543");
			PageBase.CommonControls().continueButtonDVA.click();
		}
	}
}
