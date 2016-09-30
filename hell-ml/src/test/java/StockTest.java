

import ps.hell.util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/29.
 */
public class StockTest {

    HashMap<String, ArrayList<StockDesc>> map = new HashMap<>();

    public class StockDesc {

        public String date = null;

        public String name = null;

        public Boolean isBuy = null;

        public float price = 0f;

        public StockDesc(String data) {
            try {
                String[] split = data.split("\t");
//                System.out.println(Arrays.toString(split));
                this.date = split[0];
                this.name = split[3];
//                System.out.println(split[4].equals("证券买入"));
                if (split[4].equals("证券买入")) {
                    this.isBuy = true;
                } else if (split[4].equals("证券卖出")) {
                    this.isBuy = false;
                }
                if (this.isBuy == null) {
                    this.name = null;
                }
                this.price = Float.parseFloat(split[7]);
            } catch (Exception e) {
                this.name = null;
            }
        }
    }

    public void init(String file) {
        read(file);
    }

    /**
     * 获取每月的统计
     */
    public void getStatusDate( ) {
        HashMap<String, ArrayList<StockDesc>> mapDate = new HashMap<>();
        for (Map.Entry<String, ArrayList<StockDesc>> m : this.map.entrySet()) {
            for (StockDesc stock : m.getValue()) {
                String date = stock.date.substring(0,6);
                ArrayList<StockDesc> stocks = mapDate.get(date);
                if (stocks == null) {
                    stocks = new ArrayList<StockDesc>();
                    mapDate.put(date, stocks);
                }
                stocks.add(stock);
            }
        }
        getStockStasticDate(mapDate);
    }

    /**
     * 获取每只个股的统计
     */
    public void getStockStasticDate(HashMap<String, ArrayList<StockDesc>> map) {
        for (Map.Entry<String, ArrayList<StockDesc>> m : map.entrySet()) {
            Status status = getFill(m.getValue());
            printStaticDate(m.getKey(), m.getValue(), status);
        }
    }

    public void printStaticDate(String name, ArrayList<StockDesc> stocks, Status status) {
        System.out.println("月份:" + name);
        System.out.println("总操作次数:" + stocks.size());

        System.out.println("盈亏金额:" + status.getFill());
        System.out.println("收益率:" + status.getFillRate() + '%');
    }


    /**
     * 获取每只个股的统计
     */
    public void getStockStastic() {
        float allFill = 0;
        for (Map.Entry<String, ArrayList<StockDesc>> m : this.map.entrySet()) {
            Status status = getFill(m.getValue());
            allFill += status.getFill();
            printStatic(m.getKey(), m.getValue(), status);
        }
        allFill += 84348;
        allFill -= 1900;
        System.out.println("总利润:" + allFill);
    }

    public void printStatic(String name, ArrayList<StockDesc> stocks, Status status) {
        System.out.println("股票:" + name);
        System.out.println("总操作次数:" + stocks.size());

        System.out.println("盈亏金额:" + status.getFill());
        System.out.println("收益率:" + status.getFillRate() + '%');
    }

    public class Status {
        /**
         * 总购买金额
         */
        public float allBuyPrice = 0f;
        /**
         * 卖
         */
        public float allMPrice = 0f;

        /**
         * 利润率
         *
         * @return
         */
        public float getFillRate() {
            return (allMPrice - allBuyPrice) / allBuyPrice;
        }

        /**
         * 利润
         *
         * @return
         */
        public float getFill() {
            return (allMPrice - allBuyPrice);
        }
    }

    /**
     * 获取盈亏金额
     *
     * @param stocks
     * @return
     */
    public Status getFill(ArrayList<StockDesc> stocks) {
        Status status = new Status();
        for (StockDesc stock : stocks) {
            if (stock.isBuy) {
                status.allBuyPrice += stock.price;
            } else {
                status.allMPrice += stock.price;
            }
        }
        return status;
    }

    public void read(String file) {
        FileUtil fileUtil = new FileUtil(file, "utf-8", false, null);
        ArrayList<String> list = fileUtil.readAndClose();
        for (String aList : list) {
//            System.out.println(aList);
            StockDesc stock = new StockDesc(aList);
            addStock(stock);
        }
    }

    public void addStock(StockDesc stock) {
        if (stock.name == null) {
            return;
        }
        ArrayList<StockDesc> stocks = map.get(stock.name);
        if (stocks == null) {
            stocks = new ArrayList<StockDesc>();
            map.put(stock.name, stocks);
        }
        stocks.add(stock);
    }

    public static void main(String[] args) {
        StockTest test = new StockTest();
        test.init("h:/github/gitbubtest/hell/hell-ml/src/test/java/stock_his.txt");
        test.getStockStastic();
        test.getStatusDate();
    }
}
