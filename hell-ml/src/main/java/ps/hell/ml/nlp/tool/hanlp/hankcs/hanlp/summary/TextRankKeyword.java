package ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.summary;


import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.seg.common.Term;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.tokenizer.StandardTokenizer;

import java.util.*;

/**
 * 基于TextRank算法的关键字提取，适用于单文档
 * @author hankcs
 */
public class TextRankKeyword extends KeywordExtractor
{
    /**
     * 提取多少个关键字
     */
    int nKeyword = 10;
    /**
     * 阻尼系数（ＤａｍｐｉｎｇＦａｃｔｏｒ），一般取值为0.85
     */
    final static float d = 0.85f;
    /**
     * 最大迭代次数
     */
    final static int max_iter = 200;
    final static float min_diff = 0.001f;

    /**
     * 提取关键词
     * @param document 文档内容
     * @param size 希望提取几个关键词
     * @return 一个列表
     */
    public static List<String> getKeywordList(String document, int size)
    {
        TextRankKeyword textRankKeyword = new TextRankKeyword();
        textRankKeyword.nKeyword = size;

        return textRankKeyword.getKeyword(document);
    }

    public List<String> getKeyword(String content)
    {
    	//基础分词
        List<Term> termList = StandardTokenizer.segment(content);
        List<String> wordList = new ArrayList<String>();
        for (Term t : termList)
        {
        	//去除停用词 等 去除英文的某些首字母
            if (shouldInclude(t))
            {
                wordList.add(t.word);
            }
        }
//        System.out.println(wordList);
        Map<String, Set<String>> words = new TreeMap<String, Set<String>>();
        Queue<String> que = new LinkedList<String>();
        for (String w : wordList)
        {
            if (!words.containsKey(w))
            {
                words.put(w, new TreeSet<String>());
            }
            que.offer(w);
            //一个队列只存储5个数据
            if (que.size() > 5)
            {
                que.poll();
            }

            for (String w1 : que)
            {
                for (String w2 : que)
                {
                    if (w1.equals(w2))
                    {
                        continue;
                    }
                    //做成数组对形式
                    words.get(w1).add(w2);
                    words.get(w2).add(w1);
                }
            }
        }
//        System.out.println(words);
        Map<String, Float> score = new HashMap<String, Float>();
        for (int i = 0; i < max_iter; ++i)
        {
            Map<String, Float> m = new HashMap<String, Float>();
            float max_diff = 0;
            for (Map.Entry<String, Set<String>> entry : words.entrySet())
            {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                //使用 textRank公式 获取key的得分
                m.put(key, 1 - d);
                for (String element : value)
                {
                    int size = words.get(element).size();
                    if (key.equals(element) || size == 0) continue;
                    m.put(key, m.get(key) + d / size * (score.get(element) == null ? 0 : score.get(element)));
                }
                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }
            score = m;
            if (max_diff <= min_diff) break;
        }
        List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(score.entrySet());
        //重新排序
        Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>()
        {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2)
            {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
//        System.out.println(entryList);
        int limit = Math.min(nKeyword, entryList.size());
        //获取前limit个主题词语
        List<String> result = new ArrayList<String>(limit);
        for (int i = 0; i < limit; ++i)
        {
            result.add(entryList.get(i).getKey()) ;
        }
        return result;
    }

}
