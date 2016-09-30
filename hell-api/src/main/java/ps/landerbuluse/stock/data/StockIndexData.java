package ps.landerbuluse.stock.data;

import ps.hell.util.struct.TwoMapPair;

/**
 * Created by Administrator on 2016/9/23.
 */
public class StockIndexData {

    /**
     * key 存储时间
     * value 存储 索引位置
     */
    public TwoMapPair<String,Integer> map =new TwoMapPair<String,Integer>();
    public int index =-1;

    public StockIndexData(){

    }

    /**
     * 获取大小
     * @return
     */
    public int getSize(){
        return index +1;
    }

    /**
     * 添加数据
     * @param stockBean
     */
    public void addStockBean(StockBean stockBean){
        index++;
        map.add(stockBean.date,index);
    }
}
