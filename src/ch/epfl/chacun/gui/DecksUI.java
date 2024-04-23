package ch.epfl.chacun.gui;

import ch.epfl.chacun.Tile;
import ch.epfl.chacun.Occupant;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.function.Consumer;
import ch.epfl.chacun.gui.ColorMap.*;

public class DecksUI {

    private DecksUI() {
    }

    public static Node create(
            ObservableValue<Tile> nextTileObservable,
            ObservableValue<Integer> normalTilesCountObservable,
            ObservableValue<Integer> menhirTilesCountObservable,
            ObservableValue<String> nextTileTextObservable,
            Consumer<Occupant> onSkipOccupant) {

        VBox root = new VBox();
        root.getStylesheets().add("decks.css");

        StackPane nextTileStack = new StackPane();
        nextTileStack.setId("next-tile");

        ImageView nextTileView = new ImageView();
        nextTileView.imageProperty().bind(
                () -> nextTileObservable.getValue()., // Assurez-vous que Tile a une méthode getImage
                nextTileObservable
        ));

        Text nextTileText = new Text();
        nextTileText.wrappingWidthProperty().bind(nextTileView.fitWidthProperty().multiply(0.8));
        nextTileText.textProperty().bind(nextTileTextObservable);
        nextTileText.visibleProperty().bind(nextTileTextObservable.map(text -> !text.isEmpty()));

        nextTileText.setOnMouseClicked(e -> onSkipOccupant.accept(null));
        nextTileStack.getChildren().addAll(nextTileView, nextTileText);

        HBox decks = new HBox();

        StackPane normalDeckStack = createDeckStack("NORMAL", normalTilesCountObservable);
        StackPane menhirDeckStack = createDeckStack("MENHIR", menhirTilesCountObservable);

        decks.getChildren().addAll(normalDeckStack, menhirDeckStack);

        root.getChildren().addAll(nextTileStack, decks);

        return root;
    }

    private static StackPane createDeckStack(String label, ObservableValue<Integer> tilesCountObservable) {
        ImageView deckView = new ImageView(); // Assurez-vous de définir l'image du tas correspondant
        deckView.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        deckView.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);

        Text deckText = new Text();
        deckText.textProperty().bind(Bindings.convert(tilesCountObservable));

        StackPane deckStack = new StackPane(deckView, deckText);
        deckStack.getStyleClass().add(label.toLowerCase() + "-deck");

        return deckStack;
    }
}
