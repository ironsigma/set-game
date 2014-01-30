package com.izylab.set.swing;

import com.izylab.set.model.Card;

public interface BoardPanelListener {

	void solutionFound(Card card1, Card card2, Card card3);
	void invalidSolution(Card card1, Card card2, Card card3);
	void solutionCountUpdate(int count);
	void noMoreSolutions();
	void cardsDealt(int cardsLeft);

}
