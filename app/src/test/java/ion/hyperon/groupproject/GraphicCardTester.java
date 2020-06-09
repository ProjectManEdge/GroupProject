package ion.hyperon.groupproject;

import org.junit.Test;

import static org.junit.Assert.*;

public class GraphicCardTester {

    @Test
    public void testConstructor() {

        GraphicCard g = new GraphicCard();

        assertTrue("name is correct", g.name.equals("noName"));
        assertTrue("Manufacturer is correct", g.manufacturer.equals("Unknown"));


    }
}
