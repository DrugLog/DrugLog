package lolbellum.druglog;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Eliot on 7/15/2015.
 */
public class DrugProvider extends ContentProvider {

    public String TABLE_NAME;
    public String[] TABLE_FIELDS;
    public static final String AUTHORITY = "lolbellum.druglog.DrugProvider";
    public static final String SCHEME = "content://";

    // URIs
    // Used for all persons
    public static final String DRUGS = SCHEME + AUTHORITY + "/person";
    public static final Uri URI_DRUGS = Uri.parse(DRUGS);
    // Used for a single person, just add the id to the end
    public static final String DRUG_BASE = DRUGS + "/";

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result = null;
        if(URI_DRUGS.equals(uri)){
            MyDBHandler dbHandler = MyDBHandler.getInstance(getContext(), null, null, 16);
            result = dbHandler.getReadableDatabase().query(dbHandler.TABLE_DRUG,dbHandler.DRUG_FIELDS,null,null,null,null,null,null);
            result.setNotificationUri(getContext().getContentResolver(), URI_DRUGS);
        } else if(uri.toString().startsWith(DRUG_BASE)){

        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        return result;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
