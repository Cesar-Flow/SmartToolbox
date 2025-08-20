package org.whoslv.tools.XMLProcessor;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlUtils {

    // Extrai o valor de uma tag simples: <tag>valor</tag>
    public static String extrairTag(String xml, String tag) {
        Pattern pattern = Pattern.compile(String.format("<%s>(.*?)</%s>", tag, tag));
        Matcher matcher = pattern.matcher(xml);
        return matcher.find() ? matcher.group(1) : null;
    }

    // Extrai o atributo URI da tag Reference (removendo #NFe)
    public static String extrairChaveDoArquivo(String nomeArquivo) {
        // Remove o caminho, pega apenas o nome do arquivo
        String arquivo = Paths.get(nomeArquivo).getFileName().toString();

        // Remove a extensão .xml
        if (arquivo.toLowerCase().endsWith(".xml")) {
            arquivo = arquivo.substring(0, arquivo.length() - 4);
        }

        if (arquivo.startsWith("110111")) {
            // Remove o prefixo "110111"
            String resto = arquivo.substring(6);
            // Remove os últimos dois dígitos
            if (resto.length() > 2) {
                resto = resto.substring(0, resto.length() - 2);
            }
            return resto;
        } else {
            // Para qualquer outro arquivo, retorna tudo antes do .xml (já removido acima)
            return arquivo;
        }
    }

    // Verifica se um valor/chave existe no hashmap
    public static boolean estaNaBlacklist(HashMap<String, String> blacklist, String chave) {
        return chave != null && (blacklist.containsKey(chave) || blacklist.containsValue(chave));
    }

    public static String limparNomeArquivoParaBlacklist(String nomeArquivo) {
        // Remove prefixo
        String resultado = nomeArquivo.startsWith("110111") ? nomeArquivo.substring(6) : nomeArquivo;

        // Remove sufixo
        if (resultado.endsWith("-procEventoNFe-nfe.xml")) {
            resultado = resultado.substring(0, resultado.length() - "-procEventoNFe-nfe.xml".length());
        } else if (resultado.endsWith("-procEventoNFe.xml")) {
            resultado = resultado.substring(0, resultado.length() - "-procEventoNFe.xml".length());
        } else if (resultado.endsWith(".xml")) {
            resultado = resultado.substring(0, resultado.length() - 4); // remove só .xml
        }

        // Remove os últimos 2 dígitos
        if (resultado.length() > 2) {
            resultado = resultado.substring(0, resultado.length() - 2);
        }

        return resultado;
    }

}
