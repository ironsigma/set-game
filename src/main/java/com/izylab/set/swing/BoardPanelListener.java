package com.izylab.set.swing;

import com.izylab.set.model.Card;
import com.izylab.set.model.Deck;

/**
 * Board listener interface.
 */
public interface BoardPanelListener {

    /**
     * Board setup is complete.
     * @param deck Deck used for setup
     */
    void setupComplete(Deck deck);

    /**
     * Solution was found by user.
     * @param card1 Card one
     * @param card2 Card two
     * @param card3 Card three
     */
    void solutionFound(Card card1, Card card2, Card card3);

    /**
     * Invalid solution was selected.
     * @param card1 Card one
     * @param card2 Card two
     * @param card3 Card three
     */
    void invalidSolution(Card card1, Card card2, Card card3);

    /**
     * Number of solutions on board changed.
     * @param count Number of solutions
     */
    void solutionCountUpdate(int count);

    /**
     * No more solutions available.
     */
    void noMoreSolutions();

}
