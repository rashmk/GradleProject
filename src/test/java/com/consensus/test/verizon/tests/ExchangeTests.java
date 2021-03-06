package com.consensus.test.verizon.tests;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import com.consensus.qa.framework.*;
import com.consensus.qa.framework.CSVOperations.FileName;
import com.consensus.qa.framework.Utilities.SelectDropMethod;
import com.consensus.qa.pages.GuestVerificationPage;
import com.consensus.qa.pages.ReturnProcessPage;
import com.consensus.qa.tests.CarrierCreditCheckDetails;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.Reporter.*;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.consensus.qa.pages.PaymentRequiredPage.CardType;
import com.consensus.qa.pages.ReturnProcessPage.ExchangeReason;
import com.consensus.qa.pages.ReturnProcessPage.FinancingOption;
import com.consensus.qa.pages.ServiceProviderVerificationPage.IdType;
import com.consensus.qa.pages.ServiceProviderVerificationPage.Month;
import com.consensus.qa.pages.AdminPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.lift.TestContext;

import java.awt.Robot;

import static com.consensus.qa.pages.ServiceProviderVerificationPage.IdType.DRIVERLICENCE;

public class ExchangeTests extends RetailBaseClass{

	//region public variable
	public String carrierType = "Verizon";
	public String orderId = "";
	String ChildOrderId = "";
	String iMEINumber1 = "";
	//endregion

	//region Test Methods
	//region QA 59
	@Test(groups={"verizon"})
	@Parameters("test-type")
	public void QA_59_VerizonNonEdgeExchange(@Optional String testType) throws InterruptedException, AWTException, IOException
	{
		Log.startTestCase("QA_59_VerizonNonEdgeExchange");
		testType = BrowserSettings.readConfig("test-type");
		//String fromQA62[] = Utilities.ReadCSV("QA-62");

		//System.out.println("values"+fromQA62[0]+fromQA62[1]+fromQA62[2]);
		if(testType.equals("internal"))
		{
			// Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
			AdminBaseClass adminBaseClass = new AdminBaseClass();
			adminBaseClass.launchAdminInNewTab();
			PageBase.AdminPage().navigateToSimulator();

			PageBase.AdminPage().selectWebAPIResponse("Verizon", "BackendSimulator");

			//Selecting Use Case from dropdown list.
			PageBase.AdminPage().selectAPIConfig("Verizon");
			//PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
			//PageBase.CarrierResponseXMLPage().selectOptions("current", "returnOrExchangeDevice", "default_exchange.xml");
			//PageBase.CarrierResponseXMLPage().SelectCarrierResponserXML();
		}
		else   //External
		{
			// Need to set External server from Admin page.
			AdminBaseClass adminBaseClass = new AdminBaseClass();
			adminBaseClass.launchAdminInNewTab();

			PageBase.AdminPage().navigateToSimulator();
			PageBase.AdminPage().selectWebAPIResponse("Verizon","External");
		}

		// Switching to previous tab.
		Utilities.switchPreviousTab();

		/*DBError.navigateDBErrorPage();
		int initialCount= PageBase.AdminPage().totalErrorCount(); //to be sent below previous tab

		// Switching to previous tab.
		Utilities.switchPreviousTab();*/

		String orderId = QA_59_PoaFlow();

		//Verify Ship Admin Status
		QA_59_VerifyInShipAdmin(orderId);

		DBError.navigateDBErrorPage();
		Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(3));

		Log.endTestCase("QA_59_VerizonNonEdgeExchange");
		Reporter.log("<h3>QA_59_VerizonNonEdgeExchange - Test Case Completes<h3>");
	}

	private String QA_59_PoaFlow() throws IOException, AWTException, InterruptedException {
		//Login to POA
		PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

		DependantTestCaseInputs dependantValues = Utilities.ReadFromCSV("QA_62");
		String parentOrderId = dependantValues.TC_ID;//QA_62Details[1];
		String QA62orderId = dependantValues.ORDER_ID;
		String returnDeviceIEMI = dependantValues.ESN_IMEI1; //QA_62Details[2];
		String firstName = dependantValues.FIRST_NAME; //QA_62Details[4];
		String lastName = dependantValues.LAST_NAME; //QA_62Details[5];
		String mail = dependantValues.EMAIL; //QA_62Details[6];
		String idType = dependantValues.ID_TYPE; //QA_62Details[8];
		String state = dependantValues.STATE;  //QA_62Details[9];
		String IDNumber = dependantValues.ID_NUMBER; //QA_62Details[10];


		CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
		String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
		String esnNo = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
		String simNumber = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);

		Utilities.waitForElementVisible(PageBase.HomePageRetail().guestLookupTab);
		PageBase.HomePageRetail().guestLookupTab.click();

		CreditCardDetails creditCard = new CreditCardDetails();
		creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);
		//PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys("9988776655443301");

		//Enter the Customer Details
		Utilities.waitForElementVisible(PageBase.CustomerLookupPage().orderLookupTab);
		//PageBase.CustomerLookupPage().PopulateOrderLookUpDetails("Mritunjai", "Rai", "", "mritunjaikr@aditi.com");
		PageBase.CustomerLookupPage().PopulateOrderLookUpDetails(firstName, lastName, "", mail);
		PageBase.CommonControls().continueButtonDVA.click();

		//Select a Particular Order
		//TODO : Take from QA-62 testcase
		driver.navigate().to(readConfig("CustomerVerification") + QA62orderId);

		//Enter the Device Verification Details
		Utilities.waitForElementVisible(PageBase.ReturnOrExchangeVerificationPage().cvFirstNameTextbox);
		//PageBase.ReturnOrExchangeVerificationPage().populatingPage(DRIVERLICENCE, "CA", "123451789", "Mritunjai", "", "Rai");
		PageBase.ReturnOrExchangeVerificationPage().populatingPage(DRIVERLICENCE, state, IDNumber, firstName, "", lastName);
		PageBase.CommonControls().continueCommonButton.click();

		//Enter the ESN/EMEI number for the device to be exchanged
		Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().esnIemeidTextbox);
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(returnDeviceIEMI);//"9886886108"//Todo: do be taken from previous test case CSV
		PageBase.CommonControls().continueButtonDVA.click();

		//Select the Precondictions
		Utilities.waitForElementVisible(PageBase.ReturnOrExhangePreConditions().deviceAccessoryRadioButton);
		PageBase.ReturnOrExhangePreConditions().SelectPreconditions();
		PageBase.ReturnOrExhangePreConditions().continueREVButton.click();

		//Select more Details
		Utilities.waitForElementVisible(PageBase.ReturningProcessPage().acceptReturnExchangeRadioButton);
		PageBase.ReturningProcessPage().acceptReturnExchangeRadioButton.click();
		PageBase.ReturningProcessPage().exchangeDeviceRadioButton.click();
		PageBase.ReturningProcessPage().SelectFinancingOption(FinancingOption.NOFINANCING);
		PageBase.ReturningProcessPage().SelectExchangeReason(ExchangeReason.INPOLICYGUESTRETURN);

		//Enter the new Device ESN/IEMEI
		Utilities.waitForElementVisible(PageBase.ReturningProcessPage().returnDeviceESNTextbox);
		PageBase.ReturningProcessPage().returnDeviceESNTextbox.sendKeys(esnNo);//"88007700440033001"
		PageBase.ReturningProcessPage().submitFormButton.click();
		PageBase.ReturningProcessPage().SelectExchangeReason(ExchangeReason.INPOLICYGUESTRETURN);
		PageBase.CommonControls().continueButtonDVA.click();

		//Select a Feature
		PageBase.VerizonSelectPlanFeaturesPage().clickContinue();


		Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
		PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
		PageBase.CommonControls().continueCommonButton.click();

		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		PageBase.CommonControls().continueCommonButton.click();

		Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
		String orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
		System.out.println("OrderID" + orderId);
		PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();
		//PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

		Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().sameAddressTab);
		String url = driver.getCurrentUrl();
		if (url.contains("payment")) {
			PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA);
			PageBase.PaymentRequiredPage().sameAddressTab.click();
			PageBase.PaymentRequiredPage().continuePRButton.click();
		}

		Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
		PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);//"132710003003669460"
		PageBase.PaymentVerificationPage().submitButton.click();

		Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
		PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo);  //"88007700440033001"
		PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
		PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber);//"21212121212121212121"
		PageBase.DeviceVerificationaandActivation().passwordInventoryTextbox.sendKeys("HELLO");// ToDo: Read from prev TC.
		PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

		Utilities.waitForElementVisible(PageBase.OrderReceiptPage().orderCompletionText);
		PageBase.OrderReceiptPage().verifyOrderCompletionPage();
		PageBase.CSVOperations().UpdateIMEIStatusToUsed(esnNo, FileName.SamsungGalaxyS4_16GBBlack);
		//driver.findElement(By.xpath("//span[contains(text(),'"+orderId+"']"));

		//check for device status in Inventory
		PageBase.InventoryManagementPage().launchInventoryInNewTab();
		PageBase.InventoryManagementPage().verifyDeviceStatus(esnNo, Constants.SOLD);//"88007700440033001"
		return orderId;
	}

	private void QA_59_VerifyInShipAdmin(String orderId) {
		//Check order Status in ShipAdmin
		ShipAdminBaseClass.launchShipAdminInNewTab();
		PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
		String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
		String status = PageBase.OrderSummaryPage().getOrderStatus();
		Assert.assertEquals(status, Constants.SHIPPED);
		Assert.assertTrue(eventLogTableContent.contains(Constants.EXCHANGE_ORDER_CREATED));
	}
//endregion QA 59

	//region QA 51
	@Test(groups={"verizon"})
	@Parameters("test-type")
	public void QA_51_VerizonNonEdgeExchangeLikeForUnlike(@Optional String testtype) throws IOException, 
	InterruptedException, AWTException {		

		DependantTestCaseInputs dependantValues = Utilities.ReadFromCSV("QA_50");
		if(dependantValues.TC_ID == "" ||dependantValues.ORDER_ID==""){
			AddNewLineTests addNewLine = new AddNewLineTests();
			addNewLine.QA50_VerizonNonEdgeWithNumberPortCCInCA(BrowserSettings.readConfig("test-type"));
			dependantValues = Utilities.ReadFromCSV("QA_50");
			if(dependantValues.TC_ID == "" ||dependantValues.ORDER_ID==""){
				Reporter.log("<br> Data is not available from QA_50");
				Assert.fail("Data is not available from QA_50");
			}
		}
		String ChildOrderId = null;
		String newIMEI1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBWhite);
		String newIMEI2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBWhite);
		String simType1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);
		String simType2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);
		String testType = BrowserSettings.readConfig("test-type");
		Reporter.log("QA_51_VerizonNonEdgeLikeForUnlike Starts." );
		Reporter.log("<br>Launching Browser");
		Log.startTestCase("QA_51_VerizonNonEdgeExchange_LikeForUnlike");

		if(testType.equals("internal"))
		{
			AdminBaseClass adminBaseClass = new AdminBaseClass();
			adminBaseClass.launchAdminInNewTab();
			PageBase.AdminPage().navigateToSimulator();

			if(readConfig("internalTestType").toLowerCase().equals("carrierresponder")){
				Reporter.log("Using Carrier Responder  <br> <p>", true);
				PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
				PageBase.AdminPage().selectAPIConfig("Verizon");
				Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
				PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
				Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
				PageBase.CarrierResponseXMLPage().selectOptions("current", 
						"returnOrExchangeDevice", "default_exchange.xml");
				Reporter.log("<br>XML Used: returnOrExchangeDevice - default_exchange.xml", true);
				Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
				PageBase.CarrierResponseXMLPage().saveResponseButton.click();
				Reporter.log("<br>Carrier Responder Changes Done!!!", true);
			}
		}
		else   //External
		{
			AdminBaseClass adminBaseClass = new AdminBaseClass();
			adminBaseClass.launchAdminInNewTab();

			PageBase.AdminPage().navigateToSimulator();
			PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
			Reporter.log("<br><U>Pointing to External Server</U>");
		}

		String receiptId = dependantValues.RECEIPT_ID;

		Utilities.switchPreviousTab();
		//Calling DBError utility to  find initial count or error in log files.
		DBError.navigateDBErrorPage();
		int initialCount= PageBase.AdminPage().totalErrorCount();

		Utilities.switchPreviousTab();
		PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
				Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));
		Reporter.log("Returning the device using Receipt ID: ");
		PageBase.HomePageRetail().newGuestButton.click();
		PageBase.HomePageRetail().guestLookupTab.click();
		PageBase.CustomerLookupPage().receiptIdTextbox.sendKeys(receiptId);
		Reporter.log(receiptId);

		List<WebElement> SubmitButtons = driver.findElements(By.xpath(ControlLocators.SUBMIT_RECEIPTID));
		for(WebElement visibleSubmitButton: SubmitButtons){
			if(visibleSubmitButton.isDisplayed())
			{visibleSubmitButton.click(); break; }
		}

		PageBase.CarrierCreditCheckPage().firstNameTextBox.sendKeys(dependantValues.FIRST_NAME);
		PageBase.CarrierCreditCheckPage().lastNameTextBox.sendKeys(dependantValues.LAST_NAME);
		Reporter.log("<br>First Name: "+ dependantValues.FIRST_NAME);
		Reporter.log("  Last Name: "+ dependantValues.LAST_NAME +"; ");
		SelectLicense(dependantValues.ID_TYPE, PageBase.ReturnOrExchangeVerificationPage().idTypeDropdown);
		Reporter.log("State: "+ dependantValues.STATE +"; ");
		Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().stateDropdown,
				SelectDropMethod.SELECTBYTEXT, dependantValues.STATE);
		Reporter.log("ID Type: "+ dependantValues.ID_TYPE +"; ");
		PageBase.ReturnOrExchangeVerificationPage().idNumberTextbox.sendKeys(dependantValues.ID_NUMBER);
		Reporter.log("ID Number: "+ dependantValues.ID_NUMBER +".");
		PageBase.CommonControls().continueCommonButton.click();
		Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().esnIemeidTextbox);
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.click();
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.clear();
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(dependantValues.ESN_IMEI1);
		PageBase.ReturnOrExchangeVerificationPage().continueEXCHANGE.click();

		SelectPreConditions();
		Reporter.log("<br>Pre Conditions Set for First Device");
		PageBase.ReturnOrExchangeVerificationPage().continuePRECONDITION.click();
		PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
		PageBase.ReturnOrExchangeVerificationPage().exchangeDEVICE.click();

		Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().financingOptIn, SelectDropMethod.SELECTBYINDEX, "2");
		Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().exchangeReasons, SelectDropMethod.SELECTBYINDEX, "1");
		WebElement newESNBox = driver.findElement(By.id("addDeviceId"));
		newESNBox.click();
		newESNBox.clear();
		newESNBox.sendKeys(newIMEI1);
		PageBase.ReturnOrExchangeVerificationPage().submitFormButton.click();
		Thread.sleep(2000);
		Utilities.waitForElementVisible(PageBase.ReturnOrExchangeVerificationPage().exchangeReasons);
		Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().exchangeReasons,
				SelectDropMethod.SELECTBYINDEX, "1");

		Utilities.waitForElementVisible(PageBase.ReturnOrExchangeVerificationPage().returnAnotherDevice);
		PageBase.ReturnOrExchangeVerificationPage().returnAnotherDevice.click();

		Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().esnIemeidTextbox);
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.click();
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.clear();
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(dependantValues.ESN_IMEI2);
		PageBase.ReturnOrExchangeVerificationPage().continueEXCHANGE.click();

		SelectPreConditions();
		Reporter.log("<br>Pre Conditions Set for Second Device");
		PageBase.ReturnOrExchangeVerificationPage().continuePRECONDITION.click();
		PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
		PageBase.ReturnOrExchangeVerificationPage().exchangeDEVICE.click();
		Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().financingOptIn, SelectDropMethod.SELECTBYINDEX, "2");

		Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().exchangeReasons, SelectDropMethod.SELECTBYINDEX, "1");
		newESNBox = driver.findElement(By.id("addDeviceId"));
		newESNBox.click();
		newESNBox.clear();
		newESNBox.sendKeys(newIMEI2);

		PageBase.ReturnOrExchangeVerificationPage().submitFormButton.click();
		Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().exchangeReasons,
				SelectDropMethod.SELECTBYINDEX, "1");
		PageBase.ReturnOrExchangeVerificationPage().continueEXCHANGE.click();
		PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();//driver.findElement(By.id("featureSubmit")).click();
		Thread.sleep(1000);

		Utilities.ScrollToElement(PageBase.ReturnOrExchangeVerificationPage().firstDeviceNoInsurance);
		Thread.sleep(500);

		PageBase.ReturnOrExchangeVerificationPage().firstDeviceNoInsurance.click();
		PageBase.ReturnOrExchangeVerificationPage().firstDeviceNoInsurance.click();
		Thread.sleep(500);

		PageBase.ReturnOrExchangeVerificationPage().secondDeviceNoInsurance.click();
		PageBase.ReturnOrExchangeVerificationPage().secondDeviceNoInsurance.click();

		driver.findElement(By.xpath(".//*[@id='guestReview']/div/div/label/span")).click();
		PageBase.CommonControls().continueButton.click();


		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		Utilities.ScrollToElement(PageBase.CommonControls().continueCommonButton);

		if(BrowserSettings.readConfig("Activation")=="true"){
			PageBase.CommonControls().continueCommonButton.click();


			if( driver.getCurrentUrl().contains("payment"))
			{
				PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA);
				Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
				Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
			}

			ChildOrderId= PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
			Reporter.log("Child Order Created!!!  <br> <p>" + ChildOrderId);
			PageBase.ReturnOrExchangeVerificationPage().continueExchangeMSSPage.click();
			PageBase.ReturnOrExchangeVerificationPage().continueExchangeMSSPage.click();
			Utilities.implicitWaitSleep(1000);
			PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();
			PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

			PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);
			PageBase.PaymentVerificationPage().submitButton.click();
			Reporter.log("Re-check Receipt ID - Successful <br> <p>");

			Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
			PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(newIMEI1);
			PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
			driver.findElement(By.id(PageBase.DeviceVerificationaandActivation().simTypeM + newIMEI1)).click();
			driver.findElement(By.id(PageBase.DeviceVerificationaandActivation().simTypeM + newIMEI1)).sendKeys(simType1);
			Utilities.ScrollToElement(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);

			PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(newIMEI2);
			PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
			driver.findElement(By.id(PageBase.DeviceVerificationaandActivation().simTypeM + newIMEI2)).click();
			driver.findElement(By.id(PageBase.DeviceVerificationaandActivation().simTypeM + newIMEI2)).sendKeys(simType2);
			PageBase.CommonControls().continueButtonDVA.click();
			Thread.sleep(30000);

			Utilities.waitForElementVisible(driver.findElement(By.xpath(".//*[@id='retailPage']/section/form/span/div/button")));
			String SupportCenterMessage = driver.findElement(By.xpath(".//*[@id='retailPage']/section/p")).getText();
			Reporter.log(" <br> <p> Support Center Message: " + SupportCenterMessage +" <br> <p>");
			String toCompare = Constants.SUPPORT_PAGE_MESSAGE + "#"+ChildOrderId+" when calling.";
			Reporter.log(toCompare + " <br> <p>");

			//Assert.assertTrue(SupportCenterMessage.contains(toCompare));
			ShipAdminBaseClass.launchShipAdminInNewTab();
			Reporter.log("Navigating to Parent Order: " + dependantValues.ORDER_ID +" <br> <p>");
			PageBase.OrderSummaryPage().goToOrderSummaryPage(dependantValues.ORDER_ID);
			String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(dependantValues.ORDER_ID);
			String parentOrderstatus = PageBase.OrderSummaryPage().getOrderStatus();
			Assert.assertEquals(parentOrderstatus, Constants.SHIPPED);
			Assert.assertEquals(PageBase.OrderSummaryPage().getChildOrderId(), ChildOrderId);
			Assert.assertTrue(eventLogTableContent.contains(ChildOrderId));

			PageBase.OrderSummaryPage().rOCHomeLink.click();
			Reporter.log("Navigating to Child Order: " + ChildOrderId +" <br> <p>");
			PageBase.OrderSummaryPage().goToOrderSummaryPage(ChildOrderId);
			eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(ChildOrderId);
			Assert.assertTrue(eventLogTableContent.contains("Old OrdId# " + dependantValues.ORDER_ID + ", Exchange order created"));
			String ChildOrderstatus = PageBase.OrderSummaryPage().getOrderStatus();
			Assert.assertTrue(ChildOrderstatus.contains("In-Store Activation"),"Checking for In-store activation String failed");
			Assert.assertEquals(PageBase.ReturnOrExchangeVerificationPage().parentOrderNumber.getText(),
					dependantValues.ORDER_ID,"Parent Order on Child Order is not the correct one. Check! <br> <p>");

			eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(ChildOrderId);
			PageBase.InventoryManagementPage().launchInventoryInNewTab();
			PageBase.InventoryManagementPage().verifyDeviceStatus(dependantValues.ESN_IMEI1, dependantValues.ESN_IMEI2, Constants.RETURN_TO_INVENTORY);
		}
		//DBError Verification
		DBError.navigateDBErrorPage();
		Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
	}
	//endregion QA 51

	//region QA 66
	@Test(groups={"verizon"})
	@Parameters("test-type")
	public void QA_66_VerizonNonEdgeExchangeLikeForLike(@Optional String testtype) throws IOException, InterruptedException{
		//String fromQA50[] = Utilities.ReadCSV("QA-50");
		String parentOrderId = "39720";
		boolean stopActivation = true;

		testtype = BrowserSettings.readConfig("test-type");
		try{

			Log.startTestCase("QA_66_VerizonNonEdgeExchange");

			carrierResponderSetupQA66(testtype);

			// Switching to previous tab.
			Utilities.switchPreviousTab();

			//Calling DBError utility to  find initial count or error in log files.
			DBError.navigateDBErrorPage();
			int initialCount= PageBase.AdminPage().totalErrorCount();

			// Switching to previous tab.
			Utilities.switchPreviousTab();

			poaCompleteFlowQA66(iMEINumber1, stopActivation, testtype);

			ShipAdminBaseClass.launchShipAdminInNewTab();
			Reporter.log("Navigating to Parent Order: " + parentOrderId + " <br> <p>");
			PageBase.OrderSummaryPage().goToOrderSummaryPage(parentOrderId);
			String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(parentOrderId);
			String parentOrderstatus = PageBase.OrderSummaryPage().getOrderStatus();
			Assert.assertEquals(parentOrderstatus, Constants.SHIPPED);
			Assert.assertEquals(PageBase.OrderSummaryPage().getChildOrderId(), ChildOrderId);
			Assert.assertTrue(eventLogTableContent.contains(ChildOrderId));

			PageBase.OrderSummaryPage().rOCHomeLink.click();
			Reporter.log("Navigating to Child Order: " + ChildOrderId + " <br> <p>");
			PageBase.OrderSummaryPage().goToOrderSummaryPage(ChildOrderId);
			eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(ChildOrderId);
			Assert.assertTrue(eventLogTableContent.contains("Old OrdId# " + parentOrderId + ", Exchange order created"));
			String ChildOrderstatus = PageBase.OrderSummaryPage().getOrderStatus();

			Assert.assertTrue(ChildOrderstatus.contains("In-Store Activation"),"Checking for In-store activation String failed");

			Assert.assertEquals(PageBase.ReturnOrExchangeVerificationPage().
					parentOrderNumber.getText(), parentOrderId, "Parent Order on Child Order is not the correct one. Check! <br> <p>");

			eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(ChildOrderId);
			String status = PageBase.OrderSummaryPage().getOrderStatus();
			PageBase.InventoryManagementPage().launchInventoryInNewTab();
			PageBase.InventoryManagementPage().verifyDeviceStatus("99000342701028", Constants.RETURN_TO_INVENTORY);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Log.error(ex.getMessage());
			//Utilities.driverTakesScreenshot();
			Log.error(ex.getMessage());
			Utilities.driverTakesScreenshot("QA_66_VerizonNonEdgeExchangeLikeForLike");
		}
	}

	//endregion QA 66

	//region QA 5411
	@Test(groups = {"verizon"})
	@Parameters("test-type")
	public void QA_5411_VerizonEdgeUpExchangeToVerizonNonEdge(@Optional String testType) throws IOException, InterruptedException {
		DependantTestCaseInputs dependantValues = Utilities.ReadFromCSV("QA_5355");
		String orderId = "";

		String newPhoneNumberForUpgrade = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.NewPhoneNumber);
		String newIEMIForUpgrade = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBWhite);

		try {
			Log.startTestCase("QA_5411_VerizonEdgeUpExchangeToVerizonNonEdge");
			testType = BrowserSettings.readConfig("test-type");

			if (testType.equals("internal")) {
				//Setting up Backend Simulator.
				setBackendSimulator(dependantValues.PhNumber);

			} else  //External
			{
				AdminBaseClass adminBaseClass = new AdminBaseClass();
				adminBaseClass.launchAdminInNewTab();

				PageBase.AdminPage().navigateToSimulator();
				PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
			}
			// Switching to Retail tab.
			Utilities.switchPreviousTab();

			//Login to POA
			PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
					Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

			// Returning the device.
			returnFlow(dependantValues.ESN_IMEI1, dependantValues.FIRST_NAME, dependantValues.LAST_NAME,
					dependantValues.PhNumber, dependantValues.STATE, dependantValues.ORDER_ID);

			//Setting Up Carrier Responder for Exchange/Upgrade flow.
			if (testType.equals("internal")) {
				//setUpCarrierResponder(phoneNumber);
				setUpBackendSimulatorForUpgradeFlow(newPhoneNumberForUpgrade);
			}

			// Switching to Retail tab.
			Utilities.switchPreviousTab();

			//-------------Upgrade Flow-----
			Utilities.waitForElementVisible(PageBase.HomePageRetail().newGuestButton);
			PageBase.HomePageRetail().newGuestButton.click();
			System.out.println("After Exchange");
			Utilities.waitForElementVisible(PageBase.HomePageRetail().upgradeEligibilityCheckerLink);
			PageBase.HomePageRetail().upgradeEligibilityCheckerLink.click();
			upgradeFlowNonEdge(dependantValues.PhNumber, newPhoneNumberForUpgrade, newIEMIForUpgrade,
					dependantValues.EMAIL, dependantValues.ID_TYPE, dependantValues.STATE,
					dependantValues.ID_NUMBER, dependantValues.FIRST_NAME, dependantValues.LAST_NAME,
					dependantValues.Zip, dependantValues.SSN, dependantValues.Month, dependantValues.Year);
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.error(ex.getMessage());
			Utilities.driverTakesScreenshot("QA_5411_VerizonEdgeUpExchangeToVerizonNonEdge");
		}
	}

	//endregion

	//region QA 5412
	@Test(groups = {"verizon"})
	@Parameters("test-type")
	public void QA_5412_VerizonEdgeUpExchangeToVerizonEdge(@Optional String testType) throws IOException, InterruptedException {
		DependantTestCaseInputs dependantValues = Utilities.ReadFromCSV("QA_5355");
		String orderId = "";
		String newPhoneNumberForUpgrade = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.NewPhoneNumber);
		String newIEMIForUpgrade = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBWhite);

		try {
			Log.startTestCase("QA_5412_VerizonEdgeUpExchangeToVerizonEdge");
			testType = BrowserSettings.readConfig("test-type");

			if (testType.equals("internal")) {
				//Setting up Backend Simulator.
				setBackendSimulator(dependantValues.PhNumber);
			} else  //External
			{
				AdminBaseClass adminBaseClass = new AdminBaseClass();
				adminBaseClass.launchAdminInNewTab();

				PageBase.AdminPage().navigateToSimulator();
				PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
			}
			// Switching to Retail tab.
			Utilities.switchPreviousTab();

			//Calling DBError utility to  find initial count or error in log files.
			DBError.navigateDBErrorPage();
			int initialCount= PageBase.AdminPage().totalErrorCount();

			// Switching to previous tab.
			//Utilities.switchPreviousTab();

			//Login to POA
			PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"),
					Utilities.getCredentials("storeId2766"));

			// Returning the device.
			returnFlow(dependantValues.ESN_IMEI1,dependantValues.FIRST_NAME,dependantValues.LAST_NAME,
					dependantValues.PhNumber,dependantValues.STATE,dependantValues.ORDER_ID);

			//Setting Up Carrier Responder for Exchange/Upgrade flow.
			if (testType.equals("internal")) {
				//setUpCarrierResponder(phoneNumber);
				setUpBackendSimulatorForUpgradeFlow(newPhoneNumberForUpgrade);
			}

			// Switching to Retail tab.
			Utilities.switchPreviousTab();

			//Exchange flow with inventory and shipadmin verification.
			Utilities.waitForElementVisible(PageBase.HomePageRetail().newGuestButton);
			PageBase.HomePageRetail().newGuestButton.click();
			System.out.println("After Exchange");
			Utilities.waitForElementVisible(PageBase.HomePageRetail().upgradeEligibilityCheckerLink);
			PageBase.HomePageRetail().upgradeEligibilityCheckerLink.click();
			upgradeFlowEdge(dependantValues.PhNumber, newPhoneNumberForUpgrade, newIEMIForUpgrade,
					dependantValues.Zip, dependantValues.SSN);

			//DBError Verification.
			DBError.navigateDBErrorPage();
			//Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));			

			Reporter.log("<h3>QA_5412_VerizonEdgeUpExchangeToVerizonEdge - Test Case Completes<h3>");
			Log.endTestCase("QA_5412_VerizonEdgeUpExchangeToVerizonEdge");
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.error(ex.getMessage());
			Utilities.driverTakesScreenshot("QA_5412_VerizonEdgeUpExchangeToVerizonEdge");
		}
	}
	//endregion QA 5412

	//region QA 77
	@Test(groups = {"verizon"})
	@Parameters("test-type")
	public void QA_77_VerizonEdgeUpgardeExchangetoEdge(@Optional String testtype) throws InterruptedException, AWTException, IOException {

		String testType = BrowserSettings.readConfig("test-type");
		CreditCardDetails creditCard = new CreditCardDetails();
		creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);
		String phoneNumberLine1 = "";
		String receiptId = "";
		String iMEINumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.iPhone5C);
		String simType1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);
		AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(FileName.VerizonEdgeUpgrade);
		CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
		String phoneNumber = accountDetails.MTN;
		String sSN = accountDetails.SSN;
		String zipCode = customerDetails.Zip;
		String accountPassword = accountDetails.Password;

		if (testType.equals("internal")) {
			// Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
			AdminBaseClass adminBaseClass = new AdminBaseClass();
			adminBaseClass.launchAdminInNewTab();

			PageBase.AdminPage().navigateToSimulator();
			PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));
		} else  //External
		{
			AdminBaseClass adminBaseClass = new AdminBaseClass();
			adminBaseClass.launchAdminInNewTab();

			PageBase.AdminPage().navigateToSimulator();
			PageBase.AdminPage().selectWebAPIResponse(carrierType, "External");
		}
		// Switching to Retail tab.
		Utilities.switchPreviousTab();

		String orderId = QA_77_UpgradeFlowEdge(phoneNumber, iMEINumber1, sSN, zipCode);

		//PageBase.LoginPageRetail().LaunchNewPOATab();
		PageBase.HomePageRetail().newGuestButton.click();

        returnFlowQA77(testType, orderId, customerDetails.State, iMEINumber1, GuestVerificationPage.IdType.valueOf(customerDetails.IDType),
				customerDetails.IDNumber, customerDetails.FirstName, customerDetails.LastName);
		//-------------Upgrade Flow-----
		// To do
		// cannot do upgrade with phone number of existing order
	}
	//endregion QA 77


	//endregion Test Methods

	//region Helper and refactoring codes
	private void setUpBackendSimulatorForUpgradeFlow(String phoneNumber) throws InterruptedException, AWTException, IOException {
		AdminBaseClass adminBaseClass = new AdminBaseClass();
		adminBaseClass.launchAdminInNewTab();

		PageBase.AdminPage().navigateToSimulator();
		PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("backendSimulator"));
		PageBase.AdminPage().selectAPIConfig(carrierType);
		PageBase.AdminPage().selectCreaditReadUseCase("APPROVED_EDGEUP");
		PageBase.AdminPage().retrieveCustomerDetails("ELIGIBLE");
		PageBase.AdminPage().accountPlanType("Family Share");
		PageBase.AdminPage().retrieveExistingCustomerInstallmentsDetails("SUCCESS_NO_PAYMENT");
		PageBase.AdminPage().retrievePricePlan("SUCCESS");
		PageBase.AdminPage().submitActivation("SUCCESS");
		PageBase.AdminPage().submitCreditApplication("APPROVED");
		PageBase.AdminPage().submitReciept("SUCCESS");
		PageBase.AdminPage().submitServiceDetails("SUCCESS");
		PageBase.AdminPage().submitEdgeUpPayment("SUCCESS");
		PageBase.AdminPage().returnOrExchangeDevice("SUCCESS");
		List<WebElement> phoneList =PageBase.AdminPage().phoneList.findElements(By.className("phoneNumberRow"));
		if(phoneList.size()>0)
		{
			if (phoneList.size()>1)
			{
				List<WebElement> removePhone =PageBase.AdminPage().phoneList.findElements(By.id("retrieveCustomerDetails_removePhoneNumberRowButton"));
				for (int i=0;i<removePhone.size()-1;i++)
				{
					removePhone.get(i).click();
				}
				PageBase.AdminPage().firstPhoneTextBox.clear();
				PageBase.AdminPage().firstPhoneTextBox.sendKeys(phoneNumber);
			}
			else
			{
				PageBase.AdminPage().firstPhoneTextBox.clear();
				PageBase.AdminPage().firstPhoneTextBox.sendKeys(phoneNumber);
			}
		}
		else {
			PageBase.AdminPage().addPhoneNumbers.click();
			PageBase.AdminPage().firstPhoneTextBox.clear();
			PageBase.AdminPage().firstPhoneTextBox.sendKeys(phoneNumber);
		}
		Utilities.implicitWaitSleep(2000);
		PageBase.AdminPage().save();
	}

	private void returnFlow(String deviceIEMI,String firstName,String lastName,String phoneNumber,String stateCode,String parentOrderId) throws IOException {
		String orderId;
		CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(DRIVERLICENCE);
		Utilities.waitForElementVisible(PageBase.HomePageRetail().guestLookupTab);
		PageBase.HomePageRetail().guestLookupTab.click();

		//Enter the Customer Details
		Utilities.waitForElementVisible(PageBase.CustomerLookupPage().orderLookupTab);
		PageBase.CustomerLookupPage().PopulateOrderLookUpDetails(firstName, lastName, phoneNumber, "");
		PageBase.CommonControls().continueButtonDVA.click();

		//Select a Particular Order
		driver.navigate().to(readConfig("CustomerVerification") + parentOrderId);

		PageBase.GuestVerificationPage().populateGuestVerificationDetails(GuestVerificationPage.IdType.DRIVERLICENCE, stateCode, customerDetails.IDNumber, firstName, lastName);
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.click();
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.clear();
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(deviceIEMI);
		PageBase.ReturnOrExchangeVerificationPage().continueEXCHANGE.click();

		SelectPreConditions();

		PageBase.ReturnOrExchangeVerificationPage().continuePRECONDITION.click();
		PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
		PageBase.ReturnOrExchangeVerificationPage().returnDEVICE.click();

		Utilities.waitForElementVisible(PageBase.ReturnOrExchangeVerificationPage().returnReasons);
		Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().returnReasons, SelectDropMethod.SELECTBYINDEX, "2");
		PageBase.ReturnOrExchangeVerificationPage().continueEXCHANGE.click();

		//Verizon Account Password.
		Utilities.waitForElementVisible(PageBase.AccountPasswordPage().accountPassword);
		PageBase.AccountPasswordPage().accountPassword.sendKeys("HELLO");
		PageBase.AccountPasswordPage().continueButton.click();


		if(driver.getCurrentUrl().contains("printticket"))
		{
			// Print Return MSS sheet.
			Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText);
			orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
			PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

			//Returns Confirmation Page.
			Utilities.waitForElementVisible(PageBase.ReturnConfirmation().returnConfirmation);
			Assert.assertTrue(PageBase.ReturnConfirmation().returnConfirmation.isDisplayed());
			Assert.assertTrue(PageBase.ReturnConfirmation().successfullyReturnedString.isDisplayed());
			Assert.assertTrue(PageBase.ReturnConfirmation().linePhonenoString.isDisplayed());
			PageBase.ReturnConfirmation().returnHome.click();
		}

		Utilities.implicitWaitSleep(6000);
		//Utilities.waitForElementVisible(driver.findElement(By.xpath("//button[contains(text(),'continue')]")));
		//Assert.assertTrue(driver.findElement(By.xpath("//h2[contains(text(), 'Support Center')]")).isDisplayed());

		//Deactivate via ShipAdmin
		ShipAdminBaseClass.launchShipAdminInNewTab();
		Utilities.implicitWaitSleep(5000);
		PageBase.OrderSummaryPage().SelectActivationInfo(parentOrderId);
		Utilities.switchPreviousTab();
	}

	private void upgradeFlowNonEdge(String accountPhoneNumber,String phoneNumber,String iemi,String mailId,String idType,String state,String idNumber,String firstName,String lastName,String zipCode,String ssn,String month,String year) throws InterruptedException, AWTException, IOException {
		CreditCardDetails creditCard = new CreditCardDetails();
		creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);

		//UEC Verification Page
		Utilities.waitForElementVisible(PageBase.UECVerificationPage().verizonTab);
		PageBase.UECVerificationPage().verizonTab.click();
		PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys(accountPhoneNumber);
		if(ssn.length()>4) {
			PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys(ssn.substring(5, ssn.length()));
		}
		else
		{
			PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys(ssn);
		}
		PageBase.UECVerificationPage().accountPasswordVerizonTextbox.sendKeys("");
		PageBase.UECVerificationPage().accountZipcodeVerizonTextbox.sendKeys(zipCode);
		PageBase.UECVerificationPage().continueVerizonButton.click();

		//UEC Add Lines Page
		Utilities.waitForElementVisible(PageBase.UECAddLinesPage().firstAALCheckbox);
		Assert.assertTrue(PageBase.UECAddLinesPage().eligibleForDF.getText().contains("Eligible for Device Finance\nYes"));
		//	Assert.assertTrue(UECAddLinesPage().eligibleFor2Year.getText().contains("Eligible for 2-Year\nYes"));
		//	Assert.assertTrue(UECAddLinesPage().transferEligible.getText().contains("Transfer Eligible\nYes"));
		PageBase.UECAddLinesPage().clickCheckboxForParticularPhoneNumber(phoneNumber);
		PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

		//Device Scan Page
		Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
		PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iemi);
		PageBase.DeviceScanPage().submitDeviceButton.click();

		//Verizon Non Edge Page
		Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
		PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

		//Verizon Shop Plans Page
		Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().VerizonMoreEverything);
		PageBase.VerizonShopPlansPage().VerizonMoreEverything.click();

		//Cart Page
		Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
		Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, SelectDropMethod.SELECTBYINDEX, "1");
		String phonePrice = PageBase.CartPage().firstPhonePriceText.getText();
		String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
		PageBase.CartPage().continueCartButton.click();

		//Verizon Select Plan Features Page
		Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
		//PageBase.VerizonSelectPlanFeaturesPage().basicPhoneDiscount0Checkbox.click();
		PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

		//Select Protection Plan Insurance Page
		Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
		PageBase.CommonControls().continueButton.click();

		//Service Provider Verification Page
		Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().firstNameTextbox);

		PageBase.ServiceProviderVerificationPage().populatingSPVPage(firstName, lastName,
				mailId,IdType.valueOf(idType), state, idNumber, month, Integer.parseInt(year));
		PageBase.ServiceProviderVerificationPage().guestCreditCheckCheckbox.click();
		PageBase.ServiceProviderVerificationPage().continueSPVButton.click();

		/* Order Review and Confirm Page */
		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().phonePriceLine1.isDisplayed());
		//Assert.assertEquals(OrderReviewAndConfirmPage().phoneMonthlyFee.getText(), phonePrice);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().existingPlanDiv.isDisplayed());
		String existingPlan = PageBase.OrderReviewAndConfirmPage().existingPlanDiv.getText();
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().twoYearsUpgradeLabel.isDisplayed());
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().upgradeFeeValueText.isDisplayed());
		String upgradeFee = PageBase.OrderReviewAndConfirmPage().upgradeFeeValueText.getText();
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
		String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
		PageBase.CommonControls().continueCommonButton.click();

		//Terms & Conditions Page
		Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox);
		PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
		PageBase.TermsandConditionsPage().continueTCButton.click();

		//Payment Required Page
		Utilities.implicitWaitSleep(8000);
		String url = driver.getCurrentUrl();
		if (url.contains("payment")) {
			Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().sameAddressTab);
			PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.valueOf(creditCard.CardType.toUpperCase()));
			Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
			Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
		}

		//Print Mobile Scan Sheet Page
		Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
		Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phoneModelText.getText(), phoneModel);
		Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice);
		String orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
		PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

		//Payment Verification Page
		Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
		PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId));
		PageBase.PaymentVerificationPage().submitButton.click();

		//Device Verification and Activation Page

		try {
			PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
			PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys(creditCard.CVV);
		} catch (Exception e) {
		}
		PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

		//Wireless Customer Agreement Page
		Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox, 120);
		PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox.click();
		PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
		if (readConfig("Activation").contains("true")) {
			PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

			//Order Activation and Complete Page
			Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
			Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
			Assert.assertEquals(PageBase.OrderActivationCompletePage().phoneNumberValueText.getText(),
					CommonFunction.getFormattedPhoneNumber(phoneNumber));
			Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), iemi);
			Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValueText.getText(), phonePrice);
			Assert.assertEquals(PageBase.OrderActivationCompletePage().subtotalValueText.getText(), totalDue);

			//Ship Admin
			ShipAdminBaseClass.launchShipAdminInNewTab();
			PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
			String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
			String status = PageBase.OrderSummaryPage().getOrderStatus();
			Assert.assertEquals(status, Constants.SHIPPED);
			Assert.assertTrue(eventLogTableContent.contains(Constants.RECEIPT_SUBMISSION_SUCCEEDED_COMMENT));
			Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
			Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_SUCCEEDED_COMMENT));
			Assert.assertTrue(eventLogTableContent.contains(Constants.PARKING_SUCCEEDED_COMMENT));
			Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
			Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
			Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
			Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.HANDSET_UPGRADE));
			//Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.EXISTING_ACCOUNT_HOLDER));
			Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));

			//Inventory Management
			PageBase.InventoryManagementPage().launchInventoryInNewTab();
			PageBase.InventoryManagementPage().verifyDeviceStatus(iemi, Constants.SOLD);
		}
	}

	private void upgradeFlowEdge(String accountPhoneNumber,String phoneNumber,String iemi,String zipCode,String ssn) throws InterruptedException, AWTException, IOException {
		CreditCardDetails creditCard = new CreditCardDetails();
		creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);

		//UEC Verification Page
		Utilities.waitForElementVisible(PageBase.UECVerificationPage().verizonTab);
		PageBase.UECVerificationPage().verizonTab.click();
		PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys(accountPhoneNumber);
		if(ssn.length()>4) {
			PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys(ssn.substring(5, ssn.length()));
		}
		else
		{
			PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys(ssn);
		}
		PageBase.UECVerificationPage().accountPasswordVerizonTextbox.sendKeys("");
		PageBase.UECVerificationPage().accountZipcodeVerizonTextbox.sendKeys(zipCode);
		PageBase.UECVerificationPage().continueVerizonButton.click();

		//UEC Add Lines Page
		Utilities.waitForElementVisible(PageBase.UECAddLinesPage().firstAALCheckbox);
		Assert.assertTrue(PageBase.UECAddLinesPage().eligibleForDF.getText().contains("Eligible for Device Finance\nYes"));
		PageBase.UECAddLinesPage().clickCheckboxForParticularPhoneNumber(phoneNumber);
		PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

		//Device Scan Page
		Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
		PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iemi);
		PageBase.DeviceScanPage().submitDeviceButton.click();

		//Verizon Edge Page
		Utilities.waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
		PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();

		//Filling information in Carrier Credit Check Page.
		Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().populateForm);
		CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
		PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
		PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
		PageBase.CommonControls().continueButton.click();
		//PageBase.CommonControls().continueButton.click();

		// Installment Page
		Utilities.waitForElementVisible(PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton);
		PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton.click();
		PageBase.CommonControls().continueCommonButton.click();

		//Verizon Shop Plans Page
		Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().VerizonMoreEverything);
		PageBase.VerizonShopPlansPage().VerizonMoreEverything.click();

		//Cart Page
		Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
		Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, SelectDropMethod.SELECTBYINDEX, "1");
		String phonePrice = PageBase.CartPage().firstPhonePriceText.getText();
		String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
		PageBase.CartPage().continueCartButton.click();

		//Verizon Select Plan Features Page
		Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
		//PageBase.VerizonSelectPlanFeaturesPage().basicPhoneDiscount0Checkbox.click();
		PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

		//Select Protection Plan Insurance Page
		Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
		PageBase.CommonControls().continueButton.click();

		/*Order Review and Confirm Page */
		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().phonePriceLine1.isDisplayed());
		//Assert.assertEquals(OrderReviewAndConfirmPage().phoneMonthlyFee.getText(), phonePrice);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().existingPlanDiv.isDisplayed());
		String existingPlan = PageBase.OrderReviewAndConfirmPage().existingPlanDiv.getText();
		//Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().twoYearsUpgradeLabel.isDisplayed());
		//Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().upgradeFeeValueText.isDisplayed());
		String upgradeFee = PageBase.OrderReviewAndConfirmPage().upgradeFeeValueText.getText();
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
		String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
		PageBase.CommonControls().continueCommonButton.click();

		//Terms & Conditions Page
		Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox);
		PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
		PageBase.TermsandConditionsPage().continueTCButton.click();

		//Payment Required Page
		Utilities.implicitWaitSleep(8000);
		String url = driver.getCurrentUrl();
		if (url.contains("payment")) {
			Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().sameAddressTab);
			PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.valueOf(creditCard.CardType.toUpperCase()));
			Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
			Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
		}

		//Print Mobile Scan Sheet Page
		Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
		//Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phoneModelText.getText(), phoneModel);
		//Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice);
		orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
		PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

		//Payment Verification Page
		Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
		PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId));
		PageBase.PaymentVerificationPage().submitButton.click();

		//Device Verification and Activation Page
		try {
			PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
			PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys(creditCard.CVV);
		} catch (Exception e) {
		}
		PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

		//Wireless Customer Agreement Page
		Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox, 120);
		PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox.click();
		PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
		if (readConfig("Activation").contains("true")) {
			PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

			//Device Financing Installment Contract.
			Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().print);
			PageBase.DeviceFinancingInstallmentContractPage().print.click();
			Utilities.implicitWaitSleep(3000);
			Robot robot = new Robot();
			Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
			Utilities.implicitWaitSleep(6000);
			Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
			PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox.click();
			PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
			PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();
			Utilities.implicitWaitSleep(2000);

			//Ship Admin
			ShipAdminBaseClass.launchShipAdminInNewTab();
			PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
			String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
			String status = PageBase.OrderSummaryPage().getOrderStatus();
			//Assert.assertEquals(status, Constants.SHIPPED);
			//Assert.assertTrue(eventLogTableContent.contains(Constants.RECEIPT_SUBMISSION_SUCCEEDED_COMMENT));
			//Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
			//Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_SUCCEEDED_COMMENT));
			Assert.assertTrue(eventLogTableContent.contains(Constants.PARKING_SUCCEEDED_COMMENT));
			Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
			//Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
			Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
			Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.HANDSET_UPGRADE));
			//Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.EXISTING_ACCOUNT_HOLDER));
			Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));

			//Inventory Management
			PageBase.InventoryManagementPage().launchInventoryInNewTab();
			PageBase.InventoryManagementPage().verifyDeviceStatus(iemi, Constants.SOLD);
		}
	}
	private void setUpCarrierResponder(String phoneNumber) throws InterruptedException, AWTException, IOException {
		String xmlContent1 = "";
		AdminBaseClass adminBaseClass = new AdminBaseClass();
		adminBaseClass.launchAdminInNewTab();
		PageBase.AdminPage().navigateToSimulator();
		Robot robot = new Robot();
		PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("carrierResponder"));
		PageBase.AdminPage().selectAPIConfig(carrierType);

		// Selecting Verizon and response xml.
		Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
		PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
		Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);

		PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCustomerDetails", "vwz_edge_up_eligible_not_payment_needed.xml");
		Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(5000);
		xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
		xmlContent1 = xmlContent1.replace(Constants.DEFAULT_XML_NUMBER_6567778895, phoneNumber);
		PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
		Utilities.copyPaste(xmlContent1, robot);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();

		PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveExistingCustomerInstallmentDetails", "eligible_no_payment_needed.xml");
		Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(5000);
		xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
		xmlContent1 = xmlContent1.replace(Constants.DEFAULT_XML_NUMBER_6567778895, phoneNumber);
		PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
		Utilities.copyPaste(xmlContent1, robot);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();

		PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "upgrade_approved_noedge.xml");
		Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(5000);
		xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
		xmlContent1 = xmlContent1.replace(Constants.DEFAULT_XML_NUMBER_4152647954, phoneNumber);
		PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
		Utilities.copyPaste(xmlContent1, robot);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();

		PageBase.CarrierResponseXMLPage().selectOptions("current", "retrievePricePlans", "plaid_v_1_line.xml");
		Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(5000);
		xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
		xmlContent1 = xmlContent1.replace(Constants.DEFAULT_XML_NUMBER_4152647954, phoneNumber);
		PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
		Utilities.copyPaste(xmlContent1, robot);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();

		PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
		Utilities.implicitWaitSleep(2000);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();

		PageBase.CarrierResponseXMLPage().selectOptions("current", "submitReceipt", "default.xml");
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();

		PageBase.CarrierResponseXMLPage().selectOptions("current", "submitServiceDetails", "default.xml");
		Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(5000);
		xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
		xmlContent1 = xmlContent1.replace(Constants.DEFAULT_XML_NUMBER_4152647954, phoneNumber);
		PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
		Utilities.copyPaste(xmlContent1, robot);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();

		PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_1_line.xml");
		Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(5000);
		xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
		xmlContent1 = xmlContent1.replace(Constants.DEFAULT_XML_NUMBER_4152647954, phoneNumber);
		PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
		Utilities.copyPaste(xmlContent1, robot);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();

		PageBase.CarrierResponseXMLPage().selectOptions("current", "returnOrExchangeDevice", "default_exchange.xml");
		Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(5000);
		xmlContent1 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
		xmlContent1 = xmlContent1.replace(Constants.DEFAULT_XML_NUMBER_7325551212, phoneNumber);
		PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
		Utilities.copyPaste(xmlContent1, robot);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();

		Utilities.implicitWaitSleep(3000);
		Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();
	}

	private void setBackendSimulator(String phoneNumber) throws InterruptedException, AWTException, IOException {
		AdminBaseClass adminBaseClass = new AdminBaseClass();
		adminBaseClass.launchAdminInNewTab();

		PageBase.AdminPage().navigateToSimulator();
		PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("backendSimulator"));
		PageBase.AdminPage().selectAPIConfig(carrierType);
		PageBase.AdminPage().selectCreaditReadUseCase("APPROVED_EDGEUP");
		PageBase.AdminPage().retrieveCustomerDetails("VZW_EDGE_UP_ELIGIBLE");
		List<WebElement> phoneList =PageBase.AdminPage().phoneList.findElements(By.className("phoneNumberRow"));

		if(phoneList.size()>0)
		{
			if (phoneList.size()>1)
			{
				List<WebElement> removePhone =PageBase.AdminPage().phoneList.findElements(By.id("retrieveCustomerDetails_removePhoneNumberRowButton"));
				for (int i=0;i<removePhone.size()-1;i++)
				{
					removePhone.get(i).click();
				}
				PageBase.AdminPage().firstPhoneTextBox.clear();
				PageBase.AdminPage().firstPhoneTextBox.sendKeys(phoneNumber);
			}
			else
			{
				PageBase.AdminPage().firstPhoneTextBox.clear();
				PageBase.AdminPage().firstPhoneTextBox.sendKeys(phoneNumber);
			}
		}
		else {
			PageBase.AdminPage().addPhoneNumbers.click();
			PageBase.AdminPage().firstPhoneTextBox.clear();
			PageBase.AdminPage().firstPhoneTextBox.sendKeys(phoneNumber);
		}
		PageBase.AdminPage().accountPlanType("Individual");
		PageBase.AdminPage().retrieveExistingCustomerInstallmentsDetails("SUCCESS_WITH_PAYMENT");
		PageBase.AdminPage().retrievePricePlan("SUCCESS");
		PageBase.AdminPage().submitActivation("SUCCESS");
		PageBase.AdminPage().submitCreditApplication("APPROVED");
		PageBase.AdminPage().submitReciept("SUCCESS");
		PageBase.AdminPage().submitServiceDetails("SUCCESS");
		PageBase.AdminPage().submitEdgeUpPayment("SUCCESS");
		PageBase.AdminPage().returnOrExchangeDevice("SUCCESS");
		Utilities.implicitWaitSleep(2000);
		PageBase.AdminPage().save();
	}
	public void SelectPreConditions()
	{
		PageBase.ReturnOrExchangeVerificationPage().devicePowerOn.click();

		PageBase.ReturnOrExchangeVerificationPage().deviceConditionYes.click();

		PageBase.ReturnOrExchangeVerificationPage().deviceAccessoryNo.click();

		PageBase.ReturnOrExchangeVerificationPage().devicePackingNo.click();
	}
	private CarrierCreditCheckDetails getCarrierCreditCheckDetails() throws IOException {
		CarrierCreditCheckDetails cccDetails = new CarrierCreditCheckDetails();
		PageBase.CSVOperations();
		CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(DRIVERLICENCE);
		cccDetails.setFirstName(customerDetails.FirstName);
		cccDetails.setLastName(customerDetails.LastName);
		cccDetails.setAddress1(customerDetails.Address1);
		cccDetails.setCity(customerDetails.City);
		cccDetails.setState(customerDetails.State);
		cccDetails.setZip(customerDetails.Zip);
		cccDetails.setHomePhone(customerDetails.PhNum);
		cccDetails.setEmail(customerDetails.EMail);
		cccDetails.setBirthMonth(customerDetails.BirthdayMonth);
		cccDetails.setBirthDate(customerDetails.BirthdayDay);
		cccDetails.setBirthYear(customerDetails.BirthdayYear);
		cccDetails.setSSN(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SSNWithDeposit));
		cccDetails.setIDType(DRIVERLICENCE);
		cccDetails.setIdTypeState(customerDetails.IDState);
		cccDetails.setIdNumber(customerDetails.IDNumber);
		cccDetails.setMonth(customerDetails.IDExpirationMonth);
		cccDetails.setYear(customerDetails.IDExpirationYear);
		return cccDetails;
	}

	//region QA-77 Refactored Methods
	private String QA_77_UpgradeFlowEdge(String phoneNumber,String imei,String ssn, String zipCode) throws InterruptedException, AWTException, IOException {
		CreditCardDetails creditCard = new CreditCardDetails();
		creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);

		//Login
		PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"),
				Utilities.getCredentials("storeId0003"));
		PageBase.HomePageRetail().upgradeEligibilityCheckerLink.click();

		//UEC Verification Page
		Utilities.waitForElementVisible(PageBase.UECVerificationPage().verizonTab);
		PageBase.UECVerificationPage().verizonTab.click();
		PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys(phoneNumber);
		if(ssn.length()>4) {
			PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys(ssn.substring(5, ssn.length()));
		}
		else
		{
			PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys(ssn);
		}
		PageBase.UECVerificationPage().accountPasswordVerizonTextbox.sendKeys("");
		PageBase.UECVerificationPage().accountZipcodeVerizonTextbox.sendKeys(zipCode);
		PageBase.UECVerificationPage().continueVerizonButton.click();

		//UEC Add Lines Page
		Utilities.waitForElementVisible(PageBase.UECAddLinesPage().firstAALCheckbox);
		Assert.assertTrue(PageBase.UECAddLinesPage().eligibleForDF.getText().contains("Eligible for Device Finance\nYes"));
		PageBase.UECAddLinesPage().clickCheckboxForParticularPhoneNumber(phoneNumber);
		PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

		//Device Scan Page
		Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
		PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(imei);
		PageBase.DeviceScanPage().submitDeviceButton.click();

		//Verizon Edge Page
		Utilities.waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
		PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();

		//Filling information in Carrier Credit Check Page.
		Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().populateForm);
		CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
		PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
		PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
		PageBase.CommonControls().continueButton.click();
		//PageBase.CommonControls().continueButton.click();

		// Installment Page
		Utilities.waitForElementVisible(PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton);
		PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton.click();
		PageBase.CommonControls().continueCommonButton.click();

		//Verizon Shop Plans Page
		Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().VerizonMoreEverything);
		PageBase.VerizonShopPlansPage().VerizonMoreEverything.click();

		//Cart Page
		Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
		Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, SelectDropMethod.SELECTBYINDEX, "1");
		String phonePrice = PageBase.CartPage().firstPhonePriceText.getText();
		String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
		PageBase.CartPage().continueCartButton.click();

		//Verizon Select Plan Features Page
		Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
		//PageBase.VerizonSelectPlanFeaturesPage().basicPhoneDiscount0Checkbox.click();
		PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

		//Select Protection Plan Insurance Page
		Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
		PageBase.CommonControls().continueButton.click();

		/*Order Review and Confirm Page */
		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().phonePriceLine1.isDisplayed());
		//Assert.assertEquals(OrderReviewAndConfirmPage().phoneMonthlyFee.getText(), phonePrice);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().existingPlanDiv.isDisplayed());
		String existingPlan = PageBase.OrderReviewAndConfirmPage().existingPlanDiv.getText();
		//Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().twoYearsUpgradeLabel.isDisplayed());
		//Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().upgradeFeeValueText.isDisplayed());
		String upgradeFee = PageBase.OrderReviewAndConfirmPage().upgradeFeeValueText.getText();
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
		String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
		PageBase.CommonControls().continueCommonButton.click();

		//Terms & Conditions Page
		Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox);
		PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
		PageBase.TermsandConditionsPage().continueTCButton.click();

		//Payment Required Page
		Utilities.implicitWaitSleep(8000);
		String url = driver.getCurrentUrl();
		if (url.contains("payment")) {
			Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().sameAddressTab);
			PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.valueOf(creditCard.CardType.toUpperCase()));
			Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
			Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
		}

		//Print Mobile Scan Sheet Page
		Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
		//Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phoneModelText.getText(), phoneModel);
		//Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice);
		orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
		PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

		//Payment Verification Page
		Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
		PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId));
		PageBase.PaymentVerificationPage().submitButton.click();

		//Device Verification and Activation Page
		//PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(iemi);
		try {
			PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
			PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys(creditCard.CVV);
		} catch (Exception e) {
		}
		PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

		//Wireless Customer Agreement Page
		Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox, 120);
		PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox.click();
		PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
		if (readConfig("Activation").contains("true")) {
			PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

			//Device Financing Installment Contract.
			Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().print);
			PageBase.DeviceFinancingInstallmentContractPage().print.click();
			Utilities.implicitWaitSleep(3000);
			Robot robot = new Robot();
			Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
			Utilities.implicitWaitSleep(6000);
			Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
			PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox.click();
			PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
			PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();
			Utilities.implicitWaitSleep(2000);
		}
		return orderId;
	}

	private String returnFlowQA77(@Optional String testType, String parentOrderId, String state, String iMEINumber1,
									   GuestVerificationPage.IdType idType, String idNumber, String firstName, String lastName)
			throws IOException
	{
		//Login to retail page.
		PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
				Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

		PageBase.HomePageRetail().guestLookupTab.click();

		PageBase.CustomerLookupPage().viewGuestOrders.click();
		PageBase.CustomerLookupPage().continueButton.click();

		//Select a Particular Order
		driver.navigate().to(readConfig("CustomerVerification") + parentOrderId);

		Utilities.waitForElementVisible(PageBase.GuestVerificationPage().idTypeDropdown);
		PageBase.GuestVerificationPage().populateGuestVerificationDetails(idType, state,
				idNumber, firstName, lastName);

		Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().orderID);
		orderId = PageBase.ReturnScanDevicePage().orderID.getText();
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(iMEINumber1);
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
		return orderId;
	}
	//endregion

	//region QA-66 Refactored Methods Region
	private void carrierResponderSetupQA66(String testtype) throws AWTException,
	IOException, InterruptedException
	{
		testtype ="internal";
		if(testtype.equals("internal"))
		{
			AdminBaseClass adminBaseClass = new AdminBaseClass();
			adminBaseClass.launchAdminInNewTab();

			PageBase.AdminPage().navigateToSimulator();
			Utilities.implicitWaitSleep(5000);
			PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));
			PageBase.AdminPage().selectAPIConfig(carrierType);
		}
		else   //External
		{
			AdminBaseClass adminBaseClass = new AdminBaseClass();
			adminBaseClass.launchAdminInNewTab();

			PageBase.AdminPage().navigateToSimulator();
			PageBase.AdminPage().selectWebAPIResponse(carrierType,"External");
		}
	}

	private void poaCompleteFlowQA66(String iMEINumber1, boolean stopActivation,
			String testtype) throws AWTException,
			IOException, InterruptedException
	{
		String receiptId = "132710003008663237";
		PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
				Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

		PageBase.HomePageRetail().newGuestButton.click();
		PageBase.HomePageRetail().guestLookupTab.click();
		PageBase.CustomerLookupPage().receiptIdTextbox.sendKeys(receiptId);

		List<WebElement> SubmitButtons = driver.findElements(By.xpath(ControlLocators.SUBMIT_RECEIPTID));
		for(WebElement visibleSubmitButton: SubmitButtons){
			if(visibleSubmitButton.isDisplayed())
			{visibleSubmitButton.click(); break; }
		}
		/*Utilities.waitForElementVisible(PageBase.OrderHistory().completedLink);
			PageBase.OrderHistory().completedLink.click();*/
		PageBase.GuestVerificationPage().populateGuestVerificationDetails(GuestVerificationPage.IdType.STATEID,
				"CA", "579468249", "Frank", "Consumer Two");
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.click();
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.clear();
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(iMEINumber1);
		PageBase.ReturnOrExchangeVerificationPage().continueEXCHANGE.click();

		SelectPreConditions();

		PageBase.ReturnOrExchangeVerificationPage().continuePRECONDITION.click();
		PageBase.ReturnOrExchangeVerificationPage().proceedEXCHANGE.click();
		PageBase.ReturnOrExchangeVerificationPage().exchangeDEVICE.click();

		Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().financingOptIn, SelectDropMethod.SELECTBYINDEX, "2");
		Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().exchangeReasons, SelectDropMethod.SELECTBYINDEX, "1");
		Utilities.waitForElementVisible(PageBase.ReturnOrExchangeVerificationPage().returnDeviceTextBox);
		PageBase.ReturnOrExchangeVerificationPage().returnDeviceTextBox.click();
		PageBase.ReturnOrExchangeVerificationPage().returnDeviceTextBox.clear();
		PageBase.ReturnOrExchangeVerificationPage().returnDeviceTextBox.sendKeys("99000342701028");
		PageBase.ReturnOrExchangeVerificationPage().submitFormButton.click();
		Thread.sleep(1000);
		Utilities.dropdownSelect(PageBase.ReturnOrExchangeVerificationPage().exchangeReasons, SelectDropMethod.SELECTBYINDEX, "1");
		PageBase.ReturnOrExchangeVerificationPage().continueEXCHANGE.click();

		// Selecting plan feature.
		Utilities.ClickElement(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);

		// Selecting NO Insurance.
		PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();
		Thread.sleep(500);

		// Order Review and Confirm Page
		PageBase.CommonControls().continueCommonButton.click();

		if( driver.getCurrentUrl().contains("payment"))
		{
			PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA); //ToDo: Need to read from data sheet.
			Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
			Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
		}

		ChildOrderId= PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
		PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

		// Payment Verification page.
		PageBase.PaymentVerificationPage().paymentVerification("230440567552718374");

		// Device Verification and Activation page.

		Thread.sleep(30000);

		Utilities.waitForElementVisible(driver.findElement(By.xpath(".//*[@id='retailPage']/section/form/span/div/button")));
		String SupportCenterMessage = driver.findElement(By.xpath(".//*[@id='retailPage']/section/p")).getText();
		Reporter.log(" <br> <p> Support Center Message: " + SupportCenterMessage +" <br> <p>");
		String toCompare = Constants.SUPPORT_PAGE_MESSAGE + "#"+ChildOrderId+" when calling.";
		Reporter.log(toCompare + " <br> <p>");
		//			Assert.assertTrue(SupportCenterMessage.contains(toCompare));
	}

	private void inventoryManagementVerification_QA66() throws InterruptedException, AWTException, IOException {
		PageBase.InventoryManagementPage().launchInventoryInNewTab();
		Utilities.implicitWaitSleep(5000);
		PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber1, InventoryManagementBaseClass.IMEIStatus.Sold.toString());
	}

	private void shipAdminVerification_QA66(String orderId) {
		ShipAdminBaseClass.launchShipAdminInNewTab();
		Utilities.implicitWaitSleep(5000);
		PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

		String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
		String status = PageBase.OrderSummaryPage().getOrderStatus();
		Assert.assertEquals(status, Constants.SHIPPED);
		Assert.assertTrue(eventLogTableContent.contains(Constants.RECEIPT_SUBMISSION_SUCCEEDED_COMMENT));
		Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
		Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_SUCCEEDED_COMMENT));
		Assert.assertTrue(eventLogTableContent.contains(Constants.PARKING_SUCCEEDED_COMMENT));
		Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
		Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
		Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
		Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
		Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
	}

	public static void SelectLicense(String IdType, WebElement idTypeDropdown){
		switch(IdType)
		{
		case "DRIVERLICENCE":
			Utilities.dropdownSelect(idTypeDropdown, SelectDropMethod.SELECTBYINDEX, "1");
			break;
		case "USPASSPORT":
			Utilities.dropdownSelect(idTypeDropdown, SelectDropMethod.SELECTBYINDEX, "2");
			break;
		case "STATEID" :
			Utilities.dropdownSelect(idTypeDropdown, SelectDropMethod.SELECTBYINDEX, "3");
			break;
		}
	}
}
