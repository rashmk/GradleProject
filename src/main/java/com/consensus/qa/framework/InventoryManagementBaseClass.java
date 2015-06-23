package com.consensus.qa.framework;

import java.awt.AWTException;
import java.awt.Desktop.Action;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;
import org.openqa.selenium.interactions.Actions;

public class InventoryManagementBaseClass extends BaseClass {
	
	public enum IMEIStatus{
		Sold,
		Available
	}
	
	private static Logger logFile = Logger.getLogger(Utilities.class.getName());
	@BeforeSuite
	public void LaunchApplication() throws InterruptedException, AWTException, java.io.IOException
	{
		try{
			launchBrowser(readConfig("urlInventory"));
			String window = driver.getWindowHandle();
			driver.switchTo().window(window);
			inventoryLogin();
		}
		catch(Exception e){	
			Reporter.log(e.toString());
		}
		driver.manage().timeouts().pageLoadTimeout(300000, TimeUnit.MILLISECONDS);//Setting back to default Selenium timeout
	}

	public void launchInventoryInNewTab() throws InterruptedException, AWTException, java.io.IOException
	{
		try{
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");
			driver.manage().timeouts().pageLoadTimeout(10000, TimeUnit.MILLISECONDS);

			driver.get(readConfig("urlInventory"));					

			Reporter.log("Successfully launched the Inventory Management page in new tab <br><p>");
			inventoryLogin();
		}
		catch(Exception e){	
			
			Reporter.log(e.toString());
		}
		driver.manage().timeouts().pageLoadTimeout(300000, TimeUnit.MILLISECONDS);//Setting back to default Selenium timeout
	}	
	//Region- Methods
	/* region Inventory Login Page Methods */
	public void inventoryLogin()
	{
		try {
			Utilities.implicitWaitSleep(2000);

			boolean exists = driver.findElements(By.id("logout")).size() != 0;

           if(!exists) {
			   driver.switchTo().frame(PageBase.InventoryManagementPage().iframeInventory);
			   Utilities.waitForElementVisible(PageBase.InventoryManagementPage().userNameTextbox);
			   PageBase.InventoryManagementPage().userNameTextbox.sendKeys(readConfig("inventoryUserId"));
			   PageBase.InventoryManagementPage().passwordTextbox.sendKeys(readConfig("inventoryPwd"));
			   PageBase.InventoryManagementPage().storeIDTextbox.sendKeys(readConfig("inventoryStoreIdDetail"));
			   PageBase.InventoryManagementPage().submitButton.click();
			   driver.switchTo().defaultContent();
		   }

            Utilities.waitForElementVisible(PageBase.InventoryManagementPage().productsLink);
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			logFile.error(ex.getMessage());
		}
	}
	/* end region Inventory Login Page Methods */
	//EndRegion - Methods

}
