package com.pages;

import org.openqa.selenium.WebDriver;

public class HomePage {
	private ThreadLocal<WebDriver> driver;

	public HomePage(ThreadLocal<WebDriver> driver) {
		super();
		this.driver = driver;
	}

	public void openHome() {
		driver.get().get("https://mvnrepository.com/artifact/io.github.bonigarcia/webdrivermanager/3.7.1");
	}
}
