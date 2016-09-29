package ps.hell.ml.nlp.tool.aliasi.test.unit.tokenizer;

import ps.hell.ml.nlp.tool.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.StopTokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.TokenizerFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static ps.hell.ml.nlp.tool.aliasi.test.unit.Asserts.assertNotSerializable;
import static ps.hell.ml.nlp.tool.aliasi.test.unit.tokenizer.TokenizerTest.assertFactory;

public class StopTokenizerFactoryTest {

    static final Set<String> TEST_STOP_SET
        = new HashSet<String>(Arrays.asList(new String[] {
                                                "a",
                                                "the",
                                                "it"
                                            }));

    @Test
    public void testFactory() {
        TokenizerFactory ieFactory  
            = IndoEuropeanTokenizerFactory.INSTANCE;
        TokenizerFactory factory
            = new StopTokenizerFactory(ieFactory,TEST_STOP_SET);
        assertFactory(factory,
                      "");
        assertFactory(factory,
                      "a");
        assertFactory(factory,
                      "a the");
        assertFactory(factory,
                      "The starling is flying it",
                      "The","starling","is","flying");
        assertFactory(factory,
                      "a AA A BBB",
                      "AA","A","BBB");
    }

    @Test
    public void testNotSerializable() {
        StopTokenizerFactory unserializable
            = new StopTokenizerFactory(TokenizerTest
                                       .UNSERIALIZABLE_FACTORY,
                                       TEST_STOP_SET);
        assertNotSerializable(unserializable);
    }

}
