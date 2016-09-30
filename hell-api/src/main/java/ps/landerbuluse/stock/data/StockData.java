package ps.landerbuluse.stock.data;

/**
 * Created by Administrator on 2016/9/23.
 */


import java.util.ArrayList;

/**
 * 股票 data数据源
 */
public class StockData {

    /**
     * 索引
     */
    public StockIndexData index =null;
    /**
     * 实际数据
     */
    public ArrayList<StockBean> data = null;
    /**
     * 股票名
     */
    public String stockName = null;
    /**
     * 股票id
     */
    public String stockId = null;
    /**
     * 开始时间
     */
    public String start = null;
    /**
     * 结束时间
     */
    public String end = null;
    /**
     *
     * @param stockName 股票名
     * @param stockId 股票id
     */
    public StockData(String stockName,String stockId){
        this.stockName=stockName;
        this.stockId = stockId;
    }

    /**
     * 根据日期获取数据
     * @param date
     * @return
     */
    public StockBean getDataByDate(String date){
        Integer val = this.index.map.getByKey(date);
        if(val == null){
            return null;
        }
        return this.data.get(val);
    }

    /**
     * 添加一直股票一天的数据
     * @param bean
     */
    public void addStockBeanByDay(StockBean bean){
        if(this.data == null){
            this.data = new ArrayList<StockBean>();
            this.index = new StockIndexData();
        }
        this.data.add(bean);
        this.index.addStockBean(bean);
        if(this.start ==null){
            this.start = bean.date;
            this.end = bean.date;
        }
        if(this.end.compareTo(bean.date) <0){
            this.end = bean.date;
        }
    }

    public StockIndexData getIndex() {
        return index;
    }

    public void setIndex(StockIndexData index) {
        this.index = index;
    }

    public ArrayList<StockBean> getData() {
        return data;
    }

    public void setData(ArrayList<StockBean> data) {
        this.data = data;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }
}
