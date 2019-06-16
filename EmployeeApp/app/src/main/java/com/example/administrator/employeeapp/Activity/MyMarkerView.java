package com.example.administrator.employeeapp.Activity;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.employeeapp.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.Calendar;

public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private Integer day;
    public MyMarkerView(Context context, int layoutResource, int day) {
        super(context, layoutResource);
        this.day = day;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,  -(day - Math.round(e.getX()) - 1));
            Integer month = calendar.get(Calendar.MONTH)+1;
            tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true) + ", ngày: " + calendar.get(Calendar.DAY_OF_MONTH) + "/"+month);
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
