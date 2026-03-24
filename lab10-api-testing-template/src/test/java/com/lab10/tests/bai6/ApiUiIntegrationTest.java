package com.lab10.tests.bai6;

import com.lab10.base.BaseTest;
import com.lab10.pages.CartPage;
import com.lab10.pages.InventoryPage;
import com.lab10.pages.LoginPage;
import com.lab10.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ApiUiIntegrationTest extends BaseTest {
    private boolean isApiLoginReady;
    private boolean isApiAlive;
    private String apiToken;

    private void pauseForDemo(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void prepareByApi() {
        isApiLoginReady = false;
        isApiAlive = false;
        apiToken = null;

        try {
            // API precondition: login để lấy token
            Response loginResponse = RestAssured.given()
                    .baseUri(ConfigReader.get("base.url"))
                    .contentType("application/json")
                    .header("x-api-key", ConfigReader.get("reqres.api.key"))
                    .header("User-Agent", "reqres-qa-tests/1.0")
                    .body("{\"email\":\"eve.holt@reqres.in\",\"password\":\"cityslicka\"}")
                    .when()
                    .post("/api/login");

            int loginStatus = loginResponse.getStatusCode();
            System.out.println("[API PRECONDITION] Login status = " + loginStatus);

            if (loginStatus == 200) {
                apiToken = loginResponse.jsonPath().getString("token");
                isApiLoginReady = apiToken != null && !apiToken.isBlank();
                System.out.println("[API PRECONDITION] Token = " + apiToken);
            } else {
                System.out.println("[API PRECONDITION] Login failed, response = "
                        + loginResponse.getBody().asString());
            }

            // API health check: kiểm tra reqres còn sống
            Response healthResponse = RestAssured.given()
                    .baseUri(ConfigReader.get("base.url"))
                    .header("x-api-key", ConfigReader.get("reqres.api.key"))
                    .header("User-Agent", "reqres-qa-tests/1.0")
                    .when()
                    .get("/api/users?page=2");

            int healthStatus = healthResponse.getStatusCode();
            isApiAlive = (healthStatus == 200);
            System.out.println("[API HEALTH] Status = " + healthStatus);

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

        // UI action: đăng nhập vào SauceDemo bằng form
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login(
                ConfigReader.get("ui.username"),
                ConfigReader.get("ui.password"));

        // Assertion: xác minh URL, title và trang inventory
        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"),
                "URL sau login phải chứa inventory");
        Assert.assertEquals(getDriver().getTitle(), "Swag Labs");
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory phải hiển thị");

        pauseForDemo(5000);
    }

    @Test(description = "Bài 6B - API health check ảnh hưởng tới UI flow đầy đủ")
    public void testFullUiFlowWhenApiAlive() {
        if (!isApiAlive) {
            throw new SkipException("API reqres.in đang không sẵn sàng, skip UI flow");
        }

        // UI action: login
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login(
                ConfigReader.get("ui.username"),
                ConfigReader.get("ui.password"));

        // UI action: thêm 2 sản phẩm
        inventoryPage.addItemByName("Sauce Labs Backpack");
        inventoryPage.addItemByName("Sauce Labs Bike Light");

        pauseForDemo(3000);

        // Assertion: badge giỏ hàng phải bằng 2
        Assert.assertEquals(inventoryPage.getCartItemCount(), 2,
                "Badge giỏ hàng phải bằng 2");

        // UI action: vào giỏ hàng
        CartPage cartPage = inventoryPage.goToCart();

        pauseForDemo(5000);

        // Assertion cuối: giỏ có đúng 2 sản phẩm
        Assert.assertEquals(cartPage.getItemCount(), 2,
                "Giỏ hàng phải có đúng 2 sản phẩm");
    }
}