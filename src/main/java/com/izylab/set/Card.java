package com.izylab.set;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

class Card extends JComponent {
	private static final long serialVersionUID = 1L;

	public enum Coloring {
		YELLOW(252, 211, 61),
		GREEN(126, 167, 116),
		BLUE(42, 179, 231);

		protected Color color;
		Coloring(int r, int g, int b) {
			color = new Color(r, g, b);
		}
		public Color getColor() {
			return color;
		}
	}

	public enum Shape {
		CIRCLE, SQUARE, TRIANGLE
	}

	public enum Fill {
		SOLID, OUTLINE, SHADED
	}

	protected final int WIDTH = 60;
	protected final int HEIGHT = 60;
	protected final int SPACING = 20;

	protected final static BasicStroke stroke = new BasicStroke(4);
	protected final static Color selectedColor = new Color(255, 255, 0, 128);
	protected final static Color gradientColor = Color.white;

	protected boolean selected;
	protected int count;
	protected Coloring color;
	protected Shape shape;
	protected Fill fill;

	public Card(int count, Coloring color, Shape shape, Fill fill) {
		this.count = count;
		this.color = color;
		this.shape = shape;
		this.fill = fill;
	}

	public int getCount() {
		return count;
	}

	public Coloring getColor() {
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
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

		GradientPaint paint = null;

		if (selected) {
			g2d.setColor(selectedColor);
			g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		} else {
			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		}

		g2d.setColor(Color.black);
		g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		switch (fill) {
		case SHADED:
			paint = new GradientPaint(6, 6, gradientColor, 8, 8, color.getColor(), true);
			g2d.setPaint(paint);
			break;

		case OUTLINE:
			g2d.setStroke(stroke);
			g2d.setColor(color.getColor());
			break;

		case SOLID:
			g2d.setColor(color.getColor());
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
					g2d.setColor(color.getColor());
					g2d.setStroke(stroke);
					g2d.drawOval(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
					g2d.setPaint(paint);
				}
				break;

			case SQUARE:
				if (fill == Fill.OUTLINE) {
					g2d.drawRect(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);

				} else {
					g2d.fillRect(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
					g2d.setColor(color.getColor());
					g2d.setStroke(stroke);
					g2d.drawRect(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
					g2d.setPaint(paint);
				}
				break;

			case TRIANGLE:
				int[] xPoints = new int[] {
						leading + ((WIDTH + SPACING) * i) + WIDTH / 2,
						leading + ((WIDTH + SPACING) * i),
						leading + ((WIDTH + SPACING) * i) + WIDTH,
					};

				int[] yPoints = new int[] {
						SPACING,
						SPACING + HEIGHT,
						SPACING + HEIGHT,
					};

				if (fill == Fill.OUTLINE) {
					g2d.drawPolygon(xPoints, yPoints, xPoints.length);

				} else {
					g2d.fillPolygon(xPoints, yPoints, xPoints.length);
					g2d.setColor(color.getColor());
					g2d.setStroke(stroke);
					g2d.drawPolygon(xPoints, yPoints, xPoints.length);
					g2d.setPaint(paint);
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