package com.consensus.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.consensus.qa.framework.ControlLocators;

public class CreditCheckVerificationResultsPage {
	
	public CreditCheckVerificationResultsPage(WebDriver d)
	{
		PageFactory.initElements(d, this);
	}
	
	//Region- Web Elements
	@FindBy(id = ControlLocators.CREDIT_CHECK_PASS_CHKBOX)
	public WebElement creditCheckPassChkBox;
	
	@FindBy(id = ControlLocators.DEPOSIT_CHKBOX)
	public WebElement depositCheckBox;	
	//EndRegion
}