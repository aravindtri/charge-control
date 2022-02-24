package com.automate.chargecontrol.service.impl;

import com.automate.chargecontrol.service.ChargerCurrentUpdater;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JuiceProChargerCurrentUpdater implements ChargerCurrentUpdater {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private String remoteChromeUrl;
  private String baseUrl;
  private String userName;
  private String password;
  private boolean isRemoteDriver;

  private static void addInputOnBy(WebDriver driver, By by, String keys, boolean deleteFirst) {
    WebElement emailTxtBox = driver.findElement(by);
    emailTxtBox.click();
    if (deleteFirst) {
      emailTxtBox.sendKeys(Keys.CONTROL + "a");
      emailTxtBox.sendKeys(Keys.DELETE);
    }
    emailTxtBox.sendKeys(keys);
  }

  private static void clickElement(WebDriver driver, By by) {
    driver.findElement(by).click();
  }

  @Override
  public boolean updateChargeCurrent(int ampere) {
    try {
      WebDriver driver = getWebDriver(isRemoteDriver());
      loginToJuicePro(driver);
      selectCharger(driver);
      updateChargeCurrent(ampere, driver);
      logoutOfJuicePro((JavascriptExecutor) driver);
      closeBrowserSession(driver);
      return true;
    } catch (Exception e) {
      LOG.error("Error updating charge current", e);
    }
    return false;
  }

  private void loginToJuicePro(WebDriver driver) {
    LOG.info("Logging in...");
    driver.get(getBaseUrl());
    LOG.info("Title of login page {}", driver.getTitle());

    addInputOnBy(driver, By.id("Email"), getUserName(), false);
    addInputOnBy(driver, By.id("Password"), getPassword(), false);

    clickElement(driver, By.cssSelector("form.form-vertical button[type='submit']"));
    LOG.info("Title after logging in {}", driver.getTitle());
  }

  private void selectCharger(WebDriver driver) {
    LOG.info("Selecting charger...");
    clickElement(driver, By.partialLinkText("MORE DETAILS"));
    LOG.info("Title after selecting charger in {}", driver.getTitle());
  }

  private void updateChargeCurrent(int ampere, WebDriver driver) {
    LOG.info("Updating charge current to {}...", ampere);
    addInputOnBy(driver, By.cssSelector("div#limitBlock input.text-box"), String.valueOf(ampere),
        true);
    clickElement(driver, By.cssSelector("button#buttonAllowedUpdate"));
    LOG.info("Updated charge current to {}...", ampere);
  }

  private void logoutOfJuicePro(JavascriptExecutor driver) {
    LOG.info("Logging out...");
    driver
        .executeScript("javascript:document.getElementById('logoutForm').submit();");
    LOG.info("Logout complete");
  }

  private void closeBrowserSession(WebDriver driver) {
    driver.close();
    driver.quit();
    LOG.info("Browser closed and destroyed");
  }


  private WebDriver getWebDriver(boolean isRemoteDriver) throws MalformedURLException {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    if (isRemoteDriver) {
      ThreadLocal<WebDriver> threadLocal = new ThreadLocal<>();
      threadLocal.set(new RemoteWebDriver(new URL(getRemoteChromeUrl()), options));
      return threadLocal.get();
    } else {
      return new ChromeDriver(options);
    }
  }

  public String getRemoteChromeUrl() {
    return remoteChromeUrl;
  }

  public void setRemoteChromeUrl(String remoteChromeUrl) {
    this.remoteChromeUrl = remoteChromeUrl;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isRemoteDriver() {
    return isRemoteDriver;
  }

  public void setRemoteDriver(boolean remoteDriver) {
    isRemoteDriver = remoteDriver;
  }
}
