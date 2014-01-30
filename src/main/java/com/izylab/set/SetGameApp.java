package com.izylab.set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.izylab.set.domain.SetDeck;
import com.izylab.set.model.Card;
import com.izylab.set.model.Deck;
import com.izylab.set.swing.BoardPanel;
import com.izylab.set.swing.BoardPanelListener;

public class SetGameApp extends JFrame implements BoardPanelListener {
	private static final Logger log = Logger.getLogger(SetGameApp.class);
	private static final long serialVersionUID = 1L;
	protected BoardPanel boardPanel;
	
	public void run() {
		boardPanel = new BoardPanel(new SetDeck(Deck.Type.FULL));
		getContentPane().add(boardPanel);
		boardPanel.addListener(this);
		boardPanel.setupBoard();

		setTitle("SET");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		//setLocationRelativeTo(null);
		setLocation(550, 1350);
		setVisible(true);
	}

	public static void main(String[] args) {
		new SetGameApp().run();
	}

	@Override
	public void solutionFound(Card card1, Card card2, Card card3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invalidSolution(Card card1, Card card2, Card card3) {
		JOptionPane.showMessageDialog(this, "Not a vaid SET", "SET", JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void solutionCountUpdate(int count) {
		log.info(count + " set(s) available");
	}

	@Override
	public void noMoreSolutions() {
			JOptionPane.showMessageDialog(this, "No more sets!", "SET", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void cardsDealt(int cardsLeft) {
		// TODO Auto-generated method stub
		
	}

}
