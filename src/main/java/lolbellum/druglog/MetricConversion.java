package lolbellum.druglog;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Eliot on 7/6/2015.
 */
public class MetricConversion {

    private DrugManager drugManager = Calendar.drugManager;

    private long m_id;
    private double m_conversionRate = 0.000;
    private Metric m_metricToConvert;
    private Metric m_metricToConvertTo;

    public static final String TABLE_METRIC_CONVERSION = "metricConversionTable";
    public static final String INDEX_METRIC_CONVERSION = "metricConversionIndex";
    public static final String COLUMN_METRICCONVERSION_ID = "metricConversionId";
    public static final String COLUMN_METRICTOCONVERT = "metricToConvert";
    public static final String COLUMN_METRICTOCONVERTTO = "metricToConvertTo";
    public static final String COLUMN_CONVERSIONRATE = "conversionRate";
    public static final String[] FIELDS = {COLUMN_METRICCONVERSION_ID,COLUMN_METRICTOCONVERT,COLUMN_METRICTOCONVERTTO,COLUMN_CONVERSIONRATE};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_METRIC_CONVERSION + "(" +
            COLUMN_METRICCONVERSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_METRICTOCONVERT + " TEXT, " +
            COLUMN_METRICTOCONVERTTO + " TEXT, " +
            COLUMN_CONVERSIONRATE + " DOUBLE );";

    public MetricConversion(Metric metric1, Metric metric2, double conversionRate){
        m_metricToConvert = metric1;
        m_metricToConvertTo = metric2;
        m_conversionRate = conversionRate;
        drugManager.addMetricConversion(this);
    }

    public MetricConversion(Cursor cursor){
        m_id = cursor.getLong(0);
        m_metricToConvert = drugManager.getMetric(cursor.getString(1));
        m_metricToConvertTo = drugManager.getMetric(cursor.getString(2));
        m_conversionRate = cursor.getDouble(3);
       // m_conversionRate = Double.valueOf(cursor.getString(3));
        drugManager.addMetricConversion(this);
    }

    public ContentValues getValues(){
        ContentValues values = new ContentValues();

        values.put(COLUMN_METRICTOCONVERT,m_metricToConvert.getMetricName());
        values.put(COLUMN_METRICTOCONVERTTO,m_metricToConvertTo.getMetricName());
        //values.put(COLUMN_CONVERSIONRATE,String.valueOf(m_conversionRate));
        values.put(COLUMN_CONVERSIONRATE,m_conversionRate);

        return values;
    }

    public double getConversionRate(Metric metric1, Metric metric2){
        if(metric1 == m_metricToConvert && metric2 == m_metricToConvertTo){
            return m_conversionRate;
        }
        return 0;
    }

    public long getId(){
        return m_id;
    }
    public void setId(long id){
        m_id = id;
    }

    public double getConversionRate(){
        return m_conversionRate;
    }

    public void setConversionRate(double conversionRate){
        m_conversionRate = conversionRate;
    }

    public Metric getMetricToConvert(){
        return m_metricToConvert;
    }

    public void setMetricToConvert(Metric metric){
        m_metricToConvert = metric;
    }

    public void setMetricToConvert(String metricName){
        m_metricToConvert = drugManager.getMetric(metricName);
    }

    public Metric getMetricToConvertTo(){
        return m_metricToConvertTo;
    }
    public void setMetricToConvertTo(Metric metric){
        m_metricToConvertTo = metric;
    }
    public void setMetricToConvertTo(String metricName){
        m_metricToConvertTo = drugManager.getMetric(metricName);
    }
}
