package lolbellum.druglog;


import android.content.ContentValues;
import android.database.Cursor;

public class Reminder {

    private DrugManager drugManager = Calendar.drugManager;

    private long m_id;
    private Drug m_drug;
    private DrugClass m_drugClass;
    private String m_text;
    private String m_title;
    private int m_time;
    private int m_timeFor = 0;
    private boolean m_active = true;

    public static final String TABLE_REMINDER = "reminderTable";
    public static final String INDEX_REMINDER = "reminderIndex";
    public static final String COLUMN_REMINDERID = "reminderId";
    public static final String COLUMN_REMINDER_DRUGNAME = "reminderDrugName";
    public static final String COLUMN_REMINDER_DRUGCLASSNAME = "reminderDrugClassName";
    public static final String COLUMN_REMINDER_TITLE = "reminderTitle";
    public static final String COLUMN_REMINDER_TEXT = "reminderText";
    public static final String COLUMN_REMINDER_TIME = "reminderTime";
    public static final String COLUMN_REMINDER_TIMEFOR = "reminderTimeFor";
    public static final String[] FIELDS = {COLUMN_REMINDERID, COLUMN_REMINDER_DRUGNAME,COLUMN_REMINDER_DRUGCLASSNAME,COLUMN_REMINDER_TITLE,COLUMN_REMINDER_TEXT,COLUMN_REMINDER_TIME,COLUMN_REMINDER_TIMEFOR};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_REMINDER + "(" +
            COLUMN_REMINDERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_REMINDER_DRUGNAME + " TEXT, " +
            COLUMN_REMINDER_DRUGCLASSNAME + " TEXT, " +
            COLUMN_REMINDER_TITLE + " TEXT, " +
            COLUMN_REMINDER_TEXT + " TEXT, " +
            COLUMN_REMINDER_TIME + " TEXT, " +
            COLUMN_REMINDER_TIMEFOR + " TEXT );";

    public Reminder(Drug drug, DrugClass drugClass, String reminderTitle, String reminderText, int reminderTime, int timeFor){
        m_drug = drug;
        m_drugClass = drugClass;
        m_title = reminderTitle;
        m_text = reminderText;
        m_time = reminderTime;
        m_timeFor = timeFor;
        drugManager.addReminder(this);
    }

    public Reminder(Drug drug, String reminderTitle, String reminderText, int reminderTime, int timeFor){
        m_drug = drug;
        m_title = reminderTitle;
        m_text = reminderText;
        m_time = reminderTime;
        m_timeFor = timeFor;
        drugManager.addReminder(this);
    }

    public Reminder(Cursor cursor){
        m_id = cursor.getLong(0);
        if(cursor.getString(1) != null) {
            m_drug = drugManager.getDrugFromName(cursor.getString(1));
        }
        if(cursor.getString(2) != null) {
            m_drugClass = drugManager.getDrugClass(cursor.getString(2));
        }
        m_title = cursor.getString(3);
        m_text = cursor.getString(4);
        m_time = cursor.getInt(5);
        m_timeFor = cursor.getInt(6);
        m_active = true;
        drugManager.addReminder(this);
    }

    public ContentValues getValues(){
        ContentValues values = new ContentValues();
        if(m_drug != null) {
            values.put(COLUMN_REMINDER_DRUGNAME, m_drug.getDrugName());
        }
        if(m_drugClass != null) {
            values.put(COLUMN_REMINDER_DRUGCLASSNAME, m_drugClass.getDrugClassName());
        }
        values.put(COLUMN_REMINDER_TITLE,m_title);
        values.put(COLUMN_REMINDER_TEXT,m_text);
        values.put(COLUMN_REMINDER_TIME, String.valueOf(m_time));
        values.put(COLUMN_REMINDER_TIMEFOR,String.valueOf(m_timeFor));

        return values;
    }

    public long getId(){
        return m_id;
    }
    public void setID(long id){
        m_id = id;
    }
    public Drug getDrug(){
        return m_drug;
    }
    public void setDrug(Drug drug){
        m_drug = drug;
    }
    public DrugClass getDrugClass(){
        return m_drugClass;
    }
    public void setDrugClass(DrugClass drugClass){
        m_drugClass = drugClass;
    }
    public String getTitle(){
        return m_title;
    }
    public void setTitle(String title){
        m_title = title;
    }
    public String getText(){
        return m_text;
    }
    public void setText(String text){
        m_text = text;
    }
    public int getTime(){
        return m_time;
    }
    public void setTime(int time){
        m_time = time;
    }
    public int getTimeFor(){
        return m_timeFor;
    }
    public void setTimeFor(int timeFor){
        m_timeFor = timeFor;
    }
    public boolean isActive(){
        return m_active;
    }
    public void setIsActive(boolean active){
        m_active = active;
    }
}
