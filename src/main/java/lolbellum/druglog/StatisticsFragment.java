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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class StatisticsFragment extends Fragment {

    private static  DrugManager drugManager = Calendar.drugManager;

   // ListView noteListView;
    //ListAdapter noteList;

    private String m_drugName;
    private  Drug m_drug;
    private String m_selectedTime = "All Time";
    private String m_selectedMetricName = "Grams";
    private Metric m_selectedMetric = drugManager.getMetric(m_selectedMetricName);


    public static  StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        return fragment;
    }

    public StatisticsFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);


      //  m_selectedMetric = drugManager.getMetric(m_selectedMetricName);

        //m_drugName = getArguments().getString("drugname");
       // m_drug = drugManager.getDrugFromName(m_drugName);
        m_drug = drugManager.getDrug(getArguments().getLong("drugid"));
        m_drugName = m_drug.getDrugName();
        TextView textDrugName = (TextView) rootView.findViewById(R.id.fragment_statistics_drugname);
        TextView textTimesDosed = (TextView) rootView.findViewById(R.id.text_times_dosed);
        TextView textAverageDose = (TextView) rootView.findViewById(R.id.text_most_used_drug_name);
        TextView textTotalDosage = (TextView) rootView.findViewById(R.id.text_total_dosage);
        TextView textSmallestDose = (TextView) rootView.findViewById(R.id.text_smallest_dose);
        TextView textLargestDose = (TextView) rootView.findViewById(R.id.text_largest_dose);
        TextView textMostUsedRoa = (TextView) rootView.findViewById(R.id.text_mostused_roa);

        textDrugName.setText(m_drugName);
        textTimesDosed.setText("N/A");
        textAverageDose.setText("N/A");
        textTotalDosage.setText("N/A");
        textSmallestDose.setText("N/A");
        textLargestDose.setText("N/A");
        textMostUsedRoa.setText("N/A");

        //setting up timerange spinner
        final Spinner timeSpinner = (Spinner) rootView.findViewById(R.id.fragment_statistics_timespinner);
        String timeRanges[] = new String[] {"Today", "This Week", "This Month", "This Year", "All Time"};
        ArrayAdapter<String> timeSpinnerAdapter = new ArrayAdapter<String>(rootView.getContext(),R.layout.support_simple_spinner_dropdown_item, timeRanges);
        timeSpinner.setAdapter(timeSpinnerAdapter);
        timeSpinner.setSelection(4);

        timeSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("testing", "Item selected on the timeSpinner");
                        if(position == 0){
                            m_selectedTime = "Today";
                        } else if(position == 1){
                            m_selectedTime = "This Week";
                        } else if(position == 2){
                            m_selectedTime = "This Month";
                        } else if(position == 3){
                            m_selectedTime = "This Year";
                        } else if(position == 4){
                            m_selectedTime = "All Time";
                        }

                        updateView(rootView);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        //setting up metric spinner
        final Spinner metricSpinner = (Spinner) rootView.findViewById(R.id.fragment_statistics_metricspinner);
        String[] metricNames = new String[drugManager.getAllMetrics().size()];
        for(int x = 0;x < drugManager.getAllMetrics().size();x++){
            metricNames[x] = drugManager.getAllMetrics().get(x).getMetricName();
        }
        ArrayAdapter<String> metricSpinnerAdapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.support_simple_spinner_dropdown_item, metricNames);
        metricSpinner.setAdapter(metricSpinnerAdapter);
        metricSpinner.setSelection(0);

        metricSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("testing", "Item selected on the metricSpinner");
                        m_selectedMetricName = (String) metricSpinner.getItemAtPosition(position);
                        Log.i("testing","The item selected on the metricSpinner is " + m_selectedMetricName);
                        m_selectedMetric = drugManager.getMetric(m_selectedMetricName);
                        updateView(rootView);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        //button listeners
        Button buttonViewUsage = (Button) rootView.findViewById(R.id.fragment_statistics_usagegrapghbutton);
        Button buttonViewDosage = (Button) rootView.findViewById(R.id.fragment_statistics_dosagegrapghbutton);
        Button buttonViewRoas = (Button) rootView.findViewById(R.id.fragment_statistics_roagraphbutton);

        buttonViewUsage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int numberOfDoses = 0;
                        for(int x = 0;x < drugManager.getAllDoses().size();x++){
                            Dose tempDose = drugManager.getAllDoses().get(x);
                            if(tempDose.getDrug().getDrugName().equalsIgnoreCase(m_drugName)){
                                numberOfDoses++;
                            }
                        }

                        if(numberOfDoses == 0){
                            Toast.makeText(rootView.getContext(), "You have no doses to graph!!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Intent i = new Intent(rootView.getContext(), UsageGraph.class);
                        i.putExtra("DrugName",m_drugName);
                        i.putExtra("TimeFrame", m_selectedTime);
                        i.putExtra("Metric", m_selectedMetricName);
                        startActivity(i);
                    }
                }
        );

        buttonViewDosage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );

        buttonViewRoas.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int numberOfDoses = 0;
                        for(int x = 0;x < drugManager.getAllDoses().size();x++){
                            Dose tempDose = drugManager.getAllDoses().get(x);
                            if(tempDose.getDrug().getDrugName().equalsIgnoreCase(m_drugName)){
                                numberOfDoses++;
                            }
                        }
                        if(numberOfDoses == 0){
                            Toast.makeText(rootView.getContext(),"You have no doses to graph!",Toast.LENGTH_LONG).show();
                            return;
                        }

                        Intent i = new Intent(rootView.getContext(),RoaGraph.class);
                        i.putExtra("DrugName",m_drugName);
                        i.putExtra("TimeFrame",m_selectedTime);
                        startActivity(i);
                    }
                }
        );
        return rootView;
    }

    public void updateView(View view){
        m_selectedMetric = drugManager.getMetric(m_selectedMetricName);
        TextView textTimesDosed = (TextView) view.findViewById(R.id.text_times_dosed);
        TextView textAverageDose = (TextView) view.findViewById(R.id.text_most_used_drug_name);
        TextView textTotalDosage = (TextView) view.findViewById(R.id.text_total_dosage);
        TextView textSmallestDose = (TextView) view.findViewById(R.id.text_smallest_dose);
        TextView textLargestDose = (TextView) view.findViewById(R.id.text_largest_dose);
        TextView textMostUsedRoa = (TextView) view.findViewById(R.id.text_mostused_roa);

        ArrayList<Dose> doses = new ArrayList<Dose>();
        long currentTime = System.currentTimeMillis();
        //if timeRange is 0 it will be All Time
        long timeRange = 0L;
        if(m_selectedTime == "Today"){
            timeRange = 86400000;
        } else if(m_selectedTime == "This Week"){
            //Long monthRange = Long.parseLong("86400000 * 7");
            timeRange = 86400000L * 7L;
        } else if(m_selectedTime == "This Month"){
            timeRange = 86400000L * 30L;
        } else if(m_selectedTime == "This Year"){
            timeRange = 86400000L * 365L;
        } else if(m_selectedTime == "All Time"){
            timeRange = 0;
        }

        for(int x = 0;x < drugManager.getAllDoses().size();x++){
            Dose tempDose = drugManager.getAllDoses().get(x);
            //run if the dose was of our selected drug
            if(tempDose.getDrug().getDrugName().equalsIgnoreCase(m_drug.getDrugName())) {
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
                    doses.add(tempDose);
                    //if the timeRange is anything else
                } else {
                    long timeRangeToBeIn = (currentTime - timeRange);
                    //if the dose is in the correct timerange
                    if (time > timeRangeToBeIn) {
                        doses.add(tempDose);
                    }
                }
            }
            }
        }

        if(doses.size() > 0){
            double totalDoseAmount = 0;
            double smallestDose = 1000000000;
            double largestDose = 0;

            //favorite roa
            int[] roaCount = new int[drugManager.getAllRoas().size()];
            for(int x = 0; x < drugManager.getAllRoas().size();x++){
                roaCount[x] = 0;
            }

            for(int x = 0;x < doses.size();x++){
                Dose tempDose = doses.get(x);
                Metric tempDoseMetric = tempDose.getMetric();
                Log.i("testing", "tempDose.getMetric().getMetricName() == " + tempDose.getMetric().getMetricName());
                Log.i("testing", "m_selectedMetric.getMetricName() is " + m_selectedMetric.getMetricName());
                double tempDoseAmount = tempDose.getDoseAmount();
                //double doseAmount = tempDoseAmount;
                //if(drugManager.convertMetrics(tempDoseAmount, tempDoseMetric, m_selectedMetric) != 0) {
                Log.i("testing","There are " + drugManager.getMetricConversions().size() + " metric conversions");
                    Log.i("testing", "ConvertingMetrics");
                    double doseAmount = drugManager.convertMetrics(tempDoseAmount, tempDoseMetric, drugManager.getMetric(m_selectedMetricName));
                //}

                if(doseAmount < smallestDose){
                    smallestDose = doseAmount;
                }
                if(doseAmount > largestDose){
                    largestDose = doseAmount;
                }
                totalDoseAmount += doseAmount;

                for(int i = 0; i < drugManager.getAllRoas().size();i++){
                    if(tempDose.getRoa().getRoaName().equalsIgnoreCase(drugManager.getAllRoas().get(i).getRoaName())){
                        roaCount[i] = roaCount[i] + 1;
                    }
                }
            }

            int positionOfMostUsedRoa = -1;
            int timesUsedRoa = 0;
            for(int x = 0;x < roaCount.length;x++){
                if(roaCount[x] > timesUsedRoa){
                    timesUsedRoa = roaCount[x];
                    positionOfMostUsedRoa = x;
                }
            }

            DecimalFormat df = new DecimalFormat("0.000");


            textTimesDosed.setText(String.valueOf(doses.size()));
            if((totalDoseAmount / doses.size()) >= 0.001) {
                textAverageDose.setText(df.format((totalDoseAmount / doses.size())) + " " + m_selectedMetricName);
            } else {
                textAverageDose.setText("<0.001 " + m_selectedMetricName);
            }
            if(totalDoseAmount >= 0.001) {
                textTotalDosage.setText(df.format(totalDoseAmount) + " " + m_selectedMetricName);
            } else {
                textTotalDosage.setText("<0.001 " + m_selectedMetricName);
            }
            if(smallestDose >= 0.001){
                textSmallestDose.setText(df.format(smallestDose) + " " + m_selectedMetricName);
            } else {
                textSmallestDose.setText("<0.001 " + m_selectedMetricName);
            }
            if(largestDose >= 0.001) {
                textLargestDose.setText(df.format(largestDose) + " " + m_selectedMetricName);
            } else {
                textLargestDose.setText("<0.001 " + m_selectedMetricName);
            }
            if(positionOfMostUsedRoa != -1) {
                textMostUsedRoa.setText(drugManager.getAllRoas().get(positionOfMostUsedRoa).getRoaName());
            }
        } else {
            Log.i("testing", "No doses found");
            textTimesDosed.setText("0");
            textAverageDose.setText("0.000");
            textTotalDosage.setText("0.000");
            textSmallestDose.setText("0.000");
            textLargestDose.setText("0.000");
            textMostUsedRoa.setText("N/A");
        }

    }

    public void clickViewUsageGraph(View view){
        Log.i("testing", "the button to view the usage graph was clicked");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // ((DrugView) activity).onSectionAttached(getArguments().getInt("SectionNumber"));
    }


}
