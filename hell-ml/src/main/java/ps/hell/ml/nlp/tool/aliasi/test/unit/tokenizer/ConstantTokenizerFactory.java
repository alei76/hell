package ps.hell.ml.nlp.tool.aliasi.test.unit.tokenizer;

import ps.hell.ml.nlp.tool.aliasi.tokenizer.Tokenizer;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.TokenizerFactory;

public  class ConstantTokenizerFactory implements TokenizerFactory {
    private final String[] mTokens;
    private final String[] mWhitespaces;
    public ConstantTokenizerFactory(String[] tokens,
                                    String[] whitespaces) {
        if (whitespaces.length != tokens.length + 1)
            throw new IllegalArgumentException("lengths bad");
        mTokens = tokens;
        mWhitespaces = whitespaces;
    }
    public Tokenizer tokenizer(char[] cs, int start, int length) {
        return new ConstantTokenizer(mTokens,mWhitespaces);
    }
    public Tokenizer transform(String s) {
        return tokenizer(s.toCharArray(),0,s.length());
    }
}


