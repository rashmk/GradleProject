package com.consensus.qa.pages;

import java.awt.AWTException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.consensus.qa.framework.BaseClass;
import com.consensus.qa.framework.BrowserSettings;
import com.consensus.qa.framework.ControlLocators;
import com.consensus.qa.framework.PageBase;

public class ShipAdminPage {
	public ShipAdminPage(WebDriver d)
	{
		PageFactory.initElements(d, this);
	}
	
	@FindBy(xpath = ControlLocators.ORDER_TEXTBOX)
	public WebElement orderTextbox;
	
	@FindBy(xpath = ControlLocators.MANUAL_REVIEW_STATUS)
	public WebElement manualReviewStatusOnShipAdmin;	

	@FindBy(xpath = ControlLocators.CREDIT_CHECK_RESULT_SUMMARY_TABLE)
	public WebElement creditCheckResultSummaryTable;
	
	@FindBy(xpath = ControlLocators.UPDATE_CREDIT_CHECK)
	public WebElement updateCreditCheck;
	
	@FindBy(xpath = ControlLocators.CREDIT_CHECK_RESULT_UPDATE_DROPDOWN)
	public WebElement updateCreditCheckResult;
	
	@FindBy(xpath = ControlLocators.CREDIT_CHECK_RESULT_RESOLUTION_UPDATE_DROPDOWN)
	public WebElement updateCreditCheckResultReason;
	
	@FindBy(xpath = ControlLocators.NUMBER_OF_LINES_TO_UPDATE)
	public WebElement numberOfLinesToUpdate;
	
	@FindBy(xpath = ControlLocators.ADD_UPDATE_CREDITCHECK_BUTTON)
	public WebElement addUpdateCreditCheck;
	
	@FindBy(xpath = ControlLocators.PARTNER_TEXT)
	public WebElement partnerText;
	
	@FindBy(xpath = ControlLocators.ORDER_LINE_CREDIT_DROPDOWN)
	public WebElement orderLineCredit;
	
	@FindBy(xpath = ControlLocators.MOVE_QUEUES_DROPDOWN)
	public WebElement moveQueuesdropdown;
	
	@FindBy(xpath = ControlLocators.MOVE_QUEUES)
	public WebElement moveQueues;
	
	@FindBy(xpath = ControlLocators.MOVE_QUEUES_BUTTON)
	public WebElement moveQueuesButton;
	
	
	@FindBy(xpath = ControlLocators.ORDER_SUB_STATUS)
	public WebElement orderSubStatus;
}