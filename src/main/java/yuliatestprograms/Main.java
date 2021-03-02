package yuliatestprograms;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


public class Main
{
    public static void main( String[] args )
    {

               OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://dev-api.shrimpy.io/v1/exchanges/coinbasepro/candles?quoteTradingSymbol=BTC&baseTradingSymbol=XLM&interval=1H&startDate=2021-02-20T01:00:00.000Z")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
