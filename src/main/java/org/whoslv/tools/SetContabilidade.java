package org.whoslv.tools;

import org.whoslv.database.Connect;

import java.sql.*;
import java.util.Scanner;

public class SetContabilidade {
    public static void main(String[] args) {
        final Scanner K = new Scanner(System.in);

        Connection conn = Connect.getConnection();
        boolean startMenu = true;

        String nome;
        String emailSt;
        String emailNd;
        String emailRd = "";

        while (startMenu) {
            System.out.print(showMenu());
            int choose = K.nextInt();

            if (choose == 1) {
                System.out.print("Digite o ID da contabilidade (0 para todas): ");
                int id = K.nextInt();

                getContabilidade(id);
            } else if (choose == 2) {
                System.out.print("Nome: ");
                nome = K.next();

                System.out.print("E-mail Primário: ");
                emailSt = K.next();

                System.out.print("E-mail Secundário(0 caso não tenha): ");
                emailNd = K.next();

                if (!emailNd.equals("0")) {
                    System.out.println("E-mail Terciário(0 caso não tenha): ");
                    emailRd = K.next();
                }

                if (insertContabilidade(conn, nome, emailSt, emailNd, emailRd)) {
                    System.out.println("Contabilidade cadastrada!");
                } else {
                    System.out.println("Houve um problema ao cadastrar a contabilidade!");
                }
            } else if (choose == 0) {
                startMenu = false;
            }
        }

        System.out.println("Programa Finalizado!");
        K.close();
    }

    public static String showMenu() {
        return """
        CONTABILIDADES
        [ 1 ] Listar Cadastros
        [ 2 ] Incluir Cadastro
        [ 0 ] Sair
        > """;
    }

    public static void getContabilidade(int id) {
        if (id == 0) {
            // todas
        } else {
            // especifico
        }
    }

    public static boolean insertContabilidade(Connection conn, String nome, String emailSt, String emailNd, String emailRd) {
        String sql = "INSERT INTO contabilidade (nome, email_st, email_nd, email_rd) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nome);
            stmt.setString(2, emailSt);
            stmt.setString(3, emailNd.equals("0") ? "" : emailNd);
            stmt.setString(4, emailRd.equals("0") ? "" : emailRd);

            int rowAff = stmt.executeUpdate();

            return rowAff > 0;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.out.println("E-mail principal já cadastrado!");
            } else {
                System.err.println("Erro no banco de dados: " + e.getMessage());
            }

            return false;
        }
    }
}
