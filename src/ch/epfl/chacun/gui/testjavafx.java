package ch.epfl.chacun.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class testjavafx extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button btn1 = new Button("Button 1");
        Button btn2 = new Button("Button 2");

        VBox root = new VBox(10); // VBox layout with spacing of 10
        root.getChildren().addAll(btn1, btn2);

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Two Buttons Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
