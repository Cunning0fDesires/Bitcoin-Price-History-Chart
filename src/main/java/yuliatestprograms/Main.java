package yuliatestprograms;

import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jfree.chart.ui.UIUtils;
import java.io.IOException;
import java.time.LocalDateTime;


public class Main
{
    public static void main( String[] args ) {
        //Fetch 7 days of BTC/USD price history from Shrimpy.io

        //Set up the HTTP request...

        OkHttpClient client = new OkHttpClient();
        String URL = String.format(
                "https://dev-api.shrimpy.io/v1/exchanges/binance/candles?baseTradingSymbol=BTC&quoteTradingSymbol=USDC&interval=1h&startTime=%s",
                LocalDateTime.now().minusDays(7).toString()); //get the past week depending on a user's current day
        Request request = new Request.Builder()
                .url(URL)
                .build();

        //Send the request (with error-handling)
        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string(); //the JSON text response
            ResponseParser parser = new ResponseParser(json); //pass to the responseParser to filter and edit the response
            TimeSeriesChart chart = new TimeSeriesChart(parser); //pass the correct data to the chart
            chart.pack();
        UIUtils.centerFrameOnScreen(chart);
        chart.setVisible(true);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
