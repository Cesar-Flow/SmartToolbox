package org.whoslv.frontend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexão singleton para SQLite.
 * Garante uma única Connection reutilizável durante todo o ciclo de vida do app.
 */
public final class Connect {

    private static final String DB_URL = "jdbc:sqlite:localdata.db";
    private static volatile Connection conn;

    private Connect() {}

    /** Obtém uma conexão válida. Reabre se estiver fechada. */
    public static synchronized Connection getConn() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DB_URL);
            // Inicialização de schema sem fechar a conexão
            Create.createDatabase(conn);
        }
        return conn;
    }

    /** Fecha a conexão global (use no Application.stop()). */
    public static synchronized void close() {
        if (conn != null) {
            try {
                if (!conn.isClosed()) conn.close();
            } catch (SQLException ignored) {
            } finally {
                conn = null;
            }
        }
    }
}
