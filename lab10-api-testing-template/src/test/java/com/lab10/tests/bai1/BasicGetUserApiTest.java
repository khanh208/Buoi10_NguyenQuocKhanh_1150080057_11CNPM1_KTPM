package com.lab10.tests.bai1;

import com.lab10.base.ApiBaseTest;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BasicGetUserApiTest extends ApiBaseTest {

    @Test
    public void testGetUsersPage1() {
        given()
                .spec(requestSpec)
        .when()
                .get("/api/users?page=1")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("page", equalTo(1))
                .body("data", notNullValue());
    }

    @Test
    public void testGetUsersPage2() {
        given()
                .spec(requestSpec)
        .when()
                .get("/api/users?page=2")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("page", equalTo(2))
                .body("data", notNullValue());
    }

    @Test
    public void testGetSingleUser() {
        given()
                .spec(requestSpec)
        .when()
                .get("/api/users/2")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("data.id", equalTo(2));
    }

    @Test
    public void testUserNotFound() {
        given()
                .spec(requestSpec)
        .when()
                .get("/api/users/23")
        .then()
                .statusCode(404);
    }
}