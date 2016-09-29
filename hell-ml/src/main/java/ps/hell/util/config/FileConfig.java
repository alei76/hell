package ps.hell.util.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;


public class FileConfig {
//	private static Logger log = Logger.getLogger(ParkingExecThread.class);
	/**
	 * 停车信息
	 */
	public static String parkingFile="";
	/**
	 * 编码
	 */
	public static String parkingFileCode="";
	/**
	 * job文件信息
	 */
	public static String jobsFile="";
	/**
	 * 编码
	 */
	public static String jobsFileCode="";
	/**
	 * 地铁口对应 公交车站信息
	 */
	public static String subwayAndBusFile="";
	/**
	 * 编码
	 */
	public static String subwayAndBusFileCode="";
	/**
	 * 公交信息
	 */
	public static String busAndStationFile="";
	/**
	 * 编码
	 */
	public static String busAndStationFileCode="";
	/**
	 * 搜房url
	 */
	public static String fangFile="";
	/**
	 * 编码
	 */
	public static String fangFileCode="";
	
	/**
	 * 搜房新的信息
	 */
	public static String fangNewFile="";
	
	public static String fangNewFileCode="";
	
	/**
	 * 公司文件
	 */
	public static String companyFile="";
	/**
	 * 公司对应的编码
	 */
	public static String companyFileCode="";
	
	/**
	 * 公司文件最终
	 */
	public static String companyEndFile="";
	/**
	 * 公司对应的编码
	 */
	public static String companyEndFileCode="";
	
	/**
	 * 公司文件
	 */
	public static String companySearchFile="";
	/**
	 * 公司对应的编码
	 */
	public static String companySearchFileCode="";
	
	/**
	 * 公司文件
	 */
	public static String companySearchEndFile="";
	/**
	 * 公司对应的编码
	 */
	public static String companySearchEndFileCode="";
	/**
	 * 存储 公司的详情信息
	 */
	public static String companyDescFile="";
	public static String companyDescFileCode="";
	/**
	 * 存储公司对应工作的url信息
	 */
	public static String companyDescJobFile="";
	public static String companyDescJobFileCode="";
	/**
	 * 工作的详情信息
	 */
	public static String companyDescJobDescFile="";
	public static String companyDescJobDescFileCode="";
	
	
	/**
	 * 大众点评商圈文件
	 */
	public static String bussinessAreaFile="";
	public static String bussinessAreaFileCode="";
	public static String otherFile="";
	/**
	 * 字符串对应
	 * key 为 文件中的名字
	 * String为对应的地址
	 */
	public static HashMap<String,String> map=new HashMap<String,String>();
	
	static {
		Properties props=new Properties();
		try {
			InputStream in = new BufferedInputStream (new FileInputStream("./data/file.properties"));
			//InputStream in = getClass().getResourceAsStream("/IcisReport.properties");
				try {
					props.load(in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		parkingFile=props.getProperty("parkingFile");
		jobsFile=props.getProperty("jobsFile");
		subwayAndBusFile=props.getProperty("subwayAndBusFile");
		busAndStationFile=props.getProperty("busAndStationFile");
		fangFile=props.getProperty("fangFile");
		fangNewFile=props.getProperty("fangNewFile");
		companyFile=props.getProperty("companyFile");
		companyEndFile=props.getProperty("companyEndFile");
		companySearchFile=props.getProperty("companySearchFile");
		companySearchEndFile=props.getProperty("companySearchEndFile");
		companyDescFile=props.getProperty("companyDescFile");
		companyDescJobFile=props.getProperty("companyDescJobFile");
		companyDescJobDescFile=props.getProperty("companyDescJobDescFile");
		bussinessAreaFile=props.getProperty("bussinessAreaFile");
		
		parkingFileCode=props.getProperty("parkingFileCode");
		jobsFileCode=props.getProperty("jobsFileCode");
		subwayAndBusFileCode=props.getProperty("subwayAndBusFileCode");
		busAndStationFileCode=props.getProperty("busAndStationFileCode");
		fangFileCode=props.getProperty("fangFileCode");
		fangNewFileCode=props.getProperty("fangNewFileCode");
		companyFileCode=props.getProperty("companyFileCode");
		companyEndFileCode=props.getProperty("companyEndFileCode");
		companySearchFileCode=props.getProperty("companySearchFileCode");
		companySearchEndFileCode=props.getProperty("companySearchEndFileCode");
		companyDescFileCode=props.getProperty("companyDescFileCode");
		companyDescJobFileCode=props.getProperty("companyDescJobFileCode");
		companyDescJobDescFileCode=props.getProperty("companyDescJobDescFileCode");
		bussinessAreaFileCode=props.getProperty("bussinessAreaFileCode");
		
		parkingFile=props.getProperty("parkingFile");
		jobsFile=props.getProperty("jobsFile");
		subwayAndBusFile=props.getProperty("subwayAndBusFile");
		busAndStationFile=props.getProperty("busAndStationFile");
		fangFile=props.getProperty("fangFile");
		companyFile=props.getProperty("companyFile");
		companyEndFile=props.getProperty("companyEndFile");
		companySearchFile=props.getProperty("companySearchFile");
		companySearchEndFile=props.getProperty("companySearchEndFile");
		companyDescFile=props.getProperty("companyDescFile");
		companyDescJobFile=props.getProperty("companyDescJobFile");
		companyDescJobDescFile=props.getProperty("companyDescJobDescFile");
		bussinessAreaFile=props.getProperty("bussinessAreaFile");
		map.put("parkingFile", parkingFile);
		map.put("jobsFile", jobsFile);
		map.put("subwayAndBusFile",subwayAndBusFile);
		map.put("busAndStationFile", busAndStationFile);
		map.put("fangFile", fangFile);
		map.put("fangNewFile",fangNewFile);
		map.put("companyFile", companyFile);
		map.put("companyEndFile", companyEndFile);
		map.put("companySearchFile", companySearchFile);
		map.put("companySearchEndFile", companySearchEndFile);
		map.put("companyDescFile", companyDescFile);
		map.put("companyDescJobFile", companyDescJobFile);
		map.put("companyDescJobDescFile", companyDescJobDescFile);
		map.put("bussinessAreaFile", bussinessAreaFile);
		
	}
	/**
	 * 获取地址
	 * @param str
	 * @return
	 */
	public static String getmap(String str)
	{
		System.out.println(str);
		return map.get(str);
	}
	
}
