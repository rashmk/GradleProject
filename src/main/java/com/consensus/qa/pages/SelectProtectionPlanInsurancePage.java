package com.consensus.qa.pages;

import com.consensus.qa.framework.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.consensus.qa.framework.BrowserSettings;
import com.consensus.qa.framework.ControlLocators;
import com.consensus.qa.framework.PageBase;

public class SelectProtectionPlanInsurancePage {

	//region Page Initialization
	public SelectProtectionPlanInsurancePage(WebDriver d)
	{
		PageFactory.initElements(d, this);
	}	
	
	//endregion Page Initialization  */

	//region Select Insurance Page Elements
	@FindBy(xpath = ControlLocators.E_SECURITEL_INSURANCE)
	public WebElement eSecuritelInsurance;

	@FindBy(xpath = ControlLocators.NO_INSURANCE)
	public WebElement NoInsurance;
	
	@FindBy(xpath = ControlLocators.NOINSURANCE_FIRSTMOBILE)
	public WebElement noInsuranceFirstMob;
	
	@FindBy(xpath = ControlLocators.NOINSURANCE_SECONDMOBILE)
	public WebElement noInsurancesecondMob;
	
	@FindBy(xpath = ControlLocators.NOINSURANCE_THIRDMOBILE)
	public WebElement noInsuranceThirdMob;

	@FindBy(name = ControlLocators.GUEST_REVIEW)
	public WebElement guestReview;	

	@FindBy(xpath = ControlLocators.E_SECURITEL_INSURANCE_FIRST)
	public WebElement eSecuritelInsuranceFirst;

	@FindBy(xpath = ControlLocators.APPLECARE_INSURANCE)
	public WebElement appleCareInsurance;

	@FindBy(xpath = ControlLocators.NOINSURANCE_SPRINT_FIRSTMOBILE)
	public WebElement noInsuranceSprintFirstMob;

	@FindBy(xpath = ControlLocators.NOINSURANCE_SPRINT_SECONDMOBILE)
	public WebElement noInsuranceSprintSecondMob;
	//endregion Select Insurance Page Elements

	//region Utility Methods
	public void selectAnInsurance()
	{
		PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst.click();
		PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
		PageBase.CommonControls().continueButton.click();
	}
	
	public void selectNoInsurance()
	{
		Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		Utilities.implicitWaitSleep(6000);
		PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
		Utilities.implicitWaitSleep(6000);
		PageBase.CommonControls().continueButton.click();
	}
	
	public void selectNoInsuranceForThreeDevices()
	{
		noInsuranceFirstMob.click();
		noInsuranceFirstMob.click();
		guestReview.click();
		PageBase.CommonControls().continueButton.click();
	}

	public void selectNoInsuranceForTwoDevices()
	{
		noInsuranceFirstMob.click();
		noInsuranceFirstMob.click();
		Utilities.implicitWaitSleep(10000);
		noInsuranceSprintSecondMob.click();
		noInsuranceSprintSecondMob.click();
		guestReview.click();
		Utilities.implicitWaitSleep(10000);
		PageBase.CommonControls().continueButton.click();
	}

	public void selectNoInsuranceForSprintWithTwoDevices()
	{
		noInsuranceSprintFirstMob.click();
		noInsuranceSprintFirstMob.click();
		Utilities.implicitWaitSleep(10000);
		noInsuranceSprintSecondMob.click();
		noInsuranceSprintSecondMob.click();
		guestReview.click();
		Utilities.implicitWaitSleep(10000);
		PageBase.CommonControls().continueButton.click();
	}
	//endregion Utility Methods
}
