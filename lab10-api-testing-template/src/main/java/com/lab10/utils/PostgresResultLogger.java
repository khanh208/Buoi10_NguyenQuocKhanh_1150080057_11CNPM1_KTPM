package com.lab10.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

/**
 * Lớp tùy chọn để lưu log chạy test vào PostgreSQL.
 * Không bắt buộc cho bài lab, nhưng hữu ích nếu giảng viên muốn chứng minh
 * repo có thể mở rộng dùng DB PostgreSQL.
 */
public final class PostgresResultLogger {
    private PostgresResultLogger() {
    }

    public static void saveResult(String suiteName, String testName, String status, long durationMs) {
        if (!DbConfig.isEnabled()) {
            return;
        }

        String sql = """
                insert into api_test_execution_log (suite_name, test_name, status, duration_ms, executed_at)
                values (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DriverManager.getConnection(
                DbConfig.getUrl(),
                DbConfig.getUsername(),
                DbConfig.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, suiteName);
            preparedStatement.setString(2, testName);
            preparedStatement.setString(3, status);
            preparedStatement.setLong(4, durationMs);
            preparedStatement.setObject(5, LocalDateTime.now());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("[DB-LOGGER] Không thể lưu kết quả test vào PostgreSQL: " + e.getMessage());
        }
    }
}
