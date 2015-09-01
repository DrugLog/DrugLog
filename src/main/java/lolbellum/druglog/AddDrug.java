package lolbellum.druglog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class AddDrug extends ActionBarActivity {

    private DrugManager drugManager = Calendar.drugManager;

    final static int SELECT_PICTURE = 0;

    ListView drugClassListView;
    ListView drugNicknameListView;
    ListAdapter drugClassList;
    ListAdapter drugNicknameList;

    private ArrayList<String> m_drugNicknames = new ArrayList<String>();
    private String m_drugName;
    private String m_drugNickname;
    private String m_className;
    private int m_position;
    private int defaultColor;
    private boolean classSelected = true;
    private String selectedImagePath;
    private String filemanagerstring = "";
    private Uri selectedImageUri;
    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);

        Bundle classData = getIntent().getExtras();
        if(classData == null){
            return;
        }
     //   m_position = classData.getInt("Position");
        m_className = classData.getString("SelectedClassName");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Drug (Click image to change)");

        drugClassListView = (ListView) findViewById(R.id.list_view_drug_classes);
        drugNicknameListView = (ListView) findViewById(R.id.list_view_drug_nicknames);
        TextView emptyList = (TextView) findViewById(R.id.emptyList);
        final TextView textClassToAdd = (TextView) findViewById(R.id.text_class_to_add);




        drugClassList = new DrugclassListAdapter(this, drugManager.getDrugClasses());
        drugClassListView.setAdapter(drugClassList);
        defaultColor = drugClassListView.getDrawingCacheBackgroundColor();

        textClassToAdd.setText("Adding to class: " + m_className);

        drugNicknameListView.setVisibility(View.INVISIBLE);
        emptyList.setVisibility(View.VISIBLE);

        drugClassListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            m_className = "All Drugs";
                        } else {
                            DrugClass selectedClass = (DrugClass) drugClassListView.getItemAtPosition(position);
                            m_className = selectedClass.getDrugClassName();
                        }
                        m_position = position;
                        textClassToAdd.setText("Adding to class: " + m_className);
                    }
                }
        );
    }

    public void clickDrugImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }


    public void clickAddNickname(View view){
        TextView emptyList = (TextView) findViewById(R.id.emptyList);
        EditText textDrugNickname = (EditText) findViewById(R.id.edit_text_drug_nickname);

        String drugNickname = textDrugNickname.getText().toString();

        if(drugNickname.equalsIgnoreCase("")){
            Toast.makeText(this, "No Nickname entered", Toast.LENGTH_LONG).show();
            return;
        }

        for(int x = 0;x < m_drugNicknames.size();x++){
            if(drugNickname.equalsIgnoreCase(m_drugNicknames.get(x))){
                Toast.makeText(this, "That Nickname is already listed!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        m_drugNicknames.add(drugNickname);
        textDrugNickname.setText("");
        updateNicknames();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                 selectedImageUri = data.getData();

                //OI FILE Manager
               // filemanagerstring = selectedImageUri.getPath();
                filemanagerstring = getRealPathFromURI(this, selectedImageUri);
                ImageView drugImage = (ImageView) findViewById(R.id.image_view_drug_image);
                drugImage.setImageURI(selectedImageUri);
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void clickAddDrug(View view){

        EditText textDrugName = (EditText) findViewById(R.id.edit_text_drug_name);

        String drugName = textDrugName.getText().toString();

        if(drugName.equalsIgnoreCase("")){
            Toast.makeText(this,"You must enter a Drug Name!", Toast.LENGTH_LONG).show();
            return;
        }
        for(int x = 0;x < drugManager.getDrugs().size();x++){
            Drug tempDrug =  drugManager.getDrugs().get(x);
            if(tempDrug.getDrugName().equalsIgnoreCase(drugName)){
                Toast.makeText(this, "That Drug already exists!", Toast.LENGTH_LONG).show();
                return;
            }
        }
        for(int x = 0;x < drugManager.getDrugs().size();x++){
            Drug tempDrug = drugManager.getDrugs().get(x);
            for(int i = 0;i < tempDrug.getDrugNickNames().size();i++){
                if(tempDrug.getDrugNickNames().get(i).equalsIgnoreCase(drugName)){
                    Log.i("testing", "Drug nickname found that matches this drug");
                    final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("Did you mean " + tempDrug.getDrugName() + "?");
                    alertDialog.setMessage("Similar nickname detected");
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("testing", "No");
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("testing", "Yes");
                            finish();
                        }
                    });
                    alertDialog.show();
                    return;
                }
            }
        }

        Drug newDrug;

        if(!filemanagerstring.equalsIgnoreCase("")){
           int imageResource = getResources().getIdentifier(filemanagerstring, null, getPackageName());
//            Drawable res = getResources().getDrawable(imageResource);
            //filemanagerstring = "lolbellum.druglog" + filemanagerstring;
            newDrug = new Drug(drugName, filemanagerstring, m_drugNicknames);
            Log.i("testing","imagePath is " + filemanagerstring);
        } else {
             newDrug = new Drug(drugName, m_drugNicknames);
        }


        if(!m_className.equalsIgnoreCase("All Drugs")) {
           drugManager.getDrugClass(m_className).addDrug(newDrug);
           // drugManager.addDrugToClass(drugManager.getDrugClass(m_className),newDrug);
            Toast.makeText(this, "Drug " + drugName + " added to " + m_className, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Drug " + drugName + " added to All Drugs!", Toast.LENGTH_LONG).show();
        }
       // newDrug.setDrugNicknames(m_drugNicknames);

        Intent i = getIntent();
        i.putExtra("ClassName", m_className);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void updateNicknames(){
        TextView emptyList = (TextView) findViewById(R.id.emptyList);

        if(m_drugNicknames.size() > 0){
            if(emptyList.getVisibility() == View.VISIBLE){
                emptyList.setVisibility(View.INVISIBLE);
            }
            if(drugNicknameListView.getVisibility() == View.INVISIBLE){
                drugNicknameListView.setVisibility(View.VISIBLE);
            }
        } else {
            if(emptyList.getVisibility() == View.INVISIBLE){
                emptyList.setVisibility(View.VISIBLE);
            }
            if(drugNicknameListView.getVisibility() == View.VISIBLE){
                drugNicknameListView.setVisibility(View.INVISIBLE);
            }
        }

        drugNicknameList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, m_drugNicknames);
        drugNicknameListView.setAdapter(drugNicknameList);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_drug, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

