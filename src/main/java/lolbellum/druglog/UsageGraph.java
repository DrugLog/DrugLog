package lolbellum.druglog;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class UsageGraph extends ActionBarActivity{

    private DrugManager drugManager = lolbellum.druglog.Calendar.drugManager;

    private String m_drugName;
    private Drug m_drug;
    private String m_timeFrame;
    private String m_selectedMetric;
    private long m_currentTime;
    private long m_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_graph);

        Bundle drugData = getIntent().getExtras();

        if(drugData == null){
            return;
        }

        m_drugName = drugData.getString("DrugName");
        m_drug = drugManager.getDrugFromName(m_drugName);
        m_timeFrame = drugData.getString("TimeFrame");
        m_selectedMetric = drugData.getString("Metric");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(m_drugName + " Usage/Time Graph");
        m_currentTime = System.currentTimeMillis();

        graph(m_timeFrame, m_selectedMetric);

        //setting up graph

        //usageGraph.getViewport().setScalable(true);
        //usageGraph.getGridLabelRenderer().setTextSize(5);
       // usageGraph.getGridLabelRenderer().invalidate(true, true);




    }



    private void graph(String timeFrame, String metric){

        GraphView usageGraph = (GraphView) findViewById(R.id.usage_graph);
        usageGraph.setTitle(m_drugName + " Times used/Time");
        usageGraph.getGridLabelRenderer().setVerticalAxisTitle("# of Times Dosed");
        usageGraph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        usageGraph.getGridLabelRenderer().setLabelFormatter(new BetterDateLabelFormatter(this));

        if(m_timeFrame.equalsIgnoreCase("Today")){
            usageGraph.setTitle("Times " + m_drugName + " used/Last 24 Hours");
            StaticLabelsFormatter hoursLabelFormatter = new StaticLabelsFormatter(usageGraph);
            usageGraph.getGridLabelRenderer().setHorizontalAxisTitle("Hour of day");

            Calendar c = Calendar.getInstance();


            String[] hoursLabel = new String[24];
            for(int x = 23; x > 0;x--){
                int hour = c.get(Calendar.HOUR_OF_DAY) - (23 - x);
                int minute = c.get(Calendar.MINUTE);
                if(hour < 0){
                    hour = hour + 24;
                }
                String hourString = "";
                String minuteString = "";
                String amOrPm = "";
                if(c.get(Calendar.AM_PM) == 0){
                    amOrPm = "AM";
                } else {
                    amOrPm = "PM";
                }

                if(minute < 10){
                    minuteString =  "0" + String.valueOf(minute);
                } else {
                    minuteString = String.valueOf(minute);
                }
                hourString = String.valueOf(hour);
                if(hour < 10){
                    hourString = "0" + String.valueOf(hour);
                }
                if(hour > 12){
                    if(hour - 12 < 10){
                        hourString = "0" + String.valueOf(hour - 12);
                    } else {
                        hourString = String.valueOf(hour - 12);
                    }
                } else if(hour == 0){
                    hourString = "12";
                } else {
                    if(hour < 10){
                        hourString = "0" + String.valueOf(hour);
                    } else {
                        hourString = String.valueOf(hour);
                    }
                }
                //hoursLabel[x] = hourString + ":" + minuteString + " " + amOrPm;
                hoursLabel[x] = String.valueOf(x);
            }
            hoursLabelFormatter.setHorizontalLabels(hoursLabel);
            usageGraph.getGridLabelRenderer().setLabelFormatter(hoursLabelFormatter);


        } else if(m_timeFrame.equalsIgnoreCase("This Week")){
            usageGraph.setTitle("Times " + m_drugName + " used/Last 7 Days");
            //StaticLabelsFormatter dateLabelFormatter = new StaticLabelsFormatter(usageGraph);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(m_currentTime);
            Date currentDate = c.getTime();


            String[] horizontalLabels = new String[7];

            for(int x = 0;x < 7;x++){
                // long time = 0;
                // if(x != 0) {
                //long time = m_currentTime - (86400000 * (x + 1));
                // } else {
                long time = m_currentTime - (86400000 * x);
                // }
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
                c.setTimeInMillis(m_currentTime);
                c.add(Calendar.DAY_OF_MONTH, -x);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                Date date = c.getTime();
                String dateText = sdf.format(date).substring(0,5);
                String monthString;
                String dayString;

                if(month < 10){
                    monthString = "0" + String.valueOf(month);
                } else {
                    monthString = String.valueOf(month);
                }
                if(day < 10){
                    dayString = "0" + String.valueOf(day);
                } else {
                    dayString = String.valueOf(day);
                }
                // dateText = monthString + "/" + dayString;
                horizontalLabels[6 - x] = dateText;
                // horizontalLabels[6 - x] = dateText;
            }



            ArrayList<Dose> doses = new ArrayList<Dose>();
            for(int x = 0;x < drugManager.getAllDoses().size();x++){
                Dose tempDose = drugManager.getAllDoses().get(x);
                //run if the dose was of our selected drug
                if(tempDose.getDrug().getDrugName().equalsIgnoreCase(m_drugName)){
                    String doseDate = tempDose.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    long time = 0;
                    try {
                        Date date = sdf.parse(doseDate);
                        m_time = date.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (m_time < m_currentTime) {
                        long timeRangeToBeIn = m_currentTime - (86400000L * 7L);
                        //if the dose is in the correct timerange
                        if (m_time > timeRangeToBeIn) {
                            doses.add(tempDose);
                        }
                    }
                }
            }

            double highestTimesDosed = 0;
            DataPoint[] dataPoints = new DataPoint[doses.size()];
            for(int x = 0; x < doses.size();x++){
                Dose tempDose = doses.get(x);
                String doseDate = tempDose.getDate();
                String dateString = doseDate.substring(0, 10);
                int timesDosed = timesDosedThatDay(doses, dateString);
                Log.i("testing","doseDate.substring(0,10) is " + dateString);

                if(timesDosed > highestTimesDosed){
                    highestTimesDosed = timesDosed;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                //  if(timesDosed > 0){
                try {
                    Date date = sdf.parse(doseDate);
                    dataPoints[x] = new DataPoint(date, timesDosed);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // }
            }


            BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPoints);
            //LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
             series.setSpacing(25);
            usageGraph.addSeries(series);

            usageGraph.getGridLabelRenderer().setNumHorizontalLabels(7);
            usageGraph.getViewport().setXAxisBoundsManual(true);
            c.setTimeInMillis(m_currentTime - (86400000L * 7L));
            Date minDate = new Date(m_currentTime - (86400000L * 6L));
            // usageGraph.getViewport().setMinX(currentDate.getTime() - (86400000 * 7));
            usageGraph.getViewport().setMinX(minDate.getTime());
            currentDate.setTime(m_currentTime);
            usageGraph.getViewport().setMaxX(currentDate.getTime());
            usageGraph.getGridLabelRenderer().setPadding(75);

            usageGraph.getViewport().setYAxisBoundsManual(true);
            usageGraph.getViewport().setMinY(0);
            usageGraph.getViewport().setMaxY(highestTimesDosed);
            // dateLabelFormatter.setHorizontalLabels(horizontalLabels);
            //usageGraph.getGridLabelRenderer().setLabelFormatter(dateLabelFormatter);

        } else if(m_timeFrame.equalsIgnoreCase("This Month")){
            usageGraph.setTitle("Times " + m_drugName + " used/Last 30 Days");

            Date currentDate = new Date(m_currentTime);
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);

            ArrayList<Dose> doses = new ArrayList<Dose>();
            for(int x = 0;x < drugManager.getAllDoses().size();x++){
                Dose tempDose = drugManager.getAllDoses().get(x);
                //run if the dose was of our selected drug
                if(tempDose.getDrug().getDrugName().equalsIgnoreCase(m_drugName)){
                    String doseDate = tempDose.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    long time = 0;
                    try {
                        Date date = sdf.parse(doseDate);
                        m_time = date.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (m_time < m_currentTime) {
                        long timeRangeToBeIn = m_currentTime - (86400000L * 30L);
                        //if the dose is in the correct timerange
                        if (m_time > timeRangeToBeIn) {
                            doses.add(tempDose);
                        }
                    }
                }
            }

            double highestTimesDosed = 0;
            DataPoint[] dataPoints = new DataPoint[doses.size()];
            for(int x = 0; x < doses.size();x++){
                Dose tempDose = doses.get(x);
                String doseDate = tempDose.getDate();
                String dateString = doseDate.substring(0, 10);
                int timesDosed = timesDosedThatDay(doses, dateString);
                Log.i("testing","doseDate.substring(0,10) is " + dateString);


                if(timesDosed > highestTimesDosed){
                    highestTimesDosed = timesDosed;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
               // SimpleDateFormat noTime = new SimpleDateFormat("MM/dd/yyyy");
                //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");



                // if(timesDosed > 0){
                try {

                        Date date = sdf.parse(doseDate);
                        dataPoints[x] = new DataPoint(date, timesDosed);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                 //}
            }



            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
            series.setThickness(8);
            usageGraph.addSeries(series);
            usageGraph.getGridLabelRenderer().setNumHorizontalLabels(12);
            usageGraph.getViewport().setXAxisBoundsManual(true);
            // c.setTimeInMillis(m_currentTime - (86400000 * 7));
            Date minDate = new Date(m_currentTime - (86400000L * 29L));
            // usageGraph.getViewport().setMinX(currentDate.getTime() - (86400000 * 7));
            usageGraph.getViewport().setMinX(minDate.getTime());
            currentDate.setTime(m_currentTime);
            usageGraph.getViewport().setMaxX(currentDate.getTime());
            usageGraph.getGridLabelRenderer().setPadding(75);

            usageGraph.getViewport().setYAxisBoundsManual(true);
            usageGraph.getViewport().setMinY(0);
            usageGraph.getViewport().setMaxY(highestTimesDosed + 1);
            usageGraph.getGridLabelRenderer().setHighlightZeroLines(true);

        } else if(m_timeFrame.equalsIgnoreCase("This Year")){
            usageGraph.setTitle("Times " + m_drugName + " used/Last 365 Days");
            usageGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
            Date currentDate = new Date(m_currentTime);
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);

            ArrayList<Dose> doses = new ArrayList<Dose>();
            for(int x = 0;x < drugManager.getAllDoses().size();x++){
                Dose tempDose = drugManager.getAllDoses().get(x);
                //run if the dose was of our selected drug
                if(tempDose.getDrug().getDrugName().equalsIgnoreCase(m_drugName)){
                    String doseDate = tempDose.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    long time = 0;
                    try {
                        Date date = sdf.parse(doseDate);
                        m_time = date.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (m_time < m_currentTime) {
                        long timeRangeToBeIn = m_currentTime - (86400000L * 365L);
                        //if the dose is in the correct timerange
                        if (m_time > timeRangeToBeIn) {
                            doses.add(tempDose);
                        }
                    }
                }
            }

            double highestTimesDosed = 0;
            DataPoint[] dataPoints = new DataPoint[doses.size()];
            for(int x = 0; x < doses.size();x++){
                Dose tempDose = doses.get(x);
                String doseDate = tempDose.getDate();
                String dateString = doseDate.substring(0, 10);
                int timesDosed = timesDosedThatDay(doses, dateString);
                Log.i("testing","doseDate.substring(0,10) is " + dateString);


                if(timesDosed > highestTimesDosed){
                    highestTimesDosed = timesDosed;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                // SimpleDateFormat noTime = new SimpleDateFormat("MM/dd/yyyy");
                //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");



                // if(timesDosed > 0){
                try {

                    Date date = sdf.parse(doseDate);
                    dataPoints[x] = new DataPoint(date, timesDosed);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //}
            }



            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);

            usageGraph.addSeries(series);
            usageGraph.getGridLabelRenderer().setNumHorizontalLabels(12);
            usageGraph.getViewport().setXAxisBoundsManual(true);
            // c.setTimeInMillis(m_currentTime - (86400000 * 7));
            Date minDate = new Date(m_currentTime - (86400000L * 364L));
            // usageGraph.getViewport().setMinX(currentDate.getTime() - (86400000 * 7));
            usageGraph.getViewport().setMinX(minDate.getTime());
            currentDate.setTime(m_currentTime);
            usageGraph.getViewport().setMaxX(currentDate.getTime());
            usageGraph.getGridLabelRenderer().setPadding(75);
            usageGraph.getGridLabelRenderer().setTextSize(30);
            usageGraph.getGridLabelRenderer().reloadStyles();


            usageGraph.getViewport().setYAxisBoundsManual(true);
            usageGraph.getViewport().setMinY(0);
            usageGraph.getViewport().setMaxY(highestTimesDosed + 1);
            usageGraph.getGridLabelRenderer().setHighlightZeroLines(true);

        } else if(m_timeFrame.equalsIgnoreCase("All Time")){
            usageGraph.setTitle("Times " + m_drugName + " used/All Time");
        } else {
            Log.i("testing", "The time frame selected is not an option. Something went wrong. Returning");

        }
    }


    public int timesDosedThatDay(ArrayList<Dose> doses, String tempDate){
        String date = tempDate;
        int timesDosed = 0;

        for(int x = 0; x < doses.size();x++){
            Dose tempDose = doses.get(x);
            String doseDate = tempDose.getDate();
            if(tempDate.equalsIgnoreCase(doseDate.substring(0,10))){
                timesDosed++;
            } else {
               // date = doseDate.substring(0,10);
            }
        }
        return timesDosed;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_usage_graph, menu);
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
