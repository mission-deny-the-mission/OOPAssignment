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
        penDown();
        final Color[] colors = {Color.black, Color.red, Color.yellow, Color.green};
        for (Color color : colors) {
            setPenColour(color);
            for (int i = 0; i < 4; i++) {
                square(150);
                turnLeft(90);
            }
            turnLeft(90 / colors.length);
        }
        penUp();
    }

}
