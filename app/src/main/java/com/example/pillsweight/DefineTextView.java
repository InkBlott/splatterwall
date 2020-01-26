package com.example.pillsweight;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

public class DefineTextView extends AppCompatTextView {
    double dl;
    String date;

    public String getDate() {
        return date;
    }



    public void setDate(String date) {
        this.date = date;
    }

    public double getDl() {
        return dl;
    }

    public void setDl(double dl) {
        this.dl = dl;
    }

    public DefineTextView(Context context, LinearLayout.LayoutParams layParam, double weight, String date) {
        super(context);
        setGravity(Gravity.CENTER);
        setLayoutParams(layParam);
        setDate(date);
        setDl(weight);
        setText(date + "    " + "weight: " + weight);
        setTextColor(Color.WHITE);
        setTextSize(20);
    }

    public DefineTextView(Context context, LinearLayout.LayoutParams layParam, double weight) {
        super(context);
        setGravity(Gravity.CENTER);
        setLayoutParams(layParam);
        setDl(weight);
        setText("    " + "weight: " + weight);
        setTextColor(Color.WHITE);
        setTextSize(20);
    }
}
