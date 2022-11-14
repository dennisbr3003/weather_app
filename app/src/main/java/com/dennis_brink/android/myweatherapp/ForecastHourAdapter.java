package com.dennis_brink.android.myweatherapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ForecastHourAdapter extends RecyclerView.Adapter<ForecastHourAdapter.ViewHolder> {

    private List<com.dennis_brink.android.myweatherapp.model_forecast.List> data;

    DateFormatSymbols symbols = new DateFormatSymbols(new Locale("en"));
    String dayShortNames[] = symbols.getShortWeekdays();
    String today = "";
    String tomorrow = "";

    public ForecastHourAdapter(List<com.dennis_brink.android.myweatherapp.model_forecast.List> data) {

        this.data = data;
        initDates();

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

        String sdate = ApplicationLibrary.getDate(data.get(position).getDt());
        String stime = ApplicationLibrary.getTime(data.get(position).getDt());
        int weekday = ApplicationLibrary.getDayOfWeek(data.get(position).getDt());

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
        holder.textViewTempHour.setText(ApplicationLibrary.formatToDecimals(data.get(position).getMain().getTemp(), 1) + "°");
        holder.textViewWindHour.setText("(" + data.get(position).getWind().getDeg() +  "°)");
        holder.textViewWindForceHour.setText(""+ApplicationLibrary.formatToDecimals(data.get(position).getWind().getSpeed(), 1));

        String fIcon = data.get(position).getWeather().get(0).getIcon();

        if(!AppCache.getInstance().hasElement(fIcon)){
            Picasso.get().load("https://openweathermap.org/img/wn/" + fIcon + "@2x.png")
                    .into(holder.imageViewIconHour, new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {

                            Log.d("DENNIS_B", "ForecastHourAdapter.onBindViewHolder(): weather icon loaded: " + "https://openweathermap.org/img/wn/" + fIcon + "@2x.png");
                            AppCache.getInstance().cacheElement(fIcon, holder.imageViewIconHour.getDrawable());

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("DENNIS_B", "ForecastHourAdapter.onBindViewHolder(): error loading weather icon: " + e.getLocalizedMessage());
                            holder.imageViewIconHour.setImageResource(R.mipmap.image870);
                        }
                    });
        } else { // icon is cached, read that one and spare a call

            holder.imageViewIconHour.setImageBitmap(AppCache.getInstance().loadElement(fIcon));
            Log.d("DENNIS_B", "ForecastHourAdapter.onBindViewHolder(): weather icon loaded from bCache: " + fIcon);

        }
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

    @SuppressLint("DefaultLocale")
    private void initDates(){

        Calendar calendar = Calendar.getInstance();

        today = String.format("%02d-%02d-%04d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));

        calendar.add(Calendar.DATE, 1);

        tomorrow = String.format("%02d-%02d-%04d",
                   calendar.get(Calendar.DAY_OF_MONTH),
                   calendar.get(Calendar.MONTH) + 1,
                   calendar.get(Calendar.YEAR));
    }

}
