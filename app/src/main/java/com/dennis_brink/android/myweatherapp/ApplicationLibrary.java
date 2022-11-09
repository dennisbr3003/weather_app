package com.dennis_brink.android.myweatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
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

    public static AlertDialog getErrorAlertDialog(String text, Context context){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

        builder.setCancelable(false);
        LayoutInflater inf = LayoutInflater.from(context);
        View dialogErrorMessage = inf.inflate(R.layout.error_dialog, null);

        builder.setView(dialogErrorMessage);

        TextView message = dialogErrorMessage.findViewById(R.id.textViewText);
        Button btnOk = dialogErrorMessage.findViewById(R.id.btnOk);

        message.setText(text);
        androidx.appcompat.app.AlertDialog dlg = builder.create();

        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        btnOk.setOnClickListener(view -> {
            dlg.dismiss();
        });

        return dlg;
    }

    @SuppressLint("DefaultLocale")
    public static String getTime(int timeInMillies){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillies * 1000L); // to stop overflow
        return String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY),
                                          calendar.get(Calendar.MINUTE));
    }

    @SuppressLint("DefaultLocale")
    public static String getDate(int timeInMillies){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillies * 1000L); // to stop overflow
        return String.format("%02d-%02d-%04d", calendar.get(Calendar.DAY_OF_MONTH),
                                               calendar.get(Calendar.MONTH) + 1,
                                               calendar.get(Calendar.YEAR));
    }

    public static int getDayOfWeek(int timeInMillies){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillies * 1000L); // to stop overflow
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String getTodayDateTime(){
        Calendar calendar = Calendar.getInstance();
        return String.format("%02d-%02d-%04d %02d:%02d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
    }

    public static double formatToDecimals(double number, int decimals){
        String sx="";
        switch(decimals){
            case 1:
                sx= String.format("%.1f", number);
                break;
            case 2:
                sx= String.format("%.2f", number);
                break;
        }
        sx = sx.replaceAll(",", ".");
        double d = Double.parseDouble(sx);
        return d;
    }

}
