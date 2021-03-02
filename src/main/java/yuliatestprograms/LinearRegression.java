package yuliatestprograms;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/* This class contains a linear regression method to be called statically;
 to pass slope and y-intercept values into an X/Y chart object for plotting a regression line */

public class LinearRegression {

    public static double [] linReg (List<Double> dates, List<Double> prices) {
        List<Double> squaredX = dates.stream(). //compute the squares of the dates
                map(x -> x * x).
                collect(Collectors.toList());
        List<Double> xy = new LinkedList(); //and the product of each date and time
        for (int i = 0; i < dates.size(); i++) {
            xy.add(i, dates.get(i) * prices.get(i));
        }
        double sumX = dates.stream() //sum the dates, prices, squared dates, and date * time products respectively
                .reduce(0.0, (a, b) -> a + b);
        double sumY = prices.stream()
                .reduce(0.0, (a, b) -> a + b);
        double sumX2 = squaredX.stream()
                .reduce(0.0, (a, b) -> a + b);
        double sumXY = xy.stream()
                .reduce(0.0, (a, b) -> a + b);
        double n = dates.size(); //the number of x entries
        double slope = (( n * sumXY) - (sumX * sumY)) //the equations to determine the slope and height of the regression line
                / ((n * sumX2) - Math.pow(sumX, 2));
        double height = (sumY - (slope * sumX)) / n;
        return new double[]{slope, height};
    }
}
