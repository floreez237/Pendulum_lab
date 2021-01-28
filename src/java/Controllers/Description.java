package java.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Description {
    @FXML
    private JFXButton startButton;

    @FXML
    void launchLab(ActionEvent event) throws IOException {
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.close();
        Parent root1 = FXMLLoader.load(getClass().getResource("/resources/view/pendulum.fxml"));
        Scene scene = new Scene(root1);
        stage.setScene(scene);
        stage.setTitle("PKFokam Pendulum Lab");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/logo.png")));
        stage.setResizable(false);
        stage.show();
    }



}
