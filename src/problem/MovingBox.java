package problem;

import problem.Box;

import java.awt.geom.Point2D;


/**
 * This class represents one of the moving boxes in Assignment 1.
 * 
 * @author Sergiy Dudnikov
 */
public class MovingBox extends Box {

	/**
	 * Constructs a Moving box at a position width a side width
	 * 
	 * @param pos
	 *            the position of the box
	 * @param width
	 *            the width (and height) of the box
	 */
    public MovingBox(Point2D pos, double width) {
        super(pos, width);
    }

	/**
	 * String representation of box coordinates
	 * @return String representation of box coordinates
	 */
	@Override
	public String toString() {
		return this.getPos().getX() + " "
				+ this.getPos().getY() + " ";
	}
}
