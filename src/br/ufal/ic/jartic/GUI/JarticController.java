package br.ufal.ic.jartic.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

public class JarticController implements Initializable {
    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Slider brushSize;

    @FXML
    private Canvas canvas;

    private GraphicsContext brushTool;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        brushTool = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(e -> {
            System.out.println("x: " + e.getX() + ", y: " + e.getY());
            double size = brushSize.getValue();
            double x = e.getX() - size / 2, y = e.getY() - size / 2;

            brushTool.setFill(colorPicker.getValue());
            brushTool.fillRoundRect(x, y, size, size, size, size);
        });
    }
}
