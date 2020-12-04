package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Pane simplePendulumPane;

    @FXML
    private Pane inclinedPlanePane;

    @FXML
    private Label lbInclinedPlane;

    @FXML
    private Label lbPendulum;

    @FXML
    private Rectangle rectangle;

    @FXML
    private Rectangle rectangle2;

    @FXML

    void zoomIn(MouseEvent event) {
        if(event.getSource()==this.simplePendulumPane){
            zoomInMethod(simplePendulumPane,lbPendulum,rectangle);
        }

        if(event.getSource()==this.inclinedPlanePane){
            zoomInMethod(inclinedPlanePane,lbInclinedPlane,rectangle2);
        }


    }

    @FXML
    void displayAlert(MouseEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "Sorry but this lab is not yet available").showAndWait();
    }

    @FXML

    void zoomOut(MouseEvent event) {
        if(event.getSource()==this.simplePendulumPane){
            zoomOutMethod(simplePendulumPane,lbPendulum,rectangle);
        }
        if(event.getSource()==this.inclinedPlanePane){
           zoomOutMethod(inclinedPlanePane,lbInclinedPlane,rectangle2);
        }

    }

    public void zoomInMethod(Pane pane,Label label,Rectangle rectangle){
        Scale newScale1 = new Scale();
        newScale1.setX(1.1);
        newScale1.setY(1.1);
        newScale1.setPivotX(pane.getScaleX());
        newScale1.setPivotY(pane.getScaleY());
        pane.getTransforms().add(newScale1);
        label.getTransforms().add(newScale1);
        rectangle.setVisible(true);
    }

    public void zoomOutMethod(Pane pane,Label label,Rectangle rectangle){
        Scale newScale1 = new Scale();
        newScale1.setX(1/1.1);
        newScale1.setY(1/1.1);
        newScale1.setPivotX(pane.getScaleX());
        newScale1.setPivotY(pane.getScaleY());
        pane.getTransforms().add(newScale1);
        label.getTransforms().add(newScale1);
        rectangle.setVisible(false);
    }

    @FXML
    void launchLab(MouseEvent event) throws IOException {
        Stage stage = (Stage) simplePendulumPane.getScene().getWindow();
        stage.close();
        Parent root1 = FXMLLoader.load(getClass().getResource("/resources/view/pendulum.fxml"));
        Scene scene = new Scene(root1);
        stage.setTitle("PKFokam Virtual Lab");
        stage.setMaximized(false);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       rectangle.setVisible(false);
        rectangle2.setVisible(false);

    }


}
