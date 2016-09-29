package ps.hell.ml.nlp.tool.aliasi.test.unit.io;

import ps.hell.ml.nlp.tool.aliasi.io.LogLevel;
import org.junit.Test;
import static org.junit.Assert.*;

public class LogLevelTest {

    @Test
    public void testToString() {
        assertEquals("WARN",LogLevel.WARN.toString());
    }

    @Test
    public void testComparator() {
        LogLevel[] levels
            = new LogLevel[] { LogLevel.ALL, 
                               LogLevel.DEBUG,
                               LogLevel.INFO,
                               LogLevel.WARN,
                               LogLevel.ERROR,
                               LogLevel.FATAL,
                               LogLevel.NONE };
        for (int i = 0; i < levels.length; ++i) {
            assertEquals(0,LogLevel.COMPARATOR.compare(levels[i],levels[i]));
            for (int j = i+1; j < levels.length; ++j) {
                assertEquals(-1,LogLevel.COMPARATOR.compare(levels[i],levels[j]));
                assertEquals(1,LogLevel.COMPARATOR.compare(levels[j],levels[i]));
            }
        }
    }
        
}
