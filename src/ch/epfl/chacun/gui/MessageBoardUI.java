package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Set;

public class MessageBoardUI {

    private MessageBoardUI() {}


    public static Node create(ObjectProperty<ObservableList<MessageBoard.Message>> messagesObservable, ObjectProperty<Set<Integer>> objectProperty) {

        ScrollPane messageScrollPane = new ScrollPane();
        messageScrollPane.setId("message-board");
        messageScrollPane.getStylesheets().add("message-board.css");

        VBox messagesContainer = new VBox();
        messageScrollPane.setContent(messagesContainer);



        messagesObservable.addListener((obs, oldMessages, newMessages) -> {
            updateMessages(messagesContainer, oldMessages, newMessages,objectProperty);
            Platform.runLater(() -> messageScrollPane.setVvalue(1));
        });


        return messageScrollPane;
    }

    private static void updateMessages(VBox messagesContainer, List<MessageBoard.Message> oldMessages, List<MessageBoard.Message> newMessages, ObjectProperty<Set<Integer>> selectedTile) {
        if (newMessages.size() > oldMessages.size()) {
            List<MessageBoard.Message> additionalMessages = newMessages.subList(oldMessages.size(), newMessages.size());
            for (MessageBoard.Message message : additionalMessages) {
                Text messageText = new Text(message.text());
                messageText.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
                messagesContainer.getChildren().add(messageText);

                //faudra bind le selectedTile

                messageText.setOnMouseEntered(event -> selectedTile.setValue(message.tileIds()));
                messageText.setOnMouseExited(event -> selectedTile.setValue(Set.of()));
            }
        }
    }



//    public static Node create(ObservableValue<List<MessageBoard.Message>> gameStateObservable, ObjectProperty<Set<Integer>> selectedTile) {
//        VBox root = new VBox();
//        root.getStyleClass().add("message-board");
//
//        TextFlow message = new TextFlow();
//        message.getStyleClass().add("message");
//        Text messageText = new Text();
//        messageText.textProperty().bind(gameStateObservable.map(gameState -> textMaker.message(gameState.messageBoard().lastMessage())));
//
//        root.getChildren().add(message);
//
//        VBox players = new VBox();
//        players.getStyleClass().add("players");
//
//        for (PlayerColor playerColor : PlayerColor.values()) {
//            players.getChildren().add(createPlayerInfo(gameStateObservable, textMaker, playerColor));
//        }
//
//        root.getChildren().add(players);
//
//        return root;
//    }
}

