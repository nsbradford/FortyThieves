package nsbradford;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Pile;

/**
 * Represents the move of a card to the Foundation from a Column
 * 
 * @author Nicholas
 *
 */
public class MoveCardToFoundation extends ks.common.model.Move {
	
	/** The source Column. */
	protected Column srcCol;

	/** The card being dragged (if at all). */
	protected Card draggingCard;
	
	/** The destination Foundation Pile. */
	protected Pile foundation;
	
	/**
	 * Constructor.
	 * @param bp The source Column.
	 * @param card The card being dragged (if at all).
	 * @param foundation The destination Foundation Pile.
	 */
	public MoveCardToFoundation(Column bp, Card card, Pile foundation) {
		super();
	
		srcCol = bp;
		this.draggingCard = card;
		this.foundation = foundation;
	}
	
	/**
	 * Each move should knows how to execute itself.
	 */
	public boolean doMove (Solitaire theGame) {
		// VALIDATE (should we also check for validitation by rank? suit?
		if (valid (theGame) == false)
			return false;
	
		// EXECUTE:
		// Deal with both situations
		if (draggingCard == null)
			foundation.add (srcCol.get());
		else
			foundation.add (draggingCard);
	
		// advance score
		theGame.updateScore (1);
		return true;
	}
	
	/**
	 * Undo method.
	 */
	public boolean undo(Solitaire game) {
		// VALIDATE:
		if (foundation.empty()) return false;
	
		// EXECUTE UNDO:	
		srcCol.add (foundation.get());
	
		// reverse score advance
		game.updateScore (-1);
		return true;
	}
	
	/**
	 * Action for FortyThieves: Column card dragged to Foundation Pile
	 * @param d ks.common.games.Solitaire
	 */
	public boolean valid(ks.common.games.Solitaire game) {
		// VALIDATION:
		boolean validation = false;
	
		// If draggingCard is null, then no action has yet taken place.
		if (draggingCard == null) {
			// moveWasteToFoundation(buildablePile,pile) : not foundation.empty() and not buildablePile.empty() and 
			if (!foundation.empty() && !srcCol.empty() && (srcCol.rank() == foundation.rank() + 1) && (srcCol.suit() == foundation.suit()))
				validation = true;
	
			// moveWasteToFoundation(buildablePile,pile) : foundation.empty() and not buildablePile.empty() and waste.rank() == ACE
			if (foundation.empty() && !srcCol.empty() && srcCol.rank() == Card.ACE)
				validation = true;  
		} else {
			// the action must have taken place, so act on the card.
	
			// moveWasteToFoundation(waste,pile) : not foundation.empty() and not waste.empty() and 
			if (!foundation.empty() && (draggingCard.getRank() == foundation.rank() + 1) && (draggingCard.getSuit() == foundation.suit()))
				validation = true;
	
				// moveWasteToFoundation(waste,pile) : foundation.empty() and card.rank() == ACE
			if (foundation.empty() && (draggingCard.getRank() == Card.ACE))
				validation = true;
		}
	
		return validation;
	}
}
