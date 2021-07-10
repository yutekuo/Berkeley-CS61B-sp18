package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

        String upperCaseInput = input.toUpperCase();

        if (upperCaseInput.contains(":Q")) {
            StringBuilder savedInput = new StringBuilder();
            int endIndex = upperCaseInput.indexOf(":Q");

            if (upperCaseInput.charAt(0) == 'N') {
                savedInput.append('N');
                StringBuilder seedString = new StringBuilder();
                int index = 1;
                while (upperCaseInput.charAt(index) != 'S') {
                    seedString.append(upperCaseInput.charAt(index));
                    savedInput.append(upperCaseInput.charAt(index));
                    index++;
                }
                // Now upperCaseInput.charAt[index] == 'S' of the substring "N####S".
                savedInput.append(upperCaseInput.charAt(index));
                long seed = Long.parseLong(seedString.toString());
                worldMap = createOneWorld(seed);

                index++;
                while (index < endIndex) {
                    worldMap.move(upperCaseInput.charAt(index));
                    savedInput.append(upperCaseInput.charAt(index));
                    index++;
                }
                saveInput(savedInput.toString());
            } else { // upperCaseInput.charAt(0) == 'L'
                String previousInput = loadInput();
                StringBuilder newInput = new StringBuilder(previousInput);
                if (endIndex != 1) {
                    newInput.append(upperCaseInput, 1, endIndex);
                    savedInput.append(upperCaseInput, 1, endIndex);
                }
                saveInput(savedInput.toString());
                return playWithInputString(newInput.toString());
            }
        } else { // !upperCaseInput.contains(":Q")
            if (upperCaseInput.charAt(0) == 'N') {
                StringBuilder seedString = new StringBuilder();
                int index = 1;
                while (upperCaseInput.charAt(index) != 'S') {
                    seedString.append(upperCaseInput.charAt(index));
                    index++;
                }
                // Now upperCaseInput.charAt[index] == 'S' of the substring "N####S".
                long seed = Long.parseLong(seedString.toString());
                worldMap = createOneWorld(seed);

                index++;
                while (index < upperCaseInput.length()) {
                    worldMap.move(upperCaseInput.charAt(index));
                    index++;
                }
            } else { // upperCaseInput.charAt(0) == 'L'
                String previousInput = loadInput();
                StringBuilder newInput = new StringBuilder(previousInput);
                if (upperCaseInput.length() > 1) {
                    newInput.append(upperCaseInput, 1, upperCaseInput.length());
                }
                return playWithInputString(newInput.toString());
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

    /** Load the previous input from savedGame.txt to Game.java. */
    private static String loadInput() {
        File f = new File("./savedGame.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                String loadInput = os.readObject().toString();
                os.close();
                return loadInput;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return "";
    }

    /** Save input to the file savedGame.txt. */
    private static void saveInput(String input) {
        File f = new File("./savedGame.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(input);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}
