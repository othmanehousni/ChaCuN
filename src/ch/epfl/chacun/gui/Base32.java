package ch.epfl.chacun.gui;

import ch.epfl.chacun.Preconditions;

public class Base32 {
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    public final static int BIT5 = 32;
    public final static int BIT10 = 1024;
    public final static int MASK31 = 0x1F;
    public static final int BINARY31 = 0b11111;
    public static final int BINARY3 = 0b111;

    public static boolean isValid(String stringInput) {
        if(stringInput == null || stringInput.isEmpty())
            return false;
        return stringInput.chars().allMatch(c -> ALPHABET.indexOf(Character.toUpperCase(c)) != -1);
    }

    public static String encodeBits5(int value) {
        System.out.println(value);
        Preconditions.checkArgument(value >= 0 && value < BIT5);
        return String.valueOf(ALPHABET.charAt(value));
    }

    public static String encodeBits10(int value) {
        Preconditions.checkArgument(value >= 0 && value < BIT10);
        return String.valueOf(ALPHABET.charAt(value & MASK31)) +
                String.valueOf(ALPHABET.charAt((value >> 5) & MASK31));
    }

    public static int decode(String input) {
        int result = 0;
        for (char c : input.toCharArray()) {
            result <<= 5;
            result |= ALPHABET.indexOf(Character.toUpperCase(c));
        }
        return result;
    }
}
