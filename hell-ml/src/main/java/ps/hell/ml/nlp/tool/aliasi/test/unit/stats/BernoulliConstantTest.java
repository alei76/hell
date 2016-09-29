package ps.hell.ml.nlp.tool.aliasi.test.unit.stats;

import ps.hell.ml.nlp.tool.aliasi.stats.BernoulliConstant;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class BernoulliConstantTest  {

    @Test
    public void testOne() throws IOException, ClassNotFoundException {
    BernoulliConstant coin = new BernoulliConstant(0.5);
    BernoulliEstimatorTest.assertFairCoin(coin);

    try {
        new BernoulliConstant(1.2);
        fail();
    } catch (IllegalArgumentException e) {
        assertTrue(true);
    }

    try {
        new BernoulliConstant(-1.2);
        fail();
    } catch (IllegalArgumentException e) {
        assertTrue(true);
    }

    }

}
