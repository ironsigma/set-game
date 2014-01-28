package com.izylab.set;

import javax.swing.JFrame;

import com.izylab.set.domain.SetDeck;
import com.izylab.set.model.Deck;
import com.izylab.set.swing.BoardPanel;

public class SetGameApp extends JFrame {
	private static final long serialVersionUID = 1L;
	protected BoardPanel boardPanel;
	
	public void run() {
		boardPanel = new BoardPanel(new SetDeck(Deck.Type.FULL));
		getContentPane().add(boardPanel);
		boardPanel.setupBoard();

		setTitle("SET");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args) {
		new SetGameApp().run();
	}

}
