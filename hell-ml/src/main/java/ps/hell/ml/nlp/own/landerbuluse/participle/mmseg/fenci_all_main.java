package ps.hell.ml.nlp.own.landerbuluse.participle.mmseg;

import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

public class fenci_all_main {
	public static void main(String[] args)
	{
		new_word_crawl ll=new new_word_crawl();
		ll.read_source();
		fenci_read_source test=new fenci_read_source();
		//ll.read_txt("src/fenci/衙内当官.txt","gbk");
		//ll.read_txt_direct("e:/ML_test/微博语料库/test/IT/", "gbk");
		//拷贝搜索词频到bak中
		test.clear("src/mmseg/new_word_txt_freq_bak.txt","utf-8");
		test.f_copy_f("src/mmseg/new_word_txt_freq.txt","src/mmseg/new_word_txt_freq_bak.txt", "utf-8");
		//将数据读取到struct中并修改限制以删除无效关键字
		//删除3个一下的字段
		struct tes=new struct();
		tes=test.read_f_copy_f_to_linkedlist("src/mmseg/new_word_txt_freq.txt", "utf-8");
		System.out.println("tes.size():"+tes.size());
		//tes.remove_not_key();--速度超慢
		System.out.println("tes.size():"+tes.size());
		ll.read_freq(tes);
		ll.tree1.print();
		//		LinkedList<String> dir=new LinkedList<String>();
//		dir=ll.read_direct_direct("e:/ML_test/微博语料库/test/");
//		for(int i=0;i<dir.size();i++)
//		{
//			ll.read_txt_direct(dir.get(i),"gbk");
//		}
//		//ll.read_txt("src/fenci/new_word_txt.txt","utf-8");
//		System.out.println("文件总大小:"+ll.txt.size());
//		ll.computer(2,5);
//		//	lm=ll.tree1.get_print();
//		System.out.println("写入文件");
//		ll.tree1.print_txt("src/fenci/new_word_txt_freq.txt");//写入的读取库文件

	}
	public static void main1(String[] args)
	{
		//创建数据集
		struct data=new struct();
		fenci_read_source read=new fenci_read_source();
		data=read.read_source("src/mmseg/arrays.dic", 1);

		tree_fenci tree1=new tree_fenci();
		System.out.println("数据初始化成功");
		tree1.buildtree(data);
		
		String name="三总";
		System.out.println("查询词："+name+":value:"+tree1.search(name));
		Vector<Vector<String>> ll=new Vector<Vector<String>>();
		System.out.println(tree1.size()+":"+new Date());
//		ll=tree1.node3_search_words("三足");
//		System.out.println("end"+new Date());
//		for(int i=0;i<ll.size();i++)
//				System.out.println(ll.elementAt(i).elementAt(0)+"\t"+ll.elementAt(i).elementAt(1)+"\t"+new Date());
		String lll="民国时期，尤其是五四以来，中国遭受列强侵略，社会各种思潮流行，舶来文化冲击传统文化，中国小说的发展出现多元化，各类小说题材涌现，其中现代言情小说的发端鸳鸯蝴蝶派就出现在此时。正统小说的代表性人物有“鲁郭茅巴老曹”六大家。晚清民国报纸兴起为小说创作提供了一个上佳的舞台，报纸通过了连载小说招揽人气，小说家通过报纸赚取稿费。近现代几乎所有著名的小说家最初都是从报纸上连载小说开始，从鸳鸯蝴蝶派的张恨水到鲁迅再到当代金庸。"+
"第二时期为建国后到文革结束，即1976年以前，是小说的阶级斗争阶段。"+
"这一时期的大陆小说的带有明显的政治倾向，同时，这一时期的大陆文艺青年经历了重大的人生转变，命运的沉浮、多视角的阅历以及对价值的思考，为下一个时期的辉煌埋下了伏笔（中国第一位诺贝尔文学奖得主莫言的人生转变就在这一时期）。而在港台，这一时期的言情小说和武侠小说发展到了巅峰，分别产生了琼瑶时代和金庸时代。"+
"（3）第三时期为改革开放后二十多年的时期，即2003年以前，是小说的反思和蜕变阶段。"+
"这一时期的大陆小说展现了强劲的生命力，文革结束，对外开放，知识分子思想解放，对过去的反思，对未来的向往，传统和新时代的撞击，使得小说界出现欣欣向荣的勃勃生机。以莫言、贾平凹、陈忠实等为代表文革后作家，在此期间创作了许多经典作品，莫言更是凭借在此期间创作的文学作品和影响力，在2012年获得中国第一个诺贝尔文学奖。"+
"（4）第四时期为2003年至今，是小说的“表性”网络文学阶段。"+
"随着网络普及，网络文学的出现颠覆了传统的书写和传播模式，使小说的发展更加多元，80后90后的生力军开始步入文坛并展现了惊人的创作能力。以起点为代表的武侠玄幻小说作者群和以晋江为代表的言情小说作者群（四小天后、六小公主、八小玲珑）的整体出现，标志着网络小说已经成为主流文学之外的又一创作主体";
		LinkedList<LinkedList<String>> ml=new LinkedList<LinkedList<String>>();
		fenci_ali fenci=new fenci_ali();
		//ml=fenci.analyze(tree1,lll);
		//ml=fenci.analyze_negative(tree1,lll);
		ml=fenci.analyze_mmseg(tree1,lll);
		System.out.println(ml.size()+"ml");
		for(int i=0;i<ml.size();i++)
		{
			System.out.println("第"+i+"句");
			for(int j=0;j<ml.get(i).size();j++)
			{
				System.out.print(ml.get(i).get(j)+"\t");		
			}
			System.out.println();
			
		}
		
//		ll=tree1.node3_search_words("尤");
//		System.out.println("end"+new Date());
//		for(int i=0;i<ll.size();i++)
//				System.out.println(ll.elementAt(i).elementAt(0)+"\t"+ll.elementAt(i).elementAt(1)+"\t"+new Date());
		
		
		
	}
}
