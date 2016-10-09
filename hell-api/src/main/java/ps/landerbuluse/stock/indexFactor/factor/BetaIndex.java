package ps.landerbuluse.stock.indexFactor.factor;

import ps.landerbuluse.stock.data.StockBean;
import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.indexFactor.IndexFactor;
import ps.hell.math.base.MathBase;
import ps.landerbuluse.stock.market.rate.MarketInfo;
import ps.landerbuluse.stock.user.account.AccountLogger;
import ps.landerbuluse.stock.user.account.UserStockAccount;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/9.
 * 相对考核指标
 * 贝塔系数就是考核基金净值和大盘指数的关系。贝塔系数大于1，
 * 就说明这个基金的净值跑赢指数，贝塔系数小于1，就说明基金跑不赢指数
 * (协方差)/(市场方差)
 */
public class BetaIndex implements IndexFactor{

    /**
     * 市场数据
     */
    public StockData marketData = null;
    /**
     * 使用的基金数据
     */
    public UserStockAccount userAccount = null;

    public Float betaVal =null;

    /**
     *
     * @param info 市场数据
     * @param userAccount 用户账户信息
     */
    public BetaIndex(MarketInfo info,UserStockAccount userAccount){
        this.marketData = info.getMarketData();
        this.userAccount = userAccount;
    }

    @Override
    public float getIndex() {
        if(betaVal ==null) {
            ArrayList<Float> val = new ArrayList<Float>();
            ArrayList<Float> val2 = new ArrayList<Float>();
            ArrayList<AccountLogger> data = userAccount.getAccountLogger();
            for (AccountLogger logg : data) {
                StockBean ff = this.marketData.getDataByDate(logg.getDate());
                if (ff == null) {
                    continue;
                }
                val.add(ff.getClosePrice());
                val2.add(logg.getAccountAll());
            }
            float[] marketClosePrice = new float[val.size()];
            float[] closePrice = new float[val2.size()];
            for (int i = 0; i < val.size(); i++) {
                marketClosePrice[i] = val.get(i);
                closePrice[i] = val2.get(i);
            }
            betaVal = getCov(marketClosePrice, closePrice) / getVar(marketClosePrice);
        }
        return betaVal;
    }

    @Override
    public String getName() {
        return "beta系数";
    }

    /**
     * 获取协方差
     * @return
     */
    public float getCov(float[] val1,float[] val2){
        return (float)MathBase.cov(val1,val2);
    }

    /**
     * 获取市场标准差
     * @return
     */
    public float getVar(float[] val){
        return (float)MathBase.var(val);
    }
}
