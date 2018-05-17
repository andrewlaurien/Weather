package weather.andrewlaurien.com.myweather.Utilities;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weather.andrewlaurien.com.myweather.Adapter.WeatherAdapter;
import weather.andrewlaurien.com.myweather.MainActivity;
import weather.andrewlaurien.com.myweather.Model.DailyWeatherReport;
import weather.andrewlaurien.com.myweather.R;

/**
 * Created by andrewlaurienrsocia on 03/01/2018.
 */

public class WebRequest {

    private static WebRequest webRequest;
    private static RequestQueue requestQueue;
    private static Context mcontext;


    private WebRequest(Context context) {
        mcontext = context;
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mcontext);
        }
    }

    public static synchronized WebRequest getInstance(Context context) {
        if (webRequest == null) {
            webRequest = new WebRequest(context);
        }
        return webRequest;
    }


    public void fetchWeatherData(Location location) {

        String forecastURL = "/?lat=" + location.getLatitude() + "&lon=" + location.getLongitude();
        String url = Constant.baseURL + forecastURL + Constant.units + Constant.apiKey;

        Log.d("URI", url);

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Log.v("KAT", "Rez: " + response.toString());
                try {

                    MainActivity.progressDialog.dismiss();

                    JSONObject city = response.getJSONObject("city");
                    String cityName = city.getString("name");
                    String country = city.getString("country");

                    JSONArray list = response.getJSONArray("list");

                    MainActivity.txtDate.setText(CommonFunc.formattedDateFromString("", "", list.getJSONObject(0).getString("dt_txt")));
                    MainActivity.txtWeather.setText(list.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main"));
                    MainActivity.txtlocation.setText(cityName);
                    MainActivity.txtTemperature.setText("" + list.getJSONObject(0).getJSONObject("main").getDouble("temp") + (char) 0x00B0);

                    Log.d("Weather", list.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main"));

                    switch (list.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main")) {
                        case DailyWeatherReport.WEATHER_TYPE_CLEAR:
                            MainActivity.imgWeather.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.sunny_mini));
                            Log.d("Weather0", list.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main"));
                            break;
                        case DailyWeatherReport.WEATHER_TYPE_CLOUDS:
                            Log.d("Weather1", list.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main"));
                            MainActivity.imgWeather.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.cloudy_mini));
                            break;
                        case DailyWeatherReport.WEATHER_TYPE_RAIN:
                            MainActivity.imgWeather.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.rainy_mini));
                            Log.d("Weather2", list.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main"));
                            break;
                        default:
                            MainActivity.imgWeather.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.sunny_mini));
                            Log.d("Weather3", list.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main"));

                    }

                    MainActivity.weatherReportArrayList.clear();


                    for (int x = 1; x < 6; x++) {

                        JSONObject obj = list.getJSONObject(x);
                        JSONObject main = obj.getJSONObject("main");
                        Double currentTemp = main.getDouble("temp");
                        Double maxTemp = main.getDouble("temp_max");
                        Double minTemp = main.getDouble("temp_min");

                        JSONArray weatherList = obj.getJSONArray("weather");
                        JSONObject weather = weatherList.getJSONObject(0);
                        String weatherType = weather.getString("main");

                        String rawDate = obj.getString("dt_txt");

                        DailyWeatherReport report = new DailyWeatherReport(cityName, currentTemp.intValue(), maxTemp.intValue(), minTemp.intValue(), weatherType, country, rawDate);
                        MainActivity.weatherReportArrayList.add(report);


                        MainActivity.weatherAdapter = new WeatherAdapter(mcontext, MainActivity.weatherReportArrayList);
                        MainActivity.recyclerView.setAdapter(MainActivity.weatherAdapter);

                        MainActivity.recyclerView.invalidate();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        MainActivity.recyclerView.setLayoutManager(layoutManager);


                    }

                } catch (JSONException e) {
                    Log.v("KEY", "ERR: " + e.getLocalizedMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("KAT", "ERR: " + error.toString());
            }
        });

        requestQueue.add(jsonRequest);


    }

}
