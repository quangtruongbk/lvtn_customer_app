package com.example.administrator.employeeapp.Utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class CustomPercentFormatter extends ValueFormatter {

    private DecimalFormat mFormat;

    public CustomPercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }

    public CustomPercentFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    @Override
    public String getFormattedValue(float value) {
        if (value == 0.0f)
            return "";
        return mFormat.format(value) + " %";
    }
}

