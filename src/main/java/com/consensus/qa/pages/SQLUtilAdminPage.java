package com.consensus.qa.pages;

import java.util.concurrent.TimeUnit;

import com.consensus.qa.framework.PageBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.consensus.qa.framework.AdminBaseClass;
import com.consensus.qa.framework.BaseClass;
import com.consensus.qa.framework.ControlLocators;

public class SQLUtilAdminPage extends AdminBaseClass {
	
	/* region Page Initialization */
	public SQLUtilAdminPage(WebDriver d)
    {
        PageFactory.initElements(d, this);
    }	
	/* end region Page Initialization */
	
	@FindBy(name = ControlLocators.SUBMIT_QUERY_BUTTON)
	public WebElement submitButton;
	
	@FindBy(name = ControlLocators.CHOOSE_QUERY_WRAPPER_DROPDOWN)
	public WebElement chooseQueryWrapperDropdown;
	
	@FindBy(name = ControlLocators.QUERY_TEXTBOX)
	public WebElement queryTextbox;
	
	@FindBy(xpath = ControlLocators.ORDER_SIGNATURES_TABLE)
	public WebElement orderSignaturesTable;

	@FindBy(xpath = ControlLocators.GENERAL_TABLE)
	public WebElement generalTable;
	
	@FindBy(xpath = ControlLocators.ENCRYPTED_SIGNATURE_ROW)
	public WebElement encryptedSignatureRow;
	
	public void launchSQLUtilInNewTab()
	{
		try {
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
			driver.manage().timeouts().pageLoadTimeout(10000, TimeUnit.MILLISECONDS);
			driver.get(readConfig("urlSQLUtil"));
			log.append("Successfully launched the Inventory Management page in new tab <br>\n");
		}
		catch(Exception e){	//Setting UserName and Password by Sending Keys using Robot class
			PageBase.EnvironmentSetup().loginAdmin(readConfig("adminUserId"), readConfig("adminPwd"));
		}
		driver.manage().timeouts().pageLoadTimeout(300000, TimeUnit.MILLISECONDS);//Setting back to default Selenium timeout
	}

}
