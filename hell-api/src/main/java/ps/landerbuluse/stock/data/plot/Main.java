package ps.landerbuluse.stock.data.plot;

import java.util.ArrayList;
import java.util.HashMap;   
import java.util.List;   
import java.util.Map;   
  
import org.jfree.chart.JFreeChart;   
import org.jfree.data.time.TimeSeriesCollection;   
  
  
/**   
 * @description 构造数据，测试图片生成   
 * @author Zhou-Jingxian   
 */   
public class Main {   
         
    public static void main(String[] args) {   
           
        TimeSeriesChartUtil util = new TimeSeriesChartUtil("year", "重点品牌价格走势图", "2009年8-10月走势图", "时间", "价格指数");   
        List<Bean> datalist = new ArrayList<Bean>();   
           
        Bean bean1 = new Bean();   
        bean1.setGoods_name("中华");   
        Map<String,Double> priceindexMap1 = new HashMap<String,Double>();   
        priceindexMap1.put("200903", 99.86);   
        priceindexMap1.put("200904", 99.8);   
        priceindexMap1.put("200905", 99.97);   
        priceindexMap1.put("200906", 99.96);   
        priceindexMap1.put("200907", 99.86);   
        priceindexMap1.put("200908", 99.8);   
        priceindexMap1.put("200909", 99.97);   
        priceindexMap1.put("200910", 99.96);   
        bean1.setPriceindexMap(priceindexMap1);   
        datalist.add(bean1);   
           
        Bean bean2 = new Bean();   
        bean2.setGoods_name("芙蓉王");   
        Map<String,Double> priceindexMap2 = new HashMap<String,Double>();   
        priceindexMap2.put("200903", 100.12);   
        priceindexMap2.put("200904", 100.2);   
        priceindexMap2.put("200905", 100.0);   
        priceindexMap2.put("200906", 100.08);   
        priceindexMap2.put("200907", 100.12);   
        priceindexMap2.put("200908", 100.2);   
        priceindexMap2.put("200909", 100.0);   
        priceindexMap2.put("200910", 100.08);   
        bean2.setPriceindexMap(priceindexMap2);   
        datalist.add(bean2);   
           
        Bean bean3 = new Bean();   
        bean3.setGoods_name("云烟");   
        Map<String,Double> priceindexMap3 = new HashMap<String,Double>();   
        priceindexMap3.put("200903", 99.77);   
        priceindexMap3.put("200904", 99.7);   
        priceindexMap3.put("200905", 99.83);   
        priceindexMap3.put("200906", 99.93);   
        priceindexMap3.put("200907", 99.77);   
        priceindexMap3.put("200908", 99.7);   
        priceindexMap3.put("200909", 99.83);   
        priceindexMap3.put("200910", 99.93);   
        bean3.setPriceindexMap(priceindexMap3);   
        datalist.add(bean3);   
           
        //步骤1：创建XYDataset对象（准备数据）    
        TimeSeriesCollection dataset = util.createDataset(datalist);   
        //步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置    
        JFreeChart freeChart = util.createChart(dataset);   
           
        //步骤3：将JFreeChart对象输出到文件，Servlet输出流等      
        util.saveAsFile(freeChart, "F:\\jfreechart\\lineXY_"+Math.random()*20+".png");      
    }      
     
} 