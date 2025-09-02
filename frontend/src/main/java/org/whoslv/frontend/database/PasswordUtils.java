package org.whoslv.frontend.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    // Gera hash SHA-256 da senha
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Verifica se a senha digitada bate com o hash do banco
    public static boolean checkPassword(String text, String hashBanco) {
        if (text == null || hashBanco == null) return false;

        String hashDigitado = hashPassword(text);
        return hashDigitado.equalsIgnoreCase(hashBanco);
    }
}

