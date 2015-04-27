package nsbradford;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * Controls all actions to do with mouse events over the DeckView widget.
 * 
 * @author Nicholas
 *
 */
public class DeckController extends SolitaireReleasedAdapter {

	protected FortyThieves theGame;
	protected Pile wastePile;
	protected Deck deck;

	public DeckController(FortyThieves theGame, Deck d, Pile wastePile) {
		super(theGame);

		this.theGame = theGame;
		this.wastePile = wastePile;
		this.deck = d;
	}

	/**
	 * Coordinate reaction to the beginning of a Drag Event. In this case,
	 * no drag is ever achieved, and we simply deal upon the press.
	 */
	public void mousePressed (java.awt.event.MouseEvent me) {

		// Attempting a DealFourCardMove
		Move m = new MoveDealCardToWaste (deck, wastePile);
		if (m.doMove(theGame)) {
			theGame.pushMove (m);     // Successful DealFour Move
			theGame.refreshWidgets(); // refresh updated widgets.
		}
	}
}
