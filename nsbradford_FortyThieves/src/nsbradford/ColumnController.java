package nsbradford;

import java.awt.event.MouseEvent;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;

/**
 * Controls all actions to do with mouse events over the Column widget.
 * 
 * @author Nicholas
 * 
 */
public class ColumnController extends java.awt.event.MouseAdapter {

	/** The game that we are partly controlling. */
	protected FortyThieves theGame;

	/** The src Column that initiated the event. */
	protected ColumnView src;

	/**
	 * Constructor.
	 * 
	 * @param theGame the Solitaire game being played
	 * @param bpv ColumnView widget being controlled
	 */
	public ColumnController(FortyThieves theGame, ColumnView bpv) {
		super();

		this.theGame = theGame;
		this.src = bpv;
	}
	
	/**
	 * Coordinate reaction to the beginning of a Drag Event.
	 *
	 * Note: There is no way to differentiate between a press that
	 *       will become part of a double click vs. a click that will
	 *       be held and dragged. Only mouseReleased will be able to 
	 *       help us out with that one.
	 *
	 * @param me java.awt.event.MouseEvent
	 */
	public void mousePressed(MouseEvent me) {

		// The container manages several critical pieces of information; namely, it
		// is responsible for the draggingObject; in our case, this would be a CardView
		// Widget managing the card we are trying to drag between two piles.
		Container c = theGame.getContainer();

		/** Return if there is no card to be chosen. */
		Column srcColumn = (Column) src.getModelElement();
		if (srcColumn.count() == 0) {
			return;
		}

		// Get a column of cards to move from the ColumnView
		// Note that this method will alter the model for ColumnView if the condition is met.
		ColumnView colView = src.getColumnView (me);

		// an invalid selection (either all facedown, or not in faceup region)
		if (colView == null) {
			return;
		}

		// Check conditions
		Column srcColView = (Column) colView.getModelElement();
		if (srcColView == null) {
			System.err.println ("ColumnController::mousePressed(): Unexpectedly encountered a ColumnView with no Column.");
			return; // sanity check, but should never happen.
		}

		// verify that Column has desired Properties to move
		if (srcColView.count() != 1) {
			srcColumn.push (srcColView);
			java.awt.Toolkit.getDefaultToolkit().beep();
			return; // announce our displeasure
		}

		// If we get here, then the user has indeed clicked on the top card in the PileView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// cardView widget reflect the original card location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println ("ColumnController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}

		// Tell container which object is being dragged, and where in that widget the user clicked.
		c.setActiveDraggingObject (colView, me);

		// Tell container which BuildablePileView is the source for this drag event.
		c.setDragSource (src);

		// we simply redraw our source pile to avoid flicker,
		// rather than refreshing all widgets...
		src.redraw();
	}
	
	/**
	 * Coordinate reaction to the completion of a Drag Event.
	 * <p>
	 * A bit of a challenge to construct the appropriate move, because cards
	 * can be dragged both from the WastePile (as a CardView widget) and the 
	 * ColumnView (as a ColumnView widget).
	 * <p>
	 * @param me java.awt.event.MouseEvent
	 */
	public void mouseReleased(MouseEvent me) {
		
		Container c = theGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget w = c.getActiveDraggingObject();
		if (w == Container.getNothingBeingDragged()) {
			c.releaseDraggingObject();		
			return;
		}

		/** Recover the from BuildablePile OR waste Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("ColumnController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		// Determine the To Pile
		Column toPile = (Column) src.getModelElement();

		if (fromWidget instanceof ColumnView) {
			// Must be a ColumnView widget being dragged.
			ColumnView columnView = (ColumnView) w;
			Column col = (Column) columnView.getModelElement();
			if (col == null) {
				System.err.println ("ColumnController::mouseReleased(): somehow ColumnView model element is null.");
				return;
			}

			if (fromWidget == src) {
				toPile.push (col);   // simply put right back where it came from. No move
			} 
			else {
				Column fromPile = (Column) fromWidget.getModelElement();
				Move m = new MoveColumnToColumn (fromPile, toPile, col, col.count());

				if (m.doMove (theGame)) {
					// Successful move! add move to our set of moves
					theGame.pushMove (m);
				} else {
					// Invalid move. Restore to original column. NO MOVE MADE
					fromPile.push (col);
				}
			}		
		} 
		else {
			// Must be from the WastePile
			CardView cardView = (CardView) w;
			Card theCard = (Card) cardView.getModelElement();
			if (theCard == null) {
				System.err.println ("ColumnController::mouseReleased(): somehow CardView model element is null.");
				return;
			}

			Pile wastePile = (Pile) fromWidget.getModelElement();
			Move m = new MoveWasteToColumn (wastePile, theCard, toPile);
			if (m.doMove (theGame)) {
				// Successful move! add move to our set of moves
				theGame.pushMove (m); 
			} else { 
				// Invalid move. Restore to original waste pile. NO MOVE MADE
				wastePile.add (theCard);
			}
		}
		
		// release the dragging object, (container will reset dragSource)
		c.releaseDraggingObject();
		c.repaint();
	}
}
