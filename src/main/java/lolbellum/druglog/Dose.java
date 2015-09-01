package lolbellum.druglog;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Dose {

    private DrugManager drugManager = lolbellum.druglog.Calendar.drugManager;


    private long m_id = -1;
    private Metric m_metric;
    private Drug m_drug;
    //private Calendar m_date;
    private long m_dateMilliseconds;
    private RouteOfAdministration m_roa;
    private double m_doseAmount;
    private String m_date;
    private ArrayList<Note> m_notes = new ArrayList<Note>();

    public static final String TABLE_DOSE = "doseTable";
    public static final String INDEX_DOSE = "doseIndex";
    public static final String COLUMN_DOSEID = "doseId";
    public static final String COLUMN_DRUGUSED = "drugUsed";
    public static final String COLUMN_METRIC = "metricUsed";
    public static final String COLUMN_DATE = "dateUsed";
    public static final String COLUMN_ROA = "roaUsed";
    public static final String COLUMN_DOSEAMOUNT = "doseAmount";
    public static final String[] FIELDS = {COLUMN_DOSEID, COLUMN_DRUGUSED,COLUMN_METRIC,COLUMN_DATE,COLUMN_ROA,COLUMN_DOSEAMOUNT};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_DOSE + "(" +
            COLUMN_DOSEID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DRUGUSED + " TEXT, " +
            COLUMN_METRIC + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_ROA + " TEXT,  " +
            COLUMN_DOSEAMOUNT + " TEXT " +
            ");";

  /*  public Dose(Drug drug, Metric metric, RouteOfAdministration roa, long dateMilliseconds, double doseAmount){
        m_drug = drug;
        m_metric = metric;
        m_dateMilliseconds = dateMilliseconds;
        m_doseAmount = doseAmount;
        drugManager.addDose(this);
    }*/
    public Dose(Drug drug, Metric metric, RouteOfAdministration roa, double doseAmount, String date){
        m_drug = drug;
        m_metric = metric;
        m_roa = roa;
        m_doseAmount = doseAmount;
        m_date = date;
        drugManager.addDose(this);
    }
    public Dose(Drug drug, long dateMilliseconds){
        m_drug = drug;
        m_dateMilliseconds = dateMilliseconds;
        m_metric = null;
    }

    public Dose(Cursor cursor){
        m_id = cursor.getLong(0);
        m_drug = drugManager.getDrugFromName(cursor.getString(1));
        m_metric = drugManager.getMetric(cursor.getString(2));
        m_date = cursor.getString(3);
        m_roa = drugManager.getRoa(cursor.getString(4));
        m_doseAmount = cursor.getDouble(5);
        drugManager.addDose(this);
    }

    public ContentValues getValues(){
        ContentValues values = new ContentValues();

        values.put(COLUMN_DRUGUSED,m_drug.getDrugName());
        values.put(COLUMN_METRIC,m_metric.getMetricName());
        values.put(COLUMN_DATE,m_date);
        values.put(COLUMN_ROA,m_roa.getRoaName());
        values.put(COLUMN_DOSEAMOUNT,String.valueOf(m_doseAmount));

        return values;
    }

    public long getId(){
        return m_id;
    }
    public void setId(long id){
        m_id = id;
    }

    public Drug getDrug(){
        return m_drug;
    }
    public void setDrug(Drug drug){
        m_drug = drug;
    }
    public void setDrug(String drugName){
        if(drugManager.drugExists(drugName)){
            m_drug = drugManager.getDrugFromName(drugName);
        }
    }
    public RouteOfAdministration getRoa(){
        return m_roa;
    }
    public void setRoa(RouteOfAdministration roa){
        m_roa = roa;
    }
    public void setRoa(String roaName){
        if(drugManager.getRoa(roaName) != null){
            m_roa = drugManager.getRoa(roaName);
        }
    }
    public double getDoseAmount(){
        return m_doseAmount;
    }
    public void setDoseAmount(double doseAmount){
        m_doseAmount = doseAmount;
    }

    public Metric getMetric(){
        return m_metric;
    }
    public void setMetric(Metric metric){
        m_metric = metric;
    }

    public String getDate(){
        return m_date;
    }
    public void setDate(String date){
        m_date = date;
    }

    public long getDateMilliseconds(){
        return m_dateMilliseconds;
    }
    public void setDateMilliseconds(long milliseconds){
        m_dateMilliseconds = milliseconds;
    }

    public ArrayList<Note> getNotes(){
        return m_notes;
    }
    public void setNotes(ArrayList<Note> notes){
        m_notes = notes;
    }
    public void addNote(Note note){
        m_notes.add(note);
    }
}
