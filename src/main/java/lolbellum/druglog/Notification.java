package lolbellum.druglog;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Eliot on 8/7/2015.
 */
public class Notification {

    private DrugManager drugManager = Calendar.drugManager;

    private long m_drugId = -1;
    private long m_id = -1;
    private String m_timeFrame;
    private boolean m_isActive = false;
    private double m_doseAmount = 0;
    private long m_metricId = -1;
    private long m_endTime = 0;

    public static final String TABLE_NAME = "notificationTable";
    public static final String COLUMN_ID =  "notificationId";
    public static final String COLUMN_DRUGID = "notificationDrugId";
    public static final String COLUMN_TIMEFRAME = "notificationTimeFrame";
    public static final String COLUMN_ENDTIME = "notificationEndTime";
    public static final String COLUMN_ISACTIVE = "notificationIsActive";
    public static final String COLUMN_DOSEAMOUNT = "notificationDoseAmount";
    public static final String COLUMN_METRIC = "notificationMetric";
    public static final String[] FIELDS = {COLUMN_ID,COLUMN_DRUGID,COLUMN_TIMEFRAME, COLUMN_ENDTIME,COLUMN_ISACTIVE,COLUMN_DOSEAMOUNT,COLUMN_METRIC};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DRUGID + " TEXT, " +
            COLUMN_TIMEFRAME + " TEXT, " +
            COLUMN_ENDTIME + " TEXT, " +
            COLUMN_ISACTIVE + " INTEGER, " +
            COLUMN_DOSEAMOUNT + " TEXT, " +
            COLUMN_METRIC + " TEXT " +
            ");";

    public Notification(long drugId,String timeFrame, long endTime, double doseAmount, long metricId){
        m_drugId = drugId;
        m_timeFrame = timeFrame;
        m_endTime = endTime;
        m_isActive = true;
        m_doseAmount = doseAmount;
        m_metricId = metricId;
        drugManager.addNotification(this);
    }

    public Notification(Cursor cursor){
        m_id = cursor.getLong(0);
        m_drugId = cursor.getLong(1);
        m_timeFrame = cursor.getString(2);
        m_endTime = cursor.getLong(3);
        if(cursor.getInt(4) == 0){
            m_isActive = false;
        } else {
            m_isActive = true;
        }
        m_doseAmount = cursor.getDouble(5);
        m_metricId = cursor.getLong(6);
        drugManager.addNotification(this);
    }

    public ContentValues getValues(){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,m_id);
        values.put(COLUMN_DRUGID,m_drugId);
        values.put(COLUMN_TIMEFRAME,m_timeFrame);
        values.put(COLUMN_ENDTIME,m_endTime);
        values.put(COLUMN_ISACTIVE,m_isActive);
        values.put(COLUMN_DOSEAMOUNT,m_doseAmount);
        values.put(COLUMN_METRIC,m_metricId);
        return values;
    }

    public long getId(){
        return m_id;
    }
    public void setId(long id){
        m_id = id;
    }

    public Drug getDrug(){
        for(int x = 0;x < drugManager.getDrugs().size();x++){
            Drug tempDrug = drugManager.getDrugs().get(x);
            if(tempDrug.getDrugId() == m_drugId){
                return tempDrug;
            }
        }
        return null;
    }

    public long getDrugId(){
        return m_drugId;
    }
    public void setDrugId(long drugId){
        m_drugId = drugId;
    }

    public String getTimeFrame(){
        return m_timeFrame;
    }
    public void setTimeFrame(String timeFrame){
        m_timeFrame = timeFrame;
    }

    public boolean isActive(){
        return m_isActive;
    }
    public void setActive(boolean isActive){
        m_isActive = isActive;
    }

    public double getDoseAmount(){
        return m_doseAmount;
    }
    public void setDoseAmount(double doseAmount){
        m_doseAmount = doseAmount;
    }

    public Metric getMetric(){
        for(int x = 0;x < drugManager.getAllMetrics().size();x++){
            Metric tempMetric = drugManager.getAllMetrics().get(x);
            if(tempMetric.getId() == m_metricId){
                return tempMetric;
            }
        }
        return null;
    }
    public void setMetricId(long id){
        m_metricId = id;
    }

    public long getEndTime(){
        return m_endTime;
    }
    public void setEndTime(long endTime){
        m_endTime = endTime;
    }
}
