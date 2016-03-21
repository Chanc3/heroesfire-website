package com.heroesfire.test;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StatsIncrease {

	private String BASE_URL = "http://www.heroesfire.com";

	private WebDriver driver = new FirefoxDriver();

	private WebDriverWait wait = new WebDriverWait(driver, 10);

	private Actions action = new Actions(driver);

	@AfterClass
	public void afterClass() {
	}

	@BeforeClass
	public void beforeClass() {
	}

	@Test
	public void f() {
		ArrayList<WebElement> stats = new ArrayList<WebElement>();
		ArrayList<String> beforeStats = new ArrayList<String>();
		ArrayList<String> afterStats = new ArrayList<String>();
		boolean statIncreased = true;
		driver.get(BASE_URL);
		driver.findElement(By.cssSelector(".self-clear>.megamenu a[href*='talent-calculator']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".choose-hero a[href*='abathur'")))
				.click();
		stats.addAll(wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".stats.mb10"))));
		for (WebElement stat : stats) {
			if (!stat.getText().contains("N/A")) {
				beforeStats.add(stat.getText());
			}
		}
		stats.clear();
		action.dragAndDropBy(
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-slider-handle"))), 20, 0)
				.perform();
		for (WebElement stat : stats) {
			if (!stat.getText().contains("N/A")) {
				afterStats.add(stat.getText());
			}
		}
		for (int i = 0; i < beforeStats.size(); i++) {
			statIncreased = Double.parseDouble(beforeStats.get(i)) < Double.parseDouble(afterStats.get(i));
			if (statIncreased == false) {
				break;
			}
		}
		Assert.assertEquals(statIncreased, true);
	}
}
