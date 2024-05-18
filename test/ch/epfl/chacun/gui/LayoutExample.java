package ch.epfl.chacun.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LayoutExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Layout Example");

        // Create the main layout pane
        BorderPane borderPane = new BorderPane();

        // Create components for the left section
        Label leftLabel = new Label("Big Section on Left");
        leftLabel.setMinSize(200, 400);  // Set minimum size for visibility

        // Components for the right section
        VBox rightVBox = new VBox();
        rightVBox.setSpacing(10);

        Button topButton = new Button("Top on Right");
        topButton.setMinSize(100, 150);  // Set size for visibility

        Button bottomButton = new Button("Bottom on Right");
        bottomButton.setMinSize(100, 150);  // Set size for visibility

        // Add buttons to the VBox
        rightVBox.getChildren().addAll(topButton, bottomButton);

        // Add components to the borderPane
        borderPane.setLeft(leftLabel);
        borderPane.setRight(rightVBox);

        // Setup and show the stage
        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
