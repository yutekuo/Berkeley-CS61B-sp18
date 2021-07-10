package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
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
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Uncomment to see the StdDraw window.
        //ter.initialize(WIDTH, HEIGHT);

        // Start a new game or load a previous game.
        // Start a new game: assign a new World object to worldMap.
        // Load a previous game: keep using the existing worldMap.
        input = input.toUpperCase();
        int index;
        if (input.charAt(0) == 'N') {
            StringBuilder seedString = new StringBuilder();
            index = 1;
            while (input.charAt(index) != 'S') {
                seedString.append(input.charAt(index));
                index++;
            }
            long seed = Long.parseLong(seedString.toString());
            worldMap = createOneWorld(seed);
            // Now input.charAt[index] == 'S' of the substring "N####S"
        } else {
            index = 0; // input.charAt[0] == 'L''.
        }

        // Process saving and movement.
        if (index != input.length() - 1) {
            if (input.contains(":Q")) {
                int endIndex = input.indexOf(":Q");
                index++;
                while (index < endIndex) {
                    worldMap.move(input.charAt(index));
                    index++;
                }
            } else {
                index++;
                while (index < input.length()) {
                    if (worldMap == null) {
                        throw new NullPointerException("worldMap points to null!");
                    }
                    worldMap.move(input.charAt(index));
                    index++;
                }
            }
        }

        // Draw the world on the screen.
        //Uncomment to see the StdDraw window.
        //ter.renderFrame(worldMap.getWorld());
        return worldMap.getWorld();
    }

    private World createOneWorld(long seed) {
        World world = new World(WIDTH, HEIGHT, seed);
        world.fillWithNothing();
        world.addManyRooms();
        world.addHallways();
        world.addPlayer();
        return world;
    }
}
