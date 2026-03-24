package com.lab10.tests.bai3;

import com.lab10.base.ApiBaseTest;
import com.lab10.models.CreateUserRequest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SchemaValidationTest extends ApiBaseTest {

    @Test(description = "Bài 3 - Schema cho GET /users")
    public void testUserListSchema() {
        given(requestSpec)
                .queryParam("page", 1)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user-list-schema.json"));
    }

    @Test(description = "Bài 3 - Schema cho GET /users/2")
    public void testUserDetailSchema() {
        given(requestSpec)
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test(description = "Bài 3 - Schema cho POST /users")
    public void testCreateUserSchema() {
        CreateUserRequest request = new CreateUserRequest("Khanh", "Backend Intern");

        given(requestSpec)
                .body(request)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/create-user-schema.json"));
    }
}
