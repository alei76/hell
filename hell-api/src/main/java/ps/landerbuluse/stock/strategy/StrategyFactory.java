package ps.landerbuluse.stock.strategy;

import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.market.rate.server.FeeInter;
import ps.landerbuluse.stock.model.server.ModelTrainServer;
import ps.landerbuluse.stock.runner.running.RuningMethod;
import ps.landerbuluse.stock.strategy.inter.StrategyInter;
import ps.landerbuluse.stock.user.account.UserStockAccount;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/30.
 */
public class StrategyFactory implements RuningMethod {

    /**
     * 开盘策略
     */
    private ArrayList<StrategyInter>  openingQuotation =new ArrayList<StrategyInter>();
    /**
     * 盘中策略
     */
    private ArrayList<StrategyInter>  sessionQuotation =new ArrayList<StrategyInter>();
    /**
     * 收盘策略
     */
    private ArrayList<StrategyInter>  closeQuotation =new ArrayList<StrategyInter>();

    /**
     * 训练模型方法
     */
    private ModelTrainServer modelTrain = null;

    public StrategyFactory(){

    }

    public void setModelTrain(ModelTrainServer modelTrain){
        this.modelTrain = modelTrain;
    }

    /**
     * 添加开盘策略
     * @param strate
     */
    public void addopeningQuotation(StrategyInter strate){
        strate.init();
        this.openingQuotation.add(strate);
    }

    /**
     * 添加盘中策略
     * @param strate
     */
    public void addsessionQuotation(StrategyInter strate){
        strate.init();
        this.sessionQuotation.add(strate);
    }

    /**
     * 添加关盘策略
     * @param strate
     */
    public void addcloseQuotation(StrategyInter strate){
        strate.init();
        this.closeQuotation.add(strate);
    }

    public void init(ArrayList<StockData> stocks){
        trainModule(stocks);
    }

    /**
     * 训练模型
     * @param stocks
     */
    public void trainModule(ArrayList<StockData> stocks){
        if(this.modelTrain != null){
            modelTrain.runData(stocks);
        }
    }
    /**
     * 迭代器
     * @param stockData 原始数据源
     * @param date 使用的索引值
     */
    public void interatorRunning(UserStockAccount account,StockData stockData,String date){
        interatorRunningByOpening(account,stockData,date);
        interatorRunningBySession(account,stockData,date);
        interatorRunningByClose(account,stockData,date);
    }
    /**
     * 迭代器
     * @param stockData 原始数据源
     * @param date 对应使用的时间戳
     */
    public void interatorRunning(UserStockAccount account,ArrayList<StockData> stockData,String date) {
        ////////////////////////////////////////
    }

    /**
     * 开盘执行方法
     * @param stockData
     * @param date
     */
    public void interatorRunningByOpening(UserStockAccount account,StockData stockData,String date){
        if(this.modelTrain !=null){
            this.modelTrain.interatorRunningByOpening(account,stockData,date);
        }
        for(StrategyInter inter:openingQuotation){
            inter.runStrategy(account,stockData,date);
        }
    }

    /**
     * 盘中执行方法
     * @param stockData
     * @param date
     */
    public void interatorRunningBySession(UserStockAccount account,StockData stockData,String date){
        if(this.modelTrain !=null){
            this.modelTrain.interatorRunningBySession(account,stockData,date);
        }
        for(StrategyInter inter:sessionQuotation){
            inter.runStrategy(account,stockData,date);
        }
    }

    /**
     * 盘末执行方法
     * @param stockData
     * @param date
     */
    public void interatorRunningByClose(UserStockAccount account,StockData stockData,String date){
        if(this.modelTrain !=null){
            this.modelTrain.interatorRunningByClose(account,stockData, date);
        }
        for(StrategyInter inter:closeQuotation){
            inter.runStrategy(account,stockData,date);
        }
    }
}
