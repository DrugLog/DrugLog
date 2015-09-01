package lolbellum.druglog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class EditDrug extends ActionBarActivity {

    private DrugManager drugManager = Calendar.drugManager;

    private static final int SELECT_PICTURE = 0;
    private String imagePath = "";
    private Uri selectedImageUri;
    ArrayList<String> nicknames = new ArrayList<String>();
    ListView drugNicknameListView;
    Drug drug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_drug);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            Log.i("testing","EditDrug bundle is null");
            return;
        }

       // drug = drugManager.getDrugFromName(bundle.getString("DrugName"));
        drug = drugManager.getDrug(bundle.getLong("DrugId"));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit " + drug.getDrugName());

        EditText editTextDrugName = (EditText) findViewById(R.id.edit_text_drug_name);
        EditText editTextErowidLink = (EditText) findViewById(R.id.edit_text_erowid_link);
        EditText editTextWikiLink = (EditText) findViewById(R.id.edit_text_wiki_link);
        EditText editTextRedditLink = (EditText) findViewById(R.id.edit_text_reddit_link);
        drugNicknameListView = (ListView) findViewById(R.id.list_view_drug_nicknames);
        TextView emptyList = (TextView) findViewById(R.id.emptyList);
        ImageView drugImage = (ImageView) findViewById(R.id.image_view_drug_image);

        editTextDrugName.setText(drug.getDrugName());
        editTextErowidLink.setText(drug.getErowidLink());
        editTextWikiLink.setText(drug.getWikiLink());
        editTextRedditLink.setText(drug.getRedditLink());
        if(!drug.getImagePath().equalsIgnoreCase("")){
            Bitmap imageBitmap = BitmapFactory.decodeFile(drug.getImagePath());
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 100,100,true);
            drugImage.setImageBitmap(resizedBitmap);
        } else {
            drugImage.setImageResource(drug.getImageId());
        }

        nicknames = drug.getDrugNickNames();
        updateNicknames();

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();

                //OI FILE Manager
                // filemanagerstring = selectedImageUri.getPath();
                imagePath = getRealPathFromURI(this, selectedImageUri);
                ImageView drugImage = (ImageView) findViewById(R.id.image_view_drug_image);
                //drugImage.setImageURI(selectedImageUri);
                Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 100,100,true);
                drugImage.setImageBitmap(resizedBitmap);
            }
        }
    }

    public void clickDrugImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void clickEditDrug(View view){
        EditText editTextDrugName = (EditText) findViewById(R.id.edit_text_drug_name);
        EditText editTextErowidLink = (EditText) findViewById(R.id.edit_text_erowid_link);
        EditText editTextWikiLink = (EditText) findViewById(R.id.edit_text_wiki_link);
        EditText editTextRedditLink = (EditText) findViewById(R.id.edit_text_reddit_link);
        drugNicknameListView = (ListView) findViewById(R.id.list_view_drug_nicknames);

        drug.setDrugName(editTextDrugName.getText().toString());
        drug.setDrugNicknames(nicknames);
        if(!imagePath.equalsIgnoreCase("")){
            drug.setImagePath(imagePath);
        }
        drug.setErowidLink(editTextErowidLink.getText().toString());
        drug.setWikiLink(editTextWikiLink.getText().toString());
        drug.setRedditLink(editTextRedditLink.getText().toString());

        drugManager.updateDrug(drug, drug.getDrugId());


        Log.i("testing","Drug edited!");
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void clickAddNickname(View view){
        EditText textDrugNickname = (EditText) findViewById(R.id.edit_text_drug_nickname);

        String drugNickname = textDrugNickname.getText().toString();

        if(drugNickname.equalsIgnoreCase("")){
            Toast.makeText(this, "No Nickname entered", Toast.LENGTH_LONG).show();
            return;
        }

        for(int x = 0;x < drug.getDrugNickNames().size();x++){
            if(drugNickname.equalsIgnoreCase(drug.getDrugNickNames().get(x))){
                Toast.makeText(this, "That Nickname is already listed!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        nicknames.add(drugNickname);
        drug.getDrugNickNames().add(drugNickname);
        textDrugNickname.setText("");
        updateNicknames();
    }

    public void updateNicknames(){
        TextView emptyList = (TextView) findViewById(R.id.emptyList);

        if(drug.getDrugNickNames().size() > 0){
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

        ArrayAdapter<String> drugNicknameList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,drug.getDrugNickNames());
        drugNicknameListView.setAdapter(drugNicknameList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_drug, menu);
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
