package ps.hell.ml.nlp.own.landerbuluse.participle.mmseg;

import java.util.LinkedList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class fenci_ali {
	
	public fenci_ali()
	{}
	//mmseg调用算法
	/**
	 * mmseg调度函数
	 */
	public LinkedList<LinkedList<String>> analyze_mmseg(tree_fenci tre,String str)
	{
		
		LinkedList<LinkedList<String>> strr=new  LinkedList<LinkedList<String>>();
		Pattern p=Pattern.compile("[,\\.，。?:\"'“”<>\\(\\)-、]");
		Matcher m=p.matcher(str);
		int start=0;
		int end=0;
		//int flag=0;
		while(m.find())
		{
			
			//flag++;
			 end=m.end();
			 System.out.println("查询到:"+str.substring(start,end));
			 LinkedList<String> strr1=new  LinkedList<String>();
			 strr1=mmseg(tre,str.substring(start,end));
			 strr.add(strr1);
			 start=end;
		}
		if(!str.isEmpty())
		{
			System.out.println("查询："+start+"\t"+str.length());
			System.out.println("查询到222:"+str.substring(start-1));
			 LinkedList<String> strr1=new  LinkedList<String>();
			 System.out.println("查询222："+start+"\t"+str.length());
			 if(str.substring(start-1)!=""&&!str.substring(start-1).isEmpty())
			 {
				 strr1=mmseg(tre,str.substring(start-1));
				 strr.add(strr1);
			 }
		}
		return strr;
	}
	//
	//mmseg主程序
	public LinkedList<String> mmseg(tree_fenci tre,String str)
	{
		LinkedList<String> strr=new  LinkedList<String>();
		//规则一如果是唯一的一个单词则用简单最大匹配
		//如果是多个值的那么执行复杂对打匹配
		//简单最大匹配
		//调用前向最大匹配方法中的获取代码
		int i=0;
		while(true)
		{
			if(i==str.length())
			{
				System.out.println("句子查询结束");
				break;
			}
			Vector<Vector<String>> temp=new Vector<Vector<String>>();
			temp=tre.node3_search_words(str.substring(i,i+1));
			System.out.println("查询的第一个字为:"+str.substring(i,i+1));
			if(temp.isEmpty())
			{//如果没有没有搜索到//那么执行简单最大匹配
				strr.add(str.substring(i,i+1));
				System.out.println("没有搜索到结果添加单个字:"+str.substring(i,i+1));
				i++;
				continue;
			}
			else
			{	
				Vector<Vector<String>> temp1=new Vector<Vector<String>>();
				//用于存储有效字符
				for(int ii=0;ii<temp.size();ii++)
				{//获取字符长度
					//System.out.println("temp:"+temp.get(ii).size());
					//System.out.println(temp.get(ii).get(0)+"\t"+temp.get(ii).get(1));
					int len=temp.get(ii).get(0).length();
					if(temp.get(ii).get(0).compareTo(str.substring(i, i+len>str.length()?str.length():i+len))==0)
					{
						temp1.add(temp.get(ii));		
					}
				}
				//查看temp1中字符串的形式
				if(temp1.isEmpty())
				{//如果没有匹配出字符
					strr.add(str.substring(i,i+1));
					System.out.println("str中不存在字典中的字:"+str.substring(i,i+1));
					i++;
					continue;
				}
				else if(temp1.size()==1)
				{//如果只是一个那么就简单匹配---直接添加
					strr.add(temp1.get(0).get(0));
					i+=temp1.get(0).get(0).length();
					System.out.println("只有一个字并且在str中:"+temp1.get(0).get(0));
					continue;
				}
				else
				{
					//如果为多个那么就执行复杂匹配
					//获取前三个字符如果没有三个那么就去最少的几个
					Vector<Vector<String>> temp_all=new Vector<Vector<String>>();
					//用来存储所有的三个字符串
					for(int iii=0;iii<temp1.size();iii++)
					{
						//对每一个查询对应的查询出所有有效的最多三个chunk
						String name=temp1.get(iii).get(0);//第一个词
						String value=temp1.get(iii).get(1);//获取name对应的频度
						System.out.println("name:"+name+"\tvalue:"+value);
						int len_name=name.length();
						int j=i+len_name;
						if(j+1<str.length())
						{
							//获取第二个字符串						
							int j_2=0;
							String name2;
							String value_2;
							int val_flag_2=0;
							while(true)
							{
								j_2++;
								if(j_2>tre.size_length()||j+j_2>str.length())
								{//如果超长则退出
									break;
								}
								name2=str.substring(j,j+j_2);
								value_2=tre.search(name2);
								System.out.println("name2:"+name2);
								if(value_2.isEmpty()||value_2.compareTo("")==0)
								{
									continue;
								}
								else
								{//如果查询到了就开始查询第三个
									System.out.println("val_flag_2:"+name2+":"+value_2);
									val_flag_2=1;//将第二个chunk是否查找到标记为是
									int j_3=1;
									String name3;
									String value_3;
									int val_flag_3=0;//标记是否查询到
									while(true)
									{
										if(j_3>tre.size_length()||j+j_2+j_3>str.length())
										{//如果超长则退出
											break;
										}
										name3=str.substring(j+j_2,j+j_2+j_3);
										System.out.println("name3:"+name3);
										value_3=tre.search(name3);
										j_3++;
										if(value_3.isEmpty()||value_3.compareTo("")==0)
										{
											continue;
										}
										else
										{//如果查询到了第三个就添加到tempall中
											System.out.println("添加:"+name+"\t"+name2+"\t"+name3);
											val_flag_3=1;//修改flag
											Vector<String> temp_2_chunk=new Vector<String>();
											temp_2_chunk.add(name);
											temp_2_chunk.add(value);
											temp_2_chunk.add(name2);
											temp_2_chunk.add(value_2);
											temp_2_chunk.add(name3);
											temp_2_chunk.add(value_3);
											temp_all.add(temp_2_chunk);
										}
									}
									if(val_flag_3==0)
									{//第三个没有查询到
										//判断长度
										Vector<String> temp_2_chunk=new Vector<String>();
										temp_2_chunk.add(name);
										temp_2_chunk.add(value);
										temp_2_chunk.add(name2);
										temp_2_chunk.add(value_2);
										if(j+j_2+1>str.length())
										{//没有长度了
											//只添加2个chunk		
											System.out.println("只添加了2个chunk");
										}
										else
										{
											//添加一个词
											System.out.println("添加第三个为1个词并且在字典中没有查到,value标记为1");
											temp_2_chunk.add(str.substring(j+j_2,j+j_2+1));
											temp_2_chunk.add("1");//将所有没有被查询到的返回为1
											//temp_all.add(temp_2_chunk);
										}
										temp_all.add(temp_2_chunk);
									}
								}
								
							}
							//如果没有搜索到
							//获取有效的字符
							System.out.println("第一个词查询完毕:"+val_flag_2);
//							if(val_flag_2==0)
//							{//如果第二个chunk没有查找到
//								System.out.println("chunk只有一个字:"+name);
//								Vector<String> temp_2_chunk1=new Vector<String>();
//								temp_2_chunk1.add(name);
//								temp_2_chunk1.add(value);
//								temp_all.add(temp_2_chunk1);
//							}
						}
						else
						{
							//只有第一个字符已经到str的底步
							//那么只添加这个字符串
							System.out.println("chunk只有一个字并似乎str中最后一个字:"+name);
							Vector<String> temp_2_chunk1=new Vector<String>();
							temp_2_chunk1.add(name);
							temp_2_chunk1.add(value);
							temp_all.add(temp_2_chunk1);						
						}
						
					}
					//将所有temp_all中的数据进行比较
					System.out.println("temp_all中的所有字段显示出来");
					for(int q=0;q<temp_all.size();q++)
					{
						for(int q1=0;q1<temp_all.get(q).size();q1++)
						{
							System.out.print(temp_all.get(q).get(q1)+"\t");
						}
						System.out.println();
					}
					//规则2找品均词长最长的
					Vector<Float> chunk_avg_vec=new Vector<Float>();//存储均值
					Vector<Float> chunk_std_vec=new Vector<Float>();//存储最小方差
					float chunk_avg=-1;
					int chunk_index=-1;
					int length1=0;//用来存储3个长度
					int length2=0;
					int length3=0;
					System.out.println("temp_all.size():"+temp_all.size());
					for(int p=0;p<temp_all.size();p++)
					{
						int chunk_num=0;//存储chunk数量
						int chunk_len=0;//存储chunks的长度
						for(int p2=0;p2<temp_all.get(p).size();p2+=2)
						{
							if(p2==0)
							{
								length1=temp_all.get(p).get(p2).length();
							}
							if(p2==2)
							{
								length2=temp_all.get(p).get(p2).length();
							}
							if(p2==4)
							{
								length3=temp_all.get(p).get(p2).length();
							}
							chunk_num++;
							chunk_len+=temp_all.get(p).get(p2).length();
						}
						float chunk_avg1=(float)chunk_len/chunk_num;
						chunk_avg_vec.add(chunk_avg1);
						if(chunk_avg1>chunk_avg)
						{
							chunk_index=p;
							chunk_avg=chunk_avg1;
						}
						//计算方差
						float std=0;
						if(length1!=0)
							std=(length1-chunk_avg1)*(length1-chunk_avg1);
						if(length2!=0)
							std+=(length2-chunk_avg1)*(length1-chunk_avg1);
						if(length3!=0)
							std+=(length3-chunk_avg1)*(length1-chunk_avg1);
						std/=chunk_num;
						chunk_std_vec.add(std);//将方差存储上
					}
					//将最终得到的chunk_index存储如
					//查询有几个最大的值、、如果只有一个那么添加，如果是多个则判断方差
					Vector<Integer> xulie=new Vector<Integer>();
					System.out.println("chunk_avg:"+chunk_avg);
					for(int p=0;p<chunk_avg_vec.size();p++)
					{					
						if(Math.abs(chunk_avg_vec.get(p)-chunk_avg)<1E-5)
						{
							xulie.add(p);
						}	
					}
					if(xulie.size()==1)
					{
						//直接添加
						i+=temp_all.get(xulie.get(0)).get(0).length();
						strr.add(temp_all.get(xulie.get(0)).get(0));
					}
					else
					{//需要判断方差最小
						float std_temp=Float.MAX_VALUE;
						for(int p2=0;p2<xulie.size();p2++)
						{
							if(chunk_std_vec.get(xulie.get(p2))<std_temp)
							{
								std_temp=chunk_std_vec.get(xulie.get(p2));
							}
						}
						System.out.println("std_temp:"+std_temp);
						//查询最小方差数量
						Vector<Integer> xulie_std=new Vector<Integer>();//存储最小方差对应的序号
						
						for(int p2=0;p2<xulie.size();p2++)
						{
							if(Math.abs(chunk_std_vec.get(xulie.get(p2))-std_temp)<1E-5)
							{
								xulie_std.add(xulie.get(p2));
							}
						}
						System.out.println("xulie_std长度:"+xulie_std.size());
						if(xulie_std.size()>1)
						{//规则4需要查询语素自由度的最大和 最大的如果相同取第一个
							int end_index=-1;//存储的最后的序号
							int end_sum=0;
							for(int p3=0;p3<xulie_std.size();p3++)
							{
								int sum=0;
								for(int p4=0;p4<temp_all.get(xulie_std.get(p3)).size();p4+=2)
								{
									System.out.println("xulie_:"+xulie_std.get(p3));
									sum+=Integer.parseInt(temp_all.get(xulie_std.get(p3)).get(p4+1));
								}
								//判断
								if(sum>end_sum)
								{
									end_index=p3;
									end_sum=sum;
								}
							}
							i+=temp_all.get(end_index).get(0).length();
							strr.add(temp_all.get(end_index).get(0));
						}
						else
						{//只有一个最小方差
							i+=temp_all.get(xulie_std.get(0)).get(0).length();
							strr.add(temp_all.get(xulie_std.get(0)).get(0));
						}
					}
				}
				
				
			}
		}
		return strr;
	}
	
	//正向最大匹配方法
	//调用方法analyze(struct ,str)


	
	public  LinkedList<LinkedList<String>> analyze(tree_fenci tre,String str)
	{
		//首先将特殊的间断语句给拆分出来
		LinkedList<LinkedList<String>> strr=new  LinkedList<LinkedList<String>>();
		Pattern p=Pattern.compile("[,\\.，。?:\"'“”<>\\(\\)-、]");
		Matcher m=p.matcher(str);
		int start=0;
		int end=0;
		//int flag=0;
		while(m.find())
		{
			
			//flag++;
			 end=m.end();
			 System.out.println("查询到:"+str.substring(start,end));
			 LinkedList<String> strr1=new  LinkedList<String>();
			 strr1=pipei_forward(tre,str.substring(start,end));
			 strr.add(strr1);
			 start=end;
		}
		if(!str.isEmpty())
		{
			System.out.println("查询："+start+"\t"+str.length());
			System.out.println("查询到222:"+str.substring(start-1));
			 LinkedList<String> strr1=new  LinkedList<String>();
			 System.out.println("查询222："+start+"\t"+str.length());
			 if(str.substring(start-1)!=""&&!str.substring(start-1).isEmpty())
			 {
				 strr1=pipei_forward(tre,str.substring(start-1));
				 strr.add(strr1);
			 }
		}
		return strr;
	}
	public LinkedList<String> pipei_forward(tree_fenci tre,String str)
	{
		LinkedList<String> strr=new LinkedList<String>();
		int j=0;
		int len=tre.size_length()<str.length()?tre.size_length():str.length();
		int i=0;
		while(true)
		{
			j+=i;
			if(j==str.length())
			{
				System.out.println("句结束");
				break;
			}
			i=0;
			//重新计算还剩下的len长度
			int len1=len;
			if(j+len>str.length())
			{
				int ii=1;
				while(j+len-ii>str.length())
				{
					ii++;
				}
				len1=len-ii;
			}
			String temp;
//			if(len1<0)
//				len1=0;
			System.out.println("j,j+len1:"+j+"\t"+(j+len1));
			System.out.println(str+"\t"+str.length());
			
			temp=str.substring(j,j+len1);
			String temp1="";
			System.out.println("temp:"+temp);
			
			if(temp.length()==1)
			{
				i++;
				strr.add(temp);
			}
			else
			{
				String tf=tre.search(temp);		
				if(tf.isEmpty())
				{//如果没有查找到那么减少一个词直到为0则返回整个语句
					System.out.println("空");
					while(true)
					{
						i++;
						if(len1-i==0)
						{
							i=1;
							strr.add(temp.substring(0,1));
							System.out.println("错误只加入一个字");
							break;
						}
						temp1=temp.substring(0,len1-i);
						String tff=tre.search(temp1);
						System.out.println("temp1:"+temp1+":"+(len1-i));
						if(tff.isEmpty())
						{
							System.out.println("tff为空");
							continue;
						}
						else
						{
							strr.add(temp1);
							System.out.println("tff匹配成功:"+i);
							System.out.println(len1+"\t"+i);
							i=len1-i;
							break;
						}
						
					}
				}
				else
				{
					i=len1;
					strr.add(temp);
				}
			}
		}
		return strr;
	}
	//通过索引执行正向匹配方法
	//调用方法analyze_f(struct,str)
	public  LinkedList<LinkedList<String>> analyze_f(tree_fenci tre,String str)
	{
		//首先将特殊的间断语句给拆分出来
		LinkedList<LinkedList<String>> strr=new  LinkedList<LinkedList<String>>();
		Pattern p=Pattern.compile("[,\\.，。?:\"'“”<>\\(\\)-、]");
		Matcher m=p.matcher(str);
		int start=0;
		int end=0;
		//int flag=0;
		while(m.find())
		{
			
			//flag++;
			 end=m.end();
			 System.out.println("查询到:"+str.substring(start,end));
			 LinkedList<String> strr1=new  LinkedList<String>();
			 strr1=pipei_forward_f(tre,str.substring(start,end));
			 strr.add(strr1);
			 start=end;
		}
		if(!str.isEmpty())
		{
			System.out.println("查询："+start+"\t"+str.length());
			System.out.println("查询到222:"+str.substring(start-1));
			 LinkedList<String> strr1=new  LinkedList<String>();
			 System.out.println("查询222："+start+"\t"+str.length());
			 if(str.substring(start-1)!=""&&!str.substring(start-1).isEmpty())
			 {
				 strr1=pipei_forward_f(tre,str.substring(start-1));
				 strr.add(strr1);
			 }
		}
		return strr;
	}
	public LinkedList<String> pipei_forward_f(tree_fenci tre,String str)//已于索引结构的快速方法
	{
		LinkedList<String> strr=new LinkedList<String>();
		int j=1;
		//int len=tre.size_length()<str.length()?tre.size_length():str.length();
		int i=0;
		while(true)
		{
			if(i==str.length())
			{//如果搜索到最后则跳出
				break;
			}
			System.out.println("str:"+str.length()+":"+i);
			Vector<Vector<String>>  ll=tre.node3_search_words(str.substring(i,i+1));//获取第一个词是str的
			if(ll.isEmpty())
			{
				System.out.println("ll为空："+str.substring(i,i+1));
				strr.add(str.substring(i,i+1));
				i++;			
			}
			else
			{
				//从ll中匹配出str中最常的一个
				LinkedList<String> lm=new LinkedList<String>();
				//提取最长字符并匹配如果成功则continue，如果不存在则只添加第一个字符
				int max_len=0;
				int max_index=-1;
				String max_str="";
				int len_ll=ll.size();
				for(int iii=0;iii<len_ll;iii++)
				{
					max_len=0;
					max_index=-1;
					max_str="";
					for(int ii=0;ii<ll.size();ii++)
					{
						int max_len_tem=ll.get(ii).get(0).length();
						if(max_len<max_len_tem)
						{
						max_str=ll.get(ii).get(0);
						max_len=max_len_tem;
						max_index=ii;
						}
					}
					if(str.substring(i,i+max_len>=str.length()?str.length():i+max_len).compareTo(max_str)==0)
					{//如果匹配相同
						System.out.println("匹配成功:"+str.substring(i,i+max_len));
						strr.add(str.substring(i,i+max_len));
						i+=max_len;
						
						break;
					}
					else
					{
						//清除掉max_index地方的数据
						System.out.println("max:"+max_index);
						System.out.println(ll.size());
						ll.remove(max_index); 
						continue;
					}	
				}
				if(ll.isEmpty())
				{//表示vector已经清空
					System.out.println("Vector全部清空+:"+str.substring(i,i+1));
					strr.add(str.substring(i,i+1));
					i++;
				}
			}
		}
		return strr;
	}
	//负向最大匹配方法
	//调用方法analyze_negative(struct ,str)
	public  LinkedList<LinkedList<String>> analyze_negative(tree_fenci tre,String str)
	{
		//首先将特殊的间断语句给拆分出来
		LinkedList<LinkedList<String>> strr=new  LinkedList<LinkedList<String>>();
		Pattern p=Pattern.compile("[,\\.，。?:\"'“”<>\\(\\)-、]");
		Matcher m=p.matcher(str);
		int start=0;
		int end=0;
		//int flag=0;
		while(m.find())
		{
			
			//flag++;
			 end=m.end();
			 System.out.println("查询到:"+str.substring(start,end));
			 LinkedList<String> strr1=new  LinkedList<String>();
			 strr1=pipei_negative(tre,str.substring(start,end));
			 strr.add(strr1);
			 start=end;
		}
		if(!str.isEmpty())
		{
			System.out.println("查询："+start+"\t"+str.length());
			System.out.println("查询到222:"+str.substring(start-1));
			 LinkedList<String> strr1=new  LinkedList<String>();
			 System.out.println("查询222："+start+"\t"+str.length());
			 if(str.substring(start-1)!=""&&!str.substring(start-1).isEmpty())
			 {
				 strr1=pipei_negative(tre,str.substring(start-1));
				 strr.add(strr1);
			 }
		}
		return strr;
	}
	public LinkedList<String> pipei_negative(tree_fenci tre,String str)
	{
		LinkedList<String> strr=new LinkedList<String>();
		int j=str.length();
		int len=tre.size_length()<str.length()?tre.size_length():str.length();
		int i=0;
		while(true)
		{
			j-=i;
			if(j==0)
			{
				System.out.println("句结束");
				break;
			}
			i=0;
			//重新计算还剩下的len长度
			int len1=len;
			if(j-len<0)
			{
				int ii=1;
				while(j-len+ii<0)
				{
					ii++;
				}
				len1=len-ii;
			}
			String temp;
//			if(len1<0)
//				len1=0;
			System.out.println("j,j+len1:"+j+"\t"+(j+len1));
			System.out.println(str+"\t"+str.length());
			
			temp=str.substring(j-len1,j);
			String temp1="";
			System.out.println("temp:"+temp);
			
			if(temp.length()==1)
			{
				i++;
				strr.add(temp);
			}
			else
			{
				String tf=tre.search(temp);		
				if(tf.isEmpty())
				{//如果没有查找到那么减少一个词直到为0则返回整个语句
					System.out.println("空");
					while(true)
					{
						i++;
						if(i==len1-1)
						{
							i=1;
							strr.add(temp.substring(len1-1));
							System.out.println("错误只加入一个字"+temp.substring(len1-1));
							break;
						}
						temp1=temp.substring(i);
						String tff=tre.search(temp1);
						System.out.println("temp1:"+temp1+":"+(i));
						if(tff.isEmpty())
						{
							System.out.println("tff为空");
							continue;
						}
						else
						{
							strr.add(temp1);
							System.out.println("tff匹配成功:"+i);
							System.out.println(len1+"\t"+i);
							i=len1-i;
							break;
						}
						
					}
				}
				else
				{
					i=len1;
					strr.add(temp);
				}
			}
		}
		return strr;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//通过索引查找负向最大匹配//需要重新构建tree树才行//目前不是有效方法
	//调用方法analyze_negative_f(Struct,str)
	/////////////////////////////////////////////////////////////////需要调整tree结构才有效
	public  LinkedList<LinkedList<String>> analyze_negative_f(tree_fenci tre,String str)
	{
		//首先将特殊的间断语句给拆分出来
		LinkedList<LinkedList<String>> strr=new  LinkedList<LinkedList<String>>();
		Pattern p=Pattern.compile("[,\\.，。?:\"'“”<>\\(\\)-、]");
		Matcher m=p.matcher(str);
		int start=0;
		int end=0;
		//int flag=0;
		while(m.find())
		{
			
			//flag++;
			 end=m.end();
			 System.out.println("查询到:"+str.substring(start,end));
			 LinkedList<String> strr1=new  LinkedList<String>();
			 strr1=pipei_negative_f(tre,str.substring(start,end));
			 strr.add(strr1);
			 start=end;
		}
		if(!str.isEmpty())
		{
			System.out.println("查询："+start+"\t"+str.length());
			System.out.println("查询到222:"+str.substring(start-1));
			 LinkedList<String> strr1=new  LinkedList<String>();
			 System.out.println("查询222："+start+"\t"+str.length());
			 if(str.substring(start-1)!=""&&!str.substring(start-1).isEmpty())
			 {
				 strr1=pipei_negative_f(tre,str.substring(start-1));
				 strr.add(strr1);
			 }
		}
		return strr;
	}
	public LinkedList<String> pipei_negative_f(tree_fenci tre,String str)//已于索引结构的快速方法
	{
		LinkedList<String> strr=new LinkedList<String>();
		int j=1;
		//int len=tre.size_length()<str.length()?tre.size_length():str.length();
		int i=str.length();
		while(true)
		{
			if(i==0)
			{//如果搜索到最后则跳出
				break;
			}
			System.out.println("str:"+str.length()+":");
			Vector<Vector<String>>  ll=tre.node3_search_words(str.substring(i-1,i));//获取第一个词是str的
			if(ll.isEmpty())
			{
				System.out.println("ll为空："+str.substring(i,i+1));
				strr.add(str.substring(i-1,i));
				i--;			
			}
			else
			{
				//从ll中匹配出str中最常的一个
				LinkedList<String> lm=new LinkedList<String>();
				//提取最长字符并匹配如果成功则continue，如果不存在则只添加第一个字符
				int max_len=0;
				int max_index=-1;
				String max_str="";
				int len_ll=ll.size();
				for(int iii=0;iii<len_ll;iii++)
				{
					max_len=0;
					max_index=-1;
					max_str="";
					for(int ii=0;ii<ll.size();ii++)
					{
						int max_len_tem=ll.get(ii).get(0).length();
						if(max_len<max_len_tem)
						{
						max_str=ll.get(ii).get(0);
						max_len=max_len_tem;
						max_index=ii;
						}
					}
					System.out.println(str.length()+":"+i+":"+":"+(i-max_len));
					if(str.substring(i-max_len<=0?0:i-max_len,i).compareTo(max_str)==0)
					{//如果匹配相同
						System.out.println("匹配成功:"+str.substring(i-max_len,i));
						strr.add(str.substring(i-max_len,i));
						i-=max_len;
						
						break;
					}
					else
					{
						//清除掉max_index地方的数据
						System.out.println("max:"+max_index);
						System.out.println(ll.size());
						ll.remove(max_index); 
						continue;
					}	
				}
				if(ll.isEmpty())
				{//表示vector已经清空
					System.out.println("Vector全部清空+:"+str.substring(i-1,i));
					strr.add(str.substring(i-1,i));
					i--;
				}
			}
		}
		return strr;
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	
}
