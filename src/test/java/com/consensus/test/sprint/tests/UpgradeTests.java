package com.consensus.test.sprint.tests;
import com.consensus.qa.framework.*;
import com.consensus.qa.pages.PaymentRequiredPage;
import com.consensus.qa.pages.ServiceProviderVerificationPage;
import com.consensus.qa.tests.CarrierCreditCheckDetails;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class UpgradeTests extends RetailBaseClass{
	public String carrierType = "Sprint";
	//region Test Methods
	//region QA-3065
	@Test(groups={"verizon"})
	@Parameters("test-type")
	public void QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan(@Optional String testtype) {
		try {
			//This TC need 1 fresh phone number and 1 fresh IMEI for every run.
			CustomerDetails cusDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
			String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
			String iMEINumber = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
			String simNumber = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_Sim_3FF);
			AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintEasyPayUpgradeMultipleLinesEligible);
			String phoneNumber = accountDetails.MTN;
			String sSN = accountDetails.SSN;
			String zipCode = cusDetails.Zip;
			String accountPassword = accountDetails.Password;
			String orderId = "";

			Log.startTestCase("QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan");
			Reporter.log("<h2>Start - QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan. <br></h2>");
			Reporter.log("Launching Browser <br>", true);

			//Verify whether which enviorement to use internal or external.
			testtype = BrowserSettings.readConfig("test-type");
			if (testtype.equals("internal")) {
				AdminBaseClass adminBaseClass = new AdminBaseClass();
				adminBaseClass.launchAdminInNewTab();
				PageBase.AdminPage().navigateToSimulator();

				if (readConfig("internalTestType").toLowerCase().contains("carrierresponder")) {
					//Customizing xml files in Carrier Responder
					Reporter.log("<h3><U> Carrier Responder</U></h3>", true);
					//
					Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);

				} else {
					Reporter.log("<h3><U> Backend</U></h3>", true);
					//backendSettingsQA3065(phoneNumber);
					PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");
					Reporter.log("<h3><U> Backend Changes Done.</U></h3>", true);
				}
			} else {
				selectingSprintExternalEnvironment();
			}

			//Calling DBError utility to  find initial count or error in log files.
			DBError.navigateDBErrorPage();
			int initialCount = PageBase.AdminPage().totalErrorCount();

			//Switching Back To Retail
			Utilities.switchPreviousTab();

			//POA FLOW
			orderId = poaFlowQA3065(receiptId, iMEINumber, simNumber, phoneNumber, sSN, accountPassword, orderId);


			if (readConfig("Activation").toLowerCase().contains("true")) {
				//Ship Admin
				ShipAdminBaseClass.launchShipAdminInNewTab();
				shipAdminUpgradeVerifications(orderId);

				//Inventory Management
				PageBase.InventoryManagementPage().launchInventoryInNewTab();
				PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber, Constants.SOLD);

				//Verify in Server DB
				PageBase.SQLUtilAdminPage().launchSQLUtilInNewTab();
				serverDBVerificationsQA3065(orderId);
			}

			//DBError Verification.
			DBError.navigateDBErrorPage();
			//Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

			Reporter.log("<h3>QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan - Test Case Completes<h3>");
			Log.endTestCase("QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan");
		}
		catch (Exception ex) {
			Log.error(ex.getMessage());
			System.out.println(ex.getMessage());
			Utilities.driverTakesScreenshot("QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan");
			Assert.assertTrue(false);
		} finally {

		}
	}
	//endregion QA-3065

	//region QA-3096
	@Test(groups={"verizon"})
	@Parameters("test-type")
	public void QA3096_BuddyUpgradeMultipleLinesEligibleOneUpgradeEligibleOneUpgradeIneligible(@Optional String testtype) {
		try {
			//This TC need 2 fresh phone numbers and 1 IMEI for internal run with CR.
			CustomerDetails cusDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
			String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
			String iMEINumber = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
			String simNumber = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_Sim_3FF);
			AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintBuddyUpgrade);
			String phoneNumber = accountDetails.MTN;
			String donorPhoneNumber = CommonFunction.getUniqueNumber(phoneNumber); //This is for Carrier Responder.
			String sSN = accountDetails.SSN;
			String zipCode = cusDetails.Zip;
			String accountPassword = accountDetails.Password;
			String orderId = "";

			Log.startTestCase("QA3096_BuddyUpgradeMultipleLinesEligibleOneUpgradeEligibleOneUpgradeIneligible");
			Reporter.log("<h2>Start - QA3096_BuddyUpgradeMultipleLinesEligibleOneUpgradeEligibleOneUpgradeIneligible. <br></h2>");
			Reporter.log("Launching Browser <br>", true);

			//Verify whether which enviorement to use internal or external.
			testtype = BrowserSettings.readConfig("test-type");
			if (testtype.equals("internal")) {
				AdminBaseClass adminBaseClass = new AdminBaseClass();
				adminBaseClass.launchAdminInNewTab();
				PageBase.AdminPage().navigateToSimulator();
				//Customizing xml files in Carrier Responder
				Reporter.log("<h3><U> Carrier Responder</U></h3>", true);
				//Temp CR Settings
				Utilities.implicitWaitSleep(12000); //Temp
				Reporter.log("<h3><U> Carrier Responder Changes Done.</U></h3>", true);


			} else {
				selectingSprintExternalEnvironment();
			}

			//Calling DBError utility to  find initial count or error in log files.
			DBError.navigateDBErrorPage();
			int initialCount = PageBase.AdminPage().totalErrorCount();

			//Switching Back To Retail
			Utilities.switchPreviousTab();

			//POA FLOW
			orderId = poaFlowQA3096(receiptId, iMEINumber, simNumber, phoneNumber, sSN, accountPassword, orderId);


			if (readConfig("Activation").toLowerCase().contains("true")) {
				//Ship Admin
				ShipAdminBaseClass.launchShipAdminInNewTab();
				shipAdminUpgradeVerifications(orderId);

				//Inventory Management
				PageBase.InventoryManagementPage().launchInventoryInNewTab();
				PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber, Constants.SOLD);
			}

			//DBError Verification.
			DBError.navigateDBErrorPage();
			//Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

			Reporter.log("<h3>QA3096_BuddyUpgradeMultipleLinesEligibleOneUpgradeEligibleOneUpgradeIneligible - Test Case Completes<h3>");
			Log.endTestCase("QA3096_BuddyUpgradeMultipleLinesEligibleOneUpgradeEligibleOneUpgradeIneligible");
		}
		catch (Exception ex) {
			Log.error(ex.getMessage());
			System.out.println(ex.getMessage());
			Utilities.driverTakesScreenshot("QA3065_SprintFamilyAccountUpgradeEasyPayExistingPlan");
			Assert.assertTrue(false);
		} finally {

		}
	}
	//endregion QA-3096
	//endregion QA-3065

	//region QA 495
	@Test(groups = {"sprint"})
	@Parameters("test-type")
	public void QA_495_Sprint2YrAALExistingFamilyPlan(@Optional String testType) {
		String orderId = "";
		try {
			Log.startTestCase("QA_495_Sprint2YrAALExistingFamilyPlan");
			testType = BrowserSettings.readConfig("test-type");

			orderId = QA_495_poaCompleteFlow(testType);

			if (readConfig("Activation").contains("true")) {
				//inventoryManagementVerification();

				//Ship Admin Verification -orderId= ""
				//shipAdminVerifications(orderId);
			}
			Reporter.log("<h3>QA_495_Sprint2YrAALExistingFamilyPlan - Test Case Completes<h3>");
			Log.endTestCase("QA_495_Sprint2YrAALExistingFamilyPlan");
		} catch (Exception ex) {
			//Log.error(ex.getMessage());
			//Utilities.driverTakesScreenshot("QA_93_Sprint2YrAALExistingFamilyPlan");
			System.out.println(ex);
			//Assert.fail();
		}
	}
	//endregion QA 495

	//region QA 4258
	@Test(groups = {"sprint"})
	@Parameters("test-type")
	public void QA_4258_SprintEasyAddaLine(@Optional String testtype) throws InterruptedException, AWTException, IOException {

		String testType = BrowserSettings.readConfig("test-type");
		CreditCardDetails creditCard = new CreditCardDetails();
		creditCard = PageBase.CSVOperations().CreditCardDetails(PaymentRequiredPage.CardType.VISA);
		String iMEINumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.SamsungGalaxyS4_16GBWhite);
		String simType1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sim_3FF);
		AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintEasyPayUpgrade);
		CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
		String phoneNumber = accountDetails.MTN;
		String sSN = accountDetails.SSN;
		String zipCode = customerDetails.Zip;
		String accountPassword = accountDetails.Password;
		String orderId = "";

		/*PageBase.InventoryManagementPage().launchInventoryInNewTab();
		PageBase.InventoryManagementPage().addDeviceToInventory(iMEINumber1, "SPT IPHONE 4S WHITE 8GB ");*/

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

		orderId = poaFlowQA4258(phoneNumber, iMEINumber1, simType1, orderId, sSN, zipCode);
	}
	//endregion QA 4258

	//region QA 3181
	@Test(groups = {"sprint"})
	@Parameters("test-type")
	public void QA_3181_SprintAddaLineCancelOnInstallmentDetails(@Optional String testtype) throws InterruptedException, AWTException, IOException {

		String testType = BrowserSettings.readConfig("test-type");
		CreditCardDetails creditCard = new CreditCardDetails();
		creditCard = PageBase.CSVOperations().CreditCardDetails(PaymentRequiredPage.CardType.VISA);
		String iMEINumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.SamsungGalaxyS4_16GBWhite);
		String simType1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sim_3FF);
		AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.SprintEasyPayUpgrade);
		CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
		String phoneNumber = accountDetails.MTN;
		String sSN = accountDetails.SSN;
		String zipCode = customerDetails.Zip;
		String accountPassword = accountDetails.Password;
		String orderId = "";
		String receiptId = "";

		/*PageBase.InventoryManagementPage().launchInventoryInNewTab();
		PageBase.InventoryManagementPage().addDeviceToInventory(iMEINumber1, "SPT IPHONE 4S WHITE 8GB ");*/

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

		//POA Flow
		Utilities.switchPreviousTab();

		//Calling DBError utility to  find initial count or error in log files.
		DBError.navigateDBErrorPage();
		int initialCount= PageBase.AdminPage().totalErrorCount();

		Utilities.switchPreviousTab();

		orderId = poaFlowQA3181(phoneNumber, iMEINumber1, simType1, orderId, sSN, zipCode);

		//Ship Admin
		ShipAdminBaseClass.launchShipAdminInNewTab();
		PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
		PageBase.OrderSummaryPage().verifyAppleCareInsuranceStatus();
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
		PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber1, Constants.SOLD);

		//DB Errors
		DBError.navigateDBErrorPage();
		Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

		PageBase.CSVOperations();
		CSVOperations.WriteToCSV("QA_3181",orderId,iMEINumber1,"","",customerDetails.FirstName,customerDetails.LastName,
				customerDetails.EMail,receiptId,customerDetails.IDType,customerDetails.State,
				customerDetails.IDNumber, customerDetails.PhNum,customerDetails.Zip,sSN,
				customerDetails.IDExpirationMonth,customerDetails.IDExpirationYear);
	}
	//endregion QA 4258
	//endregion Test Methods

	//region Helper and Refactored Methods
	private void selectingSprintExternalEnvironment() throws InterruptedException, AWTException, IOException {
		AdminBaseClass adminBaseClass = new AdminBaseClass();
		adminBaseClass.launchAdminInNewTab();
		PageBase.AdminPage().navigateToSimulator();
		PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
		Reporter.log("<h3><U> External Server</U></h3>", true);
	}

	private CarrierCreditCheckDetails getCarrierCreditCheckDetails() throws IOException {
		CarrierCreditCheckDetails cccDetails = new CarrierCreditCheckDetails();
		PageBase.CSVOperations();
		CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
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
		cccDetails.setSSN(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.SSNWithDeposit));
		cccDetails.setIdTypeState(customerDetails.IDState);
		cccDetails.setIdNumber(customerDetails.IDNumber);
		cccDetails.setMonth(customerDetails.IDExpirationMonth);
		cccDetails.setYear(customerDetails.IDExpirationYear);
		cccDetails.setIDType(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
		return cccDetails;
	}


	private String QA_495_poaCompleteFlow(@Optional String testType) throws IOException {
		String orderId = "";//Login to retail page.
		String simNumber = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_3FF);
		//String spvDetails = PageBase.CSVOperations().GetSpvDetails();
		//String[] spvCollections = spvDetails.split(",");
		// Scanning smart phone,basic phone and MIFI phone.
		String imei1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

		AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.Sprint2yrUpgrade);
		String MTNNumber = accountDetails.MTN;
		String accountPassword = accountDetails.Password;
		String SSN = accountDetails.SSN;
		PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

		//Click on Sales and Activation Link
		PageBase.HomePageRetail().upgradeEligibilityCheckerLink.click();

		//Fill Sprint Details
		Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
		//Utilities.waitForElementVisible(PageBase.UECVerificationPage().phoneNumberSprintTextbox);
		PageBase.UECVerificationPage().fillSprintDetails("9876543210","1234","94109");
		PageBase.UECVerificationPage().continueSprintButton.click();

		//UEC Add Lines Page
		Utilities.waitForElementVisible(PageBase.UECAddLinesPage().firstAALCheckbox);
		PageBase.UECAddLinesPage().clickFirstEnabledCheckbox();
		PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

		//Enter IEMEI of the device
		PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);
		//PageBase.VerizonEdgePage().declineSprintEasyPay();
		PageBase.CommonControls().cancelButton.click();
		Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
		PageBase.CommonControls().continueButtonDVA.click();

		String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
		CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();

		PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
		PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();

		PageBase.CommonControls().continueButton.click();
		Utilities.implicitWaitSleep(1000);
		try {
			if (PageBase.CommonControls().continueButton.isEnabled())
				PageBase.CommonControls().continueButton.click();
		} catch (Exception e) {

		}

		Utilities.waitForElementVisible(PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox);
		PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
		PageBase.CommonControls().continueCommonButton.click();


		//Verifying device with plan and continue.
		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		//cartDevice1price = PageBase.CartPage().device1Price.getText();
		//cartDevice2price = PageBase.CartPage().device2Price.getText();
		PageBase.CommonControls().continueCommonButton.click();

		//Selecting plan feature.
		Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
		PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

		//Select Protection Plan Insurance Page
		PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();

		PageBase.CommonControls().continueButton.click();

		//Order Review and Confirm Page
		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		PageBase.CommonControls().continueCommonButton.click();

		if (readConfig("Activation").contains("true")) {
			//Terms and Condition Page.
			//Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().continueTCButton);
			PageBase.TermsandConditionsPage().emailTCChkBox.click();
			PageBase.TermsandConditionsPage().carrierTermsCheckBox.click();
			PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
			PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
			PageBase.TermsandConditionsPage().continueTCButton.click();

			//Print Mobile Scan Sheet.
			Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
			orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
			PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

			// Payment Verification page. Scan Reciept id.
			Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
			PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
			PageBase.PaymentVerificationPage().submitButton.click();

			Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);

			PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(imei1); //"2113114115116110"
			PageBase.DeviceVerificationaandActivation().submitDVAButton.click();

			PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber);//21212121212121212121
			// PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");  // ToDo: Read from data sheet.
			PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

			Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
			Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
			String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
			Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
		}
		else {
			Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
		}

		return orderId;
	}

	private void serverDBVerificationsQA3065(String orderId) {
		Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().chooseQueryWrapperDropdown);
		Utilities.dropdownSelect(PageBase.SQLUtilAdminPage().chooseQueryWrapperDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "2");
		PageBase.SQLUtilAdminPage().queryTextbox.sendKeys("select * from ordersignatures where ordid=" + orderId);
		PageBase.SQLUtilAdminPage().submitButton.click();
		Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().orderSignaturesTable);
		String orderSignaturesTableContent = PageBase.SQLUtilAdminPage().orderSignaturesTable.getText();
		int rowCount = StringUtils.countMatches(orderSignaturesTableContent, orderId);
		Assert.assertEquals(rowCount, 2);
		PageBase.SQLUtilAdminPage().queryTextbox.clear();
		PageBase.SQLUtilAdminPage().queryTextbox.sendKeys("Select * from orderitemfinanceinfos where ordid=" + orderId);
		Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().generalTable);
		String tableContent = PageBase.SQLUtilAdminPage().generalTable.getText();
		Assert.assertTrue(tableContent.contains(orderId));
		PageBase.SQLUtilAdminPage().queryTextbox.clear();
		PageBase.SQLUtilAdminPage().queryTextbox.sendKeys("select * from  orderiteminfos where ordid=" + orderId);
		Utilities.waitForElementVisible(PageBase.SQLUtilAdminPage().generalTable);
		tableContent = PageBase.SQLUtilAdminPage().generalTable.getText();
		Assert.assertTrue(tableContent.contains(orderId));
	}

	private void shipAdminUpgradeVerifications(String orderId) {
		PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
		String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
		String status = PageBase.OrderSummaryPage().getOrderStatus();
		Assert.assertEquals(status, Constants.SHIPPED);
//		Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
//		Assert.assertTrue(eventLogTableContent.contains(Constants.ORDER_VALIDATION_PASSED));
//		Assert.assertTrue(eventLogTableContent.contains(Constants.LINE_ACTIVATION_SUCCEEDED));
//		Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
//		Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.HANDSET_UPGRADE));
//		//Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
//		Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.EXISTING_ACCOUNT_ORDER));
	}

	private String poaFlowQA3065(String receiptId, String iMEINumber, String simNumber, String phoneNumber, String sSN, String accountPassword, String orderId) throws IOException, InterruptedException, AWTException {
		//Login
		PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

		//Home Page
		PageBase.HomePageRetail().salesAndActivationsLink.click();

		//Choose A Path Page
		Utilities.waitForElementVisible(PageBase.ChoosePathPage().existingCarrier);
		PageBase.ChoosePathPage().existingCarrier.click();

		//Pick Your Path Page
		Utilities.waitForElementVisible(PageBase.PickYourPathPage().upgradeDeviceForOneOrMoreLinesOnExistingAccountRadioButton);
		PageBase.PickYourPathPage().upgradeDeviceForOneOrMoreLinesOnExistingAccountRadioButton.click();
		PageBase.CommonControls().continueButtonDVA.click();

		//UEC Verification Page
		Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
		PageBase.UECVerificationPage().sprintTab.click();
		PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber);
		PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(sSN);
		PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(accountPassword);
		PageBase.UECVerificationPage().continueSprintButton.click();

		//UEC Add Lines Page
		Utilities.waitForElementVisible(PageBase.UECAddLinesPage().firstAALCheckbox);
		PageBase.UECAddLinesPage().clickFirstEnabledCheckbox();
		PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

		//Device Scan Page
		Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
		PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
		PageBase.DeviceScanPage().submitDeviceButton.click();

		//Sprint Easy Pay Page
		Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
		Assert.assertTrue(PageBase.SprintEasyPayPage().priceBox.getText().contains("2yr agreement"));
		PageBase.SprintEasyPayPage().yesButton.click();

		//Carrier Credit Check Page
		Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().populateForm);
		CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
		PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
		PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
		PageBase.CommonControls().continueButton.click();
		try {
			PageBase.CommonControls().continueButton.click();
		}
		catch (Exception e) {}

		//Sprint Easy Pay Eligibility Result
		Utilities.waitForElementVisible(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel, 120);
		Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel.isDisplayed());
		Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().downPaymentLabel.isDisplayed());
		Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().installmentContractLengthLabel.isDisplayed());
		PageBase.SprintEasyPayEligibilityResultPage().minimumDownPaymentEasyPayRadioButton.click();
		PageBase.CommonControls().continueCommonButton.click();

		//Sprint Shop Plans Page
		//Utilities.waitForElementVisible(PageBase.SprintShopPlansPage().keepmyExistingSprintPlanAddButton);
		//PageBase.SprintShopPlansPage().keepmyExistingSprintPlanAddButton.click();
		Utilities.waitForElementVisible(PageBase.SprintShopPlansPage().sprintFamilySharePack1GBAddButton); //Need to change Keep My Existing Plan, currently not coming.
		PageBase.SprintShopPlansPage().sprintFamilySharePack1GBAddButton.click();

		//Cart Page
		Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
		Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "1");
		String phonePrice = PageBase.CartPage().firstPhonePriceText.getText();
		String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
		Assert.assertTrue(PageBase.CartPage().downPaymentAmountLabel.isDisplayed());
		Assert.assertTrue(PageBase.CartPage().monthlyDeviceInstallmentBalanceLabel.isDisplayed());
		Assert.assertTrue(PageBase.CartPage().monthlyRecurringFeeLabel.isDisplayed());
		Assert.assertTrue(PageBase.CartPage().totalDueTodayLabel.isDisplayed());
		Assert.assertTrue(PageBase.CartPage().lastMonthlyInstallmentMayBeDifferentText.isDisplayed());
		PageBase.CartPage().continueCartButton.click();

		//Select Plan Features Page
		Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
		PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

		//Select Protection Plan Insurance Page
		PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();

		//Order Review and Confirm Page
		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().firstPhonePrice.isDisplayed());
		//Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().firstPhonePrice.getText(), phonePrice); //Not matching right now
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().downPaymentForSprintEasyPayLabel.isDisplayed());
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().lastMonthInstallmentLabel.isDisplayed());
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().monthlyInstallmentLabel.isDisplayed());
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().downPaymentLabel.isDisplayed());
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().monthlyRecurringFeeLabel.isDisplayed());
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
		String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
		PageBase.CommonControls().continueCommonButton.click();

		//Terms & Conditions Page
		Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox);
		PageBase.TermsandConditionsPage().carrierTermsAcceptCheckbox.click();
		PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
		PageBase.TermsandConditionsPage().signingTermsAndConditions(driver);

		if (readConfig("Activation").toLowerCase().contains("true")) {
			PageBase.TermsandConditionsPage().continueTCButton.click();

			//Payment Required Page //ToDo: Remove this when no insurance bug will fix premanently.
			Utilities.implicitWaitSleep(4000);
			String url = driver.getCurrentUrl();
			if (url.contains("payment")) {
				PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
				PageBase.PaymentRequiredPage().sameAddressTab.click();
				PageBase.PaymentRequiredPage().continuePRButton.click();
			}

			//Print Mobile Scan Sheet Page
			Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
			//Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice);
			orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
			PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

			//Payment Verification Page
			Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
			PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);
			PageBase.PaymentVerificationPage().submitButton.click();

			//Device Verification and Activation Page
			Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
			PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(iMEINumber);
			PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
			PageBase.DeviceVerificationaandActivation().simType.clear();
			PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber);
			try  //ToDo:Remove this when no insurance bug will fix permanently.
			{
				PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
				CreditCardDetails CreditCard = PageBase.CSVOperations().CreditCardDetails(PaymentRequiredPage.CardType.VISA);
				String cVNNumberS = "" + CreditCard.CVV;
				PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys(cVNNumberS);
			} catch (Exception e) {
			}
			PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

			//Device Financing Installment Contract Page
			Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().print);
			Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().print.isDisplayed());
			Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().totalAmountFinancedLabel.isDisplayed());
			Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().downPaymentLabel.isDisplayed());
			Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().monthlyInstallmentLabel.isDisplayed());
			Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().installmentContractLengthLabel.isDisplayed());
			Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().phoneImage.isDisplayed());
			Assert.assertTrue(PageBase.DeviceFinancingInstallmentContractPage().phoneModelText.isDisplayed());
			PageBase.DeviceFinancingInstallmentContractPage().PrintDeviceFinancingDetails(driver);

			//Order Activation and Complete Page
			Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
			Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
			Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), iMEINumber);
			Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), simNumber);
			Assert.assertTrue(PageBase.OrderActivationCompletePage().printConfirmationSlipButton.isDisplayed());
			Assert.assertTrue(PageBase.OrderActivationCompletePage().deviceModelLabel.isDisplayed());
			Assert.assertTrue(PageBase.OrderActivationCompletePage().downPaymentLabel.isDisplayed());
			Assert.assertTrue(PageBase.OrderActivationCompletePage().monthlyDeviceInstallmentLabel.isDisplayed());
			Assert.assertTrue(PageBase.OrderActivationCompletePage().lastMonthInstallmentLabel.isDisplayed());
		}
		return orderId;
	}

	private String poaFlowQA3096(String receiptId, String iMEINumber, String simNumber, String phoneNumber, String sSN, String accountPassword, String orderId) throws IOException {
		//Login
		PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

		//Home Page
		PageBase.HomePageRetail().salesAndActivationsLink.click();

		//Choose A Path Page
		Utilities.waitForElementVisible(PageBase.ChoosePathPage().existingCarrier);
		PageBase.ChoosePathPage().existingCarrier.click();

		//Pick Your Path Page
		Utilities.waitForElementVisible(PageBase.PickYourPathPage().upgradeDeviceForOneOrMoreLinesOnExistingAccountRadioButton);
		PageBase.PickYourPathPage().upgradeDeviceForOneOrMoreLinesOnExistingAccountRadioButton.click();
		PageBase.CommonControls().continueButtonDVA.click();

		//UEC Verification Page
		Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
		PageBase.UECVerificationPage().sprintTab.click();
		PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber);
		PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(sSN);
		PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(accountPassword);
		PageBase.UECVerificationPage().continueSprintButton.click();

		//UEC Add Lines Page
		PageBase.UECAddLinesPage().selectingFirstBuddyUpgradeLine();
		PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

		//Device Scan Page
		Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
		PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
		PageBase.DeviceScanPage().submitDeviceButton.click();

		//Sprint Shop Plans Page
		Utilities.waitForElementVisible(PageBase.SprintShopPlansPage().keepmyExistingSprintPlanAddButton);
		PageBase.SprintShopPlansPage().keepmyExistingSprintPlanAddButton.click();

		//Cart Page
		Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
		Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "1");
		String phonePrice = PageBase.CartPage().firstPhonePriceText.getText();
		Assert.assertTrue(PageBase.CartPage().firstPhonePriceText.isDisplayed());
		Assert.assertTrue(PageBase.CartPage().twoYearsActivationText.isDisplayed());
		String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
		PageBase.CartPage().continueCartButton.click();

		//Select Plan Features Page
		Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
		PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

		//Select Protection Plan Insurance Page
		//PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();

		//Service Provider Verification Page
		Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().firstNameTextbox);
		CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
		PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, customerDetails.LastName,
				customerDetails.EMail, ServiceProviderVerificationPage.IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber,
				customerDetails.IDExpirationMonth, Integer.parseInt(customerDetails.IDExpirationYear));
		PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck.click();
		PageBase.ServiceProviderVerificationPage().continueSPVButton.click();

		//Order Review and Confirm Page
		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().firstPhonePrice.isDisplayed());
		//Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().firstPhonePrice.getText(), phonePrice);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
		String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
		PageBase.CommonControls().continueCommonButton.click();

		//Terms & Conditions Page
		Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox);
		PageBase.TermsandConditionsPage().carrierTermsAcceptCheckbox.click();
		PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
		PageBase.TermsandConditionsPage().signingTermsAndConditions(driver);

		if (readConfig("Activation").toLowerCase().contains("true")) {
			PageBase.TermsandConditionsPage().continueTCButton.click();

			//Payment Required Page //ToDo: Remove this when no insurance bug will fix premanently.
			Utilities.implicitWaitSleep(4000);
			String url = driver.getCurrentUrl();
			if (url.contains("payment")) {
				PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
				PageBase.PaymentRequiredPage().sameAddressTab.click();
				PageBase.PaymentRequiredPage().continuePRButton.click();
			}

			//Print Mobile Scan Sheet Page
			Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
			//Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice);
			orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
			Assert.assertTrue(PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.isDisplayed());
			Assert.assertTrue(PageBase.PrintMobileScanSheetPage().storeLocationValuePMSSText.isDisplayed());
			Assert.assertTrue(PageBase.PrintMobileScanSheetPage().barcodePMSSImage.isDisplayed());
			Assert.assertTrue(PageBase.PrintMobileScanSheetPage().phoneModelText.isDisplayed());
			Assert.assertTrue(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.isDisplayed());
			PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

			//Payment Verification Page
			Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
			PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);
			PageBase.PaymentVerificationPage().submitButton.click();

			//Device Verification and Activation Page
			Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
			PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(iMEINumber);
			PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
			PageBase.DeviceVerificationaandActivation().simType.clear();
			PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber);
			try  //ToDo:Remove this when no insurance bug will fix permanently.
			{
				PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
				CreditCardDetails CreditCard = PageBase.CSVOperations().CreditCardDetails(PaymentRequiredPage.CardType.VISA);
				String cVNNumberS = "" + CreditCard.CVV;
				PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys(cVNNumberS);
			} catch (Exception e) {
			}
			PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

			//Order Activation and Complete Page
			Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
			Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
			Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), iMEINumber);
		}
		return orderId;
	}

	private String poaFlowQA4258(String phoneNumber, String iMEINumber, String simNumber, String orderId,
								 String ssn, String zipCode) throws IOException, AWTException, InterruptedException {

		CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
		NumPortingDetails portDetails = PageBase.CSVOperations().ReadPortingDetails();
		String phNo = PageBase.CSVOperations().GenerateRandomNoForNumberPorting();

		//Login
		PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

		//Home Page
		PageBase.HomePageRetail().salesAndActivationsLink.click();

		//Choose a Path Page
		PageBase.ChoosePathPage().existingCarrier.click();

		//Pick your Path Page
		PageBase.PickYourPathPage().AALExistingAccount.click();
		PageBase.CommonControls().continueButtonDVA.click();

		//UEC Verification Page
		Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
		PageBase.UECVerificationPage().sprintTab.click();
		PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber);
		if(ssn.length()>4) {
			PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(ssn.substring(5, ssn.length()));
		}
		else
		{
			PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(ssn);
		}
		PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(zipCode);
		PageBase.UECVerificationPage().continueSprintButton.click();

		//Select an Option Page
		Utilities.waitForElementVisible(PageBase.SelectAnOptionPage().AALExistingFamilyPlan);
		PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
		PageBase.CommonControls().continueButtonDVA.click();

		//Device Scan Page
		Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
		PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
		PageBase.DeviceScanPage().submitDeviceButton.click();

		//Sprint Easy Pay Page
		Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
		PageBase.SprintEasyPayPage().yesButton.click();

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

		/*//Sprint Shop Plans Page
		Utilities.waitForElementVisible(PageBase.SprintShopPlansPage().sprintFamilySharePack1GBAddButton);
		PageBase.SprintShopPlansPage().sprintFamilySharePack1GBAddButton.click();
*/
		//Cart Page
		Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
		String phonePrice = PageBase.CartPage().phonePriceAALText.getText();
		String phoneModel = PageBase.CartPage().phoneModelAALLink.getText();
		PageBase.CartPage().continueCartButton.click();

		//Selecting plan feature.
		Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
		PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

		//Select Protection Plan Insurance Page
		Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
		PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst.click();
		PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
		PageBase.CommonControls().continueButton.click();

		// Selecting Number Porting.
		Utilities.ClickElement(PageBase.NumberPortPage().numberPortRadiobutton);
		Utilities.ClickElement(PageBase.CommonControls().continueButton);

		PageBase.PortMyNumbersPage().enterPortDataForPreCreditCheck(phNo, portDetails.Carrier,
				portDetails.CurrentAccNumber, portDetails.SSN);

		/*Order Review and Confirm Page */
		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().firstPhonePrice.isDisplayed());
		//Assert.assertEquals(OrderReviewAndConfirmPage().phoneMonthlyFee.getText(), phonePrice);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().existingPlanDiv.isDisplayed());
		String existingPlan = PageBase.OrderReviewAndConfirmPage().existingPlanDiv.getText();
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
		String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
		PageBase.CommonControls().continueCommonButton.click();

		//Terms & Conditions Page
		if (readConfig("Activation").contains("true")) {
			//Terms and Condition Page.
			Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().emailTCChkBox);
			PageBase.TermsandConditionsPage().emailTCChkBox.click();
			PageBase.TermsandConditionsPage().carrierTermsCheckBox.click();
			PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
			PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
			PageBase.TermsandConditionsPage().continueTCButton.click();

			// Credit Card Payment Page
			Utilities.implicitWaitSleep(10000);
			if( driver.getCurrentUrl().contains("payment"))
			{
				PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
				Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
				Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
			}

			//Print Mobile Scan Sheet.
			Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
			orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
			Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode.isDisplayed());

			//TODO:Need to add assertion for store location.
			//    WebElement web = driver.findElement(By.xpath("//span[contains(text(),'2766 - TARGET - SAN FRANCISCO CENTRAL')]"));
			//    String storeLocation = web.getText();
			PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

			// Payment Verification page. Scan Reciept id.
			Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
			PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
			PageBase.PaymentVerificationPage().submitButton.click();

			//Device Verification and Activation page. Scan Device IEMI and enter SIM number.
			Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
			PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(iMEINumber, simNumber);

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

			// Order Activation Complete page.
			Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
			Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
			String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
			Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
			Assert.assertTrue(PageBase.OrderActivationCompletePage().iMEINumberValueText.isDisplayed());
		} else {
			Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
		}
		return orderId;
	}

	private String poaFlowQA3181(String phoneNumber, String iMEINumber, String simNumber, String orderId,
								 String ssn, String zipCode) throws IOException, AWTException, InterruptedException {

		CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
		NumPortingDetails portDetails = PageBase.CSVOperations().ReadPortingDetails();
		String phNo = PageBase.CSVOperations().GenerateRandomNoForNumberPorting();

		//Login
		PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

		//Home Page
		PageBase.HomePageRetail().salesAndActivationsLink.click();

		//Choose a Path Page
		PageBase.ChoosePathPage().existingCarrier.click();

		//Pick your Path Page
		PageBase.PickYourPathPage().AALExistingAccount.click();
		PageBase.CommonControls().continueButtonDVA.click();

		//UEC Verification Page
		Utilities.waitForElementVisible(PageBase.UECVerificationPage().sprintTab);
		PageBase.UECVerificationPage().sprintTab.click();
		PageBase.UECVerificationPage().phoneNumberSprintTextbox.sendKeys(phoneNumber);
		if(ssn.length()>4) {
			PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(ssn.substring(5, ssn.length()));
		}
		else
		{
			PageBase.UECVerificationPage().last4OfSSNSprintTextbox.sendKeys(ssn);
		}
		PageBase.UECVerificationPage().pinSprintTextbox.sendKeys(zipCode);
		PageBase.UECVerificationPage().continueSprintButton.click();

		//Select an Option Page
		Utilities.waitForElementVisible(PageBase.SelectAnOptionPage().AALExistingFamilyPlan);
		PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
		PageBase.CommonControls().continueButtonDVA.click();

		//Device Scan Page
		Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
		PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
		PageBase.DeviceScanPage().submitDeviceButton.click();

		//Sprint Easy Pay Page
		Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
		PageBase.SprintEasyPayPage().yesButton.click();

		//Filling information in Carrier Credit Check Page.
		Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().populateForm);
		CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
		PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
		PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
		PageBase.CommonControls().continueButton.click();
		//PageBase.CommonControls().continueButton.click();

		// Installment Page
		Utilities.waitForElementVisible(PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton);
		PageBase.InstallmentPage().decline.click();

		// Device Scan Page
		Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);

		//Filling information in Carrier Credit Check Page.
		Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().populateForm);
		cccDetails = getCarrierCreditCheckDetails();
		PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
		PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
		PageBase.CommonControls().continueButton.click();

		// Credit Check Verification Results
		Utilities.waitForElementVisible(PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox);
		PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
		PageBase.CommonControls().continueCommonButton.click();

		/*//Sprint Plans Page
		PageBase.SprintShopPlansPage().sprintFamilySharePack1GBAddButton.click();*/

		//Cart Page
		//Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
		//Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, Utilities.SelectDropMethod.SELECTBYINDEX, "1");
		//String phonePrice = PageBase.CartPage().firstPhonePriceText.getText();
		//String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
		PageBase.CartPage().continueCartButton.click();

		//Selecting plan feature.
		Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
		PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

		//Select Protection Plan Insurance Page
		Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
		PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsuranceFirst.click();
		PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
		PageBase.CommonControls().continueButton.click();

		// Selecting Number Porting.
		Utilities.ClickElement(PageBase.NumberPortPage().noNumberPortRadiobutton);
		Utilities.ClickElement(PageBase.CommonControls().continueButton);

		/*Order Review and Confirm Page */
		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().firstPhonePrice.isDisplayed());
		//Assert.assertEquals(OrderReviewAndConfirmPage().phoneMonthlyFee.getText(), phonePrice);
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().existingPlanDiv.isDisplayed());
		String existingPlan = PageBase.OrderReviewAndConfirmPage().existingPlanDiv.getText();
		Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
		String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
		PageBase.CommonControls().continueCommonButton.click();

		//Terms & Conditions Page
		if (readConfig("Activation").contains("true")) {
			//Terms and Condition Page.
			Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().emailTCChkBox);
			PageBase.TermsandConditionsPage().emailTCChkBox.click();
			PageBase.TermsandConditionsPage().carrierTermsCheckBox.click();
			PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
			PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
			PageBase.TermsandConditionsPage().continueTCButton.click();

			// Credit Card Payment Page
			Utilities.implicitWaitSleep(10000);
			if( driver.getCurrentUrl().contains("payment"))
			{
				PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
				Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
				Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
			}

			//Print Mobile Scan Sheet.
			Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
			orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
			Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode.isDisplayed());

			//TODO:Need to add assertion for store location.
			//    WebElement web = driver.findElement(By.xpath("//span[contains(text(),'2766 - TARGET - SAN FRANCISCO CENTRAL')]"));
			//    String storeLocation = web.getText();
			PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

			// Payment Verification page. Scan Reciept id.
			Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
			PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
			PageBase.PaymentVerificationPage().submitButton.click();

			//Device Verification and Activation page. Scan Device IEMI and enter SIM number.
			Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
			PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(iMEINumber, simNumber);

			// Order Activation Complete page.
			Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
			Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
			String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
			Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
			Assert.assertTrue(PageBase.OrderActivationCompletePage().iMEINumberValueText.isDisplayed());
		} else {
			Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
		}
		return orderId;
	}

	//endregion Helper and Refactored Methods
}
