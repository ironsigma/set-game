package com.izylab.set.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import com.izylab.set.model.Card;
import com.izylab.set.model.Card.Shade;

/**
 * Card panel component.
 */
final class CardPanel extends JComponent {
    /** Serial version. */
    private static final long serialVersionUID = 1L;

    /** Gradient x1. */
    private static final int GRADIENT_X1 = 6;
    /** Gradient y1. */
    private static final int GRADIENT_Y1 = 6;
    /** Gradient x2. */
    private static final int GRADIENT_X2 = 8;
    /** Gradient y2. */
    private static final int GRADIENT_Y2 = 6;
    /** Number of cards per row. */
    private static final int NUM_CARDS = 3;
    /** Shape width. */
    private static final int WIDTH = 60;
    /** Shape height. */
    private static final int HEIGHT = 60;
    /** Spacing between shapes. */
    private static final int SPACING = 20;

    /** Shape stroke. */
    private static final BasicStroke STROKE = new BasicStroke(4);
    /** Selected color. */
    private static final Color SELECTED_COLOR = new Color(255, 255, 0, 128);
    /** Yellow color. */
    private static final Color YELLOW_COLOR = new Color(252, 211, 61);
    /** Green color. */
    private static final Color GREEN_COLOR = new Color(126, 167, 116);
    /** Blue color. */
    private static final Color BLUE_COLOR = new Color(42, 179, 231);
    /** Gradient mix color. */
    private static final Color GRADIENT_COLOR = Color.white;

    /** Card. */
    private Card domainCard;
    /** Selection status. */
    private boolean isSelected = false;

    /** Create panel. */
    public CardPanel() {
        setVisible(false);
    }

    /**
     * Set the card.
     * @param card card to set
     */
    public void setCard(final Card card) {
        this.domainCard = card;
    }

    /**
     * Get the card.
     * @return the card
     */
    public Card getCard() {
        return domainCard;
    }

    /**
     * Get selection status.
     * @return status
     */
    public boolean getSelected() {
        return isSelected;
    }

    /**
     * Set selection status.
     * @param selected status
     */
    public void setSelected(final boolean selected) {
        this.isSelected = selected;
        repaint();
    }

    /** Hide and clear card. */
    public void clear() {
        setVisible(false);
        domainCard = null;
    }

    @Override
    public String toString() {
        String color;
        switch (domainCard.getColor()) {
        case COLOR1: color = "Yellow"; break;
        case COLOR2: color = "Blue"; break;
        case COLOR3: color = "Green"; break;
        default: color = "Unknown"; break;
        }
        String shade;
        switch (domainCard.getShade()) {
        case SHADE1: shade = "Shaded"; break;
        case SHADE2: shade = "Outlined"; break;
        case SHADE3: shade = "Solid"; break;
        default: shade = "Unknown"; break;
        }
        String shape;
        switch(domainCard.getShape()) {
        case SHAPE1: shape = "Circle"; break;
        case SHAPE2: shape = "Square"; break;
        case SHAPE3: shape = "Triangle"; break;
        default: shape = "Unknown";
        }
        return String.format("%d %s %s %s",
                domainCard.getCount(), color, shade, shape);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH * NUM_CARDS + SPACING * (NUM_CARDS + 1),
                HEIGHT + SPACING * 2);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        if (!isVisible()) {
            return;
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint paint = null;

        if (isSelected) {
            g2d.setColor(SELECTED_COLOR);
            g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        } else {
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        }

        g2d.setColor(Color.black);
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        Color renderColor;
        switch (domainCard.getColor()) {
        case COLOR1:
            renderColor = YELLOW_COLOR;
            break;
        case COLOR2:
            renderColor = BLUE_COLOR;
            break;
        case COLOR3:
            renderColor = GREEN_COLOR;
            break;
        default:
            renderColor = null;
        }

        switch (domainCard.getShade()) {
        case SHADE1:
            paint = new GradientPaint(GRADIENT_X1, GRADIENT_Y1, GRADIENT_COLOR,
                    GRADIENT_X2, GRADIENT_Y2, renderColor,
                    true);
            g2d.setPaint(paint);
            break;

        case SHADE2:
            g2d.setStroke(STROKE);
            g2d.setColor(renderColor);
            break;

        case SHADE3:
            g2d.setColor(renderColor);
            break;

        default:
            /* empty */
        }

        int leading;
        switch (domainCard.getCount()) {
        case 1:
            leading = SPACING * 2 + WIDTH;
            break;
        case 2:
            leading = SPACING + ((WIDTH + SPACING) / 2);
            break;
        default:
            leading = SPACING;
        }

        for (int i = 0; i < domainCard.getCount(); i++) {
            switch (domainCard.getShape()) {
            case SHAPE1:
                if (domainCard.getShade() == Shade.SHADE2) {
                    g2d.drawOval(leading + i * (WIDTH + SPACING), SPACING,
                            WIDTH, HEIGHT);

                } else {
                    g2d.fillOval(leading + i * (WIDTH + SPACING), SPACING,
                            WIDTH, HEIGHT);
                    g2d.setColor(renderColor);
                    g2d.setStroke(STROKE);
                    g2d.drawOval(leading + i * (WIDTH + SPACING), SPACING,
                            WIDTH, HEIGHT);
                    g2d.setPaint(paint);
                }
                break;

            case SHAPE2:
                if (domainCard.getShade() == Shade.SHADE2) {
                    g2d.drawRect(leading + i * (WIDTH + SPACING), SPACING,
                            WIDTH, HEIGHT);

                } else {
                    g2d.fillRect(leading + i * (WIDTH + SPACING), SPACING,
                            WIDTH, HEIGHT);
                    g2d.setColor(renderColor);
                    g2d.setStroke(STROKE);
                    g2d.drawRect(leading + i * (WIDTH + SPACING), SPACING,
                            WIDTH, HEIGHT);
                    g2d.setPaint(paint);
                }
                break;

            case SHAPE3:
                int[] xPoints = new int[] {
                        leading + ((WIDTH + SPACING) * i) + WIDTH / 2,
                        leading + ((WIDTH + SPACING) * i),
                        leading + ((WIDTH + SPACING) * i) + WIDTH, };

                int[] yPoints = new int[] {SPACING, SPACING + HEIGHT,
                        SPACING + HEIGHT};

                if (domainCard.getShade() == Shade.SHADE2) {
                    g2d.drawPolygon(xPoints, yPoints, xPoints.length);

                } else {
                    g2d.fillPolygon(xPoints, yPoints, xPoints.length);
                    g2d.setColor(renderColor);
                    g2d.setStroke(STROKE);
                    g2d.drawPolygon(xPoints, yPoints, xPoints.length);
                    g2d.setPaint(paint);
                }

            default:
                /* unknown shape */
            }
        }

        g2d.dispose();
    }
}
