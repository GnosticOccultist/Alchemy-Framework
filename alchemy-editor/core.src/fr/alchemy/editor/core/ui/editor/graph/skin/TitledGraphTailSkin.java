package fr.alchemy.editor.core.ui.editor.graph.skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.skin.GraphTailSkin;
import fr.alchemy.editor.core.ui.FXUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

public class TitledGraphTailSkin extends GraphTailSkin {
	
	protected final Polyline line = new Polyline();
    protected final Polygon endpoint = new Polygon();
    protected final Group group = new Group(line, endpoint);
	
    private static final double SIZE = 15;
    
	public TitledGraphTailSkin(GraphConnector connector) {
		super(connector);
		
		line.getStyleClass().setAll("titled-tail");
		endpoint.getStyleClass().setAll("titled-tail-endpoint");
		endpoint.getPoints().setAll(0D, 0D, 0D, SIZE, SIZE, SIZE, SIZE, 0D);
		group.setManaged(false);
	}

	@Override
	public Node getRoot() {
		return group;
	}
	
	@Override
    public void draw(final Point2D start, final Point2D end) {
        endpoint.setVisible(true);
        layoutEndpoint(end);
        drawStupid(start, end);
    }
	
    @Override
    public void draw(final Point2D start, final Point2D end, final GraphConnector target, final boolean valid) {

        endpoint.setVisible(false);
        if(valid) {
            drawSmart(start, end, target);
        } else {
            drawStupid(start, end);
        }
    }

    private void drawSmart(Point2D start, Point2D end, GraphConnector target) {
       clearPoints();
       addPoint(start);
       
       final Side startSide = getSide(getElement().getType());
       final Side endSide = getSide(target.getType());
       
       final List<Point2D> points = createPath(start, end, startSide, endSide);
       points.stream().forEachOrdered(point -> addPoint(point));
       
       addPoint(end);
	}
    
    private List<Point2D> createPath(Point2D startPosition, Point2D endPosition, Side startSide, Side endSide) {
        if (startSide.equals(Side.LEFT) && endSide.equals(Side.LEFT)) {
            return connectLeftToLeft(startPosition, endPosition);

        } else if (startSide.equals(Side.LEFT) && endSide.equals(Side.RIGHT)) {
            return connectLeftToRight(startPosition, endPosition);

        } else if (startSide.equals(Side.LEFT) && endSide.equals(Side.TOP)) {
            return connectLeftToTop(startPosition, endPosition);

        } else if (startSide.equals(Side.LEFT) && endSide.equals(Side.BOTTOM)) {
            return connectLeftToBottom(startPosition, endPosition);

        } else if (startSide.equals(Side.RIGHT) && endSide.equals(Side.LEFT)) {
            return reverse(connectLeftToRight(endPosition, startPosition));

        } else if (startSide.equals(Side.RIGHT) && endSide.equals(Side.RIGHT)) {
            return connectRightToRight(startPosition, endPosition);

        } else if (startSide.equals(Side.RIGHT) && endSide.equals(Side.TOP)) {
            return connectRightToTop(startPosition, endPosition);

        } else if (startSide.equals(Side.RIGHT) && endSide.equals(Side.BOTTOM)) {
            return connectRightToBottom(startPosition, endPosition);

        } else if (startSide.equals(Side.TOP) && endSide.equals(Side.LEFT)) {
            return reverse(connectLeftToTop(endPosition, startPosition));

        } else if (startSide.equals(Side.TOP) && endSide.equals(Side.RIGHT)) {
            return reverse(connectRightToTop(endPosition, startPosition));

        } else if (startSide.equals(Side.TOP) && endSide.equals(Side.TOP)) {
            return connectTopToTop(startPosition, endPosition);

        } else if (startSide.equals(Side.TOP) && endSide.equals(Side.BOTTOM)) {
            return connectTopToBottom(startPosition, endPosition);

        } else if (startSide.equals(Side.BOTTOM) && endSide.equals(Side.LEFT)) {
            return reverse(connectLeftToBottom(endPosition, startPosition));

        } else if (startSide.equals(Side.BOTTOM) && endSide.equals(Side.RIGHT)) {
            return reverse(connectRightToBottom(endPosition, startPosition));

        } else if (startSide.equals(Side.BOTTOM) && endSide.equals(Side.TOP)) {
            return reverse(connectTopToBottom(endPosition, startPosition));

        } else {
            return connectBottomToBottom(startPosition, endPosition);
        }
	}
    
    private static List<Point2D> connectRightToRight(final Point2D start, final Point2D end) {

        final List<Point2D> path = new ArrayList<>();

        final double maxX = Math.max(start.getX(), end.getX());
        addPoint(path, maxX + 30, start.getY());
        addPoint(path, maxX + 30, end.getY());

        return path;
    }
    
    private static List<Point2D> connectLeftToTop(final Point2D start, final Point2D end) {

        final List<Point2D> path = new ArrayList<>();

        if (start.getX() > end.getX() + 30) {
            if (start.getY() < end.getY() - 30) {
                addPoint(path, end.getX(), start.getY());
            } else {
                final double averageX = (start.getX() + end.getX()) / 2;
                addPoint(path, averageX, start.getY());
                addPoint(path, averageX, end.getY() - 30);
                addPoint(path, end.getX(), end.getY() - 30);
            }
        } else {
            if (start.getY() < end.getY() - 30) {
                final double averageY = (start.getY() + end.getY()) / 2;
                addPoint(path, start.getX() - 30, start.getY());
                addPoint(path, start.getX() - 30, averageY);
                addPoint(path, end.getX(), averageY);
            } else {
                addPoint(path, start.getX() - 30, start.getY());
                addPoint(path, start.getX() - 30, end.getY() - 30);
                addPoint(path, end.getX(), end.getY() - 30);
            }
        }

        return path;
    }
    
    private static List<Point2D> connectLeftToBottom(final Point2D start, final Point2D end) {

        final List<Point2D> path = new ArrayList<>();

        if (start.getX() > end.getX() + 30) {
            if (start.getY() > end.getY() + 30) {
                addPoint(path, end.getX(), start.getY());
            } else {
                final double averageX = (start.getX() + end.getX()) / 2;
                addPoint(path, averageX, start.getY());
                addPoint(path, averageX, end.getY() + 30);
                addPoint(path, end.getX(), end.getY() + 30);
            }
        } else {
            if (start.getY() > end.getY() + 30) {
                final double averageY = (start.getY() + end.getY()) / 2;
                addPoint(path, start.getX() - 30, start.getY());
                addPoint(path, start.getX() - 30, averageY);
                addPoint(path, end.getX(), averageY);
            } else {
                addPoint(path, start.getX() - 30, start.getY());
                addPoint(path, start.getX() - 30, end.getY() + 30);
                addPoint(path, end.getX(), end.getY() + 30);
            }
        }

        return path;
    }
    
    private static List<Point2D> connectRightToTop(final Point2D start, final Point2D end) {

        final List<Point2D> path = new ArrayList<>();

        if (start.getX() < end.getX() - 30) {
            if (start.getY() < end.getY() - 30) {
                addPoint(path, end.getX(), start.getY());
            } else {
                final double averageX = (start.getX() + end.getX()) / 2;
                addPoint(path, averageX, start.getY());
                addPoint(path, averageX, end.getY() - 30);
                addPoint(path, end.getX(), end.getY() - 30);
            }
        } else {
            if (start.getY() < end.getY() - 30) {
                final double averageY = (start.getY() + end.getY()) / 2;
                addPoint(path, start.getX() + 30, start.getY());
                addPoint(path, start.getX() + 30, averageY);
                addPoint(path, end.getX(), averageY);
            } else {
                addPoint(path, start.getX() + 30, start.getY());
                addPoint(path, start.getX() + 30, end.getY() - 30);
                addPoint(path, end.getX(), end.getY() - 30);
            }
        }

        return path;
    }
    
    private static List<Point2D> connectRightToBottom(final Point2D start, final Point2D end) {

        final List<Point2D> path = new ArrayList<>();

        if (start.getX() < end.getX() - 30) {
            if (start.getY() > end.getY() + 30) {
                addPoint(path, end.getX(), start.getY());
            } else {
                final double averageX = (start.getX() + end.getX()) / 2;
                addPoint(path, averageX, start.getY());
                addPoint(path, averageX, end.getY() + 30);
                addPoint(path, end.getX(), end.getY() + 30);
            }
        } else {
            if (start.getY() > end.getY() + 30) {
                final double averageY = (start.getY() + end.getY()) / 2;
                addPoint(path, start.getX() + 30, start.getY());
                addPoint(path, start.getX() + 30, averageY);
                addPoint(path, end.getX(), averageY);
            } else {
                addPoint(path, start.getX() + 30, start.getY());
                addPoint(path, start.getX() + 30, end.getY() + 30);
                addPoint(path, end.getX(), end.getY() + 30);
            }
        }

        return path;
    }
    
    private static List<Point2D> connectTopToTop(final Point2D start, final Point2D end) {

        final List<Point2D> path = new ArrayList<>();

        final double minY = Math.min(start.getY(), end.getY());
        addPoint(path, start.getX(), minY - 30);
        addPoint(path, end.getX(), minY - 30);

        return path;
    }
    
    private static List<Point2D> connectLeftToLeft(final Point2D start, final Point2D end) {

        final List<Point2D> path = new ArrayList<>();

        final double minX = Math.min(start.getX(), end.getX());
        addPoint(path, minX - 30, start.getY());
        addPoint(path, minX - 30, end.getY());

        return path;
    }
    
    private static List<Point2D> connectLeftToRight(final Point2D start, final Point2D end) {

        final List<Point2D> path = new ArrayList<>();

        if (start.getX() >= end.getX() + 2 * 30) {
            final double averageX = (start.getX() + end.getX()) / 2;
            addPoint(path, averageX, start.getY());
            addPoint(path, averageX, end.getY());
        } else {
            final double averageY = (start.getY() + end.getY()) / 2;
            addPoint(path, start.getX() - 30, start.getY());
            addPoint(path, start.getX() - 30, averageY);
            addPoint(path, end.getX() + 30, averageY);
            addPoint(path, end.getX() + 30, end.getY());
        }

        return path;
    }
    
    private static List<Point2D> connectTopToBottom(final Point2D start, final Point2D end) {

        final List<Point2D> path = new ArrayList<>();

        if (start.getY() >= end.getY() + 2 * 30) {
            final double averageY = (start.getY() + end.getY()) / 2;
            addPoint(path, start.getX(), averageY);
            addPoint(path, end.getX(), averageY);
        } else {
            final double averageX = (start.getX() + end.getX()) / 2;
            addPoint(path, start.getX(), start.getY() - 30);
            addPoint(path, averageX, start.getY() - 30);
            addPoint(path, averageX, end.getY() + 30);
            addPoint(path, end.getX(), end.getY() + 30);
        }

        return path;
    }
    
    private static List<Point2D> connectBottomToBottom(final Point2D start, final Point2D end) {

        final List<Point2D> path = new ArrayList<>();

        final double maxY = Math.max(start.getY(), end.getY());
        addPoint(path, start.getX(), maxY + 30);
        addPoint(path, end.getX(), maxY + 30);

        return path;
    }
    
    private static void addPoint(final List<Point2D> path, final double x, final double y) {
        path.add(new Point2D(x, y));
    }
    
    private static List<Point2D> reverse(final List<Point2D> points) {
        Collections.reverse(points);
        return points;
    }

	private Side getSide(final String type) {
    	if(type.contains("top")) {
    		return Side.TOP;
    	} else if(type.contains("right")) {
    		return Side.RIGHT;
    	} else if(type.contains("bottom")) {
    		return Side.BOTTOM;
    	} else if(type.contains("left")) {
    		return Side.LEFT;
    	} else {
    		return null;
    	}
    }

	@Override
    public void draw(final Point2D start, final Point2D end, final List<Point2D> jointPositions) {
        draw(start, end);
    }
    
    @Override
    public void draw(final Point2D start, final Point2D end, final List<Point2D> jointPositions, 
    		final GraphConnector target, final boolean valid) {
        draw(start, end, target, valid);
    }
    
    @Override
    public List<Point2D> allocateJointPositions() {

        final List<Point2D> jointPositions = new ArrayList<>();

        for (int i = 2; i < line.getPoints().size() - 2; i = i + 2) {

            final double x = FXUtils.moveOnPixel(line.getPoints().get(i));
            final double y = FXUtils.moveOnPixel(line.getPoints().get(i + 1));
            jointPositions.add(new Point2D(x, y));
        }

        return jointPositions;
    }

    protected void layoutEndpoint(final Point2D position) {
    	endpoint.setLayoutX(FXUtils.moveOnPixel(position.getX() - SIZE / 2));
        endpoint.setLayoutY(FXUtils.moveOnPixel(position.getY() - SIZE / 2));
    }
    
    private void drawStupid(final Point2D start, final Point2D end) {

        clearPoints();
        addPoint(start);

        if(getSide(getElement().getType()).isVertical()) {
        	addPoint((start.getX() + end.getX()) / 2, start.getY());
        	addPoint((start.getX() + end.getX()) / 2, end.getY());
        } else {
        	addPoint(start.getX(), (start.getY() + end.getY()) / 2);
        	addPoint(end.getX(), (start.getY() + end.getY()) / 2);
        }
        
        addPoint(end);
    }

    private void clearPoints() {
        line.getPoints().clear();
    }
    
    private void addPoint(final Point2D point) {
        addPoint(point.getX(), point.getY());
    }

    private void addPoint(final double x, final double y) {
    	line.getPoints().addAll(FXUtils.moveOffPixel(x), FXUtils.moveOffPixel(y));
    }

	@Override
	protected void selectionChanged(boolean selected) {
		// TODO Auto-generated method stub

	}
}
