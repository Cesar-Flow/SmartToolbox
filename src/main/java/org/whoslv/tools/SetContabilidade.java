package org.whoslv.tools;

import org.whoslv.database.Connect;

import java.sql.Connection;
import java.util.Scanner;

public class SetContabilidade {
    public static void main(String[] args) {
        final Scanner K = new Scanner(System.in);

        Connection conn = Connect.getConnection();
        boolean startMenu = true;

        String nome;
        String emailSt;
        String emailNd;
        String emailRd = null;

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

                System.out.println("Contabilidade cadastrada");
                System.out.printf("%s %s %s %s", nome, emailSt, emailNd, emailRd);
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
}
