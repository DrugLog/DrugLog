package lolbellum.druglog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ReminderFragment extends Fragment {

    private static  DrugManager drugManager = Calendar.drugManager;

    ListView reminderListView;
    ListAdapter reminderList;

    private  Drug m_drug;
    private View m_view;


    public static  ReminderFragment newInstance() {
        ReminderFragment fragment = new ReminderFragment();
        return fragment;
    }

    public ReminderFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);
        m_view = rootView;
        setHasOptionsMenu(true);

        TextView textDrugName = (TextView) rootView.findViewById(R.id.fragment_reminder_drugname);
        reminderListView = (ListView) rootView.findViewById(R.id.fragment_reminder_list_view);

        String drugName = getArguments().getString("drugname");
        m_drug = drugManager.getDrug(getArguments().getLong("drugid"));
        textDrugName.setText(drugName);

        updateReminders(rootView);

        //runs when one of the reminders is clicked, will go to the ReminderView
        reminderListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        );

        return rootView;
    }

    public void updateReminders(View view){
        TextView emptyList = (TextView) view.findViewById(R.id.emptyList);

        ArrayList<Reminder> reminders = new ArrayList<Reminder>();
        for(int x = 0;x < drugManager.getReminders().size();x++){
            Log.i("testing", "There are " + drugManager.getReminders().size() + " reminders in DrugManager");
            Reminder tempReminder = drugManager.getReminders().get(x);
            if(m_drug.getDrugName().equalsIgnoreCase(tempReminder.getDrug().getDrugName())){
                reminders.add(tempReminder);
            }
        }

        if(reminders.size() > 0){

            if(emptyList.getVisibility() == View.VISIBLE){
                emptyList.setVisibility(View.INVISIBLE);
            }

            if(reminderListView.getVisibility() == View.INVISIBLE){
                reminderListView.setVisibility(View.VISIBLE);
            }
        } else {
            if(reminderListView.getVisibility() == View.VISIBLE) {
                reminderListView.setVisibility(View.INVISIBLE);
            }

            if(emptyList.getVisibility() == View.INVISIBLE) {
                emptyList.setVisibility(View.VISIBLE);
            }
            return;
        }

        reminderList = new ReminderListAdapter(view.getContext(), reminders);
        reminderListView.setAdapter(reminderList);
    }

    public View getView(){
        return m_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // ((DrugView) activity).onSectionAttached(getArguments().getInt("SectionNumber"));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_reminder, menu);
    }
}
