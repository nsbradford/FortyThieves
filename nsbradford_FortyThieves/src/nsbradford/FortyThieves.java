package nsbradford;

import java.awt.Dimension;

import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;
import ks.common.view.CardImages;
import ks.common.view.ColumnView;
import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.launcher.Main;

/**
 * Plugin to play the Forty Thieves variation of Solitaire.
 * <p>
 * @author Nicholas S. Bradford (nsbradford@wpi.edu)
 */
public class FortyThieves extends Solitaire {

	int numFoundations = 8;
	int numColumns = 10;
	MultiDeck deck;
	Pile wastePile;
	Pile foundationPile[] = new Pile[numFoundations];
	Column column[] = new Column[numColumns];
	DeckView deckView;
	PileView wastePileView;
	PileView foundationPileView[] = new PileView[numFoundations];
	ColumnView columnView[] = new ColumnView[numColumns];
	protected IntegerView scoreView;
	protected IntegerView numRemainingView;

	/**
	 * Constructor
	 */
	public FortyThieves(){
		super();
		/*if (!this.hasWon())
			System.out.println("No Idea what went wrong");*/
	}
	
	@Override
	public String getName() {
		if (hasWon())
			return "nsbradford-FortyThieves: VICTORY";
		else
			return "nsbradford-FortyThieves";
	}

	/**
	 * Return whether the game has won.
	 * <p>
	 * In Forty Thieves, the game is won once the score reaches 104, signifying that
	 * all both sets thirteen cards of each of the four suits has been placed in foundations.
	 */
	@Override
	public boolean hasWon() {
		return getScoreValue() == 104;
	}

	/**
	 * 
	 */
	@Override
	public void initialize() {
		// initialize model
		initializeModel(getSeed());
		initializeView();
		initializeController();
		
		// prepare game by dealing cards to columns
		for (int i = 0; i < numColumns; i++){
			// deal 4 cards to each column
			for (int j = 0; j < 4; j++){
				column[i].add(deck.get());
			}
		}
		updateNumberCardsLeft(-40);
	}


	/**
	 * Initializes the entity elements, and add them to the model.
	 * @param seed is used to create the deck
	 */
	private void initializeModel(int seed) {
		deck = new MultiDeck("deck", 2);
		deck.create(seed);
		wastePile = new Pile("waste");
		model.addElement(deck);
		model.addElement(wastePile);
		
		// foundationPile
		for (int i = 0; i < numFoundations; i++){
			foundationPile[i] = new Pile("foundation" + i);
			model.addElement(foundationPile[i]);
		}
		
		// column
		for (int i = 0; i < numColumns; i++){
			column[i] = new Column("column" + i);
			model.addElement(column[i]);
		}
		
		// number of cards left is (cards in 2 decks) - (4 cards per column) = 104 - numColumns * 4 = 64
		this.updateNumberCardsLeft(104);
		this.updateScore(0);
	}

	/**
	 * Initializes all View objects from their respective entities,
	 * and adds them to the container.
	 */
	private void initializeView() {
		CardImages ci = getCardImages();
		
		// DECK
		deckView = new DeckView(deck);
		deckView.setBounds(20,30, ci.getWidth(), ci.getHeight());
		container.addWidget(deckView);
		
		// WASTE PILE
		wastePileView = new PileView(wastePile);
		wastePileView.setBounds(20*2 + ci.getWidth(), 30, ci.getWidth(), ci.getHeight());
		container.addWidget(wastePileView);
		
		// SCORE
		scoreView = new IntegerView (getScore());
		scoreView.setBounds(numFoundations*20 + numFoundations*ci.getWidth(), 60 + ci.getHeight(), 160, 60);
		container.addWidget(scoreView);

		// NUMBER REMAINING
		numRemainingView = new IntegerView (getNumLeft());
		numRemainingView.setFontSize(14);
		numRemainingView.setBounds (20 + ci.getWidth()/4, 10, ci.getWidth(), 20);
		container.addWidget(numRemainingView);

		// PILE VIEWS
		for (int i = 0; i < numFoundations; i++){
			foundationPileView[i] = new PileView(foundationPile[i]);
			foundationPileView[i].setBounds(20*(i+1) + ci.getWidth()*i, 30*2 + ci.getHeight(), ci.getWidth(), ci.getHeight());
			container.addWidget(foundationPileView[i]);
		}
		
		// COLUMN VIEWS
		for (int i = 0; i < numColumns; i++){
			columnView[i] = new ColumnView(column[i]);
			columnView[i].setBounds(20*(i+1) + ci.getWidth()*i, 30*3 + ci.getHeight()*2, ci.getWidth(), 16 * ci.getOverlap() + ci.getHeight());
			container.addWidget(columnView[i]);
		}
		
	}

	/**
	 * 
	 */
	private void initializeController() {
		// Initialize Controllers for DeckView
		deckView.setMouseAdapter(new DeckController(this, deck, wastePile));
		deckView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		deckView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// WastePile
		wastePileView.setMouseAdapter (new WastePileController(this, wastePileView));
		wastePileView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
		wastePileView.setUndoAdapter (new SolitaireUndoAdapter(this));
		
		// Now for each BuildablePile.
		for (int i = 0; i < numColumns; i++) {
			columnView[i].setMouseAdapter (new ColumnController (this, columnView[i]));
			columnView[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			columnView[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}

		// Now for each Foundation.
		for (int i = 0; i < numFoundations; i++) {
			foundationPileView[i].setMouseAdapter (new FoundationController (this, foundationPileView[i]));
			foundationPileView[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			foundationPileView[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}
		
		// Ensure that any releases (and movement) are handled by the non-interactive widgets
		numRemainingView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		numRemainingView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		numRemainingView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// same for scoreView
		scoreView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		scoreView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		scoreView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// Finally, cover the Container for any events not handled by a widget:
		getContainer().setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		getContainer().setMouseAdapter (new SolitaireReleasedAdapter(this));
		getContainer().setUndoAdapter (new SolitaireUndoAdapter(this));
	}
	
	/**
	 * Returns the preferred size needed for this solitaire game to function.
	 * <p>
	 * Each subclass should override this method if more space is needed
	 * initially when the solitaire version is initialized.
	 * <p>
	 * The default value returned is (769, 635).
	 * 
	 * @return java.awt.Dimension the desired dimension of the game.
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(970, 850);
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		Main.generateWindow(new FortyThieves(), Deck.OrderBySuit);
	}
	
}
