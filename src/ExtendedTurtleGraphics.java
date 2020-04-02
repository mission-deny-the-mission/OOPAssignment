import uk.ac.leedsbeckett.oop.TurtleGraphics;

import java.awt.*;

public class ExtendedTurtleGraphics extends TurtleGraphics {
    public void square(int length) {
        for (int i = 0; i < 4; i++) {
            forward(length);
            turnLeft(90);
        }
    }

    public void circle(int radius) {
        // todo: implement
    }

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
