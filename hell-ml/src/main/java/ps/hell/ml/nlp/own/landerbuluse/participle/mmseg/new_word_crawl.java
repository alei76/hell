package ps.hell.ml.nlp.own.landerbuluse.participle.mmseg;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class new_word_crawl {
	fenci_read_source read=new fenci_read_source();
	struct data=new struct();
	tree_fenci tree1=new tree_fenci();
	LinkedList<String> txt=new LinkedList<String>();
	public new_word_crawl(){};
	public void read_freq(struct ll)
	{
		//this.data=ll;
		tree1.buildtree(ll);
	}
	public void read_source()
	{	
		
		data=read.read_source("src/mmseg/new_word.dic", 1);
		//获取词库
		
		System.out.println("数据初始化成功");
		tree1.buildtree(data);
		//设定最常词为10个
	}
	public void read_txt(String filename,String code)
	{
		txt=read.read(filename,code);
		
	}
	public void read_txt_direct(String director,String code)
	{
		LinkedList<String> txt1=new LinkedList<String>();
		txt1=read.read_director(director);
		for(int i=0;i<txt1.size();i++)
		{
			System.out.println(txt1.get(i));
			LinkedList<String> txt2=new LinkedList<String>();
			txt2=read.read(txt1.get(i),code);
			for(int j=0;j<txt2.size();j++)
			{
				txt.add(txt2.get(j));
			}
			
		}
	}
	public LinkedList<String> read_direct_direct(String director)
	{
		LinkedList<String> txt1=new LinkedList<String>();
		txt1=read.read_director_director(director);
		return txt1;
	}
	public void computer(int flag_begin,int flag_end)//标记爬去几个
	{
		if(txt.isEmpty())
		{
			System.out.println("文件中没有内容");
			return;
		}
		//将信息隔开
		for(int i=0;i< txt.size();i++)
		{
			if(i%100==0)
			{
			System.out.println("文档总行数:"+txt.size()+" 文档读取行数"+i);
			}
			Pattern p=Pattern.compile("[\t\\s,\\.，。?？:\"'“”♂□<>」「『』〖〗‘→★⑤②③①╰つ◆﹏］/\\～＝=；\\(\\)\\-、0-9＜＞《》\\{\\}（）【】\\[\\]、|=+——~·~`！!#$%^&*@#￥%……&]");
			Matcher m=p.matcher(txt.get(i)+"。");
			int start=0;
			while(m.find())
			{
				//System.out.println(txt.get(i)+":"+txt.get(i).length());
				//System.out.println(start+"\t"+m.end());
				if(start>=txt.get(i).length())
				{
					break;
				}
				String ll=(txt.get(i)+"。").substring(start,m.end());
				//System.out.println(ll);
				for(int h=0;h<ll.length();h++)
				{
					String lll=ll.substring(h);
					for(int j=flag_begin-1;j<flag_end-1;j++)//只爬去2个以上的
					{
						if(j+1>=lll.length())
						{
							break;
						}
						//System.out.println(lll);
						//System.out.println("输入:"+lll.substring(0,j+1));
						tree1.insert3_add(lll.substring(0,j+1), "1");
					}
				}
				start=m.end();
			}
			//tree1.insert3_add("。", "-1");
		}
	}
	
}
