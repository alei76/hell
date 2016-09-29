package ps.hell.ml.nlp.tool.aliasi.test.unit.features;

import ps.hell.ml.nlp.tool.aliasi.features.CacheFeatureExtractor;
import ps.hell.ml.nlp.tool.aliasi.util.AbstractExternalizable;
import ps.hell.ml.nlp.tool.aliasi.util.FeatureExtractor;
import org.junit.Test;

import java.io.IOException;
import java.io.NotSerializableException;
import java.util.HashMap;
import java.util.Map;

import static ps.hell.ml.nlp.tool.aliasi.test.unit.features.MockFeatureExtractor.assertFeats;
import static org.junit.Assert.*;

public class CacheFeatureExtractorTest {

    @Test
    public void testCache() {
        MockFeatureExtractor mfe = new MockFeatureExtractor();
        mfe.put(1, new String[] { "A", "B" }, new double[] { 10.0, 2.0 });
        mfe.put(2, new String[] { "C" }, new double[] { -50.0 });
                
        CacheFeatureExtractor<Integer> cfe
            = new CacheFeatureExtractor<Integer>(mfe,
                                                 new HashMap<Integer,Map<String,? extends Number>>());
        
        assertFeats(cfe,1,new String[] { "A", "B" }, new double[] { 10.0, 2.0 });
        // go again and make sure cache doesn't mess up
        assertFeats(cfe,1,new String[] { "A", "B" }, new double[] { 10.0, 2.0 });
        assertFeats(cfe,2,new String[] { "C" }, new double[] { -50.0 });
        assertFeats(cfe,2,new String[] { "C" }, new double[] { -50.0 });
        assertFeats(cfe,1,new String[] { "A", "B" }, new double[] { 10.0, 2.0 });
    }

    @Test
    public void testSerialize() throws IOException, ClassNotFoundException {
        SerializableMockFeatureExtractor mfe = new SerializableMockFeatureExtractor();
        mfe.put(1, new String[] { "A", "B" }, new double[] { 10.0, 2.0 });
        mfe.put(2, new String[] { "C" }, new double[] { -50.0 });
                
        CacheFeatureExtractor<Integer> cfe
            = new CacheFeatureExtractor<Integer>(mfe,
                                                 new HashMap<Integer,Map<String,? extends Number>>());

        assertFeats(cfe,1,new String[] { "A", "B" }, new double[] { 10.0, 2.0 });

        FeatureExtractor<Integer> cfe2
            = (FeatureExtractor<Integer>) AbstractExternalizable.serializeDeserialize(cfe);
        
        assertFeats(cfe2,1,new String[] { "A", "B" }, new double[] { 10.0, 2.0 });
        assertFeats(cfe,2,new String[] { "C" }, new double[] { -50.0 });
    }

    @Test(expected = NotSerializableException.class)
    public void testUnSerialize() throws IOException, ClassNotFoundException {
        MockFeatureExtractor mfe = new MockFeatureExtractor();
        CacheFeatureExtractor cacheFe 
            = new CacheFeatureExtractor(mfe,
                                        new HashMap<Integer,Map<String,? extends Number>>());
                                                                  
        AbstractExternalizable.serializeDeserialize(cacheFe);
    }


}
