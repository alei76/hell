
package ps.hell.ml.nlp.tool.aliasi.test.unit.features;

import ps.hell.ml.nlp.tool.aliasi.features.KnownFeatureExtractor;
import ps.hell.ml.nlp.tool.aliasi.util.AbstractExternalizable;
import ps.hell.ml.nlp.tool.aliasi.util.FeatureExtractor;
import ps.hell.ml.nlp.tool.aliasi.util.ObjectToDoubleMap;
import org.junit.Test;

import java.io.IOException;
import java.io.NotSerializableException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class KnownFeatureExtractorTest {

    @Test
    public void testBasic() {
        ObjectToDoubleMap<String> feats1 = new ObjectToDoubleMap<String>();
        feats1.put("A",10.0);
        feats1.put("B",2.0);
        feats1.put("C",-50.0);

        MockFeatureExtractor mfe = new MockFeatureExtractor();
        mfe.put(1,feats1);

        Set<String> knownFeatureSet = new HashSet<String>(Arrays.asList(new String[] { "A", "C" }));
        KnownFeatureExtractor<Integer> knownFe = new KnownFeatureExtractor(mfe,knownFeatureSet);
        assertEquals(knownFeatureSet,knownFe.knownFeatureSet());
        Map<String,? extends Number> feats = knownFe.features(1);
        assertEquals(10.0,feats.get("A"));
        assertNull(feats.get("B"));
        assertEquals(-50.0,feats.get("C"));
    }

    @Test
    public void testSerialize() throws IOException, ClassNotFoundException {
        ObjectToDoubleMap<String> feats1 = new ObjectToDoubleMap<String>();
        feats1.put("A",10.0);
        feats1.put("B",2.0);
        feats1.put("C",-50.0);

        SerializableMockFeatureExtractor mfe = new SerializableMockFeatureExtractor();
        mfe.put(1,feats1);

        Set<String> knownFeatureSet = new HashSet<String>(Arrays.asList(new String[] { "A", "C" }));
        KnownFeatureExtractor<Integer> knownFe 
            = new KnownFeatureExtractor<Integer>(mfe,knownFeatureSet);

        FeatureExtractor<Integer> knownFe2
            = (FeatureExtractor<Integer>) AbstractExternalizable.serializeDeserialize(knownFe);

        Map<String,? extends Number> feats = knownFe2.features(1);
        assertEquals(10.0,feats.get("A"));
        assertNull(feats.get("B"));
        assertEquals(-50.0,feats.get("C"));
    }

    @Test(expected = NotSerializableException.class)
    public void testUnSerialize() throws IOException, ClassNotFoundException {
        MockFeatureExtractor mfe = new MockFeatureExtractor();
        Set<String> knownSet = new HashSet<String>();
        KnownFeatureExtractor<Integer> knownFe = new KnownFeatureExtractor<Integer>(mfe,knownSet);
        AbstractExternalizable.serializeDeserialize(knownFe);
    }

}
