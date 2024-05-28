package ch.epfl.chacun.gui;

import ch.epfl.chacun.Base32;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The ActionUI class is a public, non-instantiable class that contains the code
 * for creating the part of the graphical user interface that displays and validates actions input by the user.
 */
 public final class ActionUI {

    private final static int MAX_ACTIONS = 4;
    /**
     * Private constructor to prevent instantiation.
     */
    private ActionUI() {}

    /**
     * Creates an HBox containing the graphical representation of the actions.
     *
     * @param actionO the observable version of the list of actions
     * @param actionValidation the event handler for validating the action input by the user
     * @return the HBox containing the graphical representation of the actions
     */
    public static HBox create(ObservableValue<List<String>> actionO, Consumer<String> actionValidation) {
        HBox hbox = new HBox();
        hbox.getStylesheets().add("actions.css");
        hbox.setId("actions");
        Text text = new Text();

        // Bind the text property to the list of actions, it has to be at most 4 and shift each time the max is reached
        // Each message is mapped to a string representing the encoded action with the index
        text.textProperty().bind(actionO.map(list ->
                IntStream.range(Math.max(0, list.size() - MAX_ACTIONS), list.size())
                .mapToObj(i -> STR."\{i + 1} : \{list.get(i)}")
                .collect(Collectors.joining(", "))));

        // Create a text field for the user to input the action and validate it while updating it at each change.
        TextField textField = new TextField();
        textField.setId("action-field");
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getText().chars().filter(c -> Base32.ALPHABET.indexOf(Character.toUpperCase(c)) != -1)
                    .map(Character::toUpperCase)
                    .mapToObj(c -> String.valueOf((char) c))
                    .collect(Collectors.joining());
            change.setText(newText);
            return change;
        }));

        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 2) {
                textField.setText(textField.getText().substring(0, 2));
            }
        });

        // Validate the action when the user presses enter and clear the text field
        textField.setOnAction(_ -> {
            String action = textField.getText();
            if (!action.isEmpty()) {
                actionValidation.accept(action);
                textField.clear();
            }
        });

        hbox.getChildren().addAll(text, textField);
        return hbox;
    }
}
