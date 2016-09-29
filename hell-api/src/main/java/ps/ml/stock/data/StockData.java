package ps.ml.stock.data;

/**
 * Created by Administrator on 2016/9/23.
 */

import java.util.ArrayList;
import java.util.List;

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
    public List<StockBean> data = null;
    /**
     * 股票名
     */
    public String stockName = null;
    /**
     * 股票id
     */
    public Long stockId = null;


}
