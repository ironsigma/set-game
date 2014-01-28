package com.izylab.set.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import com.izylab.set.model.Card;
import com.izylab.set.model.Card.Shade;

class CardPanel extends JComponent {
	private static final long serialVersionUID = 1L;

	protected final int WIDTH = 60;
	protected final int HEIGHT = 60;
	protected final int SPACING = 20;

	protected final static BasicStroke stroke = new BasicStroke(4);
	protected final static Color selectedColor = new Color(255, 255, 0, 128);
	protected final static Color yellowColor = new Color(252, 211, 61);
	protected final static Color greenColor = new Color(126, 167, 116);
	protected final static Color blueColor = new Color(42, 179, 231);
	protected final static Color gradientColor = Color.white;

	protected Card card;
	protected boolean selected = false;
	
	public CardPanel() {
		setVisible(false);
	}
	
	public void setCard(Card card) {
		this.card = card;
	}
	
	public Card getCard() {
		return card;
	}

	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH * 3 + SPACING * 4, HEIGHT + SPACING * 2);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if ( !isVisible() ) return;
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
		
		Color renderColor = null;
		switch ( card.getColor() ) {
		case COLOR1: renderColor = yellowColor; break;
		case COLOR2: renderColor = blueColor; break;
		case COLOR3: renderColor = greenColor; break;
		}

		switch ( card.getShade() ) {
		case SHADE1:
			paint = new GradientPaint(6, 6, gradientColor, 8, 8, renderColor, true);
			g2d.setPaint(paint);
			break;

		case SHADE2:
			g2d.setStroke(stroke);
			g2d.setColor(renderColor);
			break;

		case SHADE3:
			g2d.setColor(renderColor);
			break;
		}

		int leading = SPACING;
		switch ( card.getCount() ) {
		case 1:
			leading = SPACING * 2 + WIDTH;
			break;
		case 2:
			leading = SPACING + ((WIDTH + SPACING) / 2);
			break;
		}

		for (int i = 0; i < card.getCount(); i++) {
			switch ( card.getShape() ) {
			case SHAPE1:
				if ( card.getShade() == Shade.SHADE2) {
					g2d.drawOval(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
					
				} else {
					g2d.fillOval(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
					g2d.setColor(renderColor);
					g2d.setStroke(stroke);
					g2d.drawOval(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
					g2d.setPaint(paint);
				}
				break;

			case SHAPE2:
				if ( card.getShade() == Shade.SHADE2) {
					g2d.drawRect(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);

				} else {
					g2d.fillRect(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
					g2d.setColor(renderColor);
					g2d.setStroke(stroke);
					g2d.drawRect(leading + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
					g2d.setPaint(paint);
				}
				break;

			case SHAPE3:
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

				if ( card.getShade() == Shade.SHADE2) {
					g2d.drawPolygon(xPoints, yPoints, xPoints.length);

				} else {
					g2d.fillPolygon(xPoints, yPoints, xPoints.length);
					g2d.setColor(renderColor);
					g2d.setStroke(stroke);
					g2d.drawPolygon(xPoints, yPoints, xPoints.length);
					g2d.setPaint(paint);
				}
			}
		}

		g2d.dispose();
	}
}