package com.lab10.tests.bai6;

import com.lab10.base.BaseTest;
import com.lab10.pages.CartPage;
import com.lab10.pages.InventoryPage;
import com.lab10.pages.LoginPage;
import com.lab10.utils.ConfigReader;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ApiUiIntegrationTest extends BaseTest {
    private boolean isApiLoginReady;
    private boolean isApiAlive;
    private String apiToken;

    @BeforeMethod(alwaysRun = true)
    public void prepareByApi() {
        try {
            apiToken = RestAssured.given()
                    .baseUri(ConfigReader.get("reqres.baseUri"))
                    .basePath(ConfigReader.get("reqres.basePath"))
                    .contentType("application/json")
                    .body("{\"email\":\"eve.holt@reqres.in\",\"password\":\"cityslicka\"}")
                    .when()
                    .post("/api/login")
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getString("token");

            isApiLoginReady = apiToken != null && !apiToken.isBlank();
            System.out.println("[API PRECONDITION] Token = " + apiToken);

            int status = RestAssured.given()
                    .baseUri(ConfigReader.get("reqres.baseUri"))
                    .basePath(ConfigReader.get("reqres.basePath"))
                    .when()
                    .get("/api/users")
                    .then()
                    .extract()
                    .statusCode();

            isApiAlive = status == 200;
        } catch (Exception e) {
            isApiLoginReady = false;
            isApiAlive = false;
            System.err.println("[API PRECONDITION] Failed: " + e.getMessage());
        }
    }

    @Test(description = "Bài 6A - API precondition pass thì mới login UI và verify inventory")
    public void testLoginUiAfterApiPrecondition() {
        if (!isApiLoginReady) {
            throw new SkipException("Bỏ qua test UI vì API login precondition thất bại");
        }

        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login(
                ConfigReader.get("ui.username"),
                ConfigReader.get("ui.password"));

        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"),
                "URL sau login phải chứa inventory");
        Assert.assertEquals(getDriver().getTitle(), "Swag Labs");
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory phải hiển thị");
    }

    @Test(description = "Bài 6B - API health check ảnh hưởng tới UI flow đầy đủ")
    public void testFullUiFlowWhenApiAlive() {
        if (!isApiAlive) {
            throw new SkipException("API reqres.in đang không sẵn sàng, skip UI flow");
        }

        // Bước 1 - UI action: login vào SauceDemo
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login(
                ConfigReader.get("ui.username"),
                ConfigReader.get("ui.password"));

        // Bước 2 - UI action: thêm 2 sản phẩm
        inventoryPage.addItemByName("Sauce Labs Backpack");
        inventoryPage.addItemByName("Sauce Labs Bike Light");

        // Bước 3 - Assertion UI: badge phải bằng 2
        Assert.assertEquals(inventoryPage.getCartItemCount(), 2, "Badge giỏ hàng phải bằng 2");

        // Bước 4 - UI action: vào giỏ hàng
        CartPage cartPage = inventoryPage.goToCart();

        // Bước 5 - Assertion cuối: giỏ có đúng 2 sản phẩm
        Assert.assertEquals(cartPage.getItemCount(), 2, "Giỏ hàng phải có đúng 2 sản phẩm");
    }
}
