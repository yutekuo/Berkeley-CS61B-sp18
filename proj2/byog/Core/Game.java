package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private long seed;
    private World worldMap;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param s the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String s) {
        //Uncomment to see the StdDraw window.
        //ter.initialize(WIDTH, HEIGHT);

        char[] input = s.toCharArray();
        StringBuilder seedString = new StringBuilder();
        int index = 1;

        if (input[0] == 'n' || input[0] == 'N') {
            // Start a new game.
            while (input[index] != 's' && input[index] != 'S') {
                seedString.append(input[index]);
                index++;
            }
            seed = Long.parseLong(seedString.toString());
            worldMap = new World(WIDTH, HEIGHT, seed);
            worldMap.fillWithNothing();
            worldMap.addManyRooms();
            worldMap.addHallways();
            //worldMap.addDoor();
            worldMap.addPlayer();
        } else if (input[0] == 'l' || input[0] == 'L') {
            index = 0;
        }

        index++;
        while (index < input.length && input[index] != ':') {
            worldMap.move(input[index]);
            index++;
        }

        // Draw the world on the screen.
        //Uncomment to see the StdDraw window.
        //ter.renderFrame(worldMap.getWorld());
        return worldMap.getWorld();
    }
}
