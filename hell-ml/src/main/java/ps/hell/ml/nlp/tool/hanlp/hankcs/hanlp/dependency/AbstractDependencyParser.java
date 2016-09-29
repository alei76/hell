/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/11/20 17:29</create-date>
 *
 * <copyright file="AbstractDependencyParser.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.dependency;

import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.seg.common.Term;

import java.util.List;

/**
 * @author hankcs
 */
public abstract class AbstractDependencyParser
{
    public abstract CoNLLSentence parse(List<Term> termList);
}
