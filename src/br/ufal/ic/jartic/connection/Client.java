package br.ufal.ic.jartic.connection;

import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Connector{
    private String serverIP;

    public Client(String host, TextField status) {
        this.serverIP = host;
        this.status = status;
    }

    //connect to server
    public void startRunning(){
        connectToServer();
    }

    //connect to server
    private void connectToServer() {
        System.out.println("Attempting connection... \n");
        status.setText("Tentando conectar...");
        Thread clientThread = new Thread(() -> {
            try {
                connection = new Socket(InetAddress.getByName(serverIP), 1717);
                System.out.println("Accepted");
                setupStreams();
                System.out.println("Connection Established! Connected to: " + connection.getInetAddress().getHostName());
                setChanged();
                notifyObservers();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientThread.setDaemon(true);
        clientThread.start();
    }
}
