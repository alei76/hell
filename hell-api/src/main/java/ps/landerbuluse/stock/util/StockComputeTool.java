package ps.landerbuluse.stock.util;

import ps.landerbuluse.stock.data.StockBean;
import ps.landerbuluse.stock.data.StockData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/9.
 */
public class StockComputeTool {

    /**
     * 获取收益率
     * @param data
     * @return
     */
    public static float getIncomeRate(StockData data){
        ArrayList<StockBean> bean = data.getData();
        if(bean ==null || bean.size()==0){
            return 0;
        }
        float val =bean.get(0).getOpenPrice();
        float val2 = bean.get(bean.size()-1).getClosePrice();
        return (val2-val)/val2;
    }
}
