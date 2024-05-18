package ch.epfl.chacun.gui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.converter.DefaultStringConverter;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class ActionsUI {

    private ActionsUI() {}

    public static HBox create(ObservableValue<List<String>> actionO, Consumer<String> actionValidation) {
        HBox hbox = new HBox();
        hbox.getStylesheets().add("actions.css");
        hbox.setId("actions");

        Text text = new Text();
        text.textProperty().bind(actionO.map(list -> {
            StringJoiner joiner = new StringJoiner(", ");
            for (int i = Math.max(0, list.size()-4); i < list.size(); i++) {
                joiner.add(STR."\{i+1} : \{list.get(i)}");
            }
            return joiner.toString();
        }));

        TextField textField = new TextField();
        textField.setId("action-field");
        textField.setTextFormatter(new TextFormatter<>( change -> {
            String newText = change.getText().chars().filter(c -> Base32.ALPHABET.indexOf(Character.toUpperCase(c)) != -1)
                    .map(Character::toUpperCase)
                    .mapToObj(c -> String.valueOf((char) c))
                    .collect(Collectors.joining());
            change.setText(newText);
            return change;
        }));

        textField.setOnAction(e -> {
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
