package org.whoslv.database.insert;

import org.whoslv.model.Contabilidade;
import java.sql.*;

public class InsertContabilidade {

    public boolean insert(Connection conn, Contabilidade contabilidade) {
        String sql = "INSERT INTO contabilidade (nome, email_st, email_nd, email_rd) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, contabilidade.getNome());
            stmt.setString(2, contabilidade.getEmailSt());
            stmt.setString(3, contabilidade.getEmailNd().equals("0") ? null : contabilidade.getEmailNd());
            stmt.setString(4, contabilidade.getEmailRd().equals("0") ? null : contabilidade.getEmailRd());

            int rowAff = stmt.executeUpdate();
            return rowAff > 0;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.out.println("E-mail principal já cadastrado!");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
}
