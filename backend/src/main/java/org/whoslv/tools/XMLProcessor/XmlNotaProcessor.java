package org.whoslv.tools.XMLProcessor;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class XmlNotaProcessor {

    private final List<NotasDoZip> notasPorZip;

    public XmlNotaProcessor(List<NotasDoZip> notasPorZip) {
        this.notasPorZip = notasPorZip;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        for (NotasDoZip zip : notasPorZip) {
            double totalNFe = 0;
            double totalNFCe = 0;
            double totalNFeCanceladas = 0;
            double totalNFCeCanceladas = 0;

            int qtdNFe = 0;
            int qtdNFCe = 0;
            int qtdNFeCanceladas = 0;
            int qtdNFCeCanceladas = 0;

            for (HashMap<String, String> nota : zip.getNotas()) {
                String mod = nota.get("mod");
                String cancelada = nota.get("cancelada");
                String valorStr = nota.get("vNF");
                double valor = 0;

                if (valorStr != null) {
                    try {
                        valor = Double.parseDouble(valorStr.replace(",", "."));
                    } catch (NumberFormatException e) {
                        valor = 0;
                    }
                }

                if ("55".equals(mod)) { // NFe
                    if ("sim".equalsIgnoreCase(cancelada)) {
                        totalNFeCanceladas += valor;
                        qtdNFeCanceladas++;
                    } else {
                        totalNFe += valor;
                        qtdNFe++;
                    }
                } else if ("65".equals(mod)) { // NFCe
                    if ("sim".equalsIgnoreCase(cancelada)) {
                        totalNFCeCanceladas += valor;
                        qtdNFCeCanceladas++;
                    } else {
                        totalNFCe += valor;
                        qtdNFCe++;
                    }
                }
            }

            sb.append(String.format("Abrindo ZIP: %s%n", zip.getNomeZip()));
            sb.append(String.format("Totais do arquivo ZIP %s%n", zip.getNomeZip()));
            sb.append(String.format("NFe=%s - %d qtdNotas%n", nf.format(totalNFe), qtdNFe));
            sb.append(String.format("NFCe=%s - %d qtdNotas%n", nf.format(totalNFCe), qtdNFCe));
            sb.append(String.format("NFe Canceladas=%s - %d qtdNotas%n", nf.format(totalNFeCanceladas), qtdNFeCanceladas));
            sb.append(String.format("NFCe Canceladas=%s - %d qtdNotas%n%n", nf.format(totalNFCeCanceladas), qtdNFCeCanceladas));
        }

        return sb.toString();
    }
}
