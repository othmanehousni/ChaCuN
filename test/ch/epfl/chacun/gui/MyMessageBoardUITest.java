package ch.epfl.chacun.gui;

import ch.epfl.chacun.MessageBoard;
import ch.epfl.chacun.PlayerColor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Set;

public class MyMessageBoardUITest extends Application {

    private final ObjectProperty<ObservableList<MessageBoard.Message>> messages =
            new SimpleObjectProperty<>(FXCollections.observableArrayList()); // Initialise avec une liste observable
    private final ObjectProperty<Set<Integer>> highlightedTilesProperty =
            new SimpleObjectProperty<>(Set.of()); // Initialise avec un ensemble vide

    @Override
    public void start(Stage primaryStage) {
        // Ajoutez vos messages initiaux à la liste observable, pas directement à l'ObjectProperty
        messages.get().add(new MessageBoard.Message("aa a marque bien joue", 1, Set.of(PlayerColor.RED), Set.of(1, 2, 3)));
        messages.get().add(new MessageBoard.Message("lui aussi a marque dinguerie", 3, Set.of(PlayerColor.BLUE), Set.of(3)));

        // Création de la vue du tableau d'affichage en passant l'ObjectProperty contenant la liste observable
        Node messageBoardView = MessageBoardUI.create(
                messages, // ObservableValue de la liste des messages
                highlightedTilesProperty // ObjectProperty pour les tuiles mises en évidence
        );



        BorderPane root = new BorderPane();
        root.setCenter(messageBoardView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Test du Tableau d'Affichage");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
