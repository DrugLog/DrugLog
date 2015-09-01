package lolbellum.druglog;

import android.content.Context;

import com.jjoe64.graphview.DefaultLabelFormatter;

import java.text.DateFormat;

/**
 * Created by Eliot on 8/13/2015.
 */
public class HourLabelFormatter extends DefaultLabelFormatter {
    protected final DateFormat mDateFormat;
    protected final java.util.Calendar mCalendar;

    public HourLabelFormatter(Context context) {
        this.mDateFormat = android.text.format.DateFormat.getDateFormat(context);
        this.mCalendar = java.util.Calendar.getInstance();
    }

    public HourLabelFormatter(Context context, DateFormat dateFormat) {
        this.mDateFormat = dateFormat;
        this.mCalendar = java.util.Calendar.getInstance();
    }

    public String formatLabel(double value, boolean isValueX) {
        if(isValueX) {
            this.mCalendar.setTimeInMillis((long)value);
            //return this.mDateFormat.format(Long.valueOf(this.mCalendar.getTimeInMillis())).substring(0,5);
            return this.mDateFormat.format(Long.valueOf(this.mCalendar.getTimeInMillis())).substring(0,1);
        } else {
            return super.formatLabel(value, isValueX);
        }
    }
}
