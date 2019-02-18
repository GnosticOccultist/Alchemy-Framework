package fr.alchemy.editor.api.editor.region;

/**
 * <code>AutoScrollingWindow</code> is an extension of {@link PanningWindow} which adds an auto-scrolling
 * mechanism.
 * <p>
 * The auto-scrolling occurs when the mouse is dragged to the edge of the window. The scrolling rate increases 
 * the longer the cursor is outside the window.
 * </p>
 * 
 * @author GnosticOccultist
 */
public class AutoScrollingWindow extends PanningWindow {
	
	/**
	 * Instantiates a new <code>AutoScrollingWindow</code>.
	 */
	public AutoScrollingWindow() {
		
	}
}
