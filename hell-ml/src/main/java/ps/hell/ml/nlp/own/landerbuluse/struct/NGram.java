package ps.hell.ml.nlp.own.landerbuluse.struct;

import java.util.Arrays;


public class NGram {
	
	public static String[] getNGram(String[] str,int n){
		int size=str.length-n+1;
		
		if(n<=0||size<=0){
			return null;
		}
		if(n==1){
			return str.clone();
		}
		
		String[] result=new String[size];
		StringBuffer sb=null;
		for(int i=0;i<size;i++){
			sb=new StringBuffer(str[i]);
			for(int j=1;j<n;j++){
				sb.append(" ").append(str[i+j]);
			}
			result[i]=sb.toString();
		}
		return result;
	}
	public static void main(String[] args) {
		String  s = "HOWEVER the egg only got larger and larger and more and more human";
		WordsGet get=new WordsGet();
		String[] str=get.split(s);
		System.out.println(Arrays.toString(NGram.getNGram(str, 1)));
		System.out.println(Arrays.toString(NGram.getNGram(str, 2)));
		System.out.println(Arrays.toString(NGram.getNGram(str, 3)));
	}
}
