package ps.landerbuluse.stock.market.rate;

import ps.landerbuluse.stock.market.rate.server.FeeInter;

/**
 * Created by Administrator on 2016/9/30.
 * 交易手续费
 */
public class HandlingCharge implements FeeInter {

    /**
     * 比率
     */
    public float rate = 0.0003f;
    @Override
    public float getFee(float price) {
        return price*rate;
    }
}
