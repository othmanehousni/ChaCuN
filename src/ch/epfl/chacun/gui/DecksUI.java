package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

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

        StackPane nextTileStack = createTileToPlace(nextTileO, nextTileTextO, onSkipOccupant);
        StackPane normalDeckStack = createDeckStack("NORMAL", normalTilesCountO);
        StackPane menhirDeckStack = createDeckStack("MENHIR", menhirTilesCountO);

        decks.getChildren().addAll(normalDeckStack, menhirDeckStack);
        root.getChildren().addAll(decks, nextTileStack);
        return root;
    }

    private static StackPane createDeckStack(String label, ObservableValue<Integer> tilesCountObservable) {
        ImageView deckView = new ImageView();

        deckView.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
        deckView.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
        deckView.setImage(new Image(STR."\{ImageLoader.LARGE_TILE_PIXEL_SIZE}/\{label}.jpg"));

        Text deckText = new Text();
        deckText.textProperty().bind(Bindings.convert(tilesCountObservable));
        deckText.visibleProperty().bind(tilesCountObservable.map(count -> count > 0));

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

        nextTileView.imageProperty().bind(tileObservableValue.map(tile -> ImageLoader.largeImageForTile(tile.id())));
        nextTileView.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        nextTileView.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);

        nextTileText.setWrappingWidth(WRAPPING_PERCENTAGE * ImageLoader.LARGE_TILE_FIT_SIZE);
        nextTileText.textProperty().bind(nextTileTextObservable);
        nextTileText.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        nextTileText.visibleProperty().bind(nextTileTextObservable.map(s -> !s.isEmpty()));
        nextTileText.setOnMouseClicked(_ -> onSkipOccupant.accept(null));
        nextTileStack.getChildren().addAll(nextTileText, nextTileView);
        return nextTileStack;
    }

}
