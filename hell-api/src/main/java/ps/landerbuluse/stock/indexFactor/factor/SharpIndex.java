package ps.landerbuluse.stock.indexFactor.factor;

import ps.hell.math.base.MathBase;
import ps.landerbuluse.stock.data.StockBean;
import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.indexFactor.IndexFactor;
import ps.landerbuluse.stock.market.rate.MarketInfo;
import ps.landerbuluse.stock.user.account.AccountLogger;
import ps.landerbuluse.stock.user.account.UserStockAccount;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/9.
 * 夏普比率
 * 风险和收益比值
 * 夏普比率是衡量基金每.承担一份风险能获得多少收益的指标
 * 数值越大越好
 * (期望收益率-无风险收益率)/(标准差)
 */
public class SharpIndex implements IndexFactor {

    public float notRiskRate = 0.0f;

    public float[] vals = null;
    public UserStockAccount userAccount=null;

    /**
     *
     * @param info 市场信息
     * @param userAccount 用户账户信息
     */
    public SharpIndex(MarketInfo info,UserStockAccount userAccount){
        this.notRiskRate = info.getMarketnotRiskRate();
        this.userAccount = userAccount;
    }
    @Override
    public float getIndex() {
        ArrayList<AccountLogger> data = this.userAccount.getAccountLogger();
        vals =new float[data.size()];
        for(int i=0;i<data.size();i++){
            vals[i] = data.get(i).getAccountAll();
        }
        float rp = (vals[vals.length-1]-vals[0])/vals[0];
        return (float)((rp-notRiskRate)/ MathBase.sqrt(MathBase.var(vals)));
    }

    @Override
    public String getName() {
        return "夏普系数";
    }
}
