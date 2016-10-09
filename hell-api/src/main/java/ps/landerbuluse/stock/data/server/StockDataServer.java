package ps.landerbuluse.stock.data.server;

import ps.landerbuluse.stock.config.StockConfig;
import ps.landerbuluse.stock.config.StocksConfig;
import ps.landerbuluse.stock.data.StockData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/23.
 */
public interface StockDataServer {

    /**
     * 获取基准市场数据集
     * @return
     */
    public StockData getStranderData();

    /**
     * 获取某一个数据集
     * @param stockName
     * @param stockId
     * @param start
     * @param end
     * @return
     */
    public StockData getData(String stockName,String stockId,String start,String end);

    public ArrayList<StockData> getData(StocksConfig config);

    /**
     * 需要加载到stockData中
     * @param stockData
     * @param line
     */
    public void parse(StockData stockData,String line);

    public void test();
}
