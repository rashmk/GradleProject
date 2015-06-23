package com.consensus.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.consensus.qa.framework.BrowserSettings;
import com.consensus.qa.framework.ControlLocators;
import com.consensus.qa.framework.Utilities;
import com.consensus.qa.framework.Utilities.SelectDropMethod;

public class ReceiptPage {
	public ReceiptPage( WebDriver d)
	{
		PageFactory.initElements(d, this);
	}
	
	@FindBy(xpath = ControlLocators.ORDER_COMPLETION_TEXT)
	public WebElement orderCompletionText;

	public void verifyOrderCompletionPage()
	{
		if(orderCompletionText.isDisplayed()==true)
		{
			 System.out.println("Receipt Page verification Completed Successfully");
		}
			
	}
}