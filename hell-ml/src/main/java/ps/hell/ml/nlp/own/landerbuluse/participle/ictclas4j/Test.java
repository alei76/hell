package ps.hell.ml.nlp.own.landerbuluse.participle.ictclas4j;
import org.ictclas4j.bean.SegResult;
import org.ictclas4j.segment.SegTag;

public class Test {
    public static void main(String[] args) throws Exception{
        SegTag st = new SegTag(1);
        String line = "这是ictclas4j中文分词测试程序。";
        SegResult sr = st.split(line);
        System.out.println(sr.getFinalResult());
        }
    }