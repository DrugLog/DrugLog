package lolbellum.druglog;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class AddNote extends ActionBarActivity {

    int PICK_TIME = 0;

    private DrugManager drugManager = lolbellum.druglog.Calendar.drugManager;

    private String m_drugName;
    private String m_roaName;
    private String m_dateDose;
    private String m_time;
    private Dose m_dose;
    private int m_minute;
    private int m_hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ActionBar actionBar = getSupportActionBar();

        Bundle doseData = getIntent().getExtras();
        if(doseData == null){
            return;
        }

        TextView textNoteTime = (TextView) findViewById(R.id.text_note_time);

        Calendar c = Calendar.getInstance();
        m_hour = c.get(Calendar.HOUR);
        m_minute = c.get(Calendar.MINUTE);

        String minute;
        String hour;
        String amOrPm;

         int test = c.get(Calendar.AM_PM);


        if(test == 0){
            amOrPm = "AM";
        } else {
            amOrPm = "PM";
        }

        if(m_minute < 10){
            minute =  "0" + String.valueOf(m_minute);
        } else {
            minute = String.valueOf(m_minute);
        }

        if(m_hour > 12){
            if((m_hour - 12) < 10) {
                hour = "0" + String.valueOf(m_hour - 12);
            } else {
                hour = String.valueOf(m_hour - 12);
            }
        } else {
            if(m_hour < 10) {
                hour = "0" + String.valueOf(m_hour);
                if(m_hour == 0){
                    hour = "12";
                }
            } else {
                hour = String.valueOf(m_hour);
            }
        }

        textNoteTime.setText(hour + ":" + minute + " " + amOrPm);

       // if(m_hour == 0){
        //    hour = "00";
      //  }

        m_time = hour + ":" + minute + " " + amOrPm;

        m_drugName = doseData.getString("DrugName");
        m_roaName = doseData.getString("RoaName");
        m_dateDose = doseData.getString("Date");
        m_dose = drugManager.getDose(m_drugName, m_roaName, m_dateDose);
        actionBar.setTitle("Add New Note");


    }

    public void clickAddNote(View view){
        EditText textNoteTitle = (EditText) findViewById(R.id.note_title);
        EditText textNoteText = (EditText) findViewById(R.id.text_note_text);

        String noteTitle = textNoteTitle.getText().toString();
        String noteText = textNoteText.getText().toString();

        if(noteTitle.equalsIgnoreCase("")){
            Toast.makeText(this, "You must title your note!",Toast.LENGTH_LONG).show();
            return;
        }
        if(noteText.equalsIgnoreCase("")){
            Toast.makeText(this, "You have no note text!", Toast.LENGTH_LONG).show();
            return;
        }
        for(int x = 0;x < drugManager.getAllNotes().size();x++){
            Note tempNote = drugManager.getAllNotes().get(x);
            if(m_drugName.equalsIgnoreCase(tempNote.getNoteDose().getDrug().getDrugName()) && m_roaName.equalsIgnoreCase(tempNote.getNoteDose().getRoa().getRoaName()) && m_dateDose.equalsIgnoreCase(tempNote.getNoteDose().getDate()) && noteTitle.equalsIgnoreCase(tempNote.getNoteTitle())){
                Toast.makeText(this, "A note with this title already exists!",Toast.LENGTH_LONG).show();
                return;
            }
        }
        Note newNote = new Note(m_dose, noteTitle, noteText, m_time);
        Toast.makeText(this, "Note " + noteTitle + " at " + m_time + " added!", Toast.LENGTH_LONG).show();
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void clickTime(View view){
        Intent i = new Intent(this, SetTime.class);
        i.putExtra("hour", m_hour);
        i.putExtra("minute", m_minute);
        startActivityForResult(i, PICK_TIME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView textNoteTime = (TextView) findViewById(R.id.text_note_time);
        if(requestCode == PICK_TIME && resultCode == Activity.RESULT_OK){
            Bundle timeDate = data.getExtras();

            m_hour = timeDate.getInt("hour");
            m_minute = timeDate.getInt("minute");

            String minute;
            String hour;
            String amOrPm;

            if(m_minute < 10){
                minute =  "0" + String.valueOf(m_minute);
            } else {
                minute = String.valueOf(m_minute);
            }

            if(m_hour > 12){
                amOrPm = "PM";
                if((m_hour - 12) < 10) {
                    hour = "0" + String.valueOf(m_hour - 12);
                } else {
                    hour = String.valueOf(m_hour - 12);
                }
            } else {
                amOrPm = "AM";
                if(m_hour < 10) {
                    hour = "0" + String.valueOf(m_hour);
                    if(m_hour == 0){
                        hour = "12";
                    }
                } else {
                    hour = String.valueOf(m_hour);
                }
            }

            textNoteTime.setText(hour + ":" + minute + " " + amOrPm);

          //  if(m_hour == 0){
            //    hour = "00";
          //  }

            m_time = hour + ":" + minute + " " + amOrPm;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
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
