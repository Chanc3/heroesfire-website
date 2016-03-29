package com.heroesfire.test;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StatsIncrease {

	private static double calculateIncrease(double gain, double baseStat, int level) {
		double newStat = baseStat;
		if (gain != 0) {
			for (int i = 1; i < level; i++) {
				newStat = (newStat * gain) + newStat;
			}
		}
		return newStat;
	}

	private String BASE_URL = "http://www.heroesfire.com";

	private WebDriver driver;

	private WebDriverWait wait;

	private Actions action;

	@AfterClass(groups = { "firefox", "chrome" })
	public void afterClass() {
		driver.quit();
	}

	@BeforeClass(groups = { "chrome" })
	public void beforeClassChrome() {
		System.setProperty("webdriver.chrome.driver",
				"/Users/chance/Dropbox/sqaworkspace/heroesfire-website/Drivers/chromedriver");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 10);
		action = new Actions(driver);
		driver.get(BASE_URL);
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".self-clear>.megamenu a[href*='talent-calculator']")))
				.click();
	}

	@BeforeClass(groups = { "firefox" })
	public void beforeClassFirefox() {
		driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 10);
		action = new Actions(driver);
		driver.get(BASE_URL);
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".self-clear>.megamenu a[href*='talent-calculator']")))
				.click();
	}

	@Test(dataProvider = "heroData", groups = { "firefox", "chrome" })
	public void healthIncreaseTest(String hero) {
		ArrayList<WebElement> stats = new ArrayList<WebElement>();
		ArrayList<String> beforeStats = new ArrayList<String>();
		ArrayList<String> afterStats = new ArrayList<String>();
		ArrayList<Double> statGain = new ArrayList<Double>();
		boolean statIncreased = true;
		int level;
		double total;
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".choose-hero a[href*='" + hero + "']")))
				.click();
		stats.add(wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".stats.mb10>div:nth-child(1) [data-base]"))));
		for (WebElement stat : stats) {
			beforeStats.add(stat.getText());
		}
		stats.clear();
		action.dragAndDropBy(
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-slider-handle"))), 20, 0)
				.perform();
		stats.add(wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".stats.mb10>div:nth-child(1) [data-gain]"))));
		for (WebElement stat : stats) {
			if (!stat.getText().contains("N/A")) {
				afterStats.add(stat.getText());
				statGain.add(Double.parseDouble(stat.getAttribute("data-gain")));
			}
		}
		level = Integer.parseInt(driver.findElement(By.cssSelector(".slider>.float-right")).getText());
		for (int i = 0; i < beforeStats.size(); i++) {
			total = calculateIncrease(statGain.get(i), Double.parseDouble(beforeStats.get(i)), level);
			total = Math.round(total * 100.0) / 100.0;
			statIncreased = (total + 0.01 == Double.parseDouble(afterStats.get(i))
					|| total - 0.01 == Double.parseDouble(afterStats.get(i))
					|| total == Double.parseDouble(afterStats.get(i)));
			if (statIncreased == false) {
				break;
			}
		}
		Assert.assertEquals(statIncreased, true);
	}

	@DataProvider
	public Object[][] heroData() {
		return new Object[][] { new Object[] { "abathur" }, new Object[] { "jaina" }, new Object[] { "butcher" } };
	}

	@Test(dataProvider = "heroData")
	public void regenIncreaseTest(String hero) {
		ArrayList<WebElement> stats = new ArrayList<WebElement>();
		ArrayList<String> beforeStats = new ArrayList<String>();
		ArrayList<String> afterStats = new ArrayList<String>();
		ArrayList<Double> statGain = new ArrayList<Double>();
		boolean statIncreased = true;
		int level;
		double total;
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href*='" + hero + "']"))).click();
		stats.add(wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".stats.mb10>div:nth-child(2) [data-base]"))));
		for (WebElement stat : stats) {
			beforeStats.add(stat.getText());
		}
		stats.clear();
		action.dragAndDropBy(
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-slider-handle"))), 20, 0)
				.perform();
		stats.add(wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector(".stats.mb10>div:nth-child(2) [data-gain]"))));
		for (WebElement stat : stats) {
			if (!stat.getText().contains("N/A")) {
				afterStats.add(stat.getText());
				statGain.add(Double.parseDouble(stat.getAttribute("data-gain")));
			}
		}
		level = Integer.parseInt(driver.findElement(By.cssSelector(".slider>.float-right")).getText());
		for (int i = 0; i < beforeStats.size(); i++) {
			total = calculateIncrease(statGain.get(i), Double.parseDouble(beforeStats.get(i)), level);
			total = Math.round(total * 100.0) / 100.0;
			statIncreased = (total + 0.01 == Double.parseDouble(afterStats.get(i))
					|| total - 0.01 == Double.parseDouble(afterStats.get(i))
					|| total == Double.parseDouble(afterStats.get(i)));
			if (statIncreased == false) {
				break;
			}
		}
		Assert.assertEquals(statIncreased, true);
	}
}
