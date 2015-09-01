package lolbellum.druglog;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class NoteView extends ActionBarActivity {

    private String m_noteTitle;
    private String m_noteText;
    private String m_noteTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        Bundle noteData = getIntent().getExtras();
        if(noteData == null){
            return;
        }

        TextView textTimeAndTitle = (TextView) findViewById(R.id.text_time_and_title);
        TextView textNoteText = (TextView) findViewById(R.id.note_text_text);

        m_noteTitle = noteData.getString("NoteTitle");
        m_noteText = noteData.getString("NoteText");
        m_noteTime = noteData.getString("NoteTime");

        textTimeAndTitle.setText(m_noteTime + " - " + m_noteTitle);
        textNoteText.setText(m_noteText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_view, menu);
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
