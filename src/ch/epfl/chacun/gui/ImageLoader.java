package ch.epfl.chacun.gui;
import javafx.scene.image.Image;

/**
 * The ImageLoader class is a public, non-instantiable class designed
 * to load images for tiles. It also provides a number of public, static, and final constants
 * that specify the dimensions of the various images.
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 */
 public final class ImageLoader {

    /** Size of large tiles. */
    public static final int LARGE_TILE_PIXEL_SIZE = 512;

    /** Display size of large tiles. */
    public static final int LARGE_TILE_FIT_SIZE = LARGE_TILE_PIXEL_SIZE >> 1;

    /** Size of normal tiles. */
    public static final int NORMAL_TILE_PIXEL_SIZE = 256;

    /** Display size of normal tiles. */
    public static final int NORMAL_TILE_FIT_SIZE = NORMAL_TILE_PIXEL_SIZE >> 1;

    /** Size of marker. */
    public static final int MARKER_PIXEL_SIZE = 96;

    /** Display size of marker. */
    public static final int MARKER_FIT_SIZE = MARKER_PIXEL_SIZE >> 1;

    /**
     * Private constructor to prevent instantiation.
     */
    private ImageLoader() {}

    /**
     * Returns the 256-pixel image for the specified tile ID.
     *
     * @param tileId the ID of the tile
     * @return the image of the tile
     */
    public static Image normalImageForTile(int tileId) {
        return new Image(STR."/\{NORMAL_TILE_PIXEL_SIZE}/\{String.format("%02d", tileId)}.jpg");
    }

    /**
     * Returns the 512-pixel image for the specified tile ID.
     *
     * @param tileId the ID of the tile
     * @return the image of the tile
     */
    public static Image largeImageForTile(int tileId) {
        return new Image(STR."/\{LARGE_TILE_PIXEL_SIZE}/\{String.format("%02d", tileId)}.jpg");
    }
}

