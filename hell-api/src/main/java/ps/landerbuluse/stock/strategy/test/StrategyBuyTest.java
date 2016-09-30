package ps.landerbuluse.stock.strategy.test;

import ps.landerbuluse.stock.data.StockBean;
import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.strategy.inter.StrategyInter;
import ps.landerbuluse.stock.user.account.UserStockAccount;

/**
 * Created by Administrator on 2016/9/30.
 */
public class StrategyBuyTest implements StrategyInter{
    @Override
    public void init() {

    }

    @Override
    public void runStrategy(UserStockAccount account, StockData stockData, String date) {
        StockBean bean =stockData.getDataByDate(date);
        if(bean == null){
            return;
        }
        if(bean.getOpenPrice() > bean.getClosePrice()){

            int buyStockByCount = account.getCanBuyMaxCount(bean.getClosePrice())/2;
            account.buy(stockData.getStockId(), buyStockByCount, bean.getClosePrice());
        }
    }

}
