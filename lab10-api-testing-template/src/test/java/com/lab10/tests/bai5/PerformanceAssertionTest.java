package com.lab10.tests.bai5;

import com.lab10.base.ApiBaseTest;
import com.lab10.models.CreateUserRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.LongSummaryStatistics;
import java.util.stream.LongStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PerformanceAssertionTest extends ApiBaseTest {

    @DataProvider(name = "slaScenarios")
    public Object[][] slaScenarios() {
        return new Object[][]{
                {"GET", "/api/users", 2000L, 200},
                {"GET", "/api/users/2", 1500L, 200},
                {"POST", "/api/users", 3000L, 201},
                {"POST", "/api/login", 2000L, 200},
                {"DELETE", "/api/users/2", 1000L, 204}
        };
    }

    @Step("Gọi {method} {endpoint} - SLA: {maxMs}ms")
    private Response executeRequest(String method, String endpoint, long maxMs) {
        switch (method) {
            case "GET":
                return given(requestSpec).when().get(endpoint);
            case "POST":
                if (endpoint.equals("/api/users")) {
                    return given(requestSpec).body(new CreateUserRequest("Perf User", "QA")).when().post(endpoint);
                }
                return given(requestSpec).body("{\"email\":\"eve.holt@reqres.in\",\"password\":\"cityslicka\"}").when().post(endpoint);
            case "DELETE":
                return given(requestSpec).when().delete(endpoint);
            default:
                throw new IllegalArgumentException("Unsupported method: " + method);
        }
    }

    @Test(dataProvider = "slaScenarios", description = "Bài 5 - SLA cho 5 endpoint")
    public void testSlaForEndpoints(String method, String endpoint, long maxMs, int expectedStatus) {
        long start = System.currentTimeMillis();
        Response response = executeRequest(method, endpoint, maxMs);
        long elapsed = System.currentTimeMillis() - start;

        System.out.printf("[SLA] %s %s -> %d ms%n", method, endpoint, elapsed);

        response.then()
                .statusCode(expectedStatus)
                .time(lessThan(maxMs));

        if (method.equals("GET") && endpoint.equals("/api/users")) {
            response.then().body("data.size()", greaterThanOrEqualTo(1));
        }
        if (method.equals("GET") && endpoint.equals("/api/users/2")) {
            response.then().body("data.id", equalTo(2));
        }
        if (method.equals("POST") && endpoint.equals("/api/users")) {
            response.then().body("id", notNullValue());
        }
        if (method.equals("POST") && endpoint.equals("/api/login")) {
            response.then().body("token", notNullValue());
        }
    }

    @Test(description = "Bài 5 - Chạy 10 lần và tính average/min/max")
    public void testSimpleMonitoringForTenRuns() {
        long[] responseTimes = LongStream.range(0, 10)
                .map(index -> {
                    long start = System.currentTimeMillis();
                    given(requestSpec).when().get("/api/users").then().statusCode(200);
                    return System.currentTimeMillis() - start;
                })
                .toArray();

        LongSummaryStatistics stats = LongStream.of(responseTimes).summaryStatistics();

        System.out.println("[MONITOR] Average: " + stats.getAverage() + " ms");
        System.out.println("[MONITOR] Min: " + stats.getMin() + " ms");
        System.out.println("[MONITOR] Max: " + stats.getMax() + " ms");

        Assert.assertTrue(stats.getAverage() < 3000, "Average response time vượt ngưỡng 3000ms");
    }
}
