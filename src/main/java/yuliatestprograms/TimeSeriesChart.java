package yuliatestprograms;

import java.util.stream.IntStream;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class TimeSeriesChart extends ApplicationFrame{

    final TimeSeries dataSeries = new TimeSeries( "Price Fluctuation Curve" );

    public TimeSeriesChart(ResponseParser parser) {
        //Initialise the time series chart
        super("Past Week's Bitcoin data");
        JFreeChart bitcoinChart = ChartFactory.createTimeSeriesChart("Past Week's Bitcoin Price History",
                "Time", "Price ($USD)", createDataset(parser), false, false, false);
        ChartPanel chartPanel = new ChartPanel(bitcoinChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000 ,367));
        final XYPlot plot = bitcoinChart.getXYPlot();

        //get the Y axis to switch off its flags in order to not start from zero
        NumberAxis yAxis = (NumberAxis)plot.getRangeAxis();
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setAutoRangeStickyZero(false);

        //Render all data
        plot.setRenderer(new StandardXYItemRenderer());
        addRegressionLine(parser, plot);
        setContentPane(chartPanel);
    }

    private TimeSeriesCollection createDataset(ResponseParser parser) { //give the edited data to the chart

        IntStream.range(0, parser.getClosePrices().size()) //determine the range for a loop
                .forEach(i -> dataSeries.add( //iterate over each sorted list
                        parser.getDates().get(i), //and add the values to the series
                        parser.getClosePrices().get(i)
                ));
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(dataSeries); //pass the candles with the data
        return dataset;
    }

    private void addRegressionLine (ResponseParser parser, XYPlot plot) {
        double[] coefficients = LinearRegression.linReg(parser.getDatesAsInts(), parser.getClosePrices());
        double slope = coefficients[0];
        double height = coefficients[1];

        double x1 = dataSeries.getTimePeriod(0).getFirstMillisecond();
        double x2 = dataSeries.getNextTimePeriod().getLastMillisecond();
        double y2 = (slope * parser.getClosePrices().size()) + height;
        plot.addAnnotation(new XYLineAnnotation(x1, height, x2, y2));
    }
}
