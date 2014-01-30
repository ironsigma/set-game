package com.izylab.set.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import com.izylab.set.model.Card;
import com.izylab.set.model.Deck;

public class SetDeck implements Deck {
	protected List<Card> cardList = new ArrayList<Card>();
	protected Deck.Type type;
	protected int index = 0;
	protected Card.Coloring coloring;
	
	public SetDeck(Deck.Type type) {
		this.type = type;
		for ( Card.Coloring color : Card.Coloring.values() ) {
			for (Card.Shape shape : Card.Shape.values()) {
				for (Card.Shade fill : Card.Shade.values()) {
					for ( int count = 1; count < 4; count ++) {
						cardList.add(new SetCard(count, color, shape, fill));
					}
				}
			}
		}
		Collections.shuffle(cardList);
	}

	@Override
	public int getCount() {
		return type.getCount();
	}
	
	@Override
	public boolean hasNext() {
		return index < type.getCount();
	}

	@Override
	public Card getNext() {
		try {
			Card card = cardList.get(index++);
			if ( type == Type.TINY ) {
				if ( coloring == null ) {
					coloring = card.getColor();
				} else {
					while ( coloring != card.getColor() ) {
						card = cardList.get(index++);
					}
				}
			}
			return card;
		} catch ( IndexOutOfBoundsException ex ) {
			throw new NoSuchElementException();
		}
	}

}
