package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Set;

public final class MessageBoardUI {

    private MessageBoardUI() {}

    public static Node create(ObservableValue<List<MessageBoard.Message>> messagesObservable, ObjectProperty<Set<Integer>> highlightedTilesObservable) {

        ScrollPane messageScrollPane = new ScrollPane();
        messageScrollPane.setId("message-board");
        messageScrollPane.getStylesheets().add("message-board.css");


        VBox messagesContainer = new VBox();
        messageScrollPane.setContent(messagesContainer);

        messagesObservable.addListener((_, oldMessages, newMessages) -> {
            updateMessages(messagesContainer, oldMessages, newMessages, highlightedTilesObservable);
            messageScrollPane.layout();
            messageScrollPane.setVvalue(1);
        });
        return messageScrollPane;
    }

    private static void updateMessages(VBox messagesContainer, List<MessageBoard.Message> oldMessages, List<MessageBoard.Message> newMessages, ObjectProperty<Set<Integer>> selectedTiles) {
        if (newMessages.size() > oldMessages.size()) {
            List<MessageBoard.Message> additionalMessages = newMessages.subList(oldMessages.size(), newMessages.size());
            for (MessageBoard.Message message : additionalMessages) {
                Text messageText = new Text(message.text());
                messageText.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
                messagesContainer.getChildren().add(messageText);

                messageText.setOnMouseEntered(event -> selectedTiles.setValue(message.tileIds()));
                messageText.setOnMouseExited(event -> selectedTiles.setValue(Set.of()));
            }
        }
    }
}

