package lolbellum.druglog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class AddReminder extends ActionBarActivity {

    private DrugManager drugManager = Calendar.drugManager;

    private String m_drugName;
    private String m_timeIntervalPeriod = "Seconds";
    private String m_timeForPeriod = "Seconds";
    private PendingIntent m_pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Reminder");

        Bundle drugData = getIntent().getExtras();
        if(drugData == null){
            return;
        }
        m_drugName = drugData.getString("DrugName");

        TextView reminderDrug = (TextView) findViewById(R.id.text_view_reminder_Drug);
        final Spinner timeIntervalSpinner = (Spinner) findViewById(R.id.spinner_time_interval);
        final Spinner timeForSpinner = (Spinner) findViewById(R.id.spinner_time_for);

        reminderDrug.setText("Reminder for " + m_drugName);

        ArrayList<String> timePeriods = new ArrayList<String>(Arrays.asList("Seconds", "Minutes", "Hours", "Days"));
        ArrayAdapter<String> timePeriodAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,timePeriods);
        timeIntervalSpinner.setAdapter(timePeriodAdapter);
        timeForSpinner.setAdapter(timePeriodAdapter);

        timeIntervalSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        m_timeIntervalPeriod = (String) timeIntervalSpinner.getItemAtPosition(position);
                        Log.i("testing", "Item " + m_timeIntervalPeriod + " selected in the time interval spinner");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        timeForSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        m_timeForPeriod = (String) timeForSpinner.getItemAtPosition(position);
                        Log.i("testing","Item " + m_timeForPeriod + " selected in the time for spinner");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }

    public void clickAddReminder(View view){
        int timeInterval = 0;
        int timeFor = 0;
        EditText reminderEditText = (EditText) findViewById(R.id.edit_text_reminder_text);
        EditText timeIntervalEditText = (EditText) findViewById(R.id.edit_text_time_interval);
        EditText timeForEditText = (EditText) findViewById(R.id.edit_text_time_for);
        String reminderText = reminderEditText.getText().toString();
        String timeIntervalText = timeIntervalEditText.getText().toString();
        String timeForText = timeForEditText.getText().toString();

        if(reminderText.equalsIgnoreCase("")){
            Toast.makeText(this,"You must enter text for the reminder!",Toast.LENGTH_LONG).show();
            return;
        }
        if(timeIntervalText.equalsIgnoreCase("")){
            Toast.makeText(this,"You must enter a time interval!",Toast.LENGTH_LONG).show();
            return;
        }
        if(timeForText.equalsIgnoreCase("")){
            Toast.makeText(this, "You must enter the time for!",Toast.LENGTH_LONG).show();
            return;
        }
        int timeIntervalPeriods = Integer.valueOf(timeIntervalText);
        int timeForPeriods = Integer.valueOf(timeForText);

        if(m_timeIntervalPeriod.equalsIgnoreCase("Seconds")){
            timeInterval = 1000 * timeIntervalPeriods;
        } else if(m_timeIntervalPeriod.equalsIgnoreCase("Minutes")){
            timeInterval = 60000 * timeIntervalPeriods;
        } else if(m_timeIntervalPeriod.equalsIgnoreCase("Hours")){
            timeInterval = 3600000 * timeIntervalPeriods;
;        } else if(m_timeIntervalPeriod.equalsIgnoreCase("Days")){
            timeInterval = 86400000 * timeIntervalPeriods;
        }

        if(m_timeForPeriod.equalsIgnoreCase("Seconds")){
            timeFor = 1000 * timeForPeriods;
        } else if(m_timeForPeriod.equalsIgnoreCase("Minutes")){
            timeFor = 60000 * timeForPeriods;
        } else if(m_timeForPeriod.equalsIgnoreCase("Hours")){
            timeFor = 3600000 * timeForPeriods;
        } else if(m_timeForPeriod.equalsIgnoreCase("Days")){
            timeFor = 86400000 * timeForPeriods;
        }

        if(timeInterval > timeFor){
            Toast.makeText(this,"Your time interval can't be longer than your time for!",Toast.LENGTH_LONG).show();
            return;
        }

        Drug reminderDrug = drugManager.getDrugFromName(m_drugName);
        Reminder newReminder = new Reminder(reminderDrug, null, reminderText, reminderText, timeInterval, timeFor);
        newReminder.setIsActive(true);




        //set the alarm if there are active doses
        for(int x = 0;x < drugManager.getAllDoses().size();x++){
            Dose tempDose = drugManager.getAllDoses().get(x);
            if(tempDose.getDrug().getDrugName().equalsIgnoreCase(m_drugName)){
                String doseDate = tempDose.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
               try{
                   Date date = sdf.parse(doseDate);
                   long doseTime = date.getTime();
                   if(doseTime + timeFor > System.currentTimeMillis()){
                       long endTime = doseTime + timeFor;
                       long triggerAtMillis = System.currentTimeMillis();
                       Intent i = new Intent(this, ReminderReceiver.class);
                       i.putExtra("ReminderText",reminderText);
                       i.putExtra("TimeInterval", timeInterval);
                       i.putExtra("TimeFor", timeFor);
                       i.putExtra("EndTime", endTime);

                       m_pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);

                       AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                       alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, timeInterval, m_pendingIntent);
                       Log.i("testing", "Repeating alarm set");
                   }
               } catch (Exception e){

               }
            }
        }

        Toast.makeText(this, "Reminder added!",Toast.LENGTH_LONG).show();
       // setResult(Activity.RESULT_OK, i);
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
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
