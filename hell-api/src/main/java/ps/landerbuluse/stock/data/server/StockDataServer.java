package ps.landerbuluse.stock.data.server;

import ps.landerbuluse.stock.config.StockConfig;
import ps.landerbuluse.stock.config.StocksConfig;
import ps.landerbuluse.stock.data.StockData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/23.
 */
public interface StockDataServer {


    public StockData getData(String stockName,String stockId,String start,String end);

    public ArrayList<StockData> getData(StocksConfig config);

    public void parse(StockData stockData,String line);

    public void test();
}
