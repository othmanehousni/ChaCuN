package ch.epfl.chacun;


/**
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 * <p>
 * <p>
 * Represents an animal with a unique identifier and a specific kind.
 * This record encapsulates the properties of an animal in the game, including its identification number and type.
 */
public record Animal(int id, Kind kind) {

    /**
     * Calculates the tile identification number for the animal based on its id.
     * This method maps the animal's unique identifier to a specific tile in the game world.
     *
     * @return the tile identification number derived from the animal's id.
     */
    public int tileId() {
        return Zone.tileId(id) / 10;

    }

    /**
     * Enumerates the different kinds of animals that can exist.
     * Each kind represents a distinct type of animal in the game world.
     */
    public enum Kind {
        MAMMOTH,
        AUROCHS,
        DEER,
        TIGER,
    }
}
