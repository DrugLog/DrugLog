package lolbellum.druglog;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ReminderListAdapter extends ArrayAdapter<Reminder>{

    private ArrayList<Reminder> m_reminders = new ArrayList<Reminder>();

    public ReminderListAdapter(Context context, ArrayList<Reminder> reminders){
        super(context, R.layout.reminder_row, reminders);
        m_reminders = reminders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.reminder_row, parent, false);

      //  TextView textReminderTitle = (TextView) customView.findViewById(R.id.text_reminder_title);
        TextView textReminderInfo = (TextView) customView.findViewById(R.id.text_reminder_info);

        String[] reminderTitles = new String[m_reminders.size()];
        int[] reminderTimes = new int[m_reminders.size()];
        int[] reminderTimesFor = new int[m_reminders.size()];

        for(int x = 0; x < m_reminders.size();x++){
            reminderTitles[x] = m_reminders.get(x).getText();
            reminderTimes[x] = m_reminders.get(x).getTime();
            reminderTimesFor[x] = m_reminders.get(x).getTimeFor();
        }

        Reminder reminder = getItem(position);
        String reminderTitle = reminderTitles[position];
        int reminderTime = reminderTimes[position];
        int reminderTimeFor = reminderTimesFor[position];

        //textReminderTitle.setText(reminderTitle);
        textReminderInfo.setText("Remind me to " + reminderTitle + " every " + reminderTime + " milliseconds for " + reminderTimeFor + " milliseconds after dosing");


        return customView;
    }
}
