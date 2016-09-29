package ps.hell.util.db.elasticsearch;
//
//import org.elasticsearch.action.bulk.BulkRequestBuilder;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.action.update.UpdateRequest;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHits;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import ps.hell.util.FileUtil;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class EsClientTool {
//	/**
//	 * 日志
//	 */
//	public static Logger logger = LoggerFactory.getLogger(EsClientTool.class);
//	public Client client;
//	FileUtil file = null;
//
//	String index = "crm_index_test";
//
//	String type = "crm_test";
//
//	String cluseterName = "es-cluster";
//	/**
//	 * 打印数量
//	 */
//	public int printNum = 1000;
//	/**
//	 * 批量数量
//	 */
//	public int bulkNum = 500;
//	/**
//	 * 行最小长度过滤
//	 */
//	public int minLength = 48;
//	/**
//	 * 文件分隔符
//	 */
//	public String split = "";
//
//	/**
//	 * 对应更新的地址
//	 */
//	public String tags = null;
//	/**
//	 * tag对应的尾部
//	 */
//	public String tagsEnd = null;
//
//	Pattern pattern = Pattern.compile("\"user_id\":\"([^\"]*?)\"");
//	Matcher matcher = null;
//
//	/**
//	 *
//	 * @param cluseterName
//	 * @param ip
//	 * @param port
//	 * @param index
//	 * @param type
//	 */
//	public EsClientTool(String cluseterName, String[] ip, int port,
//			String index, String type) {
//		this.cluseterName = cluseterName;
//		init(ip, port, index, type);
//
//	}
//
//	/**
//	 * 文件输入
//	 *
//	 * @param source
//	 * @param ip
//	 * @param port
//	 * @param index
//	 * @param type
//	 * @param printNum
//	 *            打印对应的数量
//	 * @param bulkNum
//	 *            批量对应的数量
//	 */
//	public EsClientTool(String source, String cluseterName, String[] ip,
//			int port, String index, String type, int printNum, int bulkNum,
//			int minLength) {
//		file = new FileUtil(source, "utf-8", false, null);
//		this.cluseterName = cluseterName;
//		this.printNum = printNum;
//		this.bulkNum = bulkNum;
//		this.minLength = minLength;
//		init(ip, port, index, type);
//
//	}
//
//	public String scrollid = null;
//
//	/**
//	 * 批量查询接口
//	 * 循环调用 getNextBulk获取批量结果
//	 */
//	public void searchBluk(int bulkNumber) {
//		SearchResponse response = client.prepareSearch(index)
//				.setTypes(type).setQuery(QueryBuilders.matchAllQuery())
//				.setSize(bulkNumber).setScroll(new TimeValue(600000))
//				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).execute().actionGet();
//		this.scrollid = response.getScrollId();
//	}
//
//	/**
//	 * 获取下一次查询获取的数据
//	 * @return 如果返回为null则表示结束
//	 */
//	public ArrayList<String> getNextBulk() {
//		SearchResponse response2 = client.prepareSearchScroll(scrollid)
//				.setScroll(new TimeValue(1000000)).execute().actionGet();
//		SearchHits searchHit = response2.getHits();
//		// 再次查询不到数据时跳出循环
//		if (searchHit.getHits().length == 0) {
//			return null;
//		}
//		ArrayList<String> list = new ArrayList<String>();
//		 System.out.println("查询数量 ：" + searchHit.getHits().length);
//		for (int i = 0; i < searchHit.getHits().length; i++) {
//			String json = searchHit.getHits()[i].getSourceAsString();
//			list.add(json);
//		}
//		return list;
//	}
//
//	/**
//	 * 文件输入
//	 *
//	 * @param source
//	 * @param ip
//	 * @param port
//	 * @param index
//	 * @param type
//	 * @param printNum
//	 *            打印对应的数量
//	 * @param bulkNum
//	 *            批量对应的数量
//	 * @param tags
//	 *            对应es中索引的修改键 已逗号分隔
//	 */
//	public EsClientTool(String source, String cluseterName, String[] ip,
//			int port, String index, String type, int printNum, int bulkNum,
//			int minLength, String split, String tags) {
//		file = new FileUtil(source, "utf-8", false, null);
//		this.cluseterName = cluseterName;
//		this.printNum = printNum;
//		this.bulkNum = bulkNum;
//		this.minLength = minLength;
//		this.split = split;
//		if (tags != null) {
//			String[] tag = tags.split("\\.");
//			StringBuffer sb = new StringBuffer();
//			StringBuffer sb2 = new StringBuffer();
//			for (String st : tag) {
//				sb.append("{").append("\"").append(st).append("\":");
//				sb2.append("}");
//			}
//			this.tags = sb.toString();
//			tagsEnd = sb2.toString();
//		}
//		init(ip, port, index, type);
//
//	}
//
//	/**
//	 * 初始化
//	 *
//	 * @param ip
//	 * @param port
//	 * @param index
//	 * @param type
//	 */
//	public void init(String[] ip, int port, String index, String type) {
//		this.index = index;
//		this.type = type;
//		try {
//			Settings settings = Settings.settingsBuilder()
//					.put("cluster.name", cluseterName).build();
//			client = TransportClient.builder().settings(settings).build();
//			for (String ipo : ip) {
//				System.out.println(ipo);
//				((TransportClient) client)
//						.addTransportAddress(new InetSocketTransportAddress(
//								InetAddress.getByName(ipo), port));
//			}
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 搜索测试集合
//	 *
//	 * @param query
//	 */
//	public void searchTest(String query) {
//		SearchResponse response = client.prepareSearch(index).setTypes(type)
//				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//				.setQuery(QueryBuilders.termQuery("content", query)) // Query
//				// .setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18));
//				.setFrom(0).setSize(10).setExplain(true).execute().actionGet();
//		SearchHits hits = response.getHits();
//		System.out.println(hits.getTotalHits());
//		for (int i = 0; i < hits.getHits().length; i++) {
//			System.out.println(hits.getHits()[i].getSourceAsString());
//		}
//	}
//
//	/**
//	 * 创建新索引
//	 */
//	public void createIndex(String jsonData) {
//		client.prepareIndex(index, type).setSource(jsonData).execute()
//				.actionGet();
//	}
//
//	public void close() {
//		client.close();
//	}
//
//	/**
//	 * 获取数据集
//	 *
//	 * @return
//	 */
//	public ArrayList<String> getData() {
//		return file.readAndClose();
//	}
//
//	/**
//	 * 批量流式更新
//	 */
//	public void bulkStreamUpdateCondition() {
//		BulkRequestBuilder builder = null;
//		builder = client.prepareBulk();
//		String data = null;
//		int i = 0;
//		String user_id = null;
//		while ((data = file.getNextLine()) != null) {
//			if (data.length() <= minLength) {
//				logger.info("长度小于" + minLength + ":" + data);
//				continue;
//			}
//			String[] datas = data.split(this.split);
//			// logger.info(user_id);
//			if (datas == null || datas.length <= 1) {
//				logger.error("文件内容错误 不存在主键，或不存在内容 以 " + this.split + " 为分隔符:"
//						+ data);
//				continue;
//			}
//			user_id = datas[0];
//			String doc = null;
//			i++;
//			if (i % printNum == 0) {
//				logger.info("doc number:" + i);
//			}
//			if (tags == null) {
//				doc = datas[1];
//			} else {
//				doc = this.tags + datas[1] + this.tagsEnd;
//			}
//			IndexRequest indexRequest = new IndexRequest(index, type, user_id)
//					.source(doc);
//			if (tags == null) {
//				UpdateRequest updateRequest = new UpdateRequest(index, type,
//						user_id).doc(doc).upsert(indexRequest);
//				// client.update(updateRequest);
//				// Thread.sleep(10);
//				builder.add(updateRequest);
//			} else {
//				UpdateRequest updateRequest = null;
//				updateRequest = new UpdateRequest(index, type, user_id)
//						.doc(doc).upsert(indexRequest);
//				builder.add(updateRequest);
//			}
//			if (builder.numberOfActions() >= bulkNum) {
//				BulkResponse bulkResponse = builder.execute().actionGet();
//				builder = client.prepareBulk();
//				if (bulkResponse.hasFailures()) {
//					logger.error(bulkResponse.buildFailureMessage());
//					logger.error("批量创建索引错误！");
//					System.exit(1);
//				}
//			}
//		}
//		if (builder.numberOfActions() > 0) {
//			BulkResponse bulkResponse = builder.execute().actionGet();
//			builder = client.prepareBulk();
//			if (bulkResponse.hasFailures()) {
//				logger.error(bulkResponse.buildFailureMessage());
//				logger.error("批量创建索引错误！");
//				System.exit(1);
//			}
//		}
//		logger.info("总共执行了:" + i + "行");
//	}
//
//	/**
//	 * 流式批量插入
//	 */
//	public void bulkStreamInsert() {
//		BulkRequestBuilder builder = null;
//		builder = client.prepareBulk();
//		String data = null;
//		int i = 0;
//		String user_id = null;
//		while ((data = file.getNextLine()) != null) {
//			if (data.length() <= minLength) {
//				logger.info("长度小于" + minLength + ":" + data);
//				continue;
//			}
//
//			matcher = pattern.matcher(data);
//			if (matcher == null) {
//				logger.info("matcher 异常:" + data);
//				continue;
//			}
//
//			if (matcher.find()) {
//				user_id = matcher.group(1);
//			} else {
//				logger.info("user_id 未找到：" + data);
//				continue;
//			}
//			// logger.info(user_id);
//			i++;
//			if (i % printNum == 0) {
//				logger.info("doc number:" + i);
//			}
//			IndexRequest indexRequest = new IndexRequest(index, type, user_id)
//					.source(data);
//			UpdateRequest updateRequest = new UpdateRequest(index, type,
//					user_id).doc(data).upsert(indexRequest);
//			// client.update(updateRequest);
//			// Thread.sleep(10);
//			builder.add(updateRequest);
//
//			if (builder.numberOfActions() >= bulkNum) {
//				BulkResponse bulkResponse = builder.execute().actionGet();
//				builder = client.prepareBulk();
//				if (bulkResponse.hasFailures()) {
//					logger.error(bulkResponse.buildFailureMessage());
//					logger.error("批量创建索引错误！");
//					System.exit(1);
//				}
//			}
//		}
//		if (builder.numberOfActions() > 0) {
//			BulkResponse bulkResponse = builder.execute().actionGet();
//			builder = client.prepareBulk();
//			if (bulkResponse.hasFailures()) {
//				logger.error(bulkResponse.buildFailureMessage());
//				logger.error("批量创建索引错误！");
//				System.exit(1);
//			}
//		}
//		logger.info("总共执行了:" + i + "行");
//	}
//
//	public static void main(String[] args) {
//		// EsClientDemo demo = new
//		// EsClientDemo("F:/work/crm/project/000000_0","es-cluster",
//		// new String[] { "10.255.52.20" }, 9300, "crm_index_test",
//		// "crm_test", 1000, 500);
//		// demo.bulkInsert();
//		// demo.close();
//		logger.info("filePath es_cluster_name,ip:ip,port,index,type,printNum,bulkNum");
//		logger.info("args=new String[]{\"F:/work/crm/project/000000_0\",\"es-cluster\",\"10.255.52.20:\",\"9300\", \"crm_index_test\",\"crm_test\",\"1000\",\"500\",\"48\",\"ds\",\"fund.logger\"}");
//		// args=new
//		// String[]{"F:/work/crm/000000_0","es-cluster","10.255.52.20:","9300",
//		// "crm_index_test","crm_test","1000","500"};
//		logger.info("输出参数");
//		logger.info(Arrays.toString(args));
//		String filePath = args[0];
//		String clusterName = args[1];
//		String[] esIps = args[2].split(":");
//		int port = Integer.parseInt(args[3]);
//		String index = args[4];
//		String type = args[5];
//		int printNum = Integer.parseInt(args[6]);
//		int bulkNum = Integer.parseInt(args[7]);
//		int minLenfth = Integer.parseInt(args[8]);
//		if (args.length == 10) {
//			EsClientTool demo = new EsClientTool(filePath, clusterName, esIps,
//					port, index, type, printNum, bulkNum, minLenfth, args[9],
//					null);
//			demo.bulkStreamUpdateCondition();
//			demo.close();
//		}
//		if (args.length == 11) {
//			EsClientTool demo = new EsClientTool(filePath, clusterName, esIps,
//					port, index, type, printNum, bulkNum, minLenfth, args[9],
//					args[10]);
//			demo.bulkStreamUpdateCondition();
//			demo.close();
//		} else if (args.length == 9) {
//			EsClientTool demo = new EsClientTool(filePath, clusterName, esIps,
//					port, index, type, printNum, bulkNum, minLenfth);
//			demo.bulkStreamInsert();
//			demo.close();
//		} else {
//			logger.info("输入参数异常，请确认参数");
//			System.exit(1);
//		}
//	}
//}
