package byog.Core;

import byog.TileEngine.TETile;
import org.junit.Test;
import static org.junit.Assert.*;
public class testPhase2 {
    @Test
    public void test() {
        Game game = new Game();
        TETile[][] worldState = game.playWithInputString("N999SDDDWWWDDD");
        TETile[][] second = game.playWithInputString("N999SDDDWWWDDD");

        assertArrayEquals(worldState, second);
        System.out.println(TETile.toString(worldState));

    }
}
