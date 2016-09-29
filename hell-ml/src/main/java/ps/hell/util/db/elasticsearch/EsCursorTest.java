package ps.hell.util.db.elasticsearch;
//
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHits;
//
//import java.util.ArrayList;
//
//public class EsCursorTest {
//
//	public static void main(String[] args) {
//		EsClientTool tool = new EsClientTool("bigdata-es",
//				new String[] { "10.10.202.18" }, 9300, "crm_index", "crm");
//		Client client = tool.client;
//		SearchResponse response = client.prepareSearch(tool.index)
//				.setTypes(tool.type).setQuery(QueryBuilders.matchAllQuery())
//				.setQuery(QueryBuilders.rangeQuery("age").gt(1)).setSize(10000)
//				.setScroll(new TimeValue(600000))
//				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).execute()
//				.actionGet();
//		String scrollid = response.getScrollId();
//		while (true) {
//			SearchResponse response2 = client.prepareSearchScroll(scrollid)
//					.setScroll(new TimeValue(1000000)).execute().actionGet();
//			SearchHits searchHit = response2.getHits();
//			// 再次查询不到数据时跳出循环
//			if (searchHit.getHits().length == 0) {
//				break;
//			}
//			ArrayList<String> list = new ArrayList<String>();
//			System.out.println("查询数量 ：" + searchHit.getHits().length);
//			for (int i = 0; i < searchHit.getHits().length; i++) {
//				String json = searchHit.getHits()[i].getSourceAsString();
//				list.add(json);
//			}
//		}
//	}
//}
