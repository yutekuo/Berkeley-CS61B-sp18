package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

public class TestClorus {
    @Test
    public void testAttack() {
        Clorus clorus = new Clorus();
        Plip plip = new Plip();
        double expected = clorus.energy() + plip.energy();
        clorus.attack(plip);
        assertEquals(expected, clorus.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus clorus = new Clorus(2);
        Clorus offspring = clorus.replicate();
        assertNotSame(offspring, clorus);
        assertEquals(offspring.energy(), clorus.energy(), 0.01);
    }

    @Test
    public void testChooseAction() {
        // Test: If there are no empty squares, the Clorus will STAY.
        Clorus clorus = new Clorus(10);
 /*       HashMap<Direction, Occupant> surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());
        Action actual = clorus.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);
        assertEquals(expected, actual);

        /**
         * Test: Otherwise, if any Plips are seen,
         * the Clorus will ATTACK one of them randomly.
         */
        HashMap<Direction, Occupant> surrounded2 = new HashMap<>();
        surrounded2.put(Direction.TOP, new Plip());
        surrounded2.put(Direction.BOTTOM, new Empty());
        surrounded2.put(Direction.LEFT, new Impassible());
        surrounded2.put(Direction.RIGHT, new Impassible());
        Action actual2 = clorus.chooseAction(surrounded2);
        Action expected2 = new Action(Action.ActionType.ATTACK, Direction.TOP);
        assertEquals(expected2, actual2);

        /**
         * Otherwise, if the Clorus has energy greater than or equal to one,
         * it will REPLICATE to a random empty square.
         */
        HashMap<Direction, Occupant> surrounded3 = new HashMap<>();
        surrounded3.put(Direction.TOP, new Empty());
        surrounded3.put(Direction.BOTTOM, new Impassible());
        surrounded3.put(Direction.LEFT, new Impassible());
        surrounded3.put(Direction.RIGHT, new Impassible());
        Action actual3 = clorus.chooseAction(surrounded3);
        Action expected3 = new Action(Action.ActionType.REPLICATE, Direction.TOP);
        assertEquals(expected3, actual3);

        /** Otherwise, the Clorus will MOVE to a random empty square. */
        Clorus weakClorus = new Clorus(0.5);
        HashMap<Direction, Occupant> surrounded4 = new HashMap<>();
        surrounded4.put(Direction.TOP, new Empty());
        surrounded4.put(Direction.BOTTOM, new Impassible());
        surrounded4.put(Direction.LEFT, new Impassible());
        surrounded4.put(Direction.RIGHT, new Impassible());
        Action actual4 = weakClorus.chooseAction(surrounded4);
        Action expected4 = new Action(Action.ActionType.MOVE, Direction.TOP);
        assertEquals(expected4, actual4);
    }
}
