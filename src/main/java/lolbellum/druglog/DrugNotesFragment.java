package lolbellum.druglog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class DrugNotesFragment extends Fragment {

    private static  DrugManager drugManager = Calendar.drugManager;

    ListView noteListView;
    ListAdapter noteList;

    private  Drug m_drug;


  /*  public static  DrugInfoFragment newInstance() {
        DrugInfoFragment fragment = new DrugInfoFragment();
        return fragment;
    }*/
    public static  DrugNotesFragment newInstance() {
        DrugNotesFragment fragment = new DrugNotesFragment();
        return fragment;
    }

    public DrugNotesFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drug_notes, container, false);

        TextView textDrugName = (TextView) rootView.findViewById(R.id.doses_fragment_drug_name);
        noteListView = (ListView) rootView.findViewById(R.id.notification_list);
        TextView emptyList = (TextView)rootView.findViewById(R.id.emptyList);

        // m_drug = drugManager.getDrugFromName(getArguments().getString("DrugName"));
        Log.i("testing", "Getting arguments");
        String drugName = getArguments().getString("drugname");
        Log.i("testing","getArguments.getString(DrugName) is " + drugName);
       // m_drug = drugManager.getDrugFromName(drugName);
        m_drug = drugManager.getDrug(getArguments().getLong("drugid"));

        textDrugName.setText(m_drug.getDrugName());


        ArrayList<Note> notes = new ArrayList<Note>();

        for(int x = 0; x < drugManager.getAllNotes().size();x++){
            Note tempNote = drugManager.getAllNotes().get(x);
            if(m_drug.getDrugName().equalsIgnoreCase(tempNote.getNoteDose().getDrug().getDrugName())){
                notes.add(tempNote);
                tempNote.setNoteTime(tempNote.getNoteDose().getDate().substring(0,10) +  " " + tempNote.getNoteTime());
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
            return rootView;
        }

        noteList = new NoteListAdapter(rootView.getContext(), notes);
        noteListView.setAdapter(noteList);

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

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // ((DrugView) activity).onSectionAttached(getArguments().getInt("SectionNumber"));
    }


}
