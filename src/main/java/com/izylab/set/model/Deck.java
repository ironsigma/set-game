package com.izylab.set.model;

public interface Deck {
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

	int getCount();
	Card getNext();
	boolean hasNext();
}
