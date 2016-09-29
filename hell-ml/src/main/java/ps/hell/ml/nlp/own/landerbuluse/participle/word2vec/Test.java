package ps.hell.ml.nlp.own.landerbuluse.participle.word2vec;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        Word2VEC w1 = new Word2VEC() ;
    	String model=System.getProperty("user.dir")
				+ "/config/dataset/Text/word2vec/model";
        w1.loadGoogleModel(model) ;
        
        System.out.println(w1.distance("奥尼尔"));
        
        System.out.println(w1.distance("毛泽东"));
        
        System.out.println(w1.distance("邓小平"));
        
        
        System.out.println(w1.distance("魔术队"));
        
        System.out.println(w1.distance("魔术"));
        
    }
}
