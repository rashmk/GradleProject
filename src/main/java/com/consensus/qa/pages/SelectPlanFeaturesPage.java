package com.consensus.qa.pages;

import java.util.List;

import com.consensus.qa.framework.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.consensus.qa.framework.BrowserSettings;
import com.consensus.qa.framework.ControlLocators;

public class SelectPlanFeaturesPage {

	public SelectPlanFeaturesPage(WebDriver d)
	{
		PageFactory.initElements(d, this);
	}

	//Region- Web Elements
	@FindBy(xpath = ControlLocators.CANCEL_SPF_BUTTON)
	public WebElement cancelSPFButton;

	@FindBy(id = ControlLocators.CONTINUE_SPF_BUTTON)
	public WebElement continueSPFButton;
	//EndRegion

	//Region - Methods

	// Selecting Network access plan.
	public void selectNetworkAccessPlan(int planSequence)
	{
		//Get all the links displayed
		List<WebElement> networkList =  BrowserSettings.driver.findElements(By.xpath("//span[contains(text(),'Network Access')]/ancestor::a"));

		//Selecting Network Access plan.
		networkList.get(planSequence).click();
		Utilities.checkingChkbox(BrowserSettings.driver.findElement(By.id("addPoptInput_2_99020")));
	}

	// Selecting Family base plan.
	public void selectFamilyBasePlan(int planSequence)
	{
		//Get all the links displayed
		List<WebElement> networkList =  BrowserSettings.driver.findElements(By.xpath("//span[contains(text(),'FamilyBase')]/ancestor::a"));

		//Selecting Family Base plan.
		networkList.get(planSequence).click();
		Utilities.checkingChkbox(BrowserSettings.driver.findElement(By.id("addPoptInput_1_99334")));
	}
	//EndRegion
}