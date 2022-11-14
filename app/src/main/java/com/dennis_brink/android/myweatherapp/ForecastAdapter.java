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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<Day> data;
    private Context context;

    private ArrayList<Integer>imageList = new ArrayList<>();
    private Map<String, byte[]> bCache = new HashMap<>();

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
        holder.textViewMin.setText(ApplicationLibrary.formatToDecimals(data.get(position).getMintemp(), 1) + "°");
        holder.textViewMax.setText(ApplicationLibrary.formatToDecimals(data.get(position).getMaxtemp(), 1) + "°");
        holder.textViewPressureDay.setText(""+data.get(position).getPressure());
        holder.textViewConditionDay.setText(data.get(position).getCondition());
        holder.textViewHumidityDay.setText(data.get(position).getHumidity()+ "%");
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

        String fIcon = data.get(position).getIcon().replaceAll("n", "d"); // always use 'day' icons because they look nicer

        if(!AppCache.getInstance().hasElement(fIcon)) {
            Picasso.get().load("https://openweathermap.org/img/wn/" + fIcon + "@2x.png")
                    .into(holder.imageViewIcon, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                            Log.d("DENNIS_B", "ForecastAdapter.onBindViewHolder(): weather icon loaded: " + "https://openweathermap.org/img/wn/" + fIcon + "@2x.png");
                            AppCache.getInstance().cacheElement(fIcon, holder.imageViewIcon.getDrawable());

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("DENNIS_B", "ForecastAdapter.onBindViewHolder(): error loading weather icon: " + e.getLocalizedMessage());
                            holder.imageViewIcon.setImageResource(R.mipmap.image870);
                        }
                    });
        } else {

            holder.imageViewIcon.setImageBitmap(AppCache.getInstance().loadElement(fIcon));
            Log.d("DENNIS_B", "ForecastAdapter.onBindViewHolder(): weather icon loaded from bCache: " + fIcon);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewDate, textViewTemp, textViewDayDisplay, textViewMin, textViewMax,
        textViewConditionDay, textViewWindDay, textViewRainDay, textViewPressureDay, textViewHumidityDay;
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
            textViewPressureDay = itemView.findViewById(R.id.textViewPressureDay);
            textViewHumidityDay = itemView.findViewById(R.id.textViewHumidityDay);
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
