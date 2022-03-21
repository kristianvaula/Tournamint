package edu.ntnu.idatt1002.k1g01;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TournamentApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomePageWindow.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getCause());
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
