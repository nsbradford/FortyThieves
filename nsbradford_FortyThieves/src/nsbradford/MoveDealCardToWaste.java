package nsbradford;

import ks.common.games.Solitaire;
import ks.common.model.Deck;
import ks.common.model.Pile;

/**
 * Move card from top of deck to top of waste pile.
 * @author Nicholas
 *
 */
public class MoveDealCardToWaste extends ks.common.model.Move {

	Deck deck;
	Pile wastePile;
	
	public MoveDealCardToWaste(Deck deck, Pile wastePile){
		this.deck = deck;
		this.wastePile = wastePile;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game)) {
			return false;
		}
		else{
			wastePile.add(deck.get());
			game.updateNumberCardsLeft(-1);
			return true;
		}
	}

	@Override
	public boolean undo(Solitaire game) {
		deck.add(wastePile.get());
		game.updateNumberCardsLeft(1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		return !deck.empty();
	}

}
