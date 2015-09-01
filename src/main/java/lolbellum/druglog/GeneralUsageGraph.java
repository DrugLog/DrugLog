package lolbellum.druglog;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GeneralUsageGraph extends AppCompatActivity {

    private DrugManager drugManager = Calendar.drugManager;
    private String timeFrame;
    private long time = -1;
    private boolean individualDrugs = false;
    private long currentTime;
    private DrugClass drugClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_usage_graph);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            Log.i("testing","Intent extras from GeneralStatistiics was null");
            return;
        }

        if(bundle.getString("ClassName") != null){
            drugClass = drugManager.getDrugClass(bundle.getString("ClassName"));
        }
        timeFrame = bundle.getString("TimeFrame");
        individualDrugs = bundle.getBoolean("WantIndividual");
        currentTime = System.currentTimeMillis();


        if(timeFrame.equalsIgnoreCase("Today")){
            time = TimeUnit.DAYS.toMillis(1);
        } else if(timeFrame.equalsIgnoreCase("This Week")){
            time = TimeUnit.DAYS.toMillis(7);
        } else if(timeFrame.equalsIgnoreCase("This Month")){
            time = TimeUnit.DAYS.toMillis(30);
        } else if(timeFrame.equalsIgnoreCase("This Year")){
            time = TimeUnit.DAYS.toMillis(365);
        } else if(timeFrame.equalsIgnoreCase("All Time")){
            time = -1;
        }
        graph();
    }

    public void graph(){
        GraphView usageGraph = (GraphView) findViewById(R.id.usage_graph);
        String className;
        if(drugClass == null){
            className = "All Drugs";

        } else {
            className = drugClass.getDrugClassName();
        }

       if(individualDrugs){
           usageGraph.setTitle("Drugs in " + className + " usage/" + timeFrame);
       } else {
           usageGraph.setTitle(className + " usage/" + timeFrame);
       }

        //gather doses
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        ArrayList<Dose> doses = new ArrayList<Dose>();
        for(int x = 0;x < drugManager.getAllDoses().size();x++){
            Dose tempDose = drugManager.getAllDoses().get(x);
            if(className.equalsIgnoreCase("All Drugs")){
                if(time == -1){
                    doses.add(tempDose);
                } else {
                    try{
                        Date doseDate = sdf.parse(tempDose.getDate());
                        long doseTime = doseDate.getTime();
                        if(doseTime <= currentTime && doseTime >= (currentTime - time)){
                            doses.add(tempDose);
                        }
                    } catch(Exception e){
                    }
                }
            } else {
                for(int i = 0; i < drugClass.getDrugs().size();i++){
                    Drug drug = drugClass.getDrugs().get(i);
                    if(drug.getDrugId() == tempDose.getDrug().getDrugId()){
                        if(time == -1){
                            doses.add(tempDose);
                        } else {
                            try{
                                Date doseDate = sdf.parse(tempDose.getDate());
                                long doseTime = doseDate.getTime();
                                if(doseTime <= currentTime && doseTime >= (currentTime - time)){
                                    doses.add(tempDose);
                                }
                            } catch(Exception e){
                            }
                        }
                    }
                }
            }
        }
        Log.i("testing","There are " + drugManager.getAllDoses().size() + " total doses in the database. ArrayList<Dose> doses contains " + doses.size() + " doses");
        //finding the date in milliseconds of the first dose ever to use as the lowest time if the Time Frame is AllTime
       if(time == -1){
           for(int x = 0;x < doses.size();x++){
               Dose tempDose = doses.get(x);
               try{
                   Date doseDate = sdf.parse(tempDose.getDate());
                   long doseTime = doseDate.getTime();
                   if(time == -1){
                       time = doseTime;
                   } else {
                       if(doseTime < time){
                           time = doseTime;
                       }
                   }
               } catch(Exception e){

               }
           }
       }

        //setting up graph properties
        usageGraph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        usageGraph.getGridLabelRenderer().setVerticalAxisTitle("# of Times Used");
        usageGraph.getGridLabelRenderer().setLabelFormatter(new BetterDateLabelFormatter(this));
        usageGraph.getGridLabelRenderer().setPadding(75);
        //setting minimum and maximum x values
        usageGraph.getViewport().setXAxisBoundsManual(true);
        usageGraph.getViewport().setMinX(currentTime - time);
        usageGraph.getViewport().setMaxX(currentTime);
        //usageGraph.getViewport().setYAxisBoundsManual(true);

        //setting how incremented the x labels are so that if Today is selected it will show by the hour and if This Month is selected it will shot by the Week
        //TODO - for the "Today" view we must make a custom label formatter that shows the hours simply as 0-23 because the current one with dates overlaps a shit load
        //TODO - For the yearly view, sort the doses and labels by each month(January, February,March,Etc). May need to create a new custom label formatter(Nov 2014,Dec 2014,Jan 2015,Feb 2015, March 2015, etc)
        if(timeFrame.equalsIgnoreCase("Today")){
            usageGraph.getGridLabelRenderer().setNumHorizontalLabels(24);
        } else if(timeFrame.equalsIgnoreCase("This Week")){
            usageGraph.getGridLabelRenderer().setNumHorizontalLabels(7);
        } else if(timeFrame.equalsIgnoreCase("This Month")){
            usageGraph.getGridLabelRenderer().setNumHorizontalLabels(4);
        } else if(timeFrame.equalsIgnoreCase("This Year")){
            usageGraph.getGridLabelRenderer().setNumHorizontalLabels(12);
        } else if(timeFrame.equalsIgnoreCase("All Time")){
            if(time <= TimeUnit.DAYS.toMillis(1)){
                usageGraph.getGridLabelRenderer().setNumHorizontalLabels(24);
            } else if(time <= TimeUnit.DAYS.toMillis(7)){
                usageGraph.getGridLabelRenderer().setNumHorizontalLabels(7);
            } else if(time <= TimeUnit.DAYS.toMillis(30)){
                usageGraph.getGridLabelRenderer().setNumHorizontalLabels(4);
            } else if(time <= TimeUnit.DAYS.toMillis(365)){
                usageGraph.getGridLabelRenderer().setNumHorizontalLabels(12);
            } else {

            }
        }
        //Set all the doses into dates based on how many horizontal labels there are. For example if the user is Viewing for the month, each datapoint should have times used for that entire weel(1/4 horizontal labels)
       // ArrayList<String> dates = new ArrayList<String>();
        ArrayList<Date> dates = new ArrayList<Date>();
                if(timeFrame.equalsIgnoreCase("Today")){
                    for(int x = 0;x < 24;x++){
                        dates.add(new Date(currentTime - ((time / 24) * x)));
                    }
                } else if(timeFrame.equalsIgnoreCase("This Week")){
                    for(int x = 0;x < 7;x++){
                        dates.add(new Date(currentTime - ((time / 7) * x)));
                    }
                } else if(timeFrame.equalsIgnoreCase("This Month")){
                    for(int x = 0;x < 4;x++){
                        dates.add(new Date(currentTime - ((time / 4) * x)));
                    }
                } else if(timeFrame.equalsIgnoreCase("This Year")){
                    for(int x = 0;x < 12;x++){
                        dates.add(new Date(currentTime - ((time / 12) * x)));
                    }
                } else if(timeFrame.equalsIgnoreCase("All Time")){
                    if(currentTime - time <= TimeUnit.DAYS.toMillis(1)){
                        for(int x = 0;x < 24;x++){
                            dates.add(new Date(currentTime - ((time / 24) * x)));
                        }
                    } else if(currentTime - time <= TimeUnit.DAYS.toMillis(7)){
                        for(int x = 0;x < 7;x++){
                            dates.add(new Date(currentTime - ((time / 7) * x)));
                        }
                    } else if(currentTime - time <= TimeUnit.DAYS.toMillis(30)){
                        for(int x = 0;x < 4;x++){
                            dates.add(new Date(currentTime - ((time / 4) * x)));
                        }
                    } else if(currentTime - time <= TimeUnit.DAYS.toMillis(365)){
                        for(int x = 0;x < 12;x++){
                            dates.add(new Date(currentTime - ((time / 12) * x)));
                        }
                    } else {
                        for(int x = 0;x < 12;x++){
                            dates.add(new Date(currentTime - ((time / 12) * x)));
                        }
                    }
                }

        for(int x = 0;x < dates.size();x++){
            Date date = dates.get(x);
          //  Log.i("testing","dates(" + x + ") is " + sdf.format(date));
        }

        Integer[] usageCount = new Integer[dates.size()];
        for(int v = 0;v < usageCount.length;v++){
            usageCount[v] = 0;
        }

        //create series with actual datapoints
        if(individualDrugs == false){

            DataPoint[] dataPoints = new DataPoint[dates.size()];

            for(int x = 0;x < doses.size();x++){
                Dose tempDose = doses.get(x);
                try{
                    Date doseDate = sdf.parse(tempDose.getDate());
                    long doseTime = doseDate.getTime();
                    long tempDifference = -1;
                    int closestDatePosition = 0;
                    for(int i = 0;i < dates.size();i++){
                        Date tempDate = dates.get(i);
                        long tempDateTime = tempDate.getTime();
                        long difference = Math.abs(tempDateTime - doseTime);
                        if(tempDifference == -1){
                            tempDifference = difference;
                            closestDatePosition = i;
                        } else {
                            if(difference < tempDifference){
                                tempDifference = difference;
                                closestDatePosition = i;
                            }
                        }
                    }
                    usageCount[closestDatePosition] += 1;
                } catch(Exception e){

                }
            }
            for(int x = 0;x < dates.size();x++){

            }
            for(int x = 0;x < usageCount.length;x++){

                dataPoints[x] = new DataPoint(dates.get(x),usageCount[x]);
            }

            Log.i("testing","There are " + dataPoints.length + " datapoints");
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
            usageGraph.addSeries(series);
        } else {
            usageGraph.getLegendRenderer().setVisible(true);
            usageGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
           ArrayList<Drug> drugs = new ArrayList<Drug>();
            if(className.equalsIgnoreCase("All Drugs")){
                drugs = drugManager.getDrugs();
            } else {
                drugs = drugClass.getDrugs();
            }
            for(int x = 0;x < drugs.size();x++){
                //Integer[] usageCount = new Integer[dates.size()];
               // for(int v = 0;v < usageCount.length;v++){
               //     usageCount[v] = 0;
              //  }
                Drug drug = drugs.get(x);
                ArrayList<Dose> drugDoses = new ArrayList<Dose>();
                for(int i = 0;i < doses.size();i++){
                    Dose tempDose = doses.get(i);
                    if(tempDose.getDrug().getDrugId() == drug.getDrugId()){
                        drugDoses.add(tempDose);
                    }
                }
                if(drugDoses.size() > 0){
                    DataPoint[] dataPoints = new DataPoint[dates.size()];
                    for(int i = 0;i < doses.size();i++){
                        Dose tempDose = doses.get(i);
                        try{
                            Date doseDate = sdf.parse(tempDose.getDate());
                            long doseTime = doseDate.getTime();
                            long tempDifference = -1;
                            int closestDatePosition = 0;
                            for(int z = 0;z < dates.size();z++){
                                Date tempDate = dates.get(z);
                                long tempDateTime = tempDate.getTime();
                                long difference = Math.abs(tempDateTime - doseTime);
                                if(tempDifference == -1){
                                    tempDifference = difference;
                                    closestDatePosition = z;
                                } else {
                                    if(difference < tempDifference){
                                        tempDifference = difference;
                                        closestDatePosition = z;
                                    }
                                }
                            }
                            usageCount[closestDatePosition] += 1;
                        } catch(Exception e){

                        }
                    }
                    for(int i = 0;i < usageCount.length;i++){

                        dataPoints[i] = new DataPoint(dates.get(i),usageCount[i]);
                    }

                    Log.i("testing", drug.getDrugName() + " has " + dataPoints.length + " datapoints");
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    series.setColor(color);
                    series.setTitle(drug.getDrugName());
                    usageGraph.addSeries(series);

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_general_usage_graph, menu);
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
