package org.whoslv.database.update;

import org.whoslv.model.Contabilidade;
import java.sql.*;

public class UpdateContabilidade {

    public boolean update(Connection conn, Contabilidade contabilidade, int id) {
        String sql = "UPDATE contabilidade SET nome = ?, email_st = ?, email_nd = ?, email_rd = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contabilidade.getNome());
            stmt.setString(2, contabilidade.getEmailSt());
            stmt.setString(3, contabilidade.getEmailNd());
            stmt.setString(4, contabilidade.getEmailRd());
            stmt.setInt(5, id);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
