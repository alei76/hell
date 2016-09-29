package ps.hell.ml.nlp.tool.aliasi.test.unit.tag;

import ps.hell.ml.nlp.tool.aliasi.corpus.ObjectHandler;
import ps.hell.ml.nlp.tool.aliasi.tag.LineTaggingParser;
import ps.hell.ml.nlp.tool.aliasi.tag.Tagging;
import org.junit.Test;

public class LineTaggingParserTest {
    
    @Test
    public void testLfCr() {
        String lfcr = "foo Noun\r\n";
        char[] cs = lfcr.toCharArray();
        LineTaggingParser parser 
            = new LineTaggingParser("(foo) (Noun)",1,2,"nothing","nothing");

        parser.setHandler(new ObjectHandler<Tagging<String>> () {
                public void handle(Tagging<String> tagging) {
                    // System.out.println(tagging);
                }
            });
        
        parser.parseString(cs,0,cs.length);
    }
    
    @Test
        public void testLfCrMulti() {
        String lfcr = "foo Noun\r\nfoo Noun\r\r";
        char[] cs = lfcr.toCharArray();
        LineTaggingParser parser 
            = new LineTaggingParser("(foo) (Noun)",1,2,"nothing","nothing");

        parser.setHandler(new ObjectHandler<Tagging<String>> () {
                public void handle(Tagging<String> tagging) {
                    // System.out.println(tagging);
                }
            });
        
        parser.parseString(cs,0,cs.length);
    }
    
    @Test //just not dying right now.
        public void testLf() {
        String lfcr = "foo Noun\n";
        char[] cs = lfcr.toCharArray();
        LineTaggingParser parser 
            = new LineTaggingParser("(foo) (Noun)",1,2,"nothing","nothing");

        parser.setHandler(new ObjectHandler<Tagging<String>> () {
                public void handle(Tagging<String> tagging) {
                    // System.out.println(tagging);
                }
            });
        
        parser.parseString(cs,0,cs.length);
    }

    @Test 
        public void testCr() {
        String lfcr = "foo Noun\r";
        char[] cs = lfcr.toCharArray();
        LineTaggingParser parser 
            = new LineTaggingParser("(foo) (Noun)",1,2,"nothing","nothing");

        parser.setHandler(new ObjectHandler<Tagging<String>> () {
                public void handle(Tagging<String> tagging) {
                    // System.out.println(tagging);
                }
            });
        
        parser.parseString(cs,0,cs.length);
    }

}