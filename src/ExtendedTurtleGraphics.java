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
        // todo: implement
    }

    // method to draw example graphic
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
                square(150);
                turnLeft(90);
            }
            // turn some more to offset next square
            turnLeft(90 / colors.length);
        }
        penUp();
    }

}
