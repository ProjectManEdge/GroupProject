package ion.hyperon.groupproject;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    public void testAssertStatement(){
        try{
            assertEquals(4, 2 + 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}