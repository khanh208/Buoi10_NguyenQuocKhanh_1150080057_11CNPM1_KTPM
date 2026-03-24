package com.lab10.pages;

import com.lab10.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class InventoryPage extends BasePage {
    private final By inventoryItems = By.cssSelector(".inventory_item");
    private final By addToCartButtons = By.cssSelector("button.btn_inventory");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");
    private final By cartLink = By.cssSelector(".shopping_cart_link");
    private final By appLogo = By.cssSelector(".app_logo");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        waitForPageLoad();
        return isElementVisible(appLogo);
    }

    public InventoryPage addFirstItemToCart() {
        waitAndClick(addToCartButtons);
        return this;
    }

    public InventoryPage addItemByName(String productName) {
        By productButton = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button");
        waitAndClick(productButton);
        return this;
    }

    public int getCartItemCount() {
        if (!isElementVisible(cartBadge)) {
            return 0;
        }
        return Integer.parseInt(getText(cartBadge));
    }

    public int getInventoryCount() {
        return driver.findElements(inventoryItems).size();
    }

    public CartPage goToCart() {
        waitAndClick(cartLink);
        return new CartPage(driver);
    }
}
