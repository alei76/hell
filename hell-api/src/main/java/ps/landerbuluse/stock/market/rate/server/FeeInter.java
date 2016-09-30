package ps.landerbuluse.stock.market.rate.server;

/**
 * Created by Administrator on 2016/9/30.
 */
public interface FeeInter {
    /**
     * 获取金额
     * @param price
     * @return
     */
    public float getFee(float price);
}
