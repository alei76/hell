package ps.landerbuluse.stock.data.server.sina;

import ps.landerbuluse.stock.config.StockConfig;
import ps.landerbuluse.stock.config.StocksConfig;
import ps.landerbuluse.stock.data.StockBean;
import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.data.server.StockDataServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/23.
 */
public class StockDataSinaApi implements StockDataServer {


    public String url ="";

    public String stockId= "sh600000";

    public String start = "20150101";

    public String end = "20150809";

    public void test(){
        StockData obj  = getData(null,this.stockId,this.start,this.end);
//        int len = obj.size();
//        for(int i=0;i<len;i++){
//            System.out.println(obj.get(i));
//        }
    }


    @Override
    public ArrayList<StockData> getData(StocksConfig config) {
        ArrayList<StockData> datas=new ArrayList<StockData>();
        for(StockConfig con :config.getStocksConfig()) {
            datas.add(getData(con.getStockName(), con.getStockId(), con.getStart(), con.getEnd()));
        }
        return datas;
    }

    @Override
    public void parse(StockData stockData, String line) {
        StockBean bean =new StockBean();
        String[] splits = line.split(",");
        bean.setDate(splits[0]);
        bean.setOpenPrice(Float.parseFloat(splits[1]));
        bean.setHightPrice(Float.parseFloat(splits[2]));
        bean.setClosePrice(Float.parseFloat(splits[3]));
        bean.setLowerPrice(Float.parseFloat(splits[4]));
        bean.setUrnover(Long.parseLong(splits[5]));
        stockData.addStockBeanByDay(bean);
    }

    public StockData getData(String stockName,String stockId, String start, String end) {
        StringBuffer sb = new StringBuffer("http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?&rand=random(10000)&symbol=");
        sb.append(stockId);
        sb.append("&end_date=");
        sb.append(end);
        sb.append("&begin_date=");
        sb.append(start);
        sb.append("&type=plain");
        URL ur = null;
        StockData stock =new StockData(stockName,stockId);
        try {
            //搜狐股票行情历史接口
//          ur = new URL("http://q.stock.sohu.com/hisHq?code=cn_300228&start=20130930&end=20131231&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp");
            //新浪股票行情历史接口
//            System.out.println("url:"+sb.toString());
            ur = new URL(sb.toString());
            HttpURLConnection uc = (HttpURLConnection) ur.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ur.openStream(),"GBK"));
            String line;
            while((line = reader.readLine()) != null){
//                System.out.println(line);
                parse(stock,line);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stock;
    }


    public static void main(String[] args){
        StockDataServer server =new StockDataSinaApi();
        server.test();
    }
}
