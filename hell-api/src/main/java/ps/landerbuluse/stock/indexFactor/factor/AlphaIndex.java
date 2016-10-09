package ps.landerbuluse.stock.indexFactor.factor;

import ps.landerbuluse.stock.indexFactor.IndexFactor;
import ps.landerbuluse.stock.market.rate.MarketInfo;
import ps.landerbuluse.stock.user.account.AccountLogger;
import ps.landerbuluse.stock.user.account.UserStockAccount;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/9.
 * alpah计算公式
 * 绝对绩效考核
 * 阿尔法系数=投资的实际回报率-市场无风险利率-贝塔系数*市场回报
 * 相对于市场无风险利率 该回报是否合适
 * 数值越大越好 大于0更好
 * 实际收益率-市场无风险收益率-beta系数*市场收益率
 */
public class AlphaIndex implements IndexFactor {

    /**
     *用户账户信息
     */
    public UserStockAccount userAccount = null;
    /**
     * 市场无风险利率
     */
    public float marketNotRiskRate = 0.0f;
    /**
     * beta系数
     */
    public BetaIndex beta = null;
    /**
     * 市场率
     */
    public float marketRate = 0.0f;

    /**
     *
     * @param userAccount 账户信息
     * @param info 无风险市场收益率
     * @param beta beta系数值
     */
    public AlphaIndex(UserStockAccount userAccount,MarketInfo info,BetaIndex beta){
        this.userAccount = userAccount;
        this.marketNotRiskRate = info.getMarketnotRiskRate();
        this.beta = beta;
        this.marketRate = info.getMarketRate();
    }

    @Override
    public float getIndex() {
        return userAccount.getRealRate() - this.marketNotRiskRate - this.beta.getIndex()*this.marketRate;
    }

    @Override
    public String getName() {
        return "alpha系数";
    }
}
