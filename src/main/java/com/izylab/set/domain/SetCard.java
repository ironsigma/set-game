package com.izylab.set.domain;

import com.izylab.set.model.Card;

public class SetCard implements Card {
	protected int count;
	protected Shape shape;
	protected Shade shade;
	protected Coloring color;


	public SetCard(int count, Coloring color, Shape shape, Shade shade) {
		this.count = count;
		this.color = color;
		this.shape = shape;
		this.shade = shade;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Shape getShape() {
		return shape;
	}

	@Override
	public Coloring getColor() {
		return color;
	}

	@Override
	public Shade getShade() {
		return shade;
	}

}
