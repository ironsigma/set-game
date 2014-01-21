package com.izylab.set;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class App extends JFrame implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(App.class);
	protected enum DeckType { FULL, HALF, EASY }
	

	protected List<Card> cardList = new ArrayList<Card>();
	protected Iterator<Card> cardIterator;
	protected List<Card> hand = new ArrayList<Card>();
	protected List<Card> selectedCardList = new ArrayList<Card>(3);
	protected JPanel cardPanel;
	protected JLabel statusLabel = new JLabel();
	protected int cardsLeft;
	
	public void run() {
		JPanel statusBar = new JPanel();
		statusBar.add(statusLabel);
		
		cardPanel = new JPanel();
		cardPanel.setPreferredSize(new Dimension(700, 350));
		getContentPane().add(cardPanel, BorderLayout.CENTER);
		getContentPane().add(statusBar, BorderLayout.SOUTH);

		cardList = buildDeck(DeckType.HALF);
		cardIterator = cardList.iterator();
		for ( Card c : cardList ) {
			c.addMouseListener(this);
		}
		
		for ( int i = 0; i < 12; i ++ ) {
			Card card = cardIterator.next();
			hand.add(card);
			cardPanel.add(card);
			//log.info(card);
		}

		cardsLeft = cardList.size();
		
		findSolutions();
		setTitle("SET");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	protected int findSolutions() {
		List<Solution> solutions = new ArrayList<Solution>();
		Component[] cards = cardPanel.getComponents();
		for ( int i = 0; i < cards.length; i ++ ) {
			for ( int j = 0; j < cards.length; j ++) {
				if ( i == j ) continue;
				for ( int k = 0; k < cards.length; k ++ ) {
					if ( i == k || k == j ) continue;

					Solution sol = new Solution(
							(Card)cards[i], 
							(Card)cards[j],
							(Card)cards[k]);

					if ( sol.isValid() && !solutions.contains(sol) ) {
						solutions.add(sol);
					}
				}
				
			}
		}
		
		statusLabel.setText(String.format("%d set available, %d cards left", solutions.size(), cardsLeft));
		//log.info(statusLabel.getText());
//		for ( Solution s : solutions ) {
//			log.info(s);
//		}
		return solutions.size();
	}
	
	protected void selectCard(Card card) {
		if ( card.getSelected() ) {
			card.setSelected(false);
			selectedCardList.remove(card);
			return;
		}

		card.setSelected(true);
		selectedCardList.add(card);
		if ( selectedCardList.size() == 3 ) {
			Solution sol = new Solution();
			for ( Card c : selectedCardList ) {
				c.setSelected(false);
				sol.add(c);
			}
			
			if ( sol.isValid() ) {
				//log.info("That's correct!");
				Component[] components = cardPanel.getComponents();

				for ( Card c : selectedCardList ) {
					for ( int i = 0; i < components.length; i ++ ) {
						if ( components[i] == c ) {
							cardPanel.remove(c);
							cardsLeft --;
							try {
								cardPanel.add(cardIterator.next(), i);
							} catch ( NoSuchElementException ex ) {
								// TODO: add empty card??
							}
						}
					}
				}
				
				cardPanel.revalidate();
				cardPanel.repaint();
				
				findSolutions();

			} else {
				JOptionPane.showMessageDialog(this, "Not a vaid SET", "SET", JOptionPane.WARNING_MESSAGE);
				//log.info("Invalid");
			}

			selectedCardList.clear();
		}
	}

	protected static List<Card> buildDeck(DeckType type) {
		List<Card> cardList = new ArrayList<Card>();
		for (Card.Color color : Card.Color.values()) {
			if ( type == DeckType.EASY ) {
				List<Card.Color> colors = Arrays.asList(Card.Color.values());
				Collections.shuffle(colors);
				color = colors.get(0);
			}
			for (Card.Shape shape : Card.Shape.values()) {
				for (Card.Fill fill : Card.Fill.values()) {
					for ( int count = 1; count < 4; count ++) {
						cardList.add(new Card(count, color, shape, fill));
					}
				}
			}
			if ( type == DeckType.EASY ) {
				break;
			}
		}
		Collections.shuffle(cardList);
		if ( type == DeckType.HALF ) {
			cardList.subList(42, cardList.size()).clear();
		}
		return cardList;
	}

	@Override public void mouseClicked(MouseEvent e) { }
	@Override public void mousePressed(MouseEvent e) { }
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) {
		Component c = e.getComponent();
		if ( c instanceof Card ) {
			selectCard((Card)c);
		}
	}

	public static void main(String[] args) {
		new App().run();
	}

}
