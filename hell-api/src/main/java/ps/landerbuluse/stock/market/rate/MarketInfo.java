package ps.landerbuluse.stock.market.rate;

import ps.landerbuluse.stock.config.StockConfig;
import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.data.server.StockDataServer;
import ps.landerbuluse.stock.util.StockComputeTool;

/**
 * Created by Administrator on 2016/10/9.
 */
public class MarketInfo {

    /**
     * 市场无风险收益率
     */
    public float marketnotRiskRate =2.7f;
    /**
     * 市场收益率
     */
    public float marketRate =0f;

    /**
     * 市场数据
     */
    public StockData marketData =null;

    public MarketInfo(StockDataServer server){
        //初始化市场收益率值
        this.marketData= server.getStranderData();
        this.marketRate = StockComputeTool.getIncomeRate(marketData);
    }

    public StockData getMarketData() {
        return marketData;
    }

    public void setMarketData(StockData marketData) {
        this.marketData = marketData;
    }

    public float getMarketnotRiskRate() {
        return marketnotRiskRate;
    }

    public void setMarketnotRiskRate(float marketnotRiskRate) {
        this.marketnotRiskRate = marketnotRiskRate;
    }

    public float getMarketRate() {
        return marketRate;
    }

    public void setMarketRate(float marketRate) {
        this.marketRate = marketRate;
    }
}
