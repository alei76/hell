package ps.landerbuluse.stock.config;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/30.
 */
public class StocksConfig {

    /**
     * sockelist
     */
    ArrayList<StockConfig> stocksConfig = new ArrayList<StockConfig>();

    public StocksConfig(){

    }

    /**
     * 添加需要获取的股票类
     * @param stockName
     * @param stockId
     * @param start
     * @param end
     */
    public void add(String stockName,String stockId,String start,String end){
        stocksConfig.add(new StockConfig(stockName,stockId,start,end));
    }

    public ArrayList<StockConfig> getStocksConfig() {
        return stocksConfig;
    }

    public void setStocksConfig(ArrayList<StockConfig> stocksConfig) {
        this.stocksConfig = stocksConfig;
    }
}
