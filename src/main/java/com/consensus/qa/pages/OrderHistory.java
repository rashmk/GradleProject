package com.consensus.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.consensus.qa.framework.BrowserSettings;
import com.consensus.qa.framework.ControlLocators;
import com.consensus.qa.framework.Utilities;
import com.consensus.qa.framework.Utilities.SelectDropMethod;

public class OrderHistory {

	public OrderHistory(WebDriver d)
	{
		PageFactory.initElements(d, this);
	}

	@FindBy(xpath = ControlLocators.COMPLETED_LINK)
	public WebElement completedLink;

	@FindBy(xpath = ControlLocators.FIRST_COMPLETED_LINK)
	public WebElement firstCompletedLink;
	
}
