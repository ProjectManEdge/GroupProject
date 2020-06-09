package ion.hyperon.groupproject;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LoaderTest {

    @Test
    public void testConstructor() {

        Loader l = new Loader();

        assertTrue("Information has been loaded.", l.saveFileName.equals("savedfile.txt"));

    }
}
