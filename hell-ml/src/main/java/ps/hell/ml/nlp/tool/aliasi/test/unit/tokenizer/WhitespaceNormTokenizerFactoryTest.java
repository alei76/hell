package ps.hell.ml.nlp.tool.aliasi.test.unit.tokenizer;

import ps.hell.ml.nlp.tool.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.TokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.WhitespaceNormTokenizerFactory;
import org.junit.Test;

import static ps.hell.ml.nlp.tool.aliasi.test.unit.Asserts.assertNotSerializable;
import static ps.hell.ml.nlp.tool.aliasi.test.unit.tokenizer.TokenizerTest.assertFactory;

public class WhitespaceNormTokenizerFactoryTest {

    @Test
    public void testFactory() {
        TokenizerFactory ieFactory  
            = IndoEuropeanTokenizerFactory.INSTANCE;
        TokenizerFactory factory
            = new WhitespaceNormTokenizerFactory(ieFactory);
        assertFactory(factory,
                      "",
                      new String[] { },
                      new String[] { "" });
        assertFactory(factory,
                      "a",
                      new String[] { "a" },
                      new String[] { "", "" });
        assertFactory(factory,
                      "\n\na   \n",
                      new String[] { "a" },
                      new String[] { " ", " " });
        assertFactory(factory,
                      " a bb\nc\n",
                      new String[] { "a", "bb", "c" },
                      new String[] { " ", " ", " ", " " });
    }

    @Test
    public void testNotSerializable() {
        WhitespaceNormTokenizerFactory unserializable
            = new WhitespaceNormTokenizerFactory(TokenizerTest
                                                .UNSERIALIZABLE_FACTORY);
        assertNotSerializable(unserializable);
    }

}
