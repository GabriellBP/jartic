package br.ufal.ic.jartic.GUI;

import br.ufal.ic.jartic.connection.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

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

    @FXML
    private TextField serverIP, status, chatInput;

    @FXML
    private TextArea chat;

    @FXML
    private Button btnEnter, btnCreate, btnChat;

    private GraphicsContext brushTool;
    private Connector connector;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        brushTool = canvas.getGraphicsContext2D();
        btnChat.setDefaultButton(true);
    }

    private void disableButtons(boolean disable) {
        this.btnCreate.setDisable(disable);
        this.btnEnter.setDisable(disable);
        this.btnChat.setDisable(!disable);
    }

    @FXML
    void sendMessage() {
        if(connector != null) {
            MessagePacket message = new MessagePacket(chatInput.getText());
            connector.sendPacket(message);
            this.chatInput.clear();
            write(message, true);
        }
    }

    @FXML
    void createRoom() {
        disableButtons(true);
        connector = new Server(status);
        connector.addObserver(this);
        connector.startRunning();
    }

    @FXML
    void enterRoom() {
        disableButtons(true);
        connector = new Client(serverIP.getText(), status);
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
            connector.sendPacket(drawPacket);
            this.draw(drawPacket);
        });
    }

    @Override
    public void update(Observable observable, Object obj) {
        if (observable instanceof Server || observable instanceof Client) {
            status.setText("Conexão estabelecida!");
            ConnectionThread drawThread = new ConnectionThread(connector);
            drawThread.setDaemon(true);
            drawThread.start();
            startDrawing();
        }
    }

    private void draw(DrawPacket draw) {
        brushTool.setFill(new Color(draw.colorRed, draw.colorGreen, draw.colorBlue, draw.colorOpacity));
        brushTool.fillRoundRect(draw.x, draw.y, draw.brushSize, draw.brushSize, draw.brushSize, draw.brushSize);
    }

    private void write(MessagePacket message, boolean owner) {
        chat.appendText((owner ? "Você: " : "Amigo: ") + message.message + "\n");
    }

    class ConnectionThread extends Thread {
        Connector con;

        ConnectionThread(Connector con){
            this.con = con;
        }

        @Override
        public void run() {
            Packet packet;
            do{
                try{
                    packet = (Packet) this.con.input.readObject();
                    if (packet.type.equals("DRAW")) {
                        draw((DrawPacket) packet);
                    } else if (packet.type.equals("MESSAGE")) {
                        write((MessagePacket) packet, false);
                    }
                }catch(Exception e){
                    System.out.println("The user has sent an unknown object!");
//                    e.printStackTrace();
                    break;
                }
            } while (!packet.type.equals("END"));

            this.con.closeConnection();
            status.setText("Conexão perdida!");
            disableButtons(false);
            canvas.setOnMouseDragged(null);
        }
    }
}
