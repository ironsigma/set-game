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

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.apache.log4j.Logger;

public class App extends JFrame implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(App.class);
	protected enum DeckType { FULL, HALF, TINY, EASY }
	

	protected List<Card> cardList = new ArrayList<Card>();
	protected Iterator<Card> cardIterator;
	protected List<Card> hand = new ArrayList<Card>();
	protected List<Card> selectedCardList = new ArrayList<Card>(3);
	protected JPanel cardPanel;
	protected JLabel statusLabel = new JLabel();
	protected int cardsLeft;
	protected int setsFound = 0;
	protected long timer;
	protected int score = 0;
	
	public void run() {
		JMenuItem quitMenuItem = new JMenuItem("Quit");

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(quitMenuItem);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);

		JPanel statusBar = new JPanel();
		cardPanel = new JPanel();
		cardPanel.setPreferredSize(new Dimension(800, 532));
		getContentPane().add(cardPanel, BorderLayout.CENTER);
		getContentPane().add(statusBar, BorderLayout.SOUTH);

		cardList = buildDeck(DeckType.TINY);
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

		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		statusBar.add(new JLabel(" "));
		statusBar.add(statusLabel);
		
		setTitle("SET");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		timer = System.currentTimeMillis();
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
		
		statusLabel.setText(String.format("%d set available, %d sets found, %d cards left, %d points scored.",
				solutions.size(), setsFound, cardsLeft, score));
		//log.info(statusLabel.getText());
//		for ( Solution s : solutions ) {
//			log.info(s);
//		}
		return solutions.size();
	}
	
	protected void updateScore() {
		long elapsed = System.currentTimeMillis() - timer;
		log.info("Found set in " + (elapsed / 1000) + " seconds");
		if ( elapsed <= 2000 ) {
			score += 1000;
		} else if ( elapsed < 4000 ) {
			score += 900;
		} else if ( elapsed < 8000 ) {
			score += 800;
		} else if ( elapsed < 16000 ) {
			score += 700;
		} else if ( elapsed < 32000 ) {
			score += 600;
		} else if ( elapsed < 64000 ) {
			score += 500;
		} else if ( elapsed < 128000 ) {
			score += 400;
		} else if ( elapsed < 256000 ) {
			score += 300;
		} else if ( elapsed < 512000 ) {
			score += 200;
		}
		score += 100;
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
				log.info("Selected card: " + c.toString());
				c.setSelected(false);
				sol.add(c);
			}
			
			if ( sol.isValid() ) {
				log.info("Correct SET");

				updateScore();
				setsFound ++;
				Component[] cardsOnBoard = cardPanel.getComponents();
				log.debug(cardsOnBoard.length + " cards on the board");

				for ( Card c : selectedCardList ) {
					for ( int i = 0; i < cardsOnBoard.length; i ++ ) {
						if ( cardsOnBoard[i] == c ) {
							log.debug("Found selected card, removing from board: "+ c.toString());
							cardPanel.remove(c);
							cardsLeft --;
							try {
								if ( cardsOnBoard.length <= 12 ) {
									Card newCard = cardIterator.next();
									log.debug("Adding new card at last position: " + newCard.toString());
									cardPanel.add(newCard, i);
								}
							} catch ( NoSuchElementException ex ) {
								log.debug("No more card in deck");
							}
						}
					}
				}
				
				int solutions;
				while ( (solutions = findSolutions()) == 0 && cardIterator.hasNext() ) {
					log.debug("No sets found with new cards adding three more... ");
					for ( int i = 0; i < 3; i ++ ) {
						try {
							Card newCard = cardIterator.next();
							log.debug("Adding extra card: " + newCard.toString());
							cardPanel.add(newCard);
						} catch ( NoSuchElementException ex ) {
							log.debug("Can't add more, No more card in deck");
							break;
						}
					}
				}

				cardPanel.revalidate();
				cardPanel.repaint();
				
				if ( solutions == 0 ) {
					log.debug("No more sets found");
					JOptionPane.showMessageDialog(this, "No more sets!", "SET", JOptionPane.ERROR_MESSAGE);
				}

			} else {
				JOptionPane.showMessageDialog(this, "Not a vaid SET", "SET", JOptionPane.WARNING_MESSAGE);
				log.info("Invalid set selected");
			}

			selectedCardList.clear();
		}
	}

	protected static List<Card> buildDeck(DeckType type) {
		List<Card> cardList = new ArrayList<Card>();
		for (Card.Coloring color : Card.Coloring.values()) {
			if ( type == DeckType.EASY ) {
				List<Card.Coloring> colors = Arrays.asList(Card.Coloring.values());
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
		if ( type == DeckType.TINY ) {
			cardList.subList(21, cardList.size()).clear();
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
