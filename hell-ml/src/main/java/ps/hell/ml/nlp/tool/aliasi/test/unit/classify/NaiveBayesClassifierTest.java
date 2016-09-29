package ps.hell.ml.nlp.tool.aliasi.test.unit.classify;

import ps.hell.ml.nlp.tool.aliasi.classify.NaiveBayesClassifier;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import org.junit.Test;
import static org.junit.Assert.*;


public class NaiveBayesClassifierTest  {

    @Test
    public void testOne() {
    NaiveBayesClassifier classifier
        = new NaiveBayesClassifier(new String[] { "a", "b", "c" },
                                   IndoEuropeanTokenizerFactory.INSTANCE);
    classifier.train("a","John Smith",1);
    classifier.train("a","John Smith",1);
    classifier.train("b","Fred Smith",1);
    classifier.train("b","Fred Smith",1);
    classifier.train("c","Fred Jones",1);
    classifier.train("c","Fred Jones",1);
    
    assertEquals("a",classifier.classify("John Smith").bestCategory());
    }

}
