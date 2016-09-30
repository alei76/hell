package ps.landerbuluse.stock.runner.running;

import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.user.account.UserStockAccount;

/**
 * Created by Administrator on 2016/9/30.
 */
public interface RuningMethod {
    /**
     * 开盘执行方法
     *
     * @param stockData
     * @param date
     */
    public void interatorRunningByOpening(UserStockAccount account,StockData stockData, String date);

    /**
     * 盘中执行方法
     *
     * @param stockData
     * @param date
     */
    public void interatorRunningBySession(UserStockAccount account,StockData stockData, String date);

    /**
     * 盘末执行方法
     *
     * @param stockData
     * @param date
     */
    public void interatorRunningByClose(UserStockAccount account,StockData stockData, String date);
}