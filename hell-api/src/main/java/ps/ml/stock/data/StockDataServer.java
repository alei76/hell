package ps.ml.stock.data;

/**
 * Created by Administrator on 2016/9/23.
 */
public interface StockDataServer {


    public Object getData(String stockId,String start,String end);

    public void test();
}
