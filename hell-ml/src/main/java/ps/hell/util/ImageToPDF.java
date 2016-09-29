package ps.hell.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import be.abeel.graphics.GraphicsFileExport;
import be.abeel.graphics.wrapper.JFreeChartWrapper;

public class ImageToPDF {
	public static void main(String[] args) {
		XYSeries ser = new XYSeries("Sample");

		ser.add(0.0D, 1.0D);
		ser.add(1.0D, 5.0D);
		ser.add(2.0D, 3.0D);
		ser.add(3.0D, 0.0D);
		XYSeriesCollection col = new XYSeriesCollection();
		col.addSeries(ser);
		JFreeChart chart = ChartFactory.createXYLineChart("Sample chart",
				"x-axis", "y-axis", col, PlotOrientation.VERTICAL, true, false,
				false);
		GraphicsFileExport.exportPDF(new JFreeChartWrapper(chart), "export",
				640, 480);
	}
}
