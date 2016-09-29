package ps.hell.ml.nlp.tool.aliasi.test.unit.lm;

import ps.hell.ml.nlp.tool.aliasi.lm.LanguageModel;
import ps.hell.ml.nlp.tool.aliasi.lm.UniformBoundaryLM;
import org.junit.Test;

import java.io.*;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.*;

public class UniformBoundaryLMTest  {

    @Test
    public void testBoundary() {
        UniformBoundaryLM lm
            = new UniformBoundaryLM(31);
        assertEquals(-5.0,
                     lm.log2Estimate(new char[0],0,0),
                     0.005);
        lm.train("foo");
        assertEquals(-5.0,
                     lm.log2Estimate(new char[0],0,0),
                     0.005);
        assertEquals(-10.0,
                     lm.log2Estimate(new char[] { 'a' },0,1),
                     0.005);
        assertEquals(-15.0,
                     lm.log2Estimate(new char[] { 'a', 'b' },0,2),
                     0.005);
    }

    public static Object compileRead(LanguageModel.Dynamic model) 
        throws ClassNotFoundException, IOException {

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
        model.compileTo(objOut);
        byte[] modelBytes = bytesOut.toByteArray();
        ByteArrayInputStream bytesIn = new ByteArrayInputStream(modelBytes);
        ObjectInputStream objIn = new ObjectInputStream(bytesIn);
        return objIn.readObject();
    }

    @Test
    public void testSerializable() throws ClassNotFoundException, IOException {
        UniformBoundaryLM lm
            = new UniformBoundaryLM(31);

        Object serDeser = compileRead(lm);
        UniformBoundaryLM lmIO
            = (UniformBoundaryLM) serDeser;
        assertEquals(-5.0,
                     lmIO.log2Estimate(new char[0],0,0),
                     0.005);
        assertEquals(-10.0,
                     lmIO.log2Estimate(new char[] { 'a' },0,1),
                     0.005);
        assertEquals(-15.0,
                     lmIO.log2Estimate(new char[] { 'a', 'b' },0,2),
                     0.005);
    }

    @Test
    public void testExs() {
        try {
            new UniformBoundaryLM(-1);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            new UniformBoundaryLM(Integer.MAX_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            new UniformBoundaryLM(-1);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            new UniformBoundaryLM(Integer.MAX_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

    }

}
