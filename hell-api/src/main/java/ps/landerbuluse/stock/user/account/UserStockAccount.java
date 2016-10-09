package ps.landerbuluse.stock.user.account;

import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.data.plot.KLineCombineChart;
import ps.landerbuluse.stock.indexFactor.IndexFactor;
import ps.landerbuluse.stock.market.FeeFactory;
import ps.landerbuluse.stock.market.rate.server.FeeInter;
import ps.landerbuluse.stock.user.account.stock.StockAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/30.
 * 用户股票账户
 */
public class UserStockAccount {

//    Logger logger =new Logger.getLogger(UserStockAccount.class);

    /**
     * 所有股票的持有信息
     */
    private HashMap<String,StockAccount> stocksAccount = new HashMap<String,StockAccount>();

    /**
     * 本金
     */
    private float sourceAccount =0f;

    /**
     * 账户历史值
     */
    private ArrayList<AccountLogger> accountLogger = new ArrayList<AccountLogger>();

    /**
     * 滤重
     */
    private HashSet<String> accountSet =new HashSet<String>();

    /**
     * 账户余额
     */
    private float  accountBalanceMoney =0f;

    /**
     * 股票账户总金额
     */
    private float stockBalanceMoney = 0f;

    /**
     * 费率计算
     */
    private FeeFactory fee =new FeeFactory();

    public UserStockAccount(){

    }

    public float getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(float sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public ArrayList<AccountLogger> getAccountLogger() {
        return accountLogger;
    }

    public void setAccountLogger(ArrayList<AccountLogger> accountLogger) {
        this.accountLogger = accountLogger;
    }

    /**
     * 计算股票账户
     */
    public void computestockBalanceMoney(String date){
        for(Map.Entry<String,StockAccount> m:stocksAccount.entrySet()){
            StockAccount account =  m.getValue();
            this.stockBalanceMoney = account.num*account.currentPrice;
        }
        if(date == null || this.accountSet.contains(date)){
            return;
        }
        this.accountSet.add(date);
        AccountLogger lo =new AccountLogger();
        lo.setDate(date);
        lo.setAccountAll(this.accountBalanceMoney+this.stockBalanceMoney);
        this.accountLogger.add(lo);

    }


    /**
     * 添加费率接口
     * @param fee
     */
    public void addFee(FeeInter fee){
        this.fee.addFee(fee);
    }

    /**
     * 充值金额
     * @param price
     */
    public void recharge(float price){
        this.accountBalanceMoney += price;
        this.sourceAccount += price;
    }

    /**
     * 购买股票
     * @param stockId
     * @param changeCount
     * @param currentStockPrice
     */
    public void buy(String stockId,int changeCount,float currentStockPrice){
        if(changeCount<=0){
            return;
        }
        if(changeCount >getCanBuyMaxCount(currentStockPrice)){
            System.out.println("超过可购买的最大数量:请使用getCanBuyMaxCount去获取可购买最大数量:");
            return;
        }
        StockAccount stockOne =stocksAccount.get(stockId);
        if(stockOne ==null){
            stockOne = new StockAccount();
            stocksAccount.put(stockId,stockOne);
        }
        stockOne.num += changeCount;
        stockOne.currentPrice = currentStockPrice;
        float price = currentStockPrice*changeCount;
        this.accountBalanceMoney -= price + this.fee.getFee(price);
//        System.out.println("买入数量:"+changeCount);
//        System.out.println("账户余额:"+accountBalanceMoney);

    }

    /**
     * 卖出
     * @param stockId
     * @param changeCount
     * @param currentStockPrice
     */
    public void sale(String stockId,int changeCount,float currentStockPrice){
        if(changeCount <=0){
            return;
        }
        StockAccount stockOne =stocksAccount.get(stockId);
        if(stockOne.num<changeCount){
            System.out.println("超过最大可卖出数量:");
            return;
        }
        if(stockOne ==null){
            return;
        }
        stockOne.num -= changeCount;
        if(stockOne.num <=0){
            stocksAccount.remove(stockId);
        }
        stockOne.currentPrice = currentStockPrice;
        this.accountBalanceMoney += currentStockPrice*changeCount;
//        System.out.println("卖出数量:"+changeCount);
//        System.out.println("账户余额:"+accountBalanceMoney);
    }

    /**
     * 获取可以购买的最大数量
     * @param currentStockPrice
     * @return
     */
    public int getCanBuyMaxCount(float currentStockPrice){
        int index =0;
        int usedCount =0;
        while(true) {
            int max = (int) (this.accountBalanceMoney / currentStockPrice) - index;
            if(max <=0){
                usedCount =0;
                break;
            }
            float by = max * currentStockPrice;
            float feed = this.fee.getFee(by);
            if (feed + by > this.accountBalanceMoney) {
                index += 1;
                continue;
            }
            usedCount = max;
            break;
        }
        return usedCount;
    }

    /**
     * 可以获取的最大卖出数量
     * @param stockId 股票id
     * @return
     */
    public int getCanSaleMaxCount(String stockId){
        StockAccount acc =stocksAccount.get(stockId);
        if(acc ==null){
            return 0;
        }
        return acc.num;
    }

    /**
     * 获取账户真实收益率
     * @return
     */
    public float getRealRate(){
        return (float)((this.accountBalanceMoney+this.stockBalanceMoney)/this.sourceAccount);
    }

    public void printAccountStatus(ArrayList<IndexFactor> IndexFactor){
        computestockBalanceMoney(null);
        System.out.println("成本:" + this.sourceAccount);
        System.out.println("账户余额:" + this.accountBalanceMoney);
        System.out.println("股票账户:" + this.stockBalanceMoney);
        System.out.println("收益率:"+((this.accountBalanceMoney+this.stockBalanceMoney)*100/this.sourceAccount-100)+"%");
        for(IndexFactor index:IndexFactor) {
            System.out.println(index.getName()+":"+index.getIndex());
        }
    }
}
