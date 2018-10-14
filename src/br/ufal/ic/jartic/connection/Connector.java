package br.ufal.ic.jartic.connection;

import br.ufal.ic.jartic.connection.packet.Packet;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public abstract class Connector extends Observable {
    public ObjectOutputStream output;
    public ObjectInputStream input;
    ServerSocket server;
    Socket connection;
    TextField status;

    void setupStreams() throws IOException{
        System.out.println(this.connection);
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());

        System.out.println("\n Streams are now setup \n");
    }

    public void closeConnection(){
        System.out.println("\n Closing Connections... \n");
        try{
            output.close(); //Closes the output path to the client
            input.close(); //Closes the input path to the server, from the client.
            connection.close(); //Closes the connection between you can the client
            notifyObservers(new Packet("END"));
            System.out.println("Connections closed!");
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //Send a packet to the client
    public void sendPacket(Packet pkt){
        try{
            output.writeObject(pkt);
            output.flush();
        }catch(IOException ioException){
            System.out.println("\n ERROR: CANNOT SEND MESSAGE/DRAW, PLEASE RETRY");
            ioException.printStackTrace();
        }
    }

    public abstract void startRunning();
}
