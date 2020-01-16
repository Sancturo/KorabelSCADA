
package ScadaGUI;

import com.jcraft.jsch.Session;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import korabelscada.KorabelSCADA;


public class MyAppFX extends Application {
    public static Session SSHDBSess;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ScadaGUI/FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(635);
        stage.setWidth(750);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
