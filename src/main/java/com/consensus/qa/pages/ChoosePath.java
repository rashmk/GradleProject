package com.consensus.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.consensus.qa.framework.ControlLocators;

public class ChoosePath {
	
	/*region Page Initialization*/
	 public ChoosePath(WebDriver d)
	 {
		 PageFactory.initElements(d, this);		 
	 }	   
	 /* end region Page Initialization */

	 @FindBy(xpath = ControlLocators.NEW_ACTIVATION)
	 public WebElement  newActivation;
	 
	 @FindBy(xpath = ControlLocators.EXISTING_CAREER)
	 public WebElement  existingCarrier;
	 
	 @FindBy(xpath = ControlLocators.BROWSE_PHONES)
	 public WebElement  browsePhones;
	 
	 @FindBy(xpath = ControlLocators.BROWSE_PLANS)
	 public WebElement  browsePlans;
}