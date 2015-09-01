package lolbellum.druglog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;


public class DoseView extends ActionBarActivity {

   int ADD_NOTE = 0;

    private DrugManager drugManager = Calendar.drugManager;
    ListAdapter noteList;
    ListView noteListView;

    private String m_drugName;
    private String m_roaName;
    private String m_doseDate;
    private String m_time;
    private String m_metricName;
    private double m_doseAmount;
    private Drug m_drug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dose_view);

        ActionBar actionBar = getSupportActionBar();

        Bundle doseDate = getIntent().getExtras();
        if(doseDate == null){
            return;
        }

        ImageView drugImage = (ImageView) findViewById(R.id.image_drug);
        TextView textDrugName = (TextView) findViewById(R.id.text_drug_name);
        TextView textRoaName = (TextView) findViewById(R.id.text_roa);
        TextView textDose = (TextView) findViewById(R.id.text_dose);
        noteListView = (ListView) findViewById(R.id.list_notes);

        m_drugName = doseDate.getString("DrugName");
        m_roaName = doseDate.getString("RoaName");
        m_time = doseDate.getString("Time");
        m_metricName = doseDate.getString("MetricName");
        m_doseAmount = doseDate.getDouble("DoseAmount");
        m_doseDate = doseDate.getString("Date");

        m_drug = drugManager.getDrugFromName(m_drugName);

        actionBar.setTitle(m_doseDate + " " + m_drugName);

        if(!m_drug.getImagePath().equalsIgnoreCase("")){
            Bitmap imageBitmap = BitmapFactory.decodeFile(m_drug.getImagePath());
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 100,100,true);
            drugImage.setImageBitmap(resizedBitmap);
        } else {
            //drugImageView.setImageResource(m_drug.getImageId());
            if(m_drug.getImageId() != R.drawable.lol){
                drugImage.setImageResource(m_drug.getImageId());
            }
        }


        textDrugName.setText(m_drugName);
        textDose.setText(m_doseAmount + " " + m_metricName);
        textRoaName.setText(m_roaName);
        updateNotes();

       noteListView.setOnItemClickListener(
               new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       Intent i = new Intent(parent.getContext(), NoteView.class);

                       Note note = (Note) noteListView.getItemAtPosition(position);
                       i.putExtra("NoteTitle", note.getNoteTitle());
                       i.putExtra("NoteText", note.getNoteText());
                       i.putExtra("NoteTime", note.getNoteTime());

                       startActivity(i);
                   }
               }
       );

    }

    public void updateNotes(){
        ArrayList<Note> notes = new ArrayList<Note>();

        TextView emptyList = (TextView) findViewById(R.id.emptyList);

        for(int x = 0; x < drugManager.getAllNotes().size();x++){
            Note tempNote = drugManager.getAllNotes().get(x);
            if(tempNote.getNoteDose().getDrug().getDrugName().equalsIgnoreCase(m_drugName) && tempNote.getNoteDose().getRoa().getRoaName().equalsIgnoreCase(m_roaName) && tempNote.getNoteDose().getDate().equalsIgnoreCase(m_doseDate)){
                notes.add(tempNote);
            }
        }
        if(notes.size() > 0){

            if(emptyList.getVisibility() == View.VISIBLE){
                emptyList.setVisibility(View.INVISIBLE);
            }

            if(noteListView.getVisibility() == View.INVISIBLE){
                noteListView.setVisibility(View.VISIBLE);
            }
        } else {
            if(noteListView.getVisibility() == View.VISIBLE) {
                noteListView.setVisibility(View.INVISIBLE);
            }

            if(emptyList.getVisibility() == View.INVISIBLE) {
                emptyList.setVisibility(View.VISIBLE);
            }
        }

        noteList = new NoteListAdapter(this, notes);
        noteListView.setAdapter(noteList);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_NOTE && resultCode == Activity.RESULT_OK){
            updateNotes();
        }
    }

    public void clickDrugImage(View view){
        Intent i = new Intent(this,DrugView.class);
        i.putExtra("DrugId",m_drug.getDrugId());
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dose_view, menu);
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
        if(id == R.id.add_note){
            Intent i = new Intent(this, AddNote.class);
            i.putExtra("DrugName", m_drugName);
            i.putExtra("RoaName", m_roaName);
            i.putExtra("Date", m_doseDate);

            startActivityForResult(i, ADD_NOTE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
