package com.lab10.tests.bai2;

import com.lab10.base.ApiBaseTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCrudApiTest extends ApiBaseTest {

    @Test
    public void testCreateUser() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "morpheus");
        body.put("job", "leader");

        given()
                .spec(requestSpec)
                .body(body)
        .when()
                .post("/api/users")
        .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", notNullValue());
    }

    @Test
    public void testUpdateUserWithPut() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "morpheus");
        body.put("job", "zion resident");

        given()
                .spec(requestSpec)
                .body(body)
        .when()
                .put("/api/users/2")
        .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"));
    }

    @Test
    public void testPartialUpdateWithPatch() {
        Map<String, Object> body = new HashMap<>();
        body.put("job", "senior tester");

        given()
                .spec(requestSpec)
                .body(body)
        .when()
                .patch("/api/users/2")
        .then()
                .statusCode(200)
                .body("job", equalTo("senior tester"));
    }

    @Test
    public void testDeleteUser() {
        given()
                .spec(requestSpec)
        .when()
                .delete("/api/users/2")
        .then()
                .statusCode(204);
    }

    @Test
    public void testCreateThenGetConfirmationFlow() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "khanh");
        body.put("job", "tester");

        given()
                .spec(requestSpec)
                .body(body)
        .when()
                .post("/api/users")
        .then()
                .statusCode(201)
                .body("name", equalTo("khanh"))
                .body("job", equalTo("tester"))
                .body("id", notNullValue());
    }
}