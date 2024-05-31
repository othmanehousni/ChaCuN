package ch.epfl.chacun;

public class Base32 {

    //todo verifier to uppercase derniere ligne
    /**
     * The Base32 alphabet.
     */
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    /**
     * Mask to extract the lower 5 bits.
     */
    public static final int MASK31 = 0x1F;

    /**
     * Mask to extract the first 4 bits.
     */
    public static final int MASK4FIRST = 0xF;
    /**
     * Binary representation of no occupant using 5 bits.
     */
    public static final int NO_OCCUPANT_BINARY31 = 0b11111;
    /**
     * Binary representation of 3 using 2 bits.
     */
    public static final int BINARY3 = 0b11;

    /**
     * Private constructor to prevent instantiation.
     */
    private Base32() {}

    /**
     * Checks if the provided string is a valid Base32 string.
     *
     * @param stringInput The input string to be validated
     * @return True if the input string is a valid Base32 string, otherwise false
     */

    public static boolean isValid(String stringInput) {
        if(stringInput == null || stringInput.isEmpty())
            return false;
        return stringInput.chars().allMatch(c -> ALPHABET.indexOf(c) != -1);
    }

    /**
     * Encodes the lower 5 bits of the given value into a Base32 character.
     *
     * @param value The value to encode (must be in the range [0, 31])
     * @return The Base32 character representing the encoded value
     * @throws IllegalArgumentException if the value is out of range
     */
    public static String encodeBits5(int value) {
        Preconditions.checkArgument((value >>> 5) == 0);
        return String.valueOf(ALPHABET.charAt(value));
    }

    /**
     * Encodes the lower 10 bits of the given value into a Base32 string of length 2.
     *
     * @param value The value to encode (must be in the range [0, 1023])
     * @return The Base32 string representing the encoded value
     * @throws IllegalArgumentException if the value is out of range
     */
    public static String encodeBits10(int value) {
        Preconditions.checkArgument((value >>> 10) == 0);
        return encodeBits5((value >> 5)  & MASK31) +
                encodeBits5(value  & MASK31);
    }

    /**
     * Decodes the given Base32 string into an integer value.
     *
     * @param input The input Base32 string (of length 1 or 2)
     * @return The integer value decoded from the input string
     * @throws IllegalArgumentException if the input string is invalid or has incorrect length
     */
    public static int decode(String input) {
        Preconditions.checkArgument(input.length() == 1 || input.length() == 2);
        Preconditions.checkArgument(isValid(input));
        int result = 0;
        for (char c : input.toCharArray()) {
            result <<= 5;
            result |= ALPHABET.indexOf(Character.toUpperCase(c));
        }
        return result;
    }
}
