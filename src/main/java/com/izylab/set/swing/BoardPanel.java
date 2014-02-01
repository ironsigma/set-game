package com.izylab.set.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.izylab.set.model.Card;
import com.izylab.set.model.Deck;

/**
 * Board panel for Set Game.
 */
public final class BoardPanel extends JPanel implements MouseListener {
    /** Logger. */
    private static Logger log = Logger.getLogger(BoardPanel.class);

    /** Serial version. */
    private static final long serialVersionUID = 1L;

    /** Max number of cards on the board. */
    private static final int MAX_NUM_CARDS = 15;

    /** Number of cards on the board. */
    private static final int NUM_CARDS = 12;

    /** Size of a set. */
    private static final int SET_SIZE = 3;

    /** Listener list. */
    private List<BoardPanelListener> listenerList
        = new ArrayList<BoardPanelListener>();

    /** Card display components. */
    private CardPanel[] cardList = new CardPanel[MAX_NUM_CARDS];

    /** Solution of currently selected cards. */
    private SolutionSet solution = new SolutionSet();

    /** Deck of cards. */
    private Deck deck;

    /**
     * Board panel constructor.
     * @param setDeck The deck of cards to use
     */
    public BoardPanel(final Deck setDeck) {
        super();
        deck = setDeck;

        // Create all the cards on the board
        log.debug("Creating card components");
        for (int i = 0; i < MAX_NUM_CARDS; i++) {
            cardList[i] = new CardPanel();
            cardList[i].addMouseListener(this);
            add(cardList[i]);
        }

        // TODO: calculate from cards
        setPreferredSize(new Dimension(800, 532));
    }

    /**
     * Add an event listener.
     * @param listener Listener to receive events
     */
    public void addListener(final BoardPanelListener listener) {
        log.debug("Addign listener: " + listener.toString());
        listenerList.add(listener);
    }

    /** Setup the board for a new game. */
    public void setupBoard() {
        log.debug("Setting up board");
        for (int i = 0; i < NUM_CARDS; i++) {
            cardList[i].setCard(deck.getNext());
            cardList[i].setVisible(true);
        }
        addAdditionalCardsIfRequired();

        for (BoardPanelListener listener : listenerList) {
            listener.setupComplete(deck);
        }
    }

    /** Clear the board. */
    public void clearBoard() {
        log.debug("Clearing board");
        for (int i = 0; i < MAX_NUM_CARDS; i++) {
            cardList[i].setVisible(false);
        }
    }

    /**
     * Add additional cards.
     * This will check to see if there's a solution with the cards that are  on
     * the board. If there's no solution an additional 3 are added until a
     * solution is available or the deck is exhausted.
     */
    private void addAdditionalCardsIfRequired() {
        log.debug("Checking solutions, for posible additinal cards");
        int solutions = 0;
        while (deck.hasNext()) {
            solutions = countSolutions();
            if (solutions == 0) {
                break;
            }
            log.debug("No sets found, adding three more... ");
            for (int i = 0; i < SET_SIZE; i++) {
                if (!deck.hasNext()) {
                    log.debug("No more cards in deck");
                    break;
                }
                Card newCard = deck.getNext();
                boolean assigned = false;
                for (Component component : getComponents()) {
                    if (!component.isVisible()) {
                        ((CardPanel) component).setCard(newCard);
                        component.setVisible(true);
                        log.debug("Adding extra card: " + component.toString());
                        assigned = true;
                        break;
                    }
                }
                if (!assigned) {
                    log.fatal("Wow, ran out of panels to diplay card!");
                }
            }
        }

        if (solutions == 0) {
            log.debug("No more sets found");
            for (BoardPanelListener listener : listenerList) {
                listener.noMoreSolutions();
            }
            return;
        }

        for (BoardPanelListener listener : listenerList) {
            listener.solutionCountUpdate(solutions);
        }

    }

    /**
     * Number of cards on the board.
     * @return The number of cards on the board.
     */
    private int cardsOnBoard() {
        int count = 0;
        for (Component component : getComponents()) {
            if (component.isVisible()) {
                count++;
            }
        }
        return count;
    }

    /** Check to see if selected cards is a valid solution. */
    private void checkSolution() {
        if (solution.getCount() != SET_SIZE) {
            return;
        }

        if (!solution.isValid()) {
            log.debug("Invalid set selected");
            for (BoardPanelListener listener : listenerList) {
                listener.invalidSolution(solution.cardPanels[0].getCard(),
                        solution.cardPanels[1].getCard(),
                        solution.cardPanels[2].getCard());
            }
            solution.clear();
            return;
        }

        log.debug("Found solution");
        for (BoardPanelListener listener : listenerList) {
            listener.solutionFound(solution.cardPanels[0].getCard(),
                    solution.cardPanels[1].getCard(),
                    solution.cardPanels[2].getCard());
        }

        int cardsOnBoard = cardsOnBoard();

        // Add swap for new cards
        log.debug("Swaping set found with new cards");
        if (cardsOnBoard <= NUM_CARDS && deck.hasNext()) {
            solution.cardPanels[0].setCard(deck.getNext());
        } else {
            log.debug("No more cards in deck to replace card 1");
            solution.cardPanels[0].clear();
        }

        if (cardsOnBoard <= NUM_CARDS && deck.hasNext()) {
            solution.cardPanels[1].setCard(deck.getNext());
        } else {
            log.debug("No more cards in deck to replace card 2");
            solution.cardPanels[1].clear();
        }

        if (cardsOnBoard <= NUM_CARDS && deck.hasNext()) {
            solution.cardPanels[2].setCard(deck.getNext());
        } else {
            log.debug("No more cards in deck to replace card 3");
            solution.cardPanels[2].clear();
        }

        solution.clear();
        addAdditionalCardsIfRequired();
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (e.getSource() instanceof CardPanel) {
            CardPanel cardPanel = (CardPanel) e.getSource();
            cardPanel.setSelected(!cardPanel.getSelected());
            if (cardPanel.getSelected()) {
                solution.add(cardPanel);
            } else {
                solution.remove(cardPanel);
            }
            checkSolution();
        }
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mousePressed(final MouseEvent e) {
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }

    /**
     * Count the number of solutions.
     * @return the available solutions with the cards on the board.
     */
    private int countSolutions() {
        List<SolutionSet> solutions = new ArrayList<SolutionSet>();
        Component[] cards = getComponents();
        for (int i = 0; i < cards.length; i++) {
            if (((CardPanel) cards[i]).getCard() == null) {
                continue;
            }
            for (int j = 0; j < cards.length; j++) {
                if (i == j || ((CardPanel) cards[j]).getCard() == null) {
                    continue;
                }
                for (int k = 0; k < cards.length; k++) {
                    if (i == k || k == j
                            || ((CardPanel) cards[k]).getCard() == null) {
                        continue;
                    }

                    SolutionSet sol = new SolutionSet((CardPanel) cards[i],
                            (CardPanel) cards[j], (CardPanel) cards[k]);

                    if (sol.isValid() && !solutions.contains(sol)) {
                        solutions.add(sol);
                    }
                }

            }
        }

        log.debug(String.format("%d set available", solutions.size()));
        for (SolutionSet sol : solutions) {
            log.debug(sol);
        }
        return solutions.size();
    }

    /**
     * Solution set.
     */
    private class SolutionSet {
        /** Max number of cards in solution. */
        private static final int NUM_CARDS = 3;

        /** Card Panels. */
        private CardPanel[] cardPanels = new CardPanel[NUM_CARDS];

        /** Create an empty solution. */
        public SolutionSet() {
            /* empty */
        }

        /**
         * Create a solution with cards.
         * @param c1 Card 1
         * @param c2 Card 2
         * @param c3 Card 3
         */
        public SolutionSet(final CardPanel c1, final CardPanel c2,
                final CardPanel c3) {
            cardPanels[0] = c1;
            cardPanels[1] = c2;
            cardPanels[2] = c3;
        }

        /**
         * Add a card to the solution.
         * @param c Card to add
         */
        public void add(final CardPanel c) {
            for (int i = 0; i < NUM_CARDS; i++) {
                if (cardPanels[i] == null) {
                    cardPanels[i] = c;
                    cardPanels[i].setSelected(true);
                    return;
                }
            }
            throw new IndexOutOfBoundsException();
        }

        /**
         * Remove a card from the solution.
         * @param c Card to remove.
         */
        public void remove(final CardPanel c) {
            for (int i = 0; i < NUM_CARDS; i++) {
                if (cardPanels[i] == c) {
                    cardPanels[i] = null;
                    c.setSelected(false);
                    return;
                }
            }
            throw new NoSuchElementException();
        }

        /** Remove the all the cards from the solution. */
        public void clear() {
            for (int i = 0; i < NUM_CARDS; i++) {
                cardPanels[i].setSelected(false);
                cardPanels[i] = null;
            }
        }

        /**
         * Get number of cards in solution.
         * @return Number of cards
         */
        public int getCount() {
            int count = 0;
            for (int i = 0; i < NUM_CARDS; i++) {
                if (cardPanels[i] != null) {
                    count++;
                }
            }
            return count;
        }

        /**
         * Check to see if a solution is valid.
         * @return true if solution is good, false otherwise.
         */
        public boolean isValid() {
            Card card1 = cardPanels[0].getCard();
            Card card2 = cardPanels[1].getCard();
            Card card3 = cardPanels[2].getCard();
            return ((card1.getColor() == card2.getColor()
                        && card2.getColor() == card3.getColor())

            || (card1.getColor() != card2.getColor()
                    && card2.getColor() != card3.getColor()
                    && card1.getColor() != card3.getColor()))

            && ((card1.getShape() == card2.getShape()
                    && card2.getShape() == card3.getShape())

            || (card1.getShape() != card2.getShape()
                    && card2.getShape() != card3.getShape()
                    && card1.getShape() != card3.getShape()))

            && ((card1.getCount() == card2.getCount()
                    && card2.getCount() == card3.getCount())

            || (card1.getCount() != card2.getCount()
                    && card2.getCount() != card3.getCount()
                    && card1.getCount() != card3.getCount()))

            && ((card1.getShade() == card2.getShade()
                    && card2.getShade() == card3.getShade())

            || (card1.getShade() != card2.getShade()
                    && card2.getShade() != card3.getShade()
                    && card1.getShade() != card3.getShade()));
        }

        @Override
        public String toString() {
            return String.format("[%s] [%s] [%s]", cardPanels[0].toString(),
                    cardPanels[1].toString(), cardPanels[2].toString());
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SolutionSet sol = (SolutionSet) obj;
            if ((cardPanels[0] == null && sol.cardPanels[0] != null)
                || (cardPanels[0] != null && sol.cardPanels[0] == null)
                || (cardPanels[1] == null && sol.cardPanels[1] != null)
                || (cardPanels[1] != null && sol.cardPanels[1] == null)
                || (cardPanels[2] == null && sol.cardPanels[2] != null)
                || (cardPanels[2] != null && sol.cardPanels[2] == null)) {
                return false;
            }
            Card c1 = cardPanels[0].getCard();
            Card c2 = cardPanels[1].getCard();
            Card c3 = cardPanels[2].getCard();
            Card s1 = sol.cardPanels[0].getCard();
            Card s2 = sol.cardPanels[1].getCard();
            Card s3 = sol.cardPanels[2].getCard();
            return (c1.equals(s1) || c1.equals(s2) || c1.equals(s3))
                    && (c2.equals(s1) || c2.equals(s2) || c2.equals(s3))
                    && (c3.equals(s1) || c3.equals(s2) || c3.equals(s3));
        }
    }
}
