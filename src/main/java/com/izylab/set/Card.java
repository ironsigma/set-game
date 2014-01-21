package com.izylab.set;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

class Card extends JComponent {
	private static final long serialVersionUID = 1L;

	public enum Color {
		RED, GREEN, BLUE
	}

	public enum Shape {
		CIRCLE, SQUARE, TRIANGLE
	}

	public enum Fill {
		SOLID, OUTLINE, SHADED
	}

	protected final int WIDTH = 60;
	protected final int HEIGHT = 60;
	protected final int SPACING = 10;

	protected final static BasicStroke stroke = new BasicStroke(4);
	protected final static java.awt.Color selectedColor = new java.awt.Color(255, 255, 0, 128);

	protected boolean selected;
	protected int count;
	protected Color color;
	protected Shape shape;
	protected Fill fill;

	public Card(int count, Color color, Shape shape, Fill fill) {
		this.count = count;
		this.color = color;
		this.shape = shape;
		this.fill = fill;
	}

	public int getCount() {
		return count;
	}

	public Color getColor() {
		return color;
	}

	public Shape getShape() {
		return shape;
	}

	public Fill getFill() {
		return fill;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH * 3 + SPACING * 4, HEIGHT + SPACING * 2);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		GradientPaint paint;
		java.awt.Color shapeColor = java.awt.Color.black;

		if (selected) {
			g2d.setColor(selectedColor);
			g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		} else {
			g2d.setColor(java.awt.Color.white);
			g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		}

		g2d.setColor(java.awt.Color.black);
		g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		switch (color) {
		case RED:
			shapeColor = java.awt.Color.red;
			break;

		case BLUE:
			shapeColor = java.awt.Color.blue;
			break;

		case GREEN:
			shapeColor = java.awt.Color.green;
			break;
		}

		switch (fill) {
		case SHADED:
			paint = new GradientPaint(5, 5, shapeColor, 5, 8, java.awt.Color.white, true);
			g2d.setPaint(paint);
			break;

		case OUTLINE:
			g2d.setStroke(stroke);
			// fall through

		case SOLID:
			g2d.setColor(shapeColor);
			break;
		}

		int leading = SPACING;
		switch (count) {
		case 1:
			leading = SPACING * 2 + WIDTH;
			break;
		case 2:
			leading = SPACING + ((WIDTH + SPACING) / 2);
			break;
		}

		for (int i = 0; i < count; i++) {
			switch (shape) {
			case CIRCLE:
				if (fill == Fill.OUTLINE) {
					g2d.drawOval(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
				} else {
					g2d.fillOval(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
				}
				break;

			case SQUARE:
				if (fill == Fill.OUTLINE) {
					g2d.drawRect(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
				} else {
					g2d.fillRect(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
				}
				break;

			case TRIANGLE:
				if (fill == Fill.OUTLINE) {
					g2d.drawPolygon(new int[] { leading + ((WIDTH + SPACING) * i) + WIDTH / 2,
							leading + ((WIDTH + SPACING) * i), leading + ((WIDTH + SPACING) * i) + WIDTH }, new int[] {
							SPACING, SPACING + HEIGHT, SPACING + HEIGHT }, 3);
				} else {
					g2d.fillPolygon(new int[] { leading + ((WIDTH + SPACING) * i) + WIDTH / 2,
							leading + ((WIDTH + SPACING) * i), leading + ((WIDTH + SPACING) * i) + WIDTH }, new int[] {
							SPACING, SPACING + HEIGHT, SPACING + HEIGHT }, 3);
				}
			}
		}

		g2d.dispose();
	}

	@Override
	public boolean equals(Object card) {
		if (!(card instanceof Card)) {
			return false;
		}
		Card c = (Card) card;
		return color == c.color && shape == c.shape && count == c.count && fill == c.fill;
	}

	public String toString() {
		return String.format("%d %s %s %s", count, color.toString(), fill.toString(), shape.toString());
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}
}