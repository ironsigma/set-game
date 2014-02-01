package com.izylab.set.model;

/**
 * Card interface.
 */
public interface Card {

    /** Coloring. */
    public enum Coloring {
        /** Color 1. */
        COLOR1,
        /** Color 2. */
        COLOR2,
        /** Color 3. */
        COLOR3
    }

    /** Shape. */
    public enum Shape {
        /** Shape 1. */
        SHAPE1,
        /** Shape 2. */
        SHAPE2,
        /** Shape 3. */
        SHAPE3
    }

    /** Shade. */
    public enum Shade {
        /** Shade 1. */
        SHADE1,
        /** Shade 2. */
        SHADE2,
        /** Shade 3. */
        SHADE3
    }

    /**
     * Get the number of shapes on the card.
     * @return count
     */
    int getCount();

    /**
     * Get the shape of the card.
     * @return shape
     */
    Shape getShape();

    /**
     * Get the color of the card.
     * @return color
     */
    Coloring getColor();

    /**
     * Get the shade of the card.
     * @return shade
     */
    Shade getShade();

}
