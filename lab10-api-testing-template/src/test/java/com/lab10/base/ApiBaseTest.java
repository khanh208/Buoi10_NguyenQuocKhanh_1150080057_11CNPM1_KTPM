package com.lab10.base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiBaseTest {

    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;
    protected Properties properties;

    @BeforeClass(alwaysRun = true)
    public void setUpApi() {
        properties = loadProperties();

        String baseUrl = getRequiredProperty("base.url");
        String apiKey = getOptionalProperty("reqres.api.key");
        String userAgent = getOptionalProperty("user.agent");

        if (userAgent.isEmpty()) {
            userAgent = "reqres-qa-tests/1.0";
        }

        RestAssured.baseURI = baseUrl;

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", userAgent);

        if (!apiKey.isEmpty()) {
            builder.addHeader("x-api-key", apiKey);
        }

        requestSpec = builder.build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }

    protected Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config/config.properties")) {

            if (input == null) {
                throw new RuntimeException("Khong tim thay file src/test/resources/config/config.properties");
            }

            props.load(input);
            return props;
        } catch (IOException e) {
            throw new RuntimeException("Khong doc duoc file config.properties", e);
        }
    }

    protected String getRequiredProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Thieu cau hinh bat buoc trong config.properties: " + key);
        }
        return value.trim();
    }

    protected String getOptionalProperty(String key) {
        String value = properties.getProperty(key);
        return value == null ? "" : value.trim();
    }
}