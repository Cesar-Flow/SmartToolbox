package org.whoslv.tools.XMLProcessor;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlNotaExtractor {
    private static final ArrayList<File> arquivos = new ArrayList<>();
    private static final HashMap<String, String> blacklist = new HashMap<>();
    private static final List<NotasDoZip> notas = new ArrayList<>();

    public void addZip(File arquivoZip) {
        arquivos.add(arquivoZip);
    }

    public List<NotasDoZip> getNotas() {
        return notas;
    }

    // Primeiro passo: Identificar notas canceladas e adicionar a blacklist junto com a chave de origem
    public void setBlacklist() throws IOException {
        for (File currentZip : arquivos) {
            // Abre o ZIP com charset configurado (evita problema com acentos)
            try (ZipFile arquivoZip = new ZipFile(currentZip)) {
                arquivoZip.setCharset(StandardCharsets.UTF_8);

                // Lista todos os arquivos do ZIP
                List<FileHeader> entries = arquivoZip.getFileHeaders();

                for (FileHeader entry : entries) {
                    // Buscar por tudo que seja .XML
                    if (!entry.isDirectory() && entry.getFileName().toLowerCase().endsWith(".xml")) {
                        try (InputStream is = arquivoZip.getInputStream(entry)) {
                            String content = new String(is.readAllBytes());

                            if (!content.contains("<mod>")) {
                                Pattern pattern = Pattern.compile("<chNFe>(.*?)</chNFe>");
                                Matcher matcher = pattern.matcher(content);

                                if (matcher.find()) {
                                    String chNFe = matcher.group(1);
                                    String fileIndex = Paths.get(entry.getFileName()).getFileName().toString();
                                    blacklist.put(fileIndex, chNFe);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void extrairNotas() throws IOException {
        notas.clear(); // limpa a lista antiga
        this.setBlacklist();

        for (File currentZip : arquivos) {
            NotasDoZip zipAtual = new NotasDoZip(currentZip.getName());

            try (ZipFile arquivoZip = new ZipFile(currentZip)) {
                arquivoZip.setCharset(StandardCharsets.UTF_8);

                for (FileHeader entry : arquivoZip.getFileHeaders()) {
                    if (entry.isDirectory() || !entry.getFileName().toLowerCase().endsWith(".xml")) continue;

                    String content;
                    try (InputStream is = arquivoZip.getInputStream(entry)) {
                        content = new String(is.readAllBytes());
                    }

                    HashMap<String, String> dadosTemp = new HashMap<>();
                    String nomeArquivoOriginal = Paths.get(entry.getFileName()).getFileName().toString();
                    String nomeArquivoLimpo = nomeArquivoOriginal;

                    dadosTemp.put("nome_arquivo", nomeArquivoOriginal);

                    // 1️⃣ Verificação inicial de cancelamento usando o nome do arquivo
                    if (nomeArquivoLimpo.startsWith("110111")) {
                        nomeArquivoLimpo = XmlUtils.limparNomeArquivoParaBlacklist(nomeArquivoLimpo);
                    }

                    boolean cancelada = blacklist.containsValue(nomeArquivoLimpo);

                    dadosTemp.put("cancelada", cancelada ? "sim" : "não");

                    // 2️⃣ Extrai as tags mod, chNFe e vNF
                    String[] tags = {"mod", "chNFe", "vNF"};
                    for (String tag : tags) {
                        String valor = XmlUtils.extrairTag(content, tag);
                        if (valor != null) {
                            dadosTemp.put(tag, valor);

                            // Se a tag chNFe estiver presente, refina a verificação de cancelada
                            if (tag.equals("chNFe")) {
                                cancelada = cancelada || blacklist.containsValue(valor);
                                dadosTemp.put("cancelada", cancelada ? "sim" : "não");
                            }
                        }
                    }

                    // 3️⃣ Se chNFe não foi encontrada nas tags, pega pelo nome do arquivo
                    if (!dadosTemp.containsKey("chNFe")) {
                        String chave = XmlUtils.extrairChaveDoArquivo(nomeArquivoOriginal).replace("-nfe", "");
                        dadosTemp.put("chNFe", chave);

                        // Atualiza novamente a verificação de cancelada
                        cancelada = cancelada || blacklist.containsValue(chave);
                        dadosTemp.put("cancelada", cancelada ? "sim" : "não");
                    }

                    zipAtual.addNota(dadosTemp); // adiciona a nota ao ZIP atual
                }
            }

            notas.add(zipAtual); // adiciona o ZIP à lista geral
        }
    }


}
