package com.consensus.test.verizon.tests;

import com.consensus.qa.pages.PaymentRequiredPage;
import com.consensus.qa.tests.CarrierCreditCheckDetails;

import org.apache.velocity.runtime.directive.Parse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import com.consensus.qa.framework.*;
import com.consensus.qa.framework.NumPortingDetails;
import com.consensus.qa.framework.CSVOperations.FileName;
import com.consensus.qa.framework.InventoryManagementBaseClass.IMEIStatus;
import com.consensus.qa.framework.Utilities.SelectDropMethod;
import com.consensus.qa.pages.PaymentRequiredPage.CardType;
import com.consensus.qa.pages.ServiceProviderVerificationPage.IdType;
import com.consensus.qa.pages.ServiceProviderVerificationPage;
import com.consensus.qa.pages.ReturnProcessPage;
import com.consensus.qa.pages.ServiceProviderVerificationPage.Month;

public class AddNewLineTests extends RetailBaseClass {
	//region Variable Declaration
	public String iMEINumber1 = "";
	public String imei1 = "";
	public String imei2 = "";
	public String imei3 = "";
	public String carrierType = "Verizon";  //ToDo: Need to read from data sheet.
	public String cartDevice1price = "";
	public String cartDevice2price = "";
	public String simType1 = "";
	String receiptId = "";
    IMEIDetails imeiDetails = null;
    //endregion

    //region Test Methods
	//region QA 78
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_78_NonedgeNewActivationMultiplelinesExchangetoEdge(@Optional String testtype) throws InterruptedException, AWTException, IOException {
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        String order = VerizonActivateVerizonNonedgeMultipleLines(customerDetails);
        //PageBase.LoginPageRetail().LaunchNewPOATab();
        PageBase.HomePageRetail().newGuestButton.click();
        System.out.println("Clicked once");

        VerizonExchangeDevice(order, customerDetails);
        //-------------Upgrade Flow-----
        Utilities.waitForElementVisible(PageBase.HomePageRetail().newGuestButton);
        PageBase.HomePageRetail().newGuestButton.click();
        System.out.println("After Exchange");

        String orderId = VerizonExchangetoNonEdgeUpgradeFlow(customerDetails);

        QA_78ShipadminVerification(orderId);
    }
//endregion QA 78

    //region QA-5437
    @Test(groups={"verizon"})
    @Parameters("test-type")
    public void QA_5437_VerizonNonEdgeAAL(@Optional String testtype){
        try
        {
            //This TC requires 1 fresh IMEI for every run.
            Log.startTestCase("QA_5437_VerizonNonEdgeAAL");
            String iMEINumber = "9990000000000002";
            System.out.println(iMEINumber);
            String simNumber = "12345678901234567890";
            String orderId = "";

            //Verify whether which enviorement to use internal or external.
            testtype = BrowserSettings.readConfig("test-type");

            if(testtype.equals("internal"))
            {
                //Customizing xml files in Carrier Responder
                carrierResponderSettingsQA5437();
            }
            else
            {
                selectingVerizonExternalEnvironment();
            }

            //Calling DBError utility to  find initial count or error in log files.
            DBError.navigateDBErrorPage();
            int initialCount= PageBase.AdminPage().totalErrorCount();

            //Switching Back To Retail
            Utilities.switchPreviousTab();

            //POA FLOW
            orderId = poaFlowQA5437(testtype, iMEINumber, simNumber, orderId);


            if (readConfig("Activation").toLowerCase().contains("true")) {
                //Ship Admin
                shipAdminVerificationsQA5437(testtype, orderId);

                //Inventory Management
                PageBase.InventoryManagementPage().launchInventoryInNewTab();
                PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber, Constants.SOLD);
            }

            //DBError Verification.
            DBError.navigateDBErrorPage();
            //Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
            DBError.navigateDBErrorPage();
            Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

            Log.endTestCase("QA_5437_VerizonNonEdgeAAL");
        }
        catch(Exception ex)
        {
            Log.error(ex.getMessage());
            System.out.println(ex.getMessage());
            Utilities.driverTakesScreenshot("QA_5437_VerizonNonEdgeAAL");
            Assert.assertTrue(false);
        }
    }
    //endregion QA-5437

    //region QA-71
    @Test(groups={"verizon"})
    @Parameters("test-type")
    public void QA_71_VerizonEdgeTryToSwitchIndividualToFamilyAndAAL(@Optional String testtype){
        try
        {
            Log.startTestCase("QA_71_VerizonEdgeTryToSwitchIndividualToFamilyAndAAL");

            //Verify whether which enviorement to use internal or external.
            testtype = BrowserSettings.readConfig("test-type");

            if(testtype.contains("internal"))
            {
                //Customizing xml files in Carrier Responder
                carrierResponderSettingsQA71();
            }
            else
            {
                selectingVerizonExternalEnvironment();
            }

            //Calling DBError utility to  find initial count or error in log files.
            DBError.navigateDBErrorPage();
            int initialCount= PageBase.AdminPage().totalErrorCount();

            //Switching Back To Retail
            Utilities.switchPreviousTab();

            //POA FLOW
            poaFlowQA71();

            //DBError Verification.
            DBError.navigateDBErrorPage();
            //Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
            DBError.navigateDBErrorPage();
            Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

            Log.endTestCase("QA_71_VerizonEdgeTryToSwitchIndividualToFamilyAndAAL");
        }
        catch(Exception ex)
        {
            Log.error(ex.getMessage());
            System.out.println(ex.getMessage());
            Utilities.driverTakesScreenshot("QA_71_VerizonEdgeTryToSwitchIndividualToFamilyAndAAL");
            Assert.assertTrue(false);
        }
        finally
        {

        }
    }
    //endregion QA-71

	//region QA-50
	@Test(groups={"verizon"})
	@Parameters("test-type")
	public void QA50_VerizonNonEdgeWithNumberPortCCInCA(@Optional String testtype)
			throws IOException, InterruptedException, AWTException{

		boolean  stopActivation = false;
		try{
			CreditCardDetails creditCard = new CreditCardDetails();
			creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);
			String orderId = "";
			String phoneNumberLine1 = "";

			iMEINumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBWhite);
			simType1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);
			receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
			//CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);

			Reporter.log("<h2>Start - QA50_VerizonNonEdgeWithNumberPortCCInCA. <br></h2>" );
			Reporter.log("<h3>Description: Verizon-NonEdge-with Number Port</h3>");
			Reporter.log("Launching Browser <br>", true);
			Log.startTestCase("QA50_VerizonNonEdgeWithNumberPortCCInCA");

			selectCarrierResponderQA50(testtype);

			// Switching to previous tab.
			Utilities.switchPreviousTab();

			//Calling DBError utility to  find initial count or error in log files.
			DBError.navigateDBErrorPage();
			int initialCount= PageBase.AdminPage().totalErrorCount();

			// Switching to previous tab.
			Utilities.switchPreviousTab();

			orderId = poaCompleteFlowQA50(orderId);

			//Inventory Management and Ship admin Page verification.
			if(readConfig("Activation")=="true") {
				// Inventory Management Page verification.
				PageBase.InventoryManagementPage().launchInventoryInNewTab();
				PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber1, IMEIStatus.Sold.toString());

				//Ship Admin Verification -orderId= "";
				shipadminVerificationQA50(orderId);
			}
			//DBError Verification.
			DBError.navigateDBErrorPage();
			Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

			Reporter.log("<h3>QA50_VerizonNonEdgeWithNumberPortCCInCA - Test Case Completes<h3>");
			Log.endTestCase("QA50_VerizonNonEdgeWithNumberPortCCInCA");
		}
		catch(Exception ex)
		{
			Log.error(ex.getMessage());
			Utilities.driverTakesScreenshot("QA50_VerizonNonEdgeWithNumberPortCCInCA");
            Assert.fail();
		}
	}
	//endregion QA 50

    //region QA 5574
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_5574_VerizonNonEdgeWithDeposit(@Optional String testType) {
        String orderId = "";
        try {
            Log.startTestCase("QA_5574_VerizonNonEdgeWithDeposit");
            testType = BrowserSettings.readConfig("test-type");

            // Verify whether which enviorement to use internal or external.
            selectingCarrierEnviornment(testType);

            // Switching to previous tab.
           Utilities.switchPreviousTab();

            //Calling DBError utility to  find initial count or error in log files.
            DBError.navigateDBErrorPage();
            int initialCount = PageBase.AdminPage().totalErrorCount();

            // Switching to previous tab.
            Utilities.switchPreviousTab();

            orderId = poaCompleteFlow(testType);

            //Inventory Management Page verification.
            if (readConfig("Activation").contains("true")) {
                inventoryManagementVerification();

                //Ship Admin Verification -orderId= ""
               shipAdminVerification(orderId);
            }

            //DBError Verification.
            DBError.navigateDBErrorPage();
            Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

            Reporter.log("<h3>QA_5574_VerizonNonEdgeWithDeposit - Test Case Completes<h3>");
            Log.endTestCase("QA_5574_VerizonNonEdgeWithDeposit");
        } catch (Exception ex) {
            Log.error(ex.getMessage());
            Utilities.driverTakesScreenshot("QA_5574_VerizonNonEdgeWithDeposit");
            Assert.fail();
        }
    }
    //endregion QA 5574

    //region QA 57
    @Test(groups = {"verizon"})
    public void QA_57_VerizonNonEdgeSkipCreditCheck() throws IOException, AWTException, InterruptedException {
        String testType = BrowserSettings.readConfig("test-type");
        String orderId = "";

        Reporter.log("<h1>Start - QA_51_VerizonNonEdgeLikeForUnlike. <br></h1>");
        Reporter.log("<h3>Description: </h3> Verizon non-edge new activation , Run credit check in RTCC, CC result - "
                + "manual review and goes to support center. Roc team updates CC result - "
                + "approved with deposit manually. Order becomes non-xml");
        Reporter.log("Launching Browser <br>", true);
        Log.startTestCase("QA_51_VerizonNonEdgeExchange_LikeForUnlike");
        if (testType.equals("internal")) {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();

            if (testType.equals("internal")) {
                AdminBaseClass adminBaseClassInternla = new AdminBaseClass();
                adminBaseClassInternla.launchAdminInNewTab();
                PageBase.AdminPage().navigateToSimulator();

                if (readConfig("internalTestType").toLowerCase().equals("carrierresponder")) {
                    Reporter.log("<h3><U> Carrier Responder</U></h3>", true);

                    PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
                    PageBase.AdminPage().selectAPIConfig("Verizon");
                    Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
                    PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();

                    Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
                    PageBase.CarrierResponseXMLPage().selectOptions("current",
                            "submitCreditApplication", "manual_review.xml");

                    Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
                    PageBase.CarrierResponseXMLPage().saveResponseButton.click();

                    Reporter.log("<br>Carrier Responder Changes Done!!!", true);
                } else {
                    PageBase.AdminPage().selectWebAPIResponse("Verizon", "BackendSimulator");
                    PageBase.AdminPage().selectAPIConfig("Verizon");
                    PageBase.AdminPage().selectCreaditWriteUseCase("MANUAL_REVIEW");
                    PageBase.AdminPage().save();
                }
            } else   //External
            {
                AdminBaseClass adminBaseClassExternal = new AdminBaseClass();
                adminBaseClassExternal.launchAdminInNewTab();

                PageBase.AdminPage().navigateToSimulator();
                PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
                Reporter.log("<h3><U> External Server</U></h3>", true);
            }
            if (readConfig("internalTestType").toLowerCase().equals("carrierresponder")) {
                Reporter.log("<h3><U> Carrier Responder</U></h3>", true);

                PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
                PageBase.AdminPage().selectAPIConfig("Verizon");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
                PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();

                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
                PageBase.CarrierResponseXMLPage().selectOptions("current",
                        "submitCreditApplication", "manual_review.xml");

                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();

                Reporter.log("<br>Carrier Responder Changes Done!!!", true);
            } else {
                PageBase.AdminPage().selectWebAPIResponse("Verizon", "BackendSimulator");
                PageBase.AdminPage().selectAPIConfig("Verizon");
                PageBase.AdminPage().selectCreaditWriteUseCase("MANUAL_REVIEW");
                PageBase.AdminPage().save();
            }
        } else   //External
        {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
            Reporter.log("<h3><U> External Server</U></h3>", true);
        }

        Utilities.switchPreviousTab();

        lStartTime = new Date().getTime();
        pageName = readPageName("PoaLogin");
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on Sales & Activations page.
        lStartTime = new Date().getTime();
        pageName = readPageName("SaleAndActivation");
        PageBase.HomePageRetail().salesAndActivationsLink.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on New Activation link.
        lStartTime = new Date().getTime();
        pageName = readPageName("DeviceScan");
        PageBase.ChoosePathPage().newActivation.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);
        Reporter.log("<br> Declining Verizon Edge");
        PageBase.VerizonEdgePage().declineVerizonEdge();

        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();

        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        PageBase.CarrierCreditCheckPage().skip.click();
        Reporter.log("<br> Skip Credit Check");
        Thread.sleep(1000);
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().verizonMorePlanOnly);
        PageBase.VerizonShopPlansPage().selectPlanWithMore();
        PageBase.VerizonShopPlansPage().addPlan();
        Reporter.log("<br> Added a plaid plan");
        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        //Select Plan - Storing the Device and plan prices for further verification.
        cartDevice1price = PageBase.CartPage().device1Price.getText();

        //cartPlanprice = PageBase.CartPage().planPriceActual.getText();   // Fix it.
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        Reporter.log("<br> Selecting Feature");
        PageBase.SelectPlanFeaturesPage().selectFamilyBasePlan(0);
        Reporter.log("<br> Family Base Plan feature Selected");
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting No Insurance .
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();
        Reporter.log("<br> No Insurance for the device");
        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        Reporter.log("<br> No Number Porting Selected ");
        PageBase.CommonControls().continueButton.click();

        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.STATEID);
        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().firstNameTextbox);
        PageBase.ServiceProviderVerificationPage().populatingSPVPageAllMandateFields(customerDetails.FirstName,
                customerDetails.LastName, customerDetails.Address1, customerDetails.City, customerDetails.State,
                customerDetails.Zip, customerDetails.PhNum, customerDetails.EMail, customerDetails.BirthdayMonth,
                customerDetails.BirthdayDay, customerDetails.BirthdayYear, customerDetails.IDType,
                customerDetails.IDState, customerDetails.IDNumber, customerDetails.IDExpirationMonth,
                customerDetails.IDExpirationYear);
        Utilities.setValue(PageBase.CarrierCreditCheckPage().ssnTextBox,
                PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SSNWithoutDeposit));

        Reporter.log("<br> Entered Customer Details at the Service Provider Verification Page");
        Utilities.ClickElement(PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);
        if (testType.equals("internal")) Utilities.ClickElement(PageBase.CommonControls().continueButton);
        Utilities.ClickElement(PageBase.CommonControls().continueCommonButton);

        PageBase.TermsandConditionsPage().acceptTermsAndConditions();
        Reporter.log("<br> Terms and conditions accepted");
        Thread.sleep(20000);

        if (driver.getCurrentUrl().contains("payment")) {

            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA); //ToDo: Need to read from data sheet.
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }

        Utilities.waitForElementVisible(PageBase.CommonControls().supportCenterPageMessage);
        Reporter.log("<br> Support Center Page for Manual Review");
        orderId = PageBase.CommonControls().orderIdSupportCenterPage.getText();
        orderId = orderId.substring(1, orderId.length());
        Reporter.log("<h3><font color=green> Order Id is : " + orderId + "</font></h3>");

        ShipAdminBaseClass.launchShipAdminInNewTab();
        Reporter.log("Navigating to ship admin page for Manual Review");
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        Reporter.log("<br> Navigating to Order Summary Page for " + orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        Reporter.log("<br><U>" + PageBase.ShipAdminPage().manualReviewStatusOnShipAdmin.getText() + "</U>");
        Assert.assertEquals(PageBase.ShipAdminPage().manualReviewStatusOnShipAdmin.getText(),
                "Credit Check Required - Manual Review",
                "Status of the Order is not: Credit Check Required - Manual Review");

        Reporter.log("<br>" + PageBase.ShipAdminPage().creditCheckResultSummaryTable.getText() + " Table exists");
        PageBase.ShipAdminPage().updateCreditCheck.click();
        Reporter.log("<br> Clicked on Update button to edit Credit Check Results");

        Utilities.dropdownSelect(PageBase.ShipAdminPage().updateCreditCheckResult,
                SelectDropMethod.SELECTBYTEXT, "Credit Approved");

        Utilities.dropdownSelect(PageBase.ShipAdminPage().updateCreditCheckResultReason,
                SelectDropMethod.SELECTBYTEXT, "$0 deposit");
        Reporter.log("<br>Credit Approved with $0 deposit");
        Utilities.waitForElementVisible(PageBase.ShipAdminPage().numberOfLinesToUpdate);
        PageBase.ShipAdminPage().numberOfLinesToUpdate.sendKeys("1");

        PageBase.ShipAdminPage().addUpdateCreditCheck.click();
        Reporter.log("<br><U>" + PageBase.OrderSummaryPage().getOrderStatus() + "</U>");
        Utilities.switchPreviousTab();
        Reporter.log("<br> Switched back to POA After Manual Review");

        PageBase.CommonControls().continueSupportCenter.click();
        Reporter.log("<br> Capture Credit Card Details ");
        if (driver.getCurrentUrl().contains("payment")) {
            Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().creditCardNumberTextbox);
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA); //ToDo: Need to read from data sheet.
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }

        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        Reporter.log("<br> Credit Card Details Accepted");
        Assert.assertEquals(PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText(), orderId,
                "Order Id missmatch on Support Page and MSS page");
        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        //Payment Verification page. Scan Reciept id.
        Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
        Reporter.log("<br> Entered Receipt Id: " + receiptId + "<br>");
        PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);
        PageBase.PaymentVerificationPage().submitButton.click();


        Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
        PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(imei1);
        PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
        try {
            if (driver.findElement(By.id("scanerror")).isDisplayed()) {
                Assert.fail(driver.findElement(By.id("scanerror")).getText() + "<h2>Device is not aviable. Re-run the test case</h2.");
            }
        } catch (Exception e) {
            Reporter.log("No Error After Sim is submitted @ Device Verification and Activation Page");
        }
        driver.findElement(By.id(PageBase.DeviceVerificationaandActivation().simTypeM + imei1)).click();
        driver.findElement(By.id(PageBase.DeviceVerificationaandActivation().simTypeM + imei1)).sendKeys(simType1);
        Utilities.ScrollToElement(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);


        try {
            if (PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.isEnabled())
                PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
            PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");
        } catch (Exception e) {
            Reporter.log("CVN Number No Needed");
        }
        PageBase.CommonControls().continueActivation.click();
        do {
            Utilities.implicitWaitSleep(1000);
        } while (!driver.getCurrentUrl().contains("retail/support.htm"));
        Utilities.waitForElementVisible(PageBase.CommonControls().supportCenterPageMessage);

        ShipAdminBaseClass.launchShipAdminInNewTab();
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        try {
            if (!driver.findElement(By.xpath(".//*[@id='actFormDiv']/div[1]/input[1]")).isDisplayed()) {
                Utilities.dropdownSelect(driver.findElement(By.xpath("html/body/table[2]/tbody/tr[2]/th[4]/b/select")),
                        SelectDropMethod.SELECTBYTEXT, "Move queues");

                Utilities.dropdownSelect(driver.findElement(By.xpath(".//select[@name='ordsubstId']")),
                        SelectDropMethod.SELECTBYTEXT, "Awaiting Carrier Resolution");
                driver.findElement(By.xpath(".//select[@value='Move Queues']")).click();
                PageBase.OrderSummaryPage().rOCHomeLink.click();
                PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
            }
        } catch (Exception e) {

        }

        driver.findElement(By.xpath(".//*[@id='actOrdAccountFields']/tbody/tr[5]/td[2]/input")).sendKeys("598764987");
        driver.findElement(By.xpath(".//*[@id='actItemFieldsBlock']/table/tbody/tr[4]/td[2]/input")).sendKeys("6649728193");
        driver.findElement(By.xpath(".//*[@id='actItemFieldsBlock']/table/tbody/tr[8]/td/input[1]")).click();

        driver.findElement(By.xpath(".//*[@id='actFormDiv']/div[1]/input[1]")).click();
        try {
            if (driver.findElement(By.xpath(".//*[@id='actOrdAccountFields']/tbody/tr[9]/td[2]/input")).isEnabled()) {
                driver.findElement(By.xpath(".//*[@id='actOrdAccountFields']/tbody/tr[9]/td[2]/input")).sendKeys("123");
            }
        } catch (Exception e) {

        }
//		//NavigateBackToPage
//		//Activation Complete
        PageBase.OrderSummaryPage().rOCHomeLink.click();
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        Reporter.log(PageBase.OrderSummaryPage().getOrderStatus());
        if (!PageBase.OrderSummaryPage().getOrderStatus().toLowerCase().contains("shipped")) {
            try {
                Thread.sleep(1000);
                if (driver.findElement(By.xpath(".//*[@id='actFormDiv']/div[1]/input[1]")).isDisplayed()) {
                    driver.findElement(By.xpath(".//*[@id='actOrdAccountFields']/tbody/tr[5]/td[2]/input")).sendKeys("598764987");
                    driver.findElement(By.xpath(".//*[@id='actItemFieldsBlock']/table/tbody/tr[4]/td[2]/input")).sendKeys("6649728193");
                    driver.findElement(By.xpath(".//*[@id='actItemFieldsBlock']/table/tbody/tr[8]/td/input[1]")).click();
                    driver.findElement(By.xpath(".//*[@id='actOrdAccountFields']/tbody/tr[9]/td[2]/input")).sendKeys("123");
                    driver.findElement(By.xpath(".//*[@id='actFormDiv']/div[1]/input[1]")).click();
                    PageBase.OrderSummaryPage().rOCHomeLink.click();
                    PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
                }
            } catch (Exception e) {

            }
        }
        Assert.assertTrue(PageBase.OrderSummaryPage().getOrderStatus().toLowerCase().contains("shipped"));
        Reporter.log("<br> Order Moved to Shipped" + PageBase.OrderSummaryPage().getOrderStatus());
        Utilities.switchPreviousTab();
        Utilities.waitForElementVisible(driver.findElement(By.xpath(".//button[contains(text(),'continue')]")));
        driver.findElement(By.xpath(".//button[contains(text(),'continue')]")).click();
        Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
        Reporter.log("<h2><font color='green'> Test Case Completes </font></h2>");
        //		Reporter.log("<h4>Errors on Event Log: </h4>" + eventLogTableContent);

    }

    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_56_VerizonESecuritelDisabled(@Optional String testtype) throws InterruptedException, AWTException, IOException {

        testtype = "internal";
        if (testtype.equals("internal")) {
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
            //Selecting Use Case from dropdown list.
            PageBase.AdminPage().selectAPIConfig("Verizon");
        }
        Utilities.switchPreviousTab();


        //Calling DBError utility to  find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount = PageBase.AdminPage().totalErrorCount();

        // Switching to previous tab.
        Utilities.switchPreviousTab();

        String orderId = QA_56_POAFlow();
        QA_56_VerifyShipadminDetails(orderId);

        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

        Log.endTestCase("QA_56_VerizonNonEdgeExchange");
        Reporter.log("<h3>QA_56_VerizonNonEdgeExchange - Test Case Completes<h3>");
    }

    private String QA_56_POAFlow() throws InterruptedException, AWTException, IOException {
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0263"));

        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
        String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
        String esnNo = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
        String simNumber = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);

        //Click on the Sales and Activation Link
        Utilities.waitForElementVisible(PageBase.HomePageRetail().salesAndActivationsLink);
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        //Click on the new Activation Link
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().newActivation);
        PageBase.ChoosePathPage().newActivation.click();

        PageBase.DeviceScanPage().enterDeviceScanDetails(esnNo);//"8799098765778"
        //PageBase.VerizonEdgePage().declineVerizonEdge();
        Utilities.waitForElementVisible(PageBase.CommonControls().cancelButton);
        PageBase.CommonControls().cancelButton.click();

        Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);

        //Skip the Carrier Credit Check.
        Utilities.ClickElement(PageBase.CarrierCreditCheckPage().skip);

        // Selecting Plan.
        Utilities.ClickElement(PageBase.VerizonShopPlansPage().VerizonMoreEverything);

        String cartDeviceprice = PageBase.CartPage().device1Price.getText();
        Utilities.ClickElement(PageBase.CartPage().continueCartButton);

        // Selecting plan feature.
        Utilities.ClickElement(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);

        Utilities.waitForElementVisible(PageBase.NumberPortPage().continueSPVButton);
        PageBase.NumberPortPage().continueSPVButton.click();

        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().populateFormByClassButton);
        PageBase.ServiceProviderVerificationPage().populateFormByClassButton.click();
        // Enter data in Service Provider Verification page.
        //PageBase.ServiceProviderVerificationPage().populatingSPVPage
        //("Fred", "", "Consumer Two", "nobody@letstalk.com", IdType.DRIVERLICENCE,
        //		"CA", "123456789", Month.DECEMBER,
        //		2020, "9887303387", "April", 11, 1970);
        PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, customerDetails.LastName,
                customerDetails.EMail, IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber,
                customerDetails.IDExpirationMonth, Integer.parseInt(customerDetails.IDExpirationYear));

        Utilities.ClickElement(PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck);
        PageBase.ServiceProviderVerificationPage().continueSPVButton.click();

        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().continueSPVButton);
        PageBase.ServiceProviderVerificationPage().continueSPVButton.click();

        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        PageBase.CommonControls().continueCommonButton.click();

        //Accept the Terms and Conditions
        PageBase.TermsandConditionsPage().acceptTermsAndConditions();

        Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
        String orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
        PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

        //Device Verification Details
        Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
        PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);//"132710003003669460"
        PageBase.PaymentVerificationPage().submitButton.click();
        Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
        PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo);//"8799098765778"  // ToDo: Read from data sheet.
        PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
        PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber);//"21212121212121212121"
        // PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");  // ToDo: Read from data sheet.
        PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

        // Click on Accept Terms and Conditions
        Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox, 120);
        PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox.click();

        //Save Signature
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.TermsandConditionsPage().saveSignatureButton.click();
        PageBase.TermsandConditionsPage().continueTCButton.click();

        //Verify Order generated on Receipt Page
        Utilities.waitForElementVisible(PageBase.OrderReceiptPage().orderCompletionText);
        PageBase.OrderReceiptPage().verifyOrderCompletionPage();
        PageBase.CSVOperations().UpdateIMEIStatusToUsed(esnNo, FileName.SamsungGalaxyS4_16GBBlack);

        //Check inventory Details
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().verifyDeviceStatus(esnNo, "Sold");//"8799098765778"
        return orderId;
    }

    private void QA_56_VerifyShipadminDetails(String orderId) {
        //Check for order Details in Shipadmin
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
    }
    //endregion QA 56

    //region QA 5571
    @Test
    public void QA_5571_VerizonNonEdgeWithDeposit() throws InterruptedException, AWTException, IOException {

        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

		DBError.navigateDBErrorPage();
		int initialCount= PageBase.AdminPage().totalErrorCount();

		// Switching to previous tab.
		Utilities.switchPreviousTab();

		String orderId = QA_5571_PoaFlow();

		QA_79ShipAdminVerification(orderId);

		//DBError Verification.
		DBError.navigateDBErrorPage();
		Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
		Reporter.log("<h3>QA_5571_VerizonNonEdgeWithDeposit - Test Case Completes<h3>");
		Log.endTestCase("QA_5571_VerizonNonEdgeWithDeposit");

    }

	private String QA_5571_PoaFlow() throws IOException, AWTException, InterruptedException {
		CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
		NumPortingDetails portDetails = CSVOperations.ReadPortingDetails();
		String esnNo1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
		String esnNo2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
		String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
		String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);
		String simNumber2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);

		Utilities.waitForElementVisible(PageBase.HomePageRetail().salesAndActivationsLink);
		PageBase.HomePageRetail().salesAndActivationsLink.click();
		Utilities.waitForElementVisible(PageBase.ChoosePathPage().existingCarrier);
		PageBase.ChoosePathPage().existingCarrier.click();
		//		Utilities.waitForElementVisible(PageBase.PlanTypeSelectionPage().AALExistingAccount);
		//		PageBase.PlanTypeSelectionPage().AALExistingAccount.click();

		Utilities.waitForElementVisible(PageBase.PickYourPathPage().AALExistingAccount);
		PageBase.PickYourPathPage().AALExistingAccount.click();

		PageBase.CommonControls().continueButtonDVA.click();
		PageBase.UECVerificationPage().fillVerizonDetails("5598466345", "4850", "HELLO", "94109");
		PageBase.UECVerificationPage().continueVerizonButton.click();

		Utilities.waitForElementVisible(PageBase.SelectAnOptionPage().AALExistingFamilyPlan);
		PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
		PageBase.CommonControls().continueButtonDVA.click();

		PageBase.DeviceScanPage().enterDeviceScanDetails(esnNo1);//"78998767887645"
		//PageBase.VerizonEdgePage().declineVerizonEdge();
		Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
		PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

		Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
		PageBase.DeviceScanPage().enterDeviceScanDetails(esnNo2);//"8976785467898633"
		//PageBase.VerizonEdgePage().declineVerizonEdge();
		Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
		PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

		Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);
		Utilities.ClickElement(PageBase.CarrierCreditCheckPage().skip);

		Utilities.ClickElement(PageBase.CartPage().continueCartButton);

		//PageBase.SelectPlanFeaturesPage().selectFamilyBasePlan(0);
		List<WebElement> networkList = BrowserSettings.driver.findElements(By.xpath("//span[contains(text(),'Network Access')]/ancestor::a"));
		//Selecting Network Access plan.
		networkList.get(0).click();
		driver.findElement(By.xpath("(//span[contains(text(),'Network Access')])[2]")).click();
		//PageBase.SelectPlanFeaturesPage().selectNetworkAccessPlan(1);
		List<WebElement> netwkList = BrowserSettings.driver.findElements(By.xpath("//span[contains(text(),'FamilyBase')]/ancestor::a"));
		//Selecting Family Base plan.
		netwkList.get(1).click();
		driver.findElement(By.xpath("(//span[contains(text(),'FamilyBase')])[4]")).click();
		PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

		Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().guestReview);
		PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForTwoDevices();

		Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
		PageBase.NumberPortPage().numberPortRadiobutton.click();
		PageBase.CommonControls().continueButton.click();

		/*PageBase.PortMyNumbersPage().enterMultiplePortData(0, "9850045987", "Other", "572010311", "162364217",
                "Fred", "Consumer Two", "83144 O'Doula St.", "GARBAGY", "San Francisco", "CA",
                "94109");

		PageBase.PortMyNumbersPage().enterMultiplePortData(1, "9840345927", "Other", "572010311", "162364217",
				"Fred", "Consumer Two", "83144 O'Doula St.", "GARBAGY", "San Francisco", "CA",
				"94109");*/


		PageBase.PortMyNumbersPage().enterPortData(portDetails.CurrentPhoneNumber, portDetails.Carrier,portDetails.CurrentAccNumber, portDetails.SSN, customerDetails.FirstName, customerDetails.LastName,customerDetails.Address1,"", customerDetails.City, customerDetails.State, customerDetails.Zip);
		PageBase.PortMyNumbersPage().enterPortData(portDetails.CurrentPhoneNumber, portDetails.Carrier,portDetails.CurrentAccNumber, portDetails.SSN, customerDetails.FirstName, customerDetails.LastName,customerDetails.Address1,"", customerDetails.City, customerDetails.State, customerDetails.Zip);
		PageBase.CommonControls().continueButton.click();

	/*	PageBase.ServiceProviderVerificationPage().populatingSPVPage
                ("Fred", "", "Consumer Two", "nobody@letstalk.com", IdType.DRIVERLICENCE,
                        "CA", "123456789", Month.DECEMBER,
                        2020, "9850045987", "April", 11, 1970);*/
		PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, "", customerDetails.LastName, customerDetails.EMail, IdType.DRIVERLICENCE, customerDetails.City, customerDetails.IDNumber, Month.valueOf(customerDetails.IDExpirationMonth),
				Integer.parseInt(customerDetails.IDExpirationYear), customerDetails.PhNum, customerDetails.BirthdayMonth,
				Integer.parseInt(customerDetails.BirthdayDay), Integer.parseInt(customerDetails.BirthdayYear));
		Utilities.ClickElement(PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck);
		Utilities.ClickElement(PageBase.CommonControls().continueButton);
		Utilities.ClickElement(PageBase.CommonControls().continueButton);

		Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);

		// Terms and Condition Page.
		PageBase.TermsandConditionsPage().acceptTermsAndConditions();

		Utilities.implicitWaitSleep(10000);
		if (driver.getCurrentUrl().contains("payment")) {

            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA); //ToDo: Need to read from data sheet.
            Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
            Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
        }

		// MSS page.
		//ToDo: BAR Code verification after clarifying on the expected Bar code.
		Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
		String orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
		PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

		PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);//"132710003003669460"
		PageBase.PaymentVerificationPage().submitButton.click();

		Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
		PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo1);//"78998767887645"
		PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
		// ToDo: Read from data sheet.
		PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo2);//"8976785467898633"
		PageBase.DeviceVerificationaandActivation().submitDVAButton.click();

		PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber1);//"21212121212121212121"
		PageBase.DeviceVerificationaandActivation().simType2Textbox.sendKeys(simNumber2);//"23232323232323232323"

		try {
            PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");  // ToDo: Read from data sheet.
        } catch (Exception e) {
        }
		PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();
		Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
		PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
		PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

		Utilities.waitForElementVisible(PageBase.OrderReceiptPage().orderCompletionText);
		PageBase.OrderReceiptPage().verifyOrderCompletionPage();
		Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());

		//Check inventory Details
		PageBase.InventoryManagementPage().launchInventoryInNewTab();
		PageBase.InventoryManagementPage().verifyDeviceStatus(esnNo1, esnNo1, "Sold");

		return orderId;
	}

	private void QA_79ShipAdminVerification(String orderId) {
		ShipAdminBaseClass.launchShipAdminInNewTab();
		Utilities.implicitWaitSleep(5000);
		PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

		String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog("39659");
		String status = PageBase.OrderSummaryPage().getOrderStatus();
		Assert.assertEquals(status, Constants.SHIPPED);
		Assert.assertTrue(eventLogTableContent.contains(Constants.RECEIPT_SUBMISSION_SUCCEEDED_COMMENT));
		Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
		Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_SUCCEEDED_COMMENT));
		Assert.assertTrue(eventLogTableContent.contains(Constants.PARKING_SUCCEEDED_COMMENT));
		Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
		Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
		Assert.assertTrue(eventLogTableContent.contains(Constants.INQUIRING_NUMBER_PORT_ELIGIBILITY));
		Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
		Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
		Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.NUMBER_PORTABILITY));
		Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
	}
	//endregion QA 5571

	// region QA_67_Verizon Edge With Number Portability
@Test(groups = { "verizon" })
@Parameters("test-type")
public void QA_67_VerizonEdgeWithNumberPorting(@Optional String testType)
        throws InterruptedException, AWTException, IOException {
    String orderId = "";
    boolean stopActivation = false;
    final String imeiQA_67 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBWhite);
    try {
// Printing the begin of testcase
        Log.startTestCase("QA_67_VerizonEdgeWithNumberPorting");
// Verify whether which environment to use internal or external.
        testType = BrowserSettings.readConfig("test-type");
        if (testType.equals("internal")) {
// Need to set "Backend Simulator or Carrier Responder .
            setUpCarrierResponderForQA_67();
        } else {
//Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();
            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType,
                    "External");
        }
// Switching to Retail tab.
        Utilities.switchPreviousTab();
//Calling DBError utility to find initial count or error in log files.
        DBError.navigateDBErrorPage();
        int initialCount= PageBase.AdminPage().totalErrorCount();
// Switching to Retail tab.
        Utilities.switchPreviousTab();
        orderId = poaCompleteFlow_QA_67(orderId, imeiQA_67);
//Inventory Management and Ship admin Page verification.
        if(readConfig("Activation")=="true") {
// Inventory Management Page verification.
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(imeiQA_67, IMEIStatus.Sold.toString());
//Ship Admin Verification -orderId= "";
            shipadminVerificationQA50(orderId);
        }
//DBError Verification.
        DBError.navigateDBErrorPage();
        Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));
        Reporter.log("<h3>QA_67_VerizonEdgeWithNumberPorting - Test Case Completes<h3>");
        Log.endTestCase("QA_67_VerizonEdgeWithNumberPorting");
    } catch (Exception ex) {
        Log.error(ex.getMessage());
        System.out.println(ex.getMessage());
    }
}
//endregion QA_67_Verizon Edge With Number Portability

    //region QA 75
    @Test(groups = {"verizon"})
    @Parameters("test-type")
    public void QA_75_VerizonEdgeAttemptToEdgeWithDeposit(@Optional String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";
        imeiDetails = PageBase.CSVOperations().GetIMEIAndProductName(FileName.SamsungGalaxyS4_16GBBlack);

        // Adding Devices to Inventory.
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        PageBase.InventoryManagementPage().addDeviceToInventory(imeiDetails.IMEI,imeiDetails.ProductName);

        PageBase.InventoryManagementPage().closeInventoryTab();
        Utilities.switchPreviousTab();
        try {
            Log.startTestCase("QA_75_VerizonEdgeAttemptToEdgeWithDeposit");
            testType = BrowserSettings.readConfig("test-type");

            // Verify whether which enviorement to use internal or external.
            selectingCarrierEnviornment_75(testType);

            // Switching to previous tab.
           // Utilities.switchPreviousTab();

            //Calling DBError utility to  find initial count or error in log files.
            DBError.navigateDBErrorPage();
            int initialCount = PageBase.AdminPage().totalErrorCount();

            // Switching to previous tab.
            Utilities.switchPreviousTab();

            orderId = poaCompleteFlow_75(testType);

            //Inventory Management Page verification.
            if (readConfig("Activation").contains("true")) {
                inventoryManagementVerification_75();

                //Ship Admin Verification -orderId= ""
                shipAdminVerification_75(orderId);
            }

            //DBError Verification.
            DBError.navigateDBErrorPage();
            Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

            Reporter.log("<h3>QA_75_VerizonEdgeAttemptToEdgeWithDeposit - Test Case Completes<h3>");
            Log.endTestCase("QA_75_VerizonEdgeAttemptToEdgeWithDeposit");
        } catch (Exception ex) {
            Log.error(ex.getMessage());
            Utilities.driverTakesScreenshot("QA_75_VerizonEdgeAttemptToEdgeWithDeposit");
            Assert.fail();
        }
    }

    //endregion QA 5574
	//endregion Test Methods

   //region Private Methods and Refacotored Codes

    //region QA 5574  Refactored Methods
    private String poaCompleteFlow(@Optional String testType) throws IOException {
        String orderId = "";//Login to retail page.
        CreditCardDetails creditCard = new CreditCardDetails();
        creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);
        lStartTime = new Date().getTime();
        pageName = readPageName("PoaLogin");
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on Sales & Activations page.
        lStartTime = new Date().getTime();
        pageName = readPageName("SaleAndActivation");
        PageBase.HomePageRetail().salesAndActivationsLink.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on New Activation link.
        lStartTime = new Date().getTime();
        pageName = readPageName("DeviceScan");
        PageBase.ChoosePathPage().newActivation.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Scanning smart phone,basic phone and MIFI phone.
        imei1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBWhite);
        imei2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBWhite);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);
        PageBase.VerizonEdgePage().declineVerizonEdge();
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei2);
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);

        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();

        lStartTime = new Date().getTime();
        pageName = readPageName("CarrierCreditCheck");
        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {

        }
        //if(testType.equals("internal")) PageBase.CommonControls().continueButton.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Credit Check Verification Results with deposits.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        boolean exists = driver.findElements(By.id("checkbox-deposit-1")).size() != 0;
        if (exists) {
            Reporter.log("<br> Credit Check Completes.");
            PageBase.CreditCheckVerificationResultsPage().depositCheckBox.click();
            Reporter.log("<br> Selected Deposit Check Box ");
        } else {
            PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
        }
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().verizonMorePlanOnly);
        PageBase.VerizonShopPlansPage().selectPlanWithMore();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        //Select Plan - Storing the Device and plan prices for further verification.
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        cartDevice2price = PageBase.CartPage().device2Price.getText();
        //cartPlanprice = PageBase.CartPage().planPriceActual.getText();   // Fix it.
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().selectFamilyBasePlan(0);
        PageBase.SelectPlanFeaturesPage().selectNetworkAccessPlan(1);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting No Insurance .
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
        PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
                cartDevice1price);
		/*Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device2PriceActual.getText(),
					cartDevice2price);
			Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().planPrice.getText(),
					cartPlanprice);*/

		/*Assert.assertEquals(PageBase.OrderRevieandConfirmPage().device1ActivationFeeActual.getText(),
					PageBase.OrderRevieandConfirmPage().device1ActivationFeeExpected);
			Assert.assertEquals(PageBase.OrderRevieandConfirmPage().device2ActivationFeeActual.getText(),
					PageBase.OrderRevieandConfirmPage().device2ActivationFeeExpected);
			Assert.assertEquals(PageBase.OrderRevieandConfirmPage().device3ActivationFeeActual.getText(),
					PageBase.OrderRevieandConfirmPage().device3ActivationFeeExpected);
			Assert.assertEquals(PageBase.OrderRevieandConfirmPage().totalFeeActual.getText(),
					PageBase.OrderRevieandConfirmPage().totalFeeExpected);*/

        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().continueTCButton);
            PageBase.TermsandConditionsPage().emailTCChkBox.click();
            PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.TermsandConditionsPage().continueTCButton.click();

            // Credit Check Verification Results with deposits.
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
            boolean exist = driver.findElements(By.id("checkbox-mini-1")).size() != 0;
            if (exist) {
                PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
                PageBase.CommonControls().continueButtonDeposit.click();
            }

            // Credit card payment  page is coming.
            boolean visaexists = driver.findElements(By.id("radio-1a")).size() != 0;
            if (visaexists) {
                Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().visaTab);
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA);
                PageBase.PaymentRequiredPage().continuePRButton.click();
            }

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            PageBase.PrintMobileScanSheetPage().verifyAllTwoDeviceBarCode();

            //TODO:Need to add assertion for store location.
            //		WebElement web = driver.findElement(By.xpath("//span[contains(text(),'2766 - TARGET - SAN FRANCISCO CENTRAL')]"));
            //		String storeLocation = web.getText();
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId));
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor3Devices(imei1, imei2, "");

            // WCA Signature page activity and verifications.
            Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);

            List<WebElement> wcaLists = driver.findElements(By.xpath("//div[@class ='termsandcondition'][1]/label"));
            Assert.assertEquals("Application ID No.:", wcaLists.get(0).getText());
            Assert.assertEquals("Order Date:", wcaLists.get(1).getText());
            Assert.assertEquals("Bill Acct. No.:", wcaLists.get(2).getText());
            Assert.assertEquals("Agent Name:", wcaLists.get(3).getText());
            Assert.assertEquals("Activation Type:", wcaLists.get(4).getText());

            PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

            // Order Activation Complete page.
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().ActivationComplete);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
            String orderIdfromActPage = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
            Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
            //Assert.assertTrue(PageBase.OrderActivationCompletePage().device2PhoneNo.isDisplayed());
            Assert.assertTrue(PageBase.OrderActivationCompletePage().iMEINumberValueText.isDisplayed());
            //Assert.assertTrue(PageBase.OrderActivationCompletePage().device2IMEINo.isDisplayed());
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void inventoryManagementVerification() throws InterruptedException, AWTException, IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.InventoryManagementPage().verifyDeviceStatus(imei1, imei2, IMEIStatus.Sold.toString());
    }

    private void shipAdminVerification(String orderId) {
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

    private void selectingCarrierEnviornment(@Optional String testType) throws InterruptedException, AWTException, IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            selectCarrierResponderQA5574();

            //Selecting Carrier Responder
            //selectCarrierResponderQA5574();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
        }
    }

    private CarrierCreditCheckDetails getCarrierCreditCheckDetails() throws IOException {
        CarrierCreditCheckDetails cccDetails = new CarrierCreditCheckDetails();
        PageBase.CSVOperations();
        CustomerDetails customerDetails = CSVOperations.ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
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
        cccDetails.setIDType(IdType.DRIVERLICENCE);
        cccDetails.setIdTypeState(customerDetails.IDState);
        cccDetails.setIdNumber(customerDetails.IDNumber);
        cccDetails.setMonth(customerDetails.IDExpirationMonth);
        cccDetails.setYear(customerDetails.IDExpirationYear);
        return cccDetails;
    }

    private void selectCarrierResponderQA5574() {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Verizon");

        // Selecting Verizon and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved_with_deposit.xml");
        Utilities.implicitWaitSleep(3000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        //PageBase.CarrierResponseXMLPage().loadResponseButton.click();
    }

    private void selectingBackendSimulatorForQA5574() {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "BackendSimulator");

        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Verizon");

        PageBase.AdminPage().selectCreaditReadUseCase("APPROVE_WITH_DEPOSIT");
        PageBase.AdminPage().selectCreaditWriteUseCase("APPROVE_WITH_DEPOSIT");
        PageBase.AdminPage().retrieveCustomerDetails("ELIGIBLE");

        List<WebElement> phoneList = PageBase.AdminPage().phoneList.findElements(By.className("phoneNumberRow"));

        if (phoneList.size() > 0) {
            List<WebElement> removePhone = PageBase.AdminPage().phoneList.findElements(By.id("retrieveCustomerDetails_removePhoneNumberRowButton"));
            for (int i = 0; i < removePhone.size(); i++) {
                removePhone.get(i).click();
            }
        }

        PageBase.AdminPage().accountPlanType("Family Share");
        PageBase.AdminPage().retrieveExistingCustomerInstallmentsDetails("SUCCESS_WITH_PAYMENT");
        PageBase.AdminPage().retrievePricePlan("SUCCESS");
        PageBase.AdminPage().submitActivation("SUCCESS");
        PageBase.AdminPage().submitReciept("SUCCESS");
        PageBase.AdminPage().submitServiceDetails("SUCCESS");
        PageBase.AdminPage().submitEdgeUpPayment("SUCCESS");
        PageBase.AdminPage().returnOrExchangeDevice("SUCCESS");

        PageBase.AdminPage().save();
    }

    //endregion QA 5574 Refactored Methods

	private String poaCompleteFlowQA50 (String orderId)  throws IOException
	{
		CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(IdType.DRIVERLICENCE);
		NumPortingDetails portDetails = PageBase.CSVOperations().ReadPortingDetails();

		//Login to retail page.
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

		// Click on Sales & Activations page.
        Utilities.ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);

		// Click on New Activation link.
		PageBase.ChoosePathPage().newActivation.click();

		// Scanning 3 Verizon smart phones.
		PageBase.DeviceScanPage().enterDeviceScanDetails(iMEINumber1);
		PageBase.VerizonEdgePage().declineVerizonEdge();
		//PageBase.DeviceScanPage().enterDeviceScanDetails(imei2);
		//PageBase.DeviceScanPage().enterDeviceScanDetails(imei3);
		Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);

		//Skip the Carrier Credit Check.
		Utilities.ClickElement(PageBase.CarrierCreditCheckPage().skip);

		// Selecting Plan.
		Utilities.ClickElement(PageBase.VerizonShopPlansPage().VerizonMoreEverything);

		// Storing the Device and plan prices for further verification.
		cartDevice1price = PageBase.CartPage().device1Price.getText();
		//cartDevice2price = PageBase.CartPage().device2Price.getText();
		//cartDevice3price = PageBase.CartPage().device3Price.getText();
		//cartPlanprice = PageBase.CartPage().planPriceActual.getText();

		Utilities.ClickElement(PageBase.CartPage().continueCartButton);

		// Selecting plan feature.
		Utilities.ClickElement(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);

		// Selecting NO Insurance.
		PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForThreeDevices();

		// Selecting Number Porting.
		Utilities.ClickElement(PageBase.NumberPortPage().numberPortRadiobutton);
		Utilities.ClickElement(PageBase.CommonControls().continueButton);

		PageBase.PortMyNumbersPage().enterPortData(portDetails.CurrentPhoneNumber, portDetails.Carrier,
				portDetails.CurrentAccNumber, portDetails.SSN, customerDetails.FirstName, customerDetails.LastName,
				customerDetails.Address1, "", customerDetails.City, customerDetails.State, customerDetails.Zip);

		// Enter data in Service Provider Verification page.
		PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, "",  customerDetails.LastName,
				customerDetails.EMail, IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber,
				Month.valueOf(customerDetails.IDExpirationMonth.toUpperCase()), Integer.parseInt(customerDetails.IDExpirationYear),
				portDetails.CurrentPhoneNumber, customerDetails.BirthdayMonth, Integer.parseInt(customerDetails.BirthdayDay),
				Integer.parseInt(customerDetails.BirthdayYear));
		Utilities.ClickElement(PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck);
		Utilities.ClickElement(PageBase.CommonControls().continueButton);
		Utilities.ClickElement(PageBase.CommonControls().continueButton);

		// Order Review and Confirm Page.   //ToDo: Need to read from data sheet.
		Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
				cartDevice1price);
			/*Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device2PriceActual.getText(),
					cartDevice2price);
			Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device3PriceActual.getText(),
					cartDevice3price);
			Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().planPrice.getText(),
					cartPlanprice);*/

			/*Assert.assertEquals(PageBase.OrderRevieandConfirmPage().device1ActivationFeeActual.getText(),
					PageBase.OrderRevieandConfirmPage().device1ActivationFeeExpected);
			Assert.assertEquals(PageBase.OrderRevieandConfirmPage().device2ActivationFeeActual.getText(),
					PageBase.OrderRevieandConfirmPage().device2ActivationFeeExpected);
			Assert.assertEquals(PageBase.OrderRevieandConfirmPage().device3ActivationFeeActual.getText(),
					PageBase.OrderRevieandConfirmPage().device3ActivationFeeExpected);
			Assert.assertEquals(PageBase.OrderRevieandConfirmPage().totalFeeActual.getText(),
					PageBase.OrderRevieandConfirmPage().totalFeeExpected);*/
		Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);

		// Terms and Condition Page.
		PageBase.TermsandConditionsPage().acceptTermsAndConditions();

		// Credit Card Payment Page
		Utilities.implicitWaitSleep(10000);
		if( driver.getCurrentUrl().contains("payment"))
		{
			PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA);
			Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
			Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
		}

		// MSS page.
		//ToDo: BAR Code verification after clarifying on the expected Bar code.
		orderId= PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
		Utilities.ClickElement(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);

		//Assert.assertTrue(PageBase.PrintMobileScanSheetPage().barcodePMSSImage.isDisplayed());
		//Assert.assertTrue(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.isDisplayed());
			/*PageBase.PrintMobileScanSheetPage().barcodePMSSImage2.isDisplayed();
			PageBase.PrintMobileScanSheetPage().barcodePMSSImage3.isDisplayed();*/

		// Payment Verification page.
		PageBase.PaymentVerificationPage().paymentVerification(receiptId);

		// Device Verification and Activation page.
		//PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor3Devices(imei1,imei2,imei3);
		PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei3);

		// WCA Signature page.
		Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
		PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
		PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

		// Order Activation Complete page.
		Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
		//orderId = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
		//Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
			/*Assert.assertTrue(PageBase.OrderActivationCompletePage().device2PhoneNo.isDisplayed());
			Assert.assertTrue(PageBase.OrderActivationCompletePage().device3PhoneNo.isDisplayed());*/
		//Assert.assertTrue(PageBase.OrderActivationCompletePage().iMEINumberValueText.isDisplayed());
			/*Assert.assertTrue(PageBase.OrderActivationCompletePage().device2IMEINo.isDisplayed());
			Assert.assertTrue(PageBase.OrderActivationCompletePage().device3IMEINo.isDisplayed());	*/

		return orderId;
	}

	private void shipadminVerificationQA50(String orderId)
	{
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
		Assert.assertTrue(eventLogTableContent.contains(Constants.INQUIRING_NUMBER_PORT_ELIGIBILITY));
		Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
		Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
		Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.NUMBER_PORTABILITY));
		Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
	}

	private void selectCarrierResponderQA50(@Optional String testtype) throws IOException, InterruptedException, AWTException
	{
		// Verify whether which environment to use internal or external.
		testtype = BrowserSettings.readConfig("test-type");
		if(testtype.equals("internal")){
			// Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
			AdminBaseClass adminBaseClass = new AdminBaseClass();
			adminBaseClass.launchAdminInNewTab();

			PageBase.AdminPage().navigateToSimulator();
			PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));

			//Selecting Use Case from dropdown list.
			PageBase.AdminPage().selectAPIConfig(carrierType);

			//Customizing xml files in Carrier Responder
			PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
			PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
			Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
			Utilities.implicitWaitSleep(4000);
			PageBase.CarrierResponseXMLPage().saveResponseButton.click();
			PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "new_activation_approved_no_edge.xml");
			Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
			Utilities.implicitWaitSleep(4000);
			PageBase.CarrierResponseXMLPage().saveResponseButton.click();
			PageBase.CarrierResponseXMLPage().selectOptions("current", "validatePortInEligibility", "default_1_line.xml");
			Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
			Utilities.implicitWaitSleep(4000);
			PageBase.CarrierResponseXMLPage().saveResponseButton.click();
			PageBase.CarrierResponseXMLPage().selectOptions("current", "submitPortInDetails", "default.xml");
			Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
			Utilities.implicitWaitSleep(4000);
			PageBase.CarrierResponseXMLPage().saveResponseButton.click();
			PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveNpaNxx", "single_area_code.xml");
			Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
			Utilities.implicitWaitSleep(4000);
			PageBase.CarrierResponseXMLPage().saveResponseButton.click();
		}
		else{   //External// Need to set External server from Admin page.
			AdminBaseClass adminBaseClass = new AdminBaseClass();
			adminBaseClass.launchAdminInNewTab();

			PageBase.AdminPage().navigateToSimulator();
			PageBase.AdminPage().selectWebAPIResponse(carrierType, "External");
		}
	}

	public String VerizonActivateVerizonNonedgeMultipleLines(CustomerDetails customerDetails) throws IOException {

		//PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

		String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
		String esnNo1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
		String esnNo2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
		String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);
		String simNumber2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);

		//Click on the Sales and Activation Link
		Utilities.waitForElementVisible(PageBase.HomePageRetail().salesAndActivationsLink);
		PageBase.HomePageRetail().salesAndActivationsLink.click();

		//Click on the new Activation Link
		Utilities.waitForElementVisible(PageBase.ChoosePathPage().newActivation);
		PageBase.ChoosePathPage().newActivation.click();

		//Scan a Device and Click on 'No' Option
		PageBase.DeviceScanPage().enterDeviceScanDetails(esnNo1); //"6007003004006"
		//PageBase.VerizonEdgePage().declineVerizonEdge();
		Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
		PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

		//Scan Second Device
		Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
		PageBase.DeviceScanPage().enterDeviceScanDetails(esnNo2);//"6007003004007"
		//PageBase.VerizonEdgePage().declineVerizonEdge();
		Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
		PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

		//Skip the Credit Check
		Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);
		Utilities.ClickElement(PageBase.CarrierCreditCheckPage().skip);

		//Choose a Plan
		Utilities.ClickElement(PageBase.VerizonShopPlansPage().VerizonMoreEverything);

		//Click on continue from Cart PAge
		Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
		Utilities.ClickElement(PageBase.CartPage().continueCartButton);

		//Select 'Network Access' and 'Family Base' Features for Each Device
		//PageBase.SelectPlanFeaturesPage().selectFamilyBasePlan(0);
		List<WebElement> networkList = BrowserSettings.driver.findElements(By.xpath("//span[contains(text(),'Network Access')]/ancestor::a"));
		//Selecting Network Access plan.
		networkList.get(0).click();
		driver.findElement(By.xpath("(//span[contains(text(),'Network Access')])[2]")).click();
		//PageBase.SelectPlanFeaturesPage().selectNetworkAccessPlan(1);
		List<WebElement> netwkList = BrowserSettings.driver.findElements(By.xpath("//span[contains(text(),'FamilyBase')]/ancestor::a"));
		//Selecting Family Base plan.
		netwkList.get(1).click();
		driver.findElement(By.xpath("(//span[contains(text(),'FamilyBase')])[4]")).click();
		PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

		//Do not select an insurance for any of the Device
		Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().guestReview);
		PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForTwoDevices();

		//Click on 'No number port' Option
		Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
		PageBase.NumberPortPage().noNumberPortRadiobutton.click();
		PageBase.CommonControls().continueButton.click();

		// Fill SPV details
		//Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().populateForm);
		PageBase.ServiceProviderVerificationPage().populateFormByClassButton.click();
		//PageBase.ServiceProviderVerificationPage().populatingSPVPage
		//		("Fred", "", "Consumer Two", "nobody@letstalk.com", IdType.DRIVERLICENCE,
		//				"CA", "123456789", ServiceProviderVerificationPage.Month.DECEMBER,
		//				2020, "9882303487", "April", 11, 1970);

		PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, "", customerDetails.LastName, customerDetails.EMail, IdType.DRIVERLICENCE, customerDetails.City, customerDetails.IDNumber, Month.valueOf(customerDetails.IDExpirationMonth),
				Integer.parseInt(customerDetails.IDExpirationYear), customerDetails.PhNum, customerDetails.BirthdayMonth,
				Integer.parseInt(customerDetails.BirthdayDay), Integer.parseInt(customerDetails.BirthdayYear));

		Utilities.ClickElement(PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck);
		Utilities.ClickElement(PageBase.CommonControls().continueButton);
		Utilities.ClickElement(PageBase.CommonControls().continueButton);

		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		PageBase.CommonControls().continueCommonButton.click();

		//Accept the Terms and Conditions
		PageBase.TermsandConditionsPage().acceptTermsAndConditions();

		Utilities.implicitWaitSleep(10000);
		if (driver.getCurrentUrl().contains("payment")) {

			PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA); //ToDo: Need to read from data sheet.
			Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
			Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
		}

		// MSS page.
		//ToDo: BAR Code verification after clarifying on the expected Bar code.
		Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
		String orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
		PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

		//Enter Receipt ID
		PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);//"132710003003669460"
		PageBase.PaymentVerificationPage().submitButton.click();

		//Enter Both Device Details
		Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
		PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo1);//"6007003004006"
		PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
		// ToDo: Read from data sheet.
		PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo2);//"6007003004007"
		PageBase.DeviceVerificationaandActivation().submitDVAButton.click();

		//Enter Sim Details
		PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber1);//"21212121212121212121"
		PageBase.DeviceVerificationaandActivation().simType2Textbox.sendKeys(simNumber2);//"23232323232323232323"

		try {
			PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");  // ToDo: Read from data sheet.
		} catch (Exception e) {
		}
		PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

		//Sign and Accept Terms and Conditions on WCA Page
		Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
		PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
		PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

		// Order Receipt Page
		Utilities.waitForElementVisible(PageBase.OrderReceiptPage().orderCompletionText);
		PageBase.OrderReceiptPage().verifyOrderCompletionPage();
		Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
		PageBase.CSVOperations().UpdateIMEIStatusToUsed(esnNo1, FileName.SamsungGalaxyS4_16GBBlack);
		PageBase.CSVOperations().UpdateIMEIStatusToUsed(esnNo2, FileName.SamsungGalaxyS4_16GBBlack);
		return orderId;
	}

	public void VerizonExchangeDevice(String OrderId, CustomerDetails customerDetails) throws IOException {

		String receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId);
		String esnNo = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);

		//Click on Guest Lookup Tab
		Utilities.waitForElementVisible(PageBase.HomePageRetail().guestLookupTab);
		PageBase.HomePageRetail().guestLookupTab.click();
		Utilities.waitForElementVisible(PageBase.CustomerLookupPage().receiptIdTextbox);
		PageBase.CustomerLookupPage().receiptIdTextbox.sendKeys(receiptId);//"132710003003669460"

		List<WebElement> SubmitButtons = driver.findElements(By.xpath(ControlLocators.SUBMIT_RECEIPTID));
		for (WebElement visibleSubmitButton : SubmitButtons) {
			if (visibleSubmitButton.isDisplayed()) {
				visibleSubmitButton.click();
				break;
			}
		}

		//Select the Order
		driver.navigate().to(readConfig("CustomerVerification") + OrderId);

		//Enter the Customer Details for Verification
		Utilities.waitForElementVisible(PageBase.ReturnOrExchangeVerificationPage().cvFirstNameTextbox);
		//PageBase.ReturnOrExchangeVerificationPage().populatingPage(IdType.DRIVERLICENCE, "CA", "123456789", "FredFred", "", "Consumer Two");
		PageBase.ReturnOrExchangeVerificationPage().populatingPage(IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber, customerDetails.FirstName, "", customerDetails.LastName);
		PageBase.CommonControls().continueCommonButton.click();

		//Enter the ESN/EMEI number for the device to be exchanged
		Utilities.waitForElementVisible(PageBase.ReturnScanDevicePage().esnIemeidTextbox);
		PageBase.ReturnScanDevicePage().esnIemeidTextbox.sendKeys(esnNo);//"6007003004006"
		PageBase.CommonControls().continueButtonDVA.click();

		Utilities.waitForElementVisible(PageBase.ReturnOrExhangePreConditions().deviceAccessoryRadioButton);
		PageBase.ReturnOrExhangePreConditions().SelectPreconditions();
		PageBase.ReturnOrExhangePreConditions().continueREVButton.click();

		//Select more Exchange Details
		Utilities.waitForElementVisible(PageBase.ReturningProcessPage().acceptReturnExchangeRadioButton);
		PageBase.ReturningProcessPage().acceptReturnExchangeRadioButton.click();
		PageBase.ReturningProcessPage().exchangeDeviceRadioButton.click();
		PageBase.ReturningProcessPage().SelectFinancingOption(ReturnProcessPage.FinancingOption.CHECKFINANCING);
		Assert.assertEquals(PageBase.ReturningProcessPage().exchangeMessage.getText(), Constants.EXCHANGE_MESSAGE);
		Assert.assertTrue(PageBase.ReturningProcessPage().continueExchangeButton.isDisplayed());
		PageBase.ReturningProcessPage().SelectExchangeReason(ReturnProcessPage.ExchangeReason.INPOLICYGUESTRETURN);
		PageBase.CommonControls().continueButtonDVA.click();

		Utilities.waitForElementVisible(PageBase.AccountPasswordPage().continueButton);
		PageBase.AccountPasswordPage().accountPassword.sendKeys("HELLO");
		PageBase.AccountPasswordPage().continueButton.click();

		//Deactivate via ShipAdmin
		ShipAdminBaseClass.launchShipAdminInNewTab();
		Utilities.implicitWaitSleep(5000);
		PageBase.OrderSummaryPage().SelectActivationInfo(OrderId);
		Utilities.switchPreviousTab();
	}

	public String VerizonExchangetoNonEdgeUpgradeFlow(CustomerDetails customerDetails) throws InterruptedException, AWTException, IOException {
		//Click on UEC Link
		Utilities.waitForElementVisible(PageBase.HomePageRetail().upgradeEligibilityCheckerLink);
		PageBase.HomePageRetail().upgradeEligibilityCheckerLink.click();
		Utilities.waitForElementVisible(PageBase.UECVerificationPage().verizonTab);
		PageBase.UECVerificationPage().verizonTab.click();

		String esnNo = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.SamsungGalaxyS4_16GBBlack);
		String simNumber = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.Sim_3FF);
		//Enter Verizon Details
		// PageBase.UECVerificationPage().fillVerizonDetails("9882303487", "4850", "HELLO", "94109");
		PageBase.UECVerificationPage().fillVerizonDetails(customerDetails.PhNum,customerDetails.SSN,"",customerDetails.Zip);
		PageBase.UECVerificationPage().continueVerizonButton.click();

		//Click on the First Number that appears
		Utilities.waitForElementVisible(PageBase.UECAddLinesPage().firstAALCheckbox);
		PageBase.UECAddLinesPage().firstAALCheckbox.click();
		PageBase.UECAddLinesPage().continueUECAddLinesButton.click();

		//Scann a Device
		Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
		PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(esnNo);//"889988990007"
		PageBase.DeviceScanPage().submitDeviceButton.click();

		Utilities.waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
		PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();

		//Enter the Customer Information
		PageBase.ServiceProviderVerificationPage().populateFormByClassButton.click();
        /*PageBase.ServiceProviderVerificationPage().populatingSPVPage
				("Fred", "", "Consumer Two", "nobody@letstalk.com", IdType.DRIVERLICENCE,
						"CA", "123456789", ServiceProviderVerificationPage.Month.DECEMBER,
						2020, "9880203387", "April", 11, 1970);*/
		PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
		PageBase.CommonControls().continueButton.click();
		PageBase.CommonControls().continueButton.click();

		//Choose the Edge Option
		Utilities.waitForElementVisible(PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton);
		PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton.click();
		PageBase.CommonControls().continueCommonButton.click();

		//Choose an Existing Plan in Shop Plans
		Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().KeepMyExistingVerizonWirelessLegacyAddButton);
		PageBase.VerizonShopPlansPage().KeepMyExistingVerizonWirelessLegacyAddButton.click();

		Utilities.waitForElementVisible(PageBase.CartPage().firstAssignNumberDropdown);
		Utilities.dropdownSelect(PageBase.CartPage().firstAssignNumberDropdown, SelectDropMethod.SELECTBYINDEX, "1");
		String phonePrice = PageBase.CartPage().firstPhonePriceText.getText();
		String phoneModel = PageBase.CartPage().firstPhoneModelLink.getText();
		PageBase.CartPage().continueCartButton.click();

		Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
		//PageBase.VerizonSelectPlanFeaturesPage().basicPhoneDiscount0Checkbox.click();
		PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

		//Select Protection Plan Insurance Page
		Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
		PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
		PageBase.CommonControls().continueButton.click();

		Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
		PageBase.CommonControls().continueCommonButton.click();

		//Accept the Terms and Conditions
		Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox);
		PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
		PageBase.TermsandConditionsPage().continueTCButton.click();
		Thread.sleep(1000);

		try {
			Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().sameAddressTab);
			PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA); //ToDo: Need to read from data sheet.
			Utilities.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
			Utilities.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
		} catch (Exception e) {
		}

		// MSS page.
		//ToDo: BAR Code verification after clarifying on the expected Bar code.
		Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
		String orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
		PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

		PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(receiptId);//"132710003003669460"
		PageBase.PaymentVerificationPage().submitButton.click();

		Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
		PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(esnNo);//"889988990007"
		PageBase.DeviceVerificationaandActivation().submitDVAButton.click();

		PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber);//21212121212121212121

		try {
			PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");  // ToDo: Read from data sheet.
		} catch (Exception e) {
		}
		PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();
		Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
		PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
		PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

		//Print the Details
		PageBase.DeviceFinancingInstallmentContractPage().PrintDeviceFinancingDetails(driver);

		//Verify that the Order is Successfully Created
		Utilities.waitForElementVisible(PageBase.OrderReceiptPage().orderCompletionText);
		PageBase.OrderReceiptPage().verifyOrderCompletionPage();
		Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
		//String orderId = PageBase.OrderSummaryPage().orderNumberText.getText().replace("#","");

		//Check inventory Details
		PageBase.InventoryManagementPage().launchInventoryInNewTab();
		PageBase.InventoryManagementPage().verifyDeviceStatus(esnNo, "Sold");//"889988990007"
		return orderId;
	}

	private void QA_78ShipadminVerification(String orderId) {
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
	}

	private void setUpCarrierResponderForQA_67() throws InterruptedException, AWTException, IOException {
		AdminBaseClass adminBaseClass = new AdminBaseClass();
		adminBaseClass.launchAdminInNewTab();
		PageBase.AdminPage().navigateToSimulator();
		PageBase.AdminPage().selectWebAPIResponse(carrierType,
				BrowserSettings.readConfig("internalTestType"));
// Selecting Use Case from dropdown list.
		PageBase.AdminPage().selectAPIConfig(carrierType);
// Customizing xml files in Carrier Responder
		PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
		PageBase.CarrierResponseXMLPage().selectOptions("current",
				"submitCreditApplication", "approved.xml");
		Utilities.waitForElementVisible(PageBase
				.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(4000);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();
// PageBase.CarrierResponseXMLPage().selectOptions("current",
// "retrieveCreditApplication",
// "new_activation_approved_edge.xml");
// PageBase.CarrierResponseXMLPage().saveResponseButton.click();
		PageBase.CarrierResponseXMLPage().selectOptions("current",
				"validatePortInEligibility", "default_1_line.xml");
		Utilities.waitForElementVisible(PageBase
				.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(4000);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();
		PageBase.CarrierResponseXMLPage().selectOptions("current",
				"submitPortInDetails", "default.xml");
		Utilities.waitForElementVisible(PageBase
				.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(4000);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();
		PageBase.CarrierResponseXMLPage().selectOptions("current",
				"retrieveNpaNxx", "single_area_code.xml");
		Utilities.waitForElementVisible(PageBase
				.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(4000);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();
		PageBase.CarrierResponseXMLPage().selectOptions("current",
				"retrieveCreditApplication",
				"new_activation_approved_edge.xml");
		Utilities.waitForElementVisible(PageBase
				.CarrierResponseXMLPage().xmlTextArea);
		Utilities.implicitWaitSleep(4000);
		PageBase.CarrierResponseXMLPage().saveResponseButton.click();
	}

	private String poaCompleteFlow_QA_67(String orderId, String imeiQA_67) throws IOException {
		PageBase.LoginPageRetail().poaLogin(
				Utilities.getCredentials("tuserUN"),
				Utilities.getCredentials("tuserPwd"),
				Utilities.getCredentials("storeId0003"));
// Click on Sales & Activations page.
		Utilities
				.ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);
// Click on New Activation link.
		PageBase.ChoosePathPage().newActivation.click();
		PageBase.DeviceScanPage().enterDeviceScanDetails(imeiQA_67);
// Accept Edge
		Utilities
				.waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
		PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();
// Running Credit Check
		Utilities
				.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
		CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
		PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(
				cccDetails);
		PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
		lStartTime = new Date().getTime();
		pageName = readPageName("CarrierCreditCheck");
		PageBase.CommonControls().continueButton.click();
		Utilities.implicitWaitSleep(1000);
		try {
			if (PageBase.CommonControls().continueButton.isEnabled())
				PageBase.CommonControls().continueButton.click();
		} catch (Exception e) {
		}
// Verizon Edge
		Utilities
				.waitForElementVisible(PageBase.VerizonEdgePage().customerDownPayment);
		PageBase.VerizonEdgePage().monthlyDownPayment.click();
		PageBase.CartPage().continueCartButton.click();
// Verizon Shop Plans Page
		Utilities
				.waitForElementVisible(PageBase.VerizonShopPlansPage().verizonMoreEverythingUnlimitedMinutesAndMessaging500MBDataAddButton);
		PageBase.VerizonShopPlansPage().verizonMoreEverythingUnlimitedMinutesAndMessaging500MBDataAddButton
				.click();
		String phonePrice = PageBase.CartPage().device1Price.getText();
		System.out.println("-----------------------------" + phonePrice);
		PageBase.CartPage().continueCartButton.click();
// Verizon Select Plan Features Page
		Utilities.waitForElementVisible(PageBase
				.VerizonSelectPlanFeaturesPage().continueSPFButton);
// PageBase.VerizonSelectPlanFeaturesPage().basicPhoneDiscount0Checkbox.click();
		PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();
// Select Protection Plan Insurance Page
		Utilities.waitForElementVisible(PageBase
				.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
		PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance
				.click();
		Utilities.implicitWaitSleep(5000);
		PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
		PageBase.CommonControls().continueButton.click();
// Selecting Number Porting.
		Utilities.ClickElement(PageBase.NumberPortPage().numberPortRadiobutton);
		Utilities.ClickElement(PageBase.CommonControls().continueButton);
		PageBase.PortMyNumbersPage().populatePortData();
		Utilities.ClickElement(PageBase.CommonControls().continueCommonButton);
// Terms and Condition Page.
		PageBase.TermsandConditionsPage().acceptTermsAndConditions();
// Credit Card Payment Page
		Utilities.implicitWaitSleep(10000);
		if (driver.getCurrentUrl().contains("payment")) {
			PageBase.PaymentRequiredPage()
					.populatingCardDetailsPaymentRequired(CardType.VISA);
			Utilities
					.ClickElement(PageBase.PaymentRequiredPage().sameAddressTab);
			Utilities
					.ClickElement(PageBase.PaymentRequiredPage().continuePRButton);
		}
		if(readConfig("Activation").contains("true")) {
// MSS page.
// ToDo: BAR Code verification after clarifying on the expected Bar
// code.
			orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText
					.getText();
			Utilities
					.ClickElement(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
// Payment Verification page.
			PageBase.PaymentVerificationPage().paymentVerification(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId));
//Device Activation page
			PageBase.DeviceVerificationaandActivation()
					.deviceVerificationActiavtionFor1Device(imeiQA_67);
// WCA Signature page.
			Utilities
					.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
			PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
			PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();
// Order Activation Complete page.
			Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete
					.isDisplayed());
		}
		return orderId;
	}

    private void selectingVerizonExternalEnvironment() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
        Reporter.log("<h3><U> External Server</U></h3>", true);
    }

    //region QA-71 Refactored Methods
    private void poaFlowQA71() {
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
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().verizonTab);
        PageBase.UECVerificationPage().verizonTab.click();
        PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys(Constants.DEFAULT_XML_NUMBER_8155491829);
        PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys("7777");
        PageBase.UECVerificationPage().accountPasswordVerizonTextbox.sendKeys("");
        PageBase.UECVerificationPage().accountZipcodeVerizonTextbox.sendKeys("60007");
        PageBase.UECVerificationPage().continueVerizonButton.click();

        //Select an Option Page
        Utilities.waitForElementVisible(PageBase.SelectAnOptionPage().switchFromAnIndividualToFamilyPlanAndAddALineRadioButton);
        PageBase.SelectAnOptionPage().switchFromAnIndividualToFamilyPlanAndAddALineRadioButton.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //Processing Error Page
        Utilities.waitForElementVisible(PageBase.ProcessingErrorPage().processingErrorText);
        Assert.assertTrue(PageBase.ProcessingErrorPage().processingErrorText.isDisplayed());
        Assert.assertTrue(PageBase.ProcessingErrorPage().processingErrorMessageText.isDisplayed());
        Assert.assertTrue(PageBase.ProcessingErrorPage().aTTHelpNumberText.isDisplayed());
        Assert.assertTrue(PageBase.ProcessingErrorPage().verizonHelpNumberText.isDisplayed());
        Assert.assertTrue(PageBase.ProcessingErrorPage().sprintHelpNumberText.isDisplayed());
    }

    private void carrierResponderSettingsQA71() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        Robot robot = new Robot();
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Verizon");
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCustomerDetails", "IN.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }
    //endregion QA-71 Refactored Methods

    //region QA-5437 Refactored Methods
    private void shipAdminVerificationsQA5437(@Optional String testtype, String orderId) {
        if (testtype.equals("internal")) {
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            Utilities.switchPreviousTab();
            driver.navigate().back();
            Utilities.waitForElementVisible(PageBase.OrderSummaryPage().statusValueLink);
        } else {
            ShipAdminBaseClass.launchShipAdminInNewTab();
            PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        }
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
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains("Phone and Plan"));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
    }

    private String poaFlowQA5437(@Optional String testtype, String iMEINumber, String simNumber, String orderId) throws IOException, AWTException, InterruptedException {
        //Login
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"), Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId2766"));

        //Home Page
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        //Choose a Path Page
        PageBase.ChoosePathPage().existingCarrier.click();

        //Pick your Path Page
        PageBase.PickYourPathPage().AALExistingAccount.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //UEC Verification Page
        Utilities.waitForElementVisible(PageBase.UECVerificationPage().verizonTab);
        PageBase.UECVerificationPage().verizonTab.click();
        PageBase.UECVerificationPage().phoneNumberVerizonTextbox.sendKeys("6567778895");
        PageBase.UECVerificationPage().last4OfSSNVerizonTextbox.sendKeys("7777");
        PageBase.UECVerificationPage().accountPasswordVerizonTextbox.sendKeys("");
        PageBase.UECVerificationPage().accountZipcodeVerizonTextbox.sendKeys("60007");
        PageBase.UECVerificationPage().continueVerizonButton.click();

        //Select an Option Page
        Utilities.waitForElementVisible(PageBase.SelectAnOptionPage().AALExistingFamilyPlan);
        PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().iMEIESNTextbox);
        PageBase.DeviceScanPage().iMEIESNTextbox.sendKeys(iMEINumber);
        PageBase.DeviceScanPage().submitDeviceButton.click();

        //Verizon Edge Page
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().noContinueWith2YearButton);
        PageBase.VerizonEdgePage().noContinueWith2YearButton.click();

        //Device Scan Page
        Utilities.waitForElementVisible(PageBase.DeviceScanPage().continueDSButton);
        PageBase.DeviceScanPage().continueDSButton.click();

        //Carrier Credit Check Page
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        PageBase.CarrierCreditCheckPage().skip.click();

        //Cart Page
        Utilities.waitForElementVisible(PageBase.CartPage().continueCartButton);
        String phonePrice = PageBase.CartPage().phonePriceAALText.getText();
        String phoneModel = PageBase.CartPage().phoneModelAALLink.getText();
        PageBase.CartPage().continueCartButton.click();

        //Verizon Select Plan Features Page
        Utilities.waitForElementVisible(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);
        PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton.click();

        //Select Protection Plan Insurance Page
        Utilities.waitForElementVisible(PageBase.SelectProtectionPlanInsurancePage().eSecuritelInsurance);
        PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
        PageBase.SelectProtectionPlanInsurancePage().NoInsurance.click();
        Utilities.implicitWaitSleep(6000);
        PageBase.SelectProtectionPlanInsurancePage().guestReview.click();
        PageBase.CommonControls().continueButton.click();

        //Number Port Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        Utilities.waitForElementVisible(PageBase.ServiceProviderVerificationPage().firstNameTextbox);
        //PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, "", customerDetails.LastName, customerDetails.EMail, IdType.DRIVERLICENCE, customerDetails.State, customerDetails.DLNumber, Month.FEBRUARY, customerDetails.DLExpirationYear);
        PageBase.ServiceProviderVerificationPage().populatingSPVPageWithCreditCardDetails("Faizal", "", "Khan", "TECH", "PARK", "San Francisco", "CA", "94109", "9999999999", "JanUary", "20", "1990", "faizal.khan@test.com", "651247895", IdType.DRIVERLICENCE, "CA", "123456789", "JUNE", "2020");
        PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck.click();
        PageBase.ServiceProviderVerificationPage().continueSPVButton.click();
        PageBase.ServiceProviderVerificationPage().continueSPVButton.click();

        //Order Review and Confirm Page
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        //Assert.assertEquals(driver.findElement(By.xpath("(((//div[@class='orConfirmRow']/child::div)[5]/child::li/child::div)[1]/child::div)[2]")).getText(), phonePrice); //Phone Price Not Coming Same
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().existingPlanDiv.isDisplayed());
        String existingPlan1 = CommonFunction.getPlan(PageBase.OrderReviewAndConfirmPage().existingPlanDiv, 17);
        Assert.assertTrue(PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.isDisplayed());
        String totalDue = PageBase.OrderReviewAndConfirmPage().totalDueTodayValue.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Terms & Conditions Page
        Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox);
        PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
        if (readConfig("Activation").toLowerCase().contains("true")) {
            PageBase.TermsandConditionsPage().continueTCButton.click();

            //Print Mobile Scan Sheet Page
            try {
                Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton, 70);
            } catch (Exception e)  //ToDo:Remove this when no insurance bug will fix.
            {
                Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().sameAddressTab, 10);
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA);
                PageBase.PaymentRequiredPage().sameAddressTab.click();
                PageBase.PaymentRequiredPage().continuePRButton.click();
                Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            }
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            //Assert.assertEquals(PageBase.PrintMobileScanSheetPage().phonePriceValuePMSSText.getText(), phonePrice); //Phone Price Not Same w.r.t. Cart Page
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            //Payment Verification Page
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(Utilities.receiptCalculator());
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation Page
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceIMEITextbox.sendKeys(iMEINumber);
            PageBase.DeviceVerificationaandActivation().submitDVAButton.click();
            PageBase.DeviceVerificationaandActivation().simType.clear();
            PageBase.DeviceVerificationaandActivation().simType.sendKeys(simNumber);
            try  //ToDo:Remove this when no insurance bug will fix.
            {
                PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.clear();
                PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys("123");
                //PageBase.DeviceVerificationaandActivation().cvnNumberDVATextbox.sendKeys(String.valueOf(creditCardDetails.CVV));
            } catch (Exception e) {
            }
            PageBase.DeviceVerificationaandActivation().continueButtonDVA.click();

            //Wireless Customer Agreement Page
            Utilities.waitForElementVisible(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox, 120);

            if (testtype.equals("internal")) {
                //Getting new number assigned from shipadmin
                ShipAdminBaseClass.launchShipAdminInNewTab();
                PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
                Utilities.dropdownSelect(PageBase.OrderSummaryPage().printableFormsDropdown, SelectDropMethod.SELECTBYINDEX, "1");
                Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().phoneNumberValueText);
                String newPhoneNumberAssigned = PageBase.OrderActivationCompletePage().phoneNumberValueText.getText();
                newPhoneNumberAssigned = CommonFunction.getUnFormattedPhoneNumber(newPhoneNumberAssigned);

                //Customizing xml files in Carrier Responder
                Utilities.switchPreviousTab();
                Utilities.switchPreviousTab();
                PageBase.CarrierResponseXMLPage().selectOptions("current", "submitactivation", "success_1_line.xml");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
                Utilities.implicitWaitSleep(5000);
                String xmlContent2 = PageBase.CarrierResponseXMLPage().xmlTextArea.getAttribute("value");
                xmlContent2 = xmlContent2.replace(Constants.DEFAULT_XML_NUMBER_4152647954, newPhoneNumberAssigned);
                PageBase.CarrierResponseXMLPage().xmlTextArea.clear();
                Robot robot = new Robot();
                Utilities.copyPaste(xmlContent2, robot);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();

                //Switching To Retail
                Utilities.switchPreviousTab();
                Utilities.switchPreviousTab();
                Utilities.switchPreviousTab();
            }

            ////Wireless Customer Agreement Page
            PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

            //Order Activation and Complete Page
            Utilities.waitForElementVisible(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText, 120);
            Assert.assertTrue(PageBase.OrderActivationCompletePage().orderAndActivationCompleteText.isDisplayed());
            Assert.assertEquals(PageBase.OrderActivationCompletePage().iMEINumberValueText.getText(), iMEINumber);
            Assert.assertEquals(PageBase.OrderActivationCompletePage().simNumberValueText.getText(), simNumber);
            //Assert.assertEquals(PageBase.OrderActivationCompletePage().priceValueText.getText(), phonePrice);
        }
        return orderId;
    }

    private void carrierResponderSettingsQA5437() throws InterruptedException, AWTException, IOException {
        AdminBaseClass adminBaseClass = new AdminBaseClass();
        adminBaseClass.launchAdminInNewTab();
        PageBase.AdminPage().navigateToSimulator();
        Robot robot = new Robot();
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");
        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Verizon");
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCustomerDetails", "vwz_edge_up_eligible_not_payment_needed.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveCreditApplication", "default_upgrade.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        PageBase.CarrierResponseXMLPage().selectOptions("current", "retrieveExistingCustomerInstallmentDetails", "eligible_no_payment_needed.xml");
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
        Utilities.implicitWaitSleep(4000);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
    }
    //endregion QA-5437 Refactored Methods

    //region QA 75 Refactored Methods
    private String poaCompleteFlow_75(String testType) throws IOException {
        String orderId = "";//Login to retail page.
        CreditCardDetails creditCard = new CreditCardDetails();
        creditCard = PageBase.CSVOperations().CreditCardDetails(CardType.VISA);
        lStartTime = new Date().getTime();
        pageName = readPageName("PoaLogin");
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on Sales & Activations page.
        lStartTime = new Date().getTime();
        pageName = readPageName("SaleAndActivation");
        PageBase.HomePageRetail().salesAndActivationsLink.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Click on New Activation link.
        lStartTime = new Date().getTime();
        pageName = readPageName("DeviceScan");
        PageBase.ChoosePathPage().newActivation.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Scanning smart phone.
        PageBase.DeviceScanPage().enterDeviceScanDetails(imeiDetails.IMEI);
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);

        //Verizon Edge Page
        Utilities.waitForElementVisible(PageBase.VerizonEdgePage().YesCheckEligibilityButton);
        PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);

        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();

        lStartTime = new Date().getTime();
        pageName = readPageName("CarrierCreditCheck");
        PageBase.CommonControls().continueButton.click();
        Utilities.implicitWaitSleep(1000);
        try {
            if (PageBase.CommonControls().continueButton.isEnabled())
                PageBase.CommonControls().continueButton.click();
        } catch (Exception e) {

        }
        //if(testType.equals("internal")) PageBase.CommonControls().continueButton.click();
        Utilities.webPageLoadTime(lStartTime, pageName);

        // Credit Check Verification Results with deposits.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        boolean exists = driver.findElements(By.id("checkbox-deposit-1")).size() != 0;
        if (exists) {
            Reporter.log("<br> Credit Check Completes.");
            PageBase.CreditCheckVerificationResultsPage().depositCheckBox.click();
            Reporter.log("<br> Selected Deposit Check Box ");
        } else {
            PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
        }
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().verizonMorePlanOnly);
        PageBase.VerizonShopPlansPage().selectPlanWithMore();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        //Select Plan - Storing the Device and plan prices for further verification.
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().selectFamilyBasePlan(0);
        PageBase.SelectPlanFeaturesPage().selectNetworkAccessPlan(1);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting No Insurance .
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
        PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
                cartDevice1price);

        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().continueTCButton);
            PageBase.TermsandConditionsPage().emailTCChkBox.click();
            PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.TermsandConditionsPage().continueTCButton.click();

            // Credit Check Verification Results with deposits.
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
            boolean exist = driver.findElements(By.id("checkbox-mini-1")).size() != 0;
            if (exist) {
                PageBase.CreditCheckVerificationResultsPage().creditCheckPassChkBox.click();
                PageBase.CommonControls().continueButtonDeposit.click();
            }

            // Credit card payment  page is coming.
            boolean visaexists = driver.findElements(By.id("radio-1a")).size() != 0;
            if (visaexists) {
                Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().visaTab);
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(CardType.VISA);
                PageBase.PaymentRequiredPage().continuePRButton.click();
            }

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            //PageBase.PrintMobileScanSheetPage().verifyAllTwoDeviceBarCode();

            //TODO:Need to add assertion for store location.
            //		WebElement web = driver.findElement(By.xpath("//span[contains(text(),'2766 - TARGET - SAN FRANCISCO CENTRAL')]"));
            //		String storeLocation = web.getText();
            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(FileName.ReceiptId));
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imeiDetails.IMEI,imeiDetails.Sim);

            // WCA Signature page activity and verifications.
            Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);

            List<WebElement> wcaLists = driver.findElements(By.xpath("//div[@class ='termsandcondition'][1]/label"));
            Assert.assertEquals("Application ID No.:", wcaLists.get(0).getText());
            Assert.assertEquals("Order Date:", wcaLists.get(1).getText());
            Assert.assertEquals("Bill Acct. No.:", wcaLists.get(2).getText());
            Assert.assertEquals("Agent Name:", wcaLists.get(3).getText());
            Assert.assertEquals("Activation Type:", wcaLists.get(4).getText());

            PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

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

    private void shipAdminVerification_75(String orderId) {
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

    private void inventoryManagementVerification_75() throws InterruptedException, AWTException, IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.InventoryManagementPage().verifyDeviceStatus(imei1, IMEIStatus.Sold.toString());
    }

    private void selectingCarrierEnviornment_75(String testType) throws InterruptedException, AWTException, IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            selectingBackendSimulatorForQA75();

            //Selecting Carrier Responder

        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Verizon", "External");
        }
    }

    private void selectingBackendSimulatorForQA75() {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "BackendSimulator");

        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Verizon");

        PageBase.AdminPage().selectCreaditReadUseCase("APPROVE_WITH_DEPOSIT");
        PageBase.AdminPage().selectCreaditWriteUseCase("APPROVE_WITH_DEPOSIT");
        PageBase.AdminPage().retrieveCustomerDetails("ELIGIBLE");

        List<WebElement> phoneList = PageBase.AdminPage().phoneList.findElements(By.className("phoneNumberRow"));

        if (phoneList.size() > 0) {
            List<WebElement> removePhone = PageBase.AdminPage().phoneList.findElements(By.id("retrieveCustomerDetails_removePhoneNumberRowButton"));
            for (int i = 0; i < removePhone.size(); i++) {
                removePhone.get(i).click();
            }
        }

        PageBase.AdminPage().accountPlanType("Family Share");
        PageBase.AdminPage().retrieveExistingCustomerInstallmentsDetails("SUCCESS_WITH_PAYMENT");
        PageBase.AdminPage().retrievePricePlan("SUCCESS");
        PageBase.AdminPage().submitActivation("SUCCESS");
        PageBase.AdminPage().submitReciept("SUCCESS");
        PageBase.AdminPage().submitServiceDetails("SUCCESS");
        PageBase.AdminPage().submitEdgeUpPayment("SUCCESS");
        PageBase.AdminPage().returnOrExchangeDevice("SUCCESS");

        PageBase.AdminPage().save();
    }
    //endregion QA 75 Refactored Methods
    //endregion Private Methods and Refacotored Codes
}
