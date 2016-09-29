package ps.hell.ml.nlp.tool.aliasi.test.unit.stats;

import ps.hell.ml.nlp.tool.aliasi.stats.PoissonConstant;
import org.junit.Test;

import java.io.*;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;
import static org.junit.Assert.*;

public class PoissonConstantTest  {
    
    @Test
    public void testOne() {
        PoissonConstant dist = new PoissonConstant(2.0);

    PoissonDistributionTest.assertPoissonTwo(dist);

    }

    @Test
    public void testCompile() throws ClassNotFoundException, IOException {
        PoissonConstant dist = new PoissonConstant(2.0);
    ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
    ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
    dist.compileTo(objOut);
    byte[] bytes = bytesOut.toByteArray();
    ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
    ObjectInputStream dataIn = new ObjectInputStream(bytesIn);
    PoissonConstant dist2 = (PoissonConstant) dataIn.readObject();
    
    PoissonDistributionTest.assertPoissonTwo(dist2);
    }

    @Test
    public void testExceptions() {
        PoissonConstant dist = new PoissonConstant(2.0);
    assertTrue(Double.NEGATIVE_INFINITY == dist.log2Probability(-1));
    assertEquals(0.0,dist.probability(-1),0.005);
    
    try {
        new PoissonConstant(-2.0);
    } catch (IllegalArgumentException e) {
        assertTrue(true);
    }
    
    }


}
