package fr.alchemy.editor.core.ui.editor.graph.skin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectionSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphJointSkin;
import fr.alchemy.editor.api.editor.region.DraggableBox;
import fr.alchemy.editor.core.ui.FXUtils;
import fr.alchemy.editor.core.ui.editor.graph.connections.ConnectionSegment;
import fr.alchemy.editor.core.ui.editor.graph.connections.GappedConnectionSegment;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class TitledGraphConnectionSkin extends GraphConnectionSkin {

    protected final Group root = new Group();
    protected final Path path = new Path();
    protected final Path backgroundPath = new Path();
    
    //private final JointAlignmentManager jointAlignmentManager;
    
    private List<GraphJointSkin> jointSkins;
    
    protected final List<ConnectionSegment> connectionSegments = new ArrayList<>();
	
	public TitledGraphConnectionSkin(GraphConnection connection) {
		super(connection);
		
		root.setManaged(false);

		// Background path is invisible and used only to capture hover events.
		root.getChildren().add(backgroundPath);
		root.getChildren().add(path);

		path.setMouseTransparent(true);
		
		//jointAlignmentManager = new JointAlignmentManager(connection);
		
		backgroundPath.getStyleClass().setAll("default-connection-background");
		path.getStyleClass().setAll("default-connection");
	}
	
	@Override
	public void setGraphEditor(GraphNodeEditor editor) {
		super.setGraphEditor(editor);
		
		//jointAlignmentManager.setSkinLookup(editor.getSkinDictionary());
	}
	
	@Override
	public void setJointSkins(List<GraphJointSkin> jointSkins) {
		if(this.jointSkins != null) {
			removeOldRectangularConstraints();
		}
		
		this.jointSkins = jointSkins;
		
		addRectangularConstraints();
		
		//jointAlignmentManager.addAlignmentHandlers(jointSkins);
	}
	
	@Override
	public Point2D[] update() {
		final Point2D[] points = super.update();
        checkFirstAndLastJoints(points);
		return points;
	}

	@Override
	public void draw(Map<GraphConnectionSkin, Point2D[]> allPoints) {
		super.draw(allPoints);
	
		final double[][] intersections = null;
		
		final Point2D[] points = allPoints == null ? null : allPoints.get(this);
		if(points != null) {
			drawAllSegments(points, intersections);
		} else {
			connectionSegments.clear();
			path.getElements().clear();
		}
	}
	
    private void removeOldRectangularConstraints() {
        for (int i = 0; i < jointSkins.size() - 1; i++) {
            final DraggableBox thisJoint = jointSkins.get(i).getRoot();
            final DraggableBox nextJoint = jointSkins.get(i + 1).getRoot();

            if (isSegmentHorizontal(getElement(), i)) {
                thisJoint.bindLayoutX(null);
                nextJoint.bindLayoutX(null);
            } else {
                thisJoint.bindLayoutY(null);
                nextJoint.bindLayoutY(null);
            }
        }
    }
    
    private void addRectangularConstraints() {
        // Our rectangular connection logic assumes an even number of joints.
        for (int i = 0; i < jointSkins.size() - 1; i++) {
            final DraggableBox thisJoint = jointSkins.get(i).getRoot();
            final DraggableBox nextJoint = jointSkins.get(i + 1).getRoot();

            if (isSegmentHorizontal(getElement(), i)) {
                thisJoint.bindLayoutX(nextJoint);
                nextJoint.bindLayoutX(thisJoint);
            } else {
                thisJoint.bindLayoutY(nextJoint);
                nextJoint.bindLayoutY(thisJoint);
            }
        }
    }
    
    private void checkFirstAndLastJoints(final Point2D[] points) {
        alignJoint(points, isSegmentHorizontal(getElement(), 0), true);
        alignJoint(points, isSegmentHorizontal(getElement(), points.length - 2), false);
    }
    
    public static boolean isSegmentHorizontal(final GraphConnection connection, final int i) {

        final String sourceType = connection.getSource().getType();
        final boolean horizontal = sourceType.contains("left") || sourceType.contains("right");

        return horizontal == ((i & 1) == 0);
    }
    
    private void alignJoint(final Point2D[] points, final boolean vertical, final boolean start) {
        final int targetPositionIndex = start ? 0 : points.length - 1;
        final int jointPositionIndex = start ? 1 : points.length - 2;
        final GraphJointSkin jointSkin = jointSkins.get(start ? 0 : jointSkins.size() - 1);

        if(vertical) {
            final double newJointY = points[targetPositionIndex].getY();
            final double newJointLayoutY = FXUtils.moveOnPixel(newJointY - jointSkin.getHeight() / 2);
            jointSkin.getRoot().setLayoutY(newJointLayoutY);

            final double currentX = points[jointPositionIndex].getX();
            points[jointPositionIndex] = new Point2D(currentX, newJointY);
        } else {
            final double newJointX = points[targetPositionIndex].getX();
            final double newJointLayoutX = FXUtils.moveOnPixel(newJointX - jointSkin.getWidth() / 2);
            jointSkin.getRoot().setLayoutX(newJointLayoutX);

            final double currentY = points[jointPositionIndex].getY();
            points[jointPositionIndex] = new Point2D(newJointX, currentY);
        }
    }

	private void drawAllSegments(Point2D[] points, double[][] intersections) {
		final double startX = points[0].getX();
        final double startY = points[0].getY();
        
        final MoveTo moveTo = new MoveTo(FXUtils.moveOffPixel(startX), FXUtils.moveOffPixel(startY));
        
        connectionSegments.clear();
        path.getElements().clear();
        path.getElements().add(moveTo);
        
        for(int i = 0; i < points.length - 1; i++) {
        	final Point2D start = points[i];
        	final Point2D end = points[i + 1];
        	
        	final double[] segmentIntersections = intersections != null ? intersections[i] : null;
        	final ConnectionSegment segment = new GappedConnectionSegment(start, end, segmentIntersections);
        	
        	segment.draw();
        	
        	connectionSegments.add(segment);
        	path.getElements().addAll(segment.getPathElements());
        }
        
        backgroundPath.getElements().clear();
        backgroundPath.getElements().addAll(path.getElements());
	}

	@Override
	public Node getRoot() {
		return root;
	}

	@Override
	protected void selectionChanged(boolean selected) {
		// TODO Auto-generated method stub
		
	}
}
