package ps.landerbuluse.stock.runner.test;

import ps.landerbuluse.stock.config.StockConfig;
import ps.landerbuluse.stock.config.StocksConfig;
import ps.landerbuluse.stock.runner.MainRunnerMan;
import ps.landerbuluse.stock.strategy.inter.StrategyInter;
import ps.landerbuluse.stock.strategy.test.StrateSalTest;
import ps.landerbuluse.stock.strategy.test.StrategyBuyTest;
import ps.landerbuluse.stock.user.UserBean;

/**
 * Created by Administrator on 2016/9/30.
 */
public class MainRunnerManTest {

    public static void main(String[] args) {
        StocksConfig config = new StocksConfig();
        config.add("test", "sz000025", "20160101", "20160901");
        //策略执行
        MainRunnerMan man = new MainRunnerMan(config);
        UserBean bean = new UserBean();
        bean.recharge(100000);
        StrategyInter buy = new StrategyBuyTest();
        StrategyInter sale = new StrateSalTest();
        bean.addcloseQuotation(buy);
        bean.addcloseQuotation(sale);
        man.initUser(bean);
        Thread thread = new Thread(man);
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bean.printAccountStatus();

        man.plot();
    }
}
