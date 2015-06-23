package com.consensus.test.sprint.tests;

import com.consensus.qa.framework.*;
import com.consensus.qa.pages.PaymentRequiredPage;
import com.consensus.qa.pages.ServiceProviderVerificationPage;
import com.consensus.qa.tests.CarrierCreditCheckDetails;
import com.gargoylesoftware.htmlunit.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class AddNewLineTests extends RetailBaseClass {

   //region Variable Declaration
   public String iMEINumber1 = ""; //ToDo: Need to read from data sheet.
   public String imei1 = "";
   public String imei2 = ""; //ToDo: Need to read from data sheet.
   public String carrierType = "Sprint";
   public String cartDevice1price = "";
   public String cartDevice2price = "";
   public String cartDevice3price = "";
   public String cartPlanprice = "";
   public String simType1 = "";
   String receiptId = "";
   //endregion

   //region Test Methods
   //region QA 1705
   @Test(groups = {"sprint"})
   @Parameters("test-type")
   public void QA_1705_Sprint2YrContractWithNewActivation(@Optional String testType) {
      String orderId = "";
      try {
         Log.startTestCase("QA_1705_Sprint2YrContractWithNewActivation");
         testType = BrowserSettings.readConfig("test-type");

         // Verify whether which enviorement to use internal or external.
         selectingCarrierEnviornment(testType);

         // Switching to previous tab.
         Utilities.switchPreviousTab();

         //Calling DBError utility to  find initial count or error in log files.
         //DBError.navigateDBErrorPage();
         //int initialCount = PageBase.AdminPage().totalErrorCount();

         // Switching to previous tab.
         //Utilities.switchPreviousTab();

         orderId = poaCompleteFlow(testType);

         //Inventory Management Page verification.
         if (readConfig("Activation").contains("true")) {
            inventoryManagementVerification();

            //Ship Admin Verification -orderId= ""
            shipAdminVerification(orderId);
         }

         //DBError Verification.
         //DBError.navigateDBErrorPage();
         //Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

         Reporter.log("<h3>QA_1705_Sprint2YrContractWithNewActivation - Test Case Completes<h3>");
         Log.endTestCase("QA_1705_Sprint2YrContractWithNewActivation");
      } catch (Exception ex) {
         Log.error(ex.getMessage());
         Utilities.driverTakesScreenshot("QA_1705_Sprint2YrContractWithNewActivation");
         Assert.fail();
      }
   }
   //endregion QA 1705

   //region QA 84
   @Test(groups = {"sprint"})
   @Parameters("test-type")
   public void QA_84_Sprint2YrContractWithAppleCareAndApprovedNoDeposit(@Optional String testType) {
      String orderId = "";
      try {
         Log.startTestCase("QA_84_Sprint2YrContractWithAppleCareAndApprovedNoDeposit");
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

         orderId = poaCompleteFlow_84(testType);

         //Inventory Management Page verification.
         if (readConfig("Activation").contains("true")) {
            inventoryManagementVerification();

            //Ship Admin Verification -orderId= ""
            shipAdminVerification_84(orderId);
         }

         //DBError Verification.
         DBError.navigateDBErrorPage();
         Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

         Reporter.log("<h3>QA_84_Sprint2YrContractWithAppleCareAndApprovedNoDeposit - Test Case Completes<h3>");
         Log.endTestCase("QA_84_Sprint2YrContractWithAppleCareAndApprovedNoDeposit");
      } catch (Exception ex) {
         Log.error(ex.getMessage());
         Utilities.driverTakesScreenshot("QA_84_Sprint2YrContractWithAppleCareAndApprovedNoDeposit");
         Assert.fail();
      }
   }

   //endregion QA 84

   //region QA_2966_SprintNewActivationWithEasyPay
   @Test(groups = { "verizon" })
   @Parameters("test-type")
   public void QA_2966_SprintNewActivationWithEasyPay(@Optional String testType) throws IOException {
      String orderId = "";
      boolean activation = false;
      String sTestCaseName = "QA_2966_SprintNewActivationWithEasyPay";
      CreditCardDetails creditCard = new CreditCardDetails();
      creditCard = PageBase.CSVOperations().CreditCardDetails(PaymentRequiredPage.CardType.VISA);
      String phoneNumberLine1 = "";

     iMEINumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.SamsungGalaxyS4_16GBWhite);
      simType1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sim_3FF);
      receiptId = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId);
      //carrierType = "Sprint";
      try {
         // Printing the start of Test Case
         Log.startTestCase(sTestCaseName);
         // Fetching the Execution Environment
         testType = BrowserSettings.readConfig("test-type");

         selectCarrierResponderQA2966(testType);

         // Switching to previous tab.
         Utilities.switchPreviousTab();

         orderId = poaCompleteFlowQA2966(orderId);

         //Inventory Management and Ship admin Page verification.
         if(readConfig("Activation")=="true") {
            // Inventory Management Page verification.
            PageBase.InventoryManagementPage().launchInventoryInNewTab();
            PageBase.InventoryManagementPage().verifyDeviceStatus(iMEINumber1, InventoryManagementBaseClass.IMEIStatus.Sold.toString());

            //Ship Admin Verification -orderId= "";
            QA_2966_shipAdminVerification(orderId);
         }
         //DBError Verification.
         DBError.navigateDBErrorPage();
         //Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

         Reporter.log("<h3>QA_2966_SprintNewActivationWithEasyPay - Test Case Completes<h3>");
         Log.endTestCase(sTestCaseName);
      } catch (Exception ex) {
         Log.error(ex.getMessage());
         System.out.println(ex.getMessage());
      }
   }

   //endregion QA_2699_SprintNewActivationWithEasyPay

   //region QA-4248
   @Test(groups = {"sprint"})
   @Parameters("test-type")
   public void QA_4248_SprintEasyPay_CancelOrder_WithOutAccepting_Contract(@Optional String testType) {
      String orderId = "";
      try {
         Log.startTestCase("QA_4248_SprintEasyPay_CancelOrder_WithOutAccepting_Contract");
         testType = BrowserSettings.readConfig("test-type");

         // Verify whether which enviorement to use internal or external.
         selectingCarrierEnviornment_4248(testType);

         // Switching to previous tab.
         Utilities.switchPreviousTab();

         //Calling DBError utility to  find initial count or error in log files.
         DBError.navigateDBErrorPage();
         int initialCount = PageBase.AdminPage().totalErrorCount();

         // Switching to previous tab.
         Utilities.switchPreviousTab();

         orderId = poaCompleteFlow_4248(testType);

         //Inventory Management Page verification.
         if (readConfig("Activation").contains("true")) {
            inventoryManagementVerification_4284();

            //Ship Admin Verification -orderId= ""
            shipAdminVerification_4248(orderId);
         }

         //DBError Verification.
         DBError.navigateDBErrorPage();
         Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

         Reporter.log("<h3>QA_4248_SprintEasyPay_CancelOrder_WithOutAccepting_Contract - Test Case Completes<h3>");
         Log.endTestCase("QA_4248_SprintEasyPay_CancelOrder_WithOutAccepting_Contract");
      } catch (Exception ex) {
         Log.error(ex.getMessage());
         Utilities.driverTakesScreenshot("QA_4248_SprintEasyPay_CancelOrder_WithOutAccepting_Contract");
         Assert.fail();
      }
   }

   //endregion QA-4248

   //region QA 4242
   @Test(groups = {"sprint"})
   @Parameters("test-type")
   public void QA_4242_NewActivationWithEasyPay(@Optional String testType) {
      String orderId = "";
      try {
         Log.startTestCase("QA_4242_NewActivationWithEasyPay");
         testType = BrowserSettings.readConfig("test-type");

         // Verify whether which enviorement to use internal or external.
         selectingCarrierEnviornment_4248(testType);

         // Switching to previous tab.
         Utilities.switchPreviousTab();

          //Calling DBError utility to  find initial count or error in log files.
         DBError.navigateDBErrorPage();
          int initialCount = PageBase.AdminPage().totalErrorCount();

         // Switching to previous tab.
         Utilities.switchPreviousTab();

         orderId = poaCompleteFlow_4242(testType);

          //Inventory Management Page verification.
         if (readConfig("Activation").contains("true")) {
            inventoryManagementVerification_4284();

             //Ship Admin Verification -orderId= ""
            shipAdminVerification_4242(orderId);
         }

         //DBError Verification.
         DBError.navigateDBErrorPage();
         Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

          Reporter.log("<h3>QA_4242_NewActivationWithEasyPay - Test Case Completes<h3>");
         Log.endTestCase("QA_4242_NewActivationWithEasyPay");
      } catch (Exception ex) {
         Log.error(ex.getMessage());
         Utilities.driverTakesScreenshot("QA_4242_NewActivationWithEasyPay");
          Assert.fail();
      }
   }
   //endregion QA 4242
   
    //region QA 93
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_93_Sprint2YrAALExistingFamilyPlan(@Optional String testType) {
        String orderId = "";
        try {
            Log.startTestCase("QA_93_Sprint2YrAALExistingFamilyPlan");
            testType = BrowserSettings.readConfig("test-type");
            selectingCarrierEnviornment(testType);
            Utilities.switchPreviousTab();
            orderId = QA_93_poaCompleteFlow(testType);

            if (readConfig("Activation").contains("true")) {
                inventoryManagementVerification();

                //Ship Admin Verification -orderId= ""
                shipAdminVerifications(orderId);
            }
            Reporter.log("<h3>QA_93_Sprint2YrAALExistingFamilyPlan - Test Case Completes<h3>");
            Log.endTestCase("QA_93_Sprint2YrAALExistingFamilyPlan");
        } catch (Exception ex) {
            //Log.error(ex.getMessage());
            //Utilities.driverTakesScreenshot("QA_93_Sprint2YrAALExistingFamilyPlan");
            System.out.println(ex);
            //Assert.fail();
        }
    }
    //endregion QA 93

    //region QA-4244
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment(@Optional String testType) {
        String orderId = "";
        try {
            Log.startTestCase("QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment");
            testType = BrowserSettings.readConfig("test-type");

            // Verify whether which enviorement to use internal or external.
            selectingCarrierEnviornment_4248(testType);

            // Switching to previous tab.
            Utilities.switchPreviousTab();

            //Calling DBError utility to  find initial count or error in log files.
            DBError.navigateDBErrorPage();
            int initialCount = PageBase.AdminPage().totalErrorCount();

            // Switching to previous tab.
            Utilities.switchPreviousTab();

            orderId = poaCompleteFlow_4244(testType);

            //Inventory Management Page verification.
            if (readConfig("Activation").contains("true")) {
                inventoryManagementVerification_4284();

                //Ship Admin Verification -orderId= ""
                shipAdminVerification_4242(orderId);
            }

            //DBError Verification.
            DBError.navigateDBErrorPage();
            Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

            Reporter.log("<h3>QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment - Test Case Completes<h3>");
            Log.endTestCase("QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment");
        } catch (Exception ex) {
            Log.error(ex.getMessage());
            Utilities.driverTakesScreenshot("QA_4244_WithEasyPay_AppleCareAndAlternateDownPayment");
            Assert.fail();
        }
    }

    //endregion QA-4244

    //region QA 3928
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_3928_SprintNonEasyPayWithNewActivation_FamilySharePackPlan(@Optional String testType) {
        String orderId = "";
        try {
            Log.startTestCase("QA_3928_SprintNonEasyPayWithNewActivation_FamilySharePackPlan");
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

            orderId = poaCompleteFlow_3928(testType);

            //Inventory Management Page verification.
            if (readConfig("Activation").contains("true")) {
                // Inventory Management Page verification.
                PageBase.InventoryManagementPage().launchInventoryInNewTab();
                Utilities.implicitWaitSleep(4000);
                PageBase.InventoryManagementPage().verifyDeviceStatus(imei1, InventoryManagementBaseClass.IMEIStatus.Sold.toString());

                //Ship Admin Verification -orderId= ""
                shipAdminVerification(orderId);
            }

            //DBError Verification.
           DBError.navigateDBErrorPage();
           Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

            Reporter.log("<h3>QA_3928_SprintNonEasyPayWithNewActivation_FamilySharePackPlan - Test Case Completes<h3>");
            Log.endTestCase("QA_3928_SprintNonEasyPayWithNewActivation_FamilySharePackPlan");
        } catch (Exception ex) {
            Log.error(ex.getMessage());
            Utilities.driverTakesScreenshot("QA_3928_SprintNonEasyPayWithNewActivation_FamilySharePackPlan");
            Assert.fail();
        }
    }

    //endregion QA 3928

    //region QA-4162
    @Test(groups = {"sprint"})
    @Parameters("test-type")
    public void QA_4162_WithEasyPay_WithDepositandNumberPort(@Optional String testType) {
        String orderId = "";
        try {
            Log.startTestCase("QA_4162_WithEasyPay_WithDepositandNumberPort");
            testType = BrowserSettings.readConfig("test-type");

            // Verify whether which enviorement to use internal or external.
            selectingCarrierEnviornment_4248(testType);

            // Switching to previous tab.
            Utilities.switchPreviousTab();

            //Calling DBError utility to  find initial count or error in log files.
            DBError.navigateDBErrorPage();
            int initialCount = PageBase.AdminPage().totalErrorCount();

            // Switching to previous tab.
            Utilities.switchPreviousTab();

            orderId = poaCompleteFlow_4244(testType);

            //Inventory Management Page verification.
            if (readConfig("Activation").contains("true")) {
                inventoryManagementVerification_4284();

                //Ship Admin Verification -orderId= ""
                shipAdminVerification_4242(orderId);
            }

            //DBError Verification.
            DBError.navigateDBErrorPage();
            Assert.assertTrue(PageBase.AdminPage().isDBErrorFound(initialCount));

            Reporter.log("<h3>QA_4162_WithEasyPay_WithDepositandNumberPort - Test Case Completes<h3>");
            Log.endTestCase("QA_4162_WithEasyPay_WithDepositandNumberPort");
        } catch (Exception ex) {
            Log.error(ex.getMessage());
            Utilities.driverTakesScreenshot("QA_4162_WithEasyPay_WithDepositandNumberPort");
            Assert.fail();
        }
    }

    //endregion QA-4162

   //endregion Test Methods

   //region private methods
   private void selectingCarrierEnviornment(@Optional String testType) throws InterruptedException, AWTException, java.io.IOException {
       if (testType.equals("internal")) {
           // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
           AdminBaseClass adminBaseClass = new AdminBaseClass();
           adminBaseClass.launchAdminInNewTab();

           PageBase.AdminPage().navigateToSimulator();

           //Selecting Backed Simulator.
           selectingBackendSimulatorForQA_84();

           //Selecting Carrier Responder
           //selectCarrierResponderQA_84();
       } else   //External
       {
           // Need to set External server from Admin page.
           AdminBaseClass adminBaseClass = new AdminBaseClass();
           adminBaseClass.launchAdminInNewTab();

           PageBase.AdminPage().navigateToSimulator();
           PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
       }
   }

    private void selectingBackendSimulatorForQA_84() {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");

        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        //PageBase.AdminPage().checkLoaanEligibility("LOAN_ELIGIBLE");
        //PageBase.AdminPage().selectCreaditWriteUseCase("APPROVE_WITH_DEPOSIT");
        //PageBase.AdminPage().retrieveCustomerDetails("ELIGIBLE");

        PageBase.AdminPage().save();
    }

    private void selectCarrierResponderQA_84() {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

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

    private String poaCompleteFlow(@Optional String testType) throws IOException {
        String orderId = "";//Login to retail page.
        String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_3FF);
        String simNumber2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_3FF);
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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

        // Scanning smart phone,basic phone and MIFI phone.
        imei1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        imei2 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);
        PageBase.VerizonEdgePage().declineSprintEasyPay();
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei2);
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
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
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        cartDevice2price = PageBase.CartPage().device2Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting No Insurance .
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsuranceForTwoDevices();

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0],spvCollections[1],spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
                cartDevice1price);


        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().emailTCChkBox);
            PageBase.TermsandConditionsPage().emailTCChkBox.click();
            PageBase.TermsandConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsandConditionsPage().continueTCButton.click();

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            PageBase.PrintMobileScanSheetPage().verifyAllTwoDeviceBarCode();

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
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor3Devices(imei1, imei2, "",simNumber1,simNumber2,"");

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

    private void inventoryManagementVerification() throws InterruptedException, AWTException, java.io.IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.InventoryManagementPage().verifyDeviceStatus(imei1, imei2, InventoryManagementBaseClass.IMEIStatus.Sold.toString());
    }

    private void shipAdminVerification(String orderId) {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        // Assert.assertTrue(eventLogTableContent.contains(Constants.RECEIPT_SUBMISSION_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        //Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_SUCCEEDED_COMMENT));
        //Assert.assertTrue(eventLogTableContent.contains(Constants.PARKING_SUCCEEDED_COMMENT));
        //Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
        //Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
    }

    private void shipAdminVerification_84(String orderId) {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        // Assert.assertTrue(eventLogTableContent.contains(Constants.RECEIPT_SUBMISSION_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_COMPLETED_SUCCESSFULLY));
        Assert.assertTrue(eventLogTableContent.contains(Constants.NO_DEPOSIT_REQUIRED));
        //Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
        //Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
    }

    private void selectingBackendSimulatorForQA1705() {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");

        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        //PageBase.AdminPage().selectCreaditReadUseCase("APPROVE_WITH_DEPOSIT");
        //PageBase.AdminPage().selectCreaditWriteUseCase("APPROVE_WITH_DEPOSIT");
        //PageBase.AdminPage().retrieveCustomerDetails("ELIGIBLE");

        PageBase.AdminPage().save();
    }

    private void selectCarrierResponderQA1705() {
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

    private CarrierCreditCheckDetails getCarrierCreditCheckDetails(String ssn) throws IOException {
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
        cccDetails.setSSN(ssn);
        cccDetails.setIDType(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        cccDetails.setIdTypeState(customerDetails.IDState);
        cccDetails.setIdNumber(customerDetails.IDNumber);
        cccDetails.setMonth(customerDetails.IDExpirationMonth);
        cccDetails.setYear(customerDetails.IDExpirationYear);
        return cccDetails;
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

    //region QA-2966 Refactored Methods
    private void selectCarrierResponderQA2966(@Optional String testtype) throws IOException, InterruptedException, AWTException
    {
        // Verify whether which environment to use internal or external.
        testtype = BrowserSettings.readConfig("test-type");
        carrierType = "Sprint";
        if(testtype.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, BrowserSettings.readConfig("internalTestType"));

            if (BrowserSettings.readConfig("internalTestType").contains("CarrierResponder")) {

                //Selecting Use Case from dropdown list.
                PageBase.AdminPage().selectAPIConfig(carrierType);
                //Customizing xml files in Carrier Responder
                PageBase.CarrierResponseXMLPage().sprintCarrierTab.click();
                PageBase.CarrierResponseXMLPage().selectOptions("current", "checkloaneligibility", "loannoteligible.xml");
                Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().xmlTextArea);
                Utilities.implicitWaitSleep(4000);
                PageBase.CarrierResponseXMLPage().saveResponseButton.click();
            }
        }
        else{   //External// Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse(carrierType, "External");
        }
    }

    private String poaCompleteFlowQA2966 (String orderId)  throws IOException
    {
        CustomerDetails customerDetails = PageBase.CSVOperations().ReadCustomerDetailsFromCSV(ServiceProviderVerificationPage.IdType.DRIVERLICENCE);
        NumPortingDetails portDetails = PageBase.CSVOperations().ReadPortingDetails();

        //Login to retail page.
        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),
                Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        // Click on Sales & Activations page.
        Utilities.ClickElement(PageBase.HomePageRetail().salesAndActivationsLink);

        // Click on New Activation link.
        PageBase.ChoosePathPage().newActivation.click();

        // Scanning 3 Verizon smart phones.
        PageBase.DeviceScanPage().enterDeviceScanDetails(iMEINumber1);
        PageBase.VerizonEdgePage().YesCheckEligibilityButton.click();
        //Utilities.ClickElement(PageBase.CommonControls().continueButtonDVA);

        ///Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().populateForm);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails();
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);
        PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox.click();
        PageBase.CommonControls().continueButton.click();

        // Installment Page
        Utilities.waitForElementVisible(PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton);
        PageBase.InstallmentPage().edgeMonthlyInstallmentRadiobutton.click();
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.ClickElement(PageBase.SprintShopPlansPage().sprintFamilySharePack1GBAddButton);

        // Storing the Device and plan prices for further verification.
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        //cartDevice2price = PageBase.CartPage().device2Price.getText();
        //cartDevice3price = PageBase.CartPage().device3Price.getText();
        //cartPlanprice = PageBase.CartPage().planPriceActual.getText();

        Utilities.ClickElement(PageBase.CartPage().continueCartButton);

        // Selecting plan feature.
        Utilities.ClickElement(PageBase.VerizonSelectPlanFeaturesPage().continueSPFButton);

        // Selecting NO Insurance.
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();

        // Selecting Number Porting.
        Utilities.ClickElement(PageBase.NumberPortPage().noNumberPortRadiobutton);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);

        // Enter data in Service Provider Verification page.
        PageBase.ServiceProviderVerificationPage().populatingSPVPage(customerDetails.FirstName, "", customerDetails.LastName,
                customerDetails.EMail, ServiceProviderVerificationPage.IdType.DRIVERLICENCE, customerDetails.State, customerDetails.IDNumber,
                ServiceProviderVerificationPage.Month.valueOf(customerDetails.IDExpirationMonth.toUpperCase()), Integer.parseInt(customerDetails.IDExpirationYear),
                portDetails.CurrentPhoneNumber, customerDetails.BirthdayMonth, Integer.parseInt(customerDetails.BirthdayDay),
                Integer.parseInt(customerDetails.BirthdayYear));
        Utilities.ClickElement(PageBase.ServiceProviderVerificationPage().guestAgreesCreditCheck);
        Utilities.ClickElement(PageBase.CommonControls().continueButton);
        //Utilities.ClickElement(PageBase.CommonControls().continueButton);

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
            PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
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
        PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(iMEINumber1, simType1);

        // WCA Signature page.
        Utilities.ClickElement(PageBase.WirelessCustomerAgreementPage().acceptsWCACheckbox);
        PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
        PageBase.WirelessCustomerAgreementPage().continueWCAButton.click();

        // Order Activation Complete page.
        Assert.assertTrue(PageBase.OrderActivationCompletePage().ActivationComplete.isDisplayed());
        PageBase.CSVOperations();
        //orderId = PageBase.OrderActivationCompletePage().orderNumberValueText.getText();
        //Assert.assertTrue(PageBase.OrderActivationCompletePage().phoneNumberValueText.isDisplayed());
      /*Assert.assertTrue(PageBase.OrderActivationCompletePage().device2PhoneNo.isDisplayed());
         Assert.assertTrue(PageBase.OrderActivationCompletePage().device3PhoneNo.isDisplayed());*/
        //Assert.assertTrue(PageBase.OrderActivationCompletePage().iMEINumberValueText.isDisplayed());
      /*Assert.assertTrue(PageBase.OrderActivationCompletePage().device2IMEINo.isDisplayed());
         Assert.assertTrue(PageBase.OrderActivationCompletePage().device3IMEINo.isDisplayed()); */

        //CSVOperations.WriteToCSV("QA_50",orderId,iMEINumber1,"","",customerDetails.FirstName,customerDetails.LastName,
      /*CSVOperations.WriteToCSV("QA_50",orderId,iMEINumber1,"","",customerDetails.FirstName,customerDetails.LastName,
            customerDetails.EMail,receiptId,customerDetails.IDType,customerDetails.State,
            customerDetails.IDNumber, customerDetails.PhNum,customerDetails.Zip, ssn,
            customerDetails.IDExpirationMonth,customerDetails.IDExpirationYear);
            customerDetails.IDNumber, customerDetails.PhNum,customerDetails.Zip,customerDetails.SSN,
            customerDetails.IDExpirationMonth,customerDetails.IDExpirationYear);*/
        return orderId;
    }

    private void QA_2966_shipAdminVerification(String orderId)
    {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.PARKING_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.MAP_SUCCEEDED_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_ORDER_VALIDATION_PASSED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.VERIZON_WIRELESS_XML));
    }
    //endregion QA-2966 Refactored Methods

    private String poaCompleteFlow_84(String testType) throws IOException {
        String orderId = "";//Login to retail page.
        String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_3FF);
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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

        // Scanning smart phone,basic phone and MIFI phone.
        imei1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_IPhone5C);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);
        PageBase.VerizonEdgePage().declineSprintEasyPay();
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
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
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting Insurance . Need to write if else condition for internal and external testing.
        if(testType.equals("internal")) {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
        }
        else
        {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
        }

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0],spvCollections[1],spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
                cartDevice1price);


        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().emailTCChkBox);
            PageBase.TermsandConditionsPage().emailTCChkBox.click();
            PageBase.TermsandConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsandConditionsPage().continueTCButton.click();

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
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei1,simNumber1);
            PageBase.CommonControls().continueActivation.click();

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

    private String poaCompleteFlow_4248(String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";//Login to retail page.
        String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_3FF);
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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
        imei1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        Assert.assertTrue(PageBase.SprintEasyPayPage().priceBox.getText().contains("2yr agreement"));
        PageBase.SprintEasyPayPage().yesButton.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
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
        Utilities.webPageLoadTime(lStartTime, pageName);

        //Sprint Easy Pay Eligibility Result
        Utilities.waitForElementVisible(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel, 120);
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().downPaymentLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().installmentContractLengthLabel.isDisplayed());
        PageBase.SprintEasyPayEligibilityResultPage().minimumDownPaymentEasyPayRadioButton.click();
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting Insurance.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
        PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0], spvCollections[1], spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
        //    cartDevice1price);


        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().emailTCChkBox);
            PageBase.TermsandConditionsPage().emailTCChkBox.click();
            PageBase.TermsandConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsandConditionsPage().continueTCButton.click();

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
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei1,simNumber1);
            PageBase.CommonControls().continueActivation.click();

            //Device Financing Installment Contract.
            Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().print);
            PageBase.DeviceFinancingInstallmentContractPage().print.click();
            Utilities.implicitWaitSleep(3000);
            Robot robot = new Robot();
            Utilities.sendKeys(KeyEvent.VK_ENTER, robot);
            Utilities.implicitWaitSleep(6000);
            Utilities.waitForElementVisible(PageBase.DeviceFinancingInstallmentContractPage().guestAcceptChkBox);
            PageBase.DeviceFinancingInstallmentContractPage().cancelOrder.click();
            Utilities.implicitWaitSleep(4000);
            driver.switchTo().alert().accept();
            Utilities.implicitWaitSleep(4000);
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void selectingBackendSimulatorForQA_4248() {
        PageBase.AdminPage().selectWebAPIResponse("Sprint", "BackendSimulator");

        //Selecting Use Case from dropdown list.
        PageBase.AdminPage().selectAPIConfig("Sprint");
        PageBase.AdminPage().checkLoaanEligibility("LOAN_ELIGIBLE");
        PageBase.AdminPage().save();
    }

    private void selectingCarrierEnviornment_4248(@Optional String testType) throws InterruptedException, AWTException, java.io.IOException {
        if (testType.equals("internal")) {
            // Need to set "Backend Simulator or Carrier Responder depend on test case  requirement.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();

            //Selecting Backed Simulator.
            selectingBackendSimulatorForQA_4248();

            //Selecting Carrier Responder
            //selectCarrierResponderQA_4284();
        } else   //External
        {
            // Need to set External server from Admin page.
            AdminBaseClass adminBaseClass = new AdminBaseClass();
            adminBaseClass.launchAdminInNewTab();

            PageBase.AdminPage().navigateToSimulator();
            PageBase.AdminPage().selectWebAPIResponse("Sprint", "External");
        }
    }

    private void selectCarrierResponderQA_4284() {
        PageBase.AdminPage().selectWebAPIResponse("Verizon", "CarrierResponder");

        //Selecting Carrier config file.
        PageBase.AdminPage().selectAPIConfig("Sprint");

        // Selecting Sprint and response xml.
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().verizonCarrierTab);
        PageBase.CarrierResponseXMLPage().verizonCarrierTab.click();
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().versionsDropdown);
        PageBase.CarrierResponseXMLPage().selectOptions("current", "submitCreditApplication", "approved_with_deposit.xml");
        Utilities.implicitWaitSleep(3000);
        Utilities.waitForElementVisible(PageBase.CarrierResponseXMLPage().saveResponseButton);
        PageBase.CarrierResponseXMLPage().saveResponseButton.click();
        //PageBase.CarrierResponseXMLPage().loadResponseButton.click();
    }

    private void inventoryManagementVerification_4284() throws InterruptedException, AWTException, IOException {
        PageBase.InventoryManagementPage().launchInventoryInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.InventoryManagementPage().verifyDeviceStatus(imei1, InventoryManagementBaseClass.IMEIStatus.Available.toString());
    }

    private void shipAdminVerification_4248(String orderId) {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.ORDER_CANCELLED_BY_USER);
        Assert.assertTrue(eventLogTableContent.contains(Constants.NO_DEPOSIT_REQUIRED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
    }

    private String poaCompleteFlow_4242(String testType) throws IOException, AWTException, InterruptedException {
        String orderId = "";//Login to retail page.
        String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_3FF);
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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
        imei1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_4GPhone);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        Assert.assertTrue(PageBase.SprintEasyPayPage().priceBox.getText().contains("2yr agreement"));
        PageBase.SprintEasyPayPage().yesButton.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
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
        Utilities.webPageLoadTime(lStartTime, pageName);

        //Sprint Easy Pay Eligibility Result
        Utilities.waitForElementVisible(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel, 120);
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().downPaymentLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().installmentContractLengthLabel.isDisplayed());
        PageBase.SprintEasyPayEligibilityResultPage().minimumDownPaymentEasyPayRadioButton.click();
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting Insurance.
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
        }
        catch (Exception ex)
        {}

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0],spvCollections[1],spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
        //    cartDevice1price);

        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().emailTCChkBox);
            PageBase.TermsandConditionsPage().emailTCChkBox.click();
            PageBase.TermsandConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsandConditionsPage().continueTCButton.click();

            // Credit card payment  page is coming.
            boolean visaexists = driver.findElements(By.id("radio-1a")).size() != 0;
            if (visaexists) {
                Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().visaTab);
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                PageBase.PaymentRequiredPage().continuePRButton.click();
            }

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode.isDisplayed());

            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei1,simNumber1);
            PageBase.CommonControls().continueActivation.click();

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
            Utilities.implicitWaitSleep(4000);
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private void shipAdminVerification_4242(String orderId) {
        ShipAdminBaseClass.launchShipAdminInNewTab();
        Utilities.implicitWaitSleep(5000);
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);

        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ACTIVATION_COMPLETED_SUCCESSFULLY));
        Assert.assertTrue(eventLogTableContent.contains(Constants.NO_DEPOSIT_REQUIRED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.PHONE_AND_PLAN));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
    }

    private String QA_93_poaCompleteFlow(@Optional String testType) throws IOException {
        String orderId = "";//Login to retail page.
        String simNumber = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_3FF);
        //String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        //String[] spvCollections = spvDetails.split(",");
        // Scanning smart phone,basic phone and MIFI phone.
        imei1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);

        AccountDetails accountDetails = PageBase.CSVOperations().GetDetails(CSVOperations.FileName.Sprint2yrUpgrade);
        String MTNNumber = accountDetails.MTN;
        String accountPassword = accountDetails.Password;
        String SSN = accountDetails.SSN;

        PageBase.LoginPageRetail().poaLogin(Utilities.getCredentials("tuserUN"),Utilities.getCredentials("tuserPwd"), Utilities.getCredentials("storeId0003"));

        //Click on Sales and Activation Link
        PageBase.HomePageRetail().salesAndActivationsLink.click();

        //Click on Existing Carrier Link
        Utilities.waitForElementVisible(PageBase.ChoosePathPage().existingCarrier);
        PageBase.ChoosePathPage().existingCarrier.click();

        //Click on AAL to existing Family Plan
        Utilities.waitForElementVisible(PageBase.PickYourPathPage().AALExistingAccount);
        PageBase.PickYourPathPage().AALExistingAccount.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //Fill Sprint Details
        //Utilities.waitForElementVisible(PageBase.UECVerificationPage().phoneNumberSprintTextbox);
        PageBase.UECVerificationPage().fillSprintDetails(MTNNumber,accountPassword,SSN);//"9876543210","1234","94109"
        PageBase.UECVerificationPage().continueSprintButton.click();

        //Select AAL to existing Family Plan
        PageBase.SelectAnOptionPage().AALExistingFamilyPlan.click();
        PageBase.CommonControls().continueButtonDVA.click();

        //Enter IEMEI of the device
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);
        //PageBase.VerizonEdgePage().declineSprintEasyPay();
        PageBase.CommonControls().cancelButton.click();
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();

        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);

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
        try {
            PageBase.SelectProtectionPlanInsurancePage().selectNoInsurance();
        }
        catch(Exception ex){}

        Utilities.implicitWaitSleep(2000);
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

    private void shipAdminVerifications(String orderId) {
        PageBase.OrderSummaryPage().goToOrderSummaryPage(orderId);
        String eventLogTableContent = PageBase.OrderSummaryPage().checkForErrorAndLog(orderId);
        String status = PageBase.OrderSummaryPage().getOrderStatus();
        Assert.assertEquals(status, Constants.SHIPPED);
        Assert.assertTrue(eventLogTableContent.contains(Constants.SHIPPED_BUT_NOT_LITERALLY_COMMENT));
        Assert.assertTrue(eventLogTableContent.contains(Constants.ORDER_VALIDATION_PASSED));
        Assert.assertTrue(eventLogTableContent.contains(Constants.LINE_ACTIVATION_SUCCEEDED));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderNumberValueSALink.getText().contains(orderId));
        Assert.assertTrue(PageBase.OrderSummaryPage().orderBuyTypeValueText.getText().contains(Constants.HANDSET_UPGRADE));
        Assert.assertTrue(PageBase.OrderSummaryPage().partnerValueText.getText().contains(Constants.SPRINT_XML));
        Assert.assertTrue(PageBase.OrderSummaryPage().additionalInfoValueText.getText().contains(Constants.EXISTING_ACCOUNT_ORDER));
    }

    private String poaCompleteFlow_4244(String testType) throws IOException, InterruptedException, AWTException {
        String orderId = "";//Login to retail page.
        String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_3FF);
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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
        imei1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_IPhone5C);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        Assert.assertTrue(PageBase.SprintEasyPayPage().priceBox.getText().contains("2yr agreement"));
        PageBase.SprintEasyPayPage().yesButton.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
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
        Utilities.webPageLoadTime(lStartTime, pageName);

        //Sprint Easy Pay Eligibility Result
        Utilities.waitForElementVisible(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel, 120);
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().downPaymentLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().installmentContractLengthLabel.isDisplayed());

        PageBase.VerizonEdgePage().customerDownPayment.sendKeys("100");
        PageBase.SprintEasyPayEligibilityResultPage().minimumDownPaymentEasyPayRadioButton.click();
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting Insurance.
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
        }
        catch (Exception ex)
        {}

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0],spvCollections[1],spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
        //    cartDevice1price);

        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().emailTCChkBox);
            PageBase.TermsandConditionsPage().emailTCChkBox.click();
            PageBase.TermsandConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsandConditionsPage().continueTCButton.click();

            // Credit card payment  page is coming.
            boolean visaexists = driver.findElements(By.id("radio-1a")).size() != 0;
            if (visaexists) {
                Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().visaTab);
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                PageBase.PaymentRequiredPage().continuePRButton.click();
            }

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode.isDisplayed());

            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei1,simNumber1);
            PageBase.CommonControls().continueActivation.click();

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
            Utilities.implicitWaitSleep(4000);
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }

    private String poaCompleteFlow_3928(String testType) throws IOException {
        String orderId = "";//Login to retail page.
        String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_3FF);
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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

        // Scanning smart phone
        imei1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SamsungGalaxyS4_16GBWhite);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);
        PageBase.VerizonEdgePage().declineSprintEasyPay();
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButtonDVA);
        PageBase.CommonControls().continueButtonDVA.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().skip);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
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
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting No Insurance .
        Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
        PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0],spvCollections[1],spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
                cartDevice1price);


        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().emailTCChkBox);
            PageBase.TermsandConditionsPage().emailTCChkBox.click();
            PageBase.TermsandConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsandConditionsPage().continueTCButton.click();

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
           // PageBase.PrintMobileScanSheetPage().verifyAllTwoDeviceBarCode();

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
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei1, simNumber1);
            PageBase.CommonControls().continueButtonDVA.click();

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

    private String poaCompleteFlow_4162(String testType) throws IOException, InterruptedException, AWTException {
        String orderId = "";//Login to retail page.
        String simNumber1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_3FF);
        String spvDetails = PageBase.CSVOperations().GetSpvDetails();
        String[] spvCollections = spvDetails.split(",");
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
        imei1 = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_IPhone5C);
        PageBase.DeviceScanPage().enterDeviceScanDetails(imei1);

        //Sprint Easy Pay Page
        Utilities.waitForElementVisible(PageBase.SprintEasyPayPage().yesButton);
        Assert.assertTrue(PageBase.SprintEasyPayPage().priceBox.getText().contains("2yr agreement"));
        PageBase.SprintEasyPayPage().yesButton.click();

        //Filling information in Carrier Credit Check Page.
        Utilities.waitForElementVisible(PageBase.CarrierCreditCheckPage().guestAgreeToRunCCCheckBox);
        String ssn = PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.Sprint_SSNWithoutDeposit);
        CarrierCreditCheckDetails cccDetails = getCarrierCreditCheckDetails(ssn);
        PageBase.CarrierCreditCheckPage().populatingCarrierCreditCheckPage(cccDetails);

        // Credit Check Verification Results page.
        PageBase.CreditCheckVerificationResultsPage().depositCheckBox.click();
        PageBase.CommonControls().continueCommonButton.click();

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
        Utilities.webPageLoadTime(lStartTime, pageName);

        //Sprint Easy Pay Eligibility Result
        Utilities.waitForElementVisible(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel, 120);
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().eligibleForEasyPayLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().downPaymentLabel.isDisplayed());
        Assert.assertTrue(PageBase.SprintEasyPayEligibilityResultPage().installmentContractLengthLabel.isDisplayed());

        PageBase.VerizonEdgePage().customerDownPayment.sendKeys("100");
        PageBase.SprintEasyPayEligibilityResultPage().minimumDownPaymentEasyPayRadioButton.click();
        PageBase.CommonControls().continueCommonButton.click();

        // Selecting Plan.
        Utilities.waitForElementVisible(PageBase.VerizonShopPlansPage().sprintFamilySharePlan);
        PageBase.VerizonShopPlansPage().sprintFamilySharePlan.click();
        PageBase.VerizonShopPlansPage().addPlan();

        //Verifying device with plan and continue.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);
        cartDevice1price = PageBase.CartPage().device1Price.getText();
        PageBase.CommonControls().continueCommonButton.click();

        //Selecting plan feature.
        Utilities.waitForElementVisible(PageBase.SelectPlanFeaturesPage().continueSPFButton);
        PageBase.SelectPlanFeaturesPage().continueSPFButton.click();

        // Selecting Insurance.
        try {
            Utilities.waitForElementVisible(PageBase.CommonControls().continueButton);
            PageBase.SelectProtectionPlanInsurancePage().selectAnInsurance();
        }
        catch (Exception ex)
        {}

        // Selecting No Number Porting.
        Utilities.waitForElementVisible(PageBase.NumberPortPage().noNumberPortRadiobutton);
        PageBase.NumberPortPage().noNumberPortRadiobutton.click();
        PageBase.CommonControls().continueButton.click();

        //Service Provider Verification Page
        PageBase.ServiceProviderVerificationPage().populatingSprintSPV(spvCollections[0], spvCollections[1], spvCollections[2]);

        // Order Review and Confirm Page.
        Utilities.waitForElementVisible(PageBase.CommonControls().continueCommonButton);

        //TODO: Need to read from data sheet.
        //Assert.assertEquals(PageBase.OrderReviewAndConfirmPage().device1PriceActual.getText(),
        //    cartDevice1price);

        PageBase.CommonControls().continueCommonButton.click();

        if (readConfig("Activation").contains("true")) {
            //Terms and Condition Page.
            Utilities.waitForElementVisible(PageBase.TermsandConditionsPage().emailTCChkBox);
            PageBase.TermsandConditionsPage().emailTCChkBox.click();
            PageBase.TermsandConditionsPage().carrierTermsCheckBox.click();
            PageBase.TermsandConditionsPage().acceptsTargetTCCheckbox.click();
            PageBase.WirelessCustomerAgreementPage().signingWCA(driver);
            PageBase.TermsandConditionsPage().continueTCButton.click();

            // Credit card payment  page is coming.
            boolean visaexists = driver.findElements(By.id("radio-1a")).size() != 0;
            if (visaexists) {
                Utilities.waitForElementVisible(PageBase.PaymentRequiredPage().visaTab);
                PageBase.PaymentRequiredPage().populatingCardDetailsPaymentRequired(PaymentRequiredPage.CardType.VISA);
                PageBase.PaymentRequiredPage().continuePRButton.click();
            }

            //Print Mobile Scan Sheet.
            Utilities.waitForElementVisible(PageBase.PrintMobileScanSheetPage().continueFirstMSSButton);
            orderId = PageBase.PrintMobileScanSheetPage().orderNumberValuePMSSText.getText();
            Assert.assertTrue(PageBase.PrintMobileScanSheetPage().firstDeviceBarCode.isDisplayed());

            PageBase.PrintMobileScanSheetPage().continueFirstMSSButton.click();

            // Payment Verification page. Scan Reciept id.
            Utilities.waitForElementVisible(PageBase.PaymentVerificationPage().textboxTargetReceiptID);
            PageBase.PaymentVerificationPage().textboxTargetReceiptID.sendKeys(PageBase.CSVOperations().GetIMEIOrSimNumberOrReceiptId(CSVOperations.FileName.ReceiptId));
            PageBase.PaymentVerificationPage().submitButton.click();

            //Device Verification and Activation page. Scan Device IEMI and enter SIM number.
            Utilities.waitForElementVisible(PageBase.DeviceVerificationaandActivation().deviceIMEITextbox);
            PageBase.DeviceVerificationaandActivation().deviceVerificationActiavtionFor1Device(imei1,simNumber1);
            PageBase.CommonControls().continueActivation.click();

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
            Utilities.implicitWaitSleep(4000);
        } else {
            Reporter.log("<h3><font color='red'> Activation is stopped purposefully. Change the key in Test Settings to Activate </h3></font>");
        }
        return orderId;
    }
    //endregion private methods
}
