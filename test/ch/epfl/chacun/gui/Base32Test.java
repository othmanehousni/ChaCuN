package ch.epfl.chacun.gui;

import ch.epfl.chacun.Base32;
import org.junit.jupiter.api.Test;

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