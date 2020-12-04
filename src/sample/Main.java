package sample;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
        private static final int COUNT_LIMIT=10;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/view/sample.fxml"));
        primaryStage.setTitle("PKFokam Virtual Lab");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/logo.png")));
        primaryStage.show();
    }

  /*  @Override
    public void init() throws Exception {
        for(int i=0;i<COUNT_LIMIT;i++){
            double progress=(double) i/COUNT_LIMIT;
            LauncherImpl.notifyPreloader(this,new Preloader.ProgressNotification(progress));
            Thread.sleep(1000);
        }
    }*/


     public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class,MyPreloader.class,args);
    }
}
