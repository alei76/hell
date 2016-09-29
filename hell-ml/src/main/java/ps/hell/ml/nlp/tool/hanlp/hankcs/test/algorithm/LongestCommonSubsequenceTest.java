package ps.hell.ml.nlp.tool.hanlp.hankcs.test.algorithm;

import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.algoritm.LongestCommonSubsequence;
import junit.framework.TestCase;

public class LongestCommonSubsequenceTest extends TestCase
{
    String a = "Tom Hanks";
    String b = "Hankcs";
    public void testCompute() throws Exception
    {
        System.out.println(LongestCommonSubsequence.compute(a, b));
    }
}