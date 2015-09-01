package lolbellum.druglog;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;


public class MyDBHandler extends SQLiteOpenHelper {

   private DrugManager drugManager = Calendar.drugManager;
    private static MyDBHandler singleton;


    private static final int DATABASE_VERSION = 25;
    private static final String DATABASE_DRUGLIST = "druglist.db";
    //private static final String INSERT = "INSERT INTO " + TABLE_DRUGCLASS + " (" +


    public static final String TABLE_DRUG = "drug";
    public static final String INDEX_DRUG = "drugIndex";
    public static final String COLUMN_ID = "drugID";
    public static final String COLUMN_DRUGNAME = "drugName";
    public static final String COLUMN_DRUGNICKNAMES = "drugNickNames";
    public static final String COLUMN_DRUGIMAGEID = "drugImageId";
    public static final String COLUMN_EROWIDLINK = "erowidLink";
    public static final String COLUMN_WIKILINK = "wikiLink";
    public static final String COLUMN_REDDITLINK = "redditLink";
    public static final String[] DRUG_FIELDS = {COLUMN_ID, COLUMN_DRUGNAME, COLUMN_DRUGNICKNAMES, COLUMN_DRUGIMAGEID,COLUMN_EROWIDLINK,COLUMN_WIKILINK,COLUMN_REDDITLINK};

    public static final String TABLE_DRUGCLASS = "drugclass";
    public static final String INDEX_DRUGCLASS = "drugClassIndex";
    public static final String COLUMN_DRUGCLASSID = "classId";
    public static final String COLUMN_DRUGCLASSNAME = "drugClassName";
    public static final String COLUMN_DRUGSINDRUGCLASS = "drugList";
    public static final String COLUMN_DRUGCLASSIMAGEID = "drugClassImageId";
    public static final String COLUMN_BADCOMBINATIONLIST = "badCombinationList";
    public static final String[] DRUGCLASS_FIELDS = {COLUMN_DRUGCLASSID,COLUMN_DRUGCLASSNAME,COLUMN_DRUGSINDRUGCLASS,COLUMN_DRUGCLASSIMAGEID,COLUMN_BADCOMBINATIONLIST};

    public static final String TABLE_ROA = "roa";
    public static final String INDEX_ROA = "roaIndex";
    public static final String COLUMN_ROAID = "roaId";
    public static final String COLUMN_ROANAME = "roaName";
    public static final String[] ROA_FIELDS = {COLUMN_ROAID, COLUMN_ROANAME};

    public static final String TABLE_DOSE = "doseTable";
    public static final String INDEX_DOSE = "doseIndex";
    public static final String COLUMN_DOSEID = "doseId";
    public static final String COLUMN_DRUGUSED = "drugUsed";
    public static final String COLUMN_METRIC = "metricUsed";
    public static final String COLUMN_DATE = "dateUsed";
    public static final String COLUMN_ROA = "roaUsed";
    public static final String COLUMN_DOSEAMOUNT = "doseAmount";
    public static final String[] DOSE_FIELDS = {COLUMN_DOSEID, COLUMN_DRUGUSED,COLUMN_METRIC,COLUMN_DATE,COLUMN_ROA,COLUMN_DOSEAMOUNT};

    public static final String TABLE_METRIC = "metricTable";
    public static final String INDEX_METRIC = "metricIndex";
    public static final String COLUMN_METRICID = "metricId";
    public static final String COLUMN_METRICDRUG = "metricDrug";
    public static final String COLUMN_METRICNAME = "metricName";
    public static final String[] METRIC_FIELDS = {COLUMN_METRICID,COLUMN_METRICNAME, COLUMN_METRICDRUG};

    public static final String TABLE_NOTE = "noteTables";
    public static final String INDEX_NOTE = "noteIndex";
    public static final String COLUMN_NOTEID = "noteId";
    public static final String COLUMN_DOSE_DRUGUSED = "doseDrugUsed";
    public static final String COLUMN_DOSE_ROAUSED = "doseRoaUsed";
    public static final String COLUMN_DOSE_DATE = "doseDate";
    public static final String COLUMN_TITLE = "noteTitle";
    public static final String COLUMN_TEXT = "noteText";
    public static final String COLUMN_TIME = "noteTime";
    public static final String[] NOTE_FIELDS = {COLUMN_NOTEID, COLUMN_DOSE_DRUGUSED,COLUMN_DOSE_ROAUSED,COLUMN_DOSE_ROAUSED,COLUMN_DOSE_DATE,COLUMN_TITLE,COLUMN_TEXT,COLUMN_TIME};

    public static final String TABLE_REMINDER = "reminderTable";
    public static final String INDEX_REMINDER = "reminderIndex";
    public static final String COLUMN_REMINDERID = "reminderId";
    public static final String COLUMN_REMINDER_DRUGNAME = "reminderDrugName";
    public static final String COLUMN_REMINDER_DRUGCLASSNAME = "reminderDrugClassName";
    public static final String COLUMN_REMINDER_TITLE = "reminderTitle";
    public static final String COLUMN_REMINDER_TEXT = "reminderText";
    public static final String COLUMN_REMINDER_TIME = "reminderTime";
    public static final String COLUMN_REMINDER_TIMEFOR = "reminderTimeFor";
    public static final String[] REMINDER_FIELDS = {COLUMN_REMINDERID, COLUMN_REMINDER_DRUGNAME,COLUMN_REMINDER_DRUGCLASSNAME,COLUMN_REMINDER_TITLE,COLUMN_REMINDER_TEXT,COLUMN_REMINDER_TIME,COLUMN_REMINDER_TIMEFOR};

    public static final String TABLE_METRIC_CONVERSION = "metricConversionTable";
    public static final String INDEX_METRIC_CONVERSION = "metricConversionIndex";
    public static final String COLUMN_METRICCONVERSION_ID = "metricConversionId";
    public static final String COLUMN_METRICTOCONVERT = "metricToConvert";
    public static final String COLUMN_METRICTOCONVERTTO = "metricToConvertTo";
    public static final String COLUMN_CONVERSIONRATE = "conversionRate";
    public static final String[] METRICCONVERSION_FIELDS = {COLUMN_METRICCONVERSION_ID,COLUMN_METRICCONVERSION_ID,COLUMN_METRICTOCONVERT,COLUMN_METRICTOCONVERTTO,COLUMN_CONVERSIONRATE};


    public final static String TABLE_SETTINGS = "settingsTable";
    public final static String COLUMN_PASSCODE = "passcode";


    public static String strSeparator = "__,__";

  /*  public static String convertArrayToString(ArrayList<Drug> drugsList){
        String str = "";
        for (int i = 0;i<drugsList.size(); i++) {
            str +=
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }*/

    private  Context context;

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_DRUGLIST, factory, DATABASE_VERSION);
        this.context = context;
    }

    public MyDBHandler(Context context){
        super(context, DATABASE_DRUGLIST,null,DATABASE_VERSION);
        this.context = context;

    }

    public static MyDBHandler getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        if(singleton == null){
            singleton = new MyDBHandler(context,name,factory,version);
        }
        return singleton;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        if(db == null){
            return;
        }

        String settingsTable = "CREATE TABLE " + TABLE_SETTINGS + "(" +
                COLUMN_PASSCODE + " TEXT );";

        //CREATE TABLE                     NAME OF TABLE
        String drugTable = "CREATE TABLE " + TABLE_DRUG + "(" +
                //LIST COLUMNS IN TABLE
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DRUGNAME + " TEXT, " +
                COLUMN_DRUGNICKNAMES + " TEXT, " +
                COLUMN_DRUGIMAGEID + " INTEGER, " +
                COLUMN_EROWIDLINK + " TEXT, " +
                COLUMN_WIKILINK + " TEXT, " +
                COLUMN_REDDITLINK + " TEXT " +
                ");";

        String drugIndex = "CREATE INDEX " + INDEX_DRUG + " ON " + TABLE_DRUG + "( " +
                COLUMN_ID + "," +
                COLUMN_DRUGNAME + "," +
                COLUMN_DRUGNICKNAMES + "," +
                COLUMN_DRUGIMAGEID + "," +
                COLUMN_EROWIDLINK + "," +
                COLUMN_WIKILINK + "," +
                COLUMN_REDDITLINK +
                " );";

        String drugClassTable = "CREATE TABLE " + TABLE_DRUGCLASS + "(" +
                COLUMN_DRUGCLASSID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DRUGCLASSNAME + " TEXT, " +
                COLUMN_DRUGSINDRUGCLASS + " TEXT, " +
                COLUMN_BADCOMBINATIONLIST + " TEXT, " +
                COLUMN_DRUGCLASSIMAGEID + " INTEGER " +
                ");";

        String drugClassIndex = "CREATE INDEX " + INDEX_DRUGCLASS + " ON " + TABLE_DRUGCLASS + "( " +
                COLUMN_DRUGCLASSID + "," +
                COLUMN_DRUGCLASSNAME + "," +
                COLUMN_DRUGSINDRUGCLASS + "," +
                COLUMN_DRUGCLASSIMAGEID +
                " );";

        String roaTable = "CREATE TABLE " + TABLE_ROA + "(" +
                COLUMN_ROAID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ROANAME + " TEXT );";

        String roaIndex = "CREATE INDEX " + INDEX_ROA + " ON " + TABLE_ROA + "( " +
                COLUMN_ROANAME +
                " );";

        Log.i("testing", "DBHandler - Creating TABLE_DOSE");
        String doseTable = "CREATE TABLE " + TABLE_DOSE + "(" +
                COLUMN_DOSEID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DRUGUSED + " TEXT, " +
                COLUMN_METRIC + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_ROA + " TEXT,  " +
                COLUMN_DOSEAMOUNT + " TEXT " +
                ");";

        String doseIndex = "CREATE INDEX " + INDEX_DOSE + " ON " + TABLE_DOSE  +  "( " +
                COLUMN_DRUGUSED + "," +
                COLUMN_METRIC + "," +
                COLUMN_DATE + "," +
                COLUMN_ROA + "," +
                COLUMN_DOSEAMOUNT +
                " );";
        Log.i("testing", "DBHandler - Creating index for TABLE_DOSE");

        String metricTable = "CREATE TABLE " + TABLE_METRIC + "(" +
                COLUMN_METRICID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_METRICDRUG + " TEXT, " +
                COLUMN_METRICNAME + " TEXT );";

        String metricIndex = "CREATE INDEX " + INDEX_METRIC + " ON " + TABLE_METRIC + "( " +
                COLUMN_METRICDRUG + "," +
                COLUMN_METRICNAME +
                " );";

        String noteTable = "CREATE TABLE " + TABLE_NOTE + "(" +
                COLUMN_NOTEID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DOSE_DRUGUSED + " TEXT, " +
                COLUMN_DOSE_ROAUSED + " TEXT, " +
                COLUMN_DOSE_DATE + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_TEXT + " TEXT, " +
                COLUMN_TIME + " TEXT );";

        String noteIndex = "CREATE INDEX " + INDEX_NOTE + " ON " + TABLE_NOTE + "( " +
                COLUMN_DOSE_DRUGUSED + "," +
                COLUMN_DOSE_ROAUSED + "," +
                COLUMN_DOSE_DATE + "," +
                COLUMN_TITLE + "," +
                COLUMN_TEXT + "," +
                COLUMN_TIME +
                " );";

        String reminderTable = "CREATE TABLE " + TABLE_REMINDER + "(" +
                COLUMN_REMINDERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REMINDER_DRUGNAME + " TEXT, " +
                COLUMN_REMINDER_DRUGCLASSNAME + " TEXT, " +
                COLUMN_REMINDER_TITLE + " TEXT, " +
                COLUMN_REMINDER_TEXT + " TEXT, " +
                COLUMN_REMINDER_TIME + " TEXT, " +
                COLUMN_REMINDER_TIMEFOR + " TEXT );";

        String reminderIndex = "CREATE INDEX " + INDEX_REMINDER + " ON " + TABLE_REMINDER + "( " +
                COLUMN_REMINDER_DRUGNAME + "," +
                COLUMN_REMINDER_DRUGCLASSNAME + "," +
                COLUMN_REMINDER_TITLE + "," +
                COLUMN_REMINDER_TEXT + "," +
                COLUMN_REMINDER_TIME + "," +
                COLUMN_REMINDER_TIMEFOR +
                " );";

        String metricConversionTable = "CREATE TABLE " + TABLE_METRIC_CONVERSION + "(" +
                COLUMN_METRICCONVERSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_METRICTOCONVERT + " TEXT, " +
                COLUMN_METRICTOCONVERTTO + " TEXT, " +
                COLUMN_CONVERSIONRATE + " TEXT );";

        //executes our query
        Log.i("testing", "DBHandler - Executing queries");
        db.execSQL(settingsTable);
        db.execSQL(Drug.CREATE_TABLE);
        db.execSQL(DrugClass.CREATE_TABLE);
        db.execSQL(RouteOfAdministration.CREATE_TABLE);
        db.execSQL(Metric.CREATE_TABLE);
        db.execSQL(Dose.CREATE_TABLE);
        db.execSQL(Note.CREATE_TABLE);
        db.execSQL(Reminder.CREATE_TABLE);
        db.execSQL(MetricConversion.CREATE_TABLE);
        db.execSQL(Notification.CREATE_TABLE);


        db.execSQL(drugIndex);
        db.execSQL(drugClassIndex);
        db.execSQL(roaIndex);
        db.execSQL(doseIndex);
        db.execSQL(metricIndex);
//        db.execSQL(noteIndex);
        db.execSQL(reminderIndex);
    }

    @Override
    //when our database upgrades
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DELETED OLD TABLE
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUGCLASS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_METRIC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_METRIC_CONVERSION);
        db.execSQL("DROP TABLE IF EXISTS " + Notification.TABLE_NAME);

        //create new one
        onCreate(db);
    }

    //how to get the number of rows a table contains, useful.
  /*  public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }*/

    //add a new row to the database
   /* public void addDrug(Drug drug){

        for(int x = 0;x < getDrugs().size();x++){
            if(getDrugs().get(x).getDrugName().equalsIgnoreCase(drug.getDrugName())){
                return;
            }
        }

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        String nicknames = "";

        //create new list of values
        ContentValues values = new ContentValues();
        values.put(COLUMN_DRUGNAME, drug.getDrugName());

        //converting nicknames ArrayList<String> to String
        if(drug.getDrugNickNames() != null){
            for(int x = 0;x<drug.getDrugNickNames().size();x++){
                 String tempNickName = drug.getDrugNickNames().get(x);
                if(x == (drug.getDrugNickNames().size() - 1)){
                    nicknames += tempNickName;
                } else {
                    nicknames += tempNickName + " , ";
                }
            }
            values.put(COLUMN_DRUGNICKNAMES, nicknames);
        }
        if(drug.getImageId() != null){
            values.put(COLUMN_DRUGIMAGEID, drug.getImageId());
        }

       // if(drug.getErowidLink() != ""){
            values.put(COLUMN_EROWIDLINK, drug.getErowidLink());
       // }
       /// if(drug.getWikiLink() != ""){
            values.put(COLUMN_WIKILINK, drug.getWikiLink());
       // }
      //  if(drug.getRedditLink() != ""){
            values.put(COLUMN_REDDITLINK, drug.getRedditLink());
       // }

        //create db object that is database that we're going to write to

        db.insert(TABLE_DRUG, null, values);
        //tells android that we're done with the database
        db.setTransactionSuccessful();
        db.endTransaction();

        db.close();
    }*/


  /*  public void addDrugClass(DrugClass drugClass){

        for(int x = 0;x < getDrugClasses().size();x++){
            if(getDrugClasses().get(x).getDrugClassName().equalsIgnoreCase(drugClass.getDrugClassName())){
                return;
            }
        }

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        //Making list of drugs in class
        String listOfDrugs = "";
        for(int x = 0; x < drugClass.getDrugs().size();x++){
            Drug tempDrug = drugClass.getDrugs().get(x);

            if(x < (drugClass.getDrugs().size() - 1)) {
                listOfDrugs += tempDrug.getDrugName() + " , ";
            } else {
                listOfDrugs += tempDrug.getDrugName();
            }
        }


        ContentValues values = new ContentValues();
        values.put(COLUMN_DRUGCLASSNAME, drugClass.getDrugClassName());
        //values.put(COLUMN_DRUGCLASSID, drugClass.getClassId());
        values.put(COLUMN_DRUGSINDRUGCLASS, listOfDrugs);
        if(drugClass.getBadCombinationList().size() > 0){
            String badCombinationList = "";

            for(int x = 0;x < drugClass.getBadCombinationList().size();x++){
                DrugClass tempDrugClass = drugClass.getBadCombinationList().get(x);

                if(x < (drugClass.getBadCombinationList().size() - 1)){
                    badCombinationList += tempDrugClass.getDrugClassName() + " , ";
                } else {
                    badCombinationList += tempDrugClass.getDrugClassName();
                }
            }

            values.put(COLUMN_BADCOMBINATIONLIST, badCombinationList);
        }

     //   if(drugClass.getImageId() != null){
            values.put(COLUMN_DRUGCLASSIMAGEID, drugClass.getImageId());
      //  }


        db.insert(TABLE_DRUGCLASS, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }*/

   /* public void addDose(Dose dose){

        for(int x = 0;x < getDoses().size();x++){
            if(getDoses().get(x).getDrug().getDrugName().equalsIgnoreCase(dose.getDrug().getDrugName()) && getDoses().get(x).getDate().equalsIgnoreCase(dose.getDate())){
                Log.i("testing", "DBHandler - dose already exists, returning");
                return;
            }
        }

        SQLiteDatabase db = getWritableDatabase();
       // db.beginTransaction();
        //String strLong = Long.toString(dose.getDateMilliseconds());

        ContentValues values = new ContentValues();
        values.put(COLUMN_DRUGUSED, dose.getDrug().getDrugName());
        values.put(COLUMN_METRIC, dose.getMetric().getMetricName());
        values.put(COLUMN_DATE, dose.getDate());
        values.put(COLUMN_ROA, dose.getRoa().getRoaName());
        values.put(COLUMN_DOSEAMOUNT, dose.getDoseAmount());

        db.insert(TABLE_DOSE, null, values);
        Log.i("testing", "Dose inserted into database ");
        //db.setTransactionSuccessful();
       // db.endTransaction();
        db.close();
    }*/

  /*  public void addMetric(Metric metric){

        for(int x = 0; x < getMetrics().size();x++){
            if(getMetrics().get(x).getMetricName().equalsIgnoreCase(metric.getMetricName())){
                return;
            }
        }

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();
       /* if(metric == null){
            //return;
        }

        if(metric.getMetricDrug() != null){
            values.put(COLUMN_METRICDRUG, metric.getMetricDrug().getDrugName());
        }
        values.put(COLUMN_METRICNAME, metric.getMetricName());

        db.insert(TABLE_METRIC, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }*/

    //how to get the number of rows a table contains, useful.


  /*  public void addRoa(RouteOfAdministration roa){

        for(int x = 0;x < getRoas().size();x++){
            if(getRoas().get(x).getRoaName().equalsIgnoreCase(roa.getRoaName())){
                return;
            }
        }

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();

        if(roa.getRoaName() != null){
            values.put(COLUMN_ROANAME, roa.getRoaName());
        }


        db.insert(TABLE_ROA, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }*/

 /*   public void addNote(Note note){
        for(int x = 0;x < getNotes().size();x++) {
            Note tempNote = getNotes().get(x);
            if (tempNote.getNoteDose().getDrug().getDrugName().equalsIgnoreCase(note.getNoteDose().getDrug().getDrugName()) && tempNote.getNoteDose().getRoa().getRoaName().equalsIgnoreCase(note.getNoteDose().getRoa().getRoaName()) && tempNote.getNoteDose().getDate().equalsIgnoreCase(note.getNoteDose().getDate()) && tempNote.getNoteTime().equalsIgnoreCase(note.getNoteTime())) {
                return;
            }
        }
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

            ContentValues values = new ContentValues();
            //if(note != null){
                values.put(COLUMN_DOSE_DRUGUSED, note.getNoteDose().getDrug().getDrugName());
                values.put(COLUMN_DOSE_ROAUSED, note.getNoteDose().getRoa().getRoaName());
                values.put(COLUMN_DOSE_DATE, note.getNoteDose().getDate());
                values.put(COLUMN_TITLE, note.getNoteTitle());
                values.put(COLUMN_TEXT, note.getNoteText());
                values.put(COLUMN_TIME, note.getNoteTime());
          //  }


            db.insert(TABLE_NOTE, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
    }*/

    /*public void addReminder(Reminder reminder){
        for(int x = 0;x < getReminders().size();x++){
            Reminder tempReminder = getReminders().get(x);
            if((reminder.getDrug().getDrugName().equalsIgnoreCase(tempReminder.getDrug().getDrugName()) || reminder.getDrugClass().getDrugClassName().equalsIgnoreCase(tempReminder.getDrugClass().getDrugClassName())) && reminder.getText().equalsIgnoreCase(tempReminder.getText()) && reminder.getTitle().equalsIgnoreCase(tempReminder.getTitle())){
                return;
            }
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if(reminder.getDrug() != null){
            values.put(COLUMN_REMINDER_DRUGNAME, reminder.getDrug().getDrugName());
        }
        if(reminder.getDrugClass() != null) {
            values.put(COLUMN_REMINDER_DRUGCLASSNAME, reminder.getDrugClass().getDrugClassName());
        }
        values.put(COLUMN_REMINDER_TITLE, reminder.getTitle());
        values.put(COLUMN_REMINDER_TEXT, reminder.getText());
        values.put(COLUMN_REMINDER_TIME, String.valueOf(reminder.getTime()));
        values.put(COLUMN_REMINDER_TIMEFOR, String.valueOf(reminder.getTimeFor()));

        db.insert(TABLE_REMINDER, null, values);

        db.close();
    }*/

  /*  public void addMetricConversion(MetricConversion metricConversion){
        for(int x = 0;x < getMetricConversions().size();x++){
            MetricConversion tempMetricConversion = getMetricConversions().get(x);
            Metric tempMetricToConvert = tempMetricConversion.getMetricToConvert();
            Metric tempMetricToConvertTo = tempMetricConversion.getMetricToConvertTo();

            if(metricConversion.getMetricToConvert().getMetricName().equalsIgnoreCase(tempMetricToConvert.getMetricName()) && metricConversion.getMetricToConvertTo().getMetricName().equalsIgnoreCase(tempMetricToConvertTo.getMetricName())){
                Log.i("testing", "DBHandler - This metric conversion already exists! Returning");
                return;
            }
        }

        SQLiteDatabase db = getWritableDatabase();
       // db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(COLUMN_METRICTOCONVERT, metricConversion.getMetricToConvert().getMetricName());
        values.put(COLUMN_METRICTOCONVERTTO, metricConversion.getMetricToConvertTo().getMetricName());
        values.put(COLUMN_CONVERSIONRATE, metricConversion.getConversionRate());

        db.insert(TABLE_METRIC_CONVERSION, null, values);
      //  Log.i("testing", "DBHandler - Metric conversion inserted into the database");
      //  db.setTransactionSuccessful();
      //  db.endTransaction();
        db.close();
    }*/


    /*public void addDrugToClass(DrugClass drugClass, Drug drug){
      drugClass.addDrug(drug);
    }*/

    //delete product from the database
    public void deleteDrug(String drugName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DRUG + " WHERE " + COLUMN_DRUGNAME + "=\"" + drugName + "\";");
    }
    public void deleteDrugClass(String drugClassName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DRUGCLASS + " WHERE " + COLUMN_DRUGCLASSNAME + "=\"" + drugClassName + "\";");
    }
    public void deleteDose(long doseTime){
        String strLong = Long.toString(doseTime);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DOSE + " WHERE " + COLUMN_DATE + "=\"" + strLong + "\";");
    }
    public void deleteMetric(String metricName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_METRIC + " WHERE " + COLUMN_METRICNAME + "=\"" + findMetric(metricName) + "\";");
    }
    public void deleteRoa(String roaName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ROA + " WHERE " + COLUMN_ROANAME  + "=\"" + roaName + "\";");
    }

    //print out database as a string
   /* public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        //              select all column               select all row
        String query = "SELECT * FROM " + TABLE_DRUG + "WHERE 1";

        //Cursoor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //move to the first row in your results
        c.moveToFirst();

        //loops through each row in database
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("drugName")) != null){
                dbString += c.getString(c.getColumnIndex("drugName"));
                dbString += "\n";
            }
        }

        db.close();
        return dbString;
    }*/

  /*  public ArrayList<Drug> getDrugs(){
       // String dbString = "";
        ArrayList<Drug> listOfDrugs = new ArrayList<Drug>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUG + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        //moves the cursor/selection to the first row in the drugs table



        c.moveToFirst();

        while(!c.isAfterLast()){
            String drugName = "";
            ArrayList<String> drugNickNames = new ArrayList<String>();
            int imageId = R.drawable.lol;
            String erowidLink = "";
            String wikiLink = "";
            String redditLink = "";

            Drug drugToAdd = new Drug("", imageId);

            // Drug drugToAdd = new Drug(null);
            if(c.getString(c.getColumnIndex("drugName")) != null){
               // dbString += c.getString(c.getColumnIndex("m_drugName"));
               // dbString += "\n";
                drugName = c.getString(c.getColumnIndex("drugName"));
                drugToAdd.setDrugName(drugName);
            }
            if(c.getString(c.getColumnIndex("drugNickNames")) != null){
               // dbString += c.getString(c.getColumnIndex("drugNicknames"));
               // dbString += "\n";
                drugNickNames = new ArrayList<String>(Arrays.asList(c.getString(c.getColumnIndex("drugNickNames")).split(" , ")));
                drugToAdd.setDrugNicknames(drugNickNames);
            }
            if(c.getString(c.getColumnIndex("drugImageId")) != null){
                 imageId = c.getInt(c.getColumnIndex("drugImageId"));
                drugToAdd.setImageId(imageId);
            }
            if(c.getString(c.getColumnIndex("erowidLink")) != null){
                erowidLink = c.getString(c.getColumnIndex("erowidLink"));
                drugToAdd.setErowidLink(erowidLink);
            }
            if(c.getString(c.getColumnIndex("wikiLink")) != null){
                wikiLink = c.getString(c.getColumnIndex("wikiLink"));
                drugToAdd.setWikiLink(wikiLink);
            }
            if(c.getString(c.getColumnIndex("redditLink")) != null){
                redditLink = c.getString(c.getColumnIndex("redditLink"));
                drugToAdd.setRedditLink(redditLink);
            }
            //Drug drugToAdd = new Drug();

            if(!drugName.equalsIgnoreCase("")){
                listOfDrugs.add(drugToAdd);
            }
            //moves the cursor/selection to the next cursor in the drug table
            c.moveToNext();
        }

        db.close();
        c.close();
        return listOfDrugs;
    }*/

   /* public ArrayList<DrugClass> getDrugClasses(){
       // String dbString = "";
        ArrayList<DrugClass> drugClasses = new ArrayList<DrugClass>();


        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGCLASS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){

            String drugClassName = "";
            ArrayList<Drug> drugsInClass = new ArrayList<Drug>();
            ArrayList<DrugClass> badCombinationList = new ArrayList<DrugClass>();
            int imageId = R.drawable.ic_drugclassdefault;

            DrugClass newDrugClass = new DrugClass(drugClassName, drugsInClass, badCombinationList, imageId);
            if(c.getString(c.getColumnIndex("drugClassName")) != null){
                drugClassName = c.getString(c.getColumnIndex("drugClassName"));
                newDrugClass.setDrugClassName(drugClassName);
            }
            if(c.getString(c.getColumnIndex("drugList")) != null){
                ArrayList<String> drugNames = new ArrayList<String>(Arrays.asList(c.getString(c.getColumnIndex("drugList")).split(" , ")));
                //ArrayList<String> drugIds = new ArrayList<String>(Arrays.asList(c.getString(c.getColumnIndex("drugList")).split(" , ")));
                for(int x = 0;x < drugNames.size();x++){
                    drugsInClass.add(findDrug(drugNames.get(x)));
                }
            }
            if(c.getString(c.getColumnIndex("drugClassImageId")) != null){
                imageId = c.getInt(c.getColumnIndex("drugClassImageId"));
                newDrugClass.setImageId(imageId);
            }
            if(c.getString(c.getColumnIndex("badCombinationList")) != null){
                ArrayList<String> badCombinationNames = new ArrayList<String>(Arrays.asList(c.getString(c.getColumnIndex("badCombinationList")).split(" , ")));
              //  ArrayList<String> ids = new ArrayList<String>(Arrays.asList(c.getString(c.getColumnIndex("badCombinationList")).split(" , ")));
                for(int x = 0;x < badCombinationNames.size();x++){
                    badCombinationList.add(findDrugClass(badCombinationNames.get(x)));
                }
            }
            if(!newDrugClass.getDrugClassName().equalsIgnoreCase("")){
                drugClasses.add(newDrugClass);
            }
            c.moveToNext();
        }

        db.close();
        c.close();
        return drugClasses;
    }*/

   /* public ArrayList<Metric> getMetrics(){
        ArrayList<Metric> metrics = new ArrayList<Metric>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_METRIC + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        //Cursor c = db.query(TABLE_METRIC, metricColumns, " metricName");
        c.moveToFirst();
        while(!c.isAfterLast()){

            String metricName = "";

            Metric metric = new Metric(metricName);
            if(c.getString(c.getColumnIndex("metricName")) != null){

                metric.setMetricName(c.getString(c.getColumnIndex("metricName")));
                metricName = c.getString(c.getColumnIndex("metricName"));
            }
            if(c.getString(c.getColumnIndex("metricDrug")) != null){
               // metric.setMetricDrug(findDrug(c.getString(c.getColumnIndex("metricDrug"))));
            }
            if(!metricName.equalsIgnoreCase("")){
                metrics.add(metric);
            }
            c.moveToNext();
        }

        db.close();
        c.close();
        return metrics;
    }*/
   /* public ArrayList<RouteOfAdministration> getRoas(){
        ArrayList<RouteOfAdministration> roas = new ArrayList<RouteOfAdministration>();
        ArrayList<String> roaNames = new ArrayList<String>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ROA + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){

            String roaName= "";
            RouteOfAdministration roa = new RouteOfAdministration(roaName);

            if(c.getString(c.getColumnIndex("roaName")) != null){
               // roaNames.add(c.getString(c.getColumnIndex("roaName")));
              // ArrayList<String> roaNames = new ArrayList<String>(Arrays.asList(c.getString(c.getColumnIndex("roaName")).split(" , ")));
                roaName = c.getString(c.getColumnIndex("roaName"));
                roa.setRoaName(roaName);

            }

            if(!roaName.equalsIgnoreCase("")){
                roas.add(roa);
            }

            c.moveToNext();
        }

        for(int x = 0; x < roaNames.size();x++){
            if(findRoa(roaNames.get(x)) != null){
                RouteOfAdministration roa = new RouteOfAdministration(roaNames.get(x));
                roas.add(findRoa(roaNames.get(x)));
            }
        }

        db.close();
        c.close();
        return roas;
    }*/

    /*public ArrayList<Dose> getDoses(){
        ArrayList<Dose> doses = new ArrayList<Dose>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DOSE + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

       // Dose newDose = null;


        while(!c.isAfterLast()){
            //Drug drugUsed;
            String drugUsedName = "";
            Drug drugUsed = findDrug(drugUsedName);
            Metric metricUsed;
            RouteOfAdministration roaUsed;
            double amountUsed;
            String dateUsed;

            Dose newDose = new Dose(findDrug(""), findMetric(""), findRoa(""), 0.000, "");
            //Dose newDose = null;
            // Dose newDose = new Dose(null,null,null,0.000,null);

            if(c.getString(c.getColumnIndex("drugUsed")) != null){
                if(findDrug(c.getString(c.getColumnIndex("drugUsed"))) != null){
                    drugUsed = findDrug(c.getString(c.getColumnIndex("drugUsed")));
                    newDose.setDrug(drugUsed);
                }
            }
            if(c.getString(c.getColumnIndex("metricUsed")) != null){
                if(findMetric(c.getString(c.getColumnIndex("metricUsed"))) != null){
                    metricUsed = findMetric(c.getString(c.getColumnIndex("metricUsed")));
                    newDose.setMetric(metricUsed);
                }
            }
            if(c.getString(c.getColumnIndex("roaUsed")) != null){
                if(findRoa(c.getString(c.getColumnIndex("roaUsed"))) != null){
                    roaUsed = findRoa(c.getString(c.getColumnIndex("roaUsed")));
                    newDose.setRoa(roaUsed);
                }
            }
            if(c.getString(c.getColumnIndex("doseAmount")) != null){
                newDose.setDoseAmount(c.getDouble(c.getColumnIndex("doseAmount")));
            }
            if(c.getString(c.getColumnIndex("dateUsed")) != null){
                dateUsed = c.getString(c.getColumnIndex("dateUsed"));
                newDose.setDate(dateUsed);
            }
            if(!newDose.getDrug().getDrugName().equalsIgnoreCase("")
                //  && !newDose.getDate().equalsIgnoreCase("")
                    ){
                doses.add(newDose);
            }
            c.moveToNext();
        }
        db.close();
        c.close();
        return doses;
    }*/

   /* public ArrayList<Note> getNotes(){
        ArrayList<Note> notes = new ArrayList<Note>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NOTE + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){

            String doseDrugUsed = "";
            String doseRoaUsed = "";
            String doseDate = "";
            String noteTitle = "";
            String noteText = "";
            String noteTime = "";

            Note note = new Note(findDose(doseDrugUsed, doseRoaUsed, doseDate), noteTitle, noteText, noteTime);

            if(c.getString(c.getColumnIndex("doseDrugUsed")) != null){
               doseDrugUsed = c.getString(c.getColumnIndex("doseDrugUsed"));
            }
            if(c.getString(c.getColumnIndex("doseRoaUsed")) != null){
                doseRoaUsed = c.getString(c.getColumnIndex("doseRoaUsed"));
            }
            if(c.getString(c.getColumnIndex("doseDate")) != null){
                doseDate = c.getString(c.getColumnIndex("doseDate"));
            }
                note.setNoteDose(findDose(doseDrugUsed, doseRoaUsed, doseDate));

            if(c.getString(c.getColumnIndex("noteTitle")) != null){
                noteTitle = c.getString(c.getColumnIndex("noteTitle"));
                note.setNoteTitle(noteTitle);
            }
            if(c.getString(c.getColumnIndex("noteText")) != null){
                noteText = c.getString(c.getColumnIndex("noteText"));
                note.setNoteText(noteText);
            }
            if(c.getString(c.getColumnIndex("noteTime")) != null){
                noteTime = c.getString(c.getColumnIndex("noteTime"));
                note.setNoteTime(noteTime);
            }
            if(note.getNoteDose() != null && !note.getNoteDose().getDate().equalsIgnoreCase("")){
                notes.add(note);
            }

            c.moveToNext();
        }

        db.close();
        c.close();
        return notes;
    }*/

  /*  public ArrayList<Reminder> getReminders(){
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_REMINDER + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            String drugName = "";
            String drugClassName = "";
            String title = "";
            String text = "";
            int time = 0;
            int timeFor = 0;

            Reminder reminder = new Reminder(findDrug(drugName), findDrugClass(drugClassName), title, text, time, timeFor);

            if(c.getString(c.getColumnIndex("reminderDrugName")) != null){
                drugName = c.getString(c.getColumnIndex("reminderDrugName"));
                reminder.setDrug(findDrug(drugName));
            }
            if(c.getString(c.getColumnIndex("reminderDrugClassName")) != null){
                drugClassName = c.getString(c.getColumnIndex("reminderDrugClassName"));
                reminder.setDrugClass(findDrugClass(drugClassName));
            }
            if(c.getString(c.getColumnIndex("reminderTitle")) != null){
                title = c.getString(c.getColumnIndex("reminderTitle"));
                reminder.setTitle(title);
            }
            if(c.getString(c.getColumnIndex("reminderText")) != null){
                text = c.getString(c.getColumnIndex("reminderText"));
                reminder.setText(text);
            }
            if(c.getString(c.getColumnIndex("reminderTime")) != null){
                time = Integer.valueOf(c.getString(c.getColumnIndex("reminderTime")));
                reminder.setTime(time);
            }
            if(c.getString(c.getColumnIndex("reminderTimeFor")) != null){
                timeFor = Integer.valueOf(c.getString(c.getColumnIndex("reminderTimeFor")));
                reminder.setTimeFor(timeFor);
            }

            if((!drugName.equalsIgnoreCase("") || !drugClassName.equalsIgnoreCase("")) && !title.equalsIgnoreCase("") && !text.equalsIgnoreCase("")){
                reminders.add(reminder);
            }

            c.moveToNext();
        }
        db.close();
        c.close();
        return reminders;
    }*/

    /*public ArrayList<MetricConversion> getMetricConversions(){

        ArrayList<MetricConversion> metricConversions = new ArrayList<MetricConversion>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_METRIC_CONVERSION + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            String metricToConvert = "";
            String metricToConvertTo = "";
            double conversionRate = 0;


            if(c.getString(c.getColumnIndex("metricToConvert")) != null && c.getString(c.getColumnIndex("metricToConvertTo")) != null && c.getString(c.getColumnIndex("conversionRate")) != null){

                metricToConvert = c.getString(c.getColumnIndex("metricToConvert"));
                metricToConvertTo = c.getString(c.getColumnIndex("metricToConvertTo"));
                conversionRate = c.getDouble(c.getColumnIndex("conversionRate"));

                MetricConversion metricConversion = new MetricConversion(findMetric(metricToConvert), findMetric(metricToConvertTo), conversionRate);

                if(!metricToConvert.equalsIgnoreCase("") && !metricToConvertTo.equalsIgnoreCase("") && conversionRate != 0){
                    metricConversions.add(metricConversion);
                }
            }

            c.moveToNext();
        }
        db.close();
        c.close();
        return metricConversions;
    }*/


      /*  public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }*/
   /* public Drug findDrug(String drugName){
        //Drug drugToAdd = null;
        Log.i("testing","finding drug");

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUG + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){

            ArrayList<String> drugNickNames = new ArrayList<String>();
            int imageId = R.drawable.lol;
            String erowidLink = "";
            String wikiLink = "";
            String redditLink = "";



            if(drugName.equalsIgnoreCase(c.getString(c.getColumnIndex("drugName")))){
                Drug drugToAdd = new Drug(drugName, imageId);
                if(c.getString(c.getColumnIndex("drugName")) != null){
                    // dbString += c.getString(c.getColumnIndex("m_drugName"));
                    // dbString += "\n";
                    drugName = c.getString(c.getColumnIndex("drugName"));
                    drugToAdd.setDrugName(drugName);
                }
                if(c.getString(c.getColumnIndex("drugNickNames")) != null){
                    // dbString += c.getString(c.getColumnIndex("drugNicknames"));
                    // dbString += "\n";
                    drugNickNames = new ArrayList<String>(Arrays.asList(c.getString(c.getColumnIndex("drugNickNames")).split(" , ")));
                    drugToAdd.setDrugNicknames(drugNickNames);
                }
                if(c.getString(c.getColumnIndex("drugImageId")) != null){
                    imageId = c.getInt(c.getColumnIndex("drugImageId"));
                    drugToAdd.setImageId(imageId);
                }
                if(c.getString(c.getColumnIndex("erowidLink")) != null){
                    erowidLink = c.getString(c.getColumnIndex("erowidLink"));
                    drugToAdd.setErowidLink(erowidLink);
                }
                if(c.getString(c.getColumnIndex("wikiLink")) != null){
                    wikiLink = c.getString(c.getColumnIndex("wikiLink"));
                    drugToAdd.setWikiLink(wikiLink);
                }
                if(c.getString(c.getColumnIndex("redditLink")) != null){
                    redditLink = c.getString(c.getColumnIndex("redditLink"));
                    drugToAdd.setRedditLink(redditLink);
                }
                if(drugToAdd.getDrugName() != null){
                    db.close();
                    c.close();
                    return drugToAdd;
                }
            }
            c.moveToNext();
        }
        db.close();
        c.close();
        return null;
    }*/

   /* public DrugClass findDrugClass(String drugClassName){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGCLASS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        ArrayList<Drug> listOfDrugs =  new ArrayList<Drug>();
        int imageId = 0;

       // DrugClass drugClass = new DrugClass(drugClassName, listOfDrugs, imageId);
      while(!c.isAfterLast()){

          if(drugClassName.equalsIgnoreCase(c.getString(c.getColumnIndex("drugClassName")))){
              if(c.getString(c.getColumnIndex("drugList")) != null){
                  ArrayList<String> drugNames = new ArrayList<String>(Arrays.asList(c.getString(c.getColumnIndex("drugList")).split(" , ")));
                  for(int x = 0;x < drugNames.size();x++){
                      listOfDrugs.add(findDrug(drugNames.get(x)));
                  }
              }
              if(c.getString(c.getColumnIndex("drugClassImageId")) != null){
                  imageId = Integer.valueOf(c.getString(c.getColumnIndex("drugClassImageId")));
              }
              DrugClass drugClass = new DrugClass(drugClassName, listOfDrugs, imageId);
              db.close();
              c.close();
              return drugClass;
          }

          c.moveToNext();
      }
        db.close();
        c.close();
        return null;
    }*/

   /* public Metric findMetric(String metricName){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_METRIC + " WHERE 1";
        Cursor c = db.rawQuery(query, null);


        c.moveToFirst();
        while(!c.isAfterLast()) {
            Metric metric = new Metric(metricName);
            if (metricName.equalsIgnoreCase(c.getString(c.getColumnIndex("metricName")))) {
                metric.setMetricName(metricName);
                if (c.getString(c.getColumnIndex("metricDrug")) != null) {
                    metric.setMetricDrug(findDrug(c.getString(c.getColumnIndex("metricDrug"))));
                }

                if (metric != null) {
                    db.close();
                    c.close();
                    return metric;
                }
            }
            c.moveToNext();
        }
        db.close();
        c.close();
        return null;
    }*/

   /* public RouteOfAdministration findRoa(String roaName){


        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ROA + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){


            if(roaName.equalsIgnoreCase(c.getString(c.getColumnIndex("roaName")))){
              //  roa.setRoaName(roaName);
                RouteOfAdministration roa = new RouteOfAdministration(roaName);
                db.close();
                c.close();
                return roa;
            }




            c.moveToNext();
        }
        db.close();
        c.close();
        return null;
    }*/

    public Dose findDose(String drugUsed, String roaUsed, String date){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DOSE + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){

            if(drugUsed.equalsIgnoreCase(c.getString(c.getColumnIndex("drugUsed"))) && roaUsed.equalsIgnoreCase(c.getString(c.getColumnIndex("roaUsed"))) && date.equalsIgnoreCase(c.getString(c.getColumnIndex("dateUsed")))){
                Dose dose = new Dose(findDrug(drugUsed), findMetric(c.getString(c.getColumnIndex("metricUsed"))), findRoa(roaUsed), Double.valueOf(c.getString(c.getColumnIndex("doseAmount"))), date);
                db.close();
                c.close();
                return dose;
            }

           /* if(drugUsed.equalsIgnoreCase(c.getString(c.getColumnIndex("drugUsed"))) && roaUsed.equalsIgnoreCase(c.getString(c.getColumnIndex("roaUsed"))) && date.equalsIgnoreCase(c.getString(c.getColumnIndex("dateUsed")))){
                Dose dose = new Dose(findDrug(drugUsed), findMetric(c.getString(c.getColumnIndex("metricUsed"))), findRoa(roaUsed), Double.valueOf(c.getString(c.getColumnIndex("doseAmount"))), date);
                db.close();
                return dose;
            }*/
            c.moveToNext();
        }

        db.close();
        c.close();
        return null;
    }

    //TESTING SHIT OUT NIGGA
    public synchronized Drug getDrug(final int id){
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_DRUG, Drug.FIELDS, Drug.COLUMN_ID + " IS ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor == null || cursor.isAfterLast()){
            return null;
        }

        Drug drug = null;
        if(cursor.moveToFirst()){
            drug = new Drug(cursor);
        }
        cursor.close();

        return drug;
    }

    public Drug findDrug(String drugName){
        Log.i("testing","Finding drug");
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_DRUG, Drug.FIELDS, Drug.COLUMN_DRUGNAME + " IS ?", new String[]{drugName}, null, null, null, null);

        if(cursor == null || cursor.isAfterLast()){
            return null;
        }
        Drug drug = null;
        if(cursor.moveToFirst()){
            drug = new Drug(cursor);
        }
        cursor.close();
        return drug;
    }

    public DrugClass findDrugClass(String drugClassName){
        Log.i("testing","Finding Drug Class");
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_DRUGCLASS, DrugClass.FIELDS, DrugClass.COLUMN_DRUGCLASSNAME + " IS ?", new String[]{drugClassName}, null, null, null, null);

        if(cursor == null || cursor.isAfterLast()){
            return null;
        }

        DrugClass drugClass = null;
        if(cursor.moveToFirst()){
            drugClass = new DrugClass(cursor);
        }
        return drugClass;
    }

    public RouteOfAdministration findRoa(String roaName){
        Log.i("testing","Finding Roa");
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_ROA, RouteOfAdministration.FIELDS, RouteOfAdministration.COLUMN_ROANAME + " IS ?", new String[]{roaName}, null, null, null, null);

        if(cursor == null || cursor.isAfterLast()){
            return null;
        }
        RouteOfAdministration roa = null;
        if(cursor.moveToFirst()){
            roa = new RouteOfAdministration(cursor);
        }
        return roa;
    }

    public Metric findMetric(String metricName){
        Log.i("testing","Finding Metric");
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_METRIC, Metric.FIELDS, Metric.COLUMN_METRICNAME + " IS ?", new String[]{metricName}, null, null, null, null);

        if(cursor == null || cursor.isAfterLast()){
            return null;
        }
        Metric metric = null;
        if(cursor.moveToFirst()){
            metric = new Metric(cursor);
        }
        return metric;
    }

    public Dose getDose(int id){
        Log.i("testing","Finding Dose with id " + id);
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_DOSE, Dose.FIELDS, Dose.COLUMN_DOSEID + " IS ?", new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor == null || cursor.isAfterLast()){
            return null;
        }
        Dose dose = null;
        if(cursor.moveToFirst()){
            dose = new Dose(cursor);
        }
        return dose;
    }

    public Note getNote(int id){
        Log.i("testing","Getting Note with id " + id);
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_NOTE, Note.FIELDS, Note.COLUMN_NOTEID + " IS ?", new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor == null || cursor.isAfterLast()){
            return null;
        }
        Note note = null;
        if(cursor.moveToFirst()){
            note = new Note(cursor);
        }
        return note;
    }

    public Reminder getReminder(int id){
        Log.i("testing","Getting Reminder with id " + id);
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_REMINDER, Reminder.FIELDS, Reminder.COLUMN_REMINDERID + " IS ?", new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor == null || cursor.isAfterLast()){
            return null;
        }
        Reminder reminder = null;
        if(cursor.moveToFirst()){
            reminder = new Reminder(cursor);
        }
        return reminder;
    }

  //  public MetricConversion getMetricConversion(){
     //
  //  }

    public Notification getNotification(int id){
        Log.i("testing","Getting Notification with id " + id);
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(Notification.TABLE_NAME,Notification.FIELDS,Notification.COLUMN_ID + " IS ?",new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor == null || cursor.isAfterLast()){
            return null;
        }
        Notification notification = null;
        if(cursor.moveToFirst()){
            notification = new Notification(cursor);
        }
        return notification;
    }

    public synchronized  ArrayList<Drug> getDrugs(){
        Cursor c = getWritableDatabase().query(TABLE_DRUG, Drug.FIELDS, null, null, null, null, null, null);
        ArrayList<Drug> drugs = new ArrayList<Drug>();
        if(c != null){
            while(c.moveToNext()){
                Drug drug = new Drug(c);
                drugs.add(drug);
            }
        }
        ArrayList<String> drugNames = new ArrayList<String>();
        for(int x = 0;x < drugs.size();x++){
            drugNames.add(drugs.get(x).getDrugName());
        }
        Collections.sort(drugNames,String.CASE_INSENSITIVE_ORDER);
        for(int x = 0;x < drugs.size();x++){
            drugs.set(x,findDrug(drugNames.get(x)));
        }

        c.close();
        return drugs;
    }

    public synchronized ArrayList<DrugClass> getDrugClasses(){
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_DRUGCLASS, DrugClass.FIELDS, null, null, null, null, null, null);
        ArrayList<DrugClass> drugClasses = new ArrayList<DrugClass>();

        if(cursor != null){
            while(cursor.moveToNext()){
                DrugClass drugClass = new DrugClass(cursor);
                drugClasses.add(drugClass);
            }
        }

        ArrayList<String> classNames = new ArrayList<String>();
        for(int x = 0;x < drugClasses.size();x++){
            classNames.add(drugClasses.get(x).getDrugClassName());
        }
        Collections.sort(classNames,String.CASE_INSENSITIVE_ORDER);
        for(int x = 0;x < drugClasses.size();x++){
            DrugClass drugClass = findDrugClass(classNames.get(x));
            drugClasses.set(x,drugClass);
        }
        cursor.close();
        return drugClasses;
    }

    public synchronized  ArrayList<RouteOfAdministration> getRoas(){
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_ROA, RouteOfAdministration.FIELDS, null, null, null, null, null, null);
        ArrayList<RouteOfAdministration> roas = new ArrayList<RouteOfAdministration>();

        if(cursor != null){
            while(cursor.moveToNext()){
                RouteOfAdministration roa = new RouteOfAdministration(cursor);
                roas.add(roa);
            }
        }
        return roas;
    }

    public synchronized ArrayList<Metric> getMetrics(){
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_METRIC, Metric.FIELDS, null, null, null, null, null, null);
        ArrayList<Metric> metrics = new ArrayList<Metric>();

        if(cursor != null){
            while(cursor.moveToNext()){
                Metric metric = new Metric(cursor);
                metrics.add(metric);
            }
        }
        return metrics;
    }

    public synchronized ArrayList<Dose> getDoses(){
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_DOSE, Dose.FIELDS, null, null, null, null, null, null);
        ArrayList<Dose> doses = new ArrayList<Dose>();

        if(cursor != null){
            while(cursor.moveToNext()){
                Dose dose = new Dose(cursor);
                doses.add(dose);
            }
        }
        //sort the doses by date

        return doses;
    }

    public synchronized ArrayList<Note> getNotes(){
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_NOTE, Note.FIELDS, null, null, null, null, null, null);
        ArrayList<Note> notes = new ArrayList<Note>();

        if(cursor != null){
            while(cursor.moveToNext()){
                Note note = new Note(cursor);
                notes.add(note);
            }
        }
        return notes;
    }

    public synchronized ArrayList<Reminder> getReminders(){
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_REMINDER, Reminder.FIELDS, null, null, null, null, null, null);
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();

        if(cursor != null){
            while(cursor.moveToNext()){
                Reminder reminder = new Reminder(cursor);
                reminders.add(reminder);
            }
        }
        return reminders;
    }

    public synchronized ArrayList<MetricConversion> getMetricConversions(){
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(TABLE_METRIC_CONVERSION, MetricConversion.FIELDS, null, null, null, null, null, null);
        ArrayList<MetricConversion> metricConversions = new ArrayList<MetricConversion>();

        if(cursor != null){
            while(cursor.moveToNext()){
                MetricConversion metricConversion = new MetricConversion(cursor);
                metricConversions.add(metricConversion);
            }
        }
        return metricConversions;
    }

    public synchronized ArrayList<Notification> getNotifications(){
        final SQLiteDatabase db = getWritableDatabase();
        final Cursor cursor = db.query(Notification.TABLE_NAME, Notification.FIELDS, null, null, null, null, null, null);
        ArrayList<Notification> notifications = new ArrayList<Notification>();

        if(cursor != null){
            while(cursor.moveToNext()){
                Notification notification = new Notification(cursor);
                notifications.add(notification);
            }
        }
        return notifications;
    }

    public void updateDrug(){

    }

    public ContentValues drugValues(Drug drug){

        ContentValues values = new ContentValues();
        values.put(COLUMN_DRUGNAME, drug.getDrugName());
        String nicknames = "";
        //converting nicknames ArrayList<String> to String
        if(drug.getDrugNickNames() != null){
            for(int x = 0;x<drug.getDrugNickNames().size();x++){
                String tempNickName = drug.getDrugNickNames().get(x);
                if(x == (drug.getDrugNickNames().size() - 1)){
                    nicknames += tempNickName;
                } else {
                    nicknames += tempNickName + " , ";
                }
            }
            values.put(COLUMN_DRUGNICKNAMES, nicknames);
        }
        if(drug.getImageId() != null){
            values.put(COLUMN_DRUGIMAGEID, drug.getImageId());
        }


        // if(drug.getErowidLink() != ""){
        values.put(COLUMN_EROWIDLINK, drug.getErowidLink());
        // }
        /// if(drug.getWikiLink() != ""){
        values.put(COLUMN_WIKILINK, drug.getWikiLink());
        // }
        //  if(drug.getRedditLink() != ""){
        values.put(COLUMN_REDDITLINK, drug.getRedditLink());
        // }
        return values;
    }


   public synchronized  boolean addDrug(final Drug drug){
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        if(drug.getDrugId() > -1){
            result += db.update(TABLE_DRUG, drug.getValues(), COLUMN_ID + " IS ?", new String[]{String.valueOf(drug.getDrugId())});
        }

        if(result > 0){
            success = true;
            Log.i("testing","Drug updated in DB");
        } else {
            Log.i("testing","Drug inserted into DB");
            final long id = db.insert(TABLE_DRUG, null,drug.getValues());
            if(id > -1){
                drug.setDrugId(id);
                success = true;
            }
        }

      //  if(success){
      //      notifyProviderOnChange();
      //  }

        return success;
    }

    public synchronized  boolean addDrugClass(final DrugClass drugClass){
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = getWritableDatabase();

        if(drugClass.getClassId() > -1){
            result += db.update(TABLE_DRUGCLASS, drugClass.getValues(), DrugClass.COLUMN_DRUGCLASSID + " IS ?", new String[]{String.valueOf(drugClass.getClassId())});
        }

        if(result > 0){
            success = true;
            Log.i("testing","DrugClass updated in DB");
        } else {
            Log.i("testing","DrugClass inserted into Database");
            final long id = db.insert(TABLE_DRUGCLASS,null,drugClass.getValues());
            if(id > -1){
                drugClass.setClassId(id);
                success = true;
            }
        }

        return success;
    }

    public synchronized boolean addRoa(final RouteOfAdministration roa){
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = getWritableDatabase();

        if(roa.getId() > -1){
            result += db.update(TABLE_ROA, roa.getValues(), RouteOfAdministration.COLUMN_ROAID + " IS ?", new String[]{String.valueOf(roa.getId())});
        }

        if(result > 0){
            success = true;
            Log.i("testing","Updated Roa in DB");
        } else {
            Log.i("testing","Inserted Roa into DB");
            final long id = db.insert(TABLE_ROA,null,roa.getValues());
            if(id > -1){
                roa.setId(id);
                success = true;
            }
        }
        return success;
    }

    public synchronized boolean addMetric(final Metric metric){
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = getWritableDatabase();

        if(metric.getId() > -1){
            result += db.update(TABLE_METRIC,metric.getValues(),Metric.COLUMN_METRICID + " IS ?", new String[]{String.valueOf(metric.getId())});
        }

        if(result > 0){
            success = true;
            Log.i("testing","Updated Metric in DB");
        } else {
            Log.i("testing","Inserted Metric into DB");
            final long id = db.insert(TABLE_METRIC,null,metric.getValues());
            if(id > -1){
                metric.setId(id);
                success = true;
            }
        }
        return success;
    }

    public synchronized boolean addDose(final Dose dose){
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = getWritableDatabase();

        if(dose.getId() > -1){
            result += db.update(TABLE_DOSE,dose.getValues(),Dose.COLUMN_DOSEID + " IS ?",new String[]{String.valueOf(dose.getId())});
        }

        if(result > 0){
            success = true;
            Log.i("testing","Updated Dose in DB");
        } else {
            Log.i("testing","Inserted Dose into DB");
            final long id = db.insert(TABLE_DOSE,null,dose.getValues());
            if(id > -1){
                dose.setId(id);
                success = true;
            }
        }
        return success;
    }

    public synchronized boolean addNote(final Note note){
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = getWritableDatabase();

        if(note.getId() > -1){
            result += db.update(TABLE_NOTE,note.getValues(),Note.COLUMN_NOTEID + " IS ?",new String[]{String.valueOf(note.getId())});
        }

        if( result > 0){
            success = true;
            Log.i("testing","Updated Note in DB");
        } else {
            Log.i("testing","Inserted Note into DB");
            final long id = db.insert(TABLE_NOTE,null,note.getValues());
            if(id > -1){
                note.setId(id);
                success = true;
            }
        }
        return success;
    }

    public synchronized boolean addReminder(final Reminder reminder){
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = getWritableDatabase();

        if(reminder.getId() > -1){
            result += db.update(TABLE_REMINDER,reminder.getValues(),Reminder.COLUMN_REMINDERID + " IS ?",new String[]{String.valueOf(reminder.getId())});
        }

        if(result > 0){
            success = true;
            Log.i("testing","Updated Reminder in DB");
        } else {
            Log.i("testing","Inserted Reminder into DB");
            final long id = db.insert(TABLE_REMINDER,null,reminder.getValues());
            if(id > -1){
                reminder.setID(id);
                success = true;
            }
        }
        return success;
    }

    public synchronized boolean addMetricConversion(final MetricConversion metricConversion){
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = getWritableDatabase();

        if(metricConversion.getId() > -1){
            result += db.update(TABLE_METRIC_CONVERSION,metricConversion.getValues(),MetricConversion.COLUMN_METRICCONVERSION_ID + " IS ?",new String[]{String.valueOf(metricConversion.getId())});
        }

        if(result > 0){
            success = true;
            Log.i("testing","Updated Metric Conversion in DB");
        } else {
            Log.i("testing","Inserted Metric Conversion into DB");
            final long id = db.insert(TABLE_METRIC_CONVERSION,null,metricConversion.getValues());
            if(id > -1){
                metricConversion.setId(id);
                success = true;
            }
        }
        return success;
    }

    public synchronized boolean addNotification(final Notification notification){
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = getWritableDatabase();

        if(notification.getId() > -1){
            result += db.update(Notification.TABLE_NAME, notification.getValues(),Notification.COLUMN_ID + " IS ?",new String[]{String.valueOf(notification.getId())});
        }

        if(result > 0){
            success = true;
            Log.i("testing","Updated Notification in DB");
        } else {
            Log.i("testing","Inserted Notification into DB");
            final long id = db.insert(Notification.TABLE_NAME,null,notification.getValues());
            if(id > -1){
                notification.setId(id);
                success = true;
            }
        }
        return success;
    }

    public synchronized int removeDrug(final Drug drug){
        final SQLiteDatabase db = this.getWritableDatabase();
        final int result = db.delete(TABLE_DRUG, Drug.COLUMN_ID + " IS ?", new String[] { Long.toString(drug.getDrugId())});

        if (result > 0) {

        }
        return result;
    }

    public synchronized int removeDrugClass(final DrugClass drugClass){
        Log.i("testing","Removing DrugClass from DB");
        final SQLiteDatabase db = this.getWritableDatabase();
        final int result = db.delete(TABLE_DRUGCLASS, DrugClass.COLUMN_DRUGCLASSID + " IS ?", new String[] { Long.toString(drugClass.getClassId()) });

        if (result > 0) {

        }
        return result;
    }




   public  void notifyProviderOnChange(){
       context.getContentResolver().notifyChange(DrugProvider.URI_DRUGS,null,false);
   }

}
