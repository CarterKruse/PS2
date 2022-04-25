import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Using a quadtree for collision detection.
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016
 * @author CBK, Fall 2016
 * @author Carter Kruse & John Deforest, Dartmouth CS 10, Spring 2022
 */
public class CollisionGUI extends DrawingGUI
{
    private static final int width = 800, height = 600; // Size of the universe.

    private List<Blob> blobs; // List of all the blobs.
    private List<Blob> colliders; // List of the blobs who collided at this step.
    private char blobType = 'b'; // What type of blob to create.
    private char collisionHandler = 'c'; // When there is a collision, color (c) or destroy (d) them.
    private int delay = 100; // Timer Control

    public CollisionGUI()
    {
        super("Super-Collider", width, height);

        blobs = new ArrayList<Blob>();

        // Timer drives the animation.
        startTimer();
    }

    /**
     * Adds a blob of the current blobType at the location.
     */
    private void add(int x, int y)
    {
        if (blobType == 'b')
        {
            blobs.add(new Bouncer(x, y, width, height));
        }

        else if (blobType == 'w')
        {
            blobs.add(new Wanderer(x, y));
        }

        else
        {
            System.err.println("Unknown Blob Type: " + blobType);
        }
    }

    /**
     * DrawingGUI Method -> Here creating a new blob.
     */
    public void handleMousePress(int x, int y)
    {
        add(x, y);
        repaint();
    }

    /**
     * DrawingGUI Method
     */
    public void handleKeyPress(char k)
    {
        if (k == 'f') // Faster
        {
            if (delay > 1) delay /= 2;
            setTimerDelay(delay);
            System.out.println("Delay: " + delay);
        }

        else if (k == 's') // Slower
        {
            delay *= 2;
            setTimerDelay(delay);
            System.out.println("Delay: " + delay);
        }

        else if (k == 'r') // Add 10 new blobs at random positions.
        {
            // Cycling through for loop (10 iterations).
            for (int i = 0; i < 10; i += 1)
            {
                add((int) (width * Math.random()), (int) (height * Math.random()));
                repaint();
            }
        }

        else if (k == 'c' || k == 'd') // Control how the collisions are handled.
        {
            collisionHandler = k;
            System.out.println("Collision: " + k);
        }

        else // Set the type for new blobs.
        {
            blobType = k;
        }
    }

    /**
     * DrawingGUI Method -> Here drawing all the blobs and then re-drawing the colliders in red.
     */
    public void draw(Graphics g)
    {
        // Checking to ensure there are blobs to draw.
        if (this.blobs != null)
        {
            // Cycling through each blob in the list.
            for (Blob blob : this.blobs)
            {
                g.setColor(Color.black); // Blobs are assigned the color black.
                blob.draw(g);
            }
        }

        // Checking to ensure there are colliders to draw.
        if (this.colliders != null)
        {
            // Cycling through each collider in the list.
            for (Blob collider : colliders)
            {
                g.setColor(Color.red); // Colliders are assigned the color red.
                collider.draw(g); // Re-drawing the colliders over the blobs.
            }
        }
    }

    /**
     * Sets colliders to include all blobs in contact with another blob.
     *
     * STILL TO DO -
     */
    private void findColliders()
    {
        // Creating an empty tree. The node will initialize the tree.
        PointQuadtree<Blob> treeOfBlobs = null;

        // Populating the tree with all of the blobs.
        for (Blob blob : blobs)
        {
            if (treeOfBlobs == null)
            {
                treeOfBlobs = new PointQuadtree<Blob>(blob, 0, 0, width, height);
            }
            else
            {
                treeOfBlobs.insert(blob);
            }
        }
        // Finding/identifying the colliders
        if (treeOfBlobs != null)
        {
            if (colliders == null)
            {
                colliders = new ArrayList<Blob>();
            }

            for (Blob blob : blobs)
            {
                if (treeOfBlobs.findInCircle(blob.x, blob.y, blob.r * 2).size() > 1)
                {
                    colliders.add(blob);
                }
            }
        }


        // TODO
        // Create the tree
        // For each blob, see if anybody else collided with it
    }

    /**
     * DrawingGUI Method -> Here moving all the blobs and checking for collisions.
     */
    public void handleTimer()
    {
        // Ask all the blobs to move themselves.
        for (Blob blob : blobs)
        {
            blob.step();
        }

        // Check for collisions.
        if (blobs.size() > 0)
        {
            findColliders();

            // Checking to see whether to destroy (d) the colliders.
            if (collisionHandler == 'd')
            {
                blobs.removeAll(colliders); // Removing the colliders from the list of blobs.
                colliders = null; // Setting the list of colliders to null.
            }
        }

        // Now update the drawing.
        repaint();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new CollisionGUI();
            }
        });
    }
}
