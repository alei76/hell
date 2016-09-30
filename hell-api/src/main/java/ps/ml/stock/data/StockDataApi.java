package ps.ml.stock.data;

import org.apache.commons.codec.EncoderException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class StockDataApi implements StockDataServer {


    public String url ="";

    public String stockId= "sh600000";

    public String start = "20150101";

    public String end = "20150809";

    public void test(){
        List<Object> obj  = getData(this.stockId,this.start,this.end);
        int len = obj.size();
        for(int i=0;i<len;i++){
            System.out.println(obj.get(i));
        }
    }


    public List<Object> getData(String stockId, String start, String end) {
        StringBuffer sb = new StringBuffer("http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?&rand=random(10000)&symbol=");
        sb.append(stockId);
        sb.append("&end_date=");
        sb.append(end);
        sb.append("&begin_date=");
        sb.append(start);
        sb.append("&type=plain");
        URL ur = null;
        List<Object> list = new ArrayList<Object>();
        try {
            //搜狐股票行情历史接口
//          ur = new URL("http://q.stock.sohu.com/hisHq?code=cn_300228&start=20130930&end=20131231&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp");
            //新浪股票行情历史接口
            System.out.println("url:"+sb.toString());
            ur = new URL(sb.toString());
            HttpURLConnection uc = (HttpURLConnection) ur.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ur.openStream(),"GBK"));
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
                list.add(line);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) throws IOException, EncoderException {
        StockDataServer server =new StockDataApi();
        server.test();
    }
}
