package org.whoslv.frontend.tools.XMLGuard;

import java.io.File;
import java.util.*;

/**
 * @param numero número da nota (sem zeros à esquerda)
 */

// Classe auxiliar para armazenar série e número da nota
public record Nota(String serie, int numero) {

    // Função para extrair notas a partir do nome dos arquivos
    public static List<Nota> extrairNotasDoDiretorio(String path) {
        List<Nota> notas = new ArrayList<>();
        File dir = new File(path);

        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("O caminho fornecido não é um diretório válido: " + path);
        }

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            String nomeArquivo = file.getName();
            if (nomeArquivo.startsWith("xmlDfe-")) {
                nomeArquivo = nomeArquivo.replace("xmlDfe-", "");
            }

            // Garantir que tenha ao menos 34 caracteres antes do "-nfe.xml"
            if (nomeArquivo.length() >= 34) {
                String trecho = nomeArquivo.substring(24, 34); // pega [24..33]

                // Série = tudo antes dos últimos 6 dígitos
                String serie = trecho.substring(0, trecho.length() - 6);

                // Número da nota = últimos 6 dígitos (removendo zeros à esquerda)
                String numeroStr = trecho.substring(trecho.length() - 6).replaceFirst("^0+", "");
                if (numeroStr.isEmpty()) numeroStr = "0"; // se for tudo zero

                int numero = Integer.parseInt(numeroStr);

                notas.add(new Nota(serie, numero));
            }
        }
        return notas;
    }

    // Função que detecta quebras e duplicados
    public static void verificarQuebrasEDuplicados(List<Nota> notas, List<String> duplicados, List<String> faltantes) {
        Set<String> vistos = new HashSet<>();

        for (int i = 0; i < notas.size() - 1; i++) {
            Nota atual = notas.get(i);
            Nota proxima = notas.get(i + 1);

            String chave = String.valueOf(atual.numero());

            // Verificar duplicados
            if (!vistos.add(chave)) {
                duplicados.add(chave);
            }

            // Verificar quebras somente dentro da mesma série
            if (atual.serie().equals(proxima.serie())) {
                int diff = proxima.numero() - atual.numero();
                if (diff > 1) {
                    for (int n = atual.numero() + 1; n < proxima.numero(); n++) {
                        faltantes.add(String.valueOf(n));
                    }
                }
            }
        }

        // Último elemento (duplicado caso já tenha aparecido)
        Nota ultimo = notas.getLast();
        String chaveUltima = ultimo.serie() + "-" + ultimo.numero();
        if (!vistos.add(chaveUltima)) {
            duplicados.add(chaveUltima);
        }
    }
}