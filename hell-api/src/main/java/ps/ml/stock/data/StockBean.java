package ps.ml.stock.data;

/**
 * Created by Administrator on 2016/9/23.
 */
public class StockBean {
    /**
     * 时间
     */
    public String date =null;

    /**
     * 开盘价
     */
    public Float openPrice = 0f;

    /**
     * 收盘价
     */
    public Float closePrice = 0f;

    /**
     * 当日最高价
     */
    public Float hightPrice =0f;
    /**
     * 当日最低价
     */
    public Float lowerPrice =0f;
    /**
     * 成交量
     */
    public Long urnover = 0L;

    public void setDate(String date) {
        this.date = date;
    }

    public void setOpenPrice(Float openPrice) {
        this.openPrice = openPrice;
    }

    public void setClosePrice(Float closePrice) {
        this.closePrice = closePrice;
    }

    public void setHightPrice(Float hightPrice) {
        this.hightPrice = hightPrice;
    }

    public void setLowerPrice(Float lowerPrice) {
        this.lowerPrice = lowerPrice;
    }

    public void setUrnover(Long urnover) {
        this.urnover = urnover;
    }

    public String getDate() {
        return date;
    }

    public Float getOpenPrice() {
        return openPrice;
    }

    public Float getClosePrice() {
        return closePrice;
    }

    public Float getHightPrice() {
        return hightPrice;
    }

    public Float getLowerPrice() {
        return lowerPrice;
    }

    public Long getUrnover() {
        return urnover;
    }
}
