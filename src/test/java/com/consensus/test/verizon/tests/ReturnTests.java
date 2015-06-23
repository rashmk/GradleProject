package com.consensus.test.verizon.tests;

import com.consensus.qa.framework.*;
import com.consensus.qa.pages.PaymentRequiredPage;
import com.consensus.qa.pages.ServiceProviderVerificationPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.consensus.qa.framework.Utilities.SelectDropMethod;
import com.consensus.qa.pages.GuestVerificationPage.IdType;

import javax.rmi.CORBA.Util;
import java.awt.*;

public class ReturnTests extends RetailBaseClass{

	String imei1 = "35799605310008";
	String orderId = "";
	public String carrierType = "Verizon"; 

	//region QA 53
	@Test(groups={"verizon"})
	@Parameters("test-type")
	public void QA_53_VerizonNonEdgeReturn(@Optional String testtype)
	{	 	  
		try {

			Log.startTestCase("QA_53_VerizonNonEdgeReturn");

			// Verify whether which environment to use internal or external.
			testtype = BrowserSettings.readConfig("test-type");
			if(testtype.equals("internal")){                      
				AdminBaseClass adminBaseClass = new AdminBaseClass();
				adminBaseClass.launchAdminInNewTab();

				PageBase.AdminPage().navigateToSimulator();
				PageBase.AdminPage().selectWebAPIResponse(carrierType,BrowserSettings.readConfig("internalTestType"));

				//Selecting Use Case from dropdown list.
				PageBase.AdminPage().selectAPIConfig(carrierType);

				//PageBase.AdminPage().selectCreaditReadUseCase("APPROVED");
				//PageBase.AdminPage().selectCreaditWriteUseCase("APPROVED");
				//PageBase.AdminPage().save();				
			}
			else{   //External// Need to set External server from Admin page.				
				AdminBaseClass adminBaseClass = new AdminBaseClass(); 
				adminBaseClass.launchAdminInNewTab();    

				PageBase.AdminPage().navigateToSimulator(); 
				PageBase.AdminPage().selectWebAPIResponse(carrierType,"External"); 
			}

			// Switching to Retail tab.
			Utilities.switchPreviousTab();
			//Login to retail page.
			PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
					Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

			PageBase.HomePageRetail().guestLookupTab.click();

			PageBase.CustomerLookupPage().viewGuestOrders.click();
			PageBase.CustomerLookupPage().continueButton.click();
			Utilities.waitForElementVisible(PageBase.GuestVerificationPage().idTypeDropdown);  
			PageBase.GuestVerificationPage().populateGuestVerificationDetails(IdType.DRIVERLICENCE, "CA",
					"123456789", "Fred", "Consumer Two");
			Utilities.waitForElementVisible(PageBase.OrderHistory().completedLink);
			PageBase.OrderHistory().completedLink.click();

			Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().orderID);
			orderId = PageBase.ReturnScanDevicePage().orderID.getText();
			PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(imei1);
			PageBase.CustomerLookupPage().continueButton.click();

			PageBase.ReturnOrExhangePreConditions().SelectPreconditions();
			PageBase.ReturnOrExhangePreConditions().continueREVButton.click();
			PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
			PageBase.ReturnOrExchangeVerificationPage().returnDEVICE.click();
			Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().returnReasons,
					SelectDropMethod.SELECTBYINDEX, "1");
			PageBase.CustomerLookupPage().continueButton.click();

			if(driver.getCurrentUrl().contains("passwordcapture"))
			{
				PageBase.VerizonAccountPassword().password.sendKeys("Hello");
				PageBase.VerizonAccountPassword().continueButton.click();
			}
			else if(driver.getCurrentUrl().contains("printticket"))
			{
				PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();
			}
			Assert.assertTrue(driver.findElement(By.xpath("//h2[contains(text(), 'Support Center')]")).isDisplayed());

			// Commenting below Assertions as this doesn't show up in Carrier Responder

			if(driver.getCurrentUrl().contains("passwordcapture"))
			{
				PageBase.VerizonAccountPassword().password.sendKeys("Hello");
				PageBase.VerizonAccountPassword().continueButton.click();
			}
			//*PageBase.CommonControls().continueButton.click();

			// RMSS Assertions - Todo from Parent Order
			/*Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(),
					"");
			Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phoneTaxValuePMSSText.getText(),
					"");
			PageBase.PrintMobileScanSheetPage().barcodePMSSImage.isDisplayed();

			PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();


			Assert.assertTrue(PageBase.ReturnConfirmation().returnConfirmation.isDisplayed());
			Assert.assertTrue(PageBase.ReturnConfirmation().successfullyReturnedString.isDisplayed());
			Assert.assertTrue(PageBase.ReturnConfirmation().linePhonenoString.isDisplayed());
			Assert.assertTrue(PageBase.ReturnConfirmation().returnStep1Text.isDisplayed());
			Assert.assertTrue(PageBase.ReturnConfirmation().returnStep2Text.isDisplayed());
			Assert.assertTrue(PageBase.ReturnConfirmation().printInstruction.isDisplayed());
			Assert.assertTrue(PageBase.ReturnConfirmation().returnHome.isDisplayed());*/

			// POS Verification
			PageBase.InventoryManagementPage().launchInventoryInNewTab();
			PageBase.InventoryManagementPage().verifyDeviceStatus(imei1, "Sold");		

			// Shipadmin Verification
			ShipAdminBaseClass.launchShipAdminInNewTab();
			PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);           
			String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId); 
			String status = PageBase.OrderSummaryPage().getOrderStatus();
			Assert.assertEquals(status, Constants.SHIPPED);
			
			// Not verifiable for Carrier Responder, since it goes to Support Center
			//Assert.assertTrue(eventLogTableContent.contains(Constants.ASSIGNED_RETURN_ORDER_NUMBER));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	//endregion QA 53

	//region QA-73
	@Test(groups={"verizon"})
	@Parameters("test-type")
	public void QA73_VerizonEdgeUpgradeAndReturn(@Optional String testtype)
	{
		boolean  stopActivation = false;
		try
		{
			//This TC requires

			Log.startTestCase("QA73_VerizonEdgeUpgradeAndReturn");

			String phoneNumber = "8553835666";
			String iMEINumber = "9886886321";
			System.out.println(iMEINumber);
			String simNumber = "12345678901234567890";
			String receiptID = "132710003003680723";


			//Login
			PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));
			//Verify whether which enviorement to use internal or external.
			testtype ="internal";
			if(testtype.equals("internal"))
			{


			}
			else
			{
				//Script for external testing.
			}
			PageBase.HomePageRetail().newGuestButton.click();

			//Home Page
			Utilities.waitForElementVisible(PageBase.HomePageRetail().upgradeEligibilityCheckerLink);
			PageBase.HomePageRetail().guestLookupTab.click();

			//Customer Lookup Page
			Utilities.waitForElementVisible(PageBase.CustomerLookupPage().receiptIdTextbox);
			PageBase.CustomerLookupPage().receiptIdTextbox.sendKeys(receiptID);
			PageBase.CustomerLookupPage().submitButton.click();
			//Order History Page
			try
			{
            Utilities.waitForElementVisible(PageBase.OrderHistory().firstCompletedLink, 12);
				PageBase.OrderHistory().firstCompletedLink.click();
			}
			catch(Exception e){}

			//Guest Verification Page
			Utilities.waitForElementVisible(PageBase.GuestVerificationPage().idTypeDropdown);
			PageBase.GuestVerificationPage().populateGuestVerificationDetails(IdType.DRIVERLICENCE, "CA",
					"123456789", "TEST", "TESTER");
			PageBase.CommonControls().continueCommonButton.click();

			//Return or Exchange Scan Device Page
			Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().esnIemeidTextbox);
			PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(iMEINumber);
			PageBase.CustomerLookupPage().continueButton.click();

			//Return or Exchange Verification Page
			Utilities.waitForElementVisible(PageBase.ReturnOrExhangePreConditions().continueREVButton);
			PageBase.ReturnOrExhangePreConditions().SelectPreconditions();
			PageBase.ReturnOrExhangePreConditions().continueREVButton.click();

			//Return or Exchange Verification
			PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
			PageBase.ReturnOrExchangeVerificationPage().returnDEVICE.click();
			Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().returnReasons,
					SelectDropMethod.SELECTBYINDEX, "1");
			PageBase.CustomerLookupPage().continueButton.click();

			//Verizon Account Password
			Utilities.waitForElementVisible(PageBase.VerizonAccountPassword().continueButton);
			PageBase.VerizonAccountPassword().continueButton.click();

			//Support Center Page, currently this is the expected behaviour
			Utilities.waitForElementVisible(PageBase.CommonControls().supportCenterText);
			PageBase.CommonControls().supportCenterText.isDisplayed();

			Log.endTestCase("QA73_VerizonEdgeUpgradeAndReturn");
		}
		catch(Exception ex)
		{
			Log.error(ex.getMessage());
			System.out.println(ex.getMessage());
			Utilities.driverTakesScreenshot("QA73_VerizonEdgeUpgradeAndReturn");
			Assert.assertTrue(false);
		}
		finally
		{

		}
	}
	//endregion QA-73
}
