	
package com.consensus.qa.att.tests;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ExchangeTests {
	@Parameters({"test-type"})
	@Test	 
	   public void testExchange1( String pram) {
	      String str = "TestNG is working fine";
	      assertEquals("TestNG is working fine", str);
	      System.out.println("End of Test 1 in Exchange Tests"+pram);
	 }

	 @Test
	 @Parameters("test-type")
	   public void testExchange2(@Optional("Abc") String S) {
	  	      System.out.println("End of Test 2 Exhange Tests");
	 }


	@Test
	public void LogInToRetail() {
		Assert.assertTrue(true);
	}
	@Test
	public void LogInToRetail1() {
		Assert.assertTrue(true);
	}
	@Test
	public void LogInToRetail2() {
		Assert.assertTrue(true);
	}
	@Test
	public void LogInToRetail3() {
		Assert.assertTrue(true);
	}
	@Test
	public void LogInToRetail4() {
		Assert.assertTrue(true);
	}
}
