package ch.epfl.chacun.gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base32Test {

    @Test
    void isValid() {
    }

    @Test
    void encodeBits5() {
        System.out.println( Base32.encodeBits5(15));
    }

    @Test
    void encodeBits10() {
        System.out.println( Base32.encodeBits10(680));
    }

    @Test
    void decode() {
    }
}