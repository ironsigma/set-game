package com.izylab.set;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

class Deck implements Iterable<Card> {
	public enum Type {
		FULL(81),
		HALF(42),
		TINY(21);
		
		int count;
		Type(int count) {
			this.count = count;
		}
		public int getCount() {
			return count;
		}
	}

	protected List<Card> cardList = new ArrayList<Card>();
	protected Type type;

	public Deck(Type type) {
		this.type = type;
		for ( Card.Coloring color : Card.Coloring.values() ) {
			for (Card.Shape shape : Card.Shape.values()) {
				for (Card.Fill fill : Card.Fill.values()) {
					for ( int count = 1; count < 4; count ++) {
						cardList.add(new Card(count, color, shape, fill));
					}
				}
			}
		}
		Collections.shuffle(cardList);
	}
	
	public int getCount() {
		return type.getCount();
	}
	
	public void addMouseListener(MouseListener listener) {
		for ( Card card : this ) {
			card.addMouseListener(listener);
		}
	}

	@Override
	public Iterator<Card> iterator() {
		return new DeckIterator();
	}
	
	class DeckIterator implements Iterator<Card> {
		protected int index = 0;
		protected Card.Coloring coloring;

		@Override
		public boolean hasNext() {
			return index < type.getCount();
		}

		@Override
		public Card next() {
			try {
				Card card = cardList.get(index++);
				if ( type == Type.TINY ) {
					if ( coloring == null ) {
						coloring = card.color;
					} else {
						while ( coloring != card.color ) {
							card = cardList.get(index++);
						}
					}
				}
				return card;
			} catch ( IndexOutOfBoundsException ex ) {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
}