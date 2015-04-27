package nsbradford;

import java.awt.event.MouseEvent;
import ks.common.model.Deck;
import ks.launcher.Main;
import ks.tests.KSTestCase;
import org.junit.Test;

/**
 * Runs tests on the DeckController using artificial MouseEvents.
 * 
 * @author Nicholas
 *
 */
public class TestDeckController extends KSTestCase{

	@Test
	public void test() {
		FortyThieves fortyThieves = new FortyThieves();
		Main.generateWindow(fortyThieves, Deck.OrderBySuit);
		
		DeckController fc = new DeckController(fortyThieves, fortyThieves.deck, fortyThieves.wastePile);
		MouseEvent me = this.createClicked(fortyThieves, fortyThieves.deckView, 0, 0);
		
		assertTrue(fortyThieves.wastePile.count() == 0);
		assertTrue(fortyThieves.deck.count() == 64);
		
		fc.mousePressed(me);
		
		assertTrue(fortyThieves.wastePile.count() == 1);
		assertTrue(fortyThieves.deck.count() == 63);
	}

}
