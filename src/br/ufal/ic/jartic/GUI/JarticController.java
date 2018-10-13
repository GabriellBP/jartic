package br.ufal.ic.jartic.GUI;

import br.ufal.ic.jartic.connection.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class JarticController implements Initializable, Observer {
    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Slider brushSize;

    @FXML
    private Canvas canvas;

    private GraphicsContext brushTool;

    private Connector connector;

    @FXML
    private TextField serverIP;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        brushTool = canvas.getGraphicsContext2D();

    }

    @FXML
    void createRoom() {
        if (connector != null)
            return;
        connector = new Server();
        connector.addObserver(this);
        connector.startRunning();
    }

    @FXML
    void enterRoom() {
        if (connector != null)
            return;
        connector = new Client(serverIP.getText());
        connector.addObserver(this);
        connector.startRunning();
    }

    private void startDrawing() {
        canvas.setOnMouseDragged(e -> {
            System.out.println("x: " + e.getX() + ", y: " + e.getY());
            double size = brushSize.getValue();
            double x = e.getX() - size / 2, y = e.getY() - size / 2;
            Color color = colorPicker.getValue();
            DrawPacket drawPacket = new DrawPacket(x, y, size, color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
            connector.sendDraw(drawPacket);
            this.draw(drawPacket);
        });
    }

    @Override
    public void update(Observable observable, Object obj) {
        if (observable instanceof Server || observable instanceof Client) {
            System.out.println("OBSERVED!");
            DrawThread drawThread = new DrawThread(connector);
            drawThread.setDaemon(true);
            drawThread.start();
            startDrawing();
        }
    }

    private void draw(DrawPacket draw) {
        brushTool.setFill(new Color(draw.colorRed, draw.colorGreen, draw.colorBlue, draw.colorOpacity));
        brushTool.fillRoundRect(draw.x, draw.y, draw.brushSize, draw.brushSize, draw.brushSize, draw.brushSize);
    }

    class DrawThread extends Thread {
        Connector con;

        DrawThread(Connector con){
            this.con = con;
        }

        @Override
        public void run() {
            Packet message = null;
            do{
                try{
                    message = (Packet) this.con.input.readObject();
                    if (message.type.equals("DRAW")) {
                        draw((DrawPacket) message);
                    }
                }catch(Exception e){
                    System.out.println("The user has sent an unknown object!");
                    e.printStackTrace();
                }
            } while (message == null || !message.type.equals("END"));
        }
    }

}
