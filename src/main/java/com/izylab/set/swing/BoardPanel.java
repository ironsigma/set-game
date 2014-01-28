package com.izylab.set.swing;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.izylab.set.model.Deck;
import com.izylab.set.domain.SetSolution;

public class BoardPanel extends JPanel implements MouseListener {
	private static Logger log = Logger.getLogger(BoardPanel.class);
	private static final long serialVersionUID = 1L;
	private static final int MAX_NUM_CARDS = 15;
	private static final int NUM_CARDS = 12;

	protected CardPanel[] cardList = new CardPanel[MAX_NUM_CARDS];
	protected SetSolution solution = new SetSolution();
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
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if ( e.getSource() instanceof CardPanel ) {
			CardPanel cardPanel = (CardPanel)e.getSource();
			boolean selected = !cardPanel.getSelected();
			cardPanel.setSelected(selected);
			if ( !selected ) {
				solution.remove(cardPanel.getCard());
			}
			solution.add(cardPanel.getCard());
			checkSolution();
		}
	}

	@Override public void mouseClicked(MouseEvent e) { }
	@Override public void mousePressed(MouseEvent e) { }
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) { }

}
