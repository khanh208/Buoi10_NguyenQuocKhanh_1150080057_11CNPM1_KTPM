package com.lab10;

/**
 * Entry point để repo có file main rõ ràng.
 * Lab 10 chủ yếu chạy bằng TestNG/Maven, không chạy business app độc lập.
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Lab10 API Testing Template - Java + TestNG + REST Assured + PostgreSQL");
        System.out.println("Chạy test bằng lệnh: mvn clean test -Dsurefire.suiteXmlFiles=testng-lab10.xml");
    }
}
