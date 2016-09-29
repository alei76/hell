package ps.hell.util.db.fastdfs;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;
import java.io.IOException;
  
import org.csource.common.MyException;
import org.csource.common.NameValuePair;  
import org.csource.fastdfs.ClientGlobal;  
import org.csource.fastdfs.ServerInfo;  
import org.csource.fastdfs.StorageClient;  
import org.csource.fastdfs.StorageServer;  
import org.csource.fastdfs.TrackerClient;  
import org.csource.fastdfs.TrackerServer;  
  
/** 
 * 测试上传 
 * @author gary 
 * 
 */  
public class TestUpload {  

	/**
	 * 
	 * @param imagePath 图片地址
	 * @param cate 文件类型
	 * @return fastdfs [0] group [1] address
	 * @throws IOException 
	 */
	public static String[] run(String imagePath,String cate)
	{
		 TrackerClient trackerClient = new TrackerClient();  
	        TrackerServer trackerServer=null;
			try {
				trackerServer = trackerClient.getConnection();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
	  
	        StorageServer storageServer = null;  
	        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
	        //不需要指定 即可不产生-m文件
	        NameValuePair[] meta_list=null;
//	        NameValuePair[] meta_list = new NameValuePair[3];  
//	        meta_list[0] = new NameValuePair("width", "120");  
//	        meta_list[1] = new NameValuePair("heigth", "120");  
//	        meta_list[2] = new NameValuePair("author", "uni2uni");  
	        File file = new File(imagePath);  
	        FileInputStream fis=null;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        byte[] file_buff = null;  
	        if(fis != null){  
	            int len=0;
				try {
					len = fis.available();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
	            file_buff = new byte[len];  
	            try {
					fis.read(file_buff);
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
	        }  
	        
	        String group_name = null;  
	        StorageServer[] storageServers=null;
			try {
				storageServers = trackerClient.getStoreStorages(trackerServer, group_name);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
	        if (storageServers == null) {  
	            System.err.println("get store storage servers fail, error code: " + storageClient.getErrorCode());  
	        }else{  
	           // System.err.println("store storage servers count: " + storageServers.length);  
//	            for (int k = 0; k < storageServers.length; k++){  
//	                System.err.println(k + 1 + ". " + storageServers[k].getInetSocketAddress().getAddress().getHostAddress() + ":" + storageServers[k].getInetSocketAddress().getPort());  
//	            }  
//	            System.err.println("");  
	        }  
	      //  long startTime = System.currentTimeMillis(); 
	        String[] results=null;
	        try {
	        	results= storageClient.upload_file(file_buff, cate,meta_list);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			  //System.out.println("upload_file time used: " + (System.currentTimeMillis() - startTime) + " ms");  
			  
		        if (results == null){  
		           // System.err.println("upload file fail, error code: " + storageClient.getErrorCode());  
		            return results;
		        }  
		        group_name = results[0];  
		        String remote_filename = results[1];  
		        System.err.println("group_name: " + group_name + ", remote_filename: " + remote_filename);  
//		        try {
//					System.err.println(storageClient.get_file_info(group_name, remote_filename));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (MyException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}  
		  
		        ServerInfo[] servers=null;
				try {
					servers = trackerClient.getFetchStorages(trackerServer, group_name, remote_filename);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
		        if (servers == null){  
		            System.err.println("get storage servers fail, error code: " + trackerClient.getErrorCode());  
		        } else {  
		            System.err.println("storage servers count: " + servers.length);  
		            for (int k = 0; k < servers.length; k++){  
		                System.err.println(k + 1 + ". " + servers[k].getIpAddr() + ":" + servers[k].getPort());  
		            }  
		            System.err.println("");  
		        }  
		        
		      //  System.err.println("文件:"+file.exists());
		        if(file.exists())
		        {
		        	file.delete();
		        }
		        System.err.println("文件被删除:"+file.getAbsolutePath());
		      //  System.err.println("文件2:"+file.exists());
	        return results;
	}
    public static void main(String[] args) throws Exception{  
       // String classPath = new File(TestUpload.class.getResource("/").getFile()).getCanonicalPath();  
       // String configFilePath = classPath + File.separator + "fdfs_client.conf"; 
    	String configFilePath ="./fdfs_client.conf";
        System.out.println("配置文件:" + configFilePath);  
          
        ClientGlobal.init(configFilePath);  
          
        TrackerClient trackerClient = new TrackerClient();  
        TrackerServer trackerServer = trackerClient.getConnection();  
  
        StorageServer storageServer = null;  
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);  
          
        NameValuePair[] meta_list = new NameValuePair[3];  
        meta_list[0] = new NameValuePair("width", "120");  
        meta_list[1] = new NameValuePair("heigth", "120");  
        meta_list[2] = new NameValuePair("author", "gary");  
          
//      byte[] file_buff = "F:\\pic.jpg".getBytes(ClientGlobal.g_charset);  
        File file = new File("e:\\Blob1.jpg");  
        FileInputStream fis = new FileInputStream(file);  
        byte[] file_buff = null;  
        if(fis != null){  
            int len = fis.available();  
            file_buff = new byte[len];  
            fis.read(file_buff);  
        }  
        System.out.println("file length: " + file_buff.length);  
          
        String group_name = null;  
        StorageServer[] storageServers = trackerClient.getStoreStorages(trackerServer, group_name);  
        if (storageServers == null) {  
            System.err.println("get store storage servers fail, error code: " + storageClient.getErrorCode());  
        }else{  
            System.err.println("store storage servers count: " + storageServers.length);  
            for (int k = 0; k < storageServers.length; k++){  
                System.err.println(k + 1 + ". " + storageServers[k].getInetSocketAddress().getAddress().getHostAddress() + ":" + storageServers[k].getInetSocketAddress().getPort());  
            }  
            System.err.println("");  
        }  
          
        long startTime = System.currentTimeMillis();  
        String[] results = storageClient.upload_file(file_buff, "jpg", meta_list);  
        System.out.println("upload_file time used: " + (System.currentTimeMillis() - startTime) + " ms");  
  
        if (results == null){  
            System.err.println("upload file fail, error code: " + storageClient.getErrorCode());  
            return;  
        }  
          
        group_name = results[0];  
        String remote_filename = results[1];  
        System.err.println("group_name: " + group_name + ", remote_filename: " + remote_filename);  
        System.err.println(storageClient.get_file_info(group_name, remote_filename));  
  
        ServerInfo[] servers = trackerClient.getFetchStorages(trackerServer, group_name, remote_filename);  
        if (servers == null){  
            System.err.println("get storage servers fail, error code: " + trackerClient.getErrorCode());  
        } else {  
            System.err.println("storage servers count: " + servers.length);  
            for (int k = 0; k < servers.length; k++){  
                System.err.println(k + 1 + ". " + servers[k].getIpAddr() + ":" + servers[k].getPort());  
            }  
            System.err.println("");  
        }  
    }  
}  