package com.tests;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class EmailData {

	private By signInLink = By.xpath(
			"//ul[contains(@class,'h-c-header__cta-list header__nav--ltr')]//a[contains(@class,'h-c-header__nav-li-link') and contains(@ga-event-action,'sign')]");
	private By emailTextbox = By.xpath("//input[@type='email']");
	/** The pwd textbox. */
	private By pwdTextbox = By.xpath("//input[@type='password']");
	private By nextBtn = By.xpath("//span[contains(@class, 'CwaK9')]");

	private By folder = By.xpath("//a[@title='dec billing']");

	/** The search dropdown. */
	private By searchDropdown = By.xpath("//button[contains(@data-tooltip,'search')]");

	/** The to textbox. */
	private By toTextbox = By
			.xpath("//*[contains(@class,'ZG')]//label[contains(text(),'To')]//following::span//input[1]");

	private By fromTextbox = By
			.xpath("//*[contains(@class,'ZG')]//label[contains(text(),'From')]//following::span[1]//input[1]");

	/** The subject textbox. */
	private By subjectTextbox = By
			.xpath("//div[contains(@class,'ZZ')]//label[contains(text(),'Subject')]//following::span//input");

	/** The keyword textbox. */
	private By keywordTextbox = By.xpath("//div[contains(@class,'ZZ')]//input[@class='ZH nr aQd']");

	/** The search btn. */
	private By searchBtn = By.xpath("//div[contains(@class,'ZZ')]//div[contains(@aria-label,'Search Mail')]");

	/** The first emailxpath. */
	String emailXpathInFolder = "//div[@role='main']//tr[contains(@class,'zA yO byw')][%s]//td[5]";

	private By attachment = By.xpath("//span[contains(@download_url,'Invoice')]");

	private By emailToLoc = By.xpath(
			"//strong[@class='gmail_sendername']/following::a[contains(@href,'mailto')][not(contains(@href,'xero'))][not(contains(@href,'@engineer.ai'))]");

	private By contactNameLoc = By.xpath("//p[contains(text(),'TAX INVOICE')]/following::p[1]");
	private By accountNumberLoc = By.xpath("//p[contains(text(),'TAX INVOICE')]/following::p[21]");
	private By emailAddressLoc = By.xpath("//span[@download_url]");
	private By poAttentionToLoc = By.xpath("//p[contains(text(),'TAX INVOICE')]/following::p[2]");
	private By poAddressLine1Loc = By.xpath("//p[contains(text(),'TAX INVOICE')]/following::p[3]");
	private By poAddressLine2Loc = By.xpath("//p[contains(text(),'TAX INVOICE')]/following::p[3]");
	private By poAddressLine3Loc = By.xpath("//p[contains(text(),'TAX INVOICE')]/following::p[3]");
	private By poAddressLine4Loc = By.xpath("//p[contains(text(),'TAX INVOICE')]/following::p[3]");
	// private By poCityLoc = By.xpath("//p[contains(text(),'TAX
	// INVOICE')]/following::p[4]");
	// private By poRegionLoc = By.xpath("//p[contains(text(),'TAX
	// INVOICE')]/following::p[5]");
	// private By poPostalCodeLoc = By.xpath("//p[contains(text(),'TAX
	// INVOICE')]/following::p[5]");
	// p[contains(text(),'GSTN')]
	// private String poAddLine4Loc =

	private String commonLoc = "/preceding-sibling::p[not(contains(text(),'TAX INVOICE'))]";
	private String poCountryLoc = "//p[contains(text(),'GSTN')]";
	private By taxNumberLoc = By.xpath("//p[contains(text(),'TAX INVOICE')]/following::p[6]");
	private By invoiceNumberLocc = By.xpath(
			"//p[contains(text(),'TAX INVOICE')]/following::p[contains(text(),'Invoice Number')]/following::p[1]");

	private String contactName;
	private String accountNumber;
	private String emailAddress;
	private String poAttentionTo;
	private String poAddressLine1;
	private String poAddressLine2;
	private String poAddressLine3;
	private String poAddressLine4;
	// private String poCity;
	// private String poRegion;
	// private String poPostalCode;
	private String poCountry;
	private String taxNumber;
	private String invoiceNumber;

	ThreadLocal<WebDriver> driver;
	private boolean isAttention;

	private final String url = "https://gmail.com";
	private final String username = "parshant.arya@engineer.ai";
	private final String password = "Admin@123";
	private final String inputSheetName = "emails";
	private final String outputSheetName = "output";
	private final String column1 = "to";
	private final String column2 = "subject";

	private String allEmailsToCSV = "";

	@BeforeClass
	public void setup() {
		initializeDriver();
		openEmail(url, username, password);
	}

	@Test
	public void master() throws Exception {
		for (int i = 78; i <= 83; i++) {
			click(folder, 5);
			allEmailsToCSV = "";
			navigateToEmailsAttachment(String.valueOf(i));
			saveAttachmentDataInExcel();
			sleep(2);
			exitAttachment();
			System.out.println("DONE for email no=" + i);
		}

	}

	public void navigateToEmailsAttachment(String counter) throws AWTException {
		click(By.xpath(String.format(emailXpathInFolder, counter)), 2);
		sleep(3);
		allEmailsToCSV = getAllSentEmails();
		click(attachment, 2);
		sleep(5);
	}

	public void initializeDriver() {
		driver = new ThreadLocal<WebDriver>();
		WebDriverManager.chromedriver().setup();
		driver.set(new ChromeDriver());
		driver.get().manage().window().maximize();
		driver.get().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterMethod(alwaysRun = true)
	public void closeBrowerAndDriver() throws IOException {
		driver.get().close();
		driver.get().quit();
		DataUtils.closeWorkbook();
	}

	public void openEmail(String url, String username, String password) {
		// open email
		driver.get().get(url);
		driver.get().findElement(emailTextbox).sendKeys(username);
		driver.get().findElement(nextBtn).click();
		sleep(2);
		driver.get().findElement(pwdTextbox).sendKeys(password);
		driver.get().findElement(nextBtn).click();
		sleep(10);
	}

	public String getValuesFromExcel(String sheetName, String whereLiteral, String column) {
		return DataUtils.getTestData(sheetName, whereLiteral, column);
	}

	public void click(By locator, int waitTime) {
		sleep(waitTime);
		driver.get().findElement(locator).click();
	}

	public void type(By locator, String text, int waitTime) {
		sleep(waitTime);
		driver.get().findElement(locator).clear();
		driver.get().findElement(locator).sendKeys(text);
	}

	public String getText(By locator, int waitTime) {
		String text = driver.get().findElement(locator).getText();
		return text;
	}

	public void exitAttachment() throws AWTException {
		sleep(1);
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_ESCAPE);
		sleep(1);
		r.keyRelease(KeyEvent.VK_ESCAPE);
		sleep(1);
	}

	public void saveAttachmentDataInExcel() throws Exception {
		String csvString = getAllDataFromAttachment();
		System.out.println("csv=" + csvString);
		DataUtils.writeExcel(csvString);

	}

	public String getAllDataFromAttachment() {
		contactName = getText(contactNameLoc, 1);
		poAttentionTo = getAttention(getText(poAttentionToLoc, 1));
		poAddressLine1 = getAddressLine1();
		poAddressLine2 = getAddressLine2();
		poAddressLine3 = getAddressLine3();
		poAddressLine4 = getAddressLine4();

		// poCity=getText(poCityLoc, 1);
		// poRegion=getText(poRegionLoc, 1);
		// poPostalCode=getText(poPostalCodeLoc, 1);
		// poCountry=getText(poCountryLoc, 1);

		taxNumber = getText(taxNumberLoc, 1);
		String csvString = contactName + ";" + poAttentionTo + ";" + getAddress() + ";" + getCityRegionCode() + ";"
				+ getPOCountry() + ";" + getTaxNumber() + ";" + getInvoiceNum() + ";" + getAllSentEmails();

		return csvString;
	}

	public String getAllSentEmails() {
		List<WebElement> allEMailsToList = driver.get().findElements(emailToLoc);
		String allEMailsToCSV = "";
		for (WebElement e : allEMailsToList) {
			allEMailsToCSV = allEMailsToCSV + e.getText() + ",";
		}
		System.out.println("allEMailsToCSV=" + allEMailsToCSV);
		return allEMailsToCSV;
	}

	public void sleep(int time) {
		time = time * 1000;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getAttention(String s) {
		if (s.contains("Attention")) {
			isAttention = true;
			return s.split("Attention:")[1];
		} else {
			isAttention = false;
			return "N.A";
		}
	}

	public String getAddressLine1() {
//		String addL1;
//		try {
//			addL1=getText(poAddressLine1Loc, 1);
//		}catch(Exception e) {
//			addL1="";
//		}
//		return addL1;

		return "";
	}

	public String getAddressLine2() {
//		String addL2;
//		try {
//			addL2=getText(poAddressLine2Loc, 1);
//		}catch(Exception e) {
//			addL2="";
//		}
//
//		return addL2;

		return "";
	}

	public String getAddressLine3() {
//		String addL3;
//		try {
//			addL3=getText(poAddressLine3Loc, 1);
//		}catch(Exception e) {
//			addL3="";
//		}
//		return addL3;

		return "";
	}

	public String getAddressLine4() {
//		String addL4;
//		try {
//			addL4=getText(By.xpath(poCountryLoc+commonLoc+"[2]"), 1);
//		}catch(Exception e) {
//			addL4="";
//		}
//		return addL4;

		return "";
	}

	public String getAddress() {
		String add;
		add = getText(By.xpath(poCountryLoc + commonLoc + "[3]"), 1) + " "
				+ getText(By.xpath(poCountryLoc + commonLoc + "[2]"), 2) + " "
				+ getText(By.xpath(poCountryLoc + commonLoc + "[1]"), 1);
		return add;
	}

	public String getCityRegionCode() {
		String crcod;
		crcod = getText(By.xpath(poCountryLoc + commonLoc + "[1]"), 1);
		return crcod;
	}

	public String getPOCountry() {
		String addL4;
		addL4 = getText(By.xpath(poCountryLoc), 1);
		return addL4;
	}

	public String getTaxNumber() {
		String taxNum;
		try {
			taxNum = getText(By.xpath("//p[contains(text(),'GSTN')]/following::p[1][contains(text(),'Regn')]"), 1);
		} catch (Exception e) {
			taxNum = "N.A.";
		}

		return taxNum;
	}

	public String getInvoiceNum() {
		return driver.get().findElement(invoiceNumberLocc).getText();
	}

}

//		DataUtils.insertData(outputSheetName, "contactName,accountNumber,emailAddress,poAttentionTo,poAddressLine1,poAddressLine2,poAddressLine3,poAddressLine4,poCity,poRegion,poPostalCode,poCountry,taxNumber,defaultTaxSales","'"+contactName+"','"+accountNumber+"','"+emailAddress+"','"+poAttentionTo+"','"+poAddressLine1+"','"+poAddressLine2+"','"+poAddressLine3+"','"+poAddressLine4+"','"+poCity+"','"+poRegion+"','"+poPostalCode+"','"+poCountry+"','"+taxNumber+"','"+defaultTaxSales+"'");

//		DataUtils.insertData(outputSheetName, "'contactName'","'"+contactName+"'");

//		DataUtils.insertData(outputSheetName, "accountNumber", accountNumber);
//		DataUtils.insertData(outputSheetName, "emailAddress", emailAddress);
//		DataUtils.insertData(outputSheetName, "poAttentionTo", poAttentionTo);
//		DataUtils.insertData(outputSheetName, "poAddressLine1", poAddressLine1);
//		DataUtils.insertData(outputSheetName, "poAddressLine2", poAddressLine2);
//		DataUtils.insertData(outputSheetName, "poAddressLine3", poAddressLine3);
//		DataUtils.insertData(outputSheetName, "poAddressLine4", poAddressLine4);
//		DataUtils.insertData(outputSheetName, "poCity", poCity);
//		DataUtils.insertData(outputSheetName, "poRegion", poRegion);
//		DataUtils.insertData(outputSheetName, "poPostalCode", poPostalCode);
//		DataUtils.insertData(outputSheetName, "poCountry", poCountry);
//		DataUtils.insertData(outputSheetName, "taxNumber", taxNumber);
//		DataUtils.insertData(outputSheetName, "defaultTaxSales", defaultTaxSales);