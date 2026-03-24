package com.lab10.listeners;

import com.lab10.utils.PostgresResultLogger;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestResultListener implements ITestListener {
    @Override
    public void onTestSuccess(ITestResult result) {
        PostgresResultLogger.saveResult(
                result.getTestClass().getName(),
                result.getMethod().getMethodName(),
                "PASS",
                result.getEndMillis() - result.getStartMillis()
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {
        PostgresResultLogger.saveResult(
                result.getTestClass().getName(),
                result.getMethod().getMethodName(),
                "FAIL",
                result.getEndMillis() - result.getStartMillis()
        );
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        PostgresResultLogger.saveResult(
                result.getTestClass().getName(),
                result.getMethod().getMethodName(),
                "SKIP",
                result.getEndMillis() - result.getStartMillis()
        );
    }
}
