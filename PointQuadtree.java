import java.util.ArrayList;
import java.util.List;

/**
 * A Point Quadtree
 * Stores an element at a 2D position, with children at the subdivided quadrants.
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, Explicit Rectangle
 * @author CBK, Fall 2016, Generic With Point2D Interface
 * @author Carter Kruse, Dartmouth CS 10, Spring 2022
 */
public class PointQuadtree<E extends Point2D>
{
    private E point; // The point anchoring this node.
    private int x1, y1; // Upper-left corner of the region.
    private int x2, y2; // Bottom-right corner of the region.
    private PointQuadtree<E> c1, c2, c3, c4; // Children

    /**
     * Initializes a leaf quadtree, holding the point in the rectangle.
     */
    public PointQuadtree(E point, int x1, int y1, int x2, int y2)
    {
        this.point = point;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    // Getters
    public E getPoint()
    {
        return point;
    }

    public int getX1()
    {
        return x1;
    }

    public int getY1()
    {
        return y1;
    }

    public int getX2()
    {
        return x2;
    }

    public int getY2()
    {
        return y2;
    }

    /**
     * Returns the child (if any) at the given quadrant, 1-4.
     */
    public PointQuadtree<E> getChild(int quadrant)
    {
        if (quadrant == 1) return c1;
        if (quadrant == 2) return c2;
        if (quadrant == 3) return c3;
        if (quadrant == 4) return c4;
        return null;
    }

    /**
     * Returns whether there is a child at the given quadrant, 1-4.
     */
    public boolean hasChild(int quadrant)
    {
        return (quadrant == 1 && c1 != null) || (quadrant == 2 && c2 != null) || (quadrant == 3 && c3 != null) || (quadrant == 4 && c4 != null);
    }

    /**
     * Inserts the point into the tree.
     */
    public void insert(E point2)
    {
        if (point2.getX() > this.point.getX() && point2.getY() < this.point.getY())
        {
            if (this.hasChild(1))
            {
                c1.insert(point2);
            }

            else
            {
                this.c1 = new PointQuadtree<E>(point2, (int) this.point.getX(), this.y1, this.x2, (int) this.point.getY());
            }
        }

        else if (point2.getX() < this.point.getX() && point2.getY() < this.point.getY())
        {
            if (this.hasChild(2))
            {
                c2.insert(point2);
            }

            else
            {
                this.c2 = new PointQuadtree<E>(point2, this.x1, this.y1, (int) this.point.getX(), (int) this.point.getY());
            }
        }

        else if (point2.getX() < this.point.getX() && point2.getY() > this.point.getY())
        {
            if (this.hasChild(3))
            {
                c3.insert(point2);
            }

            else
            {
                this.c3 = new PointQuadtree<E>(point2, this.x1, (int) this.point.getY(), (int) this.point.getX(), this.y2);
            }
        }

        else if (point2.getX() > this.point.getX() && point2.getY() > this.point.getY())
        {
            if (this.hasChild(4))
            {
                c4.insert(point2);
            }

            else
            {
                this.c4 = new PointQuadtree<E>(point2, (int) this.point.getX(), (int) this.point.getY(), this.x2, this.y2);
            }
        }
    }

    /**
     * Finds the number of points in the quadtree (including its descendants)
     */
    public int size()
    {
        int count = 1;

        if (hasChild(1)) count += this.c1.size();
        if (hasChild(2)) count += this.c2.size();
        if (hasChild(3)) count += this.c3.size();
        if (hasChild(4)) count += this.c4.size();

        return count;
    }

    /**
     * Builds a list of all the points in the quadtree (including its descendants)
     */
//    public List<E> allPoints()
//    {
//        ArrayList<E> listOfPoints = new ArrayList<E>();
//
//        if (this.hasChild())
//
//        if (this.hasChild(1))
//        {
//            //listOfPoints.add();
//        }
//
//        if (this.hasChild(2))
//        {
//
//        }
//        if (this.hasChild(3))
//        {
//
//        }
//        if (this.hasChild(4))
//        {
//
//        }
//
//        // TODO: YOUR CODE HERE
//    }

    @Override
    public String toString()
    {
        return "" + point.getX() + " " + point.getY() + " " + x1 + " " + y1 + " " + x2 + " " + y2 + " ";
    }

    /**
     * Uses the quadtree to find all points within the circle
     *
//     * @param cx circle center x
//     * @param cy circle center y
//     * @param cr circle radius
     * @return the points in the circle (and the qt's rectangle)
     */
//    public List<E> findInCircle(double cx, double cy, double cr)
//    {
//        // TODO: YOUR CODE HERE
//    }

    // TODO: YOUR CODE HERE for any helper methods
    public static void main(String[] args)
    {
        PointQuadtree<Blob> bob = new PointQuadtree<Blob>(new Blob(50,50), 0, 0, 100, 100);

        bob.insert(new Blob(70, 40));

        bob.insert(new Blob(80, 30));

        bob.insert(new Blob(20, 30));
        bob.insert(new Blob(30, 40));

        System.out.println(bob.size());

        System.out.println(bob);
        System.out.println(bob.c1);
        System.out.println(bob.c1.c1);
        System.out.println(bob.c2);
        System.out.println(bob.c2.c4);
        System.out.println(bob.c2.c3);
    }
}
