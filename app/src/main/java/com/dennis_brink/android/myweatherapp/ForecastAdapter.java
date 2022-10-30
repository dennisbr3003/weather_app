package com.dennis_brink.android.myweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dennis_brink.android.myweatherapp.model_forecast.OpenWeatherForecast;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<com.dennis_brink.android.myweatherapp.model_forecast.List> data;

    public ForecastAdapter(List<com.dennis_brink.android.myweatherapp.model_forecast.List> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textViewDummy.setText(data.get(0).getDtTxt());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewDummy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDummy = itemView.findViewById(R.id.textViewDummy);
        }
    }

}
