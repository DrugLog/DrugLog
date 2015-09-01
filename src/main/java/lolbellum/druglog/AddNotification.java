package lolbellum.druglog;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class AddNotification extends ActionBarActivity {

    private DrugManager drugManager = Calendar.drugManager;

    private boolean onClasses;

    ListView listViewSelectDrug;
    ListAdapter adapterSelectDrug;

    private Drug selectedDrug = null;
    private Metric selectedMetric;
    String m_timeFrame = "All Time";
    long metricId = -1;
    long m_endTime = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Custom Notification");

        listViewSelectDrug = (ListView) findViewById(R.id.list_view_select_drug);
        final TextView textViewSelectedDrug = (TextView)findViewById(R.id.text_view_selected_drug);
        EditText editTextDoseAmount = (EditText) findViewById(R.id.edit_text_dose_amount);
        Spinner spinnerMetric = (Spinner) findViewById(R.id.spinner_metric_list);
        Spinner spinnerTimeFrame = (Spinner) findViewById(R.id.spinner_time_frame);

        adapterSelectDrug = new DrugclassListAdapter(this, drugManager.getDrugClasses());
        listViewSelectDrug.setAdapter(adapterSelectDrug);
        onClasses = true;

        String[] metricNames = new String[drugManager.getAllMetrics().size()];
        for(int x = 0;x < drugManager.getAllMetrics().size();x++){
            metricNames[x] = drugManager.getAllMetrics().get(x).getMetricName();
        }
        spinnerMetric.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, metricNames));
        String[] timeFrames = new String[]{"Hour(s)","Day(s)","Week(s)","Month(s)","Year(s)","All Time"};
        spinnerTimeFrame.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, timeFrames));

        spinnerMetric.setSelection(0);
        spinnerTimeFrame.setSelection(5);

        /*listViewSelectDrug.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("testing", "Item Selected in ListView");
                        if (onClasses) {
                            ArrayList<Drug> drugsToShow = new ArrayList<Drug>();
                            if (position == 0) {
                                drugsToShow = drugManager.getDrugs();
                            } else {
                                DrugClass selectedClass = (DrugClass) parent.getSelectedItem();
                                drugsToShow = selectedClass.getDrugs();
                            }
                            adapterSelectDrug = new DrugListAdapter(parent.getContext(), drugsToShow);
                            listViewSelectDrug.setAdapter(adapterSelectDrug);
                            onClasses = false;
                        } else {
                            selectedDrug = (Drug) parent.getSelectedItem();
                            textViewSelectedDrug.setText("Drug selected: " + selectedDrug.getDrugName());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );*/

        listViewSelectDrug.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("testing", "Item Clicked in ListView");
                if (onClasses) {
                    ArrayList<Drug> drugsToShow = new ArrayList<Drug>();
                    if (position == 0) {
                        drugsToShow = drugManager.getDrugs();
                    } else {
                        DrugClass selectedClass = (DrugClass) parent.getItemAtPosition(position);
                        drugsToShow = selectedClass.getDrugs();
                    }
                    adapterSelectDrug = new DrugListAdapter(parent.getContext(), drugsToShow);
                    listViewSelectDrug.setAdapter(adapterSelectDrug);
                    onClasses = false;
                } else {
                    selectedDrug = (Drug) parent.getItemAtPosition(position);
                    textViewSelectedDrug.setText("Drug selected: " + selectedDrug.getDrugName());
                }
            }
        });

        spinnerMetric.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("testing", "Item selected on spinner metric");
                        Metric selectedMetric = drugManager.getMetric((String) parent.getItemAtPosition(position));
                        metricId = selectedMetric.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        spinnerTimeFrame.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("testing","Item selected on spinner time frame");
                        m_timeFrame = (String) parent.getSelectedItem();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

    }

    public void clickAddNotification(View view){
        Log.i("testing","Add Notification Clicked!");

        ListView listViewSelectDrug = (ListView) findViewById(R.id.list_view_select_drug);
        TextView textViewSelectedDrug = (TextView)findViewById(R.id.text_view_selected_drug);
        EditText editTextDoseAmount = (EditText) findViewById(R.id.edit_text_dose_amount);
        EditText editTextTimeAmount = (EditText) findViewById(R.id.edit_Text_time_amount);
        Spinner spinnerMetric = (Spinner) findViewById(R.id.spinner_metric_list);
        Spinner spinnerTimeFrame = (Spinner) findViewById(R.id.spinner_time_frame);

        if(selectedDrug == null){
            Toast.makeText(this,"You must select a drug!",Toast.LENGTH_LONG).show();
            return;
        }
        if(editTextDoseAmount.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(this,"You must enter a dose amount!",Toast.LENGTH_LONG).show();
            return;
        }
        if(spinnerMetric.getSelectedItem() != "All Time"){
            if(editTextTimeAmount.getText().toString().equalsIgnoreCase("")){
                Toast.makeText(this, "You must enter a quantity of time!",Toast.LENGTH_LONG).show();
                return;
            }
        }

        int timeQuantity = Integer.valueOf(editTextTimeAmount.getText().toString());
        double doseAmount = Double.valueOf(editTextDoseAmount.getText().toString());

        if(m_timeFrame.equalsIgnoreCase("Hour(s)")){
            m_endTime = TimeUnit.HOURS.toMillis(timeQuantity);
        } else if(m_timeFrame.equalsIgnoreCase("Day(s)")){
            m_endTime = TimeUnit.DAYS.toMillis(timeQuantity);
        } else if(m_timeFrame.equalsIgnoreCase("Week(s)")){
            m_endTime = TimeUnit.DAYS.toMillis(timeQuantity * 7);
        } else if(m_timeFrame.equalsIgnoreCase("Month(s)")){
            m_endTime = TimeUnit.DAYS.toMillis(timeQuantity * 30);
        } else if(m_timeFrame.equalsIgnoreCase("Year(s)")){
            m_endTime = TimeUnit.DAYS.toMillis(timeQuantity * 365);
        } else if(m_timeFrame.equalsIgnoreCase("All Time")){
            m_endTime = -1;
        }

        Notification notification = new Notification(selectedDrug.getDrugId(), m_timeFrame, m_endTime,doseAmount,metricId);

        Intent i = new Intent();

        Toast.makeText(this,"Notification added!",Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed(){
        if(onClasses){
            finish();
        } else {
            adapterSelectDrug = new DrugclassListAdapter(this, drugManager.getDrugClasses());
            listViewSelectDrug.setAdapter(adapterSelectDrug);
            onClasses = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_notification, menu);
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
