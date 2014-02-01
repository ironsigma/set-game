package com.izylab.set.swing;

import javax.swing.JLabel;

/**
 * Status label component.
 */
public final class StatusLabel extends JLabel {
    /** Serial version. */
    private static final long serialVersionUID = 1L;
    /** Number of cards per set. */
    private static final int CARDS_PER_SET = 3;
    /** Number of sets found. */
    private int setsFound;
    /** Number of sets available. */
    private int setsAvailable;
    /** Cards left: Deck + Board. */
    private int cardsLeft;

    /**
     * Set the number of sets found.
     * @param count number found
     */
    public void setFound(final int count) {
        this.setsFound = count;
        updateText();
    }

    /**
     * Increment the number of sets by 1.
     */
    public void incrementSetsFound() {
        this.setsFound++;
        cardsLeft -= CARDS_PER_SET;
        updateText();
    }

    /**
     * Set the number of sets available.
     * @param count number of sets.
     */
    public void setAvailable(final int count) {
        this.setsAvailable = count;
        updateText();
    }

    /**
     * Update the status text.
     */
    private void updateText() {
        setText(String.format("%d sets found, %d sets on board, %d cards left",
                setsFound, setsAvailable, cardsLeft));
    }

}
