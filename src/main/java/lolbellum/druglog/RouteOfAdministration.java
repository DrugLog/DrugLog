package lolbellum.druglog;


import android.content.ContentValues;
import android.database.Cursor;

public class RouteOfAdministration {

    private DrugManager drugManager = Calendar.drugManager;


    private String m_roaName;
    private long m_id;

    public static final String TABLE_ROA = "roa";
    public static final String INDEX_ROA = "roaIndex";
    public static final String COLUMN_ROAID = "roaId";
    public static final String COLUMN_ROANAME = "roaName";
    public static final String[] FIELDS = {COLUMN_ROAID, COLUMN_ROANAME};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_ROA + "(" +
            COLUMN_ROAID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ROANAME + " TEXT );";

    public RouteOfAdministration(String roaName){
        m_roaName = roaName;
        drugManager.addRoa(this);
    }

    public RouteOfAdministration(Cursor cursor){
        m_id = cursor.getLong(0);
        m_roaName = cursor.getString(1);
        drugManager.addRoa(this);
    }

    public ContentValues getValues(){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROANAME, m_roaName);
        return values;
    }

    public long getId(){
        return m_id;
    }
    public void setId(long id){
        m_id = id;
    }
    public String getRoaName(){
        return m_roaName;
    }
    public void setRoaName(String roaName){
        m_roaName = roaName;
    }
}
