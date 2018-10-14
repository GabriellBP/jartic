package br.ufal.ic.jartic.connection;

import java.io.Serializable;

public class Packet implements Serializable {
    public String type;

    Packet(String type) {
        this.type = type;
    }
}
