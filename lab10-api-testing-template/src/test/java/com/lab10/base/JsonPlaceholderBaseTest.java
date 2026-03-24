package com.lab10.base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;

public class JsonPlaceholderBaseTest extends ApiBaseTest {

    @BeforeClass(alwaysRun = true)
    public void setUpJsonPlaceholderApi() {
        properties = loadProperties();

        String baseUrl = getRequiredProperty("jsonplaceholder.base.url");

        RestAssured.baseURI = baseUrl;

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "jsonplaceholder-tests/1.0")
                .build();
    }
}