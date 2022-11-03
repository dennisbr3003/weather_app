package com.dennis_brink.android.myweatherapp;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

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

    public static void setColorView(EditText view){
        if(AppConfig.getInstance().isDarkThemeActive()){
            view.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.LightGrey));
        } else {
            view.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.Black));
        }
    }

    public static void setColorDrawableBackground(EditText view) {
        Drawable background = view.getBackground();
        setColorBackgroundDrawable(background);
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

    private static void setColorBackgroundDrawable(Drawable drawable){

        int color;
        if(AppConfig.getInstance().isDarkThemeActive()){
            color = R.color.WhiteSmoke;
        } else {
            color = R.color.Black;
        }

        if (drawable instanceof ShapeDrawable) {
            ShapeDrawable shapeDrawable = (ShapeDrawable) drawable;
            shapeDrawable.getPaint().setColor(ContextCompat.getColor(Application.getContext(), color));
        } else if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setColor(ContextCompat.getColor(Application.getContext(), color));
        } else if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            colorDrawable.setColor(ContextCompat.getColor(Application.getContext(), color));
        } else {
            Log.d("DENNIS_B", "Background drawable type: unknown. Background color could not be set.");
        }

    }

}
