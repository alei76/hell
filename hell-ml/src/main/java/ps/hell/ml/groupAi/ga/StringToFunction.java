package ps.hell.ml.groupAi.ga;


/**
 * 表达式转为 计算
 * @author Administrator
 *
 */
public class StringToFunction {

	
	public static double evaluateDecode(String fx)
	{
		//首先解析出x1，x2对应的元素
		
		char[] cha=fx.toCharArray();
		String first="";
		String notC="";
		String C="";
		int Cint=0;//标记括号数量
		int CFlag=0;//标记无括号 在前为1，-1为括号在前
		boolean CFlag2=false;//标记是否出现过括号
		boolean sinFlag=false;//标记为sinFlag的开关
		int sinInt=0;//对应数量
		for(int i=0;i<cha.length;i++)
		{
			if(i==0 && cha[i]=='-')
			{
				first="-";
				continue;
			}
			if(cha[i]!='-'&& cha[i]!='+'&& cha[i]!='*'&& cha[i]!='/' && 
					cha[i]!='(' && cha[i]!=')'&&
					!Character.isDigit(cha[i]))
			{
				//如果 都不是 则为 sin,cos ,tan ,cot,log,exp的标记符
				//sinFlag=true;
				
				if(sinFlag==false)
				{
					sinInt++;
					
				}
				if(CFlag2==true)
				{
					C+=cha[i];
				}
				else
				{
					notC+=cha[i];
				}
				continue;
				
			}
			//需要解析括号优先于乘除
			if(cha[i]=='(')
			{
				sinFlag=false;
				if(CFlag==0)
				{
					CFlag=-1;

				}
				CFlag2=true;
				Cint++;
				C+=cha[i];
				//System.out.println("C:"+C);
				continue;
			}
			if(Cint>0 &&cha[i]!=')')
			{
				C+=cha[i];
			}
			//System.out.println("C变化:"+C+"\tnotC:"+notC);
				if(cha[i]==')')
				{
					//提取内容
					//System.out.println("计算括号内的值:"+C.substring(C.lastIndexOf('(')+1));
					double ll=computeC(C.substring(C.lastIndexOf('(')+1));
					//System.out.println("计算值为:"+ll+"\t"+C.lastIndexOf('('));
					//需要判断外部是否加载了sin函数
					if(C.lastIndexOf('(')-3>=0)
					{
						String temp=C.substring(C.lastIndexOf('(')-3,C.lastIndexOf('('));
						//判断temp
						if(temp.toLowerCase().equals("sin")||temp.toLowerCase().equals("cos")||
								temp.toLowerCase().equals("tan")||
								temp.toLowerCase().equals("exp")||temp.toLowerCase().equals("log"))
						{
							ll=computeString(ll,temp);
							C=C.substring(0,C.lastIndexOf('(')-3)+Double.toString(ll);
						}
						else if(temp.toLowerCase().equals("qrt"))
						{
							temp=C.substring(C.lastIndexOf('(')-3,C.lastIndexOf('('));
							if(temp.toLowerCase().equals("sqrt"))
							{
								ll=computeString(ll,temp);
								C=C.substring(0,C.lastIndexOf('(')-4)+Double.toString(ll);
							}
						}
						else
						{
							C=C.substring(0,C.lastIndexOf('('))+Double.toString(ll);
						}
					
					}
					else
					{
						//System.out.println("不存在sin");
						//需要判断是否是最开始了，如果是最开始则需要判断notC+C中是否存在sin
						if(notC.length()>=3)
						{
							String temp=notC.substring(notC.length()-3);
							if(temp.equals("sin")|| temp.equals("cos")||
									temp.equals("tan")|| 
									temp.equals("exp")|| temp.equals("log"))
							{
								//计算对应值
								//System.out.println("计算String:"+temp+"\t输入值:"+ll);
								double ll2=ll=computeString(ll,temp);
								//System.out.println("结果:"+"\t值:"+ll2);
								//需要判断再前面的值
								if(ll2<0 && notC.length()-3>2)
								{
									if(notC.charAt(notC.length()-4)=='-')
									{
										if(notC.charAt(notC.length()-5)=='-')
										{
											notC=notC.substring(0,notC.length()-5)+Double.toString(ll2);
											C="";
											CFlag2=false;
										}
										else if(notC.charAt(notC.length()-5)=='+')
										{
											notC=notC.substring(0,notC.length()-5)+Double.toString(-1.0*ll2);
											C="";
											CFlag2=false;
										}
										else if(notC.charAt(notC.length()-5)=='*')
										{
											notC=notC.substring(0,notC.length()-4)+Double.toString(-1.0*ll2);
											C="";
											CFlag2=false;
										}
										else if(notC.charAt(notC.length()-5)=='/')
										{
											notC=notC.substring(0,notC.length()-4)+Double.toString(-1.0*ll2);
											C="";
											CFlag2=false;
										}
									}
									else
									{
										notC=notC.substring(0,notC.length()-3)+Double.toString(ll2);
										C="";
										CFlag2=false;
									}
								}
								else if(ll2<0 && notC.length()-3==1)
								{
									ll2=-1.0*ll2;
									notC=Double.toString(ll2);
									C="";
									CFlag2=false;
								}
								else
								{
									notC=notC.substring(0,notC.length()-3)+Double.toString(ll2);
									C="";
									CFlag2=false;
								}
							}
							else if(temp.equals("qrt"))
							{
								//System.out.println("计算String:"+"s"+temp+"\t输入值:"+ll);
								double ll2=ll=computeString(ll,"s"+temp);
								//System.out.println("返回值:"+ll2);
								notC=notC.substring(0,notC.length()-4)+Double.toString(ll2);
								C="";
								CFlag2=false;
							}
							else
							{
								C=C.substring(0,C.lastIndexOf('('))+Double.toString(ll);
							}
						}
						else
						{
						C=C.substring(0,C.lastIndexOf('('))+Double.toString(ll);
						}
					}
					Cint--;
					//System.out.println("修改到C:"+C+"\t"+Cint+"\t"+CFlag);
					//计算完成之后都需要比较自身值 是否为负数如果 为负数则需要和前面的内容匹配
					if(ll<0)
					{
						if(C.length()-Double.toString(ll).length()-1>0)
						{
							C.substring(C.length()-Double.toString(ll).length()-1,C.length()-Double.toString(ll).length());
						}
					}
					if(Cint==0)
					{
						if(CFlag==-1)
						{
							//System.out.println(C+"\t"+first+"\t"+notC);
							if(C.equals(""))
							{
								//computeC(first+notC);
								if(first.equals("-") && notC.charAt(0)=='-')
								{
									notC=notC.substring(1);
								}
								continue;
							}
							
							if(Double.parseDouble(C)<0 && first.equals("-"))
							{
								notC=C.substring(1);
							}
							//notC=first+C;
							C="";
							
							//右括号在前则替换
							//System.out.println("修改到notC:"+notC);
							CFlag=1;
						}
						else
						{
							notC+=C;
							C="";
						}
					}
					continue;
				}

				if(CFlag==0)
				{
					//System.out.println("修改CFlag");
					CFlag=1;
				}
				if(Cint==0)
				{
					notC+=cha[i];
				}
			//System.out.println("notC:"+notC);
			//需要解析乘除优先于加减
		}
		if(CFlag2==true)
		{
			//计算
			if(CFlag==-1)
			{
				C=C+notC;
				//System.out.println("C+notC:"+C);
				return computeC(C);
			}
			else
			{
				C=notC+C;
				//System.out.println("notC+C:"+C);
				return computeC(C);
			}
		}
		else
		{
			//System.out.println("返回notC:"+notC);
			//return Double.parseDouble(notC);
			return computeC(notC);
		}
		//1.5+1.1*-0.8431995903657401+-0.4161468365471424+1.4142135623730951
	}
	/**
	 * 解析括号内内容其中括号内不能再有括号
	 * @return 其中string中可以存在这种方式 *-值 或者 --值但是不能存在 三次符号，其中只对第二个是-号有效
	 * 并也不支持首个是两次-号的情况
	 */
	public static double computeC(String str)
	{
		char[] cha=str.toCharArray();
		String init="";
		String x1="";
		String x2="";
		String x3="";
		boolean x1F=false;
		boolean x2F=false;
		boolean x3F=false;
		
		int fl=0;//标记出现连续方法次数以修改减到数值中
		char cha1=' ';
		char cha2=' ';
		int i=1;
		boolean flag=false;//判断是否持续数值
		for(int j=0;j<cha.length;j++)
		{
			if(j==0 && cha[j]=='-')
			{
				init="-";
			}
			if(Character.isDigit(cha[j]) || cha[j]=='.')
			{
				fl=0;
				if(i==1 && flag==false)
				{
					x1=init+Character.toString(cha[j]);
					//System.out.println("x1:"+x1);
					x1F=true;
					flag=true;
					i++;
				}
				else if(i==2 && flag==true)
				{
					x1=x1+Character.toString(cha[j]);
					//System.out.println("x1:"+x1);
					
				}
				if(i==2 && flag==false)
				{
					x2=Character.toString(cha[j]);
					//System.out.println("x2:"+x2);
					x2F=true;
					flag=true;
					i++;
				}
				else if(i==3 && flag==true)
				{
					x2=x2+Character.toString(cha[j]);
					//System.out.println("x2:"+x2);
					
				}
				if(i==3 && flag==false)
				{
					x3=Character.toString(cha[j]);
					//System.out.println("x3:"+x3);
					x3F=true;
					flag=true;
					i++;
				}
				else if(i==4 && flag==true)
				{
					x3=x3+Character.toString(cha[j]);
					//System.out.println("x3:"+x3);
				}
				if(j==cha.length-1)
				{
					//表示已经到最后
					if(x2F==false)
					{
						//System.out.println("值返回部计算:"+x1);
						return Double.parseDouble(x1);
					}
					if(x3F==false)
					{
						//System.out.println("只计算两次:"+x1+""+cha1+""+x2);
						return compute(Double.parseDouble(x1),Double.parseDouble(x2),cha1);
						////System.out.println("修改"+x1);
					}
					else
					{
						if(cha1=='*'||cha1=='/')
						{
							//System.out.println(x1+""+cha1+""+x2+""+cha2+""+x3);
							x1=Double.toString(compute(Double.parseDouble(x1),Double.parseDouble(x2),cha1));
							cha1=cha2;
							x2=x3;
							cha2=cha[j];
							x3F=false;
							//System.out.println("修改"+x1+""+cha1+""+x2);
							i--;
						}
						else if(cha2=='*'||cha2=='/')
						{
							//System.out.println(x1+""+cha1+""+x2+""+cha2+""+x3);
							x2=Double.toString(compute(Double.parseDouble(x2),Double.parseDouble(x3),cha2));
							cha2=cha[j];
							//System.out.println("修改"+x1+""+cha1+""+x2);
							x3F=false;
							i--;
						}
						else
						{
							//System.out.println(x1+""+cha1+""+x2+""+cha2+""+x3);
							x1=Double.toString(compute(Double.parseDouble(x1),Double.parseDouble(x2),cha1));
							cha1=cha2;
							cha2=cha[j];
							x2=x3;
							x3F=false;
							//System.out.println("修改"+x1+""+cha1+""+x2);
							i--;
						}
						return compute(Double.parseDouble(x1),Double.parseDouble(x2),cha1);
					}
				}
			}
			else
			{
				//System.out.println("变化:"+cha[i]);
				flag=false;
				fl++;
				if(fl==1)
				{
					if(i==3)
					{
						cha2=cha[j];
					}
					else if(i==2)
					{
						cha1=cha[j];
					}
					if(i==4)
					{
						//判断哪部分合并
						if(cha1=='*'||cha1=='/')
						{
							//System.out.println(x1+":"+cha1+":"+x2+""+cha2+":"+x3);
							x1=Double.toString(compute(Double.parseDouble(x1),Double.parseDouble(x2),cha1));
							cha1=cha2;
							x2=x3;
							cha2=cha[j];
							x3F=false;
							//System.out.println("修改:"+x1+":"+cha1+":"+x2);
							i--;
						}
						else if(cha2=='*'||cha2=='/')
						{
							//System.out.println(x1+":"+cha1+":"+x2+":"+cha2+":"+x3);
							x2=Double.toString(compute(Double.parseDouble(x2),Double.parseDouble(x3),cha2));
							cha2=cha[j];
							//System.out.println("修改:"+x1+":"+cha1+":"+x2);
							x3F=false;
							i--;
						}
						else
						{
							//System.out.println(x1+":"+cha1+":"+x2+":"+cha2+":"+x3);
							//System.out.println(compute(Double.parseDouble(x1),Double.parseDouble(x2),cha1));
							x1=Double.toString(compute(Double.parseDouble(x1),Double.parseDouble(x2),cha1));
							cha1=cha2;
							cha2=cha[j];
							x2=x3;
							x3F=false;
							//System.out.println("修改:"+x1+":"+cha1+":"+x2);
							i--;
						}
					}
					
				}
				else if(fl==2)
				{
					//需要将-号放入值中
					if(i==3)
					{
						x3=Character.toString(cha[j]);
						//System.out.println("x3添加负号:"+x3);
						x3F=true;
						flag=true;
						i++;
					}
					else if(i==2)
					{
						x2=Character.toString(cha[j]);
						//System.out.println("x2添加负号:"+x2);
						x2F=true;
						flag=true;
						i++;
					}

				}
				else
				{
					//System.out.println("负号添加出现错误");
					System.exit(1);
				}
			}
		}
		return 0.0;
	}
	
	public static double compute(double x1,double x2,char str)
	{
		switch (str) {
		case '-':
			return x1-x2;
		case '+':
			return x1+x2;
		case '*':
			return x1*x2;
		case '/':
			return x1/x2;
			
		default:
			return 0.0;
		}
	}
	/**
	 * 计算sin(x)值
	 * cos
	 * tan
	 * cot其他的不计算
	 * exp
	 * log
	 * @param x1
	 * @param str
	 * @return
	 */
	public static double computeString(double x1,String str)
	{
		if(str.toLowerCase().equals("sin"))
		{
			return Math.sin(x1);
		}
		if(str.toLowerCase().equals("cos"))
		{
			return Math.cos(x1);
		}
		if(str.toLowerCase().equals("tan"))
		{
			return Math.tan(x1);
		}
		if(str.toLowerCase().equals("sqrt"))
		{
			return Math.sqrt(x1);
		}
		if(str.toLowerCase().equals("exp"))
		{
			return Math.exp(x1);
		}
		if(str.toLowerCase().equals("log"))
		{
			return Math.log(x1);
		}
		//System.out.println("输入错误:"+x1+"\t"+str);
		System.exit(1);
		return 0.0;
		
	}
	public static void main(String[] args) {
		//test1.evaluateDecode("2+3*4/(1-4)*3");
		
	//	//System.out.println("值为:"+FunctionToDouble.computeC("3-4"));
		
		////System.out.println("值为:"+FunctionToDouble.computeC("5-3"));
		////System.out.println("解析值:"+test1.compute(3,5,'+'));
		//System.out.println("------------------");
		////System.out.println("-(3-4)/2+1");
		////System.out.println("计算值:"+FunctionToDouble.evaluateDecode("-sin(3-4)/cos(2)+1"));
	
		
		System.out.println(StringToFunction.evaluateDecode("-sin(-2*-log(4)+2)*cos(1.3)"));
		System.out.println(StringToFunction.evaluateDecode("2+3*10*(2+1*(3))"));
		System.out.println(StringToFunction.evaluateDecode("sin(3)"));
	}
}
