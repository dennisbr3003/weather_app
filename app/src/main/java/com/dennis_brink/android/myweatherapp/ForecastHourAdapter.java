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

import com.squareup.picasso.Picasso;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Log.d("DENNIS_B", "Recycled " + holder.textViewHour.getText().toString());
    }

    @Override
    // holder becomes visible
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        setCompassNeedleRotation(holder, data.get(holder.getAdapterPosition()).getWind().getDeg());
        Log.d("DENNIS_B", "ForecastHourAdapter.onBindViewHolder(): rotate: " + holder.getAdapterPosition() + " by " + data.get(holder.getAdapterPosition()).getWind().getDeg());
    }

    @Override
    // holder is not visible anymore
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d("DENNIS_B", "ForecastHourAdapter.onViewDetachedFromWindow(): rotate: " + holder.getAdapterPosition() + " by " + (data.get(holder.getAdapterPosition()).getWind().getDeg() * -1));
        setCompassNeedleRotation(holder, (data.get(holder.getAdapterPosition()).getWind().getDeg() * -1));
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
        holder.textViewWindHour.setText(AppConfig.getInstance().getWindDirection(data.get(position).getWind().getDeg()));
        holder.textViewWindForceHour.setText(ApplicationLibrary.formatDoubleToStringWithDecimals(data.get(position).getWind().getSpeed(), 0));

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
        ImageView imageViewIconHour, imageViewCompassHour, imageViewCompassNeedleHour;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            textViewHourDate = itemView.findViewById(R.id.textViewHourDate);
            textViewTempHour = itemView.findViewById(R.id.textViewTempHour);
            textViewHour = itemView.findViewById(R.id.textViewHour);
            textViewConditionHour = itemView.findViewById(R.id.textViewConditionHour);
            textViewWindHour = itemView.findViewById(R.id.textViewWindHour);
            imageViewIconHour = itemView.findViewById(R.id.imageViewIconHour);
            textViewWindForceHour = itemView.findViewById(R.id.textViewWindForceHour);
            imageViewCompassHour = itemView.findViewById(R.id.imageViewCompassHour);
            imageViewCompassNeedleHour = itemView.findViewById(R.id.imageViewCompassNeedleHour);

            ApplicationLibrary.setCompassBackGround(imageViewCompassHour);
            ApplicationLibrary.setDrawableBackground(imageViewIconHour);

        }
    }

    private void setCompassNeedleRotation(ViewHolder holder, int degrees){
        holder.imageViewCompassNeedleHour.animate().rotationBy(degrees).setDuration(300l);
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
