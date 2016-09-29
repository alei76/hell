package ps.hell.ml.nlp.tool.aliasi.test.unit.features;

import ps.hell.ml.nlp.tool.aliasi.features.PrefixedFeatureExtractor;
import ps.hell.ml.nlp.tool.aliasi.util.AbstractExternalizable;
import ps.hell.ml.nlp.tool.aliasi.util.FeatureExtractor;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static ps.hell.ml.nlp.tool.aliasi.test.unit.features.MockFeatureExtractor.assertFeats;
import static junit.framework.Assert.assertNotNull;

public class PrefixedFeatureExtractorTest {

    @Test
    public void testAll() throws IOException {
        SerializableMockFeatureExtractor fe = new SerializableMockFeatureExtractor();
        fe.put(15,
               new String[] { "a", "b", "c" },
               new double[] { 1.5, 2.0, 3.0});
        fe.put(12,
               new String[0],
               new double[0]);
        
        assertNotNull(AbstractExternalizable.serializeDeserialize(fe));

        PrefixedFeatureExtractor<Integer> pfe
            = new PrefixedFeatureExtractor<Integer>("pref_", fe);
        
        Map<String,? extends Number> featMap = pfe.features(Integer.valueOf(15));

        assertFeats(pfe,
                    Integer.valueOf(15),
                    new String[] { "pref_a", "pref_b", "pref_c" },
                    new double[] { 1.5, 2.0, 3.0});
        assertFeats(pfe,
                    Integer.valueOf(12),
                    new String[] { },
                    new double[] { });

        @SuppressWarnings("unchecked")
        FeatureExtractor<Integer> pfeDeser
            = (FeatureExtractor<Integer>)
            AbstractExternalizable.serializeDeserialize(pfe);


        assertFeats(pfeDeser,
                    Integer.valueOf(15),
                    new String[] { "pref_a", "pref_b", "pref_c" },
                    new double[] { 1.5, 2.0, 3.0});
        assertFeats(pfeDeser,
                    Integer.valueOf(12),
                    new String[] { },
                    new double[] { });

    }

}