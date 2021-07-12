package byog.Core;

import byog.InputDemo.InputSource;
import byog.InputDemo.KeyboardInputSource;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private static final int HEADS_UP_DISPLAY_SIZE = 3;
    private static final int MENU_WIDTH = 40;
    private static final int MENU_HEIGHT = 40;
    private World worldMap;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        InputSource inputSource = new KeyboardInputSource();
        StringBuilder savedInput = new StringBuilder();
        drawMenu();

        while (true) {
            char firstKey = Character.toUpperCase(inputSource.getNextKey());
            if (firstKey == 'N') {
                savedInput.append(firstKey);
                drawPrompt("");
                String seedString = solicitSeedInput();
                savedInput.append(seedString);

                long seed = Long.parseLong(seedString.substring(0, seedString.length() - 1));
                ter.initialize(WIDTH, HEIGHT + HEADS_UP_DISPLAY_SIZE);
                worldMap = createOneWorld(seed);
                ter.renderFrame(worldMap.getWorld());
                savedInput.append(processMove(inputSource));
                saveInput(savedInput.toString());
                exit(0);
            } else if (firstKey == 'L') {
                ter.initialize(WIDTH, HEIGHT + HEADS_UP_DISPLAY_SIZE);
                String previousInput = loadInput();
                StringBuilder seedString = new StringBuilder();
                int index = 1;
                while (previousInput.charAt(index) != 'S') {
                    seedString.append(previousInput.charAt(index));
                    index++;
                }
                long seed = Long.parseLong(seedString.toString());
                worldMap = createOneWorld(seed);
                index++;
                while (index < previousInput.length()) {
                    worldMap.move(previousInput.charAt(index));
                    index++;
                }
                ter.renderFrame(worldMap.getWorld());
                savedInput.append(previousInput);

                savedInput.append(processMove(inputSource));
                saveInput(savedInput.toString());
                exit(0);
            }
        }
    }

    /** Process movement and return input string. */
    private String processMove(InputSource inputSource) {
        StringBuilder input = new StringBuilder();
        while (inputSource.possibleNextInput()) {
            char key = Character.toUpperCase(inputSource.getNextKey());
            if (key == 'Q') {
                break;
            } else if (key == 'W' || key == 'S' || key == 'A' || key == 'D') {
                input.append(key);
                worldMap.move(key);
                ter.renderFrame(worldMap.getWorld());
            }
        }
        return input.toString();
    }

    /** Ask user to input a seed which is ended with a 'S', and then return it. */
    private String solicitSeedInput() {
        StringBuilder input = new StringBuilder();
        while (!input.toString().contains("S")) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = Character.toUpperCase(StdDraw.nextKeyTyped());
            if (key == '0' || key == '1' || key == '2' || key == '3' || key == '4' || key == '5'
                    || key == '6' || key == '7' || key == '8' || key == '9' || key == 'S') {
                input.append(key);
            }
            drawPrompt(input.toString());
        }
        StdDraw.pause(500);
        return input.toString();
    }

    /** Prompt for user to type in a seed. */
    private void drawPrompt(String s) {
        StdDraw.clear(Color.black);
        StdDraw.setFont(new Font("Consolas", Font.BOLD, 25));
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT - 10, "Please enter a positive integer!");
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT - 12,
                "Press S if you have pressed the final number.");

        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.setPenColor(Color.white);
        StdDraw.text(MENU_WIDTH / 2, MENU_HEIGHT / 2, s);
        StdDraw.show();
    }

    /** Draw the menu of the game. */
    private void drawMenu() {
        ter.initialize(MENU_WIDTH, MENU_HEIGHT);
        String gameTitle = "CS61B: THE GAME";
        String newGame = "New Game (N)";
        String loadGame = "Load Game (L)";
        String quit = "Quit and Save Game (Q)";
        int middleWidth = MENU_WIDTH / 2;

        StdDraw.clear(Color.black);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 40));
        StdDraw.setPenColor(Color.orange);
        StdDraw.text(middleWidth, MENU_HEIGHT - 5, gameTitle);

        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.setPenColor(Color.yellow);
        StdDraw.text(middleWidth, MENU_HEIGHT - 15, newGame);

        StdDraw.setPenColor(Color.green);
        StdDraw.text(middleWidth, MENU_HEIGHT - 20, loadGame);

        StdDraw.setPenColor(Color.red);
        StdDraw.text(middleWidth, MENU_HEIGHT - 25, quit);

        StdDraw.show();
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

        return worldMap.getWorld();
    }

    private World createOneWorld(long seed) {
        World world = new World(WIDTH, HEIGHT, seed);
        world.fillWithNothing();
        world.addManyRooms();
        world.addHallways();
        world.addPlayer();
        world.addFlags();
        return world;
    }

    /** Load the previous input from savedGame.txt to Game.java. */
    private static String loadInput() {
        File f = new File("./SavedGame.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                String loadInput = os.readObject().toString();
                os.close();
                return loadInput;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                exit(0);
            } catch (IOException e) {
                System.out.println(e);
                exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                exit(0);
            }
        }
        return "";
    }

    /** Save input to the file savedGame.txt. */
    private static void saveInput(String input) {
        File f = new File("./SavedGame.txt");
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
            exit(0);
        } catch (IOException e) {
            System.out.println(e);
            exit(0);
        }
    }
}
