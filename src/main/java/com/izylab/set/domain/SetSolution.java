package com.izylab.set.domain;

import com.izylab.set.model.Card;

public class SetSolution {
	protected Card c1;
	protected Card c2;
	protected Card c3;

	public SetSolution() {
		/* empty */
	}

	public SetSolution(Card c1, Card c2, Card c3) {
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
	}
	
	public void add(Card c) {
		if ( c1 == null ) c1 = c;
		else if ( c2 == null ) c2 = c;
		else if ( c3 == null ) c3 = c;
	}
	
	public void remove(Card c) {
		if ( c1 == c ) c1 = null;
		else if ( c2 == c ) c2 = null;
		else if ( c3 == c ) c3 = null;
	}
	
	public void clear() {
		c1 = c2 = c3 = null;
	}
	
	public int getCount() {
		if ( c1 == null ) return 0;
		if ( c2 == null ) return 1;
		if ( c3 == null ) return 2;
		return 3;
	}
	
	@Override
	public String toString() {
		return String.format("[%s] [%s] [%s]", c1.toString(), c2.toString(), c3.toString());
	}

	public boolean isValid() {
		return
		( ( c1.getColor() == c2.getColor() && c2.getColor() == c3.getColor() ) ||
			( c1.getColor() != c2.getColor() && c2.getColor() != c3.getColor() && c1.getColor() != c3.getColor() ) ) &&
		( ( c1.getShape() == c2.getShape() && c2.getShape() == c3.getShape() ) ||
			( c1.getShape() != c2.getShape() && c2.getShape() != c3.getShape() && c1.getShape() != c3.getShape() ) ) &&
		( ( c1.getCount() == c2.getCount() && c2.getCount() == c3.getCount() ) ||
			( c1.getCount() != c2.getCount() && c2.getCount() != c3.getCount() && c1.getCount() != c3.getCount() ) ) &&
		( ( c1.getShade() == c2.getShade() && c2.getShade() == c3.getShade() ) ||
			( c1.getShade() != c2.getShade() && c2.getShade() != c3.getShade() && c1.getShade() != c3.getShade() ) );
	}

	@Override
	public boolean equals(Object solution) {
		SetSolution s = (SetSolution)solution;
		return ( s.c1.equals(c1) || s.c1.equals(c2) || s.c1.equals(c3) ) &&
				( s.c2.equals(c1) || s.c2.equals(c2) || s.c2.equals(c3) ) &&
				( s.c3.equals(c1) || s.c3.equals(c2) || s.c3.equals(c3) );
	}
}