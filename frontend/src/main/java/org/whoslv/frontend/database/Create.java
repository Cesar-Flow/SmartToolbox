package org.whoslv.frontend.database;

import java.sql.*;

import static org.whoslv.frontend.database.PasswordUtils.hashPassword;

public final class Create {

    private Create() {}

    public static void createDatabase(Connection conn) throws SQLException {
        if (conn == null || conn.isClosed()) {
            throw new SQLException("Connection inválida ao criar o banco.");
        }
        try (Statement st = conn.createStatement()) {
            // Ativa chaves estrangeiras
            st.executeUpdate("PRAGMA foreign_keys = ON");

            // Cria tabelas
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password_hash TEXT NOT NULL,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """);
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS sessions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    active INTEGER NOT NULL DEFAULT 0,
                    auto_login NOT NULL DEFAULT 0,
                    auto_login_confirm NOT NULL DEFAULT 0,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);
        }

        // Garante usuário padrão
        String defaultUsername = "suporte";
        String plainPassword = "9681";
        String hashedPassword = hashPassword(plainPassword);

        try (PreparedStatement checkUser = conn.prepareStatement("SELECT id FROM users WHERE username = ?")) {
            checkUser.setString(1, defaultUsername);
            ResultSet rs = checkUser.executeQuery();

            if (!rs.next()) {
                // Usuário não existe → cria
                try (PreparedStatement insertUser = conn.prepareStatement(
                        "INSERT INTO users (username, password_hash) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {

                    insertUser.setString(1, defaultUsername);
                    insertUser.setString(2, hashedPassword);
                    insertUser.executeUpdate();

                    // Pega o ID gerado
                    ResultSet generatedKeys = insertUser.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        long userId = generatedKeys.getLong(1);

                        // Cria sessão padrão com active = 0
                        try (PreparedStatement insertSession = conn.prepareStatement(
                                "INSERT INTO sessions (user_id, active) VALUES (?, 0)")) {
                            insertSession.setLong(1, userId);
                            insertSession.executeUpdate();
                        }
                    }
                }
            }
        }
    }
}
