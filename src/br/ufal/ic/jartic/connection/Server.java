package br.ufal.ic.jartic.connection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server extends Connector {

    public void startRunning(){
        try{
            server = new ServerSocket (1717);
            waitForConnection();
        }catch(EOFException eofException){
            System.out.println("\n Client terminated the connection");
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    private void waitForConnection(){
        System.out.println(" Waiting for someone to connect... ");
        Thread serverThread = new Thread(() -> {
            try {
                connection = server.accept();
                System.out.println("Accepted");
                setupStreams();
                System.out.println(" Now connected to " + connection.getInetAddress().getHostName());
                setChanged();
                notifyObservers();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }

}
