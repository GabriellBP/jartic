package br.ufal.ic.jartic.connection.packet;

public class MessagePacket extends Packet {
    public String message;

    public MessagePacket(String message) {
        super("MESSAGE");
        this.message = message;
    }
}
