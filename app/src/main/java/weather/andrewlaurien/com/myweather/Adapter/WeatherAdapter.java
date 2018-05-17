package weather.andrewlaurien.com.myweather.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import weather.andrewlaurien.com.myweather.Model.DailyWeatherReport;
import weather.andrewlaurien.com.myweather.R;

/**
 * Created by andrewlaurienrsocia on 05/01/2018.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHoler> {


    ArrayList<DailyWeatherReport> mList;
    Context mcontext;

    public WeatherAdapter(Context context, ArrayList<DailyWeatherReport> list) {
        mList = list;
        mcontext = context;
    }

    @Override
    public WeatherHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_weather, parent, false);
        return new WeatherHoler(card);
    }

    @Override
    public void onBindViewHolder(WeatherHoler holder, int position) {
        DailyWeatherReport report = mList.get(position);
        holder.tempHigh.setText("" + report.getMaxTemp() + (char) 0x00B0);
        holder.tempLow.setText("" + report.getMinTemp() + (char) 0x00B0);
        holder.weatherDescription.setText(report.getWeather());
        holder.weatherDay.setText(report.getDateString());
        switch (report.getWeather()) {
            case DailyWeatherReport.WEATHER_TYPE_CLEAR:
                holder.weatherImg.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.sunny_mini));
                break;
            case DailyWeatherReport.WEATHER_TYPE_CLOUDS:
                holder.weatherImg.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.cloudy_mini));
                break;
            case DailyWeatherReport.WEATHER_TYPE_RAIN:
                holder.weatherImg.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.rainy_mini));
                break;
            default:
                holder.weatherImg.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.sunny_mini));
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class WeatherHoler extends RecyclerView.ViewHolder {

        public ImageView weatherImg;
        public TextView weatherDescription;
        public TextView weatherDay;
        public TextView tempHigh;
        public TextView tempLow;

        public WeatherHoler(View itemView) {
            super(itemView);

            weatherImg = (ImageView) itemView.findViewById(R.id.imageView2);
            weatherDescription = (TextView) itemView.findViewById(R.id.textView5);
            weatherDay = (TextView) itemView.findViewById(R.id.textView4);
            tempHigh = (TextView) itemView.findViewById(R.id.textView6);
            tempLow = (TextView) itemView.findViewById(R.id.textView7);
        }
    }

}
