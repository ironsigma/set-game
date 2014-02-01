package com.izylab.set.domain;

import com.izylab.set.model.Card;

/**
 * Set Card.
 */
final class SetCard implements Card {
    /** Number of shapes. */
    private int shapeCount;
    /** Card shape. */
    private Shape cardShape;
    /** Card shade. */
    private Shade cardShade;
    /** Card coloring. */
    private Coloring cardColor;

    /**
     * Constructor.
     * @param count Count
     * @param color Coloring
     * @param shape Shape
     * @param shade Shade
     */
    public SetCard(final int count, final Coloring color, final Shape shape,
            final Shade shade) {
        this.shapeCount = count;
        this.cardColor = color;
        this.cardShape = shape;
        this.cardShade = shade;
    }

    @Override
    public int getCount() {
        return shapeCount;
    }

    @Override
    public Shape getShape() {
        return cardShape;
    }

    @Override
    public Coloring getColor() {
        return cardColor;
    }

    @Override
    public Shade getShade() {
        return cardShade;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SetCard other = (SetCard) obj;
        if (cardColor != other.cardColor) {
            return false;
        }
        if (shapeCount != other.shapeCount) {
            return false;
        }
        if (cardShade != other.cardShade) {
            return false;
        }
        if (cardShape != other.cardShape) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Count: %d, Color: %s, Shape: %s, Shade: %s",
                shapeCount, cardColor.toString(), cardShape.toString(),
                cardShade.toString());
    }
}
