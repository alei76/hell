package ps.landerbuluse.stock.market;

import ps.landerbuluse.stock.market.rate.server.FeeInter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/30.
 * 交易手续费
 */
public class FeeFactory{

    /**
     * 费率列表
     */
    ArrayList<FeeInter> fees = new ArrayList<FeeInter>();

    public FeeFactory(){

    }


    /**
     * 添加费用接口
     * @param fee
     */
    public void addFee(FeeInter fee){
        fees.add(fee);
    }

    /**
     * 获取 交易对应的费用
     * @param price
     * @return
     */
    public float getFee(float price){
        float feed = 0f;
        for (FeeInter inter : fees) {
            feed += inter.getFee(price);
        }
        return feed;
    }
}
