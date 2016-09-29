/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/11/20 17:24</create-date>
 *
 * <copyright file="WordNatureDependencyParser.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.dependency;

import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.dependency.common.Edge;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.dependency.common.Node;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.model.bigram.WordNatureDependencyModel;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.seg.common.Term;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.tokenizer.NLPTokenizer;

import java.util.List;

/**
 * 一个简单的句法分析器
 *
 * @author hankcs
 */
public class WordNatureDependencyParser extends MinimumSpanningTreeParser
{
    static final WordNatureDependencyParser INSTANCE = new WordNatureDependencyParser();

    public static CoNLLSentence compute(List<Term> termList)
    {
        return INSTANCE.parse(termList);
    }

    public static CoNLLSentence compute(String text)
    {
        return compute(NLPTokenizer.segment(text));
    }

    @Override
    protected Edge makeEdge(Node[] nodeArray, int from, int to)
    {
        return WordNatureDependencyModel.getEdge(nodeArray[from], nodeArray[to]);
    }
}
