/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/10 18:14</create-date>
 *
 * <copyright file="TestViterbi.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package ps.hell.ml.nlp.tool.hanlp.hankcs.test.seg;

import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.algoritm.Viterbi;
import junit.framework.TestCase;

import static ps.hell.ml.nlp.tool.hanlp.hankcs.test.seg.TestViterbi.Activity.*;
import static ps.hell.ml.nlp.tool.hanlp.hankcs.test.seg.TestViterbi.Weather.*;

/**
 * @author hankcs
 */
public class TestViterbi extends TestCase
{
    static enum Weather
    {
        Rainy,
        Sunny,
    }
    static enum Activity
    {
        walk,
        shop,
        clean,
    }
    static int[] states = new int[]{Weather.Rainy.ordinal(), Weather.Sunny.ordinal()};
    static int[] observations = new int[]{Activity.walk.ordinal(), Activity.shop.ordinal(), Activity.clean.ordinal()};
    double[] start_probability = new double[]{0.6, 0.4};
    double[][] transititon_probability = new double[][]{
            {0.7, 0.3},
            {0.4, 0.6},
    };
    double[][] emission_probability = new double[][]{
            {0.1, 0.4, 0.5},
            {0.6, 0.3, 0.1},
    };
    public void testCompute() throws Exception
    {
        for (int i = 0; i < start_probability.length; ++i)
        {
            start_probability[i] = -Math.log(start_probability[i]);
        }
        for (int i = 0; i < transititon_probability.length; ++i)
        {
            for (int j = 0; j < transititon_probability[i].length; ++j)
            {
                transititon_probability[i][j] = -Math.log(transititon_probability[i][j]);
            }
        }
        for (int i = 0; i < emission_probability.length; ++i)
        {
            for (int j = 0; j < emission_probability[i].length; ++j)
            {
                emission_probability[i][j] = -Math.log(emission_probability[i][j]);
            }
        }
        int[] result = Viterbi.compute(observations, states, start_probability, transititon_probability, emission_probability);
        for (int r : result)
        {
            System.out.print(Weather.values()[r] + " ");
        }
        System.out.println();
    }
}
