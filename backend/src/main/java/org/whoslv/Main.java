package org.whoslv;

import org.whoslv.tools.XMLProcessor.NotasDoZip;
import org.whoslv.tools.XMLProcessor.VisualPath;
import org.whoslv.tools.XMLProcessor.XmlNotaExtractor;
import org.whoslv.tools.XMLProcessor.XmlNotaProcessor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //final String absolutePath = "C:\\Users\\whoslv\\Documents\\EstudosJava\\XMLScope\\src\\main\\java\\org\\whoslv\\AnalisarXML\\XMLs";
        final String absolutePath = (new VisualPath()).setPath();
        final XmlNotaExtractor extractor = new XmlNotaExtractor();

        File pasta = new File(absolutePath);

        if (!pasta.exists() || !pasta.isDirectory()) {
            System.err.println("Pasta inválida: " + absolutePath);
            return;
        }

        List<File> arquivosZip = Arrays.stream(Objects.requireNonNull(pasta.listFiles()))
                .filter(f -> f.getName().toLowerCase().endsWith(".zip"))
                .toList();

        for (File arquivo : arquivosZip) { extractor.addZip(arquivo); }
        extractor.extrairNotas();

        List<NotasDoZip> notas = extractor.getNotas();

        XmlNotaProcessor processor = new XmlNotaProcessor(notas);
        System.out.println(processor); // chama automaticamente o toString()
    }
}
