package ps.landerbuluse.stock.user;

import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.indexFactor.IndexFactor;
import ps.landerbuluse.stock.indexFactor.factor.AlphaIndex;
import ps.landerbuluse.stock.indexFactor.factor.BetaIndex;
import ps.landerbuluse.stock.indexFactor.factor.SharpIndex;
import ps.landerbuluse.stock.market.rate.MarketInfo;
import ps.landerbuluse.stock.market.rate.server.FeeInter;
import ps.landerbuluse.stock.model.server.ModelTrainServer;
import ps.landerbuluse.stock.strategy.StrategyFactory;
import ps.landerbuluse.stock.strategy.inter.StrategyInter;
import ps.landerbuluse.stock.user.account.UserStockAccount;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/30.
 * 用户主体类
 */
public class UserBean  {

    /**
     * 用户账户
     */
    public UserStockAccount account = new UserStockAccount();
    /**
     * 策略
     */
    public StrategyFactory strategy = new StrategyFactory();
    /**
     * 指标因素
     */
    public ArrayList<IndexFactor> indexFactors = new ArrayList<IndexFactor>();

    public MarketInfo marketInfo =null;

    public UserBean(){
    }

    public void init(){
        BetaIndex beta =new BetaIndex(marketInfo,this.account);
        indexFactors.add(beta);
        indexFactors.add(new AlphaIndex(this.account,marketInfo,beta));
        indexFactors.add(new SharpIndex(marketInfo, this.account));
    }



    /**
     * 重算balanceMoney
     */
    public void computestockBalanceMoney(String date){
        this.account.computestockBalanceMoney(date);
    }

    public void setModelTrain(ModelTrainServer modelTrain){
        strategy.setModelTrain(modelTrain);
    }

    /**
     * 添加开盘策略
     * @param strate
     */
    public void addopeningQuotation(StrategyInter strate){
        this.strategy.addopeningQuotation(strate);
    }

    /**
     * 添加盘中策略
     * @param strate
     */
    public void addsessionQuotation(StrategyInter strate){
        this.strategy.addsessionQuotation(strate);
    }

    /**
     * 添加关盘策略
     * @param strate
     */
    public void addcloseQuotation(StrategyInter strate){
        this.strategy.addcloseQuotation(strate);
    }
    /**
     * 充值金额
     * @param price
     */
    public void recharge(float price){
        this.account.recharge(price);
    }

    /**
     * 初始化程序
     */
    public void init(ArrayList<StockData> stocks){
        strategy.init(stocks);
    }

    /**
     * 添加费率接口
     * @param fee
     */
    public void addFee(FeeInter fee){
        this.account.addFee(fee);
    }


    /**
     * 迭代器
     * @param stockData 原始数据源
     * @param date 使用的索引值
     */
    public void interatorRunning(StockData stockData,String date){
        strategy.interatorRunning(this.account, stockData, date);
    }
    /**
     * 迭代器
     * @param stockData 原始数据源
     * @param date 对应使用的时间戳
     */
    public void interatorRunning(ArrayList<StockData> stockData,String date) {
        ////////////////////////////////////////
        strategy.interatorRunning(this.account,stockData, date);
    }

    public UserStockAccount getAccount() {
        return account;
    }

    public void setAccount(UserStockAccount account) {
        this.account = account;
    }

    public StrategyFactory getStrategy() {
        return strategy;
    }

    public void setStrategy(StrategyFactory strategy) {
        this.strategy = strategy;
    }

    public ArrayList<IndexFactor> getIndexFactors() {
        return indexFactors;
    }

    public void setIndexFactors(ArrayList<IndexFactor> indexFactors) {
        this.indexFactors = indexFactors;
    }

    public MarketInfo getMarketInfo() {
        return marketInfo;
    }

    public void setMarketInfo(MarketInfo marketInfo) {
        this.marketInfo = marketInfo;
    }

    /**
     * 打印账户信息
     */
    public void printAccountStatus(){
        this.account.printAccountStatus(indexFactors);
    }



}
