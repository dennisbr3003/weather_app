package com.dennis_brink.android.myweatherapp;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dennis_brink.android.myweatherapp.model_day.Day;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForecastHourAdapter extends RecyclerView.Adapter<ForecastHourAdapter.ViewHolder> {

    private List<com.dennis_brink.android.myweatherapp.model_forecast.List> data;

    DateFormatSymbols symbols = new DateFormatSymbols(new Locale("en"));
    String dayNames[] = symbols.getWeekdays();
    String dayShortNames[] = symbols.getShortWeekdays();

    public ForecastHourAdapter(List<com.dennis_brink.android.myweatherapp.model_forecast.List> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hour, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int intdate;
        Calendar calendar = Calendar.getInstance();
        String sdate="";
        String stime="";
        int weekday=0;
        String today = String.format("%02d-%02d-%04d",
                       calendar.get(Calendar.DAY_OF_MONTH),
                       calendar.get(Calendar.MONTH) + 1,
                       calendar.get(Calendar.YEAR));

        calendar.add(Calendar.DATE, 1);

        String tomorrow = String.format("%02d-%02d-%04d",
                          calendar.get(Calendar.DAY_OF_MONTH),
                          calendar.get(Calendar.MONTH) + 1,
                          calendar.get(Calendar.YEAR));;

        intdate = data.get(position).getDt();
        calendar.setTimeInMillis(intdate * 1000L);
        sdate = String.format("%02d-%02d-%04d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));
        stime = String.format("%02d:%02d",
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
        weekday=calendar.get(Calendar.DAY_OF_WEEK);

        Log.d("DENNIS_B", "date " + sdate + " weekday " + weekday);

        if(sdate.equals(today)){
            if(position == 0){
                holder.textViewHourDate.setText("Now");
            } else {
                holder.textViewHourDate.setText("Today");
            }
        }
        else {
            if (sdate.equals(tomorrow)) {
                holder.textViewHourDate.setText("Tomorrow");
            } else{
                holder.textViewHourDate.setText(dayShortNames[weekday]);
            }
        }
        holder.textViewHour.setText(stime);
        holder.textViewConditionHour.setText(data.get(position).getWeather().get(0).getDescription());
        holder.textViewTempHour.setText(formatToOneDecimals(data.get(position).getMain().getTemp()) + "°");
        holder.textViewWindHour.setText("(" + data.get(position).getWind().getDeg() +  "°)");
        holder.textViewWindForceHour.setText(""+formatToOneDecimals(data.get(position).getWind().getSpeed()));
        String icon = data.get(position).getWeather().get(0).getIcon();
        String finalIcon = icon;

        Picasso.get().load("https://openweathermap.org/img/wn/" + icon + "@2x.png")
                .into(holder.imageViewIconHour, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal() weather icon loaded: " + "https://openweathermap.org/img/wn/" + finalIcon + "@2x.png");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal(): error loading weather icon: " + e.getLocalizedMessage());
                        holder.imageViewIconHour.setImageResource(R.mipmap.image870);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewHourDate, textViewTempHour, textViewHour, textViewConditionHour, textViewWindHour, textViewWindForceHour;
        ImageView imageViewIconHour;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHourDate = itemView.findViewById(R.id.textViewHourDate);
            textViewTempHour = itemView.findViewById(R.id.textViewTempHour);
            textViewHour = itemView.findViewById(R.id.textViewHour);
            textViewConditionHour = itemView.findViewById(R.id.textViewConditionHour);
            textViewWindHour = itemView.findViewById(R.id.textViewWindHour);
            imageViewIconHour = itemView.findViewById(R.id.imageViewIconHour);
            textViewWindForceHour = itemView.findViewById(R.id.textViewWindForceHour);
        }
    }

    private double formatToOneDecimals(double x){

        String sx = String.format("%.1f", x);
        sx = sx.replaceAll(",", ".");
        double d = Double.parseDouble(sx);
        return d;
    }

}
