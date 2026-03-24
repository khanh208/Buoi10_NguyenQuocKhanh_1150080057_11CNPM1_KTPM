package com.lab10.pages;

import com.lab10.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {

    private final By cartItems = By.className("cart_item");
    private final By cartContainer = By.className("cart_list");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartContainer));
        return driver.findElements(cartItems).size();
    }
}