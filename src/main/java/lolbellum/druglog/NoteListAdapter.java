package lolbellum.druglog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NoteListAdapter extends ArrayAdapter<Note> {

    private ArrayList<Note> m_notes = new ArrayList<Note>();

    public NoteListAdapter(Context context, ArrayList<Note> notes){
        super(context, R.layout.add_drug_listview, notes);
        m_notes = notes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.note_row, parent, false);

        String[] noteTimes = new String[m_notes.size()];
        String[] noteTitles = new String[m_notes.size()];

        for(int x = 0;x < m_notes.size();x++){
            noteTimes[x] = m_notes.get(x).getNoteTime();
            noteTitles[x] = m_notes.get(x).getNoteTitle();
        }


        Note note = getItem(position);
       // String noteTime = noteTimes[position];
       // String noteTitle = noteTitles[position];
        String noteTime = note.getNoteTime();
        String noteTitle = note.getNoteTitle();

        TextView textNoteTime = (TextView) customView.findViewById(R.id.note_time);
        TextView textNoteTitle = (TextView) customView.findViewById(R.id.note_title);

        textNoteTime.setText(noteTime);
        textNoteTitle.setText(noteTitle);

        return customView;
    }
}
