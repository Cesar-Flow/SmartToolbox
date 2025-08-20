package org.whoslv.tools.XMLProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotasDoZip {
    private String nomeZip;
    private List<HashMap<String, String>> notas;

    public NotasDoZip(String nomeZip) {
        this.nomeZip = nomeZip;
        this.notas = new ArrayList<>();
    }

    public void addNota(HashMap<String, String> nota) {
        notas.add(nota);
    }

    public String getNomeZip() {
        return nomeZip;
    }

    public List<HashMap<String, String>> getNotas() {
        return notas;
    }

    public int getCount() {
        return notas.toArray().length;
    }
}
