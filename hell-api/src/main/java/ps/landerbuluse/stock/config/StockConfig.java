package ps.landerbuluse.stock.config;

/**
 * Created by Administrator on 2016/9/30.
 */
public class StockConfig {

    public String stockName= null;
    /**
     * 股票id
     */
    public String stockId= "sh600000";
    /**
     * 股票开始时间
     */
    public String start = "20150101";
    /**
     * 股票结束时间
     */
    public String end = "20150809";

    public StockConfig(String stockName,String stockId,String start,String end){
        this.stockName = stockName;
        this.stockId = stockId;
        this.start = start;
        this.end = end;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}
