package org.whoslv.tools;

import org.whoslv.database.Connect;
import org.whoslv.database.select.SelectContabilidade;
import org.whoslv.database.insert.InsertContabilidade;
import org.whoslv.model.Contabilidade;

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
        String emailRd = "0";

        while (startMenu) {
            System.out.print(showMenu());
            int choose = K.nextInt();
            K.nextLine();

            if (choose == 1) {
                System.out.print("Digite o ID da contabilidade (0 para todas): ");
                int id = K.nextInt();

                SelectContabilidade selectDAO = new SelectContabilidade();
                if (id == 0) {
                    selectDAO.getAll(conn).forEach(c -> System.out.println(c.getId() + " - " + c.getNome() + " - " + c.getEmailSt()));
                } else {
                    Contabilidade contabilidade = selectDAO.getById(conn, id);
                    if (contabilidade != null) {
                        System.out.println(contabilidade.getNome() + " - " + contabilidade.getEmailSt());
                    } else {
                        System.out.println("Contabilidade não encontrada!");
                    }
                }
            } else if (choose == 2) {
                System.out.print("Nome: ");
                nome = K.nextLine();

                System.out.print("E-mail Primário: ");
                emailSt = K.nextLine();

                System.out.print("E-mail Secundário(0 caso não tenha): ");
                emailNd = K.nextLine();

                if (!emailNd.equals("0")) {
                    System.out.print("E-mail Terciário(0 caso não tenha): ");
                    emailRd = K.nextLine();
                }

                // Criar o objeto Contabilidade
                Contabilidade contabilidade = new Contabilidade(nome, emailSt, emailNd, emailRd);

                // Chamar a classe de inserção
                InsertContabilidade insertDAO = new InsertContabilidade();
                if (insertDAO.insert(conn, contabilidade)) {
                    System.out.println("Contabilidade cadastrada com sucesso!");
                } else {
                    System.out.println("Erro ao cadastrar contabilidade!");
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
}
