package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Set;



/**
 * The MessageBoardUI class is a public, non-instantiable class that contains the code
 * for creating the graphical user interface for the message board
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 */
public final class MessageBoardUI {

    /**
     * Private constructor to prevent instantiation.
     */
    private MessageBoardUI() {}

    /**
     * Creates the root JavaFX node of the scene graph that displays the message board.
     *
     * @param messagesObservable the observable version of the messages displayed on the message board
     * @param highlightedTilesObservable a property containing the set of tile IDs to highlight on the board
     * @return the root node of the scene graph
     */
    public static Node create(ObservableValue<List<MessageBoard.Message>> messagesObservable,
                              ObjectProperty<Set<Integer>> highlightedTilesObservable) {

        ScrollPane messageScrollPane = new ScrollPane();
        messageScrollPane.setId("message-board");
        messageScrollPane.getStylesheets().add("message-board.css");
        VBox messagesContainer = new VBox();
        messageScrollPane.setContent(messagesContainer);

        // Update the messages displayed in the message board
        messagesObservable.addListener((_, oldMessages, newMessages) -> {
            System.out.println(newMessages);
            updateMessages(messagesContainer, oldMessages, newMessages, highlightedTilesObservable);
            messageScrollPane.layout();
            messageScrollPane.setVvalue(1);
        });
        return messageScrollPane;
    }

    /**
     * Updates the messages displayed in the message board.
     *
     * @param messagesContainer the container for the messages
     * @param oldMessages the old list of messages
     * @param newMessages the new list of messages
     * @param selectedTiles the property containing the set of tile IDs to highlight
     */
    private static void updateMessages(VBox messagesContainer, List<MessageBoard.Message> oldMessages,
                                       List<MessageBoard.Message> newMessages,
                                       ObjectProperty<Set<Integer>> selectedTiles) {

        if (newMessages.size() > oldMessages.size()) { // If there are new messages
            // Add the new messages to the message container
            List<MessageBoard.Message> additionalMessages = newMessages.subList(oldMessages.size(), newMessages.size());
            for (MessageBoard.Message message : additionalMessages) {

                // for each message create a new text node and add it to the message container
                Text messageText = new Text(message.text());
                messageText.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
                messagesContainer.getChildren().add(messageText);

                // Highlight the tiles associated with the message when the mouse enters the message
                messageText.setOnMouseEntered(_ -> selectedTiles.setValue(message.tileIds()));
                messageText.setOnMouseExited(_ -> selectedTiles.setValue(Set.of()));
            }
        }
    }
}

