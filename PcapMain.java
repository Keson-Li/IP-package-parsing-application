import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class PcapMain extends Application {

    private Desktop desktop = Desktop.getDesktop();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("wireshark_app.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("wireshark_app_main.fxml"));

        primaryStage.setTitle("Wireshark App");
        primaryStage.setScene(new Scene(root, 1200, 597));
//        primaryStage.setMaximized(false);
        primaryStage.setMaxWidth(1200);
        primaryStage.setMaxHeight(597);
        primaryStage.setMinHeight(597);
        primaryStage.setMinWidth(1200);
        primaryStage.setFullScreen(false);
        primaryStage.show();

    }
}



