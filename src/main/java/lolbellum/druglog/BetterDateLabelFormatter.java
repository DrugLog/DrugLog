package lolbellum.druglog;

import android.content.Context;

import com.jjoe64.graphview.DefaultLabelFormatter;

import java.text.DateFormat;
import java.util.*;

/**
 * Created by Eliot on 7/3/2015.
 */
public class BetterDateLabelFormatter extends DefaultLabelFormatter {
    protected final DateFormat mDateFormat;
    protected final java.util.Calendar mCalendar;

    public BetterDateLabelFormatter(Context context) {
        this.mDateFormat = android.text.format.DateFormat.getDateFormat(context);
        this.mCalendar = java.util.Calendar.getInstance();
    }

    public BetterDateLabelFormatter(Context context, DateFormat dateFormat) {
        this.mDateFormat = dateFormat;
        this.mCalendar = java.util.Calendar.getInstance();
    }

    public String formatLabel(double value, boolean isValueX) {
        if(isValueX) {
            this.mCalendar.setTimeInMillis((long)value);
            return this.mDateFormat.format(Long.valueOf(this.mCalendar.getTimeInMillis())).substring(0,5);
        } else {
            return super.formatLabel(value, isValueX);
        }
    }
}
