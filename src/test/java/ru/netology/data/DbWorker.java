package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

public class DbWorker {
    private DbWorker() {
    }

    static String url = System.getProperty("database.url");
    static String user = System.getProperty("database.username");
    static String password = System.getProperty("database.password");

    @SneakyThrows
    public static void cleanTables() {
        var clean1SQL = "DELETE FROM credit_request_entity";
        var clean2SQL = "DELETE FROM order_entity";
        var clean3SQL = "DELETE FROM payment_entity";
        var runner = new QueryRunner();
        try (
                var conn = DriverManager.getConnection(
                        url, user, password
                )
        ) {
            runner.update(conn, clean1SQL);
            runner.update(conn, clean2SQL);
            runner.update(conn, clean3SQL);
        }
    }

    @SneakyThrows
    public static String getAmount() {
        var amountSQL = "SELECT amount FROM payment_entity;";
        var runner = new QueryRunner();
        try (
                var conn = DriverManager.getConnection(
                        url, user, password
                )
        ) {
            var amount = runner.query(conn, amountSQL, new ScalarHandler<>());
            return String.valueOf(amount);
        }
    }

    @SneakyThrows
    public static Object getPaymentStatus() {
        var statusSQL = "SELECT status FROM payment_entity WHERE created=(SELECT MAX(created) FROM payment_entity);";
        var runner = new QueryRunner();
        try (
                var conn = DriverManager.getConnection(
                        url, user, password
                )
        ) {
            var status = runner.query(conn, statusSQL, new ScalarHandler<>());
            return status;
        }
    }

    @SneakyThrows
    public static String getCreditStatus() {
        var statusSQL = "SELECT status FROM credit_request_entity WHERE created = (SELECT max(created) FROM credit_request_entity);";
        var runner = new QueryRunner();
        try (
                var conn = DriverManager.getConnection(
                        url, user, password
                )
        ) {
            var status = runner.query(conn, statusSQL, new ScalarHandler<>());
            return String.valueOf(status);
        }
    }
}
