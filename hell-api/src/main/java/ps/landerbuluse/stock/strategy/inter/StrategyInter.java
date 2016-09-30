package ps.landerbuluse.stock.strategy.inter;

import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.user.account.UserStockAccount;

/**
 * Created by Administrator on 2016/9/30.
 */
public interface StrategyInter {

    /**
     * 初始化参数
     */
    public void init();

    /**
     * 获取策略
     * @return 是否执行策略
     */
    public void runStrategy(UserStockAccount account,StockData stockData,String date);

}
