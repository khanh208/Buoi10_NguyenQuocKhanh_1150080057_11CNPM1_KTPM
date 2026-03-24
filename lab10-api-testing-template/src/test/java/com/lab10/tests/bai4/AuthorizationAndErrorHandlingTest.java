package com.lab10.tests.bai4;

import com.lab10.base.ApiBaseTest;
import com.lab10.models.LoginRequest;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthorizationAndErrorHandlingTest extends ApiBaseTest {

    @Test(description = "Bài 4 - Login thành công")
    public void testLoginReturnsToken() {
        String token = given(requestSpec)
                .body(new LoginRequest("eve.holt@reqres.in", "cityslicka"))
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("token", not(emptyString()))
                .extract()
                .jsonPath()
                .getString("token");

        Assert.assertFalse(token.isEmpty(), "Token không được rỗng");
    }

    @Test(description = "Bài 4 - Login thiếu password")
    public void testLoginMissingPassword() {
        given(requestSpec)
                .body(new LoginRequest("eve.holt@reqres.in", null))
                .when()
                .post("/api/login")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test(description = "Bài 4 - Login thiếu email")
    public void testLoginMissingEmail() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("password", "secret");

        given(requestSpec)
                .body(body)
                .when()
                .post("/api/login")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing email or username"));
    }

    @Test(description = "Bài 4 - Register thành công")
    public void testRegisterSuccess() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");

        given(requestSpec)
                .body(body)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test(description = "Bài 4 - Register thiếu password")
    public void testRegisterMissingPassword() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("email", "sydney@fife");

        given(requestSpec)
                .body(body)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @DataProvider(name = "loginScenarios")
    public Object[][] loginScenarios() {
        return new Object[][]{
                {"eve.holt@reqres.in", "cityslicka", 200, null},
                {"eve.holt@reqres.in", "", 400, "Missing password"},
                {"", "cityslicka", 400, "Missing email or username"},
                {"notexist@reqres.in", "wrongpass", 400, "user not found"},
                {"invalid-email", "pass123", 400, "user not found"}
        };
    }

    @Test(dataProvider = "loginScenarios", description = "Bài 4 - Data Driven login scenarios")
    public void testLoginScenarios(String email, String password, int expectedStatus, String expectedError) {
        Map<String, String> body = new LinkedHashMap<>();
        if (email != null && !email.isBlank()) {
            body.put("email", email);
        }
        if (password != null && !password.isBlank()) {
            body.put("password", password);
        }

        ValidatableResponse response = given(requestSpec)
                .body(body)
                .when()
                .post("/api/login")
                .then()
                .statusCode(expectedStatus);

        if (expectedError != null) {
            response.body("error", containsString(expectedError));
        } else {
            response.body("token", not(emptyString()));
        }
    }
}
