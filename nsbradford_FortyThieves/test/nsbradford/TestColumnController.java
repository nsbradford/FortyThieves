package nsbradford;

import java.awt.event.MouseEvent;

import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.view.ColumnView;
import ks.launcher.Main;
import ks.tests.KSTestCase;
import org.junit.Test;

/**
 * Runs test on ColumnController using artificial MouseEvents.
 * 
 * @author Nicholas
 *
 */
public class TestColumnController extends KSTestCase{

	@Test
	public void testColumnController() {
		FortyThieves fortyThieves = new FortyThieves();
		Main.generateWindow(fortyThieves, Deck.OrderBySuit);
		
		Card topCard = new Card(1,1); // pretend this was on top of the waste
		Column myColumn = new Column(); // a column of 1 card being moved
		myColumn.add(topCard);
		ColumnView myColumnView = new ColumnView(myColumn);
		
		ColumnController cc = new ColumnController(fortyThieves, fortyThieves.columnView[1]);
		MouseEvent me = this.createClicked(fortyThieves, fortyThieves.columnView[1], 0, 0);
		MouseEvent me2 = this.createClicked(fortyThieves, fortyThieves.columnView[2], 0, 0);
		
		assertTrue(fortyThieves.column[1].count() == 4);
		
		// use a dragging object, but nothing else initialized
		fortyThieves.getContainer().setActiveDraggingObject(myColumnView, me);
		cc.mousePressed(me);
		cc.mouseReleased(me);
		
		assertTrue(fortyThieves.column[1].count() == 5);
		
		// use a dragging object from a Different source, but nothing else initialized
		fortyThieves.getContainer().setActiveDraggingObject(myColumnView, me);
		cc.mousePressed(me2);
		cc.mouseReleased(me2);
		
		assertTrue(fortyThieves.column[1].count() == 6);
		
		// same test, null drag source
		fortyThieves.getContainer().setActiveDraggingObject(myColumnView, me);
		fortyThieves.getContainer().setDragSource(null);
		cc.mousePressed(me);
		cc.mouseReleased(me);
		
		assertTrue(fortyThieves.column[1].count() == 6);
	}

}
