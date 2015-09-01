package lolbellum.druglog;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class Drug {

    private DrugManager drugManager = Calendar.drugManager;


    private String m_drugName;
    private ArrayList<String> m_drugNicknames = new ArrayList<String>();
    private long m_drugId = -1;
    private Metric m_metric;
    private String m_imageSource = "";
    private int m_imageId = R.drawable.ic_drugdefault;
    private String m_imagePath = "";
    private String m_erowidLink = "";
    private String m_wikiLink = "";
    private String m_redditLink = "";

    public static final String TABLE_DRUG = "drug";
    public static final String INDEX_DRUG = "drugIndex";
    public static final String COLUMN_ID = "drugID";
    public static final String COLUMN_DRUGNAME = "drugName";
    public static final String COLUMN_DRUGNICKNAMES = "drugNickNames";
    public static final String COLUMN_DRUGIMAGEID = "drugImageId";
    public static final String COLUMN_DRUGIMAGEPATH = "drugImagePath";
    public static final String COLUMN_EROWIDLINK = "erowidLink";
    public static final String COLUMN_WIKILINK = "wikiLink";
    public static final String COLUMN_REDDITLINK = "redditLink";
    public static final String[] FIELDS = {COLUMN_ID, COLUMN_DRUGNAME, COLUMN_DRUGNICKNAMES, COLUMN_DRUGIMAGEID,COLUMN_DRUGIMAGEPATH,COLUMN_EROWIDLINK,COLUMN_WIKILINK,COLUMN_REDDITLINK};

   public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_DRUG + "(" +
            //LIST COLUMNS IN TABLE
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DRUGNAME + " TEXT, " +
            COLUMN_DRUGNICKNAMES + " TEXT, " +
            COLUMN_DRUGIMAGEID + " INTEGER, " +
            COLUMN_DRUGIMAGEPATH + " TEXT, " +
            COLUMN_EROWIDLINK + " TEXT, " +
            COLUMN_WIKILINK + " TEXT, " +
            COLUMN_REDDITLINK + " TEXT " +
            ");";

    public ContentValues getValues(){
        final ContentValues values = new ContentValues();
        values.put(COLUMN_DRUGNAME, m_drugName);
        String nicknames = "";
        for(int x = 0;x<m_drugNicknames.size();x++){
            String tempNickName = m_drugNicknames.get(x);
            if(x == (m_drugNicknames.size() - 1)){
                nicknames += tempNickName;
            } else {
                nicknames += tempNickName + " , ";
            }
        }
        values.put(COLUMN_DRUGNICKNAMES, nicknames);
        values.put(COLUMN_DRUGIMAGEID,m_imageId);
        values.put(COLUMN_DRUGIMAGEPATH,m_imagePath);
        values.put(COLUMN_EROWIDLINK,m_erowidLink);
        values.put(COLUMN_WIKILINK, m_wikiLink);
        values.put(COLUMN_REDDITLINK,m_redditLink);

        return values;
    }

    public Drug(String drugName){
        m_drugName = drugName;
        drugManager.addDrug(this);
    }

    public Drug(String drugName, int imageId){
        m_drugName = drugName;
        m_imageId = imageId;
        drugManager.addDrug(this);
    }

    public Drug(String drugName, ArrayList<String> drugNicknames){
        m_drugName = drugName;
        m_drugNicknames = drugNicknames;
        drugManager.addDrug(this);
    }

    public Drug(String drugName, int imageId, ArrayList<String> drugNicknames){
        m_drugName = drugName;
        m_imageId = imageId;
        m_drugNicknames = drugNicknames;
        drugManager.addDrug(this);
    }

    public Drug(String drugName, int imageId, ArrayList<String> drugNicknames, String erowidLink, String wikiLink, String redditLink){
        m_drugName = drugName;
        m_imageId = imageId;
        m_drugNicknames = drugNicknames;
        m_erowidLink = erowidLink;
        m_wikiLink = wikiLink;
        m_redditLink = redditLink;
        drugManager.addDrug(this);
    }

    public Drug(String drugName, int imageId, String imagePath, ArrayList<String> drugNicknames, String erowidLink, String wikiLink, String redditLink){
        m_drugName = drugName;
        m_imageId = imageId;
        m_imagePath = imagePath;
        m_drugNicknames = drugNicknames;
        m_erowidLink = erowidLink;
        m_wikiLink = wikiLink;
        m_redditLink = redditLink;
        drugManager.addDrug(this);
    }

    public Drug(String drugName, ArrayList<String> drugNicknames, String erowidLink, String wikiLink, String redditLink){
        m_drugName = drugName;
        m_drugNicknames = drugNicknames;
        m_erowidLink = erowidLink;
        m_wikiLink = wikiLink;
        m_redditLink = redditLink;
        drugManager.addDrug(this);
    }

    public Drug(String drugName, int imageId, String erowidLink, String wikiLink, String redditLink){
        m_drugName = drugName;
        m_imageId = imageId;
        m_imagePath = "android.resource://lolbellum.druglog/" + imageId;
        m_erowidLink = erowidLink;
        m_wikiLink = wikiLink;
        m_redditLink = redditLink;
        drugManager.addDrug(this);
    }

    public Drug(String drugName, String erowidLink, String wikiLink, String redditLink){
        m_drugName = drugName;
        m_erowidLink = erowidLink;
        m_wikiLink = wikiLink;
        m_redditLink = redditLink;
        drugManager.addDrug(this);
    }

    public Drug(String drugName, String imagePath){
        m_drugName = drugName;
        m_imagePath = imagePath;
        drugManager.addDrug(this);
    }
    public Drug(String drugName, String imagePath, ArrayList<String> nicknames){
        m_drugName = drugName;
        m_imagePath = imagePath;
        m_drugNicknames = nicknames;
        drugManager.addDrug(this);
    }

    public Drug(final Cursor cursor){
        m_drugId = cursor.getInt(0);
        m_drugName = cursor.getString(1);
        m_drugNicknames = new ArrayList<String>(Arrays.asList(cursor.getString(2).split(" , ")));
        m_imageId = cursor.getInt(3);
        m_imagePath = cursor.getString(4);
        m_erowidLink = cursor.getString(5);
        m_wikiLink = cursor.getString(6);
        m_redditLink = cursor.getString(7);
        drugManager.addDrug(this);
    }

    public ArrayList<String> getDrugNickNames(){
        return m_drugNicknames;
    }
    public void setDrugNicknames(ArrayList<String> drugNicknames){
        m_drugNicknames = drugNicknames;
    }

    public String getDrugName(){
        return m_drugName;
    }
    public void setDrugName(String name){
        m_drugName = name;
       // drugManager.setDrugName(this, name);
    }

    public long getDrugId(){
        return m_drugId;
    }

    public void setDrugId(long id){
        m_drugId = id;
    }

    public String getImagePath(){
        return m_imagePath;
    }
    public void setImagePath(String imagePath){
        m_imagePath = imagePath;
    }

    public Metric getMetric(){
        return m_metric;
    }
    public void setMetric(Metric metric){
        m_metric = metric;
    }

    public String getImageSource(){
        return m_imageSource;
    }
    public void setImageSource(String imageSource){
        m_imageSource = imageSource;
    }

    public Integer getImageId(){
        return m_imageId;
    }
    public void setImageId(int imageId){
        m_imageId = imageId;
       // drugManager.setDrugImageId(this, imageId);
    }

    public String getErowidLink(){
        return m_erowidLink;
    }
    public void setErowidLink(String erowidLink){
        m_erowidLink = erowidLink;
       // drugManager.setDrugErowidLink(this, erowidLink);
    }
    public String getWikiLink(){
        return m_wikiLink;
    }
    public void setWikiLink(String wikiLink){
        m_wikiLink = wikiLink;
       // drugManager.setDrugWikiLink(this, wikiLink);
    }
    public String getRedditLink(){
        return m_redditLink;
    }
    public void setRedditLink(String redditLink){
        m_redditLink = redditLink;
       // drugManager.setDrugRedditLink(this, redditLink);
    }


}
