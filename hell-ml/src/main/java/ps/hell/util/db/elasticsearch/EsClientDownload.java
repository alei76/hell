package ps.hell.util.db.elasticsearch;
//
//import ps.hell.util.FileUtil;
//
//import java.util.ArrayList;
//
//public class EsClientDownload {
//
//	public static void main(String[] args) {
//
//		String clusterName = "bigdata-es";
//		clusterName = args[0];
//		String[] esIps = null;// args[2].split(":");
//		esIps = new String[] { "10.10.202.18" };
//		esIps = args[1].split(":");
//		int port = 9300;// Integer.parseInt(args[3]);
//		port = Integer.parseInt(args[2]);
//		String index = "crm_index";// args[4];
//		index = args[3];
//		String type = "crm";// args[5];
//		type = args[4];
//		int searchBluk = 1000;
//		searchBluk = Integer.parseInt(args[5]);
//		String fileOutput = null;
//		fileOutput = args[6];
//
//		EsClientTool tool = new EsClientTool(clusterName, esIps, port, index,
//				type);
//		tool.searchBluk(searchBluk);
//		ArrayList<String> list = null;
//		int count = 0;
//		FileUtil file = new FileUtil(fileOutput, "utf-8", true, "delete");
//		while ((list = tool.getNextBulk()) != null) {
//			count++;
//			System.out.println("count:" + count+"\t"+(searchBluk*count));
//			// System.out.println(list);
//			file.write(list);
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}
//}
