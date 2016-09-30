package ps.landerbuluse.stock.model.server;

import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.runner.running.RuningMethod;
import ps.landerbuluse.stock.user.account.UserStockAccount;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/30.
 */
public abstract class ModelTrainServer implements RuningMethod {

    public ModelTrainServer(){

    }

    /**
     * 训练方法
     * @param stocks 多stock训练
     */
    public abstract void runData(ArrayList<StockData> stocks);


    @Override
    public void interatorRunningByOpening(UserStockAccount account,StockData stockData,String date) {

    }

    @Override
    public void interatorRunningBySession(UserStockAccount account,StockData stockData,String date) {

    }

    @Override
    public void interatorRunningByClose(UserStockAccount account,StockData stockData, String date) {

    }
}
