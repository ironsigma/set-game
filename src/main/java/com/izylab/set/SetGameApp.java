package com.izylab.set;

import java.awt.BorderLayout;

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

import com.izylab.set.domain.SetDeck;
import com.izylab.set.model.Card;
import com.izylab.set.model.Deck;
import com.izylab.set.swing.BoardPanel;
import com.izylab.set.swing.BoardPanelListener;
import com.izylab.set.swing.StatusLabel;

/**
 * Set Game Application.
 */
public final class SetGameApp extends JFrame implements BoardPanelListener {
    /** Serial version. */
    private static final long serialVersionUID = 1L;
    /** Board panel. */
    private BoardPanel boardPanel;
    /** Status label. */
    private StatusLabel statusLabel = new StatusLabel();

    /**
     * Run the application.
     */
    public void run() {
        buildMenu();

        boardPanel = new BoardPanel(new SetDeck(Deck.Type.TINY));
        boardPanel.addListener(this);
        boardPanel.setupBoard();

        JPanel statusBar = new JPanel();
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusBar.add(new JLabel(" "));
        statusBar.add(statusLabel);

        getContentPane().add(boardPanel, BorderLayout.CENTER);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        setTitle("SET");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        // setLocationRelativeTo(null);
        setLocation(550, 1350);
        setVisible(true);
    }

    /**
     * Build the menu.
     */
    protected void buildMenu() {
        JMenuItem quitMenuItem = new JMenuItem("Quit");

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(quitMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    @Override
    public void setupComplete(final Deck deck) {
    }

    @Override
    public void solutionFound(final Card card1, final Card card2,
            final Card card3) {
        statusLabel.incrementSetsFound();
    }

    @Override
    public void invalidSolution(final Card card1, final Card card2,
            final Card card3) {
        JOptionPane.showMessageDialog(this, "Not a vaid SET", "SET",
                JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void solutionCountUpdate(final int count) {
        statusLabel.setAvailable(count);
    }

    @Override
    public void noMoreSolutions() {
        statusLabel.setAvailable(0);
        JOptionPane.showMessageDialog(this, "No more sets!", "SET",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Main.
     * @param args Arguments
     */
    public static void main(final String[] args) {
        new SetGameApp().run();
    }

}
