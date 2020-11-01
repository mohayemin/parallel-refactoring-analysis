package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SingleCodeRangeTest {

    @Test
    public void fileMismatch_givesFalse() {
        var one = new SingleCodeRange("x", 10, 5);
        var two = new SingleCodeRange("y", 8, 10);

        Assertions.assertFalse(one.hasOverlap(two));
        Assertions.assertFalse(two.hasOverlap(one));
    }

    /**
     *     o-------o
     *   o-------------o
     */
    @Test
    public void fullOverlap() {
        var one = new SingleCodeRange("x", 10, 5);
        var two = new SingleCodeRange("x", 8, 10);

        Assertions.assertTrue(one.hasOverlap(two));
        Assertions.assertTrue(two.hasOverlap(one));
    }

    /**
     *  o-------o
     *       o---------o
     */
    @Test
    public void partialOverlap() {
        var one = new SingleCodeRange("x", 10, 5);
        var two = new SingleCodeRange("x", 12, 5);

        Assertions.assertTrue(one.hasOverlap(two));
        Assertions.assertTrue(two.hasOverlap(one));
    }

    /**
     *   o-------o
     *           o---------o
     */
    @Test
    public void marginalOverlap() {
        var one = new SingleCodeRange("x", 10, 5);
        var two = new SingleCodeRange("x", 15, 5);

        Assertions.assertTrue(one.hasOverlap(two));
        Assertions.assertTrue(two.hasOverlap(one));
    }

    /**
     *  o-------o
     *              o---------o
     */
    @Test
    public void noOverlap() {
        var one = new SingleCodeRange("x", 10, 5);
        var two = new SingleCodeRange("x", 17, 5);

        Assertions.assertFalse(one.hasOverlap(two));
        Assertions.assertFalse(two.hasOverlap(one));
    }
}

