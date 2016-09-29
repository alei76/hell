package ps.hell.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ShellExec {
	
/**
 * shell执行方法
 * @param shellStr
 */
	public static boolean execShell(String shellStr)
	{
		try {
			Runtime rt = Runtime.getRuntime();
			List<String> cmds=new ArrayList<String>();
    		cmds.add("sh");
    		cmds.add("-c");
    		cmds.add(shellStr);
    		String[] cmdss=new String[cmds.size()];
    		cmdss=cmds.toArray(cmdss);
			Process process= rt.exec(cmdss);
			int exitValue = process.waitFor();  
	        if (0 != exitValue) {  
	            System.out.println("call shell failed. error code is :" + exitValue); 
	          //  System.exit(0);
	            return false;
	        }  
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 获取结果
	 * @param shellStr
	 * @return
	 */
	public static ArrayList<String> execShellAndGet(String shellStr)
	{
	        ArrayList<String> processList = new ArrayList<String>();  
	        try {  
	        		List<String> cmds=new ArrayList<String>();
	        		cmds.add("sh");
	        		cmds.add("-c");
	        		cmds.add(shellStr);
	        		String[] cmdss=new String[cmds.size()];
	        		cmdss=cmds.toArray(cmdss);
	            Process prc= Runtime.getRuntime().exec(cmdss);  
	            BufferedReader input = new BufferedReader(new InputStreamReader(prc.getInputStream()));  
	            String line = "";  
	            while ((line = input.readLine()) != null) {  
	                processList.add(line);  
	            }  
	            input.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } 
	       return processList;
	}
	/**
	 * 执行并打印
	 * @param shellStr
	 */
	public static void execShellAndPrint(String shellStr)
	{
		  Process process = null;  
	        List<String> processList = new ArrayList<String>();  
	        try {  
	       		List<String> cmds=new ArrayList<String>();
        		cmds.add("sh");
        		cmds.add("-c");
        		cmds.add(shellStr);
        		String[] cmdss=new String[cmds.size()];
        		cmdss=cmds.toArray(cmdss);
	            process = Runtime.getRuntime().exec(cmdss);  
	            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));  
	            String line = "";  
	            while ((line = input.readLine()) != null) {  
	                processList.add(line);  
	            }  
	            input.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	  
	        for (String line : processList) {  
	            System.out.println(line);  
	        }  
	}
	
//	/**
//	 * 不打印
//	 * 通过修改中的特殊形势转化为对应的时间等格式
//	 * @param shell执行
//	 * 主要用在爬虫最后的文件数据
//	 */
//	public static void execShell(String shell, String time,Logger log) {
//		// shell 处理方法
//		String shellStr = translate(shell, time);
//		log.info("执行的shell语句为:"+shellStr);
//		try {
//			Runtime rt = Runtime.getRuntime();
//			 rt.exec(shellStr);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public static String translate(String shell, String time) {
//		String result = shell;
//		while (true) {
//			Pattern p_2 = Pattern.compile("\\{([^}]*?)\\}");
//			Matcher m_2 = p_2.matcher(result);
//
//			int i = 0;
//			if (m_2.find()) {
//				i++;
//				String[] str = m_2.group(1).split(":");
//				//System.out.println(m_2.group(1) + "\t" + m_2.start() + "\t"+ m_2.end());
//				if (str[0].equals("static")) {
//					// 从静态文件种获取信息
//					result = result.substring(0, m_2.start())
//							+ FileConfig.getmap(str[1]).substring(0,
//									FileConfig.getmap(str[1]).indexOf("."))
//							+ result.substring(m_2.end());
//				} else if (str[0].equals("date")) {
//					if (str[1].equals("time")) {
//						result = result.substring(0, m_2.start()) + time
//								+ result.substring(m_2.end());
//					}
//				}
//			//	System.out.println(result);
//			} else {
//				break;
//			}
//		}
//		return result;
//	}

	public static void main(String[] args) {
		String str = "java -jar -Xms3096m -Xmx3096m ZJCrawler_fat.jar intoMongoCompanyUpdate ./data/{static:companyDescFile}-{date:time}.txt ./data/{static:companyDescJobDescFile}-{date:time}.txt ./data/{static:companyDescJobFile}-{date:time}.txt 192.168.1.4 27017 demo company51job 1000";
		//ShellExec.execShell(str, "201-05-11",null);
	}
}
