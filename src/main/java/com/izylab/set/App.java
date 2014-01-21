package com.izylab.set;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

class Card extends JComponent {
	private static final long serialVersionUID = 1L;
	public enum Color { RED, GREEN, BLUE }
	public enum Shape { CIRCLE, SQUARE, TRIANGLE }
	public enum Fill  { SOLID, OUTLINE, SHADED }

	protected final int WIDTH = 60;
	protected final int HEIGHT = 60;
	protected final int SPACING = 10;
	
	protected final static BasicStroke stroke = new BasicStroke(4);

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
		g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		switch ( color ) {
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
		
		switch ( fill ) {
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

		for ( int i = 0; i < count; i ++ ) {
			switch ( shape ) {
			case CIRCLE:
				if ( fill == Fill.OUTLINE) {
					g2d.drawOval(SPACING + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
				} else {
					g2d.fillOval(SPACING + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
				}
				break;
				
			case SQUARE:
				if ( fill == Fill.OUTLINE) {
					g2d.drawRect(SPACING + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
				} else {
					g2d.fillRect(SPACING + i * (WIDTH + SPACING), SPACING, WIDTH, HEIGHT);
				}
				break;
				
			case TRIANGLE:
				if ( fill == Fill.OUTLINE) {
					g2d.drawPolygon(
							new int[] {
									SPACING + ((WIDTH + SPACING) * i) + WIDTH / 2,
									SPACING + ((WIDTH + SPACING) * i),
									SPACING + ((WIDTH + SPACING) * i) + WIDTH
								},
							new int[] {
										SPACING,
										SPACING + HEIGHT,
										SPACING + HEIGHT
								} , 3);
				} else {
					g2d.fillPolygon(
							new int[] {
									SPACING + ((WIDTH + SPACING) * i) + WIDTH / 2,
									SPACING + ((WIDTH + SPACING) * i),
									SPACING + ((WIDTH + SPACING) * i) + WIDTH
								},
							new int[] {
										SPACING,
										SPACING + HEIGHT,
										SPACING + HEIGHT
								} , 3);
				}
			}
		}

		g2d.dispose();
	}

	@Override
	public boolean equals(Object card) {
		Card c = (Card)card; 
		return color == c.color && shape == c.shape && count == c.count && fill == c.fill;
	}

	public String toString() {
		return String.format("%d %s %s %s", count, color.toString(), fill.toString(), shape.toString());
	}
}

class Deck {
	public enum Type { FULL, HALF, EASY }
	protected List<Card> cardList = new ArrayList<Card>();
	
	protected static Deck buildDeck(Type type) {
		Deck deck = new Deck();
		for (Card.Color color : Card.Color.values()) {
			if ( type == Type.EASY ) {
				List<Card.Color> colors = Arrays.asList(Card.Color.values());
				Collections.shuffle(colors);
				color = colors.get(0);
			}
			for (Card.Shape shape : Card.Shape.values()) {
				for (Card.Fill fill : Card.Fill.values()) {
					for ( int count = 1; count < 4; count ++) {
						deck.cardList.add(new Card(count, color, shape, fill));
					}
				}
			}
			if ( type == Type.EASY ) {
				break;
			}
		}
		Collections.shuffle(deck.cardList);
		if ( type == Type.HALF ) {
			deck.cardList.subList(42, deck.cardList.size()).clear();
		}
		return deck;
	}
	
	public List<Card> getList() {
		return cardList;
	}
	
	public void shuffle() {
		Collections.shuffle(cardList);
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(cardList.size());
		sb.append(" cards in deck:\n");
		for ( Card card : cardList ) {
			sb.append("\t");
			sb.append(card.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}

class Solution {
	protected Card c1;
	protected Card c2;
	protected Card c3;

	public Solution(Card c1, Card c2, Card c3) {
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
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
		( ( c1.getFill() == c2.getFill() && c2.getFill() == c3.getFill() ) ||
			( c1.getFill() != c2.getFill() && c2.getFill() != c3.getFill() && c1.getFill() != c3.getFill() ) );
	}

	@Override
	public boolean equals(Object solution) {
		Solution s = (Solution)solution;
		return ( s.c1.equals(c1) || s.c1.equals(c2) || s.c1.equals(c3) ) &&
				( s.c2.equals(c1) || s.c2.equals(c2) || s.c2.equals(c3) ) &&
				( s.c3.equals(c1) || s.c3.equals(c2) || s.c3.equals(c3) );
	}
}

public class App extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(App.class);

	protected List<Card> hand = new ArrayList<Card>();
	protected List<Solution> solutions = new ArrayList<Solution>();
	protected Deck deck;
	
	public void run() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(700, 350));
		getContentPane().add(panel);

		deck = Deck.buildDeck(Deck.Type.EASY);
		
		Iterator<Card> itr = deck.getList().iterator();
		for ( int i = 0; i < 12; i ++ ) {
			Card card = itr.next();
			hand.add(card);
			panel.add(card);
			//log.info(card);
		}
		
		for ( int i = 0; i < 12; i ++ ) {
			for ( int j = 0; j < 12; j ++) {
				if ( i == j ) continue;
				for ( int k = 0; k < 12; k ++ ) {
					if ( i == k || k == j ) continue;

					Solution sol = new Solution(
							deck.getList().get(i), 
							deck.getList().get(j),
							deck.getList().get(k));

					if ( sol.isValid() && !solutions.contains(sol) ) {
						solutions.add(sol);
					}
				}
				
			}
		}
		
		log.info(String.format("Found %d solutins", solutions.size()));
		for ( Solution s : solutions ) {
			log.info(s);
		}

		setTitle("Set");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args) {
		new App().run();
	}

}
