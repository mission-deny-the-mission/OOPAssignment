import uk.ac.leedsbeckett.oop.TurtleGraphics;

import java.awt.*;

// implements extra functions for turtle graphics
public class ExtendedTurtleGraphics extends TurtleGraphics {
    // function to create a square
    // used in about method further down
    public void square(int length) {
        // for each side of a square
        for (int i = 0; i < 4; i++) {
            // move forward and turn 90 degrees
            forward(length);
            turnLeft(90);
        }
    }

    @Override
    public void circle(int radius) {
        // only draw graphic if the pen is down
        if (penDown) {
            // positions circle so that is appears that the turtle has done a loop
            // which makes things like the about function work
            int centerXPos = xPos - radius;
            int centerYPos = yPos - radius;
            int directionModified = direction - 45;
            int circleXPos = (int) (centerXPos - radius * Math.sin(directionModified));
            int circleYPos = (int) (centerYPos - radius * Math.cos(directionModified));
            Graphics graphicsContext = getGraphicsContext();
            // change graphics color so that the circle is drawn in the correct colour
            graphicsContext.setColor(PenColour);
            graphicsContext.drawOval(circleXPos, circleYPos, radius * 2, radius * 2);
        }
    }

    // method to draw example graph
    @Override
    public void about() {
        // puts pen down
        penDown();
        // colours used for different squares
        final Color[] colors = {Color.black, Color.red, Color.yellow, Color.green};
        // for each colour
        for (Color color : colors) {
            //  set the pen colour
            setPenColour(color);
            // draw 4 squares 90 degrees apart
            for (int i = 0; i < 4; i++) {
                circle(75);
                turnLeft(90);
            }
            // turn some more to offset next square
            turnLeft(90 / colors.length);
        }
        penUp();
        turnRight(90);
    }

    @Override
    public void reset() {
        super.reset();
        turnLeft(90);
        penDown();
    }
}
