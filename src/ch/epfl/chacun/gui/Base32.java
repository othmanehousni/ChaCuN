package ch.epfl.chacun.gui;

import ch.epfl.chacun.Preconditions;

public class Base32 {
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    public final static int BIT5 = 32;
    public final static int BIT10 = 1024;
    public final static int MASK31 = 0x1F;
    public static final int MASK4FIRST = 0b1111;
    public static final int NO_OCCUPANT_BINARY31 = 0b11111;
    public static final int BINARY3 = 0b11;

    public static boolean isValid(String stringInput) {
        if(stringInput == null || stringInput.isEmpty())
            return false;
        return stringInput.chars().allMatch(c -> ALPHABET.indexOf(c) != -1);
    }

    public static String encodeBits5(int value) {
        Preconditions.checkArgument((value >>> 5) == 0);
        return String.valueOf(ALPHABET.charAt(value));
    }

    public static String encodeBits10(int value) {
        Preconditions.checkArgument((value >>> 10) == 0);
        return encodeBits5((value >> 5)  & MASK31) +
                encodeBits5(value  & MASK31);
    }

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
