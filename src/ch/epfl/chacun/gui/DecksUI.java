package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

/**
 * The DecksUI class is a public, non-instantiable class that contains the code
 * for creating the part of the graphical user interface that displays the tile decks as well as the tile to place,
 * and allows the player to skip placing an occupant.
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 */
public final class DecksUI {

    private final static double WRAPPING_PERCENTAGE = 0.8;

    private DecksUI() {
    }

    public static Node create(
            ObservableValue<Tile> nextTileO,
            ObservableValue<Integer> normalTilesCountO,
            ObservableValue<Integer> menhirTilesCountO,
            ObservableValue<String> nextTileTextO,
            Consumer<Occupant> onSkipOccupant) {

        HBox decks = new HBox();
        VBox root = new VBox();
        root.getStylesheets().add("decks.css");
        decks.setId("decks");

        // Create the decks and the next tile to place as stack panes
        StackPane nextTileStack = createTileToPlace(nextTileO, nextTileTextO, onSkipOccupant);
        StackPane normalDeckStack = createDeckStack("NORMAL", normalTilesCountO);
        StackPane menhirDeckStack = createDeckStack("MENHIR", menhirTilesCountO);

        decks.getChildren().addAll(normalDeckStack, menhirDeckStack);
        root.getChildren().addAll(decks, nextTileStack);
        return root;
    }

    private static StackPane createDeckStack(String label, ObservableValue<Integer> tilesCountO) {
        ImageView deckView = new ImageView();

        deckView.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
        deckView.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
        deckView.setId(label);

        Text deckText = new Text();
        // Bind the text of the deck to the number of tiles in the deck
        deckText.textProperty().bind(Bindings.convert(tilesCountO));

        StackPane deckStack = new StackPane(deckView, deckText);
        deckStack.setId(label);
        return deckStack;
    }

    private static StackPane createTileToPlace(ObservableValue<Tile> tileObservableValue,
                                               ObservableValue<String> nextTileTextObservable,
                                               Consumer<Occupant> onSkipOccupant) {

        StackPane nextTileStack = new StackPane();
        nextTileStack.setId("next-tile");
        ImageView nextTileView = new ImageView();
        Text nextTileText = new Text();

        // Bind the image of the next tile to place to the tile observable in 512 x 512 pixels
        nextTileView.imageProperty().bind(tileObservableValue.map(tile -> ImageLoader.largeImageForTile(tile.id())));
        nextTileView.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        nextTileView.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);
        nextTileText.setWrappingWidth(WRAPPING_PERCENTAGE * ImageLoader.LARGE_TILE_FIT_SIZE);

        // Bind the text of the next tile to place to the text observable
        nextTileText.textProperty().bind(nextTileTextObservable);
        // Make the text visible only if it is not empty
        nextTileText.visibleProperty().bind(nextTileTextObservable.map(s -> !s.isEmpty()));
        // Skip the occupant when the next tile is clicked
        nextTileText.setOnMouseClicked(_ -> onSkipOccupant.accept(null));

        nextTileStack.getChildren().addAll(nextTileText, nextTileView);
        return nextTileStack;
    }

}
