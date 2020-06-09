package ion.hyperon.groupproject;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SaverTest {

    @Test
    public void testSaverConstructor() {

        Saver newSave = new Saver();

        assertTrue("file name is correct", newSave.fileName.equals("graphicCardInfo.txt"));


    }
}