package com.consensus.qa.pages;

import com.consensus.qa.framework.Log;
import com.consensus.qa.framework.PageBase;
import junit.framework.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.consensus.qa.framework.ControlLocators;

public class OrderReviewAndConfirmPage {

	/* region Page Initialization */
	public OrderReviewAndConfirmPage(WebDriver d) {
		PageFactory.initElements(d, this);
	}
	/* end region Page Initialization */

	/* region Select Insurance Page Elements */
	@FindBy(xpath = ControlLocators.CANCEL_ORDER_BUTTON)
	public WebElement cancelOrderButton;

	@FindBy(xpath = ControlLocators.PLAN_DESCRIPTION_TEXT)
	public WebElement planDescriptionText;

	@FindBy(xpath = ControlLocators.VERIFY_2YR_UPGRADE_TEXT)
	public WebElement verify2yrUpgradeText;

	@FindBy(xpath = ControlLocators.VERIFY_PLAN_DESCRIPTION_TEXT)
	public WebElement verifyPlanDescriptionText;

	@FindBy(xpath = ControlLocators.phonePrice$649_99)
	public WebElement phonePrice$649_99;

	@FindBy(xpath = ControlLocators.PHONE_PRICE_LINE1)
	public WebElement phonePriceLine1;

	@FindBy(xpath = ControlLocators.PHONE_PRICE_LINE2)
	public WebElement phonePriceLine2;

	@FindBy(xpath = ControlLocators.UPGRADE_FEE_VALUE_TEXT)
	public WebElement upgradeFeeValueText;
	
	@FindBy(xpath = ControlLocators.UPGRADE_FEE_VALUE_2_TEXT)
	public WebElement upgradeFeeValue2Text;

	@FindBy(xpath = ControlLocators.TOTAL_DUE_TODAY_VALUE)
	public WebElement totalDueTodayValue;

	@FindBy(xpath = ControlLocators.EXISTING_PLAN_DIV)
	public WebElement existingPlanDiv;

	@FindBy(xpath = ControlLocators.EXISTING_PLAN_2_DIV)
	public WebElement existingPlan2Div;

	@FindBy(xpath = ControlLocators.TWO_YEARS_UPGRADE_LABEL)
	public WebElement twoYearsUpgradeLabel;
	
	@FindBy(xpath = ControlLocators.TWO_YEARS_UPGRADE_2_LABEL)
	public WebElement twoYearsUpgrade2Label;

	@FindBy(xpath = ControlLocators.DEVICE1_PRICE_ORDERREVIEW)
	public WebElement device1PriceActual;

	@FindBy(xpath = ControlLocators.DEVICE2_PRICE_ORDERREVIEW)
	public WebElement device2PriceActual;

	@FindBy(xpath = ControlLocators.DEVICE3_PRICE_ORDERREVIEW)
	public WebElement device3PriceActual;

	@FindBy(xpath = ControlLocators.DEVICE1_ACTIVATIONFEE_ORDERREVIEW)
	public WebElement device1ActivationFeeActual;

	@FindBy(xpath = ControlLocators.DEVICE2_ACTIVATIONFEE_ORDERREVIEW)
	public WebElement device2ActivationFeeActual;

	@FindBy(xpath = ControlLocators.DEVICE3_ACTIVATIONFEE_ORDERREVIEW)
	public WebElement device3ActivationFeeActual;

	@FindBy(xpath = ControlLocators.TOTAL_FEE)
	public WebElement totalFeeActual;

	@FindBy(xpath = ControlLocators.PLAN_PRICE_ORDERREVIEW)
	public WebElement planPrice;

	@FindBy(className = ControlLocators.ORDER_DETAILS)
	public WebElement orderDetails;

	@FindBy(xpath = ControlLocators.PHONE_MONTHLY_FEE)
	public WebElement phoneMonthlyFee;

	@FindBy(xpath = ControlLocators.PHONE_MONTHLY_FEE)
	public WebElement downPaymentForSprintEasyPayLabel;

	@FindBy(id = ControlLocators.PAGE_TEXT)
	public WebElement pageText;

	@FindBy(xpath = ControlLocators.FIRST_PHONE_PRICE_Value_TEXT)
	public WebElement firstPhonePrice;

	@FindBy(xpath = ControlLocators.LAST_MONTH_INSTALLMENT_LABEL)
	public WebElement lastMonthInstallmentLabel;

	@FindBy(xpath = ControlLocators.MONTHLY_INSTALLMENT_LABEL)
	public WebElement monthlyInstallmentLabel;

	@FindBy(xpath = ControlLocators.DOWN_PAYMENT_ORDER_REVIEW_LABEL)
	public WebElement downPaymentLabel;

	@FindBy(xpath = ControlLocators.MONTHLY_RECURRING_FEE_LABEL)
	public WebElement monthlyRecurringFeeLabel;

	public String GetOrderDetails() {
	String orderTableContent = orderDetails.getText();
	//String lines[] = orderTableContent.split("\n");
	//Log.info("-------------------START ERROR INFO-----------------------------");
	//Log.info("Error Info for Order Number:-"+orderId+"\n"); //ordreId
	//Log.info("Order Status:-"+PageBase.OrderSummaryPage().statusValueLink.getText()+"\n");
   // System.out.println(lines.toString());
    return orderTableContent;
}
}
