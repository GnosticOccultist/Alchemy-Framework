package fr.alchemy.editor.core.ui.editor.graph.connections;

import java.util.List;

import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.skin.GraphTailSkin;
import fr.alchemy.editor.core.ui.FXUtils;
import fr.alchemy.editor.core.ui.editor.graph.GraphNodeEditorView;
import fr.alchemy.editor.core.ui.editor.graph.GraphSkinManager;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class TailManager {

	/**
	 * The graph node editor view.
	 */
	private final GraphNodeEditorView view;
	/**
	 * The skin manager.
	 */
    private final GraphSkinManager skinManager;
    /**
     * The tail skin
     */
    private GraphTailSkin tailSkin;
    

    private Point2D sourcePosition;
    private List<Point2D> jointPositions;
	
	public TailManager(GraphSkinManager skinManager, GraphNodeEditorView view) {
		this.view = view;
		this.skinManager = skinManager;
	}
	
	public void create(final GraphConnector connector, final MouseEvent event) {
		
		if(tailSkin == null) {
			
			tailSkin = skinManager.retrieveTail(connector);
			
			sourcePosition = FXUtils.getConnectorPosition(connector, skinManager);
			final Point2D cursorPosition = getScaledPosition(FXUtils.cursorPosition(event, view));
					
			tailSkin.draw(sourcePosition, cursorPosition);

            view.add(tailSkin);
            tailSkin.getRoot().toBack();
		}
	}
	
    public void updateToNewSource(final List<Point2D> pJointPositions, final GraphConnector newSource, final MouseEvent event)  {
        cleanup();
        jointPositions = pJointPositions;

        tailSkin = skinManager.retrieveTail(newSource);

        sourcePosition = FXUtils.getConnectorPosition(newSource, skinManager);
        final Point2D cursorPosition = getScaledPosition(FXUtils.cursorPosition(event, view));

        tailSkin.draw(sourcePosition, cursorPosition, jointPositions);
        view.add(tailSkin);
        tailSkin.getRoot().toBack();
    }

    public void updatePosition(final MouseEvent event) {

        if (tailSkin != null && sourcePosition != null) {

            final Point2D cursorPosition = getScaledPosition(FXUtils.cursorPosition(event, view));

            if (jointPositions != null) {
                tailSkin.draw(sourcePosition, cursorPosition, jointPositions);
            } else {
                tailSkin.draw(sourcePosition, cursorPosition);
            }
        }
    }
    
    public void snapPosition(final GraphConnector source, final GraphConnector target, final boolean valid) {

        if (tailSkin != null) {

            final Point2D sourcePosition = FXUtils.getConnectorPosition(source, skinManager);
            final Point2D targetPosition = FXUtils.getConnectorPosition(target, skinManager);

            if (jointPositions != null) {
                tailSkin.draw(sourcePosition, targetPosition, jointPositions, target, valid);
            } else {
                tailSkin.draw(sourcePosition, targetPosition, target, valid);
            }
        }
    }

	public void cleanup() {
		jointPositions = null;
		
		if(tailSkin != null) {
			view.remove(tailSkin);
			tailSkin = null;
		}
	}
	
    private Point2D getScaledPosition(final Point2D cursorPosition) {

        final double scale = view.getLocalToSceneTransform().getMxx();
        return new Point2D(cursorPosition.getX() / scale, cursorPosition.getY() / scale);
    }
}
