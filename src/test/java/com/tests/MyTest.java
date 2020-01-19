package com.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.pages.HomePage;
import com.pages.LoginPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MyTest {

	ThreadLocal<LoginPage> loginPage = new ThreadLocal<LoginPage>();
	ThreadLocal<HomePage> homePage = new ThreadLocal<HomePage>();
	ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	
	@BeforeMethod
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver.set(new ChromeDriver());
		driver.get().manage().window().maximize();
		loginPage.set(new LoginPage(driver));
		homePage.set(new HomePage(driver));
		
		//		loginPage.set(new Login);
	}
	
	@Test
	public void login() {
		loginPage.get().openLogin();
	}
	
	@Test
	public void home() {
		homePage.get().openHome();
	}
}
