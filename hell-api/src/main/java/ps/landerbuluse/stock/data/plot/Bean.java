package ps.landerbuluse.stock.data.plot;

import java.util.Map;   
  
/**   
 * @description 数据BEAN   
 * @author Zhou-Jingxian   
 */   
public class Bean {   
  
    private String goods_name ;   
    private Map<String,Double> priceindexMap;   
       
    public String getGoods_name() {   
        return goods_name;   
    }   
    public void setGoods_name(String goods_name) {   
        this.goods_name = goods_name;   
    }   
  
    public Map<String, Double> getPriceindexMap() {   
        return priceindexMap;   
    }   
    public void setPriceindexMap(Map<String, Double> priceindexMap) {   
        this.priceindexMap = priceindexMap;   
    }   
       
       
}   