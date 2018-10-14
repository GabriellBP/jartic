package br.ufal.ic.jartic.connection.packet;

import java.io.Serializable;

public class Packet implements Serializable {
    public String type;

    public Packet(String type) {
        this.type = type;
    }
}
