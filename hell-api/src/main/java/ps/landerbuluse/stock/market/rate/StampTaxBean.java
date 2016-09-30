package ps.landerbuluse.stock.market.rate;

import ps.landerbuluse.stock.market.rate.server.FeeInter;

/**
 * Created by Administrator on 2016/9/30.
 * 印花税
 */
public class StampTaxBean implements FeeInter {

    /**
     * 比率
     */
    public float rate = 0.001f;
    @Override
    public float getFee(float price) {
        return price*rate;
    }
}
