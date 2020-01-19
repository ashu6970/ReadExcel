package com.pages;

import org.openqa.selenium.WebDriver;

public class LoginPage {
	private ThreadLocal<WebDriver> driver;

	public LoginPage(ThreadLocal<WebDriver> driver) {
		super();
		this.driver = driver;
	}

	public void openLogin() {
		driver.get().get("https://mvnrepository.com/");
	}

}
