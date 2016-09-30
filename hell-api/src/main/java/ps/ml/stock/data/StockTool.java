package ps.ml.stock.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class StockTool {

    /**
     * 获取所有的股票代码
     */
    public List<StockBean> stocks = new ArrayList<StockBean>();


    public List<StockBean> getUsedStocks(){
    
        try {
            URL url = new URL("http://218.244.146.57/static/all.csv");
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),
                "GBK"));
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line.split(",")[0]);
                
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
//        FileUtil2 =
        return null;
    }
    
    public static void main(String[] args){
        StockTool stockTool = new StockTool();
        stockTool.getUsedStocks();
    }
}
