package com.izylab.set.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.izylab.set.model.Card;
import com.izylab.set.model.Deck;

public class BoardPanel extends JPanel implements MouseListener {
	private static Logger log = Logger.getLogger(BoardPanel.class);
	private static final long serialVersionUID = 1L;
	private static final int MAX_NUM_CARDS = 15;
	private static final int NUM_CARDS = 12;

	protected CardPanel[] cardList = new CardPanel[MAX_NUM_CARDS];
	protected SolutionSet solution = new SolutionSet();
	protected Deck deck;
	
	public BoardPanel(Deck deck) {
		super();
		this.deck = deck;

		// Create all the cards on the board
		log.debug("Creating card components");
		for ( int i = 0; i < MAX_NUM_CARDS; i ++ ) {
			cardList[i] = new CardPanel();
			cardList[i].addMouseListener(this);
			add(cardList[i]);
		}

		// TODO: calculate from cards
		setPreferredSize(new Dimension(800, 532));
	}

	public void setupBoard() {
		for ( int i = 0; i < NUM_CARDS; i ++ ) {
			cardList[i].setCard(deck.getNext());
			cardList[i].setVisible(true);
		}
		countSolutions();
	}

	public void clearBoard() {
		for ( int i = 0; i < MAX_NUM_CARDS; i ++ ) {
			cardList[i].setVisible(false);
		}
	}
	
	protected void checkSolution() {
		if ( solution.getCount() != 3 ) {
			return ;
		}

		if ( !solution.isValid() ) {
			JOptionPane.showMessageDialog(this, "Not a vaid SET", "SET", JOptionPane.WARNING_MESSAGE);
			log.info("Invalid set selected");
			solution.clear();
			return;
		}

		log.info("Found solution");
		solution.clear();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if ( e.getSource() instanceof CardPanel ) {
			CardPanel cardPanel = (CardPanel)e.getSource();
			if ( cardPanel.getSelected() ) {
				solution.remove(cardPanel);
			}
			solution.add(cardPanel);
			checkSolution();
		}
	}

	@Override public void mouseClicked(MouseEvent e) { }
	@Override public void mousePressed(MouseEvent e) { }
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) { }

	protected int countSolutions() {
		List<SolutionSet> solutions = new ArrayList<SolutionSet>();
		Component[] cards = getComponents();
		for ( int i = 0; i < cards.length; i ++ ) {
			if ( ((CardPanel)cards[i]).getCard() == null ) continue;
			for ( int j = 0; j < cards.length; j ++) {
				if ( i == j || ((CardPanel)cards[j]).getCard() == null ) continue;
				for ( int k = 0; k < cards.length; k ++ ) {
					if ( i == k || k == j || ((CardPanel)cards[k]).getCard() == null ) continue;

					SolutionSet sol = new SolutionSet(
							(CardPanel)cards[i], 
							(CardPanel)cards[j],
							(CardPanel)cards[k]);

					if ( sol.isValid() && !solutions.contains(sol) ) {
						solutions.add(sol);
					}
				}
				
			}
		}
		
		log.debug(String.format("%d set available", solutions.size()));
		return solutions.size();
	}

	class SolutionSet {
		protected CardPanel cardPanel1;
		protected CardPanel cardPanel2;
		protected CardPanel cardPanel3;
		
		public SolutionSet() {
			/* empty */
		}

		public SolutionSet(CardPanel c1, CardPanel c2, CardPanel c3) {
			cardPanel1 = c1;
			cardPanel2 = c2;
			cardPanel3 = c3;
		}

		public void add(CardPanel c) {
			if ( cardPanel1 == null ) cardPanel1 = c;
			else if ( cardPanel2 == null ) cardPanel2 = c;
			else if ( cardPanel3 == null ) cardPanel3 = c;
			else throw new IndexOutOfBoundsException();
			c.setSelected(true);
		}
		
		public void remove(CardPanel c) {
			if ( cardPanel1 == c ) cardPanel1 = null;
			else if ( cardPanel2 == c ) cardPanel2 = null;
			else if ( cardPanel3 == c ) cardPanel3 = null;
			else throw new NoSuchElementException();
			c.setSelected(false);
		}
		
		public void clear() {
			cardPanel1.setSelected(false);
			cardPanel2.setSelected(false);
			cardPanel3.setSelected(false);
			cardPanel1 = cardPanel2 = cardPanel3 = null;
		}
		
		public int getCount() {
			if ( cardPanel1 == null ) return 0;
			if ( cardPanel2 == null ) return 1;
			if ( cardPanel3 == null ) return 2;
			return 3;
		}

		public boolean isValid() {
			Card card1 = cardPanel1.getCard();
			Card card2 = cardPanel2.getCard();
			Card card3 = cardPanel3.getCard();
			return
			( ( card1.getColor() == card2.getColor() && card2.getColor() == card3.getColor() ) ||
				( card1.getColor() != card2.getColor() && card2.getColor() != card3.getColor() && card1.getColor() != card3.getColor() ) ) &&
			( ( card1.getShape() == card2.getShape() && card2.getShape() == card3.getShape() ) ||
				( card1.getShape() != card2.getShape() && card2.getShape() != card3.getShape() && card1.getShape() != card3.getShape() ) ) &&
			( ( card1.getCount() == card2.getCount() && card2.getCount() == card3.getCount() ) ||
				( card1.getCount() != card2.getCount() && card2.getCount() != card3.getCount() && card1.getCount() != card3.getCount() ) ) &&
			( ( card1.getShade() == card2.getShade() && card2.getShade() == card3.getShade() ) ||
				( card1.getShade() != card2.getShade() && card2.getShade() != card3.getShade() && card1.getShade() != card3.getShade() ) );
		}
		
		@Override
		public String toString() {
			return String.format("[%s] [%s] [%s]",
					cardPanel1.getCard().toString(),
					cardPanel2.getCard().toString(),
					cardPanel3.getCard().toString());
		}

		@Override
		public boolean equals(Object obj) {
			if ( !(obj instanceof SolutionSet) ) {
				return false;
			}
			SolutionSet sol = (SolutionSet)obj;
			Card c1 = cardPanel1.getCard();
			Card c2 = cardPanel2.getCard();
			Card c3 = cardPanel3.getCard();
			Card s1 = sol.cardPanel1.getCard();
			Card s2 = sol.cardPanel2.getCard();
			Card s3 = sol.cardPanel3.getCard();
			return  ( c1.equals(s1) || c1.equals(s2) || c1.equals(s3) ) &&
					( c2.equals(s1) || c2.equals(s2) || c2.equals(s3) ) &&
					( c3.equals(s1) || c3.equals(s2) || c3.equals(s3) );
		}
	}
}
