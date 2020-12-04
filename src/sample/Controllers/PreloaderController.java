package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

public class PreloaderController implements Initializable{

    @FXML
    private ProgressBar progress;
    public static ProgressBar progressBar;

    @FXML
    private Label label;
    public static Label lbProcess;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progressBar=progress;
        lbProcess=label;
    }


}
