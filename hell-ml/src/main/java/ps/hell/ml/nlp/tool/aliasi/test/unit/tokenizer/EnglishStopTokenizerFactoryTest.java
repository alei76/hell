package ps.hell.ml.nlp.tool.aliasi.test.unit.tokenizer;

import ps.hell.ml.nlp.tool.aliasi.tokenizer.EnglishStopTokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.TokenizerFactory;
import org.junit.Test;

import static ps.hell.ml.nlp.tool.aliasi.test.unit.Asserts.assertNotSerializable;
import static ps.hell.ml.nlp.tool.aliasi.test.unit.tokenizer.TokenizerTest.assertFactory;

public class EnglishStopTokenizerFactoryTest {

    @Test
    public void testFactory() {
        TokenizerFactory ieFactory  
            = IndoEuropeanTokenizerFactory.INSTANCE;
        TokenizerFactory factory
            = new EnglishStopTokenizerFactory(ieFactory);
        assertFactory(factory,
                      "");
        assertFactory(factory,
                      "a");
        assertFactory(factory,
                      "the a");
        assertFactory(factory,
                      "the foo a",
                      "foo");
        assertFactory(factory,
                      "foo bar a the baz",
                      "foo","bar","baz");
    }

    @Test
    public void testNotSerializable() {
        EnglishStopTokenizerFactory unserializable
            = new EnglishStopTokenizerFactory(TokenizerTest
                                              .UNSERIALIZABLE_FACTORY);
        assertNotSerializable(unserializable);
    }


}
