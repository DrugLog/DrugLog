package lolbellum.druglog;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Eliot on 5/30/2015.
 */
public class Metric {
    DrugManager drugManager = Calendar.drugManager;
    private long m_id = -1;
    private String m_metricName = "";
    private Drug m_metricDrug = null;
    //double m_amount = 0.000;

    public static final String TABLE_METRIC = "metricTable";
    public static final String INDEX_METRIC = "metricIndex";
    public static final String COLUMN_METRICID = "metricId";
    public static final String COLUMN_METRICDRUG = "metricDrug";
    public static final String COLUMN_METRICNAME = "metricName";
    public static final String[] FIELDS = {COLUMN_METRICID,COLUMN_METRICNAME, COLUMN_METRICDRUG};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_METRIC + "(" +
            COLUMN_METRICID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_METRICDRUG + " TEXT, " +
            COLUMN_METRICNAME + " TEXT );";

    public Metric(String metricName){
       // m_metricDrug = drug;
        m_metricName = metricName;
        drugManager.addMetric(this);

    }

    public Metric(Cursor cursor){
        m_id = cursor.getLong(0);
        m_metricName = cursor.getString(1);
        drugManager.addMetric(this);
    }

    public ContentValues getValues(){
        ContentValues values = new ContentValues();
        values.put(COLUMN_METRICNAME,m_metricName);

        return values;
    }

    public long getId(){
        return m_id;
    }
    public void setId(long id){
        m_id = id;
    }

    public String getMetricName(){
        return m_metricName;
    }
    public void setMetricName(String metricName){
        m_metricName = metricName;
    }

    public Drug getMetricDrug(){
        return m_metricDrug;
    }
    public void setMetricDrug(Drug drug){
        m_metricDrug = drug;
    }

}
