package com.izylab.set.model;

/**
 * Deck interface.
 */
public interface Deck {
    /** Deck type. */
    public enum Type {
        /** Full deck, 81 cards. */
        FULL(81),
        /** Half deck, 42 cards. */
        HALF(42),
        /** Tiny deck 21 cards. */
        TINY(21);

        /** Number of cards in deck. */
        private int cardCount;

        /**
         * Constructor.
         * @param count count.
         */
        Type(final int count) {
            this.cardCount = count;
        }

        /**
         * Get Card count.
         * @return count
         */
        public int getCount() {
            return cardCount;
        }
    }

    /**
     * Get number of cards in the deck.
     * @return card count
     */
    int getCount();

    /**
     *  Get the next card in the deck.
     *  @return next card
     */
    Card getNext();

    /**
     * Check for available card.
     * @return true if card is available, false otherwise.
     */
    boolean hasNext();
}
