package nsbradford;

import java.awt.event.MouseEvent;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.launcher.Main;
import ks.tests.KSTestCase;
import org.junit.Test;

/**
 * Runs tests on the FoundationController using artificial MouseEvents.
 * 
 * @author Nicholas
 *
 */
public class TestFoundationController extends KSTestCase{

	@Test
	public void test() {
		FortyThieves fortyThieves = new FortyThieves();
		Main.generateWindow(fortyThieves, Deck.OrderBySuit);
		
		Card topCard = new Card(1,1); // pretend this was on top of the waste
		Column myColumn = new Column();
		myColumn.add(topCard);
		ColumnView myColumnView = new ColumnView(myColumn);
		
		FoundationController fc = new FoundationController(fortyThieves, fortyThieves.foundationPileView[1]);
		MouseEvent me = this.createClicked(fortyThieves, fortyThieves.foundationPileView[1], 0, 0);
		
		// verify initial conditions
		assertTrue(fortyThieves.foundationPile[1].count() == 0);
		
		// first invalid case
		fortyThieves.getContainer().setActiveDraggingObject(myColumnView, me);
		fc.mouseReleased(me);
		
		assertTrue(fortyThieves.foundationPile[1].count() == 0);
		
		// second invalid case
		fortyThieves.getContainer().setActiveDraggingObject(myColumnView, me);
		fortyThieves.getContainer().setDragSource(null);
		fc.mouseReleased(me);
		
		assertTrue(fortyThieves.foundationPile[1].count() == 0);
		
		// valid case
		fortyThieves.getContainer().setActiveDraggingObject(new CardView(topCard), me);
		fortyThieves.getContainer().setDragSource(fortyThieves.wastePileView);
		fc.mouseReleased(me);
		
		assertTrue(fortyThieves.foundationPile[1].count() == 1);
		
		// another invalid case
		Card topCard2 = new Card(2,1); 
		Column myColumn2 = new Column();
		myColumn.add(topCard2);
		ColumnView myColumnView2 = new ColumnView(myColumn2);
		
		fortyThieves.getContainer().setActiveDraggingObject(myColumnView2, me);
		fortyThieves.getContainer().setDragSource(fortyThieves.columnView[1]);
		fc.mouseReleased(me);
		
		assertTrue(fortyThieves.foundationPile[1].count() == 1);
		
	}

}
