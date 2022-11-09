package com.dennis_brink.android.myweatherapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dennis_brink.android.myweatherapp.model_day.Day;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<Day> data;
    private Context context;

    //static java.util.List<Day> days = new ArrayList<>();
    private ArrayList<Integer>imageList = new ArrayList<>();

    public ForecastAdapter(List<Day> days, Context context) {
        this.data = days;
        this.context = context;
        loadImageList();
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
        holder.textViewTemp.setText(ApplicationLibrary.formatToDecimals(data.get(position).getTemp(), 1) + " °C");
        holder.textViewMin.setText(ApplicationLibrary.formatToDecimals(data.get(position).getMintemp(), 1) + " °");
        holder.textViewMax.setText(ApplicationLibrary.formatToDecimals(data.get(position).getMaxtemp(), 1) + " °");
        holder.textViewConditionDay.setText(data.get(position).getCondition());
        if(data.size() > 5) {
            holder.imageViewCardBackground.setImageResource(imageList.get(position));
        } else {
            holder.imageViewCardBackground.setImageResource(imageList.get(position + 1));
        }
        holder.textViewWindDay.setText(""+ApplicationLibrary.formatToDecimals(data.get(position).getWind(), 1));

        if(!data.get(position).getRain_time().isEmpty()) {
            holder.imageViewRainDay.setVisibility(View.VISIBLE);
            holder.textViewRainDay.setVisibility(View.VISIBLE);
            holder.textViewRainDay.setText(data.get(position).getRain_time());
        } else {
            holder.imageViewRainDay.setVisibility(View.INVISIBLE);
            holder.textViewRainDay.setVisibility(View.INVISIBLE);
        }

        String icon = data.get(position).getIcon();
        icon = icon.replaceAll("n", "d");

        String finalIcon = icon;

        Picasso.get().load("https://openweathermap.org/img/wn/" + icon + "@2x.png")
                .into(holder.imageViewIcon, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal() weather icon loaded: " + "https://openweathermap.org/img/wn/" + finalIcon + "@2x.png");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal(): error loading weather icon: " + e.getLocalizedMessage());
                        holder.imageViewIcon.setImageResource(R.mipmap.image870);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewDate, textViewTemp, textViewDayDisplay, textViewMin, textViewMax,
        textViewConditionDay, textViewWindDay, textViewRainDay;
        ImageView imageViewIcon,imageViewCardBackground, imageViewRainDay;
        CardView cardViewDay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTemp = itemView.findViewById(R.id.textViewTemp);
            textViewDayDisplay = itemView.findViewById(R.id.textViewDayDisplay);
            imageViewIcon = itemView.findViewById(R.id.imageViewIconDay);
            cardViewDay = itemView.findViewById(R.id.cardViewDay);
            imageViewCardBackground = itemView.findViewById(R.id.imageViewCardBackground);
            textViewMin = itemView.findViewById(R.id.textViewMinDay);
            textViewMax = itemView.findViewById(R.id.textViewMaxDay);
            textViewConditionDay = itemView.findViewById(R.id.textViewConditionDay);
            textViewWindDay = itemView.findViewById(R.id.textViewWindDay);
            textViewRainDay = itemView.findViewById(R.id.textViewRainDay);
            imageViewRainDay = itemView.findViewById(R.id.imageViewRainDay);
        }
    }

    private void loadImageList(){
        imageList.add(R.drawable.item00);
        imageList.add(R.drawable.item0);
        imageList.add(R.drawable.item1);
        imageList.add(R.drawable.item2);
        imageList.add(R.drawable.item3);
        imageList.add(R.drawable.item4);
    }

}
