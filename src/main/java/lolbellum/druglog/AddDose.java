package lolbellum.druglog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import org.w3c.dom.Text;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.*;


public class AddDose extends Activity {

    private DrugManager drugManager = lolbellum.druglog.Calendar.drugManager;

    static int PICK_DATE = 0;
    static int PICK_TIME = 1;

    ListView drugListView;
    ListAdapter drugsClassList;
    ListAdapter drugsList;

    private String dateString = "";
    private String timeString = "";
    private int m_day;
    private int m_month;
    private int m_year;
    private int m_hour;
    private int m_minute;
    private int defaultColor;
    private boolean onClassList = true;
    private Drug selectedDrug;
    private RouteOfAdministration selectedRoa;
    private Metric selectedMetric;
    private double doseAmount = 0.000;
    private long currentTimeMilliseconds;
    private long selectedTimeMilliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dose);


        //setting date that was selected on CalendarView
        Bundle calendarData = getIntent().getExtras();
        if(calendarData == null){
            return;
        }

        TextView dateText = (TextView) findViewById(R.id.text_date);
        TextView timeText = (TextView) findViewById(R.id.text_time);
        CheckBox checkBoxNotifyMe = (CheckBox) findViewById(R.id.check_box_notify_me);




        Calendar c = Calendar.getInstance();
        int Hour = c.get(Calendar.HOUR);
        int Minute = c.get(Calendar.MINUTE);
        m_hour = c.get(Calendar.HOUR_OF_DAY);
        m_minute = c.get(Calendar.MINUTE);
        String hour;
        String minute;
        String amOrPm;

        if(c.get(Calendar.AM_PM) == 0){
            amOrPm = "AM";
        } else {
            amOrPm = "PM";
        }

        if(Minute < 10){
            minute =  "0" + String.valueOf(Minute);
        } else {
            minute = String.valueOf(Minute);
        }
        hour = String.valueOf(Hour);
        timeText.setText(hour + ":" + minute + " " + amOrPm);

        if(m_hour < 10){
            hour = "0" + String.valueOf(m_hour);
        }


        // dateString = calendarData.getString("Date");
       // timeString = "12:00 AM";
        // dateString = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(time));
         m_day = calendarData.getInt("day");
         m_month = calendarData.getInt("month");
         m_year = calendarData.getInt("year");

        //   int hour = c.get(Calendar.HOUR_OF_DAY);
        //  int minute = c.get(Calendar.MINUTE);

        dateText.setText((String.valueOf(m_month + 1)) + "/" + String.valueOf(m_day) + "/" + String.valueOf(m_year));

         currentTimeMilliseconds = System.currentTimeMillis();
         selectedTimeMilliseconds = 0;
        String selectedDate = dateText.getText().toString() + " " + hour + ":" + minute;
        Log.i("testing","selectedDate = " + selectedDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {
            Date date = sdf.parse(selectedDate);
            selectedTimeMilliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(selectedTimeMilliseconds < currentTimeMilliseconds){
            if(checkBoxNotifyMe.getVisibility() == View.VISIBLE) {
                checkBoxNotifyMe.setEnabled(false);
                checkBoxNotifyMe.setVisibility(View.INVISIBLE);
            }
        } else {
            if(checkBoxNotifyMe.getVisibility() == View.INVISIBLE){
                checkBoxNotifyMe.setEnabled(true);
                checkBoxNotifyMe.setVisibility(View.VISIBLE);
            }
        }

       // dateText.setText(timeString);

        //list of classes name's
       /* String[] listOfClasses =  new String[drugManager.getDrugClasses().size() + 1];
        listOfClasses[0] = "All Drugs";
        for(int x = 0;x < drugManager.getDrugClasses().size();x++){
            listOfClasses[x + 1] = drugManager.getDrugClasses().get(x).getDrugClassName();
        }
        //list of class image ids
        int[] imageIds = new int[drugManager.getDrugClasses().size() + 1];
        for(int x = 0;x < drugManager.getDrugClasses().size();x++){
            imageIds[x + 1] = drugManager.getDrugClasses().get(x).getImageId();
        }*/

        drugsClassList = new DrugclassListAdapter(this, drugManager.getDrugClasses());
        drugListView = (ListView) findViewById(R.id.drug_class_list);
        drugListView.setAdapter(drugsClassList);
        defaultColor = drugListView.getDrawingCacheBackgroundColor();

        //setting up the dropdown list(spinner) for metrics
        final Spinner metricSpinner = (Spinner) findViewById(R.id.metric_spinner);
        String[] metricList = new String[drugManager.getAllMetrics().size()];
        for(int x = 0; x < drugManager.getAllMetrics().size();x++){
            Metric tempMetric = drugManager.getAllMetrics().get(x);
            metricList[x] = tempMetric.getMetricName();
        }
        ArrayAdapter<String> metricSpinnerAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,metricList);
        metricSpinner.setAdapter(metricSpinnerAdapter);

        //selectedMetric = drugManager.getMetric(metricList[0]);

/*        metricSpinner.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView textView = (TextView)metricSpinner.getSelectedView();
                        selectedMetric = drugManager.getMetric(textView.getText().toString());
                    }
                }
        );*/

        //setting up the spinner for the Route of Administration
        final Spinner roaSpinner = (Spinner) findViewById(R.id.roa_spinner);
        String[] roaList = new String[drugManager.getAllRoas().size()];
        for(int x = 0;x < drugManager.getAllRoas().size();x++){
            RouteOfAdministration tempRoa = drugManager.getAllRoas().get(x);
            roaList[x] = tempRoa.getRoaName();
        }
        ArrayAdapter<String> roaSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, roaList);
        roaSpinner.setAdapter(roaSpinnerAdapter);

      //  selectedRoa = drugManager.getRoa(roaList[0]);

       /* roaSpinner.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView textView = (TextView)roaSpinner.getSelectedView();
                        selectedRoa = drugManager.getRoa(textView.getText().toString());
                    }
                }
        );*/

        drugListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView selectedDrugText = (TextView) findViewById(R.id.drug_selected);
                        //runs if the class listview is shown
                        if(onClassList == true
                               // drugListView.getAdapter() == drugsClassList
                         ){
                            //find which class was clicked on
                           // drugListView = (ListView) findViewById(R.id.drug_class_list);
                           // String classClickedName = String.valueOf(drugListView.getSelectedItem());
                           // String drugSelectedName = (String) (drugListView.getItemAtPosition(position));
                           // DrugClass selectedDrugClass = drugManager.getDrugClass(drugSelectedName);

                            ArrayList<Drug> listOfDrugs = new ArrayList<Drug>();

                            if(position == 0){
                                listOfDrugs =  drugManager.getDrugs();
                            } else {
                                DrugClass selectedDrugClass = (DrugClass) drugListView.getItemAtPosition(position);
                                listOfDrugs = selectedDrugClass.getDrugs();
                            }
                            //list of drug names and image ids
                          /*  String[] drugNames = new String[listOfDrugs.size()];
                            int[] imageIds = new int[listOfDrugs.size()];

                                for (int x = 0; x < listOfDrugs.size(); x++) {
                                    drugNames[x] = listOfDrugs.get(x).getDrugName();
                                    imageIds[x] = listOfDrugs.get(x).getImageId();
                                }*/

                            drugsList = new DrugListAdapter(parent.getContext(),listOfDrugs);
                            drugListView.setAdapter(drugsList);
                            onClassList = false;
                            return;
                        }
                        //runs if the drug listview is shown
                        drugListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        if(selectedDrug == null){
                           // String drugSelectedName = (String) (drugListView.getItemAtPosition(position));
                            Drug drug = (Drug) drugListView.getItemAtPosition(position);
                            String drugSelectedName = drug.getDrugName();
                            selectedDrugText.setText(drugSelectedName);
                            drugListView.setItemChecked(position, true);
                            view.setBackgroundColor(Color.LTGRAY);
                            selectedDrug = drugManager.getDrugFromName(drugSelectedName);
                        } else {
                            selectedDrugText.setText("None");
                            drugListView.setItemChecked(position, false);
                            selectedDrug = null;
                            view.setBackgroundColor(defaultColor);
                        }
                    }
                }
        );
    }

    public int getDay(){
        return m_day;
    }
    public void setDay(int day){
        m_day = day;
    }
    public int getMonth(){
        return m_month;
    }
    public void setMonth(int month){
        m_month = month;
    }
    public int getYear(){
        return m_year;
    }
    public void setYear(int year){
        m_year = year;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        TextView dateText = (TextView) findViewById(R.id.text_date);
        TextView timeText = (TextView) findViewById(R.id.text_time);
        CheckBox checkBoxNotifyMe = (CheckBox) findViewById(R.id.check_box_notify_me);
        if(requestCode == PICK_DATE && resultCode == Activity.RESULT_OK){
            Bundle dateData = data.getExtras();
            m_month = dateData.getInt("month");
            m_day = dateData.getInt("day");
            m_year = dateData.getInt("year");
            dateText.setText((String.valueOf(m_month + 1)) + "/" + String.valueOf(m_day) + "/" + String.valueOf(m_year));

             currentTimeMilliseconds = System.currentTimeMillis();
             selectedTimeMilliseconds = 0;
            String selectedDate = dateText.getText().toString() + " " + m_hour + ":" + m_minute;
            Log.i("testing","selectedDate = " + selectedDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            try {
                Date date = sdf.parse(selectedDate);
                selectedTimeMilliseconds = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(selectedTimeMilliseconds < currentTimeMilliseconds){
                if(checkBoxNotifyMe.getVisibility() == View.VISIBLE) {
                    checkBoxNotifyMe.setEnabled(false);
                    checkBoxNotifyMe.setVisibility(View.INVISIBLE);
                }
            } else {
                if(checkBoxNotifyMe.getVisibility() == View.INVISIBLE){
                    checkBoxNotifyMe.setEnabled(true);
                    checkBoxNotifyMe.setVisibility(View.VISIBLE);
                }
            }
        }
        if(requestCode == PICK_TIME && resultCode == Activity.RESULT_OK){
            Bundle timeData = data.getExtras();
            m_hour = timeData.getInt("hour");
            m_minute = timeData.getInt("minute");
            String minute;
            String hour;
            String amOrPm;

            if(m_minute < 10){
                minute = "0" + String.valueOf(m_minute);
            } else {
                minute = String.valueOf(m_minute);
            }

            if(m_hour > 12){
                hour = String.valueOf(m_hour - 12);
                amOrPm = "PM";
            } else if(m_hour == 0){
                hour = "12";amOrPm = "AM";
            } else {
                hour = String.valueOf(m_hour);
                amOrPm = "AM";
            }

            timeText.setText(hour + ":" + minute + " " + amOrPm);

             currentTimeMilliseconds = System.currentTimeMillis();
            selectedTimeMilliseconds = 0;
            String selectedDate = dateText.getText().toString() + " " + m_hour + ":" + m_minute;
            Log.i("testing","selectedDate = " + selectedDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            try {
                Date date = sdf.parse(selectedDate);
                selectedTimeMilliseconds = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(selectedTimeMilliseconds < currentTimeMilliseconds){
                if(checkBoxNotifyMe.getVisibility() == View.VISIBLE) {
                    checkBoxNotifyMe.setEnabled(false);
                    checkBoxNotifyMe.setVisibility(View.INVISIBLE);
                }
            } else {
                if(checkBoxNotifyMe.getVisibility() == View.INVISIBLE){
                    checkBoxNotifyMe.setEnabled(true);
                    checkBoxNotifyMe.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void clickDate(View view){
        Intent i = new Intent(this,SetDate.class);
        i.putExtra("day",m_day);
        i.putExtra("month", m_month);
        i.putExtra("year", m_year);
        startActivityForResult(i, PICK_DATE);

    }
    public void clickTime(View view){
        Intent i = new Intent(this, SetTime.class);
        i.putExtra("hour", m_hour);
        i.putExtra("minute", m_minute);
        startActivityForResult(i, PICK_TIME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_dose, menu);
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
    public void clickAddDose(View view){
        EditText doseEditText = (EditText) findViewById(R.id.text_dose);
        String doseText = String.valueOf(doseEditText.getText());
        Spinner metricSpinner = (Spinner)findViewById(R.id.metric_spinner);
        Spinner roaSpinner = (Spinner)findViewById(R.id.roa_spinner);
        CheckBox checkBoxNotifyMe = (CheckBox) findViewById(R.id.check_box_notify_me);

        selectedMetric = drugManager.getMetric(metricSpinner.getSelectedItem().toString());
        selectedRoa = drugManager.getRoa(roaSpinner.getSelectedItem().toString());



        if(selectedDrug == null){
            Toast.makeText(this, "You must select a drug!", Toast.LENGTH_LONG).show();
            return;
        }
        if(selectedMetric == null){
            Toast.makeText(this, "You must select a Metric!", Toast.LENGTH_LONG).show();
            return;
        }
        if(selectedRoa == null){
            Toast.makeText(this, "You must select a Route of administration!", Toast.LENGTH_LONG).show();
            return;
        }
        if(doseText.equalsIgnoreCase("")){
            Toast.makeText(this, "You must enter a dose!", Toast.LENGTH_LONG).show();
            return;

        }

        doseAmount = Double.valueOf(doseText);

        String month;
        String day;
        String year;
        String minute;
        String hour;
        String amOrPm;


        if(m_month + 1 < 10){
            month =  "0" + String.valueOf(m_month + 1);
        } else {
            month = String.valueOf(m_month + 1);
        }
        if(m_day < 10){
            day = "0" + String.valueOf(m_day);
        } else {
            day = String.valueOf(m_day);
        }
        year = String.valueOf(m_year);
        if(m_minute < 10){
            minute =  "0" + String.valueOf(m_minute);
        } else {
            minute = String.valueOf(m_minute);
        }
        if(m_hour < 10){
            hour = "0" + String.valueOf(m_hour);
        } else {
             hour = String.valueOf(m_hour);
        }
        dateString = month + "/" + day + "/" + year + " " + hour + ":" + minute;
       // Toast.makeText(this,"Substring(0,10) is" + dateString.substring(0,10) ,Toast.LENGTH_SHORT).show();
        //example 06/13/2015 07:37

        for(int x = 0; x < drugManager.getAllDoses().size();x++){
            Dose tempDose = drugManager.getAllDoses().get(x);
            if(tempDose.getDrug().getDrugName().equalsIgnoreCase(selectedDrug.getDrugName()) && tempDose.getDate().equalsIgnoreCase(dateString) && tempDose.getRoa().getRoaName().equalsIgnoreCase(selectedRoa.getRoaName())){
                Toast.makeText(this, "An identical dose exists!" , Toast.LENGTH_LONG).show();
                return;
            }
        }

        //check for bad combinations and alert the user
       /* ArrayList<DrugClass> classesDosedOnDate = new ArrayList<DrugClass>();
        String dateSelected = dateString.substring(0,10);
        for(int x = 0;x < drugManager.getAllDoses().size();x++){
            Dose tempDose = drugManager.getAllDoses().get(x);
            String date = tempDose.getDate().substring(0,10);
            if(date.equalsIgnoreCase(dateSelected)){

            }
        }*/


       // i.putExtra("Dose", newDose);
        Log.i("testing", "Dose added with date: " + dateString);
        Toast.makeText(this, "Log Added! (for time " + dateString + " )",Toast.LENGTH_LONG).show();
       // Toast.makeText(this, "Substring 0-10 is " + dateString.substring(0,10),Toast.LENGTH_LONG).show();

        Dose newDose = new Dose(selectedDrug, selectedMetric, selectedRoa, doseAmount, dateString);
        Log.i("testing", "Dose added!");

        //check if custom notification requirements are met

        for(int x = 0;x < drugManager.getNotifications().size();x++){
           lolbellum.druglog.Notification tempNotification = drugManager.getNotifications().get(x);
            Drug tempDrug = drugManager.getDrug(tempNotification.getDrugId());
            Metric tempMetric = tempNotification.getMetric();
            if(tempDrug.getDrugId() == newDose.getDrug().getDrugId()){
                if(tempNotification.isActive()){
                    //add up total dosage for notification time period
                    double totalDosage = 0;
                    for(int i = 0;i < drugManager.getAllDoses().size();i++){
                        Dose tempDose = drugManager.getAllDoses().get(i);
                        if(tempDrug.getDrugId() == tempDose.getDrug().getDrugId()){
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                            try{
                                Date date = sdf.parse(tempDose.getDate());
                                long doseTime = date.getTime();
                                long currentTime = System.currentTimeMillis();
                                if(doseTime <= currentTime){
                                    long timeRangeStart = currentTime - tempNotification.getEndTime();
                                    if(doseTime >= timeRangeStart){
                                        //TODO - convert metrics before adding to total dose amount
                                        totalDosage += tempDose.getDoseAmount();
                                    }
                                }
                            } catch (Exception e){

                            }

                        }
                    }
                    //TODO - make sure both amounts are converted to grams before comparing
                    if(tempNotification.getDoseAmount() <= totalDosage){
                        //notify the user, may want to move this to the onresult of Calendar.class so that they are notified after this activity closes
                        String ns = Context.NOTIFICATION_SERVICE;
                        NotificationManager notificationManager = (NotificationManager) getSystemService(ns);
                        Intent notificationIntent = new Intent();
                        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
                        Notification notification = new Notification(R.drawable.notification_template_icon_bg, "Drug Log Custom Notification", System.currentTimeMillis());
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        notification.setLatestEventInfo(this, "Custom Usage Notification", "You have dosed more than " + tempNotification.getDoseAmount() + " " + tempMetric.getMetricName(), contentIntent);
                        //int notificationId = 001;
                        notificationManager.notify(0,notification);
                    }
                }
            }
        }

        for(int x = 0;x < drugManager.getReminders().size();x++){
            Reminder reminder = drugManager.getReminders().get(x);
            if(reminder.getDrug().getDrugName().equalsIgnoreCase(selectedDrug.getDrugName())){
                if(reminder.isActive()){
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    try{
                        Date date = sdf.parse(dateString);
                        long doseTime = date.getTime();
                        long endTime = doseTime + reminder.getTimeFor();
                        long triggerAtMillis = 0;
                        if(endTime > System.currentTimeMillis()){
                            if(doseTime - 60000 > System.currentTimeMillis()) {
                                 triggerAtMillis = System.currentTimeMillis() + reminder.getTime();
                            } else {
                                 triggerAtMillis = doseTime;
                            }
                            Intent i = new Intent(this, ReminderReceiver.class);
                            i.putExtra("ReminderText",reminder.getText());
                            i.putExtra("TimeInterval", reminder.getTime());
                            i.putExtra("TimeFor", reminder.getTimeFor());
                            i.putExtra("EndTime", endTime);

                            PendingIntent m_pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);

                            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, reminder.getTime(), m_pendingIntent);
                            Log.i("testing", "Repeating alarm set");
                        }
                    } catch (Exception e){

                    }
                }
            }
        }

        if(checkBoxNotifyMe.isChecked()){
            Log.i("testing", "Check box is checked, will notify");
            String ns = Context.NOTIFICATION_SERVICE;

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);


            int icon = R.drawable.notification_template_icon_bg;
            CharSequence tickerText = "Drug Log Notification"; // ticker-text
            long when = System.currentTimeMillis();

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, m_hour);
            c.set(Calendar.MINUTE, m_minute);
            c.set(Calendar.MONTH, m_month);
            c.set(Calendar.DAY_OF_MONTH, m_day);
            c.set(Calendar.YEAR, m_year);

            Context context = getApplicationContext();
            CharSequence contentTitle = "Drug Log Notification";
            CharSequence contentText = "Reminder to dose " + doseAmount + " " + selectedMetric.getMetricName() + " of " + selectedDrug.getDrugName();
            Intent notificationIntent = new Intent(this,NotificationReceiver.class);
            notificationIntent.putExtra("text",contentText);
            PendingIntent contentIntent = PendingIntent.getBroadcast(this,0,notificationIntent,0);
            //PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
           // Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
           // notification.flags |= Notification.FLAG_AUTO_CANCEL;
           // notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            try{
                Date date = sdf.parse(newDose.getDate());
                long doseTime = date.getTime();
                // and this
                // int HELLO_ID = 1;
                //mNotificationManager.notify(1, notification);
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, doseTime, contentIntent);
                Log.i("testing", "Alarm set to notify for future dose at " + newDose.getDate());
            } catch (Exception e){

            }


        } else {
            Log.i("testing", "Check box isn't checked");
        }

        setResult(Activity.RESULT_OK);
        finish();
        //int dose = Integer.valueOf(doseEditText.getText().toString());
    }
    @Override
    public void onBackPressed() {
        ListView drugClassListView = (ListView) findViewById(R.id.drug_class_list);
        if(onClassList == false){
            drugListView.setAdapter(drugsClassList);
            onClassList = true;
            return;
        }
        //Intent i = new Intent(this, lolbellum.druglog.Calendar.class);
        finish();
       // startActivity(i);
    }
}
