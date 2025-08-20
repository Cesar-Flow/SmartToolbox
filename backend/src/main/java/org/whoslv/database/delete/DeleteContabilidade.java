package org.whoslv.database.delete;

import java.sql.*;

public class DeleteContabilidade {

    public boolean delete(Connection conn, int id) {
        String sql = "DELETE FROM contabilidade WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
