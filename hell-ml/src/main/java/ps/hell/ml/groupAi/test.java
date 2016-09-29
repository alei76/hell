package ps.hell.ml.groupAi;

import java.math.BigInteger;

public class test {
	
	public static int getInt(int count)
	{
		return (1<<count)-1;
	}
public static void main(String[] args) {
	String a = "-1195";//输入数值
	BigInteger src = new BigInteger(a);//转换为BigInteger类型
	System.out.println(src.toString(2));//转换为2进制并输出结果
	
	//2进制->10进制
	String a1 = "10010101011";//输入数值
	BigInteger src1 = new BigInteger(a1, 2);//转换为BigInteger类型
	System.out.println(src1.toString());//转换为2进制并输出结果
	
	
	System.out.println("-----------------");
	String first = "1195";//输入数值
	BigInteger firstS = new BigInteger(first);//转换为BigInteger类型
	int firstI=Integer.parseInt(first);
	String second = "1742";//输入数值
	BigInteger secondS = new BigInteger(second);//转换为BigInteger类型
	int secondI=Integer.parseInt(second);
	System.out.println(firstS.toString(2));
	System.out.println(secondS.toString(2));
	BigInteger thridS=new BigInteger(Long.toString(firstI&secondI));
	System.out.println(thridS.toString(2));
	System.out.println("替换前4位");
	int count=11;
	int changeCount=7;
	int changeCount2=7;
	BigInteger dS=new BigInteger(Long.toString(firstI>>7<<7));
	System.out.println(dS.toString(2));
	System.out.println(1+2+4+8+16+32+64);
	BigInteger eS=new BigInteger(Long.toString(secondI&127));
	System.out.println(eS.toString(2));
	System.out.println("只满足一个断点条件");
	BigInteger fS=new BigInteger(Long.toString((firstI>>(count-changeCount)<<(count-changeCount))|(secondI&test.getInt(count-changeCount))));
	System.out.println(fS.toString(2));
	
	System.out.println("满足两个断点情况");
	System.out.println(Integer.toBinaryString(firstI&(~(test.getInt(changeCount2-changeCount+1)<<(changeCount-1)))|(secondI&test.getInt(changeCount2-changeCount+1)<<(changeCount-1))));
	
	System.out.println("取反");
	int rev=4;
	System.out.println(Integer.toBinaryString(firstI^((1<<rev))));
	
	
	System.out.println(Integer.toBinaryString(firstI&(~(test.getInt(changeCount2-changeCount+1)<<changeCount))));
	System.out.println(Integer.toBinaryString(secondI&test.getInt(changeCount2-changeCount+1)<<changeCount));

	System.out.println(test.getInt(3));
	
	
	
	System.out.println("--------------");
	System.out.println(Long.toBinaryString(5000L));
	}
}
