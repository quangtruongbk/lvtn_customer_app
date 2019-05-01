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

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

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
            Log.d("6abc", "Date: " + calendar.get(Calendar.DATE));
            calendar.add(Calendar.DATE,  -Math.round(e.getX()));
            Log.d("6abc", "Date: - 7 " + calendar.get(Calendar.DATE));
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
