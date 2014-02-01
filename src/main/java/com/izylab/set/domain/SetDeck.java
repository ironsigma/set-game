package com.izylab.set.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import com.izylab.set.model.Card;
import com.izylab.set.model.Deck;

/**
 * Set Deck.
 */
public final class SetDeck implements Deck {
    /** Max number of shapes per card. */
    private static final int MAX_SHAPES = 3;
    /** Card list. */
    private List<Card> cardList = new ArrayList<Card>();
    /** Deck type. */
    private Deck.Type deckType;
    /** Iterator index. */
    private int index = 0;
    /** Color for easy deck. */
    private Card.Coloring coloring;

    /**
     * Set deck type.
     * @param type Type
     */
    public SetDeck(final Deck.Type type) {
        this.deckType = type;
        for (Card.Coloring color : Card.Coloring.values()) {
            for (Card.Shape shape : Card.Shape.values()) {
                for (Card.Shade fill : Card.Shade.values()) {
                    for (int count = 1; count <= MAX_SHAPES; count++) {
                        cardList.add(new SetCard(count, color, shape, fill));
                    }
                }
            }
        }
        Collections.shuffle(cardList);
    }

    @Override
    public int getCount() {
        return deckType.getCount();
    }

    @Override
    public boolean hasNext() {
        return index < deckType.getCount();
    }

    @Override
    public Card getNext() {
        try {
            Card card = cardList.get(index++);
            if (deckType == Type.TINY) {
                if (coloring == null) {
                    coloring = card.getColor();
                } else {
                    while (coloring != card.getColor()) {
                        card = cardList.get(index++);
                    }
                }
            }
            return card;
        } catch (IndexOutOfBoundsException ex) {
            throw new NoSuchElementException();
        }
    }

}
