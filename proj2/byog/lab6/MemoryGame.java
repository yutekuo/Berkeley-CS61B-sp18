package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        // Initialize random number generator.
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        // Generate random string of letters of length n.
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < n; i++) {
            randomString.append(CHARACTERS[rand.nextInt(CHARACTERS.length)]);
        }
        return randomString.toString();
    }

    public void drawFrame(String s) {
        StdDraw.clear();
        // If game is not over, display relevant game information at the top of the screen.
        if (!gameOver) {
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
            StdDraw.textLeft(1, height - 1, "Round: " + round);
            StdDraw.text(width * 0.5, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }

        // Take the string and display it in the center of the screen.
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.text(width * 0.5, height * 0.5, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        // Display each character in letters, making sure to blank the screen between letters.
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000); // 1 second
            drawFrame("");
            StdDraw.pause(500);  // 0.5 second
        }
    }

    public String solicitNCharsInput(int n) {
        // Read n letters of player input.
        StringBuilder input = new StringBuilder();
        while (input.length() < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            input.append(key);
            drawFrame(input.toString());
        }
        StdDraw.pause(500);
        return input.toString();
    }

    public void startGame() {
        // Set any relevant variables before the game starts.
        round = 1;
        gameOver = false;
        playerTurn = false;

        // Establish Game loop.
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round: " + round);
            StdDraw.pause(1500);

            String roundString = generateRandomString(round);
            flashSequence(roundString);

            playerTurn = true;
            String userInput = solicitNCharsInput(round);

            if (userInput.equals(roundString)) {
                round++;
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
            }
        }
    }

}
