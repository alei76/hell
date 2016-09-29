package ps.hell.ml.nlp.tool.aliasi.test.unit.tokenizer;

import ps.hell.ml.nlp.tool.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.RegExFilteredTokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.TokenizerFactory;
import org.junit.Test;

import java.util.regex.Pattern;

import static ps.hell.ml.nlp.tool.aliasi.test.unit.Asserts.assertNotSerializable;
import static ps.hell.ml.nlp.tool.aliasi.test.unit.tokenizer.TokenizerTest.assertFactory;

public class RegExFilteredTokenizerFactoryTest {

    // two or more capital letters only
    static final Pattern TEST_PATTERN = Pattern.compile("[A-Z][A-Z]+");

    @Test
    public void testFactory() {
        TokenizerFactory ieFactory  
            = IndoEuropeanTokenizerFactory.INSTANCE;
        TokenizerFactory factory
            = new RegExFilteredTokenizerFactory(ieFactory,
                                                TEST_PATTERN);
        assertFactory(factory,
                      "");
        assertFactory(factory,
                      "a");
        assertFactory(factory,
                      "A");
        assertFactory(factory,
                      "The starling is flying towards home smiling happily");
        assertFactory(factory,
                      "a AA A BBB c DDD",
                      "AA","BBB","DDD");
    }

    @Test
    public void testNotSerializable() {
        RegExFilteredTokenizerFactory unserializable
            = new RegExFilteredTokenizerFactory(TokenizerTest
                                                .UNSERIALIZABLE_FACTORY,
                                                TEST_PATTERN);
        assertNotSerializable(unserializable);
    }

}
