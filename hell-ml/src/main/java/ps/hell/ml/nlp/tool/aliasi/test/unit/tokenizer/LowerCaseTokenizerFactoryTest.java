package ps.hell.ml.nlp.tool.aliasi.test.unit.tokenizer;

import ps.hell.ml.nlp.tool.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.LowerCaseTokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.TokenizerFactory;
import org.junit.Test;

import static ps.hell.ml.nlp.tool.aliasi.test.unit.Asserts.assertNotSerializable;
import static ps.hell.ml.nlp.tool.aliasi.test.unit.tokenizer.TokenizerTest.assertFactory;

public class LowerCaseTokenizerFactoryTest {

    @Test
    public void testFactory() {
        TokenizerFactory ieFactory  
            = IndoEuropeanTokenizerFactory.INSTANCE;
        TokenizerFactory factory
            = new LowerCaseTokenizerFactory(ieFactory);
        assertFactory(factory,
                      "");
        assertFactory(factory,
                      "a",
                      "a");
        assertFactory(factory,
                      "A",
                      "a");
        assertFactory(factory,
                      "Mr. John Smith is 47 today.",
                      "mr",".","john","smith","is","47","today",".");
    }

    @Test
    public void testNotSerializable() {
        LowerCaseTokenizerFactory unserializable
            = new LowerCaseTokenizerFactory(TokenizerTest
                                            .UNSERIALIZABLE_FACTORY);
        assertNotSerializable(unserializable);
    }


}
