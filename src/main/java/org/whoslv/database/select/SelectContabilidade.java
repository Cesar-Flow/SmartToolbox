package org.whoslv.database.select;

import org.whoslv.model.Contabilidade;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectContabilidade {

    public List<Contabilidade> getAll(Connection conn) {
        List<Contabilidade> contabilidades = new ArrayList<>();
        String sql = "SELECT * FROM contabilidade";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Contabilidade contabilidade = new Contabilidade(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email_st"),
                        rs.getString("email_nd"),
                        rs.getString("email_rd")
                );
                contabilidades.add(contabilidade);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contabilidades;
    }

    public Contabilidade getById(Connection conn, int id) {
        String sql = "SELECT * FROM contabilidade WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Contabilidade(
                        rs.getString("nome"),
                        rs.getString("email_st"),
                        rs.getString("email_nd"),
                        rs.getString("email_rd")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
