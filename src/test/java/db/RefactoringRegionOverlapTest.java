package db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RefactoringRegionOverlapTest {
    @Test
    public void fileMismatch_givesFalse() {
        var one = new RefactoringRegion("x", 10, 5);
        var two = new RefactoringRegion("y", 8, 10);

        Assertions.assertFalse(one.overlaps(two));
        Assertions.assertFalse(two.overlaps(one));
    }

    /**
     *     o-------o
     *   o-------------o
     */
    @Test
    public void fullOverlap() {
        var one = new RefactoringRegion("x", 10, 5);
        var two = new RefactoringRegion("x", 8, 10);

        Assertions.assertTrue(one.overlaps(two));
        Assertions.assertTrue(two.overlaps(one));
    }

    /**
     *  o-------o
     *       o---------o
     */
    @Test
    public void partialOverlap() {
        var one = new RefactoringRegion("x", 10, 5);
        var two = new RefactoringRegion("x", 12, 5);

        Assertions.assertTrue(one.overlaps(two));
        Assertions.assertTrue(two.overlaps(one));
    }

    /**
     *   o-------o
     *           o---------o
     */
    @Test
    public void marginalOverlap() {
        var one = new RefactoringRegion("x", 10, 5);
        var two = new RefactoringRegion("x", 15, 5);

        Assertions.assertTrue(one.overlaps(two));
        Assertions.assertTrue(two.overlaps(one));
    }

    /**
     *  o-------o
     *              o---------o
     */
    @Test
    public void noOverlap() {
        var one = new RefactoringRegion("x", 10, 5);
        var two = new RefactoringRegion("x", 17, 5);

        Assertions.assertFalse(one.overlaps(two));
        Assertions.assertFalse(two.overlaps(one));
    }
}

