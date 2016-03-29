package com.heroesfire.test;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GuidesDisplay {

	private String BASE_URL = "http://www.heroesfire.com";

	private WebDriver driver;

	private WebDriverWait wait;

	@AfterClass(groups = { "firefox", "chrome", "safari" })
	public void afterClass() {
		driver.quit();
	}

	@BeforeClass(groups = { "chrome" })
	public void beforeClassChrome() {
		System.setProperty("webdriver.chrome.driver",
				"/Users/chance/Dropbox/sqaworkspace/heroesfire-website/Drivers/chromedriver");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 10);
	}

	@BeforeClass(groups = { "firefox" })
	public void beforeClassFirefox() {
		driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 10);
	}

	@BeforeClass(groups = { "safari" })
	public void beforeClassSafari() {
		driver = new SafariDriver();
		wait = new WebDriverWait(driver, 10);
	}

	@Test(dataProvider = "guideData", groups = { "firefox", "chrome", "safari" })
	public void guideDisplayTest(String hero) {
		boolean nextPage = true;
		boolean guideLoads = true;
		List<WebElement> guides = new ArrayList<WebElement>();
		ArrayList<String> guideLinks = new ArrayList<String>();
		List<WebElement> pages = new ArrayList<WebElement>();
		driver.get(BASE_URL);
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector(".box.wiki-grid.hero-rotation.mb10>div>div a[href*='" + hero + "']"))).click();
		while (nextPage != false) {
			guides.addAll(wait.until(
					ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".browse-item-list>a[href]"))));
			for (WebElement guide : guides) {
				guideLinks.add(guide.getAttribute("href"));
			}
			guides.clear();
			pages.addAll(driver.findElements(By.cssSelector("#paging>a[href]")));
			for (WebElement page : pages) {
				nextPage = ("NEXT").equals(page.getText());
				if (nextPage == true) {
					driver.get(page.getAttribute("href"));
					pages.clear();
					break;
				}
			}
		}
		for (String guidePage : guideLinks) {
			driver.get(guidePage);
			guideLoads =
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#build-rotator>article")))
							.isDisplayed();
			if (guideLoads != true) {
				break;
			}
		}
		Assert.assertEquals(guideLoads, true);
	}

	@DataProvider
	private Object[][] guideData() {
		return new Object[][] { new Object[] { "abathur" }, new Object[] { "anubarak" }, new Object[] { "jaina" } };
	}
}
