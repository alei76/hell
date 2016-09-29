package ps.hell.util.db.fastdfs;

import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
 * 删除文件
 *
 * @author gary
 */
public class TestDelete {
    public static boolean run(String groupName, String imgPath) {
        //imagePath 的第一个一定是 group组信息

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);


        int result = 0;
        try {
            result = storageClient.delete_file(groupName, imgPath);
            //System.out.println("删除状态:"+result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println("删除成功");
//			if(result==0)
//			{
//				return false;
//			}else
//			{
//				return true;
//			}
        return true;
    }

    public static void main(String[] args) throws Exception {
//        String classPath = new File(TestDelete.class.getResource("/").getFile()).getCanonicalPath();  
//        String configFilePath = classPath + File.separator + "fdfs_client.conf";  
        String configFilePath = "./fdfs_client.conf";
        System.out.println("配置文件:" + configFilePath);
        ClientGlobal.init(configFilePath);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);

        String group_name = "group1";
        String remote_filename = "M00/00/00/wKgxgk5HbLvfP86RAAAAChd9X1Y736.jpg";
        storageClient.delete_file(group_name, remote_filename);
        System.out.println("删除成功");
    }
}  