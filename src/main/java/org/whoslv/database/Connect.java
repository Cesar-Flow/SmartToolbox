package org.whoslv.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null) return connection;

        try {
            Dotenv dotenv = Dotenv.load();

            String host = dotenv.get("DB_HOST");
            String port = dotenv.get("DB_PORT");
            String dbName = dotenv.get("DB_NAME");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASS");

            String url = String.format(
                    "jdbc:postgresql://%s:%s/%s?ssl=true&sslmode=require",
                    host, port, dbName
            );

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Conectado ao banco de dados Supabase com sucesso.");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao conectar ao banco de dados: " + e.getMessage());
        }

        return connection;
    }
}
