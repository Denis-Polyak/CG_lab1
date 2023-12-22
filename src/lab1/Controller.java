package lab1;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    private double minX = 0;
    private double maxX = 3.0;
    private double minY = 0;
    private double maxY = 2.0;

    @FXML
    private Pane centerPane;

    @FXML
    private Canvas canvas;

    @FXML
    private TextField minXField;

    @FXML
    private TextField maxXField;

    @FXML
    private Button drawButton;

    public void close() {
        Platform.exit();
    }

    public void draw() {
        try {
            minX = Double.parseDouble(minXField.getText());
            maxX = Double.parseDouble(maxXField.getText());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.WHITESMOKE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.strokeLine(toScreenX(minX), toScreenY(0), toScreenX(maxX), toScreenY(0));
            gc.strokeLine(toScreenX(0), toScreenY(maxY), toScreenX(0), toScreenY(minY));
            List<Point> points = tabulation();
            gc.beginPath();
            gc.moveTo(toScreenX(points.get(0).getX()), toScreenY(points.get(0).getY()));
            for (int i = 1; i < points.size(); i++) {
                gc.lineTo(toScreenX(points.get(i).getX()), toScreenY(points.get(i).getY()));
            }
            gc.stroke();
        } catch (NumberFormatException e) {
            showAlert("Invalid input. Please enter valid numeric bounds.");
        }
    }

    private List<Point> tabulation() {
        return IntStream
                .range(0, (int) canvas.getWidth())
                .mapToDouble(this::toWorldX)
                .mapToObj(x -> new Point(x, f(x)))
                .collect(Collectors.toList());
    }

    private int toScreenX(double x) {
        return (int) (canvas.getWidth() * (x - minX) / (maxX - minX));
    }

    private int toScreenY(double y) {
        return (int) (canvas.getHeight() * (1 - (y - minY) / (maxY - minY)));
    }

    private double toWorldX(int xs) {
        return 1.0 * xs / canvas.getWidth() * (maxX - minX) + minX;
    }

    private double toWorldY(int ys) {
        return (1.0 * ys - canvas.getHeight()) / (-canvas.getHeight()) * (maxY - minY) + minY;
    }

    private double f(double x) {
        return (1 + 0.55 * x * x) / (1.5 + Math.sqrt(0.2 + x * x));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}