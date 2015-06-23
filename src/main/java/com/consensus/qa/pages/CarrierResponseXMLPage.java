package com.consensus.qa.pages;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import com.consensus.qa.framework.BrowserSettings;
import com.consensus.qa.framework.ControlLocators;
import com.consensus.qa.framework.PageBase;
import com.consensus.qa.framework.Utilities;
import com.consensus.qa.framework.Utilities.SelectDropMethod;

public class CarrierResponseXMLPage {
	public CarrierResponseXMLPage( WebDriver d)
	{
		PageFactory.initElements(d, this);
	}

	@FindBy(xpath = ControlLocators.VERIZON_CARRIER_TAB)
	public WebElement verizonCarrierTab;
	
	@FindBy(xpath = ControlLocators.SPRINT_CARRIER_TAB)
	public WebElement sprintCarrierTab;
	
	@FindBy(name = ControlLocators.VERSIONS_DROPDOWN)
	public WebElement versionsDropdown;	

	@FindBy(name = ControlLocators.SERVICES_DROPDOWN)
	public WebElement servicesDropdown;	

	@FindBy(name = ControlLocators.TEMPLATES_DROPDOWN)
	public WebElement templatesDropdown;
	
	@FindBy(xpath = ControlLocators.SAVE_RESPONSE_BUTTON)
	public WebElement saveResponseButton;

	@FindBy(xpath = ControlLocators.LOAD_RESPONSE_BUTTON)
	public WebElement loadResponseButton;
	
	@FindBy(id = ControlLocators.XML_TEXTAREA)
	public WebElement xmlTextArea;
	
	public void SelectVersions(String value)
	{  
		Utilities.dropdownSelect(versionsDropdown, Utilities.SelectDropMethod.SELECTBYVALUE, value);
	}
	
	public void SelectServices(String value)
	{  
		Utilities.dropdownSelect(servicesDropdown, Utilities.SelectDropMethod.SELECTBYVALUE, value);
	}
	
	public void SelectTemplates(String value)
	{  
		Utilities.dropdownSelect(templatesDropdown, Utilities.SelectDropMethod.SELECTBYVALUE, value);
	}
	

	public void SelectCarrierResponserXML() throws FileNotFoundException, IOException, InterruptedException
	{
		   //verizonCarrierTab.click();
		   Thread.sleep(2000); 
		    byte[] encoded;
	        StringBuilder sb = new StringBuilder();
	        String sCurrentLine;
	        try (BufferedReader br = new BufferedReader(new FileReader("C:/WorkingPOA/PoaAutomation/PoaAutomation/TestData/CarrierResponderData.xml"))){

	        	while ((sCurrentLine = br.readLine()) != null) {
	                sb.append(sCurrentLine);
	            }
	        }    
		    //System.out.println(sb);
	        xmlTextArea.sendKeys(sb);
	        saveResponseButton.click();
	}
	
	//Select all Dropdowns
	public void selectOptions(String versionsValue,String servicesValue,String templatesValue) 
	{
		Utilities.waitForElementVisible(versionsDropdown);
		Utilities.dropdownSelect(versionsDropdown, Utilities.SelectDropMethod.SELECTBYVALUE, versionsValue);
		Reporter.log("<br> Version set:" +versionsValue);
		Utilities.waitForElementVisible(servicesDropdown);
		Utilities.dropdownSelect(servicesDropdown, Utilities.SelectDropMethod.SELECTBYVALUE, servicesValue);
		Reporter.log("<br>service value:" +servicesValue);
		Utilities.waitForElementVisible(templatesDropdown);
		Utilities.dropdownSelect(templatesDropdown, Utilities.SelectDropMethod.SELECTBYVALUE, templatesValue);
		Reporter.log("<br> XML Selected: " +templatesValue);
	}
}
