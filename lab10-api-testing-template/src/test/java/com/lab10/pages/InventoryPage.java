package com.lab10.pages;

import com.lab10.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
        // Chuyển tên sản phẩm thành format data-test attribute
        // Ví dụ: "Sauce Labs Backpack" -> "sauce-labs-backpack"
        String itemId = productName.toLowerCase().replace(" ", "-");
        By addButton = By.cssSelector("[data-test='add-to-cart-" + itemId + "']");
        waitAndClick(addButton);
        // Chờ badge cập nhật
        wait.until(ExpectedConditions.presenceOfElementLocated(cartBadge));
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
        driver.findElement(By.className("shopping_cart_link")).click();
        return new CartPage(driver);
    }
}
