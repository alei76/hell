package ps.landerbuluse.stock.data.plot;

import java.awt.Color;
import java.awt.Font;   
import java.awt.GradientPaint;   
import java.io.File;   
import java.io.FileNotFoundException;   
import java.io.FileOutputStream;   
import java.io.IOException;   
import java.util.Iterator;   
import java.util.List;   
import java.util.Map;   
import java.util.Set;   
  
import org.jfree.chart.ChartFactory;   
import org.jfree.chart.ChartUtilities;   
import org.jfree.chart.JFreeChart;   
import org.jfree.chart.labels.ItemLabelAnchor;   
import org.jfree.chart.labels.ItemLabelPosition;   
import org.jfree.chart.labels.StandardXYItemLabelGenerator;   
import org.jfree.chart.plot.XYPlot;   
import org.jfree.chart.renderer.xy.XYItemRenderer;   
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;   
import org.jfree.chart.title.TextTitle;   
import org.jfree.data.time.Month;   
import org.jfree.data.time.TimeSeries;   
import org.jfree.data.time.TimeSeriesCollection;   
import org.jfree.ui.RectangleInsets;   
import org.jfree.ui.TextAnchor;   
  
/**   
 * @description 使用JFreeChart组建，生成一个价格随日期的走势图表   
 * @author Zhou-Jingxian   
 */   
public class TimeSeriesChartUtil {    
       
    private String type;//month,year   
    private int width ;//后台计算   
    private int height;//后台计算   
    private String title;//图表的主标题   
    private String subTitle;//图表的子标题   
    private String xName;//可默认值：月份   
    private String yName;//可默认值：价格指数   
       
    /***   
     * constructor function   
     * @param type   
     * @param title   
     * @param subTitle   
     * @param xName   
     * @param yName   
     */   
    public TimeSeriesChartUtil(String type,String title,String subTitle,String xName,String yName){   
        this.type = type;   
        this.title = title;   
        this.subTitle = subTitle;   
        this.xName = xName;   
        this.yName = yName;   
        if("month".equals(type)){   
            this.width = 800;   
            this.height = 600;   
        }else if("year".equals(type)){   
            this.width = 600;   
            this.height = 400;   
        }   
    }   
       
    /** 根据TimeSeriesCollection创建JFreeChart对象*/      
    public JFreeChart createChart(TimeSeriesCollection dataset) {      
           
        JFreeChart chart = ChartFactory.createTimeSeriesChart(this.title, this.xName,this.yName, dataset, true, true, true);   
           
        XYPlot plot = (XYPlot) chart.getPlot();   
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer();   
        //设置网格背景颜色   
        plot.setBackgroundPaint(Color.white);   
        //设置网格竖线颜色   
        plot.setDomainGridlinePaint(Color.pink);   
        //设置网格横线颜色   
        plot.setRangeGridlinePaint(Color.pink);   
        //设置曲线图与xy轴的距离   
        plot.setAxisOffset(new RectangleInsets(0D, 0D, 0D, 10D));   
        //设置曲线是否显示数据点   
        xylineandshaperenderer.setBaseShapesVisible(true);   
        //设置曲线显示各数据点的值   
        XYItemRenderer xyitem = plot.getRenderer();      
        xyitem.setBaseItemLabelsVisible(true);      
        xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));   
        xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());   
        xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 14));   
        plot.setRenderer(xyitem);   
           
        //设置子标题   
        TextTitle subtitle = new TextTitle(this.subTitle, new Font("黑体", Font.BOLD, 12));   
        chart.addSubtitle(subtitle);   
        //设置主标题   
        chart.setTitle(new TextTitle(this.title, new Font("隶书", Font.ITALIC, 15)));   
        //设置背景颜色   
        chart.setBackgroundPaint(new GradientPaint(0, 0, Color.white, 0, 1000,Color.blue));   
        chart.setAntiAlias(true);   
     
        return chart;      
    }      
     
    /**创建TimeSeriesCollection对象  */   
    public TimeSeriesCollection createDataset(List<Bean> datalist){   
           
        //时间曲线数据集合   
        TimeSeriesCollection lineDataset = new TimeSeriesCollection();   
        for(int i=0;i<datalist.size();i++){   
            Bean bean = datalist.get(i);   
            TimeSeries series = new TimeSeries(bean.getGoods_name(),Month.class);   
            Map<String,Double> pimap = bean.getPriceindexMap();    
            Set piset = pimap.entrySet();      
            Iterator piIterator = piset.iterator();      
            while(piIterator.hasNext()){      
                Map.Entry<String,Double> hiddenMapEntry = (Map.Entry<String,Double>)piIterator.next();     
                String key = hiddenMapEntry.getKey();      
                int year = Integer.parseInt(key.substring(0,4));   
                int month = Integer.parseInt(key.substring(4, 6));   
                series.add(new Month(month,year),hiddenMapEntry.getValue());   
            }     
            lineDataset.addSeries(series);   
        }   
        return lineDataset;   
    }   
       
    /**保存为文件*/      
    public void saveAsFile(JFreeChart chart, String outputPath) {      
        FileOutputStream out = null;      
        try {      
            File outFile = new File(outputPath);      
            if (!outFile.getParentFile().exists()) {      
                outFile.getParentFile().mkdirs();      
            }      
            out = new FileOutputStream(outputPath);      
            // 保存为PNG      
            ChartUtilities.writeChartAsPNG(out, chart, width, height);      
            // 保存为JPEG      
            // ChartUtilities.writeChartAsJPEG(out, chart, width, height);      
            out.flush();      
        } catch (FileNotFoundException e) {      
            e.printStackTrace();      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
            if (out != null) {      
                try {      
                    out.close();      
                } catch (IOException e) {      
                    // do nothing      
                }      
            }      
        }      
    }      
     
    public int getWidth() {   
        return width;   
    }   
  
    public void setWidth(int width) {   
        this.width = width;   
    }   
  
    public int getHeight() {   
        return height;   
    }   
  
    public void setHeight(int height) {   
        this.height = height;   
    }      
       
    public String getXName() {   
        return xName;   
    }   
  
    public void setXName(String name) {   
        xName = name;   
    }   
  
    public String getYName() {   
        return yName;   
    }   
  
    public void setYName(String name) {   
        yName = name;   
    }   
  
    public String getType() {   
        return type;   
    }   
  
    public void setType(String type) {   
        this.type = type;   
    }   
  
  
    public String getTitle() {   
        return title;   
    }   
  
    public void setTitle(String title) {   
        this.title = title;   
    }   
  
    public String getSubTitle() {   
        return subTitle;   
    }   
  
    public void setSubTitle(String subTitle) {   
        this.subTitle = subTitle;   
    }   
  
}  