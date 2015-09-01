package lolbellum.druglog;


import android.content.ContentValues;
import android.database.Cursor;

public class Note {

    private DrugManager drugManager = Calendar.drugManager;

    private long m_id;
    private Dose m_dose;
    private String m_noteTitle;
    private String m_noteText;
    private String m_noteTime;

    public static final String TABLE_NOTE = "noteTables";
    public static final String INDEX_NOTE = "noteIndex";
    public static final String COLUMN_NOTEID = "noteId";
    public static final String COLUMN_DOSEID = "doseId";
    public static final String COLUMN_DOSE_DRUGUSED = "doseDrugUsed";
    public static final String COLUMN_DOSE_ROAUSED = "doseRoaUsed";
    public static final String COLUMN_DOSE_DATE = "doseDate";
    public static final String COLUMN_TITLE = "noteTitle";
    public static final String COLUMN_TEXT = "noteText";
    public static final String COLUMN_TIME = "noteTime";
    //public static final String[] FIELDS = {COLUMN_NOTEID, COLUMN_DOSE_DRUGUSED,COLUMN_DOSE_ROAUSED,COLUMN_DOSE_ROAUSED,COLUMN_DOSE_DATE,COLUMN_TITLE,COLUMN_TEXT,COLUMN_TIME};
    public static final String[] FIELDS = {COLUMN_NOTEID,COLUMN_DOSEID,COLUMN_TITLE,COLUMN_TEXT,COLUMN_TIME};

   /* public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NOTE + "(" +
            COLUMN_NOTEID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DOSE_DRUGUSED + " TEXT, " +
            COLUMN_DOSE_ROAUSED + " TEXT, " +
            COLUMN_DOSE_DATE + " TEXT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_TEXT + " TEXT, " +
            COLUMN_TIME + " TEXT );";*/

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NOTE + "(" +
            COLUMN_NOTEID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DOSEID + " INTEGER, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_TEXT + " TEXT, " +
            COLUMN_TIME + " TEXT );";

    public Note(Dose dose,String noteTitle, String noteText, String noteTime){
        m_dose = dose;
        m_noteTitle = noteTitle;
        m_noteText = noteText;
        m_noteTime = noteTime;
        drugManager.addNote(this);
    }

    public Note(Cursor cursor){
        m_id = cursor.getLong(0);
        m_dose = drugManager.getDose(cursor.getInt(1));
        m_noteTitle = cursor.getString(2);
        m_noteText = cursor.getString(3);
        m_noteTime = cursor.getString(4);
        drugManager.addNote(this);

    }

    public ContentValues getValues(){
        ContentValues values = new ContentValues();

        values.put(COLUMN_DOSEID,String.valueOf(m_dose.getId()));
        values.put(COLUMN_TITLE,m_noteTitle);
        values.put(COLUMN_TEXT,m_noteText);
        values.put(COLUMN_TIME,m_noteTime);

        return values;
    }

    public long getId(){
        return m_id;
    }
    public void setId(long id){
        m_id = id;
    }
    public String getNoteTitle(){
        return m_noteTitle;
    }
    public void setNoteTitle(String noteTitle){
        m_noteTitle = noteTitle;
    }
    public String getNoteText(){
        return m_noteText;
    }
    public void setNoteText(String noteText){
        m_noteText = noteText;
    }
    public String getNoteTime(){
        return m_noteTime;
    }
    public void setNoteTime(String noteTime){
        m_noteTime = noteTime;
    }

    public Dose getNoteDose(){
        return m_dose;
    }
    public void setNoteDose(Dose dose){
        m_dose = dose;
    }
}
