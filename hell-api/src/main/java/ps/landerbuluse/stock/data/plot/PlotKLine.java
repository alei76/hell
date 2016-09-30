package ps.landerbuluse.stock.data.plot;

import ps.landerbuluse.stock.data.StockData;
import ps.landerbuluse.stock.data.server.StockDataServer;
import ps.landerbuluse.stock.data.server.sina.StockDataSinaApi;

/**
 * Created by Administrator on 2016/9/30.
 */
public class PlotKLine {
    KLineCombineChart kLineCombineChart = new KLineCombineChart();
    StockData stockData = null;
    public PlotKLine(StockData stockData){
        this.stockData = stockData;
    }

    public void plot(){
        kLineCombineChart.addData(this.stockData);
        kLineCombineChart.plot();
    }


    public static void main(String[] args) {
        StockDataServer server =new  StockDataSinaApi();
        PlotKLine plot = new PlotKLine(server.getData("test", "sh600000", "20160101", "20160901"));
        plot.plot();
    }

}
