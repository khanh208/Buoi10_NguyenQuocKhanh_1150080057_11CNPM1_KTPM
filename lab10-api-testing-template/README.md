# Lab 10 - API Testing with REST Assured + UI/API Integration

Repo này là template dùng chung cho toàn bộ 7 bài của Lab 10.

## Công nghệ
- Java 17
- Maven
- TestNG
- REST Assured
- Jackson
- Selenium
- Allure Report
- PostgreSQL (khung mở rộng để lưu test execution log)

## Cấu trúc chính
- `src/test/java/com/lab10/base`: Base cho API/UI
- `src/test/java/com/lab10/models`: POJO request/response
- `src/test/java/com/lab10/tests/bai1..bai7`: từng bài theo lab
- `src/test/resources/schemas`: JSON schema
- `src/test/resources/config`: cấu hình môi trường

## Cách chạy
```bash
mvn clean test
```

Chạy report Allure:
```bash
mvn allure:report
mvn allure:serve
```

## PostgreSQL
Bài lab không bắt buộc thao tác DB trực tiếp vì dùng public API. Tuy nhiên repo này có thêm:
- `DbConfig`
- `PostgresResultLogger`

Mục đích: nếu muốn, có thể lưu lại lịch sử chạy test vào PostgreSQL để phục vụ báo cáo / dashboard nội bộ.

## Gợi ý đặt tên repo
`lab10-api-testing-template`


## Cấu trúc chuẩn
- `src/main/java`: chứa base class, models, pages, utils dùng chung và `App.java`
- `src/test/java`: chứa toàn bộ test case theo từng bài
- `src/test/resources`: config và JSON schema

> Lưu ý: Đây là repo kiểm thử, nên file `main` chỉ đóng vai trò điểm vào minh họa. Khi chấm bài, phần được chạy thực tế là các class trong `src/test/java` thông qua TestNG.
