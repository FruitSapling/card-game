import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {


    @Test
    public void testGetValue() {

        Card card = new Card(4);

        assertEquals(4,card.getValue());

    }
}