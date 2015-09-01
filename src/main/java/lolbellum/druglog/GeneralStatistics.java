package lolbellum.druglog;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class GeneralStatistics extends ActionBarActivity {

    private DrugManager drugManager = Calendar.drugManager;
    DrugClass selectedDrugClass;
    Metric selectedMetric;
    String selectedTime = "All Time";
    private int numberOfDoses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_statistics);

        //add possible intent bundle here just in case i want to go to this screen straight from drug class view(button that says "Stats")

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("General Statistics");

        final Spinner spinnerDrugClass = (Spinner) findViewById(R.id.spinner_drug_classes);
        String[] drugClassNames = new String[drugManager.getDrugClasses().size() + 1];
        drugClassNames[0] = "All Drugs";
        for(int x = 1;x < drugManager.getDrugClasses().size() + 1;x++){
            drugClassNames[x] = drugManager.getDrugClasses().get(x - 1).getDrugClassName();
        }

        ArrayAdapter<String> adapterDrugClassNames = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, drugClassNames){
            public View getView(int position, View convertView,ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);
                return v;
            }
            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;
            }
        };

        //ArrayAdapter<String> adapterDrugClassNames = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, drugClassNames);
        spinnerDrugClass.setAdapter(adapterDrugClassNames);

        spinnerDrugClass.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            String selectedDrugClassName = (String) spinnerDrugClass.getItemAtPosition(position);
                            selectedDrugClass = drugManager.getDrugClass(selectedDrugClassName);
                            Log.i("testing",  selectedDrugClass.getDrugClassName() + " was selected on the Drug Class spinner in GeneralStatistics");
                        } else {
                            selectedDrugClass = null;
                            Log.i("testing","All Drugs was selected on the Drug Class spinner in GeneralStatistics");
                        }
                        updateView();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                }
        );

        final Spinner timeSpinner = (Spinner) findViewById(R.id.fragment_statistics_timespinner);
        String timeRanges[] = new String[] {"Today", "This Week", "This Month", "This Year", "All Time"};
        ArrayAdapter<String> timeSpinnerAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, timeRanges);
        timeSpinner.setAdapter(timeSpinnerAdapter);
        timeSpinner.setSelection(4);

        timeSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("testing", "Item: " + timeSpinner.getItemAtPosition(position) + " selected on the timeSpinner");
                        if (position == 0) {
                            selectedTime = "Today";
                        } else if (position == 1) {
                            selectedTime = "This Week";
                        } else if (position == 2) {
                            selectedTime = "This Month";
                        } else if (position == 3) {
                            selectedTime = "This Year";
                        } else if (position == 4) {
                            selectedTime = "All Time";
                        }

                        updateView();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        final Spinner metricSpinner = (Spinner) findViewById(R.id.fragment_statistics_metricspinner);
        String[] metricNames = new String[drugManager.getAllMetrics().size()];
        for(int x = 0;x < drugManager.getAllMetrics().size();x++){
            metricNames[x] = drugManager.getAllMetrics().get(x).getMetricName();
        }
        ArrayAdapter<String> metricSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, metricNames);
        metricSpinner.setAdapter(metricSpinnerAdapter);
        metricSpinner.setSelection(0);

        metricSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("testing", "Item: " + metricSpinner.getItemAtPosition(position) + " selected on the metricSpinner");
                        String selectedMetricName = (String) metricSpinner.getItemAtPosition(position);
                        selectedMetric = drugManager.getMetric(selectedMetricName);
                        updateView();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

    }



    public void updateView(){
        TextView textViewNumberOfDoses = (TextView) findViewById(R.id.text_view_number_of_doses);
        TextView textViewMostUsedDrug = (TextView) findViewById(R.id.text_view_most_used_drug);
        TextView textViewMostUsedRoa = (TextView) findViewById(R.id.text_view_most_used_roa);
        TextView textViewEarliestDose = (TextView) findViewById(R.id.text_view_earliest_dose);
        TextView textViewLatestDose = (TextView) findViewById(R.id.text_view_latest_dose);

        if(selectedMetric == null || selectedTime == null){
            Log.i("testing","Either the Selected Metric or the Selected Time or the Selected Drug Class is null in GeneralStatistics");
            return;
        }
        ArrayList<Drug> includedDrugs = new ArrayList<Drug>();
        if(selectedDrugClass == null){
            includedDrugs = drugManager.getDrugs();
        } else {
            includedDrugs = selectedDrugClass.getDrugs();
        }
        long currentTime = System.currentTimeMillis();
        //if timeRange is 0 it will be All Time
        long timeRange = 0L;
        if(selectedTime == "Today"){
            timeRange = 86400000;
        } else if(selectedTime == "This Week"){
            //Long monthRange = Long.parseLong("86400000 * 7");
            timeRange = 86400000L * 7L;
        } else if(selectedTime == "This Month"){
            timeRange = 86400000L * 30L;
        } else if(selectedTime == "This Year"){
            timeRange = 86400000L * 365L;
        } else if(selectedTime == "All Time"){
            timeRange = 0;
        }

        ArrayList<Dose> includedDoses = new ArrayList<Dose>();
        for(int x = 0;x < drugManager.getAllDoses().size();x++){
            Dose tempDose = drugManager.getAllDoses().get(x);
            for(int i = 0; i < includedDrugs.size();i++){
                Drug tempDrug = includedDrugs.get(i);
                if(tempDrug.getDrugId() == tempDose.getDrug().getDrugId()){
                    //check if in timerange
                    String doseDate = tempDose.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    long time = 0;
                    try {
                        Date date = sdf.parse(doseDate);
                        time = date.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(time < currentTime){
                        //run if the timeRange is All Time
                        if (timeRange == 0) {
                            includedDoses.add(tempDose);
                            //if the timeRange is anything else
                        } else {
                            long timeRangeToBeIn = (currentTime - timeRange);
                            //if the dose is in the correct timerange
                            if (time > timeRangeToBeIn) {
                                includedDoses.add(tempDose);
                            }
                        }
                    }
                   // includedDoses.add(tempDose);
                }
            }
        }
        numberOfDoses = includedDoses.size();
        if(includedDoses.size() > 0) {
            int numberOfDoses = includedDoses.size();
            long earliestTime = 0;
            long latestTime = 0;

            int[] drugCount = new int[drugManager.getDrugs().size()];
            for (int x = 0; x < drugCount.length; x++) {
                drugCount[x] = 0;
            }
            int[] roaCount = new int[drugManager.getAllRoas().size()];
            for (int x = 0; x < roaCount.length; x++) {
                roaCount[x] = 0;
            }

            for (int x = 0; x < includedDoses.size(); x++) {
                Dose tempDose = includedDoses.get(x);

                for (int i = 0; i < drugManager.getDrugs().size(); i++) {
                    Drug tempDrug = drugManager.getDrugs().get(i);
                    if (tempDose.getDrug().getDrugId() == tempDrug.getDrugId()) {
                        drugCount[i] += 1;
                    }
                }
                for (int i = 0; i < drugManager.getAllRoas().size(); i++) {
                    RouteOfAdministration tempRoa = drugManager.getAllRoas().get(i);
                    if (tempDose.getRoa().getId() == tempRoa.getId()) {
                        roaCount[i] += 1;
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
               try {
                   long doseTime = sdf.parse(tempDose.getDate()).getTime();
                  if(earliestTime == 0){
                      earliestTime = doseTime;
                  } else {
                      if(doseTime < earliestTime){
                          earliestTime = doseTime;
                      }
                  }
                   if(doseTime > latestTime){
                       latestTime = doseTime;
                   }
               } catch (Exception e){
                   Log.i("testing","Couldn't parse tempDose's date in GeneralStatistics");
               }

            }
            int positionDrugUsedMost = -1;
            int timesDrugUsed = 0;
            for (int x = 0; x < drugCount.length; x++) {
                if (drugCount[x] > timesDrugUsed) {
                    timesDrugUsed = drugCount[x];
                    positionDrugUsedMost = x;
                }
            }
            int positionRoaUsedMost = -1;
            int timesRoaUsed = 0;
            for (int x = 0; x < roaCount.length; x++) {
                if (roaCount[x] > timesRoaUsed) {
                    timesRoaUsed = roaCount[x];
                    positionRoaUsedMost = x;
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String earliestDate = sdf.format(earliestTime);
            String latestDate = sdf.format(latestTime);

            textViewNumberOfDoses.setText(String.valueOf(numberOfDoses));
            textViewMostUsedDrug.setText(drugManager.getDrugs().get(positionDrugUsedMost).getDrugName());
            textViewMostUsedRoa.setText(drugManager.getAllRoas().get(positionRoaUsedMost).getRoaName());
                textViewEarliestDose.setText(earliestDate);
                textViewLatestDose.setText(latestDate);
        } else {
            textViewNumberOfDoses.setText("0");
            textViewMostUsedDrug.setText("N/A");
            textViewMostUsedRoa.setText("N/A");
            textViewEarliestDose.setText("N/A");
            textViewLatestDose.setText("N/A");
        }
    }

    public void clickViewUsageGraph(View view){
        Log.i("testing","View Usage Graph clicked in General Statistics");
        CheckBox checkBoxShowIndividual = (CheckBox) findViewById(R.id.check_box_show_individual_drugs);
        if(numberOfDoses == 0){
            Toast.makeText(this,"There are no doses to graph",Toast.LENGTH_LONG).show();
            return;
        }
        Intent i = new Intent(this,GeneralUsageGraph.class);
        if(selectedDrugClass != null){
            i.putExtra("ClassName",selectedDrugClass.getDrugClassName());
        }
        i.putExtra("WantIndividual",checkBoxShowIndividual.isChecked());
        i.putExtra("TimeFrame",selectedTime);
        startActivity(i);
    }
    public void clickViewDosageGraph(View view){
        Log.i("testing","View Dosage Graph clicked in General Statistics");
        if(numberOfDoses == 0){
            Toast.makeText(this,"There are no doses to graph",Toast.LENGTH_LONG).show();
            return;
        }
    }
    public void clickViewRoaGraph(View view){
        CheckBox checkBoxShowIndividual = (CheckBox) view.findViewById(R.id.check_box_show_individual_drugs);
        Log.i("testing", "View Roa Graph clicked in General Statistics");
        if(numberOfDoses == 0){
            Toast.makeText(this,"There are no doses to graph",Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_general_statistics, menu);
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
