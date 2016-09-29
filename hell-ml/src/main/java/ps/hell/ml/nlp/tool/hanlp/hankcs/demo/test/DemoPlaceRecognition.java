/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/12/7 19:25</create-date>
 *
 * <copyright file="DemoChineseNameRecoginiton.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014+ 上海林原信息科技有限公司. All Right Reserved+ http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package ps.hell.ml.nlp.tool.hanlp.hankcs.demo.test;

import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.HanLP;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.seg.Segment;
import ps.hell.ml.nlp.tool.hanlp.hankcs.hanlp.seg.common.Term;
import ps.hell.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 地名识别
 * 
 * @author hankcs
 */
public class DemoPlaceRecognition {
	public static void main(String[] args) {
		FileUtil fileUtil = new FileUtil(System.getProperty("user.dir")
				+ "/src/com/hankcs/demo/address.txt", "utf-8", false, null);
		ArrayList<String> address = fileUtil.readAndClose();
		Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
		for (String sentence : address) {
			List<Term> termList = segment.seg(sentence);
			System.out.println(sentence+":::"+termList);
		}
	}
}
