package br.ufal.ic.jartic.connection;

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

    public void draw(DrawPacket draw) {
        System.out.println("OI");
    }

    void setupStreams() throws IOException{
        System.out.println(this.connection);
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());

        System.out.println("\n Streams are now setup \n");
    }

    void closeConnection(){
        System.out.println("\n Closing Connections... \n");
        try{
            output.close(); //Closes the output path to the client
            input.close(); //Closes the input path to the server, from the client.
            connection.close(); //Closes the connection between you can the client
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //Send a draw to the client
    public void sendDraw(DrawPacket draw){
        try{
            output.writeObject(draw);
            output.flush();
            System.out.println("\nSERVER -");
        }catch(IOException ioException){
            System.out.println("\n ERROR: CANNOT SEND MESSAGE, PLEASE RETRY");
            ioException.printStackTrace();
        }
    }

    public abstract void startRunning();
}
