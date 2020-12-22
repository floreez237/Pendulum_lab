package sample;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.Controllers.PreloaderController;

public class MyPreloader extends Preloader {

    public MyPreloader() {
    }

    private Scene scene;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage=primaryStage;
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setTitle("PKFokam Virtual Lab");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/logo.png")));
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        StateChangeNotification.Type type=info.getType();
        switch (type){
            case BEFORE_START:
                stage.hide();
                break;
        }
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if(info instanceof ProgressNotification){
            PreloaderController.lbProcess.setText("Loading...  "+(int)(((ProgressNotification) info).getProgress()*100)+"%");
            PreloaderController.progressBar.setProgress(((ProgressNotification)info).getProgress());
        }
    }

    @Override
    public void init() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/view/preloader.fxml"));
        scene=new Scene(root);
    }
}
