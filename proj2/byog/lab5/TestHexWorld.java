package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static byog.lab5.HexWorld.*;

public class TestHexWorld {
    @Test
    public void testHexRowWidth() {
        assertEquals(4, hexRowWidth(4, 0));
        assertEquals(6, hexRowWidth(4, 1));
        assertEquals(8, hexRowWidth(4, 2));
        assertEquals(10, hexRowWidth(4, 3));
        assertEquals(10, hexRowWidth(4, 4));
        assertEquals(8, hexRowWidth(4, 5));
        assertEquals(6, hexRowWidth(4, 6));
        assertEquals(4, hexRowWidth(4, 7));
    }

    @Test
    public void testHexRowOffset() {
        assertEquals(0, hexRowOffset(3, 5));
        assertEquals(-1, hexRowOffset(3, 4));
        assertEquals(-2, hexRowOffset(3, 3));
        assertEquals(-2, hexRowOffset(3, 2));
        assertEquals(-1, hexRowOffset(3, 1));
        assertEquals(0, hexRowOffset(3, 0));
        assertEquals(0, hexRowOffset(2, 0));
        assertEquals(-1, hexRowOffset(2, 1));
        assertEquals(-1, hexRowOffset(2, 2));
        assertEquals(0, hexRowOffset(2, 3));
    }
}
