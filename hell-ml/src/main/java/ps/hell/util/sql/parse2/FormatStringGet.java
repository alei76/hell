package ps.hell.util.sql.parse2;

public class FormatStringGet {

	
	public static String getT(int val){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<val-1;i++){
			sb.append("\t");
		}
		return sb.toString();
	}
}
