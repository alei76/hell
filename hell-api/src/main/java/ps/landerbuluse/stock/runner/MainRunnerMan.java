package ps.landerbuluse.stock.runner;

import ps.landerbuluse.stock.config.StockConfig;
import ps.landerbuluse.stock.config.StocksConfig;
import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.data.plot.KLineCombineChart;
import ps.landerbuluse.stock.data.server.StockDataServer;
import ps.landerbuluse.stock.data.server.sina.StockDataSinaApi;
import ps.landerbuluse.stock.market.FeeFactory;
import ps.landerbuluse.stock.market.rate.HandlingCharge;
import ps.landerbuluse.stock.market.rate.MarketInfo;
import ps.landerbuluse.stock.market.rate.StampTaxBean;
import ps.landerbuluse.stock.model.server.ModelTrainServer;
import ps.landerbuluse.stock.runner.running.RuningMethod;
import ps.landerbuluse.stock.strategy.StrategyFactory;
import ps.landerbuluse.stock.strategy.inter.StrategyInter;
import ps.landerbuluse.stock.user.UserBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/9/30.
 */
public class MainRunnerMan extends Thread {

    /**
     * 添加配置文件
     */
    private StocksConfig stocksConfig = null;
    /**
     * 数据服务端
     */
    private StockDataServer server = null;
    /**
     * 用户实体
     */
    private UserBean user = null;

    private ArrayList<StockData> stockData =null;
    /**
     *
     * @param stocksConfig 添加配置文件
     */
    public MainRunnerMan(StocksConfig stocksConfig){
        this.stocksConfig = stocksConfig;
    }

    public void initUser(UserBean user){
        if(this.server ==null){
            String start =null;
            String end =null;

            for(StockConfig config:this.stocksConfig.getStocksConfig()){
                start = config.start;
                end = config.end;
            }
            this.server = new StockDataSinaApi(start,end);
        }

        this.user = user;
        user.setMarketInfo(new MarketInfo(this.server));
        user.init();
        init();
    }

    public void setStockDataServer(StockDataServer server){
        this.server = server;
    }
    /**
     * 线程执行函数
     */
    public void run(){
        this.stockData = server.getData(this.stocksConfig);
        user.init(this.stockData);
        //获取正向有序的时间
        HashSet<String> dates =new HashSet<String>();
        for(int i=0;i<this.stockData.size();i++){
            Set<String> val =this.stockData.get(i).getIndex().map.map.keySet();
            for(String v:val){
                dates.add(v);
            }
        }
        ArrayList<String> dateSort =new ArrayList<String>();
        for(String v :dates){
            dateSort.add(v);
        }
        Collections.sort(dateSort);
        for(int i=0;i<dateSort.size();i++){
            for(int j =0;j<this.stockData.size();j++) {
                //需要将索引切换成时间
                //每一个对应的状态
//                System.out.println("执行天"+dateSort.get(i));
                interatorRunning(this.stockData.get(j),dateSort.get(i));
            }
            //整个组合对应的状态
            interatorRunning(this.stockData,dateSort.get(i));
            this.user.computestockBalanceMoney(dateSort.get(i));
        }
    }



    /**
     * 迭代器
     * @param stockData 原始数据源
     * @param date 对应使用的时间戳
     */
    public void interatorRunning(StockData stockData,String date){
        this.user.interatorRunning(stockData, date);
    }

    /**
     * 迭代器
     * @param stockData 原始数据源
     * @param date 对应使用的时间戳
     */
    public void interatorRunning(ArrayList<StockData> stockData,String date) {
        this.user.interatorRunning(stockData, date);
    }


    public void init(){

        //默认是两种费率
        //手续费
        user.addFee(new HandlingCharge());
        //印花税
        user.addFee(new StampTaxBean());
        //用户初始化程序
    }

    public void plot(){
        KLineCombineChart kLineCombineChart = new KLineCombineChart();
        StockData stockData = null;
        kLineCombineChart.addData(this.stockData.get(0));
        kLineCombineChart.addRateData(this.user.account.getSourceAccount(),this.user.account.getAccountLogger());
        kLineCombineChart.plot();
    }


}
