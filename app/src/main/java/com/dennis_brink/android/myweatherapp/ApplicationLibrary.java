package com.dennis_brink.android.myweatherapp;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Map;

public class ApplicationLibrary {

    public static void setColorTextView(Map<String, TextView> textViews){
        if(AppConfig.getInstance().isDarkThemeActive()){
            for(TextView x : textViews.values()){
                x.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.WhiteSmoke));
            }
        } else {
            for(TextView x : textViews.values()){
                x.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.Black));
            }
        }
    }

    public static void showTextViews(Map<String, TextView> textViews){

        for(TextView x : textViews.values()){
            x.setVisibility(View.VISIBLE);
        }

    }

    public static void hideRating(ArrayList<ImageView> rating){

        for(ImageView x : rating){
            x.setVisibility(View.INVISIBLE);
        }

    }

    public static void showRating(ArrayList<ImageView>rating){

        for(ImageView x : rating){
            x.setVisibility(View.VISIBLE);
        }

    }

    public static void setColorView(EditText view){
        if(AppConfig.getInstance().isDarkThemeActive()){
            view.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.LightGrey));
        } else {
            view.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.Black));
        }
    }

    public static void setColorDrawable(ImageView view , int color) {
        view.setColorFilter(ContextCompat.getColor(Application.getContext(), color));
    }

    public static void setColorDrawableBackgroundStroke(EditText view) {
        Drawable background = view.getBackground();
        if (background instanceof GradientDrawable) {
            setColorStrokeDrawable((GradientDrawable) background);
        } else {
            Log.d("DENNIS_B", "Stroke color can only be set dynamically on gradient drawables");
        }
    }

    private static void setColorStrokeDrawable(GradientDrawable drawable){
        if(AppConfig.getInstance().isDarkThemeActive()){
            drawable.setStroke(2, ContextCompat.getColor(Application.getContext(), R.color.WhiteSmoke));
        } else {
            drawable.setStroke(2, ContextCompat.getColor(Application.getContext(), R.color.Black));
        }
    }

}
