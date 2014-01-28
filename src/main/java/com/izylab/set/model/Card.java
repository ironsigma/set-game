package com.izylab.set.model;

public interface Card {

	public enum Coloring { COLOR1, COLOR2, COLOR3 }
	public enum Shape    { SHAPE1, SHAPE2, SHAPE3 }
	public enum Shade    { SHADE1, SHADE2, SHADE3 }

	int getCount();
	Shape getShape();
	Coloring getColor();
	Shade getShade();

}
