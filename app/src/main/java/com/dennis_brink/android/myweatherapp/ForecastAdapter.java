package com.dennis_brink.android.myweatherapp;

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

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<Day> data;

    public ForecastAdapter(List<Day> days) {
        this.data = days;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textViewDate.setText(data.get(position).getDate());
        holder.textViewDayDisplay.setText(data.get(position).getDay_display());
        holder.textViewTemp.setText(data.get(position).getTemp() + " °C");

        String icon = data.get(position).getIcon();
        icon = icon.replaceAll("n", "d");

        String finalIcon = icon;
        
        Picasso.get().load("https://openweathermap.org/img/wn/" + icon + "@2x.png")
                .into(holder.imageViewIcon, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal() weather icon loaded: " + "https://openweathermap.org/img/wn/" + finalIcon + "@2x.png");
                        //if(type.equals("local") || type.equals("city")) {
                        //broadcastProgressBarAlert(context);
                        //}
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal(): error loading weather icon: " + e.getLocalizedMessage());
                        holder.imageViewIcon.setImageResource(R.mipmap.image870);
                        //if(type.equals("local") || type.equals("city")) {
                        //broadcastProgressBarAlert(context);
                        //}
                    }
                });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewDate, textViewTemp, textViewDayDisplay;
        ImageView imageViewIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTemp = itemView.findViewById(R.id.textViewTemp);
            textViewDayDisplay = itemView.findViewById(R.id.textViewDayDisplay);
            imageViewIcon = itemView.findViewById(R.id.imageViewIconDay);
        }
    }

}
