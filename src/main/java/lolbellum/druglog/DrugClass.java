package lolbellum.druglog;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DrugClass {

    private DrugManager drugManager = Calendar.drugManager;

   // private Drug drugInClass = new Drug();
    private String m_drugClassName;
    private ArrayList<Drug> m_drugList = new ArrayList<Drug>();
    private ArrayList<DrugClass> m_badCombinationList = new ArrayList<DrugClass>();
    private long m_classId  = -1;
    private int m_imageId = R.drawable.ic_drugclassdefault;
   // private int m_drugInClassId;

    public static final String TABLE_DRUGCLASS = "drugclass";
    public static final String INDEX_DRUGCLASS = "drugClassIndex";
    public static final String COLUMN_DRUGCLASSID = "classId";
    public static final String COLUMN_DRUGCLASSNAME = "drugClassName";
    public static final String COLUMN_DRUGSINDRUGCLASS = "drugList";
    public static final String COLUMN_DRUGCLASSIMAGEID = "drugClassImageId";
    public static final String COLUMN_BADCOMBINATIONLIST = "badCombinationList";
    public static final String[] FIELDS = {COLUMN_DRUGCLASSID,COLUMN_DRUGCLASSNAME,COLUMN_DRUGSINDRUGCLASS,COLUMN_DRUGCLASSIMAGEID,COLUMN_BADCOMBINATIONLIST};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_DRUGCLASS + "(" +
            COLUMN_DRUGCLASSID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DRUGCLASSNAME + " TEXT, " +
            COLUMN_DRUGSINDRUGCLASS + " TEXT, " +
            COLUMN_BADCOMBINATIONLIST + " TEXT, " +
            COLUMN_DRUGCLASSIMAGEID + " INTEGER " +
            ");";

    public ContentValues getValues(){
        ArrayList<String> drugNames = new ArrayList<String>();
        for(int x = 0;x < m_drugList.size();x++){
            drugNames.add(m_drugList.get(x).getDrugName());
        }
        Collections.sort(drugNames,String.CASE_INSENSITIVE_ORDER);
        for(int x = 0;x < m_drugList.size();x++){
            m_drugList.set(x,drugManager.getDrugFromName(drugNames.get(x)));
        }
        String listOfDrugs = "";
        for(int x = 0; x < m_drugList.size();x++){
            Drug tempDrug = m_drugList.get(x);

            if(x < (m_drugList.size() - 1)) {
                listOfDrugs += tempDrug.getDrugName() + " , ";
            } else {
                listOfDrugs += tempDrug.getDrugName();
            }
        }


        ContentValues values = new ContentValues();
        values.put(COLUMN_DRUGCLASSNAME, m_drugClassName);
        //values.put(COLUMN_DRUGCLASSID, m_classId);
        values.put(COLUMN_DRUGSINDRUGCLASS, listOfDrugs);
     /*  String badCombinationList = "";
        if(m_badCombinationList.size() > 0) {

            for (int x = 0; x < m_badCombinationList.size(); x++) {
                DrugClass tempDrugClass = m_badCombinationList.get(x);
                Log.i("testing","tempBadCombinationClass for " + m_drugClassName + " is " + tempDrugClass.getDrugClassName());
                Log.i("testing","tempDrugClass(BAd Combination) has an Id of " + String.valueOf(tempDrugClass.getClassId()));

                if (x < (m_badCombinationList.size() - 1)) {
                    badCombinationList += String.valueOf(tempDrugClass.getClassId()) + " , ";
                } else {
                    badCombinationList += String.valueOf(tempDrugClass.getClassId());
                }
            }
        }
        Log.i("testing","badCombinationList is " + badCombinationList);
            values.put(COLUMN_BADCOMBINATIONLIST, badCombinationList);*/


        //   if(drugClass.getImageId() != null){
        values.put(COLUMN_DRUGCLASSIMAGEID, m_imageId);
        //  }
        return values;
    }

    public DrugClass(String drugClassName, ArrayList<Drug> drugList){
        m_drugClassName = drugClassName;
        m_drugList = drugList;
       drugManager.addDrugClass(this);
    }
    public DrugClass(String drugClassName, ArrayList<Drug> drugList, int imageId){
        m_drugClassName = drugClassName;
        m_drugList = drugList;
        m_imageId = imageId;
        drugManager.addDrugClass(this);
    }
    public DrugClass(String drugClassName, ArrayList<Drug> drugList, ArrayList<DrugClass> badCombinationList){
        m_drugClassName = drugClassName;
        m_drugList = drugList;
        m_badCombinationList = badCombinationList;
        drugManager.addDrugClass(this);
    }
    public DrugClass(String drugClassName, ArrayList<Drug> drugList, ArrayList<DrugClass> badCombinationList, int imageId){
        m_drugClassName = drugClassName;
        m_drugList = drugList;
        m_badCombinationList = badCombinationList;
        m_imageId = imageId;
        drugManager.addDrugClass(this);
    }

    public DrugClass(Cursor cursor){
        m_classId = cursor.getLong(0);
        m_drugClassName = cursor.getString(1);
        ArrayList<String> drugNames =  new ArrayList<String>(Arrays.asList(cursor.getString(2).split(" , ")));
        ArrayList<Drug> drugsInClass = new ArrayList<Drug>();
        for(int x = 0; x < drugNames.size();x++){
            drugsInClass.add(drugManager.getDrugFromName(drugNames.get(x)));
        }
        m_drugList = drugsInClass;
        ArrayList<String> badCombinationNames =  new ArrayList<String>(Arrays.asList(cursor.getString(3).split(" , ")));
        Log.i("testing","badCombinationsNames is " + cursor.getString(3));
        ArrayList<DrugClass> badCombinationList = new ArrayList<DrugClass>();
      //  for(int x = 0; x < badCombinationNames.size();x++){
           // badCombinationList.add(drugManager.getDrugClass(badCombinationNames.get(x)));
            //long tempId = Long.valueOf(badCombinationNames.get(x));
           // Log.i("testing","tempId is " + tempId);
           // DrugClass tempClass = drugManager.getDrugClass(tempId);
           // Log.i("testing","In DrugClass constructor(Cursor cursor) the badCombinationClass " + tempClass.getDrugClassName() + " is being added");
           // badCombinationList.add(tempClass);
      //  }
        m_badCombinationList = badCombinationList;
        m_imageId = cursor.getInt(4);

        drugManager.addDrugClass(this);
    }

    public String getDrugClassName(){
        return m_drugClassName;
    }

    public ArrayList<Drug> getDrugs(){
        return m_drugList;
    }

    public void setDrugClassName(String drugClassName){
        m_drugClassName = drugClassName;
    }

    public void addDrug(Drug drug){
        m_drugList.add(drug);
        drugManager.addDrugToClass(this, drug);
    }
    public void removeDrug(Drug drug){
        m_drugList.remove(drug);
        drugManager.removeDrugFromClass(this,drug);
    }

    public Integer getImageId(){
        return m_imageId;
    }
    public void setImageId(int imageId){
        m_imageId = imageId;
    }

    public Drug findDrugByName(String drugName){
        for(int x = 0; x < m_drugList.size(); x++){
            if(drugName.equalsIgnoreCase(m_drugList.get(x).getDrugName())){
                return m_drugList.get(x);
            }
        }

        return null;
    }

    public long getClassId(){
        return m_classId;
    }

    public void setClassId(long id){
        m_classId = id;
    }

    public ArrayList<DrugClass> getBadCombinationList(){
        return m_badCombinationList;
    }
    public void setBadCombinationList(ArrayList<DrugClass> badCombinationList){
        m_badCombinationList = badCombinationList;
    }

    public void alphabetizeClass(){

    }

    public void assignIds(){
        alphabetizeClass();
        for(int x = 0;x < m_drugList.size();x++){
            Drug tempDrug = m_drugList.get(x);
        }
    }

}
